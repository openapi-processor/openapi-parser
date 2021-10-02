package io.openapiparser.model.v31.validations;

import io.openapiparser.*;
import io.openapiparser.validations.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static io.openapiparser.Keywords.getProperties;
import static io.openapiparser.Keywords.getRequiredProperties;
import static io.openapiparser.model.v31.Keywords.*;
import static io.openapiparser.validations.AllowedPropertiesValidator.Extensions.INCLUDE_X;

public class OpenapiValidator implements Validator {
    private final Collection<Validator> validations = Arrays.asList (
        new AllowedPropertiesValidator (getProperties (OPENAPI_KEYS), INCLUDE_X),
        new RequiredPropertiesValidator (getRequiredProperties (OPENAPI_KEYS)),
        new AtLeastOnePropertyValidator (getProperties (OPENAPI_KEYS_AT_LEAST_ONE)),
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
