package io.openapiparser.validator;

import io.openapiparser.validator.messages.UniqueItemsError;
import io.openapiparser.validator.messages.ValidationMessage;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Validator {

    public  Collection<ValidationMessage> validate(JsonSchema schema, Object document) {
        return validate (schema, document, URI.create (""));
    }

    public  Collection<ValidationMessage> validate(JsonSchema schema, Object source, URI uri) {
        Collection<ValidationMessage> messages = new ArrayList<> ();

        // get document "node"

        Object value;
        value = getValue(source, uri);
//        Object object;
//        try {
//          object = getObject(document, uri);
//        } catch (MissingPathException e) {
//          throw new IllegalStateException(e);
//        }

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

        /*
              if (schema.isUniqueItems()) {
        Collection<Object> items = new HashSet<>();
        for (Object o : jsonArray) {
          if (!items.add(makeComparable(o))) {
            error.accept(new UniqueItemsError(uri, document, schema));
          }
        }
      }
      package net.jimblackler.jsonschemafriend;

      public class ComparableUtils {
        static Object makeComparable(Object a) {
          if (a == null) {
            return new ComparableNull();
          } else if (a instanceof Number) {
            return ((Number) a).doubleValue();
          }
          return a;
        }
      }

         */
        if (source instanceof Collection) {
            Collection<Object> array = asArray (source);

            if (schema.isUniqueItems ()) {
                Set<Object> items = new HashSet<> ();
                for (Object item : array) {
                    if (!items.add (item)) {
                        messages.add (new UniqueItemsError (uri.toString ()));
                    }
                }
            }
        } else if (source instanceof Map) {
            Map<String, Object> object = asObject (source);
            // schema.getRequiredProperties()
            // check document has required properties

            // document get property
            object.forEach ((propName, propValue) -> {
                final JsonSchema propSchema = schema.getPropertySchema (propName);
                if (propSchema == null) {
                    return;
                }
                validate (propSchema, source, append(uri, propName));
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
