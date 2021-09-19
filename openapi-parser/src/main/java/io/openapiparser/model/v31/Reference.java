package io.openapiparser.model.v31;

/**
 * the <em>Reference</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#reference-object">4.8.23 Reference Object</a>
 */
public interface Reference {

    String getRef();

    String getSummary();

    String getDescription();

}
