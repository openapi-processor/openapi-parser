/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNotNullConverter;
import io.openapiparser.schema.*;

import java.net.URI;

import static io.openapiparser.Keywords.OPENAPI;
import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.support.Nullness.nonNull;

public class OpenApiParser {
    private final Resolver resolver;

    public OpenApiParser (Resolver resolver) {
        this.resolver = resolver;
    }

    public OpenApiResult parse(URI baseUri) throws Exception {
        try {
            return createResult (resolver.resolve (baseUri));
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    public OpenApiResult parse(String resource) throws Exception {
        try {
            return createResult (resolver.resolve (resource));
        } catch (Exception e) {
            throw new ParserException (e);
        }
    }

    private OpenApiResult createResult (ResolverResult result) {
        Object document = result.getDocument ();
        Bucket api = new Bucket (result.getUri (), asMap (document));
        String version = getVersion (api);

        if (isVersion30 (version)) {
            return new OpenApiResult30 (new Context (result.getUri (), result.getRegistry ()), api);
        } else if (isVersion31 (version)) {
            return new OpenApiResult31 (new Context (result.getUri (), result.getRegistry ()), api);
        } else {
            throw new UnknownVersionException (version);
        }
    }

    private String getVersion (Bucket api) {
        return nonNull (api.convert (OPENAPI, new StringNotNullConverter ()));
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
