/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

public class Vocabularies {
    public static final Vocabularies ALL = new Vocabularies ();

    private final Map<URI, Boolean> vocabularies;
    private final SchemaVersion version;

    private enum State {
        MISSING, OPTIONAL, REQUIRED
    }

    private State applicator = State.REQUIRED;
    private State content = State.REQUIRED;
    private State core = State.REQUIRED;
    private State formatAnnotation = State.REQUIRED;
    private State formatAssertion = State.REQUIRED;
    private State metaData = State.REQUIRED;
    private State validation = State.REQUIRED;

    private Vocabularies () {
        this.vocabularies = Collections.emptyMap ();
        this.version = SchemaVersion.getLatest ();
    }

    public Vocabularies (Map<URI, Boolean> vocabularies, SchemaVersion version) {
        this.vocabularies = vocabularies;
        this.version = version;
    }

    public boolean hasApplicator () {
        return applicator.ordinal () > State.MISSING.ordinal ();
    }

    public boolean hasContent () {
        return content.ordinal () > State.MISSING.ordinal ();
    }

    public boolean hasFormatAnnotation () {
        return formatAnnotation.ordinal () > State.MISSING.ordinal ();
    }

    public boolean hasFormatAssertion () {
        return formatAssertion.ordinal () > State.MISSING.ordinal ();
    }

    public boolean hasMetaData () {
        return metaData.ordinal () > State.MISSING.ordinal ();
    }

    public boolean hasValidation () {
        return validation.ordinal () > State.MISSING.ordinal ();
    }

    /**
     * format assertion vocabulary is required?
     *
     * @return true if format assertion vocabulary is required, otherwise false
     */
    public boolean requiresFormatAssertion() {
        return State.REQUIRED.equals(formatAssertion);
    }

    public static Vocabularies create (Map<URI, Boolean> vocabularies, SchemaVersion version) {
        Vocabularies result = new Vocabularies (vocabularies, version);
        result.applicator = getState (vocabularies, version, version::isApplicatorVocabulary);
        result.content = getState (vocabularies, version, version::isContentVocabulary);
        result.core = getState (vocabularies, version, version::isCoreVocabulary);
        result.formatAnnotation = getState (vocabularies, version, version::isFormatAnnotationVocabulary);
        result.formatAssertion = getState (vocabularies, version, version::isFormatAssertionVocabulary);
        result.metaData = getState (vocabularies, version, version::isMetaDataVocabulary);
        result.validation = getState (vocabularies, version, version::isValidationVocabulary);

        // todo
        if (result.core != State.REQUIRED)
            throw new RuntimeException ();

        return result;
    }

    private static State getState (
        Map<URI, Boolean> vocabularies, SchemaVersion version, Predicate<URI> predicate
    ) {
        if (version.isBefore201909 ())
            return State.REQUIRED;

        return vocabularies.keySet ()
            .stream ()
            .filter (predicate)
            .findFirst ()
            .map (v -> vocabularies.get (v) ? State.REQUIRED : State.OPTIONAL)
            .orElse (State.MISSING);
    }
}
