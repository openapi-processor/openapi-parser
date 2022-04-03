/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

import java.util.*;

/** migration */
public class MessageStep implements ValidationStep {
    private final Collection<ValidationMessage> messages = new ArrayList<> ();

    public MessageStep (ValidationMessage message) {
        messages.add (message);
    }

    public MessageStep (Collection<ValidationMessage> messages) {
        this.messages.addAll (messages);
    }

    @Override
    public boolean isValid () {
        return messages.isEmpty ();
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return messages;
    }
}
