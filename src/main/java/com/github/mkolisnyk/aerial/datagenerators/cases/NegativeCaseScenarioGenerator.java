package com.github.mkolisnyk.aerial.datagenerators.cases;

import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.document.CaseSection;
import com.github.mkolisnyk.aerial.document.InputRecord;

public class NegativeCaseScenarioGenerator extends PositiveCaseScenarioGenerator {

    public NegativeCaseScenarioGenerator(CaseSection sectionData,
            List<InputRecord> recordsList,
            Map<String, List<String>> testDataMap) {
        super(sectionData, recordsList, testDataMap);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.datagenerators.cases.PositiveCaseScenarioGenerator#isPositive()
     */
    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public String getScenarioName() {
        return " negative test";
    }
}
