/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.AerialGlobalProperties;
import com.github.mkolisnyk.aerial.datagenerators.CustomCaseScenarioGenerator;

/**
 * @author Myk Kolisnyk
 *
 */
public class CaseSectionTest {

    private CaseSection section;
    private String ls = System.lineSeparator();

    private String sampleCaseDescription = "This is a sample test case" + ls
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Value |" + ls
            + "| Test | int  | [0;100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";
    private String sampleCaseText = sampleCaseDescription + ls
            + "Action:" + ls
            + sampleCaseAction + ls
            + "Input:" + ls
            + sampleCaseInput + ls
            + "On Success:" + ls
            + sampleCaseValidOutput + ls
            + "On Failure:" + ls
            + sampleCaseErrorOutput + ls
            + "Pre-requisites:" + ls
            + samplePrerequisites + ls;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        section = new CaseSection(null);
        section.setName("Sample Name");
        System.setProperty(AerialGlobalProperties.AERIAL_GEN_CUSTOM_CLASSES, "");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        System.setProperty(AerialGlobalProperties.AERIAL_GEN_CUSTOM_CLASSES, "");
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.document.CaseSection#validate(java.lang.String)}.
     * @throws Exception .
     */
    @Test
    public void testValidateAllContentShouldSucceed() throws Exception {
        section.parse(sampleCaseText);
        section.validate();
    }

    @Test
    public void testValidatePrerequisitesSectionIsOptional() throws Exception {
        section.parse(sampleCaseText);
        section.getSections().remove(Tokens.getPrerequisitesToken());
        Assert.assertNull(
                section.getSections().get(Tokens.getPrerequisitesToken()));
        section.validate();
    }

    @Test(expected = Throwable.class)
    public void testValidatePrerequisitesSectionShouldFailIfSetWrong() throws Exception {
        section.parse(sampleCaseText);
        section.getSections().put(Tokens.getPrerequisitesToken(), null);
        section.validate();
    }

