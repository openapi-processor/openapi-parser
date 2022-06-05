/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.schema.KeywordType.*;

public class SchemaKeywords {
    public static final SchemaKeywords draft7 = new SchemaKeywords (initDraft7 ());
    public static final SchemaKeywords draft6 = new SchemaKeywords (initDraft6 ());
    public static final SchemaKeywords draft4 = new SchemaKeywords (initDraft4 ());

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

    public boolean isNavigatable (String keyword) {
        Keyword match = keywords.get (keyword);
        if (match == null)
            return false;

        return match.isNavigatable ();
    }

    private static Map<String, Keyword> initDraft7 () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft6 ());

        // core
        keywords.put ("$comment", keyword (STRING));

        // validation: sub schemas
        keywords.put ("if", keyword (SCHEMA));
        keywords.put ("then", keyword (SCHEMA));
        keywords.put ("else", keyword (SCHEMA));

        // validation: annotations
        keywords.put ("readOnly", keyword (BOOLEAN));
        keywords.put ("writeOnly", keyword (BOOLEAN));

        // validation: non json data
        keywords.put ("contentMediaType", keyword (STRING));
        keywords.put ("contentEncoding", keyword (STRING));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> initDraft6 () {
        Map<String, Keyword> keywords = new HashMap<> (initDraft4 ());

        // core
        keywords.put ("$id", keyword (URI));

        // validation: numbers
        keywords.put ("exclusiveMaximum", keyword(NUMBER));
        keywords.put ("exclusiveMinimum", keyword(NUMBER));

        // validation: arrays
        keywords.put ("contains", keyword (SCHEMA));

        // validation: objects
        keywords.put ("propertyNames", keyword (SCHEMA));

        // validation: any
        keywords.put ("const", keyword (ANY));

        // validation: meta data
        keywords.put ("examples", keyword (ARRAY));

        return Collections.unmodifiableMap (keywords);
    }

    private static Map<String, Keyword> initDraft4 () {
        Map<String, Keyword> keywords = new HashMap<> ();

        // core
        keywords.put ("$ref", keyword (URI_REF));
        keywords.put ("$schema", keyword (URI));
        keywords.put ("id", keyword (URI));

        // validation: numbers
        keywords.put ("multipleOf", keyword(NUMBER));
        keywords.put ("maximum", keyword(NUMBER));
        keywords.put ("minimum", keyword (NUMBER));
        keywords.put ("exclusiveMaximum", keyword(BOOLEAN));
        keywords.put ("exclusiveMinimum", keyword(BOOLEAN));

        // validation: strings
        keywords.put ("maxLength", keyword (INTEGER));
        keywords.put ("minLength", keyword (INTEGER));
        keywords.put ("pattern", keyword (STRING));

        // validation: arrays
        keywords.put ("additionalItems", keyword (SCHEMA));
        keywords.put ("items", keyword (SCHEMA, SCHEMA_ARRAY));
        keywords.put ("maxItems", keyword (INTEGER));
        keywords.put ("minItems", keyword (INTEGER));
        keywords.put ("uniqueItems", keyword (BOOLEAN));

        // validation: objects
        keywords.put ("maxProperties", keyword (INTEGER));
        keywords.put ("minProperties", keyword (INTEGER));
        keywords.put ("required", keyword (ARRAY));
        keywords.put ("additionalProperties", keyword (SCHEMA));
        keywords.put ("properties", keyword (SCHEMA_MAP));
        keywords.put ("patternProperties", keyword (SCHEMA_MAP));
        keywords.put ("dependencies", keyword (SCHEMA, ARRAY));

        // validation: any
        keywords.put ("enum", keyword (ARRAY));
        keywords.put ("type", keyword (STRING, ARRAY));
        keywords.put ("allOf", keyword (SCHEMA_ARRAY));
        keywords.put ("anyOf", keyword (SCHEMA_ARRAY));
        keywords.put ("oneOf", keyword (SCHEMA_ARRAY));
        keywords.put ("not", keyword (SCHEMA));
        keywords.put ("definitions", keyword (SCHEMA_MAP));

        // validation: meta data
        keywords.put ("title", keyword (STRING));
        keywords.put ("description", keyword (STRING));
        keywords.put ("default", keyword (ANY));

        // validation: format
        keywords.put ("format", keyword (STRING));

        return Collections.unmodifiableMap (keywords);
    }

    private static Keyword keyword (KeywordType value, KeywordType... values) {
        return new Keyword (EnumSet.of (value, values));
    }
}
