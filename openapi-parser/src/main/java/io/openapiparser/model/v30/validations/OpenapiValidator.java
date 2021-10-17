/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30.validations;

import io.openapiparser.*;
import io.openapiparser.validations.*;

import java.util.*;
import java.util.stream.Collectors;

import static io.openapiparser.Keywords.getProperties;
import static io.openapiparser.Keywords.getRequiredProperties;
import static io.openapiparser.model.v30.Keywords.*;
import static io.openapiparser.validations.AllowedPropertiesValidator.Extensions.*;

public class OpenapiValidator implements Validator {

    private final Collection<Validator> validations = Arrays.asList(
        new AllowedPropertiesValidator (getProperties (OPENAPI_KEYS), INCLUDE_X),
        new RequiredPropertiesValidator (getRequiredProperties (OPENAPI_KEYS)),
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
