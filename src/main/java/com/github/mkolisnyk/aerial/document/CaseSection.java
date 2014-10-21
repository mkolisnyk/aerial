/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.datagenerators.ScenarioGenerator;

/**
 * @author Myk Kolisnyk
 */
public class CaseSection extends ContainerSection {

    public CaseSection(DocumentSection<?> container) {
        super(container);
    }

    public CaseSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String[] getSectionTokens() {
        return new String[] {Tokens.ACTION_TOKEN,
                Tokens.PREREQUISITES_TOKEN, Tokens.INPUT_TOKEN,
                Tokens.VALID_OUTPUT_TOKEN, Tokens.ERROR_OUTPUT_TOKEN };
    }

    public String[] getMandatoryTokens() {
        return new String[] {Tokens.ACTION_TOKEN,
                Tokens.INPUT_TOKEN, Tokens.VALID_OUTPUT_TOKEN,
                Tokens.ERROR_OUTPUT_TOKEN };
    }

    public Map<String, Class<?>> getCreationMap() {
        return new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(Tokens.ACTION_TOKEN, ActionSection.class);
                put(Tokens.PREREQUISITES_TOKEN,
                        PrerequisitesSection.class);
                put(Tokens.INPUT_TOKEN, InputSection.class);
                put(Tokens.VALID_OUTPUT_TOKEN, ValidOutput.class);
                put(Tokens.ERROR_OUTPUT_TOKEN, ErrorOutput.class);
            }
        };
    }

    private String generatePositiveTestData(Map<String, List<String>> testData) {
        return "";
    }

    private String generateNegativeTestData(Map<String, List<String>> testData) {
        return "";
    }

    public String generate() throws Exception {
        InputSection input = (InputSection) this.getSections().get(Tokens.INPUT_TOKEN);
        ScenarioGenerator dataGenerator = new ScenarioGenerator(input.getInputs());
        Map<String, List<String>> testData = dataGenerator.generateTestData();
        return null;
    }
}
