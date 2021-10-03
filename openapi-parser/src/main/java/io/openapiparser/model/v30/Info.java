package io.openapiparser.model.v30;

import io.openapiparser.Context;
import io.openapiparser.Node;

import static io.openapiparser.Keywords.*;

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
        return node.getString (TITLE);
    }

    public String getDescription () {
        return node.getString (DESCRIPTION);
    }

    public String getTermsOfService () {
        return node.getString (TERMS_OF_SERVICE);
    }

    public Contact getContact () {
        return new Contact (context, node.getChildNode (CONTACT));
    }

    public License getLicense () {
        return new License (context, node.getChildNode (LICENSE));
    }

    public String getVersion () {
        return node.getString (VERSION);
    }
}
