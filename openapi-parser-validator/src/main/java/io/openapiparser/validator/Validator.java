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
        boolean stop;

        public RefResult(boolean stop) {
            this.stop = stop;
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
        SchemaStep step = new SchemaStep (schema, instance);
        validate (schema, instance, null, step);
        return step;
    }

    public void validate(JsonSchema schema, JsonInstance instance, @Nullable DynamicScope parentScope, ValidationStep parentStep) {
        DynamicScope dynamicScope = calcDynamicScope (schema, parentScope);

        RefResult ref = validateRef (schema, instance, dynamicScope, parentStep);
        if (ref.shouldStop()) {
            return;
        }

        validateDynamicRef (schema, instance, dynamicScope, parentStep);
        validateIf (schema, instance, dynamicScope, parentStep);
        validateAllOf (schema, instance, dynamicScope, parentStep);
        validateAnyOf (schema, instance, dynamicScope, parentStep);
        validateOneOf (schema, instance, dynamicScope, parentStep);
        validateNot (schema, instance, dynamicScope, parentStep);
        validateEnum (schema, instance, parentStep);
        validateConst (schema, instance, parentStep);
        validateType (schema, instance, parentStep);
        validateBoolean (schema, instance, parentStep);
        validateNumber (schema, instance, parentStep);
        validateString (schema, instance, parentStep);
        validateObject (schema, instance, dynamicScope, parentStep);
        validateArray (schema, instance, dynamicScope, parentStep);
    }

    private RefResult validateRef (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        if (!schema.isRef ()) {
            return new RefResult(false);
        }

        JsonSchema refSchema = schema.getRefSchema ();
        RefStep step = new RefStep (schema, instance);
        validate (refSchema, instance, dynamicScope, step);
        parentStep.add (step);

        return new RefResult(!refAllowsSibling(step, schema));
    }

    private void validateDynamicRef (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        if (!schema.isDynamicRef ())
            return;

        URI dynamicRef = schema.getDynamicRef ();
        if (dynamicRef == null) {
            return; // todo avoid nullable check, annotate isDynamicRef()
        }

        JsonSchema refSchema = schema.getRefSchema (dynamicScope.findScope (dynamicRef));
        DynamicRefStep step = new DynamicRefStep (schema, instance);
        validate (refSchema, instance, dynamicScope, step);
        parentStep.add (step);
    }

    private void validateIf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        JsonSchema jsIf = schema.getIf ();
        JsonSchema jsThen = schema.getThen ();
        JsonSchema jsElse = schema.getElse ();

        if (jsIf == null)
            return;

        IfStep ifStep = new IfStep (jsIf, instance);
        validate (jsIf, instance, dynamicScope, ifStep);
        parentStep.add (ifStep);

        if (ifStep.isValid ()) {
            if (jsThen != null) {
                SchemaStep thenStep = new SchemaStep (jsThen, instance);
                validate (jsThen, instance, dynamicScope, thenStep);
                parentStep.add (thenStep);
            }
        } else {
            if (jsElse != null) {
                SchemaStep elseStep = new SchemaStep (jsElse, instance);
                validate (jsElse, instance, dynamicScope, elseStep);
                parentStep.add (elseStep);
            }
        }
    }

    private void validateAllOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Collection<JsonSchema> allOf = schema.getAllOf ();
        if (allOf.isEmpty ())
            return;

        AllOfStep step = new AllOfStep (schema, instance);

        for (JsonSchema allOfSchema : allOf) {
            SchemaStep allStep = new SchemaStep (allOfSchema, instance);
            validate (allOfSchema, instance, dynamicScope, allStep);
            step.add (allStep);
        }

        if (allOf.size () > 0 && step.countValid () != allOf.size ()) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private void validateAnyOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Collection<JsonSchema> anyOf = schema.getAnyOf ();
        if (anyOf.isEmpty ())
            return;

        AnyOfStep step = new AnyOfStep (schema, instance);

        for (JsonSchema anyOfSchema : anyOf) {
            // should collect annotations of all valid schemas, may shortcut on flag output
            SchemaStep anyStep = new SchemaStep (anyOfSchema, instance);
            validate (anyOfSchema, instance, dynamicScope, anyStep);
            step.add (anyStep);
        }

        if (anyOf.size () > 0 && step.countValid () == 0) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private void validateOneOf (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        Collection<JsonSchema> oneOf = schema.getOneOf ();
        if (oneOf.isEmpty ())
            return;

        OneOfStep step = new OneOfStep (schema, instance);

        for (JsonSchema oneOfSchema : oneOf) {
            SchemaStep oneStep = new SchemaStep (oneOfSchema, instance);
            validate (oneOfSchema, instance, dynamicScope, oneStep);
            step.add (oneStep);
        }

        if (oneOf.size () > 0 && step.countValid () != 1) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private void validateNot (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        JsonSchema not = schema.getNot ();
        if (not == null)
            return;

        NotStep step = new NotStep (schema, instance);

        SchemaStep notStep = new SchemaStep (not, instance);
        validate (not, instance, dynamicScope, notStep);
        step.add (notStep);

        if (notStep.isValid ()) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private void validateEnum (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Collection<JsonInstance> enums = schema.getEnum ();
        if (enums.isEmpty ())
            return;

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

        parentStep.add (step);
    }

    private void validateConst (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        JsonInstance constValue = schema.getConst ();
        if (constValue == null)
            return;

        ConstStep step = new ConstStep (schema, instance);

        if (!instance.isEqual (constValue)) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private void validateType (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        new Type ().validate (schema, instance, parentStep);
    }

    private void validateBoolean (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        if (! isBooleanSchema (schema)) {
            return;
        }

         new Boolean ().validate (schema, instance, parentStep);
    }

    private void validateArray (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        if (!instance.isArray ()) {
            return;
        }

        new MaxItems ().validate (schema, instance, parentStep);
        new MinItems ().validate (schema, instance, parentStep);
        new UniqueItems ().validate (schema, instance, parentStep);
        new Contains (this).validate (schema, instance, dynamicScope, parentStep);

        if (isBeforeDraft202012 (schema)) {
            new Items (this).validate (schema, instance, dynamicScope, parentStep);
        } else {
            new ItemsX (this).validate (schema, instance, dynamicScope, parentStep);
        }
    }

    private void validateObject (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        if (!instance.isObject ())
            return;

        new MaxProperties ().validate (schema, instance, parentStep);
        new MinProperties ().validate (schema, instance, parentStep);
        new Required ().validate (schema, instance, parentStep);
        new DependentRequired ().validate (schema, instance, parentStep);
        new DependentSchemas (this).validate (schema, instance, dynamicScope, parentStep);
        new Properties (this).validate (schema, instance, dynamicScope, parentStep);
        new Dependencies (this).validate (schema, instance, dynamicScope, parentStep);
        new PropertyNames (this).validate (schema, instance, dynamicScope, parentStep);
    }

    private void validateNumber (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        if (!instance.isNumber ()) {
            return;
        }

        if (isDraft4 (schema)) {
            new Minimum4 ().validate (schema, instance, parentStep);
            new Maximum4 ().validate (schema, instance, parentStep);
            new MultipleOf ().validate (schema, instance, parentStep);
        } else {
            if (!shouldValidate (schema))
                return;

            new Minimum ().validate (schema, instance, parentStep);
            new Maximum ().validate (schema, instance, parentStep);
            new ExclusiveMinimum ().validate (schema, instance, parentStep);
            new ExclusiveMaximum ().validate (schema, instance, parentStep);
            new MultipleOf ().validate (schema, instance, parentStep);
        }
    }

    private void validateString (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        if (!instance.isString ()) {
            return;
        }

        new MaxLength ().validate (schema, instance, parentStep);
        new MinLength ().validate (schema, instance, parentStep);
        new Pattern ().validate (schema, instance, parentStep);
        new Uuid (settings).validate (schema, instance, parentStep);
        new DateTime (settings).validate (schema, instance, parentStep);
        new Email (settings).validate (schema, instance, parentStep);
        new Hostname (settings).validate (schema, instance, parentStep);
        new IpV4 (settings).validate (schema, instance, parentStep);
        new IpV6 (settings).validate (schema, instance, parentStep);
        new Uri (settings).validate (schema, instance, parentStep);
        new UriReference (settings).validate (schema, instance, parentStep);
        new Regex ().validate (schema, instance, parentStep);
    }

    private DynamicScope calcDynamicScope (JsonSchema schema, @Nullable DynamicScope parentScope) {
        if (parentScope == null) {
            return new DynamicScope (schema);
        }

        return parentScope.add (schema);
    }

    private boolean shouldValidate (JsonSchema schema) {
        return schema.getContext ().getVocabularies ().requiresValidation ();
    }

    private boolean refAllowsSibling (ValidationStep step, JsonSchema schema) {
        if (step instanceof RefStep) {
            return schema.getContext().refAllowsSiblings();
        }

        return true;
    }

    private boolean isBooleanSchema (JsonSchema schema) {
        return schema instanceof JsonSchemaBoolean;
    }

    private boolean isBeforeDraft202012 (JsonSchema schema) {
        return schema.getContext ().getVersion ().isBefore202012 ();
    }

    private boolean isDraft4 (JsonSchema schema) {
        return SchemaVersion.Draft4.equals (schema.getContext ().getVersion ());
    }
}
