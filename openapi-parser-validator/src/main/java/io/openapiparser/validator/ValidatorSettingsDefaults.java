/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.SchemaVersion;

import static io.openapiparser.schema.Format.*;

public class ValidatorSettingsDefaults {

    public static ValidatorSettings draft201909 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft201909);
        return settings;
    }

    public static ValidatorSettings draft7 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft7);
        settings.enableFormats (
            DATE_TIME,
            EMAIL,
            HOSTNAME,
            IPV4,
            IPV6,
            URI,
            URI_REFERENCE,
            URI_TEMPLATE,
            JSON_POINTER,
            DATE,
            TIME,
            IDN_EMAIL,
            IDN_HOSTNAME,
            IRI,
            IRI_REFERENCE,
            RELATIVE_JSON_POINTER
        );
        return settings;
    }

    public static ValidatorSettings draft6 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft6);
        settings.enableFormats (
            DATE_TIME,
            EMAIL,
            HOSTNAME,
            IPV4,
            IPV6,
            URI,
            URI_REFERENCE,
            URI_TEMPLATE,
            JSON_POINTER
        );
        return settings;
    }

    public static ValidatorSettings draft4 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft4);
        settings.enableFormats (
            DATE_TIME,
            EMAIL,
            HOSTNAME,
            IPV4,
            IPV6,
            URI
        );
        return settings;
    }
}
