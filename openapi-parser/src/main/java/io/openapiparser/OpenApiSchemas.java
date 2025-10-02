/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class OpenApiSchemas {
    public static URI    OPENAPI_SCHEMA_32_ID = id("https://spec.openapis.org/oas/3.2/schema/WORK-IN-PROGRESS");
    public static String OPENAPI_SCHEMA_32 = "/openapi/schemas/v3.2/schema.yaml";
    public static String OPENAPI_VERSION_32_LATEST = "3.2.0";

    public static URI    OPENAPI_SCHEMA_31_ID = id("https://spec.openapis.org/oas/3.1/schema/2022-02-27");
    public static String OPENAPI_SCHEMA_31 = "/openapi/schemas/v3.1/schema.yaml";
    public static String OPENAPI_VERSION_31_LATEST = "3.1.1";

    public static URI    OPENAPI_SCHEMA_30_ID = id("https://spec.openapis.org/oas/3.0/schema/2021-09-28");
    public static String OPENAPI_SCHEMA_30 = "/openapi/schemas/v3.0/schema.yaml";
    public static String OPENAPI_VERSION_30_LATEST = "3.0.4";

    public static URI    OVERLAY_SCHEMA_10_ID = id("https://spec.openapis.org/overlay/1.0/schema");
    public static String OVERLAY_SCHEMA_10 = "/overlay/schemas/v1.0/schema.yaml";

    private static URI id (String uri) {
        return URI.create (uri);
    }
}
