/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.any.*;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.object.*;
import io.openapiparser.validator.string.*;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the validator.
 */
public class Validator {
    private final ValidatorSettings settings;

    public Validator() {
        settings = new ValidatorSettings ();
    }

    public Validator(ValidatorSettings settings) {
        this.settings = settings;
    }

    public Collection<ValidationMessage> validate(JsonSchema schema, JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        while (schema.isRef()) {
            schema = schema.getRefSchema ();
        }

        while (instance.isRef()) {
            instance = instance.getRefInstance ();
        }

        // if
        // then
        // else

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.3
        for (JsonSchema sao : schema.getAllOf ()) {
            messages.addAll (validate (sao, instance));
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.4
        Collection<JsonSchema> anyOf = schema.getAnyOf ();
        int anyOfValidCount = 0;
        for (JsonSchema anyOfSchema : anyOf) {
            boolean valid = validate (anyOfSchema, instance).isEmpty ();
            if (valid) {
                anyOfValidCount++;
                break;
            }
        }

        if (anyOf.size () > 0 && anyOfValidCount == 0) {
            messages.add (new AnyOfError (instance.getPath ()));
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.5
        Collection<JsonSchema> oneOf = schema.getOneOf ();
        int oneOfValidCount = 0;
        for (JsonSchema oneOfSchema : oneOf) {
            boolean valid = validate (oneOfSchema, instance).isEmpty ();
            if (valid) {
                oneOfValidCount++;
            }
        }

        if (oneOf.size () > 0 && oneOfValidCount != 1) {
            messages.add (new OneOfError (instance.getPath ()));
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.6
        JsonSchema not = schema.getNot ();
        if (not != null) {
            boolean valid = validate (not, instance).isEmpty ();
            if (valid) {
                messages.add (new NotError (instance.getPath ()));
            }
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.1
        Collection<JsonInstance> enums = schema.getEnum ();
        if (!enums.isEmpty ()) {
            boolean valid = false;

            for (JsonInstance value : enums) {
                if (instance.isEqual (value)) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                messages.add (new EnumError (instance.getPath ()));
            }
        }

        messages.addAll (new Type ().validate (schema, instance));

        if (instance.isArray ()) {
            messages.addAll (validateArray (schema, instance));

        } else if (instance.isObject ()) {
            messages.addAll (validateObject (schema, instance));

        } else if (instance.getRawValue () instanceof Number) {
            messages.addAll (validateNumber (schema, instance));

        } else if (instance.getRawValue () instanceof String) {
            messages.addAll (validateString (schema, instance));
        }

        return messages;
    }

    /** https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4
     */
    private Collection<? extends ValidationMessage> validateObject (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        messages.addAll (new MaxProperties ().validate (schema, instance));
        messages.addAll (new MinProperties ().validate (schema, instance));
        messages.addAll (new Required ().validate (schema, instance));

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4.4

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
                JsonPointer pointer = instance.getPointer ();

                instanceProperties.forEach (k -> {
                    messages.add (new AdditionalPropertiesError (pointer.append (k).toString ()));
                });
            }
        }

        // todo instance.forEach() on property names
        instance.asObject ().forEach ((propName, unused) -> {
            boolean checkAdditionalProperty = true;

            JsonSchema propSchema = schemaProperties.get (propName);
            JsonInstance propInstance = instance.getValue (propName);

            if (propSchema != null) {
                messages.addAll (validate (propSchema, propInstance));
                checkAdditionalProperty = false;
            }

            for (String pattern : patternProperties.keySet ()) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(propName);
                if (m.find()) {
                    JsonSchema patternSchema = patternProperties.get (pattern);
                    JsonInstance value = instance.getValue (propName);
                    messages.addAll (validate (patternSchema, value));
                    checkAdditionalProperty = false;
                }
            }

            if (checkAdditionalProperty && additionalProperties != null) {
                JsonInstance value = instance.getValue (propName);
                messages.addAll (validate (additionalProperties, value));
            }
        });

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.5
        Map<String, JsonDependency> dependencies = schema.getDependencies ();
        instance.asObject ().forEach ((propName, unused) -> {
            JsonDependency propDependency = dependencies.get (propName);
            if (propDependency != null) {
                if (propDependency.isSchema ()) {
                    messages.addAll (validate (propDependency.getSchema (), instance));
                } else {
                    Set<String> instanceProperties = new HashSet<>(instance.asObject ().keySet ());

                    propDependency.getProperties ().forEach ( p -> {
                        if (!instanceProperties.contains (p)) {
                            messages.add(new DependencyError (instance.getPath (), p));
                        }
                    });
                }
            }
        });

        return messages;
    }

    private Collection<ValidationMessage> validateArray (JsonSchema schema, JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxItems ().validate (schema, instance));
        messages.addAll (new MinItems ().validate (schema, instance));
        messages.addAll (new UniqueItems ().validate (schema, instance));
        messages.addAll (new Items (this).validate (schema, instance));
        return messages;
    }

