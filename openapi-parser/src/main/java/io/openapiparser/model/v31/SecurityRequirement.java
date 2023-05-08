/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiprocessor.jsonschema.schema.Bucket;

/**
 * the <em>Security Requirement</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#security-requirement-object">
 *   4.8.30 Security Requirement Object
 * </a>
 */
public class SecurityRequirement extends Properties {

    public SecurityRequirement (Context context, Bucket bucket) {
        super (context, bucket);
    }
}
