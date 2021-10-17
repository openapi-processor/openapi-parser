/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import static io.openapiparser.Keywords.OPENAPI;

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
        Object version = api.get(OPENAPI);

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
