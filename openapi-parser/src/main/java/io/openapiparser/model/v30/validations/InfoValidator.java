/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30.validations;

import io.openapiparser.*;
import io.openapiparser.validations.AllowedPropertiesValidator;
import io.openapiparser.validations.RequiredPropertiesValidator;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static io.openapiparser.Keywords.getProperties;
import static io.openapiparser.Keywords.getRequiredProperties;
import static io.openapiparser.model.v30.Keywords.*;
import static io.openapiparser.validations.AllowedPropertiesValidator.Extensions.INCLUDE_X;

public class InfoValidator implements Validator {

    private final Collection<Validator> validations = Arrays.asList(
        new AllowedPropertiesValidator (getProperties (INFO_KEYS), INCLUDE_X),
        new RequiredPropertiesValidator (getRequiredProperties (INFO_KEYS))
    );

    @Override
    public Collection<ValidationMessage> validate (ValidationContext context, Node node) {
        return validations.stream ()
            .map(v -> v.validate (context, node))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
