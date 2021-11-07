/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Keywords {

    String ALLOW_EMPTY_VALUE = "allowEmptyValue";
    String ALLOW_RESERVED = "allowReserved";
    String COMPONENTS = "components";
    String CONTACT = "contact";
    String CONTENT = "content";
    String DEFAULT = "default";
    String DELETE = "delete";
    String DEPRECATED = "deprecated";
    String DESCRIPTION = "description";
    String ENUM = "enum";
    String EMAIL = "email";
    String EXAMPLE = "example";
    String EXAMPLES = "examples";
    String EXPLODE = "explode";
    String EXTERNAL_DOCS = "externalDocs";
    String GET = "get";
    String HEAD = "head";
    String IN = "in";
    String INFO = "info";
    String JSON_SCHEMA_DIALECT = "jsonSchemaDialect";
    String LICENSE = "license";
    String NAME = "name";
    String OPENAPI = "openapi";
    String OPERATION_ID = "operationId";
    String OPTIONS = "options";
    String PARAMETERS = "parameters";
    String PATHS = "paths";
    String PATCH = "patch";
    String POST = "post";
    String PUT = "put";
    String REF = "$ref";
    String REQUIRED = "required";
    String SCHEMA = "schema";
    String SECURITY = "security";
    String SERVERS = "servers";
    String STYLE = "style";
    String SUMMARY = "summary";
    String TAGS = "tags";
    String TERMS_OF_SERVICE = "termsOfService";
    String TITLE = "title";
    String TRACE = "trace";
    String URL = "url";
    String VARIABLES = "variables";
    String VERSION = "version";
    String WEBHOOKS = "webhooks";

    static Collection<String> getProperties(Collection<Keyword> keywords) {
        return keywords
            .stream ()
            .map (Keyword::getKeyword)
            .collect (Collectors.toList ());
    }

    static Collection<String> getRequiredProperties(Collection<Keyword> keywords) {
        return keywords
            .stream ()
            .filter (Keyword::isRequired)
            .map (Keyword::getKeyword)
            .collect (Collectors.toList ());
    }

    static Keyword optional (String keyword) {
        return new Keyword (keyword);
    }

    static Keyword required (String keyword) {
        return new Keyword (keyword, true);
    }
}
