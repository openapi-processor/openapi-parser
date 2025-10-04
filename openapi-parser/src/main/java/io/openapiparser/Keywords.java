/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Keywords {

    String ADDITIONAL_OPERATIONS = "additionalOperations";
    String ADDITIONAL_PROPERTIES = "additionalProperties";
    String ALL_OF = "allOf";
    String ALLOW_EMPTY_VALUE = "allowEmptyValue";
    String ALLOW_RESERVED = "allowReserved";
    String ANY_OF = "anyOf";
    String ATTRIBUTE = "attribute";
    String CALLBACKS = "callbacks";
    String COMPONENTS = "components";
    String CONST = "const";
    String CONTACT = "contact";
    String CONTAINS = "contains";
    String CONTENT = "content";
    String CONTENT_TYPE = "contentType";
    String DATA_VALUE = "dataValue";
    String DEFAULT = "default";
    String DEFAULT_MAPPING = "defaultMapping";
    String DELETE = "delete";
    String DEPENDENT_REQUIRED = "dependentRequired";
    String DEPRECATED = "deprecated";
    String DESCRIPTION = "description";
    String DISCRIMINATOR = "discriminator";
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
    String ITEM_ENCODING = "itemEncoding";
    String ITEM_SCHEMA = "itemSchema";
    String ITEMS = "items";
    String JSON_SCHEMA_DIALECT = "jsonSchemaDialect";
    String KIND = "kind";
    String LICENSE = "license";
    String LINKS = "links";
    String MAPPING = "mapping";
    String MAX_CONTAINS = "maxContains";
    String MAX_ITEMS = "maxItems";
    String MAX_LENGTH = "maxLength";
    String MAX_PROPERTIES = "maxProperties";
    String MAXIMUM = "maximum";
    String MEDIA_TYPES = "mediaTypes";
    String MIN_CONTAINS = "minContains";
    String MIN_ITEMS = "minItems";
    String MIN_LENGTH = "minLength";
    String MIN_PROPERTIES = "minProperties";
    String MINIMUM = "minimum";
    String MULTIPLE_OF = "multipleOf";
    String NAME = "name";
    String NAMESPACE = "namespace";
    String NODE_TYPE = "nodeType";
    String NOT = "not";
    String NULLABLE = "nullable";
    String ONE_OF = "oneOf";
    String OPENAPI = "openapi";
    String OPERATION_ID = "operationId";
    String OPTIONS = "options";
    String PARAMETERS = "parameters";
    String PARENT = "parent";
    String PATCH = "patch";
    String PATHS = "paths";
    String PATH_ITEMS = "pathItems";
    String PATTERN = "pattern";
    String PATTERN_PROPERTIES = "patternProperties";
    String PREFIX_ENCODING = "prefixEncoding";
    String PREFIX_ITEMS = "prefixItems";
    String POST = "post";
    String PROPERTIES = "properties";
    String PROPERTY_NAME = "propertyName";
    String PROPERTY_NAMES = "propertyNames";
    String PUT = "put";
    String QUERY = "query";
    String READ_ONLY = "readOnly";
    String REF = "$ref";
    String REQUEST_BODY = "requestBody";
    String REQUEST_BODIES = "requestBodies";
    String REQUIRED = "required";
    String RESPONSES = "responses";
    String SCHEMA = "schema";
    String SCHEMAS = "schemas";
    String SECURITY = "security";
    String SECURITY_SCHEMES = "securitySchemes";
    String SELF = "$self";
    String SERIALIZED_VALUE = "serializedValue";
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
    String WRAPPED = "wrapped";
    String WRITE_ONLY = "writeOnly";
    String XML = "xml";

    Set<String> OPERATIONS = Set.of(DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE, QUERY);

    // overlay
    String OVERLAY = "overlay";
    String EXTENDS = "extends";
    String TARGET = "target";
    String UPDATE = "update";
    String REMOVE = "remove";
    String ACTIONS = "actions";

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
