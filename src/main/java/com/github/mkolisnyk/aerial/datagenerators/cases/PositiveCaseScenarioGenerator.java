package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class PositiveCaseScenarioGenerator extends
        CaseScenarioGenerator {
    private int offset = 1;
    private String ls = System.lineSeparator();

    public PositiveCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
        // TODO Auto-generated constructor stub
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

    public boolean isPositive() {
        return true;
    }

    @Override
    public String generate() throws Exception {
        String content = StringUtils.repeat("    ", offset) + "Scenario Outline: "
                + this.getSection().getName() + this.getScenarioName() + ls;

        content += this.generatePreRequisites(this.getSection().getSections().get(Tokens.PREREQUISITES_TOKEN));
        content += this.getSection().getSections().get(Tokens.ACTION_TOKEN).get(0).generate() + ls;
        if (isPositive()) {
            content += this.getSection().getSections().get(Tokens.VALID_OUTPUT_TOKEN).get(0).generate() + ls;
        } else {
            content += this.getSection().getSections().get(Tokens.ERROR_OUTPUT_TOKEN).get(0).generate() + ls;
        }
        content += StringUtils.repeat("    ", offset)
            + "Examples:" + ls + this.generateTestData(this.getTestData(), isPositive()) + ls;
        return content;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public String getScenarioName() {
        return " positive test";
    }

}
