package io.openapiparser.validator.array;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MinItemsStep extends SimpleStep {

    public MinItemsStep () {
        super ();
    }

    public MinItemsStep (ValidationMessage message) {
        super(message);
    }
}
