package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

class ResolverContext {
//    Set<URI> processedDocuments = new HashSet<> ();
//    Set<URI> processedReferences = new HashSet<> ();
//
    private final DocumentStore documents;
    private final DocumentLoader loader;


    public final References references = new References ();
    public final ReferenceRegistry registry;

    public ResolverContext (DocumentStore documents, DocumentLoader loader, ReferenceRegistry registry) {
        this.documents = documents;
        this.loader = loader;
        this.registry = registry;
    }

    public void addId (URI id, Map<String, Object> document) {
        if (documents.contains (id)) {
            return;
        }

        documents.addId (id, document);
    }

    public void addAnchor (URI anchor, Map<String, Object> document) {
        if (documents.contains (anchor)) {
            return;
        }

        documents.addAnchor (anchor, document);
    }

    public void addDynamicAnchor (URI anchor, Map<String, Object> document) {
        if (documents.contains (anchor)) {
            return;
        }

        documents.addDynamicAnchor (anchor, document);
    }

    public void addRef (Ref ref) {
        if (registry.hasReference (ref.getAbsoluteUri ())) {
            return;
        }

        if (references.contains (ref)) {
            return;
        }

        references.add (ref);
    }

    public void addRef (Ref ref, Map<String, Object> document) {
        if (registry.hasReference (ref.getAbsoluteUri ())) {
            return;
        }

        registry.add (ref, ref.getScope (), document);
    }

    public void addRef (Ref ref, Scope scope, Object document) {
        if (registry.hasReference (ref.getAbsoluteUri ())) {
            return;
        }

        registry.add (ref, scope, document);
    }

    public @Nullable Object getDocument (URI documentUri) {
        return documents.get (documentUri);
    }

    public @Nullable Object addDocument (URI documentUri, String source, String ref) {
        try {
            Object document;
            if (documentUri.getScheme () == null) {
                document = loader.loadDocument (documentUri.toString ());
            } else {
                document = loader.loadDocument (documentUri);
            }

            documents.addId (documentUri, document);
            return document;
        } catch (ResolverException ex) {
            throw new ResolverException (
                String.format ("failed to resolve '%s' $referenced from '%s'", ref, source), ex);
        }
    }

//    boolean isProcessedDocument (URI uri) {
//        return processedDocuments.contains (uri);
//    }
//
//    public void setProcessedDocument (URI uri) {
//        processedDocuments.add (uri);
//        processedReferences.add (uri);
//    }
//
//    boolean isProcessedReference (URI uri) {
//        return processedDocuments.contains (uri)
//            || processedReferences.contains (uri);
//    }
//
//    public void setProcessedReferences (URI uri) {
//        processedReferences.add (uri);  // add empty ref ???
//    }
}
