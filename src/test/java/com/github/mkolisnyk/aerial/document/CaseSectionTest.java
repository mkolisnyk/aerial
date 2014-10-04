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
public class CaseSectionTest {

    private CaseSection section;
    private String lineSeparator = System.lineSeparator();

    private String sampleCaseDescription = "This is a sample test case" + lineSeparator
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Format   |" + lineSeparator
            + "| Test | Int  | [0; 100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";
    private String sampleCaseText = sampleCaseDescription + lineSeparator
            + "Action:" + lineSeparator
            + sampleCaseAction + lineSeparator
            + "Input:" + lineSeparator
            + sampleCaseInput + lineSeparator
            + "On Success:" + lineSeparator
            + sampleCaseValidOutput + lineSeparator
            + "On Failure:" + lineSeparator
            + sampleCaseErrorOutput + lineSeparator
            + "Pre-requisites:" + lineSeparator
            + samplePrerequisites + lineSeparator;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        section = new CaseSection(null);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.document.CaseSection#validate(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testValidateAllContentShouldSucceed() throws Exception {
        section.parse(sampleCaseText);
        section.validate();
    }

    @Test
    public void testValidatePrerequisitesSectionIsOptional() throws Exception {
        section.parse(sampleCaseText);
        section.getSections().remove(Tokens.PREREQUISITES_TOKEN);
        Assert.assertNull(
                section.getSections().get(Tokens.PREREQUISITES_TOKEN));
        section.validate();
    }

    @Test(expected = Throwable.class)
    public void testValidatePrerequisitesSectionShouldFailIfSetWrong() throws Exception {
        section.parse(sampleCaseText);
        section.getSections().put(Tokens.PREREQUISITES_TOKEN, null);
        section.validate();
    }

    @Test
    public void testValidateShouldFailIfMandatorySectionsAreMissing() throws Exception {
        section.parse(sampleCaseText);
        for (String token : section.getMandatoryTokens()) {
            DocumentSection<?> item = section.getSections().get(token);
            section.getSections().remove(token);
            try {
                section.validate();
            } catch (Throwable e) {
                section.getSections().put(token, item);
                continue;
            }
            Assert.fail("Validation was supposed to fail for empty token: "
                    + token);
        }
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.document.CaseSection#getDescription()}.
     * @throws Exception 
     */
    @Test
    public void testParseForTextWithNoTokensShouldReturnJustDescription() throws Exception {
        String descriptionText = "This is sample case description"
                + lineSeparator + "With multiline";
        section.parse(descriptionText);
        Assert.assertEquals(descriptionText, section.getDescription());
        Assert.assertEquals(0, section.getSections().size());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.document.CaseSection#parse(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testParseValidScenarioShouldFillAllSections() throws Exception {
        section.parse(sampleCaseText);
        Assert.assertEquals(sampleCaseDescription, section.getDescription());
        Assert.assertEquals(samplePrerequisites,
           section.getSections().get(Tokens.PREREQUISITES_TOKEN).getContent());
        Assert.assertEquals(sampleCaseInput,
           section.getSections().get(Tokens.INPUT_TOKEN).getContent());
        Assert.assertEquals(sampleCaseAction,
           section.getSections().get(Tokens.ACTION_TOKEN).getContent());
        Assert.assertEquals(sampleCaseValidOutput,
           section.getSections().get(Tokens.VALID_OUTPUT_TOKEN).getContent());
        Assert.assertEquals(sampleCaseErrorOutput,
           section.getSections().get(Tokens.ERROR_OUTPUT_TOKEN).getContent());
    }

}
