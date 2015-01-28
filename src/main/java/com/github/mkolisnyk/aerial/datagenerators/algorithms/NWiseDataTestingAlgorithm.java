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

    public List<FieldsRecord> getUniqueCombinations(String[] fieldNames) {
        Map<String, List<String>> distinct = distinctTestData();
        List<FieldsRecord> result = new ArrayList<FieldsRecord>();
        if (fieldNames.length > 1) {
            List<FieldsRecord> subMap = getUniqueCombinations((String[]) ArrayUtils.remove(fieldNames, 0));
            for (String value : distinct.get(fieldNames[0])) {
                for (FieldsRecord subRow : subMap) {
                    Map<String, String> dataItem = new HashMap<String, String>();
                    dataItem.put(fieldNames[0], value);
                    for (Entry<String, String> entry : subRow.getData().entrySet()) {
                        dataItem.put(entry.getKey(), entry.getValue());
                    }
                    result.add(new FieldsRecord(dataItem));
                }
            }
        } else {
            for (String value : distinct.get(fieldNames[0])) {
                Map<String, String> dataItem = new HashMap<String, String>();
                dataItem.put(fieldNames[0], value);
                result.add(new FieldsRecord(dataItem));
            }
        }
        return result;
    }

    public List<FieldsRecord> getUniqueCombinations() {
        List<FieldsRecord> result = new ArrayList<FieldsRecord>();
        String[] fieldNames = new String[this.getTestData().keySet().size()];
        fieldNames = this.getTestData().keySet().toArray(fieldNames);
        List<String[]> columnGroups = this.getColumnGroups(fieldNames);
        for (String[] group : columnGroups) {
            for (FieldsRecord record : getUniqueCombinations(group)) {
                result.add(record);
            }
        }
        return result;
    }

    private FieldsRecord updateTable(FieldsTable table, FieldsRecord row) {
        for (FieldsRecord record:table.getData().keySet()) {
            if (row.matches(record)) {
                table.increment(record);
            }
        }
        return row;
    }

    private FieldsRecord getNextRow(FieldsTable table, List<FieldsRecord> totalRecords) {
        if (table.areAllNonZeros()) {
            return null;
        }
        FieldsRecord record = table.getSorted().firstKey();

        FieldsRecord result = null;
        for (int i = 0; i < totalRecords.size(); i++) {
            result = totalRecords.get(i);
            if (result.matches(record)) {
                result = updateTable(table, result);
                break;
            }
        }
        totalRecords.remove(result);
        return result;
    }

    public Map<String, List<String>> generateTestData() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        List<FieldsRecord> totalRecords = new ArrayList<FieldsRecord>();
        int count = this.getTestData().get(this.getTestData().keySet().iterator().next()).size();
        for (int i = 0; i < count; i++) {
            Map<String, String> localMap = new HashMap<String, String>();
            for (Entry<String, List<String>> entry:this.getTestData().entrySet()) {
                localMap.put(entry.getKey(), entry.getValue().get(i));
            }
            totalRecords.add(new FieldsRecord(localMap));
        }
        List<FieldsRecord> pairedRows = new ArrayList<FieldsRecord>();
        FieldsTable table = new FieldsTable();
        table.add(getUniqueCombinations());
        for (int i = 0; i < count; i++) {
            FieldsRecord row = getNextRow(table, totalRecords);
            if (row == null) {
                break;
            } else {
                pairedRows.add(row);
            }
        }
        for (FieldsRecord row : pairedRows) {
            for (Entry<String, String> entry : row.getData().entrySet()) {
                if (!result.containsKey(entry.getKey())) {
                    result.put(entry.getKey(), new ArrayList<String>());
                }
                result.get(entry.getKey()).add(entry.getValue());
            }
        }
        return result;
    }
}
