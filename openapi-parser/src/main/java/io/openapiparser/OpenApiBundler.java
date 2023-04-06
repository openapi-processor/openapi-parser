/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.Keyword;
import io.openapiparser.schema.Keywords;
import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.schema.Scope.createScope;

public class OpenApiBundler {
    private final Context context;
    private final DocumentStore documents;
    private final Bucket root;
    private final URI rootDocumentUri;

    private final Map<String, Object> schemas = new LinkedHashMap<> ();
    private final Map<String, Object> responses = new LinkedHashMap<> ();
    private final Map<String, Object> parameters = new LinkedHashMap<> ();
    private final Map<String, Object> examples = new LinkedHashMap<> ();
    private final Map<String, Object> requestBodies = new LinkedHashMap<> ();
    private final Map<String, Object> headers = new LinkedHashMap<> ();
    private final Map<String, Object> securitySchemes = new LinkedHashMap<> ();
    private final Map<String, Object> links = new LinkedHashMap<> ();
    private final Map<String, Object> callbacks = new LinkedHashMap<> ();
    private final Map<String, Object> paths = new LinkedHashMap<> ();

    public OpenApiBundler (Context context, DocumentStore documents, Bucket root) {
        this.context = context;
        this.documents = documents.copy ();
        this.root = root;
        this.rootDocumentUri = root.getScope ().getDocumentUri ();
    }

    public Object bundle () {
        URI documentUri = root.getScope ().getDocumentUri ();
        Object document = documents.get (documentUri);

        Bucket bundled = Bucket.toBucket (root.getScope (), document, root.getLocation ());
        if (bundled == null)
            return null;  // nullable: throw??

        walkBucket (bundled);

        Map<String, Object> rawValues = bundled.getRawValues ();
        mergeComponents (rawValues);
        return rawValues;
    }

    private void mergeComponents (Map<String, Object> bundle) {
        Map<String, Object> bundleComponents = asObject(bundle.get ("components"));
        if (bundleComponents == null) {
            bundleComponents = new LinkedHashMap<> ();

            mergeMaps (bundleComponents);

            if (!bundleComponents.isEmpty ()) {
                bundle.put ("components", bundleComponents);
            }
        } else {
            mergeMaps (bundleComponents);
        }
    }

    private void mergeMaps (Map<String, Object> components) {
        mergeMap (components, "schemas", schemas);
        mergeMap (components, "responses", responses);
        mergeMap (components, "parameters", parameters);
        mergeMap (components, "examples", examples);
        mergeMap (components, "requestBodies", requestBodies);
        mergeMap (components, "headers", headers);
        mergeMap (components, "securitySchemes", securitySchemes);
        mergeMap (components, "links", links);
        mergeMap (components, "callbacks", callbacks);
        mergeMap (components, "paths", paths);
    }

    private void mergeMap (Map<String, Object> components, String property, Map<String, Object> propertyValues) {
        if (!propertyValues.isEmpty ()) {
            Map<String, Object> bundlePaths = asObject(components.get (property));
            if (bundlePaths == null) {
                components.put (property, propertyValues);
            } else {
                bundlePaths.putAll (propertyValues);
            }
        }
    }

    private void walkBucket (Bucket bucket) {
        Scope scope = bucket.getScope ();
        JsonPointer location = bucket.getLocation ();
        SchemaVersion version = scope.getVersion ();

        List<Runnable> modifications = new ArrayList<> ();

        bucket.forEach ((name, value) -> {
            JsonPointer propLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);
            boolean navigable = keyword != null && keyword.isNavigable ();

            if (name.equals (io.openapiparser.schema.Keywords.REF) && isString (value)) {
                Runnable modify = walkRef (bucket, propLocation);
                if (modify != null) {
                    modifications.add (modify);
                }

              // not OpenAPI 3.0
//            } else if (name.equals (Keywords.RECURSIVE_REF) && isString (value)) {
//                Ref ref = createRef (scope, name, value);
//                walkRef (ref, propLocation);

            } else if (navigable && keyword.isSchema () && isObject (value)) {
                walkSchema (scope, value, propLocation);

            } else if (navigable && keyword.isSchemaArray () && isArray (value)) {
                walkSchemaArray (scope, value, propLocation);

            } else if (navigable && keyword.isSchemaMap ()) {
                walkSchemaMap (scope, value, propLocation);

            } else if (keyword == null && isObject (value)) {
                walkSchema (scope, value, propLocation);

            } else if (keyword == null && isArray (value)) {
                walkSchemaArray (scope, value, propLocation);
            }
        });

