/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.DynamicScope;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.JsonSchemaBoolean;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.Validator;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;

/**
 * validates additionalProperties, properties and patternProperties. Since Draft 4.
 */
public class Properties {
    private final Validator validator;

    public Properties (Validator validator) {
        this.validator = validator;
    }

    public void validate (JsonSchema schema, JsonInstance instance, DynamicScope dynamicScope, ValidationStep parentStep) {
        PropertiesStep propertiesStep = new PropertiesStep (schema, instance, "properties");
        PropertiesStep patternPropertiesStep = new PropertiesStep (schema, instance,"patternProperties");
        PropertiesStep additionalPropertiesStep = new PropertiesStep (schema, instance,"additionalProperties");
        PropertiesStep unevaluatedPropertiesStep = new PropertiesStep (schema, instance,"unevaluatedProperties");

        Map<String, JsonSchema> schemaProperties = schema.getProperties ();
        Map<String, JsonSchema> patternProperties = schema.getPatternProperties ();
        JsonSchema additionalProperties = schema.getAdditionalProperties ();
        JsonSchema unevaluatedProperties = schema.getUnevaluatedProperties ();

        Map<String, @Nullable Object> instanceObject = requiresNonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());

        Set<String> propertiesAnnotations = new HashSet<> ();
        Collection<String> patternPropertiesAnnotation = new ArrayList<> ();
        Collection<String> additionalPropertiesAnnotation = new ArrayList<> ();
        Collection<String> unevaluatedPropertiesAnnotation = new ArrayList<> ();

        instanceObject.keySet ().forEach (propName -> {
            boolean checkAdditionalProperty = true;

            JsonSchema propSchema = schemaProperties.get (propName);
            JsonInstance propInstance = instance.getValue (propName);

            if (propSchema != null) {
                PropertyStep propStep = new PropertyStep (propSchema, propInstance, propName);
                propertiesStep.add (propStep);

                validator.validate (propSchema, propInstance, dynamicScope, propStep);
                if (propStep.isValid ()) {
                    propertiesAnnotations.add (propName);
                }

                checkAdditionalProperty = false;
            }

            for (String pattern : patternProperties.keySet ()) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(propName);
                if (m.find()) {
                    JsonSchema patternSchema = patternProperties.get (pattern);
                    JsonInstance value = instance.getValue (propName);

                    PropertyStep propStep = new PropertyStep (patternSchema, value, propName);
                    patternPropertiesStep.add (propStep);

                    validator.validate (patternSchema, value, dynamicScope, propStep);
                    if (propStep.isValid ()) {
                        patternPropertiesAnnotation.add (propName);
                    }

                    checkAdditionalProperty = false;
                } // else no pattern match
            }

            if (checkAdditionalProperty && additionalProperties != null) {
                JsonInstance value = instance.getValue (propName);

                PropertyStep propStep = new PropertyStep (additionalProperties, value, propName);
                additionalPropertiesStep.add (propStep);

                /*ValidationStep propStep =*/ validator.validate (additionalProperties, value, dynamicScope, propStep);
                if (propStep.isValid ()) {
                    additionalPropertiesAnnotation.add (propName);
                }
            }
        });

        // no additional properties
        if (additionalProperties instanceof JsonSchemaBoolean && additionalProperties.isFalse ()) {
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
            // necessary ? reported by additionProperties
            /*
            if (!instanceProperties.isEmpty ()) {
                instanceProperties.forEach (propName -> {
                    ValidationStep invalidStep = new PropertyInvalidStep (schema, instance, propName);
                    //ValidationStep namedStep = new PropertyStep (schema, instance, propName, invalidStep);
                    additionalPropertiesStep.add (invalidStep);
                });
            }*/
        }

        Set<String> tmpAnnotations = collectAnnotations (parentStep, "properties");
        tmpAnnotations.addAll (propertiesAnnotations);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (parentStep, "patternProperties");
        tmpAnnotations.addAll (patternPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (parentStep, "additionalProperties");
        tmpAnnotations.addAll (additionalPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (parentStep, "unevaluatedProperties");
        tmpAnnotations.addAll (unevaluatedPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        if (unevaluatedProperties != null) {
            instanceProperties.forEach (propName -> {
                JsonInstance propInstance = instance.getValue (propName);

                PropertyStep propStep = new PropertyStep (unevaluatedProperties, propInstance, propName);
                unevaluatedPropertiesStep.add (propStep);

                validator.validate (unevaluatedProperties, propInstance, dynamicScope, propStep);
                if (propStep.isValid ()) {
                    unevaluatedPropertiesAnnotation.add (propName);
                }
            });
        }

        propertiesStep.addAnnotation(propertiesAnnotations);
        patternPropertiesStep.addAnnotation(patternPropertiesAnnotation);
        additionalPropertiesStep.addAnnotation(additionalPropertiesAnnotation);
        unevaluatedPropertiesStep.addAnnotation(unevaluatedPropertiesAnnotation);

        if (!schemaProperties.isEmpty ())
            parentStep.add (propertiesStep);

        if (!patternProperties.isEmpty ())
            parentStep.add (patternPropertiesStep);

        if (additionalProperties != null)
            parentStep.add (additionalPropertiesStep);

        if (unevaluatedPropertiesStep.isNotEmpty ())
            parentStep.add (unevaluatedPropertiesStep);
    }

    private Set<String> collectAnnotations (ValidationStep step, String property) {
        return step.getAnnotations (property)
            .stream ()
            .map (Annotation::asStrings)
            .flatMap (Collection::stream)
            .collect (Collectors.toSet ());
    }
}
