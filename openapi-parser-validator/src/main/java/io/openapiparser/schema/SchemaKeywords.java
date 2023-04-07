/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// VocabularyFactory
public class SchemaKeywords {
    public static final SchemaKeywords draft201909 = new SchemaKeywords (initDraft201909 ());
    public static final SchemaKeywords draft7 = new SchemaKeywords (Vocabularies7.getKeywords ());
    public static final SchemaKeywords draft6 = new SchemaKeywords (Vocabularies6.getKeywords ());
    public static final SchemaKeywords draft4 = new SchemaKeywords (Vocabularies4.getKeywords ());

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
        Map<String, Keyword> keywords = new HashMap<> ();

        keywords.putAll (Vocabularies201909.getKeywordsApplicator ());
        keywords.putAll (Vocabularies201909.getKeywordsContent ());
        keywords.putAll (Vocabularies201909.getKeywordsCore ());
        keywords.putAll (Vocabularies201909.getKeywordsFormat ());
        keywords.putAll (Vocabularies201909.getKeywordsMetaData ());
        keywords.putAll (Vocabularies201909.getKeywordsValidation ());

        // core
//        keywords.put ("$anchor", keyword (KeywordType.STRING));
//        keywords.put ("$defs", keyword (KeywordType.SCHEMA_MAP));
//        keywords.put ("dependentSchemas", keyword (KeywordType.SCHEMA_MAP));
//        keywords.put(DEPENDENT_REQUIRED, keyword (KeywordType.OBJECT));

        return Collections.unmodifiableMap (keywords);
    }
}
