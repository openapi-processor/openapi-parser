package io.openapiparser.model.v31.validations;

import io.openapiparser.*;
import io.openapiparser.model.v30.Keywords;
import io.openapiparser.validations.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static io.openapiparser.model.v31.Keywords.OPENAPI_KEYS;
import static io.openapiparser.validations.AllowedPropertiesValidator.Extensions.INCLUDE_X;

public class OpenapiValidator implements Validator {
    private final Collection<Validator> validations = Arrays.asList(
        new AllowedPropertiesValidator (Keywords.OPENAPI_KEYS, INCLUDE_X),
        new RequiredPropertiesValidator (OPENAPI_KEYS),
//        new RequiredPropertiesXorValidator (OPENAPI_KEYS_REQUIRED_XOR)
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
