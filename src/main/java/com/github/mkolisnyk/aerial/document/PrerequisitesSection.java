/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;

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
    }

    public PrerequisitesSection(DocumentSection<?> container,
            String tagValue) {
        super(container, tagValue);
    }

    public PrerequisitesSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "prerequisite");
        return template.replaceAll("\\{CONTENT\\}", this.getContent().trim());
    }
}
