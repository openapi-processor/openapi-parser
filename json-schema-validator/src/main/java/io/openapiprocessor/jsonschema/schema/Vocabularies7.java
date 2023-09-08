/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Vocabularies7 {

    public static final Vocabulary all = new Vocabulary(getKeywords ());

    private Vocabularies7() {}

    @SuppressWarnings("DuplicatedCode")
    static Map<String, Keyword> getKeywords () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (Keywords.COMMENT, Keyword.keyword (KeywordType.STRING));  // new
        keywords.put (Keywords.ID, Keyword.keyword (KeywordType.URI));
        keywords.put (Keywords.REF, Keyword.keyword (KeywordType.URI_REF));
        keywords.put (Keywords.SCHEMA, Keyword.keyword (KeywordType.URI));

        // validation: meta-data/annotations
        keywords.put (Keywords.DEFAULT, Keyword.keyword (KeywordType.ANY));
        keywords.put (Keywords.DEFINITIONS, Keyword.keyword (KeywordType.SCHEMA_MAP));
        keywords.put (Keywords.DESCRIPTION, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.EXAMPLES, Keyword.keyword (KeywordType.ARRAY));
        keywords.put (Keywords.TITLE, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.READ_ONLY, Keyword.keyword (KeywordType.BOOLEAN));  // new
        keywords.put (Keywords.WRITE_ONLY, Keyword.keyword (KeywordType.BOOLEAN));  // new

        // validation: applicators
        keywords.put (Keywords.ALL_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.ANY_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.IF, Keyword.keyword (KeywordType.SCHEMA));  // new
        keywords.put (Keywords.THEN, Keyword.keyword (KeywordType.SCHEMA)); // new
        keywords.put (Keywords.ELSE, Keyword.keyword (KeywordType.SCHEMA)); // new
        keywords.put (Keywords.ONE_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.NOT, Keyword.keyword (KeywordType.SCHEMA));

        // validation: format
        keywords.put (Keywords.FORMAT, Keyword.keyword (KeywordType.STRING));

        // validation: numbers
        keywords.put (Keywords.EXCLUSIVE_MAXIMUM, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.EXCLUSIVE_MINIMUM, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MAXIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MINIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MULTIPLE_OF, Keyword.keyword (KeywordType.INTEGER));

        // validation: strings
        keywords.put (Keywords.MAX_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.PATTERN, Keyword.keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (Keywords.ADDITIONAL_ITEMS, Keyword.keyword (KeywordType.SCHEMA, KeywordType.BOOLEAN));
        keywords.put (Keywords.CONTAINS, Keyword.keyword (KeywordType.SCHEMA)); // new
        keywords.put (Keywords.ITEMS, Keyword.keyword (KeywordType.SCHEMA, KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.MAX_ITEMS, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_ITEMS, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.UNIQUE_ITEMS, Keyword.keyword (KeywordType.BOOLEAN));

        // validation: objects
        keywords.put (Keywords.ADDITIONAL_PROPERTIES, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.DEPENDENCIES, Keyword.keyword (KeywordType.SCHEMA, KeywordType.ARRAY));
        keywords.put (Keywords.MAX_PROPERTIES, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_PROPERTIES, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.PATTERN_PROPERTIES, Keyword.keyword (KeywordType.SCHEMA_MAP));
        keywords.put (Keywords.PROPERTIES, Keyword.keyword (KeywordType.SCHEMA_MAP));
        keywords.put (Keywords.PROPERTY_NAMES, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.REQUIRED, Keyword.keyword (KeywordType.ARRAY));

        // validation: non json data
        keywords.put (Keywords.CONTENT_MEDIA_TYPE, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.CONTENT_ENCODING, Keyword.keyword (KeywordType.STRING));

        // validation: any
        keywords.put (Keywords.CONST, Keyword.keyword (KeywordType.ANY));
        keywords.put (Keywords.ENUM, Keyword.keyword (KeywordType.ARRAY));
        keywords.put (Keywords.TYPE, Keyword.keyword (KeywordType.STRING, KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }
}
