/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public class ErrorOutput extends DocumentSection<ErrorOutput> {

    public ErrorOutput(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public ErrorOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        return "Then " + this.getContent();
    }
}
