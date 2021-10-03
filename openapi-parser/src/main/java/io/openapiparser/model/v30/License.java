package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

/**
 * the <em>License</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#license-object">4.7.4 License Object</a>
 */
public class License implements Extensions {
    private final Context context;
    private final Node node;

    public License (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getName () {
        return null;
    }

    public String getUrl () {
        return null;
    }

}
