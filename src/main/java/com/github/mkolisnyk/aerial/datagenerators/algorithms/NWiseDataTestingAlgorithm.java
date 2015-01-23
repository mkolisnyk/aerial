package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class NWiseDataTestingAlgorithm {

    private Map<String, List<String>> testData;
    private int recordSize = 2;

    /**
     * @return the testData
     */
    public final Map<String, List<String>> getTestData() {
        return testData;
    }

    public NWiseDataTestingAlgorithm(Map<String, List<String>> initialTestData, int recordSizeValue) {
        this.testData = initialTestData;
        this.recordSize = recordSizeValue;
    }

    public Map<String, List<String>> distinctTestData() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        for (String key : this.getTestData().keySet()) {
            List<String> distinct = new ArrayList<String>();
            for (String value : this.getTestData().get(key)) {
                if (!distinct.contains(value)) {
                    distinct.add(value);
                }
            }
            result.put(key, distinct);
        }
        return result;
    }

    public List<String[]> getColumnGroups(String[] fieldNames) {
        List<String[]> result = new ArrayList<String[]>();
        ICombinatoricsVector<String> initialVector = Factory.createVector(fieldNames);

        Generator<String> gen = Factory.createSimpleCombinationGenerator(initialVector, this.recordSize);

        for (ICombinatoricsVector<String> combination : gen) {
            String[] vector = new String[this.recordSize];
            vector = combination.getVector().toArray(vector);
            result.add(vector);
        }
        return result;
    }

    public List<Map<String, String>> getUniqueCombinations(String[] fieldNames) {
        Map<String, List<String>> distinct = distinctTestData();
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        if (fieldNames.length > 1) {
            List<Map<String, String>> subMap = getUniqueCombinations((String[]) ArrayUtils.remove(fieldNames, 0));
            for (String value : distinct.get(fieldNames[0])) {
                for (Map<String, String> subRow : subMap) {
                    Map<String, String> dataItem = new HashMap<String, String>();
                    dataItem.put(fieldNames[0], value);
                    for (Entry<String, String> entry : subRow.entrySet()) {
                        dataItem.put(entry.getKey(), entry.getValue());
                    }
                    result.add(dataItem);
                }
            }
        } else {
            for (String value : distinct.get(fieldNames[0])) {
                Map<String, String> dataItem = new HashMap<String, String>();
                dataItem.put(fieldNames[0], value);
                result.add(dataItem);
            }
        }
        return result;
    }

    public List<Map<String, String>> getUniqueCombinations() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        String[] fieldNames = new String[this.getTestData().keySet().size()];
        fieldNames = this.getTestData().keySet().toArray(fieldNames);
        List<String[]> columnGroups = this.getColumnGroups(fieldNames);
        for (String[] group : columnGroups) {
            result.addAll(getUniqueCombinations(group));
        }
        return result;
    }
}
