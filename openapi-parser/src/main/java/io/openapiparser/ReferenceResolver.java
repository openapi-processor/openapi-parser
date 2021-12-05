/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.support.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

/**
 * resolves all $ref'erences of the base document loading all referenced documents.
 */
public class ReferenceResolver {
    private static final String HASH = "#";

    private final URI baseUri;
    private final Reader reader;
    private final Converter converter;
    private final ReferenceRegistry references;
    private final DocumentRegistry documents = new DocumentRegistry ();

    private Node baseNode;

    public ReferenceResolver (URI baseUri, Reader reader, Converter converter, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.reader = reader;
        this.converter = converter;
        this.references = references;
        this.baseNode = Node.empty ();
    }

    public void resolve () throws ResolverException {
        initBaseDocument ();
        collectReferences ();
        resolveReferences ();
    }

    // todo throw if missing
    public Reference resolve(URI baseUri, String ref) {
        if (ref.startsWith (HASH)) {
            // same document
            return references.getRef(baseUri.resolve (ref));
        } else {
            // other document
            if (ref.contains (HASH)) {
                // with path fragment
                final int idxHash = ref.indexOf (HASH);
                String document = ref.substring (0, idxHash);
                String fragment = ref.substring (idxHash);
                URI documentUri = baseUri.resolve (document);
                URI refUri = documentUri.resolve (fragment);
                return references.getRef(refUri);
            } else {
                // full document
                URI refUri = baseUri.resolve (ref);
                return references.getRef(refUri);
            }
        }
    }

    /**
     * get the {@link Node} of the root OpenAPI document. The node is empty {@link Node} if the
     * document was not {@link #resolve()}d.
     *
     * @return base node.
     */
    public Node getBaseNode () {
        return baseNode;
    }

    private void initBaseDocument () throws ResolverException {
        baseNode = new Node("$", loadDocument (baseUri));
        documents.add (baseUri, baseNode);
    }

    private void resolveReferences() {
        references.resolve((documentUri, ref) -> {
            Node documentNode = documents.get (documentUri);
            String fragment = ref.substring(ref.indexOf (HASH));
            return getRawRefNode (documentNode, fragment);
        });
    }

    private void collectReferences () throws ResolverException {
        collectReferences (baseUri, baseNode);
    }

    private void collectReferences (URI uri, Node node) throws ResolverException {
        for (String k: node.getPropertyNames ()) {
            if (k.equals (Keywords.REF)) {
                String ref = getRef (uri, node);
                if (ref.startsWith (HASH)) {
                    // into same document
                    references.add (uri, uri, ref);
                } else {
                    // into other document
                    if (ref.contains (HASH)) {
                        String document = ref.substring(0, ref.indexOf(HASH));
                        URI documentUri = uri.resolve (document);

                        if (!documents.contains (documentUri)) {
                            Node documentNode = new Node(documentUri.toString (), loadDocument (documentUri));
                            documents.add (documentUri, documentNode);
                            collectReferences (documentUri, documentNode);
                        }

                        references.add (baseUri, documentUri, ref);
                    } else {
                        // is other document
                    }
                }
            } else {
                node.traverseProperty (k, n -> {
                    collectReferences (uri, n);
                });
            }
        }
    }

    private String getRef (URI uri, Node node) {
        final String ref = node.getStringValue (Keywords.REF);
        if (ref == null) {
            throw new ResolverException (String.format ("unable to resolve empty $ref in %s.", uri));
        }
        return ref;
    }

    private @Nullable Object getRawRefNode (Node documentNode, String fragment) {
        NodePathFinder finder = new NodePathFinder (documentNode);
        return finder.find (fragment.substring (1));
    }

    private Map<String, Object> loadDocument (URI documentUri) throws ResolverException {
        try {
            return converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            throw new ResolverException (String.format ("failed to resolve %s.", documentUri), e);
        }
    }
}
