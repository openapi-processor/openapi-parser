package io.openapiparser.validator;

public class UniqueItemsError extends ValidationMessage {
    public UniqueItemsError (String path) {
        super (path, "the items are not unique");
    }
}
