/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class OpenApiSchemas {
    static URI    OPENAPI_SCHEMA_30_ID = id("https://spec.openapis.org/oas/3.0/schema/2021-09-28");
    static String OPENAPI_SCHEMA_30 = "/openapi/schemas/v3.0/schema.yaml";

    static URI    OPENAPI_SCHEMA_31_ID = id("https://spec.openapis.org/oas/3.1/schema/2022-02-27");
    static String OPENAPI_SCHEMA_31 = "/openapi/schemas/v3.1/schema.yaml";

    static URI    OVERLAY_SCHEMA_10_ID = id("https://spec.openapis.org/overlay/1.0/schema");
    static String OVERLAY_SCHEMA_10 = "/overlay/schemas/v1.0/schema.yaml";

    private static URI id (String uri) {
        return URI.create (uri);
    }
}
