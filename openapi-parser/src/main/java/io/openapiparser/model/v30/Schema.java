/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import io.openapiparser.schema.Bucket;
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
public class Schema extends Properties implements Reference, Extensions {

    public Schema (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    public Schema getRefObject () {
        return getRefObjectOrThrow (Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMultipleOf () {
        return getNumberOrNull (MULTIPLE_OF);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMaximum () {
        return getNumberOrNull (MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getExclusiveMaximum () {
        return getBooleanOrDefault (EXCLUSIVE_MAXIMUM, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMinimum () {
        return getNumberOrNull (MINIMUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getExclusiveMinimum () {
        return getBooleanOrDefault (EXCLUSIVE_MINIMUM, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMaxLength () {
        return getNumberOrNull (MAX_LENGTH);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Number getMinLength () {
        return getNumberOrNull (MIN_LENGTH);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Schema getItems () {
        return getObjectOrNull (ITEMS, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable String getPattern () {
        return getStringOrNull (PATTERN);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Integer getMaxItems () {
        return getIntegerOrNull (MAX_ITEMS);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Integer getMinItems () {
        return getIntegerOrDefault (MIN_ITEMS, 0);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Boolean getUniqueItems () {
        return getBooleanOrDefault (UNIQUE_ITEMS, false);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Integer getMaxProperties () {
        return getIntegerOrNull (MAX_PROPERTIES);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Integer getMinProperties () {
        return getIntegerOrDefault (MIN_PROPERTIES, 0);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<String> getRequired () {
        return getStringsOrEmpty (REQUIRED);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Map<String, Schema> getProperties () {
        return getMapObjectsOrEmpty (PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword. {@link Boolean} or {@link Schema} value.
     *
     * @return {@link Boolean} or {@link Schema}, true if missing
     */
    public @Nullable Object getAdditionalProperties () {
        final Object value = getRawValue (ADDITIONAL_PROPERTIES);
        if (value == null)
            return true;

        if (value instanceof Boolean)
            return getBooleanOrNull (ADDITIONAL_PROPERTIES);

        return getObjectOrNull (ADDITIONAL_PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    // todo any type
    public @Nullable Collection<?> getEnum () {
        return getStringsOrNull (ENUM);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    @Required
    public String getType () {
        return getStringOrThrow (TYPE);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getAllOf () {
        return getObjectsOrEmpty (ALL_OF, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getAnyOf () {
        return getObjectsOrEmpty (ANY_OF, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public Collection<Schema> getOneOf () {
        return getObjectsOrEmpty (ONE_OF, Schema.class);
    }

    /**
     * JSON Schema Validation: validation keyword
     */
    public @Nullable Schema getNot () {
        return getObjectOrNull (NOT, Schema.class);
    }

    /**
     * JSON Schema Validation: metadata keyword
     */
    public @Nullable String getTitle () {
        return getStringOrNull (TITLE);
    }

    /**
     * JSON Schema Validation: metadata keyword. May contain Markdown formatting (common mark).
     */
    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    /**
     * JSON Schema Validation: metadata keyword
     */
    public @Nullable Object getDefault () {
        return getRawValue (DEFAULT);
    }

    /**
     * JSON Schema Validation: semantic validation keyword
     */
    public @Nullable String getFormat() {
        return getStringOrNull (FORMAT);
    }

    /**
     * OpenAPI Schema
     */
    public Boolean getNullable () {
        return getBooleanOrDefault (NULLABLE, false);
    }

    /**
     * OpenAPI Schema
     */
    public @Nullable Discriminator getDiscriminator () {
        return getObjectOrNull (DISCRIMINATOR, Discriminator.class);
    }

    /**
     * OpenAPI Schema
     */
    public Boolean getReadOnly () {
        return getBooleanOrDefault (READ_ONLY, false);
    }

    /**
     * OpenAPI Schema
     */
    public Boolean getWriteOnly () {
        return getBooleanOrDefault (WRITE_ONLY, false);
    }

    /**
     * OpenAPI Schema
     */
    public @Nullable Xml getXml () {
        return getObjectOrNull (XML, Xml.class);
    }

    /**
     * OpenAPI Schema
     */
    public @Nullable ExternalDocumentation getExternalDocs () {
        return getObjectOrNull (EXTERNAL_DOCS, ExternalDocumentation.class);
    }

    /**
     * OpenAPI Schema
     */
    public @Nullable Object getExample () {
        return getRawValue (EXAMPLE);
    }

    /**
     * OpenAPI Schema
     */
    public Boolean getDeprecated () {
        return getBooleanOrDefault (DEPRECATED, false);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
