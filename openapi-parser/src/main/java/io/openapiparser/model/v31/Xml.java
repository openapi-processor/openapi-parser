/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;

import java.util.Map;

/**
 * the <em>XML</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#xml-object">4.8.26 XML Object</a>
 */
public class Xml extends Properties implements Extensions {

    public Xml (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
