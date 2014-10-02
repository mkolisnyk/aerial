/**
 * 
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class DocumentSection {
    private DocumentSection parent;
    public DocumentSection(DocumentSection container) {
        this.parent = container;
    }
    /**
     * @return the parent
     */
    public final DocumentSection getParent() {
        return parent;
    }
}
