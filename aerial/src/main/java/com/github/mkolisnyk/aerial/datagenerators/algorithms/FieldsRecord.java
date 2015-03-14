package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.util.Map;
import java.util.Map.Entry;

public class FieldsRecord implements Comparable<Object> {
    private Map<String, String> data;

    public FieldsRecord(Map<String, String> dataValue) {
        this.data = dataValue;
    }

    /**
     * @return the data
     */
    public final Map<String, String> getData() {
        return data;
    }

    public boolean matches(FieldsRecord other) {
        if (!this.getData().keySet().containsAll(other.getData().keySet())) {
            return false;
        }
        for (Entry<String, String> entry : other.getData().entrySet()) {
            if (!this.getData().get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public int rightDiff(FieldsRecord other) {
        int count = 0;
        for (Entry<String, String> entry : other.getData().entrySet()) {
            if (!this.getData().containsKey(entry.getKey())) {
                count++;
            }
        }
        return count;
    }

    public int rightValueDiff(FieldsRecord other) {
        int count = 0;
        for (Entry<String, String> entry : other.getData().entrySet()) {
            if (this.getData().containsKey(entry.getKey())
                    && !this.getData().get(entry.getKey()).equals(entry.getValue())) {
                count++;
            }
        }
        return count;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        result += data.hashCode();
        return result;
    }

    private boolean equalMaps(Map<String, String> valueA, Map<String, String> valueB) {
        for (Entry<String, String> entry : valueA.entrySet()) {
            if (!valueB.containsKey(entry.getKey())
                    || !valueB.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FieldsRecord)) {
            return false;
        }
        FieldsRecord other = (FieldsRecord) obj;
        return equalMaps(this.getData(), other.getData()) && equalMaps(other.getData(), this.getData());
    }

    @Override
    public int compareTo(Object arg0) {
        if (arg0 == null || !(arg0 instanceof FieldsRecord)) {
            return -1;
        }
        if (this.equals(arg0)) {
            return 0;
        } else {
            FieldsRecord other = (FieldsRecord) arg0;
            if (this.hashCode() >= other.hashCode()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}

