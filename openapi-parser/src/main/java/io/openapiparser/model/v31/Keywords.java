package io.openapiparser.model.v31;

import io.openapiparser.Keyword;

import java.util.*;

import static io.openapiparser.Keywords.*;

public interface Keywords {

    Collection<Keyword> OPENAPI_KEYS = Arrays.asList (
        required (OPENAPI),
        required (INFO),
        optional (JSON_SCHEMA_DIALECT),
        optional (SERVERS),
        optional (PATHS),
        optional (WEBHOOKS),
        optional (COMPONENTS),
        optional (SECURITY),
        optional (TAGS),
        optional (EXTERNAL_DOCS)
    );

    Collection<Keyword> OPENAPI_KEYS_AT_LEAST_ONE = Arrays.asList (
        optional (PATHS),
        optional (WEBHOOKS),
        optional (COMPONENTS));


    Collection<Keyword> INFO_KEYS = Arrays.asList (
        required (TITLE),
        optional (SUMMARY),
        optional (DESCRIPTION),
        optional (TERMS_OF_SERVICE),
        optional (CONTACT),
        optional (LICENSE),
        required (VERSION)
    );



}
