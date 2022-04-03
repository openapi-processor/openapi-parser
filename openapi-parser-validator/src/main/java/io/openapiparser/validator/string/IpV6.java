/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.IpV6Validator;

/**
 * validates ipv6.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.5">
 *     Draft 4: ipv6
 * </a>
 */
public class IpV6 {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (format == null || !format.equals ("ipv6"))
            return new NullStep ();

        String instanceValue = instance.asString ();
        boolean valid = new IpV6Validator (instanceValue).validate ();
        if (!valid) {
            return new IpV6Step (new IpV6Error (instance.getPath ()));
        }

        return new IpV6Step ();
    }
}
