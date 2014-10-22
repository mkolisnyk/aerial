/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Myk Kolisnyk
 *
 */
public class FeatureSection extends ContainerSection {
    private String ls = System.lineSeparator();

    public FeatureSection(DocumentSection<?> container) {
        super(container);
    }

    @Override
    public String[] getSectionTokens() {
        return new String[] {
                Tokens.CASE_TOKEN,
                Tokens.ADDITIONAL_SCENARIOS_TOKEN
        };
    }

    @Override
    public String[] getMandatoryTokens() {
        return new String[] {
                Tokens.CASE_TOKEN
        };
    }

    @Override
    public Map<String, Class<?>> getCreationMap() {
        return new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(Tokens.CASE_TOKEN, CaseSection.class);
                put(Tokens.ADDITIONAL_SCENARIOS_TOKEN,
                        AdditionalScenariosSection.class);
            }
        };
    }

    public String generate() throws Exception {
        Map<String, DocumentSection<?>> sections = this.getSections();
        String content = "Feature: <feature name>" + ls;
        content += sections.get(Tokens.CASE_TOKEN).generate() + ls;
        content += sections.get(Tokens.ADDITIONAL_SCENARIOS_TOKEN).generate();
        return content;
    }
}
