/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import static io.openapiparser.schema.SchemaVersion.*;

public enum Format {

    // draft-07
    DATE("date"),
    TIME("time"),
    IDN_EMAIL("idn-email"),
    IDN_HOSTNAME("idn-hostname"),
    IRI("iri"),
    IRI_REFERENCE("iri-reference"),
    RELATIVE_JSON_POINTER("relative-json-pointer"),

    // draft-06
    URI_REFERENCE("uri-reference"),
    URI_TEMPLATE("uri-template"),
    JSON_POINTER("json-pointer"),

    // draft-04
    DATE_TIME("date-time"),
    EMAIL("email"),
    HOSTNAME("hostname"),
    IPV4("ipv4"),
    IPV6("ipv6"),
    URI("uri");

    private final String format;

    Format (String format) {
        this.format = format;
    }

    public String getFormat () {
        return format;
    }
}
