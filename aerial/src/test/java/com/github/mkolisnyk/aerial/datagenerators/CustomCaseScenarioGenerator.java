package com.github.mkolisnyk.aerial.datagenerators;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;
import com.github.mkolisnyk.aerial.datagenerators.algorithms.NWiseDataTestingAlgorithm;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.Tokens;

public class CustomCaseScenarioGenerator extends
        CaseScenarioGenerator {

    public CustomCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
    }

    @Override
    public String generate() throws Exception {
        String template = AerialOutputTemplateMap.get(AerialOutputFormat.getCurrent().toString(), "case");
        String result = template.replaceAll("\\{NAME\\}", this.getSection().getName() + this.getScenarioName())
                            .replaceAll("\\{BODY\\}", "This is generated scenario")
                            .replaceAll("\\{DATA\\}", "")
                            .replaceAll("\\{TAGS\\}", this.getTagsString());
        return result;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public String getScenarioName() {
        return " custom scenario";
    }

    @Override
    public List<String> getTags(String tagBase) {
        return new ArrayList<String>() {
            {
                add("all");
                add("custom");
            }
        };
    }

}
