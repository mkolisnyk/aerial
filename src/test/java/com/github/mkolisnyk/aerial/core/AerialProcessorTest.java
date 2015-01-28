package com.github.mkolisnyk.aerial.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.readers.AerialStringReader;
import com.github.mkolisnyk.aerial.writers.AerialStringWriter;

public class AerialProcessorTest {

    private AerialProcessor processor;
    private AerialReader input;
    private AerialWriter output;
    private String ls = System.lineSeparator();

    private String sampleDocumentDescription = "This is a sample document" + ls
            + "With multiline description";
    private String sampleFeatureDescription = "This is a sample feature" + ls
            + "With multiline description";
    private String sampleCaseDescription = "This is a sample test case" + ls
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Value   |" + ls
            + "| Test | Int  | [0;100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";

    private String sampleFeatureText =
            sampleDocumentDescription + ls
            + "Feature:" + ls
            + sampleFeatureDescription + ls
            + "Case:" + ls
            + sampleCaseDescription + ls
            + "Action:" + ls
            + sampleCaseAction + ls
            + "Input:" + ls
            + sampleCaseInput + ls
            + "On Success:" + ls
            + sampleCaseValidOutput + ls
            + "On Failure:" + ls
            + sampleCaseErrorOutput + ls
            + "Pre-requisites:" + ls
            + samplePrerequisites + ls
            + "Additional Scenarios:" + ls
            + "Scenario: Sample Scenario 1";

    @Before
    public void setUp() throws Exception {
        this.processor = new AerialProcessor();
        AerialParams params = new AerialParams();
        params.parse(new String[] {sampleFeatureText, sampleFeatureText});
        this.input = new AerialStringReader(params);
        this.output = new AerialStringWriter();
    }

    @After
    public void tearDown() throws Exception {
        this.input.close();
        this.output.close();
    }

    @Test
    public void testProcessOnValidDocument() throws Exception {
        String expected = "Feature: " + ls
                + "    Scenario Outline:  positive test" + ls
                + "        Given These are our pre-requisites" + ls
                + "        When Sample action" + ls
                + "        Then This is what we see on success" + ls
                + "    Examples:" + ls
                + "        | Test | ValidInput |" + ls
                + "        | 0 | true  |" + ls
                + "        | 50 | true  |" + ls
                + "        | 51 | true  |" + ls
                + "" + ls
                + "    Scenario Outline:  negative test" + ls
                + "        Given These are our pre-requisites" + ls
                + "        When Sample action" + ls
                + "        Then This is what we see on error" + ls
                + "    Examples:" + ls
                + "        | Test | ValidInput |" + ls
                + "        | 100 | false |" + ls
                + "        | -1 | false |" + ls
                + "        | 101 | false |" + ls
                + "" + ls
                + "" + ls
                + "Scenario: Sample Scenario 1";
        processor.process(input, output);
        Assert.assertEquals(StringUtils.repeat(expected, 2), processor.getContent());
    }

}
