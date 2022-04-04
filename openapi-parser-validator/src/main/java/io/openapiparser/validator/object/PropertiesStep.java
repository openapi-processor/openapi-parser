/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;

import java.util.ArrayList;
import java.util.Collection;

public class PropertiesStep extends CompositeStep {
    private final Collection<ValidationMessage> messages = new ArrayList<> ();

    public void add (ValidationMessage message) {
        messages.add (message);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        Collection<ValidationMessage> result = new ArrayList<> ();
        result.addAll (super.getMessages ());
        result.addAll (messages);
        return result;
    }

    @Override
    public boolean isValid () {
        return super.isValid () && messages.isEmpty ();
    }

    @Override
    public String toString () {
        return isValid () ? "valid" : "invalid";
    }
}
