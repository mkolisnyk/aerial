package com.github.mkolisnyk.aerial.document;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InputSectionTest {

    private InputSection section;

    private String inputText;

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Normal set of fields",  "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition |", false, "nameValue", "int", "[0;100)", "a > 0"},
                {"Only type is missing",  "| nameValue | [0;100) | a > 0 |", "| Name | Value | Condition |", false, "nameValue", "", "[0;100)", "a > 0"},
                {"Only value is missing", "| nameValue | int | a > 0 |", "| Name | Type | Condition |", false, "nameValue", "int", "", "a > 0"},
                {"Only no condition",     "| nameValue | int | [0;100) |", "| Name | Type | Value |", false, "nameValue", "int", "[0;100)", ""},
                {"Different order should fill values properly",  "| int | nameValue | a > 0 | [0;100) |", "| Type | Name | Condition | Value |", false, "nameValue", "int", "[0;100)", "a > 0"},
                {"Varying header case should still work",  "| nameValue | int | [0;100) | a > 0 |", "| nAmE | TyPE | vaLUe | CoNDiTiON |", false, "nameValue", "int", "[0;100)", "a > 0"},
                {"Spaces in header names should still work",  "| nameValue | int | [0;100) | a > 0 |", "| n A m E | T y P E | v a L U e | C o N D i T i O N |", false, "nameValue", "int", "[0;100)", "a > 0"},
                {"The size of header doesn't match the line", "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value |", true, "", "", "", ""},
                {"Malformed Header should fail", "| nameValue | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition", true, "", "", "", ""},
                {"Malformed Line should fail", "| nameValue | int | [0;100) | a > 0", "| Name | Type | Value | Condition |", true, "", "", "", ""},
                {"Name field shouldn't be empty",  "| | int | [0;100) | a > 0 |", "| Name | Type | Value | Condition |", true, "", "", "", ""},
                {"Name field should be present",  "| int | [0;100) | a > 0 |", "| Type | Value | Condition |", true, "", "", "", ""},
        });
    }

    @Before
    public void setUp() throws Exception {
        section = new InputSection(null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseString() {
        ;
    }

}
