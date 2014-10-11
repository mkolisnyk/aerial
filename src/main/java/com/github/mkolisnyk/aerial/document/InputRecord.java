/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

/**
 * @author Myk Kolisnyk
 *
 */
public class InputRecord implements Cloneable {

    private String name;
    private String type;
    private String value;
    private String condition;
    private boolean validInput;

    /**
     * .
     */
    public InputRecord() {
        this("", "", "", "");
    }

    public InputRecord(
            String nameParam,
            String typeParam,
            String valueParam,
            String conditionParam) {
        this(nameParam, typeParam, valueParam, conditionParam, true);
    }

    public InputRecord(
            String nameParam,
            String typeParam,
            String valueParam,
            String conditionParam,
            boolean validInputParam) {
        this.name = nameParam;
        this.type = typeParam;
        this.value = valueParam;
        this.condition = conditionParam;
        this.validInput = validInputParam;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @return the condition
     */
    public final String getCondition() {
        return condition;
    }

    /**
     * @return the validInput
     */
    public final boolean isValidInput() {
        return validInput;
    }

    public void read(String line, String headerLine) throws Exception {
        Assert.assertTrue("The header doesn't match the format: |(.*)|",
                headerLine.trim().matches("[|](.*)[|]"));
        Assert.assertTrue("The record row doesn't match the format: |(.*)|",
                line.trim().matches("[|](.*)[|]"));
        String[] lineItems =
                (String[]) ArrayUtils.subarray(
                        line.split("[|]"),
                        1,
                        StringUtils.countMatches(line, "|"));
        String[] headerItems =
                (String[]) ArrayUtils.subarray(
                        headerLine.split("[|]"),
                        1,
                        StringUtils.countMatches(headerLine, "|"));
        Assert.assertEquals(
                "The size of header and line is not the same",
                lineItems.length,
                headerItems.length);
        for (int i = 0; i < headerItems.length; i++) {
            this.getClass()
                .getDeclaredField(
                        headerItems[i].trim().toLowerCase().replaceAll(" ", ""))
                .set(this, lineItems[i].trim());
        }
        Assert.assertFalse(
                "The name field shouldn't be empty",
                this.name.trim().equals(""));
    }

    public List<InputRecord> expand() {
        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((condition == null) ? 0 : condition.hashCode());
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((type == null) ? 0 : type.hashCode());
        result = prime * result + (validInput ? 1231 : 1237);
        result = prime * result
                + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InputRecord)) {
            return false;
        }
        InputRecord other = (InputRecord) obj;
        return other.toString().equals(this.toString());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InputRecord [name=" + name + ", type=" + type
                + ", value=" + value + ", condition=" + condition
                + ", validInput=" + validInput + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
