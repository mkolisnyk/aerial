package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;
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
        String dataHeader = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.header");
        String dataHeaderDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.header.delimiter");
        String dataRow = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.row");
        String dataRowDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.row.delimiter");
        String modifiedPrefix = AerialOutputTemplateMap.get(
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
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "case");
        String modifiedPrefix = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field.modified_prefix");
        String fieldPattern = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field");

        String[] uniqueRecords = getFieldsWithUniqueAttributes(this.getRecords());
        String content = "";
        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.getPrerequisitesToken()));
        content += this.getSection().getSections().get(Tokens.getActionToken()).get(0).generate();
        content += this.getSection().getSections().get(Tokens.getValidOutputToken()).get(0).generate();
        String secondPass = this.getSection().getSections().get(Tokens.getActionToken()).get(0).generate()
                + this.getSection().getSections().get(Tokens.getErrorOutputToken()).get(0).generate();
        for (String field : uniqueRecords) {
            secondPass = secondPass.replaceAll(fieldPattern.replaceAll("\\{NAME\\}", field),
                    fieldPattern.replaceAll("\\{NAME\\}", modifiedPrefix + field));
        }
        content += secondPass;
        String result = template.replaceAll("\\{NAME\\}", this.getSection().getName() + this.getScenarioName())
                .replaceAll("\\{BODY\\}", content)
                .replaceAll("\\{DATA\\}", this.generateUniqueScenarioData(this.getTestData(), uniqueRecords))
                .replaceAll("\\{TAGS\\}", this.getTagsString());
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

    @Override
    public List<String> getTags(final String tagBase) {
        List<String> tags = new ArrayList<String>() {
            {
                add("unique");
                if (!tagBase.equals("")) {
                    add(tagBase + "_unique");
                }
            }
        };
        return tags;
    }
}
