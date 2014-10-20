package com.github.mkolisnyk.aerial.document;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeatureSectionTest {

    private FeatureSection section;
    private String lineSeparator = System.lineSeparator();

    private String sampleFeatureDescription = "This is a sample feature" + lineSeparator
            + "With multiline description";
    private String sampleCaseDescription = "This is a sample test case" + lineSeparator
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Value   |" + lineSeparator
            + "| Test | Int  | [0;100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";

    private String sampleFeatureText =
            sampleFeatureDescription + lineSeparator
            + "Case:" + lineSeparator
            + sampleCaseDescription + lineSeparator
            + "Action:" + lineSeparator
            + sampleCaseAction + lineSeparator
            + "Input:" + lineSeparator
            + sampleCaseInput + lineSeparator
            + "On Success:" + lineSeparator
            + sampleCaseValidOutput + lineSeparator
            + "On Failure:" + lineSeparator
            + sampleCaseErrorOutput + lineSeparator
            + "Pre-requisites:" + lineSeparator
            + samplePrerequisites + lineSeparator
            + "Additional Scenarios:" + lineSeparator
            + "Scenario: Sample Scenario 1";

    @Before
    public void setUp() throws Exception {
        section = new FeatureSection(null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidateAllContentShouldSucceed() throws Exception {
        section.parse(sampleFeatureText);
        section.validate();
    }

    @Test
    public void testValidateScenariosSectionIsOptional() throws Exception {
        section.parse(sampleFeatureText);
        section.getSections().remove(Tokens.ADDITIONAL_SCENARIOS_TOKEN);
        Assert.assertNull(
                section.getSections().get(Tokens.ADDITIONAL_SCENARIOS_TOKEN));
        section.validate();
    }

    @Test(expected = Throwable.class)
    public void testValidateScenariosSectionShouldFailIfSetWrong() throws Exception {
        section.parse(sampleFeatureText);
        section.getSections().put(Tokens.ADDITIONAL_SCENARIOS_TOKEN, null);
        section.validate();
    }

    @Test
    public void testValidateShouldFailIfMandatorySectionsAreMissing() throws Exception {
        section.parse(sampleFeatureText);
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

    @Test
    public void testParseForTextWithNoTokensShouldReturnJustDescription() throws Exception {
        String descriptionText = "This is sample case description"
                + lineSeparator + "With multiline";
        section.parse(descriptionText);
        Assert.assertEquals(descriptionText, section.getDescription());
        Assert.assertEquals(0, section.getSections().size());
    }

    @Test
    public void testParseValidScenarioShouldFillAllSections() throws Exception {
        section.parse(sampleFeatureText);
        Assert.assertEquals(sampleFeatureDescription, section.getDescription());
        Assert.assertNotNull(section.getSections().get(Tokens.CASE_TOKEN));
        Assert.assertNotNull(section.getSections()
                .get(Tokens.ADDITIONAL_SCENARIOS_TOKEN));
    }
}
