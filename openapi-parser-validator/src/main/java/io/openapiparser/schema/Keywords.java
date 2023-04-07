/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public interface Keywords {
    // misc
    String HASH = "#";

    // core
    String ID = "$id";
    String ID4 = "id";
    String SCHEMA = "$schema";

    String REF = "$ref";
    String DYNAMIC_REF = "$dynamicRef";
    String RECURSIVE_ANCHOR = "$recursiveAnchor";
    String RECURSIVE_REF = "$recursiveRef";
    String VOCABULARY = "$vocabulary";

    // applicators
    String ALL_OF = "allOf";
    String ANY_OF = "anyOf";
    String ONE_OF = "oneOf";
    String NOT = "not";

    // meta data
    String TITLE = "title";
    String DEFAULT = "default";
    String DESCRIPTION = "description";
    String EXAMPLES = "examples";

    // format
    String FORMAT = "format";

    // validation: number
    String MULTIPLE_OF = "multipleOf";
    String MAXIMUM = "maximum";
    String MINIMUM = "minimum";
    String EXCLUSIVE_MAXIMUM = "exclusiveMaximum";
    String EXCLUSIVE_MINIMUM = "exclusiveMinimum";

    // validation: strings
    String MAX_LENGTH = "maxLength";
    String MIN_LENGTH = "minLength";
    String PATTERN = "pattern";

    // validation: arrays
    String ADDITIONAL_ITEMS = "additionalItems";
    String CONTAINS = "contains";
    String ITEMS = "items";
    String MAX_ITEMS = "maxItems";
    String MIN_ITEMS = "minItems";
    String UNIQUE_ITEMS = "uniqueItems";

    // validation: objects
    String ADDITIONAL_PROPERTIES = "additionalProperties";
    String DEPENDENCIES = "dependencies";
    String MAX_PROPERTIES = "maxProperties";
    String MIN_PROPERTIES = "minProperties";
    String PATTERN_PROPERTIES = "patternProperties";
    String PROPERTIES = "properties";
    String PROPERTY_NAMES = "propertyNames";
    String REQUIRED = "required";

    // validation: any
    String CONST = "const";
    String DEFINITIONS = "definitions";
    String ENUM = "enum";
    String TYPE = "type";

    // todo
    String DEPENDENT_REQUIRED = "dependentRequired";
}
