/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32;

import io.openapiparser.Context;
import io.openapiparser.Properties;
import io.openapiprocessor.jsonschema.converter.NoValueException;
import io.openapiprocessor.jsonschema.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Schema</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.2.0.html#schema-object">4.8.24 Schema Object</a>
 * <a href="https://tools.ietf.org/html/draft-bhutton-json-schema-00">JSON Schema</a>
 * <a href="https://tools.ietf.org/html/draft-bhutton-json-schema-validation-00">JSON Schema
 * Validation</a>
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

    @Override
    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    /**
     * JSON Schema Validation: metadata
     */
    @Override
    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    // JSON Schema: metadata keyword $schema
    // JSON Schema: metadata keyword $vocabulary
    // JSON Schema: metadata keyword $id
    // JSON Schema: core keyword $dynamicRef
    // JSON Schema: core keyword $defs
    // JSON Schema: core keyword $comment

    /**
     * JSON Schema: subschemas logic keyword
     *
     * @return all of or empty if missing
     */
    public Collection<Schema> getAllOf () {
        return getObjectsOrEmpty (ALL_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     *
     * @return any of or empty if missing
     */
    public Collection<Schema> getAnyOf () {
        return getObjectsOrEmpty (ANY_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     *
     * @return one of or empty if missing
     */
    public Collection<Schema> getOneOf () {
        return getObjectsOrEmpty (ONE_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     *
     * @return not or null if missing
     */
    public @Nullable Schema getNot () {
        return getObjectOrNull (NOT, Schema.class);
    }

    // JSON Schema: subschemas conditional keyword if
    // JSON Schema: subschemas conditional keyword then
    // JSON Schema: subschemas conditional keyword else
    // JSON Schema: subschemas conditional keyword dependentSchemas

    /**
     * JSON Schema: subschemas array keyword
     *
     * @return prefix items or empty if missing
     */
    public Collection<Schema> getPrefixItems () {
        return getObjectsOrEmpty (PREFIX_ITEMS, Schema.class);
    }

    /**
     * JSON Schema: subschemas array keyword
     *
     * @return items or null if missing
     */
    public @Nullable Schema getItems () {
        return getObjectOrNull (ITEMS, Schema.class);
    }

    /**
     * JSON Schema: subschemas array keyword
     *
     * @return contains or null if missing
     */
    public @Nullable Schema getContains () {
        return getObjectOrNull (CONTAINS, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     *
     * @return properties map or empty if missing
     */
    public Map<String, Schema> getProperties () {
        return getMapObjectsOrEmpty (PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     *
     * @return pattern properties map, empty if missing
     */
    public Map<String, Schema> getPatternProperties () {
        return getMapObjectsOrEmpty (PATTERN_PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     *
     * @return additional properties or null if missing
     */
    public @Nullable Schema getAdditionalProperties () {
        return getObjectOrNull (ADDITIONAL_PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     *
     * @return property names or null if missing
     */
    public @Nullable Schema getPropertyNames () {
        return getObjectOrNull (PROPERTY_NAMES, Schema.class);
    }

    // JSON Schema: subschemas unevaluated keyword unevaluatedItems
    // JSON Schema: subschemas unevaluated keyword unevaluatedProperties

    /**
     * JSON Schema Validation: validation keyword for any instance type
     *
     * @return types, throws if empty
     */
    public Collection<String> getType () {
        final Object value = getRawValue (TYPE);
        if (value == null) {
            return Collections.emptyList ();
        } else if (value instanceof String) {
            return Collections.singletonList (getStringOrThrow (TYPE));
        } else if (value instanceof Collection) {
            Collection<String> types = getStringsOrEmpty (TYPE);
            return types.stream ()
                .map (t -> t == null ? "null" : t)
                .collect(Collectors.toList ());
        } else {
            throw new NoValueException ("todo"/*getSource ().getPath (TYPE)*/);
        }
    }

    /**
     * JSON Schema Validation: validation keyword for any instance type
     *
     * @return enum values or empty if missing
     *
     * todo any type
     */
    public @Nullable Collection<?> getEnum () {
        return getStringsOrNull (ENUM);
    }

    /**
     * JSON Schema Validation: validation keyword for any instance type
     *
     * @return const or null if missing
     */
    public @Nullable String getConst () {
        return getStringOrNull (CONST);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     *
     * @return multiple of or null if missing
     */
    public @Nullable Number getMultipleOf () {
        return getNumberOrNull (MULTIPLE_OF);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     *
     * @return maximum or null if missing
     */
    public @Nullable Number getMaximum () {
        return getNumberOrNull (MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     *
     * @return exclusive maximum or null if missing
     */
    public @Nullable Number getExclusiveMaximum () {
        return getNumberOrNull (EXCLUSIVE_MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     *
     * @return minimum or null if missing
     */
    public @Nullable Number getMinimum () {
        return getNumberOrNull (MINIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     *
     * @return exclusive minimum or null if missing
     */
    public @Nullable Number getExclusiveMinimum () {
        return getNumberOrNull (EXCLUSIVE_MINIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     *
     * @return max length or null if missing
     */
    public @Nullable Number getMaxLength () {
        return getNumberOrNull (MAX_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     *
     * @return min length or null if missing
     */
    public @Nullable Number getMinLength () {
        return getNumberOrNull (MIN_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     *
     * @return pattern or null if missing
     */
    public @Nullable String getPattern () {
        return getStringOrNull (PATTERN);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     *
     * @return max items or null if missing
     */
    public @Nullable Integer getMaxItems () {
        return getIntegerOrNull (MAX_ITEMS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     *
     * @return min items or 0 if missing
     */
    public Integer getMinItems () {
        return getIntegerOrDefault (MIN_ITEMS, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     *
     * @return unique items or false if missing
     */
    public Boolean getUniqueItems () {
        return getBooleanOrDefault (UNIQUE_ITEMS, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     *
     * @return max contains or null if missing
     */
    public @Nullable Integer getMaxContains () {
        return getIntegerOrNull (MAX_CONTAINS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     *
     * @return min contains or 1 if missing
     */
    public Integer getMinContains () {
        return getIntegerOrDefault (MIN_CONTAINS, 1);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     *
     * @return max properties or null if missing
     */
    public @Nullable Integer getMaxProperties () {
        return getIntegerOrNull (MAX_PROPERTIES);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     *
     * @return min properties or 0 if missing
     */
    public Integer getMinProperties () {
        return getIntegerOrDefault (MIN_PROPERTIES, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     *
     * @return required properties or empty if missing
     */
    public Collection<String> getRequired () {
        return getStringsOrEmpty (REQUIRED);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     *
     * @return dependent required map, may be empty
     */
    public Map<String, Set<String>> getDependentRequired () {
        return getMapSetStringsOrEmpty (DEPENDENT_REQUIRED);
    }

    /**
     * JSON Schema Validation: semantic format
     *
     * @return format or null if missing
     */
    public @Nullable String getFormat () {
        return getStringOrNull (FORMAT);
    }

    /* todo contentEncoding
     * JSON Schema Validation: string-encoded data
     */

    /* todo contentMediaType
     * JSON Schema Validation: string-encoded data
     */

    /* todo contentSchema
     * JSON Schema Validation: string-encoded data
     */

    /**
     * JSON Schema Validation: metadata
     *
     * @return title or null if missing
     */
    public @Nullable String getTitle () {
        return getStringOrNull (TITLE);
    }

    /**
     * JSON Schema Validation: metadata
     *
     * @return default or null
     */
    public @Nullable Object getDefault () {
        return getRawValue (DEFAULT);
    }

    /**
     * JSON Schema Validation: metadata
     *
     * @return true or false
     */
    public Boolean getDeprecated () {
        return getBooleanOrDefault (DEPRECATED, false);
    }

    /**
     * JSON Schema Validation: metadata
     *
     * @return true or false
     */
    public Boolean getReadOnly () {
        return getBooleanOrDefault (READ_ONLY, false);
    }

    /**
     * JSON Schema Validation: metadata
     *
     * @return true or false
     */
    public Boolean getWriteOnly () {
        return getBooleanOrDefault (WRITE_ONLY, false);
    }

    /* todo examples
     * JSON Schema Validation: metadata
     */

    /**
     * OpenAPI base vocabulary
     *
     * @return discriminator or null if missing
     */
    public @Nullable Discriminator getDiscriminator () {
        return getObjectOrNull (DISCRIMINATOR, Discriminator.class);
    }

    /**
     * OpenAPI base vocabulary
     *
     * @return xml or null if missing
     */
    public @Nullable Xml getXml () {
        return getObjectOrNull (XML, Xml.class);
    }

    /**
     * OpenAPI base vocabulary
     *
     * @return external documentation or null if missing
     */
    public @Nullable ExternalDocumentation getExternalDocs () {
        return getObjectOrNull (EXTERNAL_DOCS, ExternalDocumentation.class);
    }

    /**
     * OpenAPI base vocabulary
     *
     * @return example or null if missing
     */
    @Deprecated
    public @Nullable Object getExample () {
        return getRawValue (EXAMPLE);
    }

    /**
     * todo not required to have x- prefix
     */
    @Override
    public Map<String, @Nullable Object> getExtensions () {
        return super.getExtensions ();
    }
}
