/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.converter.StringNotNullConverter;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.Keywords.OPENAPI;

public class OpenApiVersionParser {

    public static OpenApiVersion parseVersion(Object document) {
        String version = getVersion(document);

        if (isVersion30(version)) {
            return OpenApiVersion.V30;

        } else if (isVersion31(version)) {
            return OpenApiVersion.V31;

        } else if (isVersion32(version)) {
            return OpenApiVersion.V32;

        } else {
            throw new UnknownVersionException(version);
        }
    }

    private static boolean isVersion30(String version) {
        return checkVersion (version, "3.0");
    }

    private static boolean isVersion31(String version) {
        return checkVersion (version, "3.1");
    }

    private static boolean isVersion32(String version) {
        return checkVersion (version, "3.2");
    }

    private static boolean checkVersion (String version, String prefix) {
        return version.startsWith (prefix);
    }

    private static String getVersion(Object document) {
        if (!Types.isMap(document)) {
            throw new UnknownVersionException(null);
        }

        Map<String, @Nullable Object> root = Types.asMap(document);
        StringNotNullConverter converter = new StringNotNullConverter();
        return converter.convert(OPENAPI, root.get(OPENAPI), "");
    }
}
