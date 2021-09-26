package io.openapiparser.model.v31.validations;

import io.openapiparser.*;
import io.openapiparser.validations.RequiredValidation;
import io.openapiparser.validations.VersionValidation;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class OpenapiValidation implements Validation {
    private final Collection<Validation> validations = Arrays.asList(
        new RequiredValidation (
            Arrays.asList (
                "openapi",
                "info")
        ),
        //  alt least one of: paths, components  webhooks
        new VersionValidation ()
    );

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return validations.stream ()
            .map(v -> v.validate (context, node))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
