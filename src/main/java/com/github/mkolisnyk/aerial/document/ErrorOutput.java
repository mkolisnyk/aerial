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
public class ErrorOutput extends DocumentSection<ErrorOutput> {

    public ErrorOutput(DocumentSection<CaseSection> container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public ErrorOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        String template = AerialTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "output.error");
        return template.replaceAll("\\{CONTENT\\}", this.getContent().trim());
    }
}