        modifications.forEach (Runnable::run);
    }

    private boolean isExternalDocument (URI documentUri) {
        return ! documentUri.equals (rootDocumentUri);
    }

    private @Nullable Runnable walkRef (Bucket bucket, JsonPointer location) {
        Map<String, Object> bucketValues = bucket.getRawValues ();
        Reference reference = context.getReference (bucket);

        URI documentUri = reference.getDocumentUri ();
        boolean external = isExternalDocument (documentUri);
        Bucket documentBucket = getDocumentBucket (documentUri);

        JsonPointer refPointer = reference.getPointer ();
        String refName = refPointer.tail ();
        RawValue refValue = getRefValue (documentBucket, refPointer);

        Runnable result = null;
        if (isSchemaRef (location) && external) {
            bundleSchema (bucketValues, refName, refValue);

        } else if (isResponsesRef (location) && external) {
            bundleResponse (bucketValues, refName, refValue);

        } else if (isParametersRef (location) && external) {
            bundleParameter (bucketValues, refName, refValue);

        } else if (isExamplesRef (location) && external) {
            bundleExample (bucketValues, refName, refValue);

        } else if (isRequestBodiesRef (location) && external) {
            bundleRequestBody (bucketValues, refName, refValue);

        } else if (isHeadersRef (location) && external) {
            bundleHeader (bucketValues, refName, refValue);

        } else if (isSecuritySchemesRef (location) && external) {
            result = bundleSecurityScheme (bucketValues, refValue);

        } else if (isLinksRef (location) && external) {
            bundleLink (bucketValues, refName, refValue);

        } else if (isCallbacksRef (location) && external) {
            bundleCallback (bucketValues, refName, refValue);

        } else if (isPathRef (location) && external) {
            result = bundlePath30 (bucketValues, refValue);
        }

        // walk the ref
        walkBucket (getRefBucket (refPointer, refValue));

        return result;
    }

    private static Bucket getRefBucket (JsonPointer refPointer, RawValue refValue) {
        Bucket ref = Bucket.toBucket (refValue.getScope (), refValue.getValue (), refPointer);
        if (ref == null) {
            throw new RuntimeException ();
        }
        return ref;
    }

    private void bundleSchema (Map<String, Object> rawValues, String refName, RawValue refValue) {
        schemas.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("schemas", refName));
    }

    private void bundleResponse (Map<String, Object> rawValues, String refName, RawValue refValue) {
        responses.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("responses", refName));
    }

    private void bundleParameter (Map<String, Object> rawValues, String refName, RawValue refValue) {
        parameters.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("parameters", refName));
    }

    private void bundleExample (Map<String, Object> rawValues, String refName, RawValue refValue) {
        examples.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("examples", refName));
    }

    private void bundleRequestBody (Map<String, Object> rawValues, String refName, RawValue refValue) {
        requestBodies.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("requestBodies", refName));
    }

    private void bundleHeader (Map<String, Object> rawValues, String refName, RawValue refValue) {
        headers.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("headers", refName));
    }

    private Runnable bundleSecurityScheme (Map<String, Object> rawValues, RawValue refValue) {
        Map<String, Object> replacement = asObject (refValue.getValue ());
        if (replacement == null) {
            throw new RuntimeException ();
        }

        // postpone in-place modification...  can't remove/add while iterating
        return () -> {
            rawValues.remove (Keywords.REF);
            rawValues.putAll (replacement);
        };
    }

    private void bundleLink (Map<String, Object> rawValues, String refName, RawValue refValue) {
        links.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("links", refName));
    }

    private void bundleCallback (Map<String, Object> rawValues, String refName, RawValue refValue) {
        callbacks.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("callbacks", refName));
    }

    private Runnable bundlePath30 (Map<String, Object> rawValues, RawValue refValue) {
        // OpenAPI 3.0 has no /components/paths => inline it

        Map<String, Object> replacement = asObject (refValue.getValue ());
        if (replacement == null) {
            throw new RuntimeException ();
        }

        // postpone in-place modification...  can't remove/add while iterating
        return () -> {
            rawValues.remove (Keywords.REF);
            rawValues.putAll (replacement);
        };
    }

    private static RawValue getRefValue (Bucket documentBucket, JsonPointer refPointer) {
        RawValue refValue = documentBucket.getRawValue (refPointer);
        if (refValue == null) {
            throw new RuntimeException ();
        }
        return refValue;
    }

    private Bucket getDocumentBucket (URI documentUri) {
        Object document = documents.get (documentUri);
        if (document == null) {
            throw new RuntimeException ();
        }

        Scope scope = createScope (documentUri, document, SchemaVersion.Draft4);
        return Bucket.toBucket (scope, document);
    }

    private String createRefPointer (String type, String refName) {
        return String.format ("#/components/%s/%s", type, refName);
    }

    private void walkSchema (Scope currentScope, Object value, JsonPointer location) {
        Scope scope = currentScope.move (value);
        Bucket bucket = Bucket.toBucket (scope, value, location);
        if (bucket == null) {
            return; // todo error
        }

        walkBucket (bucket);
    }

    private void walkSchemaArray (Scope currentScope, Object value, JsonPointer location) {
        Collection<Object> items = asArray (value);
        if (items == null) {
            return; // todo error
        }

        int index = 0;
        for (Object item : items) {
            JsonPointer itemLocation = location.append (index);
            walkSchema (currentScope, item, itemLocation);
            index++;
        }
    }

    private void walkSchemaMap (Scope currentScope, Object value, JsonPointer location) {
        Scope targetScope = currentScope.move (value);
        Bucket bucket = Bucket.toBucket (targetScope, value, location);
        if (bucket == null) {
            return; // // todo error
        }

        bucket.forEach ((propName, propValue) -> {
            JsonPointer propLocation = location.append (propName);
            walkSchema (targetScope, propValue, propLocation);
        });
    }

    private boolean isSchemaRef (JsonPointer location) {
        // check for /**/schema/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 2
            && tokens.get (tokens.size () - 2).equals ("schema")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isResponsesRef (JsonPointer location) {
        // check for /**/responses/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("responses")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isParametersRef (JsonPointer location) {
        // check for /**/parameters/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("parameters")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isExamplesRef (JsonPointer location) {
        // check for /**/examples/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("examples")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isRequestBodiesRef (JsonPointer location) {
        // check for /**/requestBody/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 2
            && tokens.get (tokens.size () - 2).equals ("requestBody")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isHeadersRef (JsonPointer location) {
        // check for /**/headers/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("headers")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isSecuritySchemesRef (JsonPointer location) {
        // check for /components/securitySchemes/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("securitySchemes")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isLinksRef (JsonPointer location) {
        // check for /**/links/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("links")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isCallbacksRef (JsonPointer location) {
        // check for /**/callbacks/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("callbacks")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isPathRef (JsonPointer location) {
        // check for /paths/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () == 3
            && tokens.get (0).equals ("paths")
            && tokens.get (2).equals (Keywords.REF);
    }
}