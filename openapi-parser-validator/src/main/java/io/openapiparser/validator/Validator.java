/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.messages.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * the validator.
 */
public class Validator {

    public  Collection<ValidationMessage> validate(JsonSchema schema, Object document) {
        return validate (schema, document, URI.create (""));
    }

    public  Collection<ValidationMessage> validate(JsonSchema schema, Object source, URI uri) {
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
            Collection<Object> array = asArray (current);

            // draft4 - 5.9
            JsonSchema.Items has = schema.hasItems ();
            if (has == JsonSchema.Items.MULTIPLE) {
                JsonSchema additional = schema.getAdditionalItems ();
                if (additional != null && additional.isFalse()) {

                    final Collection<JsonSchema> items = schema.getItemsCollection ();
                    if (array.size () > items.size ()) {
                        messages.add (new ItemsSizeError (uri.toString (), items.size()));
                    }
                }
            }

            // draft4 - 5.11
            int minItems = schema.getMinItems ();
            if (array.size () < minItems) {
                messages.add (new MinItemsError (uri.toString (), minItems));
            }

            // draft4 - 5.12
            if (schema.isUniqueItems ()) {
                Set<Object> items = new HashSet<> ();
                for (Object item : array) {
                    if (!items.add (item)) {
                        messages.add (new UniqueItemsError (uri.toString ()));
                    }
                }
            }
        } else if (current instanceof Map) {
            Map<String, Object> object = asObject (current);
            // schema.getRequiredProperties()
            // check document has required properties

            // document get property
            object.forEach ((propName, propValue) -> {
                final JsonSchema propSchema = schema.getJsonSchema (propName);
                if (propSchema == null) {
                    return;
                }
                messages.addAll (validate (propSchema, source, append (uri, propName)));
            });
        }

        return messages;
    }

    private @Nullable Object getValue (Object source, URI uri) {
        return getValue (source, uri.getRawFragment ());
    }

    private @Nullable Object getValue (Object source, @Nullable String path) {
        return JsonPointer.fromJsonPointer (path).getValue (source);
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
