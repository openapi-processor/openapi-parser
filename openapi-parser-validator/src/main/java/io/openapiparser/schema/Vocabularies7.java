/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.openapiparser.schema.Keyword.keyword;
import static io.openapiparser.schema.Keywords.*;


public class Vocabularies7 {

    public static final Vocabulary all = new Vocabulary(getKeywords ());

    @SuppressWarnings("DuplicatedCode")
    static Map<String, Keyword> getKeywords () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (COMMENT, keyword (KeywordType.STRING));  // new
        keywords.put (ID, keyword (KeywordType.URI));
        keywords.put (REF, keyword (KeywordType.URI_REF));
        keywords.put (SCHEMA, keyword (KeywordType.URI));

        // validation: meta-data/annotations
        keywords.put (DEFAULT, keyword (KeywordType.ANY));
        keywords.put (DEFINITIONS, keyword (KeywordType.SCHEMA_MAP));
        keywords.put (DESCRIPTION, keyword (KeywordType.STRING));
        keywords.put (EXAMPLES, keyword (KeywordType.ARRAY));
        keywords.put (TITLE, keyword (KeywordType.STRING));
        keywords.put (READ_ONLY, keyword (KeywordType.BOOLEAN));  // new
        keywords.put (WRITE_ONLY, keyword (KeywordType.BOOLEAN));  // new

        // validation: applicators
        keywords.put (ALL_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ANY_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (IF, keyword (KeywordType.SCHEMA));  // new
        keywords.put (THEN, keyword (KeywordType.SCHEMA)); // new
        keywords.put (ELSE, keyword (KeywordType.SCHEMA)); // new
        keywords.put (ONE_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (NOT, keyword (KeywordType.SCHEMA));

        // validation: format
        keywords.put (FORMAT, keyword (KeywordType.STRING));

        // validation: numbers
        keywords.put (EXCLUSIVE_MAXIMUM, keyword (KeywordType.NUMBER));
        keywords.put (EXCLUSIVE_MINIMUM, keyword (KeywordType.NUMBER));
        keywords.put (MAXIMUM, keyword (KeywordType.INTEGER));
        keywords.put (MINIMUM, keyword (KeywordType.INTEGER));
        keywords.put (MULTIPLE_OF, keyword (KeywordType.INTEGER));

        // validation: strings
        keywords.put (MAX_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (MIN_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (PATTERN, keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (ADDITIONAL_ITEMS, keyword (KeywordType.SCHEMA, KeywordType.BOOLEAN));
        keywords.put (CONTAINS, keyword (KeywordType.SCHEMA)); // new
        keywords.put (ITEMS, keyword (KeywordType.SCHEMA, KeywordType.SCHEMA_ARRAY));
        keywords.put (MAX_ITEMS, keyword (KeywordType.NUMBER));
        keywords.put (MIN_ITEMS, keyword (KeywordType.NUMBER));
        keywords.put (UNIQUE_ITEMS, keyword (KeywordType.BOOLEAN));

        // validation: objects
        keywords.put (ADDITIONAL_PROPERTIES, keyword (KeywordType.SCHEMA));
        keywords.put (DEPENDENCIES, keyword (KeywordType.SCHEMA, KeywordType.ARRAY));
        keywords.put (MAX_PROPERTIES, keyword (KeywordType.NUMBER));
        keywords.put (MIN_PROPERTIES, keyword (KeywordType.NUMBER));
        keywords.put (PATTERN_PROPERTIES, keyword (KeywordType.SCHEMA_MAP));
        keywords.put (PROPERTIES, keyword (KeywordType.SCHEMA_MAP));
        keywords.put (PROPERTY_NAMES, keyword (KeywordType.SCHEMA));
        keywords.put (REQUIRED, keyword (KeywordType.ARRAY));

        // validation: non json data
        keywords.put (CONTENT_MEDIA_TYPE, keyword (KeywordType.STRING));
        keywords.put (CONTENT_ENCODING, keyword (KeywordType.STRING));

        // validation: any
        keywords.put (CONST, keyword (KeywordType.ANY));
        keywords.put (ENUM, keyword (KeywordType.ARRAY));
        keywords.put (TYPE, keyword (KeywordType.STRING, KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }
}
