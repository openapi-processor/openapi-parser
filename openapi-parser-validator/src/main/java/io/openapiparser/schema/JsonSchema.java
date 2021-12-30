package io.openapiparser.schema;

import java.net.URI;

public interface JsonSchema {

    URI getMetaSchema ();
    URI getId ();

    boolean isUniqueItems();

    JsonSchema getPropertySchema (String propName);
}