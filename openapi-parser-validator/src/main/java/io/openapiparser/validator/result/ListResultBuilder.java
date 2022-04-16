/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.result;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

public class ListResultBuilder {
    private final ResultTextBuilder builder;

    public ListResultBuilder () {
        builder = ValidationMessage::getText;
    }

    public ListResultBuilder (ResultTextBuilder builder) {
        this.builder = builder;
    }

    public String print (Collection<ValidationMessage> messages) {
        StringBuilder builder = new StringBuilder ();

        for (ValidationMessage message : messages) {
            builder.append (buildMessageText (message))
                .append ("\n");

            if (message.hasNestedMessages ()) {
                builder.append (print (message));
            }
        }

        return builder.toString ();
    }

    private String print (ValidationMessage parent) {
        Collection<ValidationMessage> messages = parent.getNestedMessages ();

        StringBuilder builder = new StringBuilder ();

        for (ValidationMessage message : messages) {
            builder.append (buildMessageText (message))
                .append ("\n");

            if (message.hasNestedMessages ()) {
                builder.append (print (message));
            }
        }

        return builder.toString ();
    }

    private String buildMessageText (ValidationMessage message) {
        return builder.getText (message);
    }
}

