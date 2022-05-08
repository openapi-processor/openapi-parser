/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import static io.openapiparser.schema.SchemaVersion.*;

public enum Format {
    DATE_TIME("date-time", Draft4),
    EMAIL("email", Draft4),
    HOSTNAME("hostname", Draft4),
    IPV4("ipv4", Draft4),
    IPV6("ipv6", Draft4),
    URI("uri", Draft4),
    URI_REFERENCE("uri-reference", Draft6),
    URI_TEMPLATE("uri-template", Draft6),
    JSON_POINTER("json-pointer", Draft6);

    private final String format;
    private final SchemaVersion version;

    Format (String format, SchemaVersion version) {
        this.format = format;
        this.version = version;
    }

    public String getFormat () {
        return format;
    }

    public SchemaVersion getVersion () {
        return version;
    }
}
