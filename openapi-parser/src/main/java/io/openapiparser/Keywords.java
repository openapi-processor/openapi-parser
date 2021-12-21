/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Keywords {

    String ADDITIONAL_PROPERTIES = "additionalProperties";
    String ALLOW_EMPTY_VALUE = "allowEmptyValue";
    String ALLOW_RESERVED = "allowReserved";
    String COMPONENTS = "components";
    String CONST = "const";
    String CONTACT = "contact";
    String CONTENT = "content";
    String CONTENT_TYPE = "contentType";
    String DEFAULT = "default";
    String DELETE = "delete";
    String DEPENDENT_REQUIRED = "dependentRequired";
    String DEPRECATED = "deprecated";
    String DESCRIPTION = "description";
    String ENUM = "enum";
    String EMAIL = "email";
    String ENCODING = "encoding";
    String EXAMPLE = "example";
    String EXAMPLES = "examples";
    String EXCLUSIVE_MAXIMUM = "exclusiveMaximum";
    String EXCLUSIVE_MINIMUM = "exclusiveMinimum";
    String EXPLODE = "explode";
    String EXTERNAL_DOCS = "externalDocs";
    String EXTERNAL_VALUE = "externalValue";
    String FORMAT = "format";
    String GET = "get";
    String HEAD = "head";
    String HEADERS = "headers";
    String IDENTIFIER = "identifier";
    String IN = "in";
    String INFO = "info";
    String JSON_SCHEMA_DIALECT = "jsonSchemaDialect";
    String LICENSE = "license";
    String MAX_CONTAINS = "maxContains";
    String MAX_ITEMS = "maxItems";
    String MAX_LENGTH = "maxLength";
    String MAX_PROPERTIES = "maxProperties";
    String MAXIMUM = "maximum";
    String MIN_CONTAINS = "minContains";
    String MIN_ITEMS = "minItems";
    String MIN_LENGTH = "minLength";
    String MIN_PROPERTIES = "minProperties";
    String MINIMUM = "minimum";
    String MULTIPLE_OF = "multipleOf";
    String NAME = "name";
    String NULLABLE = "nullable";
    String OPENAPI = "openapi";
    String OPERATION_ID = "operationId";
    String OPTIONS = "options";
    String PARAMETERS = "parameters";
    String PATHS = "paths";
    String PATCH = "patch";
    String PATTERN = "pattern";
    String PATTERN_PROPERTIES = "patternProperties";
    String POST = "post";
    String PROPERTIES = "properties";
    String PROPERTY_NAMES = "propertyNames";
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
    String TYPE = "type";
    String UNIQUE_ITEMS = "uniqueItems";
    String URL = "url";
    String VALUE = "value";
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
