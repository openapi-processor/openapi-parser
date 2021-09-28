package io.openapiparser.model.v30.validations;

import io.openapiparser.*;
import io.openapiparser.validations.*;

import java.util.*;
import java.util.stream.Collectors;

import static io.openapiparser.model.v30.Keywords.OPENAPI_KEYS;
import static io.openapiparser.model.v30.Keywords.OPENAPI_KEYS_REQUIRED;
import static io.openapiparser.validations.AllowedPropertiesValidator.Extensions.*;

public class OpenapiValidator implements Validator {

    private final Collection<Validator> validations = Arrays.asList(
        new AllowedPropertiesValidator (OPENAPI_KEYS, INCLUDE_X),
        new RequiredPropertiesValidator (OPENAPI_KEYS_REQUIRED),
        new VersionValidator ()
    );

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return validations.stream ()
            .map(v -> v.validate (context, node))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
