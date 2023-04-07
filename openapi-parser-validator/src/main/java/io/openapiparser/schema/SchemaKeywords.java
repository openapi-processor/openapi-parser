/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.schema.Keyword.*;
import static io.openapiparser.schema.Keywords.*;

// VocabularyFactory
public class SchemaKeywords {
    public static final SchemaKeywords draft201909 = new SchemaKeywords (initDraft201909 ());
    public static final SchemaKeywords draft7 = new SchemaKeywords (getKeywordsDraft7 ());
    public static final SchemaKeywords draft6 = new SchemaKeywords (getKeywordsDraft6 ());
    public static final SchemaKeywords draft4 = new SchemaKeywords (getKeywordsDraft4 ());

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

    public static final Vocabulary draft7All = new Vocabulary(getKeywordsDraft7 ());
    public static final Vocabulary draft6All = new Vocabulary(getKeywordsDraft6 ());
    public static final Vocabulary draft4All = new Vocabulary(getKeywordsDraft4 ());

    private final Map<String, Keyword> keywords;

    public SchemaKeywords (Map<String, Keyword> keywords) {
        this.keywords = keywords;
    }

    public boolean isKeyword (String candidate) {
        return keywords.containsKey (candidate);
    }

    public @Nullable Keyword getKeyword (String keyword) {
        return keywords.get (keyword);
    }

    public boolean isNavigable (String keyword) {
        Keyword match = keywords.get (keyword);
        if (match == null)
            return false;

        return match.isNavigable ();
    }

