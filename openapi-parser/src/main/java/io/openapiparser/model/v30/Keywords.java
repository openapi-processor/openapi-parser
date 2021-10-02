package io.openapiparser.model.v30;

import io.openapiparser.Keyword;

import java.util.*;

import static io.openapiparser.Keywords.*;

public interface Keywords {

    Collection<Keyword> OPENAPI_KEYS = Arrays.asList (
        required (OPENAPI),
        required (INFO),
        optional (SERVERS),
        required (PATHS),
        optional (COMPONENTS),
        optional (SECURITY),
        optional (TAGS),
        optional (EXTERNAL_DOCS)
    );

    Collection<Keyword> INFO_KEYS = Arrays.asList (
        required (TITLE),
        optional (DESCRIPTION),
        optional (TERMS_OF_SERVICE),
        optional (CONTACT),
        optional (LICENSE),
        required (VERSION)
    );

}
