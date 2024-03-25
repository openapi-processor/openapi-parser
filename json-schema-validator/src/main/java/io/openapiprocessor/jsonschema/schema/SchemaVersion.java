/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public enum SchemaVersion {

    DraftNext (
        new SchemaResource (
            "https://json-schema.org/draft/next/schema",
            "/json-schema.org/draft/next/schema"
        ),
        SchemaKeywords.draftNext,
        IdProvider.DRAFT201909,
        Arrays.asList (
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/applicator",
                "/json-schema.org/draft/next/meta/applicator"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/content",
                "/json-schema.org/draft/next/meta/content"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/core",
                "/json-schema.org/draft/next/meta/core"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/format-annotation",
                "/json-schema.org/draft/next/meta/format-annotation"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/format-assertion",
                "/json-schema.org/draft/next/meta/format-assertion"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/meta-data",
                "/json-schema.org/draft/next/meta/meta-data"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/unevaluated",
                "/json-schema.org/draft/next/meta/unevaluated"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/next/meta/validation",
                "/json-schema.org/draft/next/meta/validation"
            )
        )
    ),

    Draft202012 (
        new SchemaResource (
            "https://json-schema.org/draft/2020-12/schema",
            "/json-schema.org/draft/2020-12/schema"
        ),
        SchemaKeywords.draft202012,
        IdProvider.DRAFT201909,
        Arrays.asList (
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/applicator",
                "/json-schema.org/draft/2020-12/meta/applicator"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/content",
                "/json-schema.org/draft/2020-12/meta/content"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/core",
                "/json-schema.org/draft/2020-12/meta/core"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/format-annotation",
                "/json-schema.org/draft/2020-12/meta/format-annotation"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/format-assertion",
                "/json-schema.org/draft/2020-12/meta/format-assertion"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/hyper-schema",
                "/json-schema.org/draft/2020-12/meta/hyper-schema"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/meta-data",
                "/json-schema.org/draft/2020-12/meta/meta-data"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/unevaluated",
                "/json-schema.org/draft/2020-12/meta/unevaluated"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2020-12/meta/validation",
                "/json-schema.org/draft/2020-12/meta/validation"
            )
        )
    ),

    Draft201909 (
        new SchemaResource (
            "https://json-schema.org/draft/2019-09/schema",
            "/json-schema.org/draft/2019-09/schema"
        ),
        SchemaKeywords.draft201909,
        IdProvider.DRAFT201909,
        Arrays.asList (
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/applicator",
                "/json-schema.org/draft/2019-09/meta/applicator"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/content",
                "/json-schema.org/draft/2019-09/meta/content"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/core",
                "/json-schema.org/draft/2019-09/meta/core"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/format",
                "/json-schema.org/draft/2019-09/meta/format"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/meta-data",
                "/json-schema.org/draft/2019-09/meta/meta-data"
            ),
            new SchemaResource (
                "https://json-schema.org/draft/2019-09/meta/validation",
                "/json-schema.org/draft/2019-09/meta/validation"
            )
        )
    ),

    Draft7 (
        new SchemaResource (
            "http://json-schema.org/draft-07/schema#",
            "/json-schema.org/draft-07/schema"
        ),
        SchemaKeywords.draft7,
        IdProvider.DRAFT7
    ),

    Draft6 (
        new SchemaResource (
            "http://json-schema.org/draft-06/schema#",
            "/json-schema.org/draft-06/schema"
        ),
        SchemaKeywords.draft6,
        IdProvider.DRAFT6
    ),

    Draft4 (
        new SchemaResource(
            "http://json-schema.org/draft-04/schema#",
            "/json-schema.org/draft-04/schema"
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
        return Draft202012;
    }

    /**
     * try to detect schema version. If {@code schemaUri} does not represent a known json schema
     * draft it returns null.
     *
     * @param schemaUri a meta schema uri
     * @return the detected schema version or null
     */
    public static @Nullable SchemaVersion getVersion (URI schemaUri) {
        if (SchemaVersion.Draft202012.getSchemaUri ().equals (schemaUri)) {
            return Draft202012;
        }

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
     * try to detect schema version. If {@code uri} does not represent a known json schema draft
     * it returns the fallback schema version.
     *
     * @param uri current scope
     * @param fallback fallback version
     * @return the detected schema version or the latest version
     */
    public static SchemaVersion getVersion (URI uri, SchemaVersion fallback) {
        SchemaVersion version = getVersion (uri);
        if (version == null) {
            return fallback;
        }
        return version;
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

    public boolean isBefore202012 () {
        return compareTo (Draft202012) > 0;
    }

    public boolean isBefore201909 () {
        return compareTo (Draft201909) > 0;
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

    public boolean isFormatAnnotationVocabulary (URI candidate) {
        if (this == Draft201909) {
            return isVocabulary (candidate, "/vocab/format");
        }

        // since 202012
        return isVocabulary (candidate, "/vocab/format-annotation");
    }

    public boolean isFormatAssertionVocabulary (URI candidate) {
        if (this == Draft201909) {
            return isVocabulary (candidate, "/vocab/format");
        }

        // since 202012
        return isVocabulary (candidate, "/vocab/format-assertion");
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
