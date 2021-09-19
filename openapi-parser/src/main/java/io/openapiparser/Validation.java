package io.openapiparser;

import java.util.Collection;

public interface Validation {

    Collection<ValidationMessage> validate(ValidationContext context, Node node);

}
