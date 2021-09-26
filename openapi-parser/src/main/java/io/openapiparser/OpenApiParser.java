package io.openapiparser;

import java.util.*;

public class OpenApiParser {
    private final Context context;

    public OpenApiParser (Context context) {
        this.context = context;
    }

    public OpenApiResult parse() throws Exception {
        try {
            context.read ();
            return createResult (context.getBaseNode ());
        } catch (Exception e) {
            // todo
            throw e;
        }
    }

    private OpenApiResult createResult (Node api) {
        Object version = api.get("openapi");

        if (isVersion30 (version)) {
            return createOpenApiResult30 ();
        } else if (isVersion31 (version)) {
            return createOpenApiResult31 ();
        } else {
            // todo
            throw new RuntimeException ();
        }
    }

    private OpenApiResult31 createOpenApiResult31 () {
        OpenApiResult31 result = new OpenApiResult31 (context);
        result.validate();
        return result;
    }

    private OpenApiResult30 createOpenApiResult30 () {
        OpenApiResult30 result = new OpenApiResult30 (context);
        result.validate();
        return result;
    }

    private boolean isVersion30(Object version) {
        return checkVersion (version, "3.0");
    }

    private boolean isVersion31(Object version) {
        return checkVersion (version, "3.1");
    }

    private boolean checkVersion (Object version, String prefix) {
        return (version instanceof String) && ((String) version).startsWith (prefix);
    }
}
