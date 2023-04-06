/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

public class Vocabularies {
    public static final Vocabularies ALL = new Vocabularies ();

    private final Map<URI, Boolean> vocabularies;

    private enum State {
        REQUIRED, OPTIONAL, MISSING
    }

    private State applicator = State.REQUIRED;
    private State content = State.REQUIRED;
    private State core = State.REQUIRED;
    private State format = State.REQUIRED;
    private State metaData = State.REQUIRED;
    private State validation = State.REQUIRED;

    private Vocabularies () {
        this.vocabularies = Collections.emptyMap ();
    }

    public Vocabularies (Map<URI, Boolean> vocabularies) {
        this.vocabularies = vocabularies;
    }

    public boolean requiresApplicator () {
        return applicator.equals (State.REQUIRED);
    }

    public boolean requiresContent () {
        return content.equals (State.REQUIRED);
    }

    public boolean requiresFormat () {
        return format.equals (State.REQUIRED);
    }

    public boolean requiresMetaData () {
        return metaData.equals (State.REQUIRED);
    }

    public boolean requiresValidation () {
        return validation.equals (State.REQUIRED);
    }

    public static Vocabularies create (Map<URI, Boolean> vocabularies, SchemaVersion version) {
        Vocabularies result = new Vocabularies (vocabularies);
        result.applicator = getState (vocabularies, version, version::isApplicatorVocabulary);
        result.content = getState (vocabularies, version, version::isContentVocabulary);
        result.core = getState (vocabularies, version, version::isCoreVocabulary);
        result.format = getState (vocabularies, version, version::isFormatVocabulary);
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
