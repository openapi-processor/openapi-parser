/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.*;
import io.openapiparser.validator.array.*;
import io.openapiparser.validator.number.*;
import io.openapiparser.validator.object.AdditionalPropertiesError;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.openapiparser.converter.Types.asMap;

/**
 * the validator.
 *
 * draft 4:
 * https://datatracker.ietf.org/doc/html/draft-zyp-json-schema-04
 * https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00
 * https://tools.ietf.org/html/draft-luff-json-hyper-schema-00
 * https://tools.ietf.org/html/draft-pbryan-zyp-json-ref-03
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

        if (current instanceof Collection) {
            messages.addAll (validateArray (uri, schema, asArray (current)));

        } else if (current instanceof Map) {
            Map<String, Object> object = asObject (current);

            object.forEach ((propName, propValue) -> {
                final JsonSchema propSchema = schema.getJsonSchema (propName);
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
                messages.addAll (validate (propSchema, source, append (uri, propName)));
            });
        } else if (current instanceof Number) {
            messages.addAll (validateNumber (uri, schema, (Number)current));
        }

        return messages;
    }

    private Collection<ValidationMessage> validateArray (
        URI uri, JsonSchema schema, Collection<Object> array) {

        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new HasItems (uri).validate (schema, array));
        messages.addAll (new MinItems (uri).validate (schema, array));
        messages.addAll (new UniqueItems (uri).validate (schema, array));
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

    private @Nullable Object getValue (Object source, @Nullable String path) {
        if (source instanceof Map) {
            Bucket bucket = new Bucket (asMap (source));
            return bucket.getRawValue (JsonPointer.fromJsonPointer (path));
        }

        return source;
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

    private Map<String, Object> asObject (Object value) {
        //noinspection unchecked
        return (Map<String, Object>) value;
    }

    private Collection<Object> asArray (Object value) {
        //noinspection unchecked
        return (Collection<Object>) value;
    }
}
