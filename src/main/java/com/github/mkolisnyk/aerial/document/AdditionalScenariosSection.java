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
        return this.getContent();
    }
}
