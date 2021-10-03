package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Map;

import static io.openapiparser.Keywords.DESCRIPTION;
import static io.openapiparser.Keywords.URL;

/**
 * the <em>Server</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#server-object">4.7.5 Server Object</a>
 */
public class Server implements Extensions {
    private final Context context;
    private final Node node;

    public Server (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getUrl () {
        return node.getString (URL);
    }

    public String getDescription () {
        return node.getString (DESCRIPTION);
    }

    public Map<String, ServerVariable> getVariables () {
        return null;
    }
}
