/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.openapiprocessor.jsonschema.schema.Keyword.keyword;

@SuppressWarnings("DuplicatedCode")
public class Vocabularies202012 {

    public static final Vocabulary applicator = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/applicator", getKeywordsApplicator ());
    public static final Vocabulary content = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/content", getKeywordsContent ());
    public static final Vocabulary core = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/core", getKeywordsCore ());
    public static final Vocabulary formatAnnotation = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/format-annotation", getKeywordsFormatAnnotation ());
    public static final Vocabulary formatAssertion = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/format-assertion", getKeywordsFormatAssertion ());
    public static final Vocabulary hyperSchema = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/hyper-schema", getKeywordsHyperSchema ());
    public static final Vocabulary metadata = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/meta-data", getKeywordsMetaData ());
    public static final Vocabulary unevaluated = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/unevaluated", getKeywordsUnevaluated ());
    public static final Vocabulary validation = new Vocabulary (
        "https://json-schema.org/draft/2020-12/vocab/validation", getKeywordsValidation ());

    private Vocabularies202012() {}

    static Map<String, Keyword> getKeywordsCore () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (Keywords.ID, Keyword.keyword (KeywordType.URI));
        keywords.put (Keywords.SCHEMA, Keyword.keyword (KeywordType.URI));
        keywords.put (Keywords.REF, Keyword.keyword (KeywordType.URI_REF));
        keywords.put (Keywords.ANCHOR, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DYNAMIC_REF, Keyword.keyword (KeywordType.URI_REF));
        keywords.put (Keywords.DYNAMIC_ANCHOR, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.VOCABULARY, Keyword.keyword (KeywordType.OBJECT));
        keywords.put (Keywords.COMMENT, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DEFS, Keyword.keyword (KeywordType.SCHEMA_MAP));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsApplicator () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // applicator boolean
        keywords.put (Keywords.ALL_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.ANY_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.ONE_OF, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.NOT, Keyword.keyword (KeywordType.SCHEMA));

        // applicator conditionally
        keywords.put (Keywords.IF, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.THEN, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.ELSE, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.DEPENDENT_SCHEMAS, Keyword.keyword (KeywordType.SCHEMA_MAP));

        // applicator sub-schema array
        keywords.put (Keywords.ITEMS, keyword (KeywordType.SCHEMA, KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.PREFIX_ITEMS, Keyword.keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (Keywords.CONTAINS, Keyword.keyword (KeywordType.SCHEMA));

        // applicator sub-schema object
        keywords.put (Keywords.PROPERTIES, Keyword.keyword (KeywordType.SCHEMA_MAP));
        keywords.put (Keywords.PATTERN_PROPERTIES, Keyword.keyword (KeywordType.SCHEMA_MAP));
        keywords.put (Keywords.ADDITIONAL_PROPERTIES, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.PROPERTY_NAMES, Keyword.keyword (KeywordType.SCHEMA));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsValidation () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // validation: any
        keywords.put (Keywords.TYPE, keyword (KeywordType.STRING, KeywordType.ARRAY));
        keywords.put (Keywords.CONST, Keyword.keyword (KeywordType.ANY));
        keywords.put (Keywords.ENUM, Keyword.keyword (KeywordType.ARRAY));

        // validation: numbers
        keywords.put (Keywords.MULTIPLE_OF, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MAXIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.EXCLUSIVE_MAXIMUM, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MINIMUM, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.EXCLUSIVE_MINIMUM, Keyword.keyword (KeywordType.NUMBER));

        // validation: strings
        keywords.put (Keywords.MAX_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_LENGTH, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.PATTERN, Keyword.keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (Keywords.MAX_ITEMS, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_ITEMS, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.UNIQUE_ITEMS, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.MAX_CONTAINS, Keyword.keyword (KeywordType.INTEGER));
        keywords.put (Keywords.MIN_CONTAINS, Keyword.keyword (KeywordType.INTEGER));

        // validation: objects
        keywords.put (Keywords.MAX_PROPERTIES, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.MIN_PROPERTIES, Keyword.keyword (KeywordType.NUMBER));
        keywords.put (Keywords.REQUIRED, Keyword.keyword (KeywordType.ARRAY));
        keywords.put (Keywords.DEPENDENT_REQUIRED, Keyword.keyword (KeywordType.OBJECT));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsFormatAnnotation () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // format
        keywords.put (Keywords.FORMAT, Keyword.keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsFormatAssertion () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // format
        keywords.put (Keywords.FORMAT, Keyword.keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsHyperSchema () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // hyper-schema
        keywords.put (Keywords.BASE, Keyword.keyword (KeywordType.URI_TEMPLATE));
        keywords.put (Keywords.LINKS, Keyword.keyword (KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsContent () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // content
        keywords.put (Keywords.CONTENT_MEDIA_TYPE, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.CONTENT_ENCODING, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.CONTENT_SCHEMA, Keyword.keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsMetaData () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // meta-data
        keywords.put (Keywords.TITLE, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DESCRIPTION, Keyword.keyword (KeywordType.STRING));
        keywords.put (Keywords.DEFAULT, Keyword.keyword (KeywordType.ANY));
        keywords.put (Keywords.DEPRECATED, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.READ_ONLY, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.WRITE_ONLY, Keyword.keyword (KeywordType.BOOLEAN));
        keywords.put (Keywords.EXAMPLES, Keyword.keyword (KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }

    static Map<String, Keyword> getKeywordsUnevaluated () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // unevaluated
        keywords.put (Keywords.UNEVALUATED_ITEMS, Keyword.keyword (KeywordType.SCHEMA));
        keywords.put (Keywords.UNEVALUATED_PROPERTIES, Keyword.keyword (KeywordType.SCHEMA));

        return Collections.unmodifiableMap (keywords);
    }
}
