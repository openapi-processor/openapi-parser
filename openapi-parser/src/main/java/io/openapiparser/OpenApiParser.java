/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNotNullConverter;
import io.openapiparser.schema.Bucket;

import static io.openapiparser.Keywords.OPENAPI;

public class OpenApiParser {
    private final Context context;

    public OpenApiParser (Context context) {
        this.context = context;
    }

    public OpenApiResult parse() throws Exception {
        try {
//            context.read ();
            return createResult (context.read ());
        } catch (Exception e) {
            // todo
            throw e;
        }
    }

    private OpenApiResult createResult (Bucket api) {
        String version = api.convert (OPENAPI, new StringNotNullConverter ());

        if (isVersion30 (version)) {
            return new OpenApiResult30 (context, api);
        } else if (isVersion31 (version)) {
            return new OpenApiResult31 (context, api);
        } else {
            // todo unknown version
            throw new RuntimeException ();
        }
    }

    private boolean isVersion30(String version) {
        return checkVersion (version, "3.0");
    }

    private boolean isVersion31(String version) {
        return checkVersion (version, "3.1");
    }

    private boolean checkVersion (String version, String prefix) {
        return version.startsWith (prefix);
    }
}
