package com.github.mkolisnyk.aerial.document;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;
    private String lineSeparator = System.lineSeparator();

    private String sampleDocumentDescription = "This is a sample document" + lineSeparator
            + "With multiline description";
    private String sampleFeatureDescription = "This is a sample feature" + lineSeparator
            + "With multiline description";
    private String sampleCaseDescription = "This is a sample test case" + lineSeparator
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Format   |" + lineSeparator
            + "| Test | Int  | [0; 100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";

    private String sampleFeatureText =
            sampleDocumentDescription + lineSeparator
            + "Feature:" + lineSeparator 
            + sampleFeatureDescription + lineSeparator
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
        document = new Document();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidateAllContentShouldSucceed() throws Exception {
        document.parse(sampleFeatureText);
        document.validate();
    }

    @Test
    public void testValidateShouldFailIfMandatorySectionsAreMissing() throws Exception {
        document.parse(sampleFeatureText);
        for (String token : document.getMandatoryTokens()) {
            DocumentSection<?> item = document.getSections().get(token);
            document.getSections().remove(token);
            try {
                document.validate();
            } catch (Throwable e) {
                document.getSections().put(token, item);
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
        document.parse(descriptionText);
        Assert.assertEquals(descriptionText, document.getDescription());
        Assert.assertEquals(0, document.getSections().size());
    }

    @Test
    public void testParseValidScenarioShouldFillAllSections() throws Exception {
        document.parse(sampleFeatureText);
        Assert.assertEquals(sampleDocumentDescription, document.getDescription());
        Assert.assertNotNull(document.getSections().get(Tokens.FEATURE_TOKEN));
    }
}
