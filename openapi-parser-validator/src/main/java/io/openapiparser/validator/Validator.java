/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.any.*;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.bool.Boolean;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.number.draft4.Maximum4;
import io.openapiparser.validator.number.draft4.Minimum4;
import io.openapiparser.validator.object.*;
import io.openapiparser.validator.object.Properties;
import io.openapiparser.validator.steps.*;
import io.openapiparser.validator.string.*;

import java.net.URI;
import java.util.*;

/**
 * the validator.
 */
public class Validator {
    private final ValidatorSettings settings;
    private final Set<URI> visited = new HashSet<> ();

    public Validator () {
        settings = new ValidatorSettings ();
    }

    public Validator (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate(JsonSchema schema, JsonInstance instance) {
        ValidateStep step = new ValidateStep (schema, instance);

        while (schema.isRef()) {
            schema = schema.getRefSchema ();
            step.add (new SchemaRefStep (schema));
        }

        while (instance.isRef()) {
            URI refKey = instance.getRefKey ();
            if (visited.contains (refKey))
                break;

            visited.add (refKey);
            instance = instance.getRefInstance ();
            step.add (new InstanceRefStep (instance));
        }

        step.add (validateIf (schema, instance));
        step.add (validateAllOf (schema, instance));
        step.add (validateAnyOf (schema, instance));
        step.add (validateOneOf (schema, instance));
        step.add (validateNot (schema, instance));
        step.add (validateEnum (schema, instance));
        step.add (validateConst (schema, instance));
        step.add (validateType (schema, instance));
        step.add (validateBoolean (schema, instance));
        step.add (validateArray (schema, instance));
        step.add (validateObject (schema, instance));
        step.add (validateNumber (schema, instance));
        step.add (validateString (schema, instance));

        return step;
    }

    // Draft 7: https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.6
    private ValidationStep validateIf (JsonSchema schema, JsonInstance instance) {
        JsonSchema jsIf = schema.getIf ();
        JsonSchema jsThen = schema.getThen ();
        JsonSchema jsElse = schema.getElse ();

        if (jsIf == null || (jsThen == null && jsElse == null)) {
            return new NullStep ();
        }

        IfStep step = new IfStep (schema, instance);

        ValidationStep vsIf = validate (jsIf, instance);
        step.setIf (vsIf);

        if (vsIf.isValid ()) {
            if (jsThen != null) {
                step.setThen (validate (jsThen, instance));
            }
        } else {
            if (jsElse != null) {
                step.setElse (validate (jsElse, instance));
            }
        }

        return step;
    }

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.26
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.3
    private ValidationStep validateAllOf (JsonSchema schema, JsonInstance instance) {
        Collection<JsonSchema> allOf = schema.getAllOf ();
        if (allOf.isEmpty ())
            return new NullStep ();

        AllOfStep step = new AllOfStep (schema, instance);

        int allOfValidCount = 0;
        for (JsonSchema allOfSchema : allOf) {
            ValidationStep aos = validate (allOfSchema, instance);
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
    private ValidationStep validateAnyOf (JsonSchema schema, JsonInstance instance) {
        Collection<JsonSchema> anyOf = schema.getAnyOf ();
        if (anyOf.isEmpty ())
            return new NullStep ();

        AnyOfStep step = new AnyOfStep (schema, instance);

        int anyOfValidCount = 0;
        for (JsonSchema anyOfSchema : anyOf) {
            ValidationStep aos = validate (anyOfSchema, instance);
            step.add (aos);

            if (aos.isValid ()) {
                anyOfValidCount++;
                break;
            }
        }

        if (anyOf.size () > 0 && anyOfValidCount == 0) {
            step.setInvalid ();
        }

        return step;
    }

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.28
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.5
    private ValidationStep validateOneOf (JsonSchema schema, JsonInstance instance) {
        Collection<JsonSchema> oneOf = schema.getOneOf ();
        if (oneOf.isEmpty ())
            return new NullStep ();

        OneOfStep step = new OneOfStep (schema, instance);

        int oneOfValidCount = 0;
        for (JsonSchema oneOfSchema : oneOf) {
            ValidationStep oos = validate (oneOfSchema, instance);
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

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.29
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.6
    private ValidationStep validateNot (JsonSchema schema, JsonInstance instance) {
        JsonSchema not = schema.getNot ();
        if (not == null)
            return new NullStep ();

        ValidationStep step = validate (not, instance);
        return new NotStep (schema, instance, step);
    }

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.23
    // Draft 4: https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.1
    private ValidationStep validateEnum (JsonSchema schema, JsonInstance instance) {
        Collection<JsonInstance> enums = schema.getEnum ();
        if (enums.isEmpty ())
            return new NullStep ();

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

    // Draft 6: https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.24
    private ValidationStep validateConst (JsonSchema schema, JsonInstance instance) {
        JsonInstance constValue = schema.getConst ();
        if (constValue == null)
            return new NullStep ();

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
            return new NullStep ();
        }

        return new Boolean ().validate (schema, instance);
    }

    private ValidationStep validateArray (JsonSchema schema, JsonInstance instance) {
        if (!instance.isArray ()) {
            return new NullStep ();
        }

        CompositeStep step = new FlatStep ();
        step.add (new MaxItems ().validate (schema, instance));
        step.add (new MinItems ().validate (schema, instance));
        step.add (new UniqueItems ().validate (schema, instance));
        step.add (new Contains (this).validate (schema, instance));
        step.add (new Items (this).validate (schema, instance));
        return step;
    }

    private ValidationStep validateObject (JsonSchema schema, JsonInstance instance) {
        if (!instance.isObject ()) {
            return new NullStep ();
        }

        CompositeStep step = new FlatStep ();
        step.add (new MaxProperties ().validate (schema, instance));
        step.add (new MinProperties ().validate (schema, instance));
        step.add (new Required ().validate (schema, instance));
        step.add (new Properties (this).validate(schema, instance));
        step.add (new Dependencies (this).validate (schema, instance));
        step.add (new PropertyNames (this).validate (schema, instance));
        return step;
    }

    private ValidationStep validateNumber (JsonSchema schema, JsonInstance instance) {
        if (!instance.isNumber ()) {
            return new NullStep ();
        }

        CompositeStep step = new FlatStep ();
        step.add (new MultipleOf ().validate (schema, instance));

        if (isDraft4 (schema)) {
            step.add (new Minimum4 ().validate (schema, instance));
            step.add (new Maximum4 ().validate (schema, instance));
        } else {
            step.add (new Minimum ().validate (schema, instance));
            step.add (new Maximum ().validate (schema, instance));
            step.add (new ExclusiveMinimum ().validate (schema, instance));
            step.add (new ExclusiveMaximum ().validate (schema, instance));
        }

        return step;
    }

    private ValidationStep validateString (JsonSchema schema, JsonInstance instance) {
        if (!instance.isString ()) {
            return new NullStep ();
        }

        CompositeStep step = new FlatStep ();
        step.add (new MaxLength ().validate (schema, instance));
        step.add (new MinLength ().validate (schema, instance));
        step.add (new Pattern ().validate (schema, instance));
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

    private boolean isBooleanSchema (JsonSchema schema) {
        return schema instanceof JsonSchemaBoolean;
    }

    private boolean isDraft4(JsonSchema schema) {
        return SchemaVersion.Draft4.equals (schema.getContext ().getVersion ());
    }
}
