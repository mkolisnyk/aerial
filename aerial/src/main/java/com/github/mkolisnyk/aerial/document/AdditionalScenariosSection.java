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
    public AdditionalScenariosSection(ContainerSection container,
            String tagValue) {
        super(container, tagValue);
    }

    /**
     * @param container
     */
    public AdditionalScenariosSection(ContainerSection container) {
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
