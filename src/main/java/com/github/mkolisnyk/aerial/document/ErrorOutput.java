/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import org.apache.commons.lang.StringUtils;

/**
 * @author Myk Kolisnyk
 *
 */
public class ErrorOutput extends DocumentSection<ErrorOutput> {

    private final int offset = 2;

    public ErrorOutput(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public ErrorOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        return StringUtils.repeat("\t", offset)
                + "Then " + this.getContent();
    }
}
