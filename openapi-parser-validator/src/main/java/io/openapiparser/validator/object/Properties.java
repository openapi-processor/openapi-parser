/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.*;
import io.openapiparser.validator.*;
import io.openapiparser.validator.steps.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates additionalProperties, properties and patternProperties.
 *
 * <p>See specification:
 *
 * <p>Draft 2019-09:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.3.2.1">
 *     properties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.3.2.2">
 *     patternProperties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.3.2.3">
 *     additionalProperties</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-02#section-9.3.2.4">
 *     unevaluatedProperties</a>
 *
 * <br>Draft 7:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.5.4">
 *     properties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.5.5">
 *     patternProperties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-6.5.6">
 *     additionalProperties</a>
 *
 * <br>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.18">
 *     properties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.19">
 *     patternProperties</a>,
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.20">
 *     additionalProperties</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4">
 *     additionalProperties, properties and patternProperties
 * </a>
 */
public class Properties {
    private final Validator validator;

    public Properties (Validator validator) {
        this.validator = validator;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance, Annotations annotations, DynamicScope dynamicScope) {
        CompositeStep step = new FlatStep ();

        PropertiesStep propertiesStep = new PropertiesStep ("properties");
        PropertiesStep patternPropertiesStep = new PropertiesStep ("patternProperties");
        PropertiesStep additionalPropertiesStep = new PropertiesStep ("additionalProperties");
        PropertiesStep unevaluatedPropertiesStep = new PropertiesStep ("unevaluatedProperties");

        Map<String, JsonSchema> schemaProperties = schema.getProperties ();
        Map<String, JsonSchema> patternProperties = schema.getPatternProperties ();
        JsonSchema additionalProperties = schema.getAdditionalProperties ();
        JsonSchema unevaluatedProperties = schema.getUnevaluatedProperties ();

        Map<String, Object> instanceObject = nonNull(instance.asObject ());
        Set<String> instanceProperties = new HashSet<>(instanceObject.keySet ());

        // no additional properties // todo move down
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

            if (!instanceProperties.isEmpty ()) {
                instanceProperties.forEach (propName -> {
                    ValidationStep invalidStep = new PropertyInvalidStep (schema, instance, propName);
                    ValidationStep namedStep = new PropertyStep (propName, invalidStep);
                    additionalPropertiesStep.add (namedStep);
                });
            }
        }

        Set<String> propertiesAnnotations = new HashSet<> ();
        Collection<String> patternPropertiesAnnotation = new ArrayList<> ();
        Collection<String> additionalPropertiesAnnotation = new ArrayList<> ();
        Collection<String> unevaluatedPropertiesAnnotation = new ArrayList<> ();

        instanceObject.keySet ().forEach (propName -> {
            boolean checkAdditionalProperty = true;

            JsonSchema propSchema = schemaProperties.get (propName);
            JsonInstance propInstance = instance.getValue (propName);

            if (propSchema != null) {
                ValidationStep propStep = validator.validate (propSchema, propInstance, dynamicScope);
                if (propStep.isValid ()) {
                    propertiesAnnotations.add (propName);
                }

                propertiesStep.add (new PropertyStep (propName, propStep));
                checkAdditionalProperty = false;
            }

            for (String pattern : patternProperties.keySet ()) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(propName);
                if (m.find()) {
                    JsonSchema patternSchema = patternProperties.get (pattern);
                    JsonInstance value = instance.getValue (propName);

                    ValidationStep propStep = validator.validate (patternSchema, value, dynamicScope);
                    if (propStep.isValid ()) {
                        patternPropertiesAnnotation.add (propName);
                    }

                    patternPropertiesStep.add (new PropertyStep (propName, propStep));
                    checkAdditionalProperty = false;
                }
            }

            if (checkAdditionalProperty && additionalProperties != null) {
                JsonInstance value = instance.getValue (propName);

                ValidationStep propStep = validator.validate (additionalProperties, value, dynamicScope);
                if (propStep.isValid ()) {
                    additionalPropertiesAnnotation.add (propName);
                }

                additionalPropertiesStep.add (new PropertyStep (propName, propStep));
            }
        });

        Set<String> tmpAnnotations = collectAnnotations (annotations, "properties");
        tmpAnnotations.addAll (propertiesAnnotations);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (annotations, "patternProperties");
        tmpAnnotations.addAll (patternPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (annotations, "additionalProperties");
        tmpAnnotations.addAll (additionalPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        tmpAnnotations = collectAnnotations (annotations, "unevaluatedProperties");
        tmpAnnotations.addAll (unevaluatedPropertiesAnnotation);
        tmpAnnotations.forEach (instanceProperties::remove);

        if (unevaluatedProperties != null) {
            instanceProperties.forEach (propName -> {
                JsonInstance propInstance = instance.getValue (propName);

                ValidationStep propStep = validator.validate (unevaluatedProperties, propInstance, dynamicScope);
                if (propStep.isValid ()) {
                    unevaluatedPropertiesAnnotation.add (propName);
                }

                unevaluatedPropertiesStep.add (propStep);
            });
        }

        propertiesStep.addAnnotation(propertiesAnnotations);
        patternPropertiesStep.addAnnotation(patternPropertiesAnnotation);
        additionalPropertiesStep.addAnnotation(additionalPropertiesAnnotation);
        unevaluatedPropertiesStep.addAnnotation(unevaluatedPropertiesAnnotation);

        if (propertiesStep.isNotEmpty ())
            step.add (propertiesStep);

        if (patternPropertiesStep.isNotEmpty ())
            step.add (patternPropertiesStep);

        if (additionalPropertiesStep.isNotEmpty ())
            step.add (additionalPropertiesStep);

        if (unevaluatedPropertiesStep.isNotEmpty ())
            step.add (unevaluatedPropertiesStep);

        return step;
    }

    private Set<String> collectAnnotations (Annotations annotations, String property) {
        return annotations.getAnnotations (property)
            .stream ()
            .map (Annotation::asStrings)
            .flatMap (Collection::stream)
            .collect (Collectors.toSet ());
    }
}
