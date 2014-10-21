/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

/**
 * @author Myk Kolisnyk
 */
public class PrerequisitesSection
                extends DocumentSection<PrerequisitesSection> {

    /**
     * @param container
     */
    public PrerequisitesSection(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public PrerequisitesSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
