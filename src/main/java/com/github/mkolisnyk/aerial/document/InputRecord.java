/**
 * 
 */
package com.github.mkolisnyk.aerial.document;

import org.junit.Assert;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Myk Kolisnyk
 *
 */
public class InputRecord {

    private String name;
    private String type;
    private String value;
    private String condition;

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
        super();
        this.name = nameParam;
        this.type = typeParam;
        this.value = valueParam;
        this.condition = conditionParam;
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
}
