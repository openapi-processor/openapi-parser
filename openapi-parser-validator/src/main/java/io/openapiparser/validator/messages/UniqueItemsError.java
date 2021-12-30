package io.openapiparser.validator.messages;

public class UniqueItemsError extends ValidationMessage {
    public UniqueItemsError (String path) {
        super (path, "the items are not unique");
    }
}
