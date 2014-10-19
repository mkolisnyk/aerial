/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialParser;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class DocumentSection<T extends DocumentSection<?>>
        implements AerialParser<T> {
    private DocumentSection<?> parent;
    private String content;

    public DocumentSection(DocumentSection<?> container) {
        this.parent = container;
        this.content = "";
    }

    /**
     * @return the parent
     */
    public final DocumentSection<?> getParent() {
        return parent;
    }

    /**
     * @return the content
     */
    public final String getContent() {
        return content;
    }

    /**
     * @param contentValue the content to set
     */
    public void setContent(String contentValue) {
        this.content = contentValue;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialParser#parse(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public T parse(String input) throws Exception {
        validate(input);
        setContent(input);
        return (T) this;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialParser#validate(java.lang.String)
     */
    public void validate(String input) throws Exception {
        Assert.assertFalse(input == null);
        Assert.assertFalse(input.trim().equals(""));
    }

    public void validate() throws Exception {
        validate(this.getContent());
    }
}
