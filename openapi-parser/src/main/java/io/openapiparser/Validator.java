package io.openapiparser;

import java.util.Collection;

public interface Validator {

    Collection<ValidationMessage> validate(ValidationContext context, Node node);

}
