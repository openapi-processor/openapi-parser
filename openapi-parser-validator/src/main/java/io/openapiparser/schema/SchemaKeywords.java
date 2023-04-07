/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.schema.Keywords.*;

// VocabularyFactory
public class SchemaKeywords {
    public static final SchemaKeywords draft201909 = new SchemaKeywords (initDraft201909 ());
    public static final SchemaKeywords draft7 = new SchemaKeywords (initDraft7 ());
    public static final SchemaKeywords draft6 = new SchemaKeywords (initDraft6Old ());
    public static final SchemaKeywords draft4 = new SchemaKeywords (initDraft4Old ());

    public static final Vocabulary draft6V = new Vocabulary(initDraft6 ());
    public static final Vocabulary draft4V = new Vocabulary(initDraft4 ());

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
        Map<String, Keyword> keywords = new HashMap<> (initDraft7 ());

        // core
        keywords.put ("$anchor", keyword (KeywordType.STRING));
        keywords.put ("$defs", keyword (KeywordType.SCHEMA_MAP));

        keywords.put ("dependentSchemas", keyword(KeywordType.SCHEMA_MAP));
        keywords.put(DEPENDENT_REQUIRED, keyword(KeywordType.OBJECT));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> initDraft7 () {
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

    private static Map<String, Keyword> initDraft6 () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft4 ());

        // core
        keywords.remove (ID4);
        keywords.put (ID, keyword (KeywordType.URI));

        // meta-data
        keywords.put (EXAMPLES, keyword (KeywordType.ARRAY));

        // validation: numbers
        keywords.put (EXCLUSIVE_MAXIMUM, keyword(KeywordType.NUMBER));
        keywords.put (EXCLUSIVE_MINIMUM, keyword(KeywordType.NUMBER));

        // validation: arrays
        keywords.put (CONTAINS, keyword (KeywordType.SCHEMA));

        // validation: objects
        keywords.put (PROPERTY_NAMES, keyword (KeywordType.SCHEMA));

        // validation: any
        keywords.put (CONST, keyword (KeywordType.ANY));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> initDraft4 () {
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

    @Deprecated
    private static Map<String, Keyword> initDraft6Old () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft4 ());

        // core
        keywords.put ("$id", keyword (KeywordType.URI));
        // delete id

        // validation: numbers
        keywords.put ("exclusiveMaximum", keyword(KeywordType.NUMBER));
        keywords.put ("exclusiveMinimum", keyword(KeywordType.NUMBER));

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
        keywords.put ("multipleOf", keyword(KeywordType.NUMBER));
        keywords.put ("maximum", keyword(KeywordType.NUMBER));
        keywords.put ("minimum", keyword (KeywordType.NUMBER));
        keywords.put ("exclusiveMaximum", keyword(KeywordType.BOOLEAN));
        keywords.put ("exclusiveMinimum", keyword(KeywordType.BOOLEAN));

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


    private static Keyword stringOrArray () {
        return keyword (KeywordType.STRING, KeywordType.ARRAY);
    }

    private static Keyword schemaArray () {
        return keyword (KeywordType.SCHEMA_ARRAY);
    }

    private static Keyword keyword (KeywordType value, KeywordType... values) {
        return new Keyword (EnumSet.of (value, values));
    }
}
