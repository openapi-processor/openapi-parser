/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.any.Type;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.object.*;
import io.openapiparser.validator.string.MaxLength;
import io.openapiparser.validator.string.MinLength;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.openapiparser.converter.Types.asCol;
import static io.openapiparser.converter.Types.asMap;

/**
 * the validator.
 */
public class Validator {

    public  Collection<ValidationMessage> validate(JsonSchema schema, /*Object*/ JsonInstance instance) {
        return validate (schema, instance, URI.create (""));
    }

    public Collection<ValidationMessage> validate(JsonSchema schema, JsonInstance instance, URI instanceUri) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        if (schema.isRef()) {
            schema = schema.getRefSchema ();
        }

        // get instance
        JsonInstance current = getValue (instance, instanceUri);

        // if
        // then
        // else
        // ref
        // allOf
        // anyOf
        // oneOf
        // not

        // primitive type
        // collection
        // map

        messages.addAll (new Type (instanceUri, schema).validate (current));

        if (isRef(current)) {
            int i = 0;

        } else if (current.isArray ()) {
            messages.addAll (validateArray (instanceUri, current, schema));

        } else if (current.isObject ()) {
            messages.addAll (validateObject (instanceUri, instance, current, schema));

        } else if (current.getRawValue () instanceof Number) {
            messages.addAll (validateNumber (instanceUri, current, schema));

        } else if (current.getRawValue () instanceof String) {
            messages.addAll (validateString (instanceUri, current, schema));
        }

        return messages;
    }

    /** https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4
     */
    private Collection<? extends ValidationMessage> validateObject (
        URI instanceUri, JsonInstance instance, JsonInstance current, JsonSchema schema) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        messages.addAll (new MaxProperties (instanceUri).validate (schema, current));
        messages.addAll (new MinProperties (instanceUri).validate (schema, current));
        messages.addAll (new Required (instanceUri).validate (schema, current));

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4.4

        JsonSchema additionalProperties = schema.getAdditionalProperties ();
        Map<String, JsonSchema> schemaProperties = schema.getProperties ();

        if (additionalProperties instanceof JsonSchemaBoolean && additionalProperties.isFalse ()) {
            Set<String> instanceProperties = new HashSet<>(current.asObject ().keySet ());

            instanceProperties.removeAll (schemaProperties.keySet ());

            Map<String, JsonSchema> patterns = schema.getPatternProperties ();
            Iterator<String> it = instanceProperties.iterator();
            while (it.hasNext()) {
                String property = it.next ();

                for (String pattern : patterns.keySet ()) {
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(property);
                    if (m.find()) {
                        it.remove ();
                    }
                }
            }

            if (!instanceProperties.isEmpty ()) {
                instanceProperties.forEach (k -> {
                    messages.add (new AdditionalPropertiesError (append (instanceUri, k).toString ()));
                });
            }
        }

        current.asObject ().forEach ((propName, propValue) -> {
            JsonSchema propSchema = schemaProperties.get (propName);
            if (propSchema == null) {
                propSchema = additionalProperties;
            }

            if (propSchema == null)
                return;

            messages.addAll (validate (propSchema, instance, append (instanceUri, propName)));
        });

        return messages;
    }

    private Collection<ValidationMessage> validateArray (
        URI uri, JsonInstance array, JsonSchema schema) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxItems (uri).validate (schema, array));
        messages.addAll (new MinItems (uri).validate (schema, array));
        messages.addAll (new UniqueItems (uri).validate (schema, array));
        messages.addAll (new Items (uri, schema, this).validate (array));
        return messages;
    }

    private Collection<ValidationMessage> validateString (
        URI uri, JsonInstance string, JsonSchema schema) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxLength (uri, schema).validate (string));
        messages.addAll (new MinLength (uri, schema).validate (string));
        messages.addAll (new io.openapiparser.validator.string.Pattern (uri, schema).validate (string));
        return messages;
    }

    private Collection<ValidationMessage> validateNumber (
        URI uri, JsonInstance number, JsonSchema schema) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MultipleOf (uri).validate (schema, number));
        messages.addAll (new Maximum (uri, schema).validate (number));
        messages.addAll (new Minimum (uri, schema).validate (number));
        return messages;
    }

    private JsonInstance getValue (JsonInstance instance, URI uri) {
        return instance.getValue (uri.getFragment ());
    }

    @Deprecated
    private @Nullable Object getValue (Object source, URI uri) {
        return getValue (source, uri.getFragment ());
    }

    @Deprecated
    private @Nullable Object getValue (Object instance, @Nullable String path) {
        if (path == null || path.isEmpty ())
            return instance;

        if (instance instanceof Map) {
            Bucket bucket = new Bucket (asMap (instance));
            return bucket.getRawValue (JsonPointer.fromJsonPointer (path));

        } else if (isArray (instance)) {
            Object[] items = asCol (instance).toArray ();
            int idx = JsonPointer.fromJsonPointer (path).tailIndex ();
            return items[idx];
        }

        return instance;
    }

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
