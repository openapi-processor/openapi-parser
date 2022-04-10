/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.*;

public class RequiredStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public RequiredStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    /*
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

     */
}
