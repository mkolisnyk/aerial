/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import com.github.mkolisnyk.aerial.core.AerialTemplateMap;
import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;

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
        // TODO Auto-generated constructor stub
    }

    public ValidOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        String template = AerialTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "output.valid");
        return template.replaceAll("\\{CONTENT\\}", this.getContent().trim());
    }
}
