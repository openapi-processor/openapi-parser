package io.openapiparser.validator.array;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MaxItemsStep extends SimpleStep {

    public MaxItemsStep () {
        super ();
    }

    public MaxItemsStep (ValidationMessage message) {
        super(message);
    }
}
