/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 *
 */
public class AdditionalScenariosSection extends
        DocumentSection<AdditionalScenariosSection> {
    private String ls = System.lineSeparator();
    /**
     * @param container
     */
    public AdditionalScenariosSection(DocumentSection<?> container) {
        super(container);
    }

    public AdditionalScenariosSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        return ls + this.getContent();
    }
}
