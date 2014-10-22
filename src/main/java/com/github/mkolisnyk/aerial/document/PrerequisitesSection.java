/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.apache.commons.lang.StringUtils;

/**
 * @author Myk Kolisnyk
 */
public class PrerequisitesSection
                extends DocumentSection<PrerequisitesSection> {

    private final int offset = 2;

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
        return StringUtils.repeat("\t", offset)
                + "Given " + this.getContent();
    }
}
