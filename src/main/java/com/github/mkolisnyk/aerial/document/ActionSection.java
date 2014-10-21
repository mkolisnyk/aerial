/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public class ActionSection extends DocumentSection<ActionSection> {

    /**
     * @param container
     */
    public ActionSection(DocumentSection<CaseSection> container) {
        super(container);
    }

    public ActionSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        return "When " + this.getContent();
    }
}
