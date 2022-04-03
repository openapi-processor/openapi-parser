/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.IpV4Validator;

/**
 * validates ipv4.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.4">
 *     Draft 4: ipv4
 * </a>
 */
public class IpV4 {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (format == null || !format.equals ("ipv4"))
            return new NullStep ();

        String instanceValue = instance.asString ();
        boolean valid = new IpV4Validator (instanceValue).validate ();
        if (!valid) {
            return new IpV4Step (new IpV4Error (instance.getPath ()));
        }

        return new IpV4Step ();
    }
}
