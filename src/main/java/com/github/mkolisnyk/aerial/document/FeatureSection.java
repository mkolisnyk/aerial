/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

/**
 * @author Myk Kolisnyk
 *
 */
public class FeatureSection extends ContainerSection {
    private String ls = System.lineSeparator();
    private List<CaseSection> cases;

    public FeatureSection(DocumentSection<?> container) {
        super(container);
        this.cases = new ArrayList<CaseSection>();
    }

    /**
     * @return the cases
     */
    public final List<CaseSection> getCases() {
        return cases;
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

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.document.ContainerSection#parse(java.lang.String)
     */
    @Override
    public ContainerSection parse(String input) throws Exception {
        ContainerSection parsedSection = super.parse(input);
        ArrayList<DocumentSection<?>> section = this.getSections().get(Tokens.CASE_TOKEN);
        if (section != null) {
            for (DocumentSection<?> item : section) {
                this.cases.add((CaseSection) item);
            }
        }
        return parsedSection;
    }

    public String generate() throws Exception {
        Map<String, ArrayList<DocumentSection<?>>> sections = this.getSections();
        String content = "Feature: <feature name>" + ls;
        for (CaseSection section : this.cases) {
            content = content.concat(section.generate() + ls);
        }
        content += sections.get(Tokens.ADDITIONAL_SCENARIOS_TOKEN).get(0).generate();
        return content;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.document.ContainerSection#validate()
     */
    @Override
    public void validate() throws Exception {
        super.validate();
        Assert.assertTrue(this.getName().trim().length() > 0);
    }
}
