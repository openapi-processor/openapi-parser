/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Schema</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#schema-object">4.7.24 Schema Object</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-00">JSON Schema</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-00">JSON
 * Schema Validation</a>
 */
public class Schema implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Schema (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = context.getRefNodeOrNull (node);
    }

    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    @Override
    @Required
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMultipleOf() {
        return getSource ().getNumberValue (MULTIPLE_OF);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMaximum() {
        return getSource ().getNumberValue (MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getExclusiveMaximum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MAXIMUM, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMinimum() {
        return getSource ().getNumberValue (MINIMUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getExclusiveMinimum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MINIMUM, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMaxLength() {
        return getSource ().getNumberValue (MAX_LENGTH);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMinLength() {
        return getSource ().getNumberValue (MIN_LENGTH);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Schema getItems () {
        return getSource ().getObjectValue (ITEMS, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable String getPattern() {
        return getSource ().getStringValue (PATTERN);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Integer getMaxItems() {
        return getSource ().getIntegerValue (MAX_ITEMS);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Integer getMinItems() {
        return getSource ().getIntegerValue (MIN_ITEMS, 0);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getUniqueItems() {
        return getSource ().getBooleanValue (UNIQUE_ITEMS, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Integer getMaxProperties() {
        return getSource ().getIntegerValue (MAX_PROPERTIES);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Integer getMinProperties() {
        return getSource ().getIntegerValue (MIN_PROPERTIES, 0);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<String> getRequired() {
        return getSource ().getStringValuesOrEmpty (REQUIRED);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Map<String, Schema> getProperties () {
        return node.getMapObjectValuesOrEmpty (PROPERTIES, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword. {@link Boolean} or {@link Schema} value.
     *
     * @return {@link Boolean} or {@link Schema}, true if missing
     */
    public @Nullable Object getAdditionalProperties () {
        final Object value = node.getRawValue (ADDITIONAL_PROPERTIES);
        if (value == null)
            return true;

        if (value instanceof Boolean)
            return node.getBooleanValue (ADDITIONAL_PROPERTIES);

        return node.getObjectValue (ADDITIONAL_PROPERTIES, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Collection<?> getEnum() {
        return getSource ().getStringValues (ENUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public String getType() {
        return getSource ().getRequiredStringValue (TYPE);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getAllOf () {
        return getSource ().getObjectValuesOrEmpty (ALL_OF, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getAnyOf () {
        return getSource ().getObjectValuesOrEmpty (ANY_OF, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getOneOf () {
        return getSource ().getObjectValuesOrEmpty (ONE_OF, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Schema getNot () {
        return getSource ().getObjectValue (NOT, node -> new Schema (context, node));
    }

    /**
     * JSON Schema Validation: metadata keyword
     */
    public @Nullable String getTitle () {
        return getSource ().getStringValue (TITLE);
    }

    /**
     * JSON Schema Validation: metadata keyword. May contain Markdown formatting (common mark).
     */
    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    /**
     * JSON Schema Validation: metadata keyword
     */
    public @Nullable Object getDefault () {
        return getSource ().getRawValue (DEFAULT);
    }

    /**
     * JSON Schema Validation: semantic validation keyword
     */
    public @Nullable String getFormat() {
        return getSource ().getStringValue (FORMAT);
    }

    /**
     * OpenAPI Schema
     */
    public Boolean getNullable () {
        return getSource ().getBooleanValue (NULLABLE, false);
    }

    /* todo discriminator
     * OpenAPI Schema
     */

    /* todo readOnly
     * OpenAPI Schema
     */

    /* todo writeOnly
     * OpenAPI Schema
     */

    /* todo xml
     * OpenAPI Schema
     */

    /* todo externalDocs
     * OpenAPI Schema
     */

    /* todo example
     * OpenAPI Schema
     */

    /**
     * OpenAPI Schema
     */
    public Boolean getDeprecated () {
        return getSource ().getBooleanValue (DEPRECATED, false);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return getSource ().getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
