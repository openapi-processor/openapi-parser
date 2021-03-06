/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.result;

import io.openapiparser.validator.ValidationMessage;

import java.util.*;

public class MessageCollector {
    private final Collection<ValidationMessage> messages;
    private final Map<MessageKey, LinkedList<Message>> merged = new LinkedHashMap<> ();

    public MessageCollector (Collection<ValidationMessage> messages) {
        this.messages = messages;
    }

    public LinkedList<Message> collect () {
        collect (messages);
        return createResult ();
    }

    private void collect (Collection<ValidationMessage> messages) {
        for (ValidationMessage message : messages) {
            Message msg = new Message (
                message.getInstanceScope (),
                message.getInstancePath (),
                message.getSchemaPath (),
                message.getText ()
            );

            if (!msg.isEmpty ()) {
                MessageKey key = new MessageKey (message.getInstanceScope (), message.getInstancePath ());
                Collection<Message> scopeMessages = merged.computeIfAbsent (key, k -> new LinkedList<> ());
                scopeMessages.add (msg);
            }

            if (message.hasNestedMessages ()) {
                collect (message.getNestedMessages ());
            }
        }
    }

    private LinkedList<Message> createResult () {
        LinkedList<Message> result = new LinkedList<> ();
        for (LinkedList<Message> value : merged.values ()) {
            result.addAll (value);
        }
        return result;
    }
}
