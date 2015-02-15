package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.io.IOException;
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

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;
import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class MandatoryCaseScenarioGenerator extends
        CaseScenarioGenerator {

    public MandatoryCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "case");
        String modifiedPrefix = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field.modified_prefix");
        String fieldPattern = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.field");

        String[] uniqueRecords = getFieldsWithMandatoryAttributes(this.getRecords());
        String content = "";
        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.getPrerequisitesToken()));
        content += this.getSection().getSections().get(Tokens.getActionToken()).get(0).generate();
        content += this.getSection().getSections().get(Tokens.getErrorOutputToken()).get(0).generate();
        String result = template.replaceAll("\\{NAME\\}", this.getSection().getName() + this.getScenarioName())
                .replaceAll("\\{BODY\\}", content)
                .replaceAll("\\{DATA\\}", this.generateMandatoryScenarioData(this.getTestData(), uniqueRecords))
                .replaceAll("\\{TAGS\\}", this.getTagsString());
        return result;

    }

    public Map<String, List<String>> filterBy(
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

    private String generateMandatoryScenarioData(
            Map<String, List<String>> testData, String[] mandatoryFields) throws Exception {
        Map<String, List<String>> filteredData = filterBy(testData, "ValidInput", "true");
        String dataHeader = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.header");
        String dataHeaderDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.header.delimiter");
        String dataRow = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.row");
        String dataRowDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.row.delimiter");

        String content = "";

        for (String field : mandatoryFields) {
        }
        return content;
    }

    private String[] getFieldsWithMandatoryAttributes(
            List<InputRecord> input) {
        Set<String> fields = new HashSet<String>();
        String[] result;
        for (InputRecord record : input) {
            if (record.isMandatory()) {
                fields.add(record.getName());
            }
        }
        result = new String[fields.size()];
        result = fields.toArray(result);
        return result;
    }

    @Override
    public boolean isApplicable() {
        String[] mandatoryRecords = getFieldsWithMandatoryAttributes(this
                .getRecords());
        return mandatoryRecords.length > 0;

    }

    @Override
    public String getScenarioName() {
        return "mandatory values";
    }

    @Override
    public List<String> getTags(final String tagBase) {
        List<String> tags = new ArrayList<String>() {
            {
                add("mandatory");
                if (!tagBase.equals("")) {
                    add(tagBase + "_mandatory");
                }
            }
        };
        return tags;
    }
}