    @Test
    public void testValidateShouldFailIfMandatorySectionsAreMissing() throws Exception {
        section.parse(sampleCaseText);
        for (String token : section.getMandatoryTokens()) {
            ArrayList<DocumentSection<?>> item = section.getSections().get(token);
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
     * @throws Exception .
     */
    @Test
    public void testParseForTextWithNoTokensShouldReturnJustDescription() throws Exception {
        String descriptionText = "This is sample case description"
                + ls + "With multiline";
        section.parse(descriptionText);
        Assert.assertEquals(descriptionText, section.getDescription());
        Assert.assertEquals(0, section.getSections().size());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.document.CaseSection#parse(java.lang.String)}.
     * @throws Exception .
     */
    @Test
    public void testParseValidScenarioShouldFillAllSections() throws Exception {
        section.parse(sampleCaseText);
        Assert.assertEquals(sampleCaseDescription, section.getDescription());
        Assert.assertEquals(samplePrerequisites,
           section.getSections().get(Tokens.getPrerequisitesToken()).get(0).getContent());
        Assert.assertEquals(sampleCaseInput,
           section.getSections().get(Tokens.getInputToken()).get(0).getContent());
        Assert.assertEquals(sampleCaseAction,
           section.getSections().get(Tokens.getActionToken()).get(0).getContent());
        Assert.assertEquals(sampleCaseValidOutput,
           section.getSections().get(Tokens.getValidOutputToken()).get(0).getContent());
        Assert.assertEquals(sampleCaseErrorOutput,
           section.getSections().get(Tokens.getErrorOutputToken()).get(0).getContent());
    }

    @Test
    public void testGenerateShouldReturnValidFormattedText() throws Exception {
        String shortOffset = StringUtils.repeat("    ", 1);
        String midOffset = StringUtils.repeat("    ", 2);
        String expected = shortOffset + "@positive " + ls
                          + shortOffset + "Scenario Outline: Sample Name positive test" + ls
                          + midOffset + "Given These are our pre-requisites" + ls
                          + midOffset + "When Sample action" + ls
                          + midOffset + "Then This is what we see on success" + ls
                          + shortOffset + "Examples:" + ls
                          + midOffset + "| Test | ValidInput |" + ls
                          + midOffset + "| 0 | true  |" + ls
                          + midOffset + "| 50 | true  |" + ls
                          + midOffset + "| 51 | true  |" + ls
                          + "" + ls
                          + shortOffset + "@negative " + ls
                          + shortOffset + "Scenario Outline: Sample Name negative test" + ls
                          + midOffset + "Given These are our pre-requisites" + ls
                          + midOffset + "When Sample action" + ls
                          + midOffset + "Then This is what we see on error" + ls
                          + shortOffset + "Examples:" + ls
                          + midOffset + "| Test | ValidInput |" + ls
                          + midOffset + "| 100 | false |" + ls
                          + midOffset + "| -1 | false |" + ls
                          + midOffset + "| 101 | false |" + ls
                          + ls;
        section.parse(sampleCaseText);
        String actual = section.generate();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGenerateWithUniqueFieldsShouldReturnUniqueValueScenario() throws Exception {
        String shortOffset = StringUtils.repeat("    ", 1);
        String midOffset = StringUtils.repeat("    ", 2);
        String sampleUniqueCaseText = sampleCaseDescription + ls
                + "Action:" + ls
                + "I test unique <Unique> value with <Non-Unique> value" + ls
                + "Input:" + ls
                + "| Name | Type | Value | Unique |" + ls
                + "| Unique | int  | [0;100) | true |" + ls
                + "| Unique2 | int  | [100;200) | true |" + ls
                + "| Non-Unique | Date  | [01-01-2000;02-10-2010], Format: dd-MM-yyyy | false |" + ls
                + "On Success:" + ls
                + sampleCaseValidOutput + ls
                + "On Failure:" + ls
                + sampleCaseErrorOutput + ls
                + "Pre-requisites:" + ls
                + samplePrerequisites + ls;
        String expected = ls
                + midOffset + "Given These are our pre-requisites" + ls
                + midOffset + "When I test unique <Unique> value with <Non-Unique> value" + ls
                + midOffset + "Then This is what we see on success" + ls
                + midOffset + "When I test unique <Modified Unique> value with <Non-Unique> value" + ls
                + midOffset + "Then This is what we see on error" + ls
                + shortOffset + "Examples:" + ls
                + midOffset + "| Unique | Modified Unique | ValidInput | Non-Unique | Unique2 | Modified Unique2 |" + ls
                + midOffset + "| 50 | 50 | true | 02-10-2010 | 150 | 151 |" + ls
                + midOffset + "| 50 | 0 | true | 02-10-2010 | 150 | 150 |" + ls + ls;
        section.parse(sampleUniqueCaseText);
        String actual = section.generate();
        Assert.assertEquals(expected, actual.split("Scenario Outline: Sample Name unique values test")[1]);
    }

    @Test
    public void testGenerateWithMandatoryFieldsShouldReturnMandatoryValueScenario() throws Exception {
        String shortOffset = StringUtils.repeat("    ", 1);
        String midOffset = StringUtils.repeat("    ", 2);
        String sampleUniqueCaseText = sampleCaseDescription + ls
                + "Action:" + ls
                + "I test unique <Mandatory> value with <Non-Mandatory> value" + ls
                + "and <Another Mandatory> value" + ls
                + "Input:" + ls
                + "| Name | Type | Value | Mandatory |" + ls
                + "| Mandatory | int  | [0;100) | true |" + ls
                + "| Another Mandatory | int  | [100;200) | true |" + ls
                + "| Non-Mandatory | Date  | [01-01-2000;02-10-2010], Format: dd-MM-yyyy | false |" + ls
                + "On Success:" + ls
                + sampleCaseValidOutput + ls
                + "On Failure:" + ls
                + sampleCaseErrorOutput + ls
                + "Pre-requisites:" + ls
                + samplePrerequisites + ls;
        String expected = ls
                + midOffset + "Given These are our pre-requisites" + ls
                + midOffset + "When I test unique <Mandatory> value with <Non-Mandatory> value" + ls
                + "and <Another Mandatory> value" + ls
                + midOffset + "Then This is what we see on error" + ls
                + shortOffset + "Examples:" + ls
                + midOffset + "| Non-Mandatory | Mandatory | ValidInput | Another Mandatory |" + ls
                + midOffset + "| 17-05-2005 |  | true  | 150 |" + ls
                + midOffset + "| 17-05-2005 | 50 | true  |  |" + ls
                + ls;
        section.parse(sampleUniqueCaseText);
        String actual = section.generate();
        Assert.assertEquals(expected, actual.split("Scenario Outline: Sample Name mandatory values")[1]);
    }


    @Test
    public void testGenerateWithCustomConditionsShouldHaveSplitInputResults() throws Exception {
        String sampleCaseText = sampleCaseDescription + ls
                + "Action:" + ls
                + "I test conditional <test> value" + ls
                + "Input:" + ls
                + "| Name | Type | Value | Condition |" + ls
                + "| test | String  | Condition1 | item <= 150 |" + ls
                + "| test | String  | Condition2 | item > 150 |" + ls
                + "| item | int  | [100;200) | |" + ls
                + "On Success:" + ls
                + "Successful completion" + ls
                + "On Failure:" + ls
                + "Error case" + ls
                + "Pre-requisites:" + ls
                + "Some pre-conditions" + ls;
        section.parse(sampleCaseText);
        String actual = section.generate();
        String expected = "    @positive " + ls
        + "    Scenario Outline: Sample Name positive test" + ls
        + "        Given Some pre-conditions" + ls
        + "        When I test conditional <test> value" + ls
        + "        Then Successful completion" + ls
        + "    Examples:" + ls
        + "        | test | item | ValidInput |" + ls
        + "        | Condition1 | 100 | true  |" + ls
        + "        | Condition2 | 151 | true  |" + ls
        + "        | Condition1 | 150 | true  |" + ls
        + "" + ls
        + "    @negative " + ls
        + "    Scenario Outline: Sample Name negative test" + ls
        + "        Given Some pre-conditions" + ls
        + "        When I test conditional <test> value" + ls
        + "        Then Error case" + ls
        + "    Examples:" + ls
        + "        | test | item | ValidInput |" + ls
        + "        |  | 100 | false |" + ls
        + "        | Condition2Condition2 | 151 | false |" + ls
        + "        | Condition2 | 201 | false |" + ls
        + "        | Condition2 | 200 | false |" + ls
        + "        | Condition1Condition1 | 99 | false |" + ls
        + "        | Condition1Condition1 | 150 | false |" + ls
        + "        | Condition1Condition1 | 100 | false |" + ls
        + "        | Condition1 | 99 | false |" + ls
        + "        |  | 99 | false |" + ls
        + "        |  | 201 | false |" + ls
        + "        |  | 200 | false |" + ls
        + "        |  | 151 | false |" + ls
        + "        |  | 150 | false |" + ls
        + "        | Condition2Condition2 | 200 | false |" + ls
        + "        | Condition2Condition2 | 201 | false |" + ls + ls;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGenerateWithCustomGeneratorShouldReturnCustomScenario() throws Exception {
        System.setProperty(AerialGlobalProperties.AERIAL_GEN_CUSTOM_CLASSES,
                CustomCaseScenarioGenerator.class.getCanonicalName());
        String sampleUniqueCaseText = sampleCaseDescription + ls
                + "Action:" + ls
                + "I test unique <Mandatory> value with <Non-Mandatory> value" + ls
                + "and <Another Mandatory> value" + ls
                + "Input:" + ls
                + "| Name | Type | Value | Mandatory |" + ls
                + "| Mandatory | int  | [0;100) | true |" + ls
                + "| Another Mandatory | int  | [100;200) | true |" + ls
                + "| Non-Mandatory | Date  | [01-01-2000;02-10-2010], Format: dd-MM-yyyy | false |" + ls
                + "On Success:" + ls
                + sampleCaseValidOutput + ls
                + "On Failure:" + ls
                + sampleCaseErrorOutput + ls
                + "Pre-requisites:" + ls
                + samplePrerequisites + ls;
        String expected = ls
                + "This is generated scenario    Examples:" + ls + ls;
        section.parse(sampleUniqueCaseText);
        String actual = section.generate();
        Assert.assertEquals(expected, actual.split("Scenario Outline: Sample Name custom scenario")[1]);
    }
}