    private Collection<ValidationMessage> validateString (JsonSchema schema, JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxLength ().validate (schema, instance));
        messages.addAll (new MinLength ().validate (schema, instance));
        messages.addAll (new io.openapiparser.validator.string.Pattern ().validate (schema, instance));
        messages.addAll (new DateTime ().validate (schema, instance));
        messages.addAll (new Email ().validate (schema, instance));
        messages.addAll (new Hostname ().validate (schema, instance));
        messages.addAll (new IpV4 ().validate (schema, instance));
        messages.addAll (new IpV6 ().validate (schema, instance));
        messages.addAll (new Uri ().validate (schema, instance));
        return messages;
    }

    private Collection<ValidationMessage> validateNumber (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MultipleOf ().validate (schema, instance));
        messages.addAll (new Maximum ().validate (schema, instance));
        messages.addAll (new Minimum ().validate (schema, instance));
        return messages;
    }

//    private JsonInstance getValue (JsonInstance instance, URI uri) {
//        return instance.getValue (uri.getFragment ());
//    }

//    @Deprecated
//    private @Nullable Object getValue (Object source, URI uri) {
//        return getValue (source, uri.getFragment ());
//    }

//    @Deprecated // => JsonInstance
//    private @Nullable Object getValue (Object instance, @Nullable String path) {
//        if (path == null || path.isEmpty ())
//            return instance;
//
//        if (instance instanceof Map) {
//            Bucket bucket = new Bucket (asMap (instance));
//            return bucket.getRawValue (JsonPointer.fromJsonPointer (path));
//
//        } else if (isArray (instance)) {
//            Object[] items = asCol (instance).toArray ();
//            int idx = JsonPointer.fromJsonPointer (path).tailIndex ();
//            return items[idx];
//        }
//
//        return instance;
//    }

    private URI append (URI uri, String value) {
        String target = uri.toString ();
        if (!target.contains("#")) {
          target += "#";
        }

        if (target.charAt(target.length() - 1) != '/') {
          target += "/";
        }

        return URI.create (target + encode (value
            .replace ("~", "~0")
            .replace ("/", "~1")
        ));
    }

    private static String encode (String value) {
        try {
            return URLEncoder.encode (value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            // todo
            throw new RuntimeException ();
        }
    }

    private boolean isRef (Object instance) {
        if (!isObject (instance))
            return false;

        return asObject (instance).containsKey ("$ref");
    }

    private boolean isObject (Object current) {
        return current instanceof Map;
    }

    private Map<String, Object> asObject (Object value) {
        //noinspection unchecked
        return (Map<String, Object>) value;
    }

    private boolean isArray (Object current) {
        return current instanceof Collection;
    }

    private Collection<Object> asArray (Object value) {
        //noinspection unchecked
        return (Collection<Object>) value;
    }
}
