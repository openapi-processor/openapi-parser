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

@SuppressWarnings("DuplicatedCode")
public class Vocabularies201909 {

    public static final Vocabulary applicator = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/applicator", getKeywordsApplicator ());
    public static final Vocabulary content = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/content", getKeywordsContent ());
    public static final Vocabulary core = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/core", getKeywordsCore ());
    public static final Vocabulary format = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/format", getKeywordsFormat ());
    public static final Vocabulary metadata = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/meta-data", getKeywordsMetaData ());
    public static final Vocabulary validation = new Vocabulary (
        "https://json-schema.org/draft/2019-09/vocab/validation", getKeywordsValidation ());

    static Map<String, Keyword> getKeywordsCore () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (ID, keyword (KeywordType.URI));
        keywords.put (SCHEMA, keyword (KeywordType.URI));
        keywords.put (ANCHOR, keyword (KeywordType.STRING)); // new
        keywords.put (REF, keyword (KeywordType.URI_REF));
        keywords.put (RECURSIVE_REF, keyword (KeywordType.URI_REF));  // new
        keywords.put (RECURSIVE_ANCHOR, keyword (KeywordType.BOOLEAN));  // new
        keywords.put (VOCABULARY, keyword (KeywordType.OBJECT));  // new
        keywords.put (COMMENT, keyword (KeywordType.STRING));
        keywords.put (DEFS, keyword (KeywordType.SCHEMA_MAP)); // renamed, was definitions

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsApplicator () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // applicator boolean
        keywords.put (ALL_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ANY_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ONE_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (NOT, keyword (KeywordType.SCHEMA));

        // applicator conditionally
        keywords.put (IF, keyword (KeywordType.SCHEMA));
        keywords.put (THEN, keyword (KeywordType.SCHEMA));
        keywords.put (ELSE, keyword (KeywordType.SCHEMA));
        keywords.put (DEPENDENT_SCHEMAS, keyword (KeywordType.SCHEMA_MAP));

        // applicator sub-schema array
        keywords.put (ITEMS, keyword (KeywordType.SCHEMA, KeywordType.SCHEMA_ARRAY));
        keywords.put (ADDITIONAL_ITEMS, keyword (KeywordType.SCHEMA, KeywordType.BOOLEAN));
        keywords.put (UNEVALUATED_ITEMS, keyword (KeywordType.SCHEMA)); // new
        keywords.put (CONTAINS, keyword (KeywordType.SCHEMA));

        // applicator sub-schema object
        keywords.put (PROPERTIES, keyword (KeywordType.SCHEMA_MAP));
        keywords.put (PATTERN_PROPERTIES, keyword (KeywordType.SCHEMA_MAP));
        keywords.put (ADDITIONAL_PROPERTIES, keyword (KeywordType.SCHEMA));
        keywords.put (UNEVALUATED_PROPERTIES, keyword (KeywordType.SCHEMA)); // new
        keywords.put (PROPERTY_NAMES, keyword (KeywordType.SCHEMA));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsValidation () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // validation: any
        keywords.put (TYPE, keyword (KeywordType.STRING, KeywordType.ARRAY));
        keywords.put (ENUM, keyword (KeywordType.ARRAY));
        keywords.put (CONST, keyword (KeywordType.ANY));

        // validation: numbers
        keywords.put (MULTIPLE_OF, keyword (KeywordType.INTEGER));
        keywords.put (MAXIMUM, keyword (KeywordType.INTEGER));
        keywords.put (EXCLUSIVE_MAXIMUM, keyword (KeywordType.NUMBER));
        keywords.put (MINIMUM, keyword (KeywordType.INTEGER));
        keywords.put (EXCLUSIVE_MINIMUM, keyword (KeywordType.NUMBER));

        // validation: strings
        keywords.put (MAX_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (MIN_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (PATTERN, keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (MAX_ITEMS, keyword (KeywordType.NUMBER));
        keywords.put (MIN_ITEMS, keyword (KeywordType.NUMBER));
        keywords.put (UNIQUE_ITEMS, keyword (KeywordType.BOOLEAN));
        keywords.put (MAX_CONTAINS, keyword (KeywordType.INTEGER));
        keywords.put (MIN_CONTAINS, keyword (KeywordType.INTEGER));

        // validation: objects
        keywords.put (MAX_PROPERTIES, keyword (KeywordType.NUMBER));
        keywords.put (MIN_PROPERTIES, keyword (KeywordType.NUMBER));
        keywords.put (REQUIRED, keyword (KeywordType.ARRAY));
        keywords.put (DEPENDENT_REQUIRED, keyword (KeywordType.OBJECT));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsFormat () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // format
        keywords.put (FORMAT, keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsContent () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // content
        keywords.put (CONTENT_MEDIA_TYPE, keyword (KeywordType.STRING));
        keywords.put (CONTENT_ENCODING, keyword (KeywordType.STRING));
        keywords.put (CONTENT_SCHEMA, keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsMetaData () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // meta-data
        keywords.put (TITLE, keyword (KeywordType.STRING));
        keywords.put (DESCRIPTION, keyword (KeywordType.STRING));
        keywords.put (DEFAULT, keyword (KeywordType.ANY));
        keywords.put (DEPRECATED, keyword (KeywordType.BOOLEAN));
        keywords.put (READ_ONLY, keyword (KeywordType.BOOLEAN));
        keywords.put (WRITE_ONLY, keyword (KeywordType.BOOLEAN));
        keywords.put (EXAMPLES, keyword (KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }
}
