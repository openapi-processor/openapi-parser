package io.openapiparser.model.v30;

import java.util.Arrays;
import java.util.List;

import static io.openapiparser.Keywords.*;

public interface Keywords {
    List<String> OPENAPI_KEYS = Arrays.asList (
        OPENAPI,
        INFO,
        SERVERS,
        PATHS,
        COMPONENTS,
        SECURITY,
        TAGS,
        EXTERNAL_DOCS);

    List<String> OPENAPI_KEYS_REQUIRED = Arrays.asList (
        OPENAPI,
        INFO,
        PATHS);
}
