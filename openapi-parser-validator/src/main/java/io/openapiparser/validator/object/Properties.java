/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.Validator;
import io.openapiparser.validator.steps.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * validates additionalProperties, properties and patternProperties.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4">
 *     Draft 4: additionalProperties, properties and patternProperties
 * </a>
 */
public class Properties {
    private final Validator validator;

    public Properties (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        PropertiesStep step = new PropertiesStep (schema, instance);

        Map<String, JsonSchema> schemaProperties = schema.getProperties ();
        Map<String, JsonSchema> patternProperties = schema.getPatternProperties ();
        JsonSchema additionalProperties = schema.getAdditionalProperties ();

        if (additionalProperties instanceof JsonSchemaBoolean && additionalProperties.isFalse ()) {
            Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());

            instanceProperties.removeAll (schemaProperties.keySet ());

            Iterator<String> it = instanceProperties.iterator();
            while (it.hasNext()) {
                String property = it.next ();

                for (String pattern : patternProperties.keySet ()) {
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(property);
                    if (m.find()) {
                        it.remove ();
                    }
                }
            }

            if (!instanceProperties.isEmpty ()) {
                instanceProperties.forEach (k -> {
                    PropertyStep pStep = new PropertyStep (schema, instance, k);
                    pStep.setInvalid ();
                    step.add (pStep);
                });
            }
        }

        // todo instance.forEach() on property names
        instance.asObject ().forEach ((propName, unused) -> {
            boolean checkAdditionalProperty = true;

            JsonSchema propSchema = schemaProperties.get (propName);
            JsonInstance propInstance = instance.getValue (propName);

            if (propSchema != null) {
                step.add (validator.validate (propSchema, propInstance));
                checkAdditionalProperty = false;
            }

            for (String pattern : patternProperties.keySet ()) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(propName);
                if (m.find()) {
                    JsonSchema patternSchema = patternProperties.get (pattern);
                    JsonInstance value = instance.getValue (propName);
                    step.add (validator.validate (patternSchema, value));
                    checkAdditionalProperty = false;
                }
            }

            if (checkAdditionalProperty && additionalProperties != null) {
                JsonInstance value = instance.getValue (propName);
                step.add (validator.validate (additionalProperties, value));
            }
        });

        return step;
    }
}
