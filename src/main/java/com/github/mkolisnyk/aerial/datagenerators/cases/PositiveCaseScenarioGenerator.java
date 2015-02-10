package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;
import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.algorithms.NWiseDataTestingAlgorithm;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class PositiveCaseScenarioGenerator extends
        CaseScenarioGenerator {

    private static final int DEFAULT_RECORD_SIZE = 2;

    public PositiveCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
    }

    private Map<String, List<String>> filterTestData(Map<String, List<String>> testData, boolean positive) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        int count = testData.get(testData.keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            if (testData.get("ValidInput").get(i).trim().equals("false") == positive) {
                continue;
            }
            for (Entry<String, List<String>> entry:testData.entrySet()) {
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), new ArrayList<String>());
                }
                result.get(entry.getKey()).add(testData.get(entry.getKey()).get(i));
            }
        }
        return result;
    }

    private String generateTestData(Map<String, List<String>> testData) throws IOException {
        String dataHeader = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.header");
        String dataHeaderDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.header.delimiter");
        String dataRow = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "data.row");
        String dataRowDelimiter = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "data.row.delimiter");
        String content = dataHeader.replaceAll(
                "\\{TITLES\\}",
                StringUtils.join(testData.keySet().iterator(), dataHeaderDelimiter)
        );
        int count = testData.get(testData.keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            String dataRowText = "";
            String[] row = new String[testData.entrySet().size()];
            int j = 0;
            for (Entry<String, List<String>> entry : testData.entrySet()) {
                row[j] = entry.getValue().get(i);
                j++;
            }
            dataRowText = StringUtils.join(row, dataRowDelimiter);
            dataRowText = dataRow.replaceAll("\\{DATA\\}", dataRowText);
            content = content.concat(dataRowText);
        }
        return content;
    }

    public boolean isPositive() {
        return true;
    }

    @Override
    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "case");

        String content = "";
        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.getPrerequisitesToken()));
        content += this.getSection().getSections().get(Tokens.getActionToken()).get(0).generate();
        if (isPositive()) {
            content += this.getSection().getSections().get(Tokens.getValidOutputToken()).get(0).generate();
        } else {
            content += this.getSection().getSections().get(Tokens.getErrorOutputToken()).get(0).generate();
        }
        Map<String, List<String>> testData = filterTestData(this.getTestData(), isPositive());
        if (isPositive()) {
            NWiseDataTestingAlgorithm algorithm = new NWiseDataTestingAlgorithm(testData, DEFAULT_RECORD_SIZE);
            testData = algorithm.generateTestData();
        }
        String result = template.replaceAll("\\{NAME\\}", this.getSection().getName() + this.getScenarioName())
                            .replaceAll("\\{BODY\\}", content)
                            .replaceAll("\\{DATA\\}", this.generateTestData(testData))
                            .replaceAll("\\{TAGS\\}", this.getTagsString());
        return result;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public String getScenarioName() {
        return " positive test";
    }

    @Override
    public List<String> getTags(final String tagBase) {
        List<String> tags = new ArrayList<String>() {
            {
                add("positive");
                if (!tagBase.equals("")) {
                    add(tagBase + "_positive");
                }
            }
        };
        return tags;
    }

}
