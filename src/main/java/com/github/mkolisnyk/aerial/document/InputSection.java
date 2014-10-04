/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public class InputSection extends DocumentSection<InputSection> {

    /**
     * @param container
     */
    public InputSection(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public InputSection parse(String input) {
        this.setContent(input);
        return this;
    }

    public void validate(String input) {
        // TODO Auto-generated method stub
    }
}
