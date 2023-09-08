/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Vocabularies4 {

    public static final Vocabulary all = new Vocabulary(getKeywords ());

    private Vocabularies4() {}

    @SuppressWarnings("DuplicatedCode")
    static Map<String, Keyword> getKeywords () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (Keywords.ID4, Keyword.keyword (KeywordType.URI));
        keywords.put (Keywords.REF, Keyword.keyword (KeywordType.URI_REF));
        keywords.put (Keywords.SCHEMA, Keyword.keyword (KeywordType.URI));

        // applicators
        keywords.put (Keywords.ALL_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.ANY_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.ONE_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.NOT, Keyword.keyword (KeywordType.SCHEMA));

        // meta-data
        keywords.put (Keywords.TITLE, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DESCRIPTION, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DEFAULT, Keyword.keyword (KeywordType.ANY));

        // format
        keywords.put (Keywords.FORMAT, Keyword.keyword (KeywordType.STRING));

        // validation: numbers
        keywords.put (Keywords.EXCLUSIVE_MAXIMUM, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.EXCLUSIVE_MINIMUM, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.MAXIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MINIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MULTIPLE_OF, Keyword.keyword (KeywordType.INTEGER));

        // validation: strings
        keywords.put (Keywords.MAX_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.PATTERN, Keyword.keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (Keywords.ADDITIONAL_ITEMS, Keyword.keyword (KeywordType.SCHEMA, KeywordType.BOOLEAN));
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
        keywords.put (Keywords.REQUIRED, Keyword.keyword (KeywordType.ARRAY));

        // validation: any
        keywords.put (Keywords.ENUM, Keyword.keyword (KeywordType.ARRAY));
        keywords.put (Keywords.TYPE, Keyword.keyword (KeywordType.STRING, KeywordType.ARRAY));
        keywords.put (Keywords.DEFINITIONS, Keyword.keyword (KeywordType.SCHEMA_MAP));

        return Collections.unmodifiableMap (keywords);
    }
}
