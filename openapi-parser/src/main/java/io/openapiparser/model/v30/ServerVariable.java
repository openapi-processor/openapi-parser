package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Collection;

import static io.openapiparser.Keywords.DESCRIPTION;

public class ServerVariable implements Extensions {
    private final Context context;
    private final Node node;

    public ServerVariable (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public Collection<String> getEnum () {
        return null;
    }

    public String getDefault () {
        return null;
    }

    public String getDescription () {
        return node.getString (DESCRIPTION);
    }
}
