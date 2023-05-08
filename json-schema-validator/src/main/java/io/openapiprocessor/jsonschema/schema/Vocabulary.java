/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

// todo interface VocabularySingle, VocabularyComposite ???
public class Vocabulary {
    private final String uri;
    private final Map<String, Keyword> keywords;

    public Vocabulary(Map<String, Keyword> keywords) {
        this.uri = "";
        this.keywords = keywords;
    }

    public Vocabulary(String uri, Map<String, Keyword> keywords) {
        this.uri = uri;
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
}
