package io.openapiparser;

import java.util.Collection;

public interface ValidationResult {

    Collection<ValidationMessage> getMessages();

}
