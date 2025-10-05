/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class OpenApiSchemas {
    public static URI    OPENAPI_SCHEMA_32_ID = id("https://spec.openapis.org/oas/3.2/schema/2025-09-17");
    public static String OPENAPI_SCHEMA_32 = "/openapi/3.2/schema/2025-09-15.yaml";
    public static String OPENAPI_VERSION_32_LATEST = "3.2.0";

    public static URI    OPENAPI_SCHEMA_31_ID = id("https://spec.openapis.org/oas/3.1/schema/2025-09-15");
    public static String OPENAPI_SCHEMA_31 = "/openapi/3.1/schema/2025-09-15";
    public static String OPENAPI_VERSION_31_LATEST = "3.1.1";

    public static URI    OPENAPI_SCHEMA_30_ID = id("https://spec.openapis.org/oas/3.0/schema/2024-10-18");
    public static String OPENAPI_SCHEMA_30 = "/openapi/3.0/schema/2024-10-18";
    public static String OPENAPI_VERSION_30_LATEST = "3.0.4";

    public static URI    OVERLAY_SCHEMA_10_ID = id("https://spec.openapis.org/overlay/1.0/schema/2024-10-22");
    public static String OVERLAY_SCHEMA_10 = "/overlay/1.0/schema/2024-10-22";

    private static URI id (String uri) {
        return URI.create (uri);
    }
}
