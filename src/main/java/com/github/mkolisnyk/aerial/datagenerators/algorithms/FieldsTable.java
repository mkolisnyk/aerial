package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class FieldsTable {

    private Map<FieldsRecord, Integer> data;

    public FieldsTable() {
        this.data = new TreeMap<FieldsRecord, Integer>();
    }

    /**
     * @return the data
     */
    public final Map<FieldsRecord, Integer> getData() {
        return data;
    }

    public void add(List<FieldsRecord> input) {
        if (input == null) {
            return;
        }
        for (FieldsRecord item : input) {
            this.getData().put(item, 0);
        }
    }

    public void increment(FieldsRecord record) {
        if (!this.getData().containsKey(record)) {
            this.getData().put(record, 0);
        } else {
            int value = this.getData().get(record);
            value++;
            this.getData().put(record, value);
        }
    }

    public boolean areAllNonZeros() {
        for (Entry<FieldsRecord, Integer> record : this.getData().entrySet()) {
            if (record.getValue() <= 0) {
                return false;
            }
        }
        return true;
    }

    public TreeMap<FieldsRecord, Integer> getSorted() {
        ValueComparator vc =  new ValueComparator(this.data);
        TreeMap<FieldsRecord, Integer> sortedMap = new TreeMap<FieldsRecord, Integer>(vc);
        sortedMap.putAll(this.data);
        return sortedMap;
    }

    public static class ValueComparator implements Comparator<FieldsRecord>, Serializable {

        private static final long serialVersionUID = 1L;
        private Map<FieldsRecord, Integer> map;

        public ValueComparator(Map<FieldsRecord, Integer> base) {
            this.map = base;
        }

        public int compare(FieldsRecord a, FieldsRecord b) {
            if (map.get(a) >= map.get(b)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
