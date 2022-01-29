/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.any.Type;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.object.AdditionalPropertiesError;
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

    public  Collection<ValidationMessage> validate(JsonSchema schema, Object document) {
        return validate (schema, document, URI.create (""));
    }

    public Collection<ValidationMessage> validate(JsonSchema schema, Object source, URI uri) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        // get document "node"
        Object current = getValue(source, uri);

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

        messages.addAll (new Type (uri, schema).validate (current));

        if (isArray (current)) {
            messages.addAll (validateArray (uri, schema, asArray (current)));

        } else if (isObject (current)) {
            messages.addAll (validateObject (uri, schema, asObject (current)));

        } else if (current instanceof Number) {
            messages.addAll (validateNumber (uri, schema, (Number)current));

        } else if (current instanceof String) {
            messages.addAll (validateString (uri, schema, (String)current));
        }

        return messages;
    }

    /** https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.4.4
     */
    private Collection<? extends ValidationMessage> validateObject (
        URI uri, JsonSchema schema, Map<String, Object> object) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        object.forEach ((propName, propValue) -> {
            final JsonSchema propSchema = schema.getJsonSchema (propName);
            if (propSchema == null)
                return;

            /*
            if (propSchema == null) {

                // draft4 - 5.18
                if (schema.getAdditionalProperties().isFalse()) {

                    // draft4 - 5.17
                    Map<String, JsonSchema> patterns = schema.getPatternProperties ();
                    for (String pattern : patterns.keySet ()) {
                        Pattern p = Pattern.compile(pattern);
                        Matcher m = p.matcher(propName);
                        if (m.find())
                            return;
                    }

                    messages.add (new AdditionalPropertiesError (append (uri, propName).toString ()));
                }
                return;
            }
             */

            messages.addAll (validate (propSchema, object, append (uri, propName)));
        });

        return messages;
    }

    private Collection<ValidationMessage> validateArray (
        URI uri, JsonSchema schema, Collection<Object> array) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MinItems (uri).validate (schema, array));
        messages.addAll (new UniqueItems (uri).validate (schema, array));
        messages.addAll (new Items (uri, schema, this).validate (array));
        return messages;
    }

    private Collection<ValidationMessage> validateString (
        URI uri, JsonSchema schema, String value) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxLength (uri, schema).validate (value));
        messages.addAll (new MinLength (uri, schema).validate (value));
        messages.addAll (new io.openapiparser.validator.string.Pattern (uri, schema).validate (value));
        return messages;
    }

    private Collection<ValidationMessage> validateNumber (
        URI uri, JsonSchema schema, Number number) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MultipleOf (uri).validate (schema, number));
        messages.addAll (new Maximum (uri, schema).validate (number));
        messages.addAll (new Minimum (uri, schema).validate (number));
        return messages;
    }

    private @Nullable Object getValue (Object source, URI uri) {
        return getValue (source, uri.getFragment ());
    }

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
