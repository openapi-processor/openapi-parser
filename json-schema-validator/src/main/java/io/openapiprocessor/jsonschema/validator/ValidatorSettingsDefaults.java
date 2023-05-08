/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator;

import io.openapiprocessor.jsonschema.schema.SchemaVersion;
import io.openapiprocessor.jsonschema.schema.Format;

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
            Format.REGEX,
            Format.JSON_POINTER,
            Format.RELATIVE_JSON_POINTER,
            Format.URI_TEMPLATE,
            Format.URI,
            Format.URI_REFERENCE,
            Format.IRI,
            Format.IRI_REFERENCE,
            Format.IPV6,
            Format.IPV4,
            Format.HOSTNAME,
            Format.IDN_HOSTNAME,
            Format.EMAIL,
            Format.IDN_EMAIL,
            Format.DATE_TIME,
            Format.DATE,
            Format.TIME
        );
        return settings;
    }

    public static ValidatorSettings draft6 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft6);
        settings.enableFormats (
            Format.JSON_POINTER,
            Format.URI_TEMPLATE,
            Format.URI_REFERENCE,
            Format.URI,
            Format.IPV6,
            Format.IPV4,
            Format.HOSTNAME,
            Format.EMAIL,
            Format.DATE_TIME
        );
        return settings;
    }

    public static ValidatorSettings draft4 () {
        ValidatorSettings settings = new ValidatorSettings ();
        settings.setVersion (SchemaVersion.Draft4);
        settings.enableFormats (
            Format.URI,
            Format.IPV6,
            Format.IPV4,
            Format.HOSTNAME,
            Format.EMAIL,
            Format.DATE_TIME
        );
        return settings;
    }
}
