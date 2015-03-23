/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

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
    private String unique;
    private String mandatory;

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
        this(nameParam, typeParam, valueParam, conditionParam, validInputParam, "false");
    }

    public InputRecord(
            String nameParam,
            String typeParam,
            String valueParam,
            String conditionParam,
            boolean validInputParam,
            String uniqueParam) {
        this(nameParam, typeParam, valueParam, conditionParam, validInputParam, uniqueParam, "false");
    }

    public InputRecord(
            String nameParam,
            String typeParam,
            String valueParam,
            String conditionParam,
            boolean validInputParam,
            String uniqueParam,
            String mandatoryParam) {
        this.name = nameParam;
        this.type = typeParam;
        this.value = valueParam;
        this.condition = conditionParam;
        this.validInput = validInputParam;
        this.unique = uniqueParam;
        this.mandatory = mandatoryParam;
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

    /**
     * @return the unique
     */
    public final boolean isUnique() {
        return unique.trim().equalsIgnoreCase("true");
    }

    /**
     * @return the unique
     */
    public final boolean isMandatory() {
        return mandatory.trim().equalsIgnoreCase("true");
    }

    /**
     * @param valueValue the value to set
     */
    public final void setValue(String valueValue) {
        this.value = valueValue;
    }

    /**
     * @param validInputValue the validInput to set
     */
    public final void setValidInput(boolean validInputValue) {
        this.validInput = validInputValue;
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
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
                + ", validInput=" + validInput + ", unique=" + unique
                + ", mandatory=" + mandatory + "]";
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
