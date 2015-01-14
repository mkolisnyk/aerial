package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class UniqueValueCaseScenarioGenerator extends
        CaseScenarioGenerator {
    private int    offset = 1;
    private String ls     = System.lineSeparator();

    public UniqueValueCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
    }

    private String[] getFieldsWithUniqueAttributes(
            List<InputRecord> input) {
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

    Map<String, List<String>> filterBy(
            Map<String, List<String>> testData, String field,
            String value) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (String key : testData.keySet()) {
            result.put(key, new ArrayList<String>());
        }
        int count = testData.get(testData.keySet().iterator().next())
                .size();
        for (int i = 0; i < count; i++) {
            if (testData.get(field).get(i).trim().equals(value)) {
                for (String key : testData.keySet()) {
                    result.get(key).add(testData.get(key).get(i));
                }
            }
        }
        return result;
    }

    private String getValueDifferentFrom(List<String> input,
            String value) {
        for (String item : input) {
            if (!item.equals(value)) {
                return item;
            }
        }
        return value;
    }

    private String generateUniqueScenarioData(
            Map<String, List<String>> testData, String[] uniqueFields) {
        Map<String, List<String>> filteredData = filterBy(testData,
                "ValidInput", "true");
        Map<String, String> uniqueRow = new LinkedHashMap<String, String>();
        for (Entry<String, List<String>> entry : filteredData
                .entrySet()) {
            String value = entry.getValue().get(0);
            uniqueRow.put(entry.getKey(), value);
            if (ArrayUtils.contains(uniqueFields, entry.getKey())) {
                uniqueRow.put("Modified " + entry.getKey(), value);
            }
        }
        String content = StringUtils.repeat("    ", offset + 1)
                + "| "
                + StringUtils.join(uniqueRow.keySet().iterator(),
                        " | ") + " |" + ls;

        for (String field : uniqueFields) {
            content = content.concat(StringUtils.repeat("    ",
                    offset + 1));
            for (Entry<String, String> entry : uniqueRow.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.startsWith("Modified ")) {
                    String modField = key.replaceFirst("Modified ",
                            "");
                    if (!modField.equals(field)) {
                        value = getValueDifferentFrom(
                                filteredData.get(modField), value);
                    }
                } else {
                    if (!key.equalsIgnoreCase(field)
                            && !ArrayUtils
                                    .contains(uniqueFields, key)) {
                        value = getValueDifferentFrom(
                                filteredData.get(key), value);
                    }
                }
                content = content.concat("| " + value.trim() + " ");
            }
            content = content.concat("|" + ls);
        }
        return content;
    }

    @Override
    public String generate() throws Exception {
        String[] uniqueRecords = getFieldsWithUniqueAttributes(this.getRecords());
        String content = StringUtils.repeat("    ", offset)
                + "Scenario Outline: " + this.getSection().getName()
                + this.getScenarioName() + ls;
        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.PREREQUISITES_TOKEN));
        content += this.getSection().getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        content += this.getSection().getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate() + ls;
        String secondPass = this.getSection().getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls
                + this.getSection().getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate() + ls;
        for (String field : uniqueRecords) {
            secondPass = secondPass.replaceAll("<" + field + ">",
                    "<Modified " + field + ">");
        }
        content += secondPass;
        content += StringUtils.repeat("    ", offset)
                + "Examples:"
                + ls
                + this.generateUniqueScenarioData(this.getTestData(),
                        uniqueRecords) + ls;
        return content;
    }

    @Override
    public boolean isApplicable() {
        String[] uniqueRecords = getFieldsWithUniqueAttributes(this
                .getRecords());
        return uniqueRecords.length > 0;
    }

    @Override
    public String getScenarioName() {
        return " unique values test";
    }
}
