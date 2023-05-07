/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import java.util.Collection;

public class ValidationMessagePrinter {

    public String print (Collection<ValidationMessage> messages) {
        ValidationMessage[] msgs = messages.toArray (new ValidationMessage[0]);

        StringBuilder builder = new StringBuilder ();

        for (int idx = 0; idx < msgs.length; idx++) {
            ValidationMessage msg = msgs[idx];
            builder.append (msg.getText ()).append ("\n");
            builder.append (print (msg.getNestedMessages (), ""));
        }

        return builder.toString ();
    }

    private String print (Collection<ValidationMessage> messages, String indent) {
        ValidationMessage[] nested = messages.toArray (new ValidationMessage[0]);

        StringBuilder builder = new StringBuilder ();

        int lastIdx = nested.length - 1;
        for (int idx = 0; idx < nested.length; idx++) {
            ValidationMessage msg = nested[idx];

            boolean currentLast = idx == lastIdx;
            String currentIndent = currentLast ? "\\--- " : "+--- ";
            String nestedIndent = indent + (currentLast ? "     " : "|    ");
            builder.append (indent).append (currentIndent).append (msg.getText ()).append ("\n");
            builder.append (print (msg.getNestedMessages (), nestedIndent));
        }

        return builder.toString ();
    }

}

