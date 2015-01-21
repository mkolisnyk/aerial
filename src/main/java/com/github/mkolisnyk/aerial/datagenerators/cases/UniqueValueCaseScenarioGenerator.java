package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.io.IOException;
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

import com.github.mkolisnyk.aerial.core.AerialTemplateMap;
import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class UniqueValueCaseScenarioGenerator extends
        CaseScenarioGenerator {
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
            Map<String, List<String>> testData, String[] uniqueFields) throws IOException {
        Map<String, List<String>> filteredData = filterBy(testData, "ValidInput", "true");
        Map<String, String> uniqueRow = new LinkedHashMap<String, String>();
        String dataHeader = AerialTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.header");
        String dataHeaderDelimiter = AerialTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.header.delimiter");
        String dataRow = AerialTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.row");
        String dataRowDelimiter = AerialTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.row.delimiter");
        String modifiedPrefix = AerialTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field.modified_prefix");

        for (Entry<String, List<String>> entry : filteredData.entrySet()) {
            String value = entry.getValue().get(0);
            uniqueRow.put(entry.getKey(), value);
            if (ArrayUtils.contains(uniqueFields, entry.getKey())) {
                uniqueRow.put(modifiedPrefix + entry.getKey(), value);
            }
        }
        String content = dataHeader.replaceAll(
                "\\{TITLES\\}",
                StringUtils.join(uniqueRow.keySet().iterator(), dataHeaderDelimiter)
        );

        for (String field : uniqueFields) {
            String dataRowText = "";
            String[] row = new String[uniqueRow.entrySet().size()];
            int j = 0;

            for (Entry<String, String> entry : uniqueRow.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.startsWith(modifiedPrefix)) {
                    String modField = key.replaceFirst(modifiedPrefix, "");
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
                row[j] = value.trim();
                j++;
            }
            dataRowText = StringUtils.join(row, dataRowDelimiter);
            dataRowText = dataRow.replaceAll("\\{DATA\\}", dataRowText);
            content = content.concat(dataRowText);
        }
        return content;
    }

    @Override
    public String generate() throws Exception {
        String template = AerialTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "case");
        String modifiedPrefix = AerialTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field.modified_prefix");
        String fieldPattern = AerialTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field");

        String[] uniqueRecords = getFieldsWithUniqueAttributes(this.getRecords());
        String content = "";
        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.PREREQUISITES_TOKEN));
        content += this.getSection().getSections().get(Tokens.ACTION_TOKEN).get(0).generate();
        content += this.getSection().getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate();
        String secondPass = this.getSection().getSections().get(Tokens.ACTION_TOKEN).get(0).generate()
                + this.getSection().getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate();
        for (String field : uniqueRecords) {
            secondPass = secondPass.replaceAll(fieldPattern.replaceAll("\\{NAME\\}", field),
                    fieldPattern.replaceAll("\\{NAME\\}", modifiedPrefix + field));
        }
        content += secondPass;
        String result = template.replaceAll("\\{NAME\\}", this.getSection().getName() + this.getScenarioName())
                .replaceAll("\\{BODY\\}", content)
                .replaceAll("\\{DATA\\}", this.generateUniqueScenarioData(this.getTestData(), uniqueRecords));
        return result;
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
