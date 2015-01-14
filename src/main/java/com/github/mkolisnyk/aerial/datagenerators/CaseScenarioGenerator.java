package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.DocumentSection;
import com.github.mkolisnyk.aerial.document.InputRecord;

public abstract class CaseScenarioGenerator {
    private String ls = System.lineSeparator();

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
                result = result.concat(item.generate()  + ls);
            }
        }
        return result;
    }

    public abstract String generate() throws Exception;

    public abstract boolean isApplicable();

    public abstract String getScenarioName();
}
