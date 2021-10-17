package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#path-item-object">4.7.9 Path Item Object</a>
 */
public class PathItem implements Reference {
    private final Context context;
    private final Node node;

    public PathItem (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Override
    public String getRef () {
        return null;
    }

}
