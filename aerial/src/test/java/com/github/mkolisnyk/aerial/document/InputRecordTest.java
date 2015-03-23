package com.github.mkolisnyk.aerial.document;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InputRecordTest {

    private String readLine;
    private String readHeaderLine;
    private boolean shouldFail;
    private String name;
    private String type;
    private String value;
    private String condition;
    private boolean unique;
    private boolean mandatory;

    private InputRecord record;

    public InputRecordTest(
            String description,
            String readLineParam,
            String readHeaderLineParam,
            boolean shouldFailParam,
            String nameParam,
            String typeParam,
            String valueParam,
            String conditionParam,
            boolean uniqueParam,
            boolean mandatoryParam) {
        super();
        this.readLine = readLineParam;
        this.readHeaderLine = readHeaderLineParam;
        this.shouldFail = shouldFailParam;
        this.name = nameParam;
        this.type = typeParam;
        this.value = valueParam;
        this.condition = conditionParam;
        this.unique = uniqueParam;
        this.mandatory = mandatoryParam;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Normal set of fields",  "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition |", false, "nameValue", "int", "[0;100)", "a > 0", false, false},
                {"Only type is missing",  "| nameValue | [0;100) | a > 0 |", "| Name | Value | Condition |", false, "nameValue", "", "[0;100)", "a > 0", false, false},
                {"Only value is missing", "| nameValue | int | a > 0 |", "| Name | Type | Condition |", false, "nameValue", "int", "", "a > 0", false, false},
                {"Only no condition",     "| nameValue | int | [0;100) |", "| Name | Type | Value |", false, "nameValue", "int", "[0;100)", "", false, false},
                {"Different order should fill values properly",  "| int | nameValue | a > 0 | [0;100) |", "| Type | Name | Condition | Value |", false, "nameValue", "int", "[0;100)", "a > 0", false, false},
                {"Varying header case should still work",  "| nameValue | int | [0;100) | a > 0 |", "| nAmE | TyPE | vaLUe | CoNDiTiON |", false, "nameValue", "int", "[0;100)", "a > 0", false, false},
                {"Spaces in header names should still work",  "| nameValue | int | [0;100) | a > 0 |", "| n A m E | T y P E | v a L U e | C o N D i T i O N |", false, "nameValue", "int", "[0;100)", "a > 0", false, false},
                {"The size of header doesn't match the line", "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value |", true, "", "", "", "", false, false},
                {"Malformed Header should fail", "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition", true, "", "", "", "", false, false},
                {"Malformed Line should fail", "| nameValue | int | [0;100) | a > 0", "| Name | Type | Value | Condition |", true, "", "", "", "", false, false},
                {"Name field shouldn't be empty",  "| | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition |", true, "", "", "", "", false, false},
                {"Name field should be present",  "| int | [0;100) | a > 0 |", "| Type | Value | Condition |", true, "", "", "", "", false, false},
                {"Unique field set",  "| nameValue | int | [0;100) | a > 0 | true |", "| Name | Type | Value | Condition | Unique |", false, "nameValue", "int", "[0;100)", "a > 0", true, false},
                {"Mandatory field set",  "| nameValue | int | [0;100) | a > 0 | true |", "| Name | Type | Value | Condition | Mandatory |", false, "nameValue", "int", "[0;100)", "a > 0", false, true},
        });
    }

    @Before
    public void setUp() throws Exception {
        record = new InputRecord();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRead() {
        try {
            this.record.read(readLine, readHeaderLine);
        } catch (Throwable e) {
            //e.printStackTrace();
            Assert.assertTrue(
                    "This input wasn't supposed to fail!!!", this.shouldFail);
            return;
        }
        Assert.assertEquals(
                "The name is unexpected",
                this.name,
                this.record.getName());
        Assert.assertEquals(
                "The value is unexpected",
                this.value,
                this.record.getValue());
        Assert.assertEquals(
                "The type is unexpected",
                this.type,
                this.record.getType());
        Assert.assertEquals(
                "The condition is unexpected",
                this.condition,
                this.record.getCondition());
        Assert.assertEquals(
                "Expected and actual records aren't the same",
                new InputRecord(
                        this.name,
                        this.type,
                        this.value,
                        this.condition,
                        true,
                        "" + this.unique,
                        "" + this.mandatory),
                this.record);
        Assert.assertEquals(
                "Hash codes aren't the same",
                this.record.hashCode(),
                (new InputRecord(
                        this.name,
                        this.type,
                        this.value,
                        this.condition,
                        true,
                        "" + this.unique,
                        "" + this.mandatory)).hashCode());
        Assert.assertFalse(this.record.equals(null));
        Assert.assertFalse(this.record.equals(this.unique));
        Assert.assertEquals(this.unique, this.record.isUnique());
        Assert.assertEquals(this.mandatory, this.record.isMandatory());
    }

}
