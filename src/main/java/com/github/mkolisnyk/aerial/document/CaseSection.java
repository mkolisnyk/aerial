/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

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
        String content = StringUtils.repeat("    ", offset + 1) + "| "
                + StringUtils.join(testData.keySet().iterator(), " | ") + " |"  + ls;
        int count = testData.get(testData.keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            if (testData.get("ValidInput").get(i).trim().equals("false") == positive) {
                continue;
            }
            content = content.concat(StringUtils.repeat("    ", offset + 1));
            for (Entry<String, List<String>> entry : testData.entrySet()) {
                content = content.concat("| " + entry.getValue().get(i) + " ");
            }
            content = content.concat("|" + ls);
        }
        return content;
    }

    Map<String, List<String>> filterBy(Map<String, List<String>> testData, String field, String value) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (String key : testData.keySet()) {
            result.put(key, new ArrayList<String>());
        }
        int count = testData.get(testData.keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            if (testData.get(field).get(i).trim().equals(value)) {
                for (String key : testData.keySet()) {
                    result.get(key).add(testData.get(key).get(i));
                }
            }
        }
        return result;
    }

    private String getValueDifferentFrom(List<String> input, String value) {
        for (String item : input) {
            if (!item.equals(value)) {
                return item;
            }
        }
        return value;
    }

    private String generateUniqueScenarioData(
            Map<String, List<String>> testData,
            String[] uniqueFields) {
        Map<String, List<String>> filteredData = filterBy(testData, "ValidInput", "true");
        Map<String, String> uniqueRow = new LinkedHashMap<String, String>();
        for (Entry<String, List<String>> entry : filteredData.entrySet()) {
            String value = entry.getValue().get(0);
            uniqueRow.put(entry.getKey(), value);
            if (ArrayUtils.contains(uniqueFields, entry.getKey())) {
                uniqueRow.put("Modified " + entry.getKey(), value);
            }
        }
        String content = StringUtils.repeat("    ", offset + 1) + "| "
                + StringUtils.join(uniqueRow.keySet().iterator(), " | ") + " |"  + ls;

        for (String field : uniqueFields) {
            content = content.concat(StringUtils.repeat("    ", offset + 1));
            for (Entry<String, String> entry : uniqueRow.entrySet()) {
                String value = entry.getValue();
                if (!entry.getKey().equalsIgnoreCase("Modified " + field) &&
                     !entry.getKey().equalsIgnoreCase(field)) {
                    value = getValueDifferentFrom(filteredData.get(entry.getKey()), value);
                }
                content = content.concat("| " + value.trim() + " ");
            }
            content = content.concat("|" + ls);
        }
        return content;
    }

    private String[] getFieldsWithUniqueAttributes(List<InputRecord> input) {
        Set<String> fields = new HashSet<String>();
        String[] result;
        for (InputRecord record : input) {
            if (record.isUnique()) {
                fields.add(record.getName());
            }
        }
        result = new String[fields.size()];
        result = fields.toArray(result);
        return result;
    }

    private String generatePreRequisites(ArrayList<DocumentSection<?>> preRequisites) throws Exception {
        String result = "";
        if (preRequisites != null) {
            for (DocumentSection<?> section : preRequisites) {
                result = result.concat(section.generate()  + ls);
            }
        }
        return result;
    }

    public String generate() throws Exception {
        InputSection input = (InputSection) this.getSections().get(Tokens.INPUT_TOKEN).get(0);
        ScenarioGenerator dataGenerator = new ScenarioGenerator(input.getInputs());
        Map<String, List<String>> testData = dataGenerator.generateTestData();
        String content = StringUtils.repeat("    ", offset) + "Scenario Outline: "
                            + this.getName() + " positive test" + ls;

        content += this.generatePreRequisites(this.getSections().get(Tokens.PREREQUISITES_TOKEN));

        content += this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        content += this.getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate() + ls;
        content += StringUtils.repeat("    ", offset)
                + "Examples:" + ls + this.generateTestData(testData, true) + ls;
        content += StringUtils.repeat("    ", offset) + "Scenario Outline: " + this.getName() + " negative test" + ls;

        content += this.generatePreRequisites(this.getSections().get(Tokens.PREREQUISITES_TOKEN));

        content += this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        content += this.getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate() + ls;
        content += StringUtils.repeat("    ", offset) + "Examples:"
                + ls + this.generateTestData(testData, false) + ls;
        String[] uniqueRecords = getFieldsWithUniqueAttributes(input.getInputs());
        if (uniqueRecords.length > 0) {
            content += StringUtils.repeat("    ", offset) + "Scenario Outline: " + this.getName()
                        + " unique values test" + ls;
            content += this.generatePreRequisites(this.getSections().get(Tokens.PREREQUISITES_TOKEN));

            content += this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
            content += this.getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate() + ls;
            String secondPass = this.getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls
                    + this.getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate() + ls;
            for (String field : uniqueRecords) {
                secondPass = secondPass.replaceAll("<" + field + ">", "<Modified " + field + ">");
            }
            content += secondPass;
            content += StringUtils.repeat("    ", offset) + "Examples:"
                    + ls + this.generateUniqueScenarioData(testData, uniqueRecords) + ls;
        }
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
