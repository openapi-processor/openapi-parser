package io.openapiparser.validator.array;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class UniqueItemsStep extends SimpleStep {

    public UniqueItemsStep () {
        super ();
    }

    public UniqueItemsStep (ValidationMessage message) {
        super(message);
    }
}
