/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.UriValidator;

/**
 * validates format: uri.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.6">
 *     Draft 4: uri
 * </a>
 */
public class Uri {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (format == null || !format.equals ("uri"))
            return new NullStep ();

        String instanceValue = instance.asString ();

        boolean valid = new UriValidator (instanceValue).validate ();
        if (!valid) {
            return new UriStep (new UriError (instance.getPath ()));
        }

        return new UriStep ();
    }
}
