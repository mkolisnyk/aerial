/**
 * 
 */
package com.github.mkolisnyk.aerial.document;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Myk Kolisnyk
 *
 */
public class GenerateTest {
    private static String ls = System.lineSeparator();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInputSectionGenerateShouldReturnFormattedText() throws Exception {
        String input = "| Name | Type | Value | Condition |" + ls + "| nameValue | int | [0;100) | a > 0 |";
        InputSection section = new InputSection(null);
        section.parse(input);
        String actual = section.generate();
        Assert.assertNull(actual);
    }

}
