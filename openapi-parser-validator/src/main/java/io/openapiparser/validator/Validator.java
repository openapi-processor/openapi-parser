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
import io.openapiparser.validator.object.Properties;
import io.openapiparser.validator.steps.*;
import io.openapiparser.validator.string.*;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * the validator.
 */
public class Validator {
    private final ValidatorSettings settings;

    public Validator () {
        settings = new ValidatorSettings ();
    }

    public Validator (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate(JsonSchema schema, JsonInstance instance) {
        CompositeStep step = new CompositeStep ();
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
            step.add (validate (sao, instance));
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.4
        Collection<JsonSchema> anyOf = schema.getAnyOf ();
        int anyOfValidCount = 0;
        for (JsonSchema anyOfSchema : anyOf) {
//            boolean valid = validate (anyOfSchema, instance).isEmpty ();
            ValidationStep anyOfStep = validate (anyOfSchema, instance);
            if (anyOfStep.isValid ()) {
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
//            boolean valid = validate (oneOfSchema, instance).isEmpty ();
            ValidationStep oneOfStep = validate (oneOfSchema, instance);
            if (oneOfStep.isValid ()) {
                oneOfValidCount++;
            }
        }

        if (oneOf.size () > 0 && oneOfValidCount != 1) {
            messages.add (new OneOfError (instance.getPath ()));
        }

        // https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.5.6
        JsonSchema not = schema.getNot ();
        if (not != null) {
//            boolean valid = validate (not, instance).isEmpty ();
            ValidationStep notStep = validate (not, instance);
            if (notStep.isValid ()) {
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

        step.add (validateType (schema, instance));

        if (instance.isArray ()) {
            step.add (validateArray (schema, instance));

        } else if (instance.isObject ()) {
            step.add (validateObject (schema, instance));

        } else if (instance.getRawValue () instanceof Number) {
            step.add (validateNumber (schema, instance));

        } else if (instance.getRawValue () instanceof String) {
            messages.addAll (validateString (schema, instance));
        }

        step.add (new MessageStep (messages));
        return step;
    }

    private ValidationStep validateType (JsonSchema schema, JsonInstance instance) {
        return new Type ().validate (schema, instance);
    }

    private ValidationStep validateArray (JsonSchema schema, JsonInstance instance) {
        CompositeStep step = new FlatStep ();
        step.add (new MaxItems ().validate (schema, instance));
        step.add (new MinItems ().validate (schema, instance));
        step.add (new UniqueItems ().validate (schema, instance));
        step.add (new Items (this).validate (schema, instance));
        return step;
    }

    private ValidationStep validateObject (JsonSchema schema, JsonInstance instance) {
        CompositeStep step = new FlatStep ();
        step.add (new MaxProperties ().validate (schema, instance));
        step.add (new MinProperties ().validate (schema, instance));
        step.add (new Required ().validate (schema, instance));
        step.add (new Properties (this).validate(schema, instance));
        step.add (new Dependencies (this).validate (schema, instance));
        return step;
    }

    private ValidationStep validateNumber (JsonSchema schema, JsonInstance instance) {
        CompositeStep step = new FlatStep ();
        step.add (new MultipleOf ().validate (schema, instance));
        step.add (new Maximum ().validate (schema, instance));
        step.add (new Minimum ().validate (schema, instance));
        return step;
    }

    private Collection<ValidationMessage> validateString (JsonSchema schema, JsonInstance instance) {
        Collection<ValidationMessage> messages = new ArrayList<> ();
        messages.addAll (new MaxLength ().validate (schema, instance).getMessages ());
        messages.addAll (new MinLength ().validate (schema, instance).getMessages ());
        messages.addAll (new io.openapiparser.validator.string.Pattern ().validate (schema, instance).getMessages ());
        messages.addAll (new DateTime ().validate (schema, instance).getMessages ());
        messages.addAll (new Email ().validate (schema, instance).getMessages ());
        messages.addAll (new Hostname ().validate (schema, instance));
        messages.addAll (new IpV4 ().validate (schema, instance));
        messages.addAll (new IpV6 ().validate (schema, instance));
        messages.addAll (new Uri ().validate (schema, instance));
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
