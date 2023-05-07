/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.SchemaVersion;

import static io.openapiparser.schema.Format.*;

public class ValidatorSettingsDefaults {

    public static ValidatorSettings draft202012 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft202012);
        return settings;
    }

    public static ValidatorSettings draft201909 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft201909);
        return settings;
    }

    public static ValidatorSettings draft7 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft7);
        settings.enableFormats (
            REGEX,
            JSON_POINTER,
            RELATIVE_JSON_POINTER,
            URI_TEMPLATE,
            URI,
            URI_REFERENCE,
            IRI,
            IRI_REFERENCE,
            IPV6,
            IPV4,
            HOSTNAME,
            IDN_HOSTNAME,
            EMAIL,
            IDN_EMAIL,
            DATE_TIME,
            DATE,
            TIME
        );
        return settings;
    }

    public static ValidatorSettings draft6 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft6);
        settings.enableFormats (
            JSON_POINTER,
            URI_TEMPLATE,
            URI_REFERENCE,
            URI,
            IPV6,
            IPV4,
            HOSTNAME,
            EMAIL,
            DATE_TIME
        );
        return settings;
    }

    public static ValidatorSettings draft4 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft4);
        settings.enableFormats (
            URI,
            IPV6,
            IPV4,
            HOSTNAME,
            EMAIL,
            DATE_TIME
        );
        return settings;
    }
}
