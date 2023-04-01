/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.any.*;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.bool.Boolean;
import io.openapiparser.validator.conditional.DependentSchemas;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.number.draft4.Maximum4;
import io.openapiparser.validator.number.draft4.Minimum4;
import io.openapiparser.validator.object.*;
import io.openapiparser.validator.object.Properties;
import io.openapiparser.validator.steps.*;
import io.openapiparser.validator.string.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

/**
 * the validator.
 */
public class Validator {
    private final ValidatorSettings settings;

    private static class RefResult {
        ValidationStep step;
        boolean stop;

        public RefResult(ValidationStep step, boolean stop) {
            this.step = step;
            this.stop = stop;
        }

        public ValidationStep getStep() {
            return step;
        }

        public boolean shouldStop() {
            return stop;
        }
    }

    public Validator () {
        settings = new ValidatorSettings ();
    }

    public Validator (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate(JsonSchema schema, JsonInstance instance) {
        return validate (schema, instance, null);
    }

    public ValidationStep validate(JsonSchema schema, JsonInstance instance, @Nullable DynamicScope parentScope) {
        DynamicScope dynamicScope = calcDynamicScope (schema, parentScope);

        SchemaStep step = new SchemaStep (schema, instance);

        RefResult ref = validateRef (schema, instance, dynamicScope);
        step.add(ref.getStep());
        if (ref.shouldStop()) {
            return step;
        }

        step.add (validateDynamicRef (schema, instance, dynamicScope));
        step.add (validateIf (schema, instance, dynamicScope));
        step.add (validateAllOf (schema, instance, dynamicScope));
        step.add (validateAnyOf (schema, instance, dynamicScope));
        step.add (validateOneOf (schema, instance, dynamicScope));
        step.add (validateNot (schema, instance, dynamicScope));
        step.add (validateEnum (schema, instance));
        step.add (validateConst (schema, instance));
        step.add (validateType (schema, instance));
        step.add (validateBoolean (schema, instance));
        step.add (validateNumber (schema, instance));
        step.add (validateString (schema, instance));
        step.add (validateObject (schema, instance, step, dynamicScope));
        step.add (validateArray (schema, instance, step, dynamicScope));
        return step;
    }

    // Draft 2019-09: todo
    // Draft 7: todo
    // Draft 6: todo
    // Draft 4: todo
    private RefResult validateRef (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        if (!schema.isRef ()) {
            return new RefResult(new NullStep(Keywords.REF), false);
        }

        JsonSchema refSchema = schema.getRefSchema ();
        SchemaRefStep step = new SchemaRefStep (refSchema);
        step.add (validate (refSchema, instance, dynamicScope));

        return new RefResult(step, !refAllowsSibling(step, schema));
    }

    // Draft 2020-12: todo
    // Draft 2019-09: todo
    private ValidationStep validateDynamicRef (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        if (!schema.isDynamicRef ())
            return new NullStep(Keywords.DYNAMIC_REF);

        URI dynamicRef = schema.getDynamicRef ();
        if (dynamicRef == null) {
            return new NullStep(Keywords.DYNAMIC_REF);
        }

        JsonSchema refSchema = schema.getRefSchema (dynamicScope.findScope (dynamicRef));
        SchemaRefStep step = new SchemaRefStep (refSchema);
        step.add (validate (refSchema, instance, dynamicScope));
        return step;
    }


    // Draft 2019-09: https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.2.2.1
    // Draft 7: https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.6
    private ValidationStep validateIf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        JsonSchema jsIf = schema.getIf ();
        JsonSchema jsThen = schema.getThen ();
        JsonSchema jsElse = schema.getElse ();

        if (jsIf == null || (jsThen == null && jsElse == null)) {
            return new NullStep ("if");
        }

        IfStep step = new IfStep (schema, instance);

        ValidationStep vsIf = validate (jsIf, instance, dynamicScope);
        step.setIf (vsIf);

        if (vsIf.isValid ()) {
            if (jsThen != null) {
                step.setThen (validate (jsThen, instance, dynamicScope));
            }
        } else {
            if (jsElse != null) {
                step.setElse (validate (jsElse, instance, dynamicScope));
            }
        }

        return step;
    }

