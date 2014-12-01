/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.apache.commons.lang.StringUtils;

/**
 * @author Myk Kolisnyk
 *
 */
public class ValidOutput extends DocumentSection<ValidOutput> {

    private final int offset = 2;

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
        return StringUtils.repeat("    ", offset)
                + "Then " + this.getContent().trim();
    }
}
