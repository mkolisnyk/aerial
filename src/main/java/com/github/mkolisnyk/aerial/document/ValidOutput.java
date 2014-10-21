/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public class ValidOutput extends DocumentSection<ValidOutput> {

    /**
     * @param container
     */
    public ValidOutput(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public ValidOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        return "Then " + this.getContent();
    }
}
