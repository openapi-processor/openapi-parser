/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class Keywords {
    // misc
    public static String HASH = "#";

    // vocabulary: core
    public static String ANCHOR = "$anchor";
    public static String COMMENT = "$comment";
    public static String DEFS = "$defs";
    public static String DEFINITIONS = "definitions";
    public static String ID = "$id";
    public static String ID4 = "id";
    public static String SCHEMA = "$schema";

    public static String REF = "$ref";
    public static String DYNAMIC_ANCHOR = "$dynamicAnchor";
    public static String DYNAMIC_REF = "$dynamicRef";
    public static String RECURSIVE_ANCHOR = "$recursiveAnchor";
    public static String RECURSIVE_REF = "$recursiveRef";
    public static String VOCABULARY = "$vocabulary";

    // vocabulary: applicators - boolean
    public static String ALL_OF = "allOf";
    public static String ANY_OF = "anyOf";
    public static String ONE_OF = "oneOf";
    public static String NOT = "not";

    // vocabulary: applicators - conditionally
    public static String IF = "if";
    public static String THEN = "then";
    public static String ELSE = "else";
    public static String DEPENDENT_SCHEMAS = "dependentSchemas";

    // vocabulary: applicators - sub-schema array
    public static String ITEMS = "items";
    public static String PREFIX_ITEMS = "prefixItems";
    public static String ADDITIONAL_ITEMS = "additionalItems";
    public static String UNEVALUATED_ITEMS = "unevaluatedItems";
    public static String CONTAINS = "contains";

    // vocabulary: applicators sub-schema object
    public static String PROPERTIES = "properties";
    public static String PATTERN_PROPERTIES = "patternProperties";
    public static String ADDITIONAL_PROPERTIES = "additionalProperties";
    public static String UNEVALUATED_PROPERTIES = "unevaluatedProperties";
    public static String PROPERTY_NAMES = "propertyNames";

    // vocabulary: validation - any
    public static String CONST = "const";
    public static String ENUM = "enum";
    public static String TYPE = "type";

    // vocabulary: validation - numeric (number & integer)
    public static String MULTIPLE_OF = "multipleOf";
    public static String MAXIMUM = "maximum";
    public static String EXCLUSIVE_MAXIMUM = "exclusiveMaximum";
    public static String MINIMUM = "minimum";
    public static String EXCLUSIVE_MINIMUM = "exclusiveMinimum";

    // vocabulary: validation - strings
    public static String MAX_LENGTH = "maxLength";
    public static String MIN_LENGTH = "minLength";
    public static String PATTERN = "pattern";

    // vocabulary: validation - arrays
    public static String MAX_ITEMS = "maxItems";
    public static String MIN_ITEMS = "minItems";
    public static String UNIQUE_ITEMS = "uniqueItems";
    public static String MAX_CONTAINS = "maxContains";
    public static String MIN_CONTAINS = "minContains";

    // vocabulary: validation - objects
    public static String MAX_PROPERTIES = "maxProperties";
    public static String MIN_PROPERTIES = "minProperties";
    public static String REQUIRED = "required";
    public static String DEPENDENT_REQUIRED = "dependentRequired";

    // vocabulary: format
    public static String FORMAT = "format";

    // vocabulary: content
    public static String CONTENT_MEDIA_TYPE = "contentMediaType";
    public static String CONTENT_ENCODING = "contentEncoding";
    public static String CONTENT_SCHEMA = "contentSchema";

    // vocabulary: meta data
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String DEFAULT = "default";
    public static String DEPRECATED = "deprecated";
    public static String READ_ONLY = "readOnly";
    public static String WRITE_ONLY = "writeOnly";
    public static String EXAMPLES = "examples";

    // vocabulary: hyper-schema
    public static String BASE = "base";
    public static String LINKS = "links";

    // other
    public static String DEPENDENCIES = "dependencies";

    private Keywords() {}
}
