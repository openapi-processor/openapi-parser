/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public enum SchemaVersion {

    Draft201909 (
        new SchemaResource (
            "https://json-schema.org/draft/2019-09/schema",
            "/json-schema/draft-2019-09/schema.json"
        ),
        SchemaKeywords.draft201909,
        IdProvider.DRAFT201909,
        Arrays.asList (
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/applicator",
                "/json-schema/draft-2019-09/meta/applicator.json"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/content",
                "/json-schema/draft-2019-09/meta/content.json"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/core",
                "/json-schema/draft-2019-09/meta/core.json"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/format",
                "/json-schema/draft-2019-09/meta/format.json"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/meta-data",
                "/json-schema/draft-2019-09/meta/meta-data.json"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/vocab/validation",
                "/json-schema/draft-2019-09/meta/validation.json"
            )
        )
    ),

    Draft7 (
        new SchemaResource (
            "http://json-schema.org/draft-07/schema#",
            "/json-schema/draft-07/schema.json"
        ),
        SchemaKeywords.draft7,
        IdProvider.DRAFT7
    ),

    Draft6 (
        new SchemaResource (
            "http://json-schema.org/draft-06/schema#",
            "/json-schema/draft-06/schema.json"
        ),
        SchemaKeywords.draft6,
        IdProvider.DRAFT6
    ),

    Draft4 (
        new SchemaResource(
            "http://json-schema.org/draft-04/schema#",
            "/json-schema/draft-04/schema.json"
        ),
        SchemaKeywords.draft4,
        IdProvider.DRAFT4
    );



    private final SchemaResource schema;
    private final SchemaKeywords keywords;
    private final IdProvider idProvider;
    private final Set<SchemaResource> vocabularies = new HashSet<> ();

    SchemaVersion(
        SchemaResource schema,
        SchemaKeywords keywords,
        IdProvider idProvider
    ) {
        this.schema = schema;
        this.keywords = keywords;
        this.idProvider = idProvider;
    }

    SchemaVersion(
        SchemaResource schema,
        SchemaKeywords keywords,
        IdProvider idProvider,
        Collection<SchemaResource> vocabularies
    ) {
        this.schema = schema;
        this.keywords = keywords;
        this.idProvider = idProvider;
        this.vocabularies.addAll (vocabularies);
    }

    public static SchemaVersion getLatest () {
        return Draft201909;
    }

    /**
     * try to detect schema version. If {@code schemaUri} does not represent a known json schema
     * draft it returns null.
     *
     * @param schemaUri a meta schema uri
     * @return the detected schema version or null
     */
    public static @Nullable SchemaVersion getVersion (URI schemaUri) {
        if (SchemaVersion.Draft201909.getSchemaUri ().equals (schemaUri)) {
            return Draft201909;
        }

        if (SchemaVersion.Draft7.getSchemaUri ().equals (schemaUri)) {
            return Draft7;
        }

        if (SchemaVersion.Draft6.getSchemaUri ().equals (schemaUri)) {
            return Draft6;
        }

        if (SchemaVersion.Draft4.getSchemaUri ().equals (schemaUri)) {
            return Draft4;
        }

        return null;
    }

    /**
     * try to detect schema version. If {@code scope} does not represent a known json schema draft
     * it returns the fallback schema version.
     *
     * @param scope current scope
     * @param fallback fallback version
     * @return the detected schema version or the latest version
     */
    public static SchemaVersion getVersion (URI scope, SchemaVersion fallback) {
        if (SchemaVersion.Draft4.getSchemaUri ().equals (scope)) {
            return Draft4;
        }

        return fallback;
    }

    public URI getSchemaUri () {
        return schema.getUri ();
    }

    SchemaResource getSchemaResource () {
        return schema;
    }

    Set<SchemaResource> getVocabularyResources () {
        return vocabularies;
    }

    public @Nullable Keyword getKeyword (String name) {
        return keywords.getKeyword (name);
    }

    public IdProvider getIdProvider () {
        return idProvider;
    }

    public boolean validatesRefSiblings () {
        return isLaterOrEqualTo201909 ();
    }

    public boolean isLaterOrEqualTo201909 () {
        return compareTo (Draft201909) <= 0;
    }

    public boolean isBefore201909 () {
        return compareTo (Draft201909) < 0;
    }

    public boolean isApplicatorVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/applicator");
    }

    public boolean isContentVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/content");
    }

    public boolean isCoreVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/core");
    }

    public boolean isFormatVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/format");
    }

    public boolean isMetaDataVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/meta-data");
    }

    public boolean isValidationVocabulary (URI candidate) {
        return isVocabulary (candidate, "/vocab/validation");
    }

    private boolean isVocabulary (URI candidate, String vocabulary) {
        String parentUri = getSchemaParentUri ();
        String candidateUri = candidate.toString ();
        boolean matchParent = candidateUri.startsWith (parentUri);
        boolean matchCore = candidateUri.endsWith (vocabulary);
        return matchParent && matchCore;
    }

    private String getSchemaParentUri () {
        String uri = getSchemaUri ().toString ();
        return uri.substring (0, uri.lastIndexOf ("/"));
    }
}
