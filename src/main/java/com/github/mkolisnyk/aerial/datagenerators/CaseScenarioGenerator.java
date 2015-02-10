package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialOutputTemplateMap;
import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.DocumentSection;
import com.github.mkolisnyk.aerial.document.InputRecord;

public abstract class CaseScenarioGenerator {
    private CaseSection section;
    private List<InputRecord> records;
    private Map<String, List<String>> testData;

    public CaseScenarioGenerator(
            CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        this.section = sectionData;
        this.records = recordsList;
        this.testData = testDataMap;
    }

    /**
     * @return the records
     */
    public final List<InputRecord> getRecords() {
        return records;
    }

    /**
     * @return the testData
     */
    public final Map<String, List<String>> getTestData() {
        return testData;
    }

    /**
     * @return the section
     */
    public final CaseSection getSection() {
        return section;
    }

    public String generatePreRequisites(ArrayList<DocumentSection<?>> preRequisites) throws Exception {
        String result = "";
        if (preRequisites != null) {
            for (DocumentSection<?> item : preRequisites) {
                result = result.concat(item.generate());
            }
        }
        return result;
    }

    public abstract String generate() throws Exception;

    public abstract boolean isApplicable();

    public abstract String getScenarioName();

    public abstract List<String> getTags(String tagBase);

    public String getTagsString() throws Exception {
        String tagFormat = AerialOutputTemplateMap.get(
                AerialOutputFormat.getCurrent().toString(), "tag.format");
        String tagBase = this.getSection().getTag();
        List<String> tags = getTags(tagBase);
        String result = "";
        for (String tag : tags) {
            result = result.concat(tagFormat.replaceAll("\\{TAG\\}", tag));
        }
        return result;
    }
}
