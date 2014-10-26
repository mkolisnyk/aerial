package com.github.mkolisnyk.aerial.core;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
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
        this.input = new AerialStringReader();
        this.output = new AerialStringWriter();
    }

    @After
    public void tearDown() throws Exception {
        this.input.close();
        this.output.close();
    }

    @Test
    public void testProcessOnValidDocument() throws Exception {
        String expected = "Feature: <feature name>" + ls
                + "\tScenario Outline: positive test" + ls
                + "\t\tGiven These are our pre-requisites" + ls
                + "\t\tWhen Sample action" + ls
                + "\t\tThen This is what we see on success" + ls
                + "\tExamples:" + ls
                + "\t\t| Test | ValidInput |" + ls
                + "\t\t| 50 | true  |" + ls
                + "\t\t| 0 | true  |" + ls
                + "\t\t" + ls
                + "\tScenario Outline: negative test" + ls
                + "\t\tGiven These are our pre-requisites" + ls
                + "\t\tWhen Sample action" + ls
                + "\t\tThen This is what we see on error" + ls
                + "\tExamples:" + ls
                + "\t\t| Test | ValidInput |" + ls
                + "\t\t| 100 | false |" + ls
                + "\t\t| -1 | false |" + ls
                + "\t\t| 101 | false |" + ls
                + "\t\t" + ls
                + "" + ls
                + "Scenario: Sample Scenario 1";
        this.input.open(sampleFeatureText);
        processor.process(input, output);
        Assert.assertEquals(expected, processor.getContent());
    }

}
