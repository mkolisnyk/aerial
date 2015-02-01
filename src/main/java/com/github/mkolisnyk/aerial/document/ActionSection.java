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
public class ActionSection extends DocumentSection<ActionSection> {

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
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "action");
        return template.replaceAll("\\{ACTION\\}", this.getContent().trim());
    }
}
