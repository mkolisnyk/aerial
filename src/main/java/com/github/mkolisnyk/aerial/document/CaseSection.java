/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.datagenerators.ScenarioGenerator;

/**
 * @author Myk Kolisnyk
 */
public class CaseSection extends ContainerSection {
    private int offset = 1;
    private String ls = System.lineSeparator();

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

    private String generateTestData(Map<String, List<String>> testData, boolean positive) {
        String content = StringUtils.repeat("\t", offset + 1) + "| "
                + StringUtils.join(testData.keySet().iterator(), " | ") + " |"  + ls
                + StringUtils.repeat("\t", offset + 1);
        int count = testData.get(testData.keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            if (testData.get("ValidInput").get(i).trim().equals("false") == positive) {
                continue;
            }
            for (Entry<String, List<String>> entry : testData.entrySet()) {
                content = content.concat("| " + entry.getValue().get(i) + " ");
            }
            content = content.concat("|" + ls + StringUtils.repeat("\t", offset + 1));
        }
        return content;
    }

    public String generate() throws Exception {
        InputSection input = (InputSection) this.getSections().get(Tokens.INPUT_TOKEN).get(0);
        ScenarioGenerator dataGenerator = new ScenarioGenerator(input.getInputs());
        Map<String, List<String>> testData = dataGenerator.generateTestData();
        String content = StringUtils.repeat("\t", offset) + "Scenario Outline: positive test" + ls;
        content += this.getSections().get(Tokens.PREREQUISITES_TOKEN).get(0).generate()  + ls;
        content += this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        content += this.getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate() + ls;
        content += StringUtils.repeat("\t", offset)
                + "Examples:" + ls + this.generateTestData(testData, true) + ls;
        content += StringUtils.repeat("\t", offset) + "Scenario Outline: negative test" + ls;
        content += this.getSections().get(Tokens.PREREQUISITES_TOKEN).get(0).generate() + ls;
        content += this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        content += this.getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate() + ls;
        content += StringUtils.repeat("\t", offset) + "Examples:"
                + ls + this.generateTestData(testData, false) + ls;
        return content;
    }
}
