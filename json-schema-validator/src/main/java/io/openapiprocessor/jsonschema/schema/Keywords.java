/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public interface Keywords {
    // misc
    String HASH = "#";

    // vocabulary: core
    String ANCHOR = "$anchor";
    String COMMENT = "$comment";
    String DEFS = "$defs";
    String DEFINITIONS = "definitions";
    String ID = "$id";
    String ID4 = "id";
    String SCHEMA = "$schema";

    String REF = "$ref";
    String DYNAMIC_ANCHOR = "$dynamicAnchor";
    String DYNAMIC_REF = "$dynamicRef";
    String RECURSIVE_ANCHOR = "$recursiveAnchor";
    String RECURSIVE_REF = "$recursiveRef";
    String VOCABULARY = "$vocabulary";

    // vocabulary: applicators - boolean
    String ALL_OF = "allOf";
    String ANY_OF = "anyOf";
    String ONE_OF = "oneOf";
    String NOT = "not";

    // vocabulary: applicators - conditionally
    String IF = "if";
    String THEN = "then";
    String ELSE = "else";
    String DEPENDENT_SCHEMAS = "dependentSchemas";

    // vocabulary: applicators - sub-schema array
    String ITEMS = "items";
    String PREFIX_ITEMS = "prefixItems";
    String ADDITIONAL_ITEMS = "additionalItems";
    String UNEVALUATED_ITEMS = "unevaluatedItems";
    String CONTAINS = "contains";

    // vocabulary: applicators sub-schema object
    String PROPERTIES = "properties";
    String PATTERN_PROPERTIES = "patternProperties";
    String ADDITIONAL_PROPERTIES = "additionalProperties";
    String UNEVALUATED_PROPERTIES = "unevaluatedProperties";
    String PROPERTY_NAMES = "propertyNames";

    // vocabulary: validation - any
    String CONST = "const";
    String ENUM = "enum";
    String TYPE = "type";

    // vocabulary: validation - numeric (number & integer)
    String MULTIPLE_OF = "multipleOf";
    String MAXIMUM = "maximum";
    String EXCLUSIVE_MAXIMUM = "exclusiveMaximum";
    String MINIMUM = "minimum";
    String EXCLUSIVE_MINIMUM = "exclusiveMinimum";

    // vocabulary: validation - strings
    String MAX_LENGTH = "maxLength";
    String MIN_LENGTH = "minLength";
    String PATTERN = "pattern";

    // vocabulary: validation - arrays
    String MAX_ITEMS = "maxItems";
    String MIN_ITEMS = "minItems";
    String UNIQUE_ITEMS = "uniqueItems";
    String MAX_CONTAINS = "maxContains";
    String MIN_CONTAINS = "minContains";

    // vocabulary: validation - objects
    String MAX_PROPERTIES = "maxProperties";
    String MIN_PROPERTIES = "minProperties";
    String REQUIRED = "required";
    String DEPENDENT_REQUIRED = "dependentRequired";

    // vocabulary: format
    String FORMAT = "format";

    // vocabulary: content
    String CONTENT_MEDIA_TYPE = "contentMediaType";
    String CONTENT_ENCODING = "contentEncoding";
    String CONTENT_SCHEMA = "contentSchema";

    // vocabulary: meta data
    String TITLE = "title";
    String DESCRIPTION = "description";
    String DEFAULT = "default";
    String DEPRECATED = "deprecated";
    String READ_ONLY = "readOnly";
    String WRITE_ONLY = "writeOnly";
    String EXAMPLES = "examples";

    // vocabulary: hyper-schema
    String BASE = "base";
    String LINKS = "links";

    // other
    String DEPENDENCIES = "dependencies";
}
