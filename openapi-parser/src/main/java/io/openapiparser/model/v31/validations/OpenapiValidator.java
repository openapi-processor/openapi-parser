package io.openapiparser.model.v31.validations;

import io.openapiparser.*;
import io.openapiparser.validations.RequiredPropertiesValidator;
import io.openapiparser.validations.VersionValidator;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class OpenapiValidator implements Validator {
    private final Collection<Validator> validations = Arrays.asList(
        new RequiredPropertiesValidator (
            Arrays.asList (
                "openapi",
                "info")
        ),
        //  alt least one of: paths, components  webhooks
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