    // Draft 2019-09: todo
    // Draft 7: todo
    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.26
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.3
    private ValidationStep validateAllOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        Collection<JsonSchema> allOf = schema.getAllOf ();
        if (allOf.isEmpty ())
            return new NullStep ("allOf");

        AllOfStep step = new AllOfStep (schema, instance);

        int allOfValidCount = 0;
        for (JsonSchema allOfSchema : allOf) {
            ValidationStep aos = validate (allOfSchema, instance, dynamicScope);
            step.add (aos);

            if (aos.isValid ())
                allOfValidCount++;
        }

        if (allOf.size () > 0 && allOfValidCount != allOf.size ()) {
            step.setInvalid ();
        }

        return step;
    }

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.27
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.4
    private ValidationStep validateAnyOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        Collection<JsonSchema> anyOf = schema.getAnyOf ();
        if (anyOf.isEmpty ())
            return new NullStep ("anyOf");

        AnyOfStep step = new AnyOfStep (schema, instance);

        int anyOfValidCount = 0;
        for (JsonSchema anyOfSchema : anyOf) {
            ValidationStep aos = validate (anyOfSchema, instance, dynamicScope);
            step.add (aos);

            if (aos.isValid ()) {
                anyOfValidCount++;
                // should collect annotations of all valid schemas
                //break;
            }
        }

        if (anyOf.size () > 0 && anyOfValidCount == 0) {
            step.setInvalid ();
        }

        return step;
    }

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.28
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.5
    private ValidationStep validateOneOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        Collection<JsonSchema> oneOf = schema.getOneOf ();
        if (oneOf.isEmpty ())
            return new NullStep ("oneOf");

        OneOfStep step = new OneOfStep (schema, instance);

        int oneOfValidCount = 0;
        for (JsonSchema oneOfSchema : oneOf) {
            ValidationStep oos = validate (oneOfSchema, instance, dynamicScope);
            step.add (oos);

            if (oos.isValid ()) {
                oneOfValidCount++;
            }
        }

        if (oneOf.size () > 0 && oneOfValidCount != 1) {
            step.setInvalid ();
        }

        return step;
    }

    // Draft: 2019-09: todo
    // Draft: 7: todo
    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.29
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.6
    private ValidationStep validateNot (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope) {
        JsonSchema not = schema.getNot ();
        if (not == null)
            return new NullStep ("not");

        ValidationStep step = validate (not, instance, dynamicScope);
        return new NotStep (schema, instance, step);
    }

    // Draft: 2019-09: todo
    // Draft 7: https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.1.2
    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.23
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.1
    private ValidationStep validateEnum (JsonSchema schema, JsonInstance instance) {
        Collection<JsonInstance> enums = schema.getEnum ();
        if (enums.isEmpty ())
            return new NullStep ("enum");

        EnumStep step = new EnumStep (schema, instance);

        boolean valid = false;
        for (JsonInstance value : enums) {
            if (instance.isEqual (value)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    // Draft: 2019-09: todo
    // Draft 7: https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.1.3
    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.24
    private ValidationStep validateConst (JsonSchema schema, JsonInstance instance) {
        JsonInstance constValue = schema.getConst ();
        if (constValue == null)
            return new NullStep ("const");

        ConstStep step = new ConstStep (schema, instance);

        if (!instance.isEqual (constValue)) {
            step.setInvalid ();
        }

        return step;
    }

    private ValidationStep validateType (JsonSchema schema, JsonInstance instance) {
        return new Type ().validate (schema, instance);
    }

    private ValidationStep validateBoolean (JsonSchema schema, JsonInstance instance) {
        if (! isBooleanSchema (schema)) {
            return new NullStep ("boolean");
        }

        return new Boolean ().validate (schema, instance);
    }

    private ValidationStep validateArray (JsonSchema schema, JsonInstance instance, Annotations annotations, DynamicScope dynamicScope) {
        if (!instance.isArray ()) {
            return new NullStep ("array");
        }

        CompositeStep step = new FlatStep ();
        step.add (new MaxItems ().validate (schema, instance));
        step.add (new MinItems ().validate (schema, instance));
        step.add (new UniqueItems ().validate (schema, instance));
        step.add (new Contains (this).validate (schema, instance, dynamicScope));
        step.add (new Items (this).validate (schema, instance, annotations, dynamicScope));
        return step;
    }

    private ValidationStep validateObject (JsonSchema schema, JsonInstance instance, Annotations annotations, DynamicScope dynamicScope) {
        if (!instance.isObject ())
            return new NullStep ("no object");

        CompositeStep step = new FlatStep ();
        AnnotationsComposite current = step.mergeAnnotations (annotations);

        step.add (new MaxProperties ().validate (schema, instance));
        step.add (new MinProperties ().validate (schema, instance));
        step.add (new Required ().validate (schema, instance));
        step.add (new DependentRequired ().validate (schema, instance));
        step.add (new DependentSchemas (this).validate (schema, instance, dynamicScope));
        step.add (new Properties (this).validate(schema, instance, current, dynamicScope));
        step.add (new Dependencies (this).validate (schema, instance, dynamicScope));
        step.add (new PropertyNames (this).validate (schema, instance, dynamicScope));
        return step;
    }

    private ValidationStep validateNumber (JsonSchema schema, JsonInstance instance) {
        if (!instance.isNumber ()) {
            return new NullStep ("no number");
        }

        CompositeStep step = new FlatStep ();

        if (isDraft4 (schema)) {
            step.add (new Minimum4 ().validate (schema, instance));
            step.add (new Maximum4 ().validate (schema, instance));
            step.add (new MultipleOf ().validate (schema, instance));
        } else {
            if (!shouldValidate (schema))
                return new NullStep ("no validation");

            step.add (new Minimum ().validate (schema, instance));
            step.add (new Maximum ().validate (schema, instance));
            step.add (new ExclusiveMinimum ().validate (schema, instance));
            step.add (new ExclusiveMaximum ().validate (schema, instance));
            step.add (new MultipleOf ().validate (schema, instance));
        }

        return step;
    }

    private ValidationStep validateString (JsonSchema schema, JsonInstance instance) {
        if (!instance.isString ()) {
            return new NullStep ("no string");
        }

        CompositeStep step = new FlatStep ();
        step.add (new MaxLength ().validate (schema, instance));
        step.add (new MinLength ().validate (schema, instance));
        step.add (new Pattern ().validate (schema, instance));
        step.add (new Uuid (settings).validate (schema, instance));
        step.add (new DateTime (settings).validate (schema, instance));
        step.add (new Email (settings).validate (schema, instance));
        step.add (new Hostname (settings).validate (schema, instance));
        step.add (new IpV4 (settings).validate (schema, instance));
        step.add (new IpV6 (settings).validate (schema, instance));
        step.add (new Uri (settings).validate (schema, instance));
        step.add (new UriReference (settings).validate (schema, instance));
        step.add (new Regex ().validate (schema, instance));
        return step;
    }

    // Draft 2020-12: todo
    // Draft 2019-09: todo
    private DynamicScope calcDynamicScope (JsonSchema schema, @Nullable DynamicScope parentScope) {
        if (parentScope == null) {
            return new DynamicScope (schema);
        }

        return parentScope.add (schema);
        // .resolve ("#" + dynamicAnchor);
    }

    private boolean shouldValidate (JsonSchema schema) {
        return schema.getContext ().getVocabularies ().requiresValidation ();
    }

    private boolean refAllowsSibling (ValidationStep step, JsonSchema schema) {
        if (step instanceof SchemaRefStep) {
            return schema.getContext().refAllowsSiblings();
        }

        return true;
    }

    private boolean isBooleanSchema (JsonSchema schema) {
        return schema instanceof JsonSchemaBoolean;
    }

    private boolean isDraft4(JsonSchema schema) {
        return SchemaVersion.Draft4.equals (schema.getContext ().getVersion ());
    }
}
