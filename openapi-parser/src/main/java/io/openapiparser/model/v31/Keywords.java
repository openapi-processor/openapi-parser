package io.openapiparser.model.v31;

import java.util.Arrays;
import java.util.List;

import static io.openapiparser.Keywords.*;

public interface Keywords {
    List<String> OPENAPI_KEYS = Arrays.asList (
        OPENAPI,
        INFO,
        JSON_SCHEMA_DIALECT,
        SERVERS,
        PATHS,
        WEBHOOKS,
        COMPONENTS,
        SECURITY,
        TAGS,
        EXTERNAL_DOCS);

    List<String> OPENAPI_KEYS_REQUIRED = Arrays.asList (
        OPENAPI,
        INFO);

    List<String> OPENAPI_KEYS_REQUIRED_XOR = Arrays.asList (
        PATHS,
        WEBHOOKS,
        COMPONENTS);
}