    private static Map<String, Keyword> initDraft201909 () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft7Old ());

        // core
        keywords.put ("$anchor", keyword (KeywordType.STRING));
        keywords.put ("$defs", keyword (KeywordType.SCHEMA_MAP));

        keywords.put ("dependentSchemas", keyword (KeywordType.SCHEMA_MAP));
        keywords.put(DEPENDENT_REQUIRED, keyword (KeywordType.OBJECT));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> getKeywordsCore () {
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

    private static Map<String, Keyword> getKeywordsApplicator () {
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

    private static Map<String, Keyword> getKeywordsValidation () {
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

    private static Map<String, Keyword> getKeywordsFormat () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // format
        keywords.put (FORMAT, keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> getKeywordsContent () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // content
        keywords.put (CONTENT_MEDIA_TYPE, keyword (KeywordType.STRING));
        keywords.put (CONTENT_ENCODING, keyword (KeywordType.STRING));
        keywords.put (CONTENT_SCHEMA, keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> getKeywordsMetaData () {
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

    private static Map<String, Keyword> getKeywordsDraft7 () {
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

    private static Map<String, Keyword> getKeywordsDraft6 () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (ID, keyword (KeywordType.URI));  // replaces ID4
        keywords.put (REF, keyword (KeywordType.URI_REF));
        keywords.put (SCHEMA, keyword (KeywordType.URI));

        // validation: applicators
        keywords.put (ALL_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ANY_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ONE_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (NOT, keyword (KeywordType.SCHEMA));

        // validation: meta-data
        keywords.put (DEFINITIONS, keyword (KeywordType.SCHEMA_MAP)); // moved
        keywords.put (EXAMPLES, keyword (KeywordType.ARRAY)); // new
        keywords.put (TITLE, keyword (KeywordType.STRING));
        keywords.put (DESCRIPTION, keyword (KeywordType.STRING));
        keywords.put (DEFAULT, keyword (KeywordType.ANY));

        // validation: format
        keywords.put (FORMAT, keyword (KeywordType.STRING));

        // validation: numbers
        keywords.put (EXCLUSIVE_MAXIMUM, keyword (KeywordType.NUMBER));  // modified
        keywords.put (EXCLUSIVE_MINIMUM, keyword (KeywordType.NUMBER));  // modified
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
        keywords.put (PROPERTY_NAMES, keyword (KeywordType.SCHEMA));  // new
        keywords.put (REQUIRED, keyword (KeywordType.ARRAY));

        // validation: any
        keywords.put (CONST, keyword (KeywordType.ANY));  // new
        keywords.put (ENUM, keyword (KeywordType.ARRAY));
        keywords.put (TYPE, keyword (KeywordType.STRING, KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> getKeywordsDraft4 () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put (ID4, keyword (KeywordType.URI));
        keywords.put (REF, keyword (KeywordType.URI_REF));
        keywords.put (SCHEMA, keyword (KeywordType.URI));

        // applicators
        keywords.put (ALL_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ANY_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (ONE_OF, keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put (NOT, keyword (KeywordType.SCHEMA));

        // meta-data
        keywords.put (TITLE, keyword (KeywordType.STRING));
        keywords.put (DESCRIPTION, keyword (KeywordType.STRING));
        keywords.put (DEFAULT, keyword (KeywordType.ANY));

        // format
        keywords.put (FORMAT, keyword (KeywordType.STRING));

        // validation: numbers
        keywords.put (EXCLUSIVE_MAXIMUM, keyword (KeywordType.BOOLEAN));
        keywords.put (EXCLUSIVE_MINIMUM, keyword (KeywordType.BOOLEAN));
        keywords.put (MAXIMUM, keyword (KeywordType.INTEGER));
        keywords.put (MINIMUM, keyword (KeywordType.INTEGER));
        keywords.put (MULTIPLE_OF, keyword (KeywordType.INTEGER));

        // validation: strings
        keywords.put (MAX_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (MIN_LENGTH, keyword (KeywordType.NUMBER));
        keywords.put (PATTERN, keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put (ADDITIONAL_ITEMS, keyword (KeywordType.SCHEMA, KeywordType.BOOLEAN));
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
        keywords.put (REQUIRED, keyword (KeywordType.ARRAY));

        // validation: any
        keywords.put (ENUM, keyword (KeywordType.ARRAY));
        keywords.put (TYPE, keyword (KeywordType.STRING, KeywordType.ARRAY));
        keywords.put (DEFINITIONS, keyword (KeywordType.SCHEMA_MAP));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> initDraft7Old () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft6Old ());

        // core
        keywords.put ("$comment", keyword (KeywordType.STRING));

        // validation: sub schemas
        keywords.put ("if", keyword (KeywordType.SCHEMA));
        keywords.put ("then", keyword (KeywordType.SCHEMA));
        keywords.put ("else", keyword (KeywordType.SCHEMA));

        // validation: annotations
        keywords.put ("readOnly", keyword (KeywordType.BOOLEAN));
        keywords.put ("writeOnly", keyword (KeywordType.BOOLEAN));

        // validation: non json data
        keywords.put ("contentMediaType", keyword (KeywordType.STRING));
        keywords.put ("contentEncoding", keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }

    @Deprecated
    private static Map<String, Keyword> initDraft6Old () {
        Map<String, Keyword> keywords = new HashMap<> (getKeywordsDraft4 ());

        // core
        keywords.put ("$id", keyword (KeywordType.URI));
        // delete id

        // validation: numbers
        keywords.put ("exclusiveMaximum", keyword (KeywordType.NUMBER));
        keywords.put ("exclusiveMinimum", keyword (KeywordType.NUMBER));

        // validation: arrays
        keywords.put ("contains", keyword (KeywordType.SCHEMA));

        // validation: objects
        keywords.put ("propertyNames", keyword (KeywordType.SCHEMA));

        // validation: any
        keywords.put ("const", keyword (KeywordType.ANY));

        // validation: meta data
        keywords.put ("examples", keyword (KeywordType.ARRAY));

        return Collections.unmodifiableMap (keywords);
    }

    @Deprecated
    private static Map<String, Keyword> initDraft4Old () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put ("$ref", keyword (KeywordType.URI_REF));
        keywords.put ("$schema", keyword (KeywordType.URI));
        keywords.put ("id", keyword (KeywordType.URI));

        // validation: numbers
        keywords.put ("multipleOf", keyword (KeywordType.NUMBER));
        keywords.put ("maximum", keyword (KeywordType.NUMBER));
        keywords.put ("minimum", keyword (KeywordType.NUMBER));
        keywords.put ("exclusiveMaximum", keyword (KeywordType.BOOLEAN));
        keywords.put ("exclusiveMinimum", keyword (KeywordType.BOOLEAN));

        // validation: strings
        keywords.put ("maxLength", keyword (KeywordType.INTEGER));
        keywords.put ("minLength", keyword (KeywordType.INTEGER));
        keywords.put ("pattern", keyword (KeywordType.STRING));

        // validation: arrays
        keywords.put ("additionalItems", keyword (KeywordType.SCHEMA));
        keywords.put ("items", keyword (KeywordType.SCHEMA, KeywordType.SCHEMA_ARRAY));
        keywords.put ("maxItems", keyword (KeywordType.INTEGER));
        keywords.put ("minItems", keyword (KeywordType.INTEGER));
        keywords.put ("uniqueItems", keyword (KeywordType.BOOLEAN));

        // validation: objects
        keywords.put ("maxProperties", keyword (KeywordType.INTEGER));
        keywords.put ("minProperties", keyword (KeywordType.INTEGER));
        keywords.put ("required", keyword (KeywordType.ARRAY));
        keywords.put ("additionalProperties", keyword (KeywordType.SCHEMA));
        keywords.put ("properties", keyword (KeywordType.SCHEMA_MAP));
        keywords.put ("patternProperties", keyword (KeywordType.SCHEMA_MAP));
        keywords.put ("dependencies", keyword (KeywordType.SCHEMA, KeywordType.ARRAY));

        // validation: any
        keywords.put ("enum", keyword (KeywordType.ARRAY));
        keywords.put ("type", keyword (KeywordType.STRING, KeywordType.ARRAY));
        keywords.put ("allOf", keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put ("anyOf", keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put ("oneOf", keyword (KeywordType.SCHEMA_ARRAY));
        keywords.put ("not", keyword (KeywordType.SCHEMA));
        keywords.put ("definitions", keyword (KeywordType.SCHEMA_MAP));

        // validation: meta data
        keywords.put ("title", keyword (KeywordType.STRING));
        keywords.put ("description", keyword (KeywordType.STRING));
        keywords.put ("default", keyword (KeywordType.ANY));

        // validation: format
        keywords.put ("format", keyword (KeywordType.STRING));

        return Collections.unmodifiableMap (keywords);
    }
}
