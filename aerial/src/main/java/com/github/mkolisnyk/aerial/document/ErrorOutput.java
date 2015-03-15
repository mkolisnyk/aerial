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
public class ErrorOutput extends DocumentSection<ErrorOutput> {

    public ErrorOutput(ContainerSection container) {
        super(container);
        // TODO Auto-generated constructor stub
    }

    public ErrorOutput(ContainerSection container, String tagValue) {
        super(container, tagValue);
    }

    public ErrorOutput parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "output.error");
        return template.replaceAll("\\{CONTENT\\}", this.getContent().trim());
    }
}
