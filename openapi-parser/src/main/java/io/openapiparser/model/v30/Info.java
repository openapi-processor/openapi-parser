package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.support.Node;

/**
 * the <em>Info</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#info-object">4.7.2 Info Object</a>
 */
public class Info {
    private final Context context;
    private final Node node;

    public Info (Context context, Node node) {
        this.context = context;
        this.node = node;
    }

    public String getTitle () {
        return node.getString ("title");
    }

    public String getDescription () {
        return node.getString ("description");
    }

    public String getTermsOfService () {
        return node.getString ("termsOfService");
    }

    public Contact getContact () {
        return null;
    }

    public License getLicense () {
        return null;
    }

    public String getVersion () {
        return node.getString ("version");
    }
}
