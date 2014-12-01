/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.apache.commons.lang.StringUtils;

/**
 * @author Myk Kolisnyk
 *
 */
public class ActionSection extends DocumentSection<ActionSection> {

    private int offset = 2;

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
        return StringUtils.repeat("    ", offset)
                + "When " + this.getContent().trim();
    }
}
