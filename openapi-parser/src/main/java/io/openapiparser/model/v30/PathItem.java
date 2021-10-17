package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import java.util.Collection;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Path Item</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#path-item-object">4.7.9 Path Item Object</a>
 */
public class PathItem implements Reference, Extensions {
    private final Context context;
    private final Node node;

    public PathItem (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    @Override
    public String getRef () {
        return node.getString (REF);
    }

    public String getSummary () {
        return null;
    }

    public String getDescription () {
        return null;
    }

    public Operation getGet () {
        return null;
    }

    public Operation getPut () {
        return null;
    }

    public Operation getPost () {
        return null;
    }

    public Operation getDelete () {
        return null;
    }

    public Operation getOptions () {
        return null;
    }

    public Operation getHead () {
        return null;
    }

    public Operation getPatch () {
        return null;
    }

    public Operation getTrace () {
        return null;
    }

    public Collection<Server> getServers () {
        return null;
    }

    public Collection<Parameter> getParameters () {
        return null;
    }

}
