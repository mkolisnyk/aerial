/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialGenerator;
import com.github.mkolisnyk.aerial.AerialParser;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class DocumentSection<T extends DocumentSection<?>>
        implements AerialParser<T>, AerialGenerator {
    private String name;
    private ContainerSection parent;
    private String content;
    private String tag;

    public DocumentSection(ContainerSection container) {
        this(container, null);
    }

    public DocumentSection(ContainerSection container, String tagValue) {
        this.name = "";
        this.parent = container;
        this.content = "";
        this.tag = tagValue;
    }

    /**
     * @return the tag
     */
    public final String getTag() {
        if (tag == null) {
            return "";
        }
        return tag;
    }

    /**
     * @return the parent
     */
    public final ContainerSection getParent() {
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

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param nameValue the name to set
     */
    public final void setName(String nameValue) {
        this.name = nameValue;
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
