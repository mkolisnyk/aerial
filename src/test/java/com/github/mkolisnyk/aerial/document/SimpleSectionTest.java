/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Myk Kolisnyk
 *
 */
@RunWith(Parameterized.class)
public class SimpleSectionTest {

    private DocumentSection<?> section;
    private String content;
    private Class<?> clazz;
    private String expected;
    private static String contentString = "I log into the system\n"
            + "Navigate to the Home Page";

    public SimpleSectionTest(Class<?> classValue, String description, String expectedValue) {
        this.clazz = classValue;
        this.expected = expectedValue;
    }

    @Parameters(name = "Test: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ActionSection.class, "Action Section", "When " + contentString},
                {ValidOutput.class, "Valid Output Section", "Then " + contentString},
                {ErrorOutput.class, "Error Output Section", "Then " + contentString},
                {PrerequisitesSection.class, "Prerequisites Section", "Given " + contentString},
                {AdditionalScenariosSection.class,
                                        "Additional Scenario Section", contentString}
           });
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        section = (DocumentSection<?>) clazz.getConstructors()[0]
                                            .newInstance(new Object[]{null});
        content = contentString;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseValidFormat() throws Exception {
        section = section.parse(content);
        Assert.assertNull(section.getParent());
        Assert.assertEquals(content, section.getContent());
    }

    @Test(expected = Throwable.class)
    public void testParseNullStringShouldThrowError() throws Exception {
        section.parse(null);
    }

    @Test(expected = Throwable.class)
    public void testParseEmptyStringShouldThrowError() throws Exception {
        section.parse("");
    }

    @Test
    public void testValidateValidFormat() {
        try {
            section.validate(content);
        } catch (Throwable e) {
            Assert.fail("No exceptions expected for valid content");
        }
    }

    @Test(expected = Throwable.class)
    public void testValidateShouldReturnExceptionOnEmptyInput() throws Exception {
        section.validate("");
    }

    @Test(expected = Throwable.class)
    public void testValidateShouldReturnExceptionOnNullInput() throws Exception {
        section.validate(null);
    }

    @Test
    public void testGenerateShouldReturnFormattedString() throws Exception {
        section = section.parse(content);
        String actual = section.generate();
        Assert.assertEquals("Generated string differs from expected", this.expected, actual);
    }
}
