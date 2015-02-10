/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;

/**
 * @author Myk Kolisnyk
 *
 */
public class ValidOutput extends DocumentSection<ValidOutput> {

    /**
     * @param container
     */
    public ValidOutput(DocumentSection<CaseSection> container) {
        super(container);
    }

    public ValidOutput(DocumentSection<?> container, String tagValue) {
        super(container, tagValue);
    }

    public ValidOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "output.valid");
        return template.replaceAll("\\{CONTENT\\}", this.getContent().trim());
    }
}
