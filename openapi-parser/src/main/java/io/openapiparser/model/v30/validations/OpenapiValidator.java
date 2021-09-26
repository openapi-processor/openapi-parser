package io.openapiparser.model.v30.validations;

import io.openapiparser.*;
import io.openapiparser.validations.*;

import java.util.*;
import java.util.stream.Collectors;

import static io.openapiparser.model.v30.OpenApi.*;

public class OpenapiValidator implements Validator {
    private final Collection<Validator> validations = Arrays.asList(
        new RequiredPropertiesValidator (Arrays.asList (OPENAPI, INFO, PATHS)),
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
