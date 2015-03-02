package com.github.mkolisnyk.aerial.writers;

import org.junit.Assert;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.AerialTagList;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;
import com.github.mkolisnyk.aerial.document.Document;
import com.github.mkolisnyk.aerial.readers.AerialFileReader;

public class AerialFileWriterTest {

    private AerialFileReader reader;
    private AerialFileWriter writer;
    private String lineSeparator = System.lineSeparator();

    @Before
    public void setUp() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        AerialParams params = new AerialParams();
        params.parse(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.SOURCE.toString(), "src/test/resources/",
                AerialParamKeys.DESTINATION.toString(), "src/test/resources/temp",
                fileName
        });
        writer = new AerialFileWriter(params);
        reader = new AerialFileReader(params, new AerialTagList());
        reader.open(params);
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
        writer.close();
        FileUtils.deleteDirectory(new File("src/test/resources/temp"));
    }

    @Test
    public void testSampleGenerateDocumentShouldProduceValidFormattedFile() throws Exception {
        String expected = "@all @SampleDocument1.document " + lineSeparator
          + "Feature: Sample Feature" + lineSeparator
          + "    @positive @SampleDocument1.document_positive " + lineSeparator
          + "    Scenario Outline: Sample Test positive test" + lineSeparator
          + "        Given These are our pre-requisites" + lineSeparator
          + "        When Sample action" + lineSeparator
          + "        Then This is what we see on success" + lineSeparator
          + "    Examples:" + lineSeparator
          + "        | Test | ValidInput |" + lineSeparator
          + "        | 0 | true  |" + lineSeparator
          + "        | 50 | true  |" + lineSeparator
          + "        | 51 | true  |" + lineSeparator
          + "" + lineSeparator
          + "    @negative @SampleDocument1.document_negative " + lineSeparator
          + "    Scenario Outline: Sample Test negative test" + lineSeparator
          + "        Given These are our pre-requisites" + lineSeparator
          + "        When Sample action" + lineSeparator
          + "        Then This is what we see on error" + lineSeparator
          + "    Examples:" + lineSeparator
          + "        | Test | ValidInput |" + lineSeparator
          + "        | 100 | false |" + lineSeparator
          + "        | -1 | false |" + lineSeparator
          + "        | 101 | false |" + lineSeparator
          + "" + lineSeparator
          + "" + lineSeparator
          + "    Scenario: Sample Scenario 1";
        Document document = new Document(reader.getTags().next());
        document = (Document) document.parse(reader.readNext());
        writer.open(document);
        Assert.assertTrue(writer.hasNext());
        writer.writeNext();
        File expectedFile = new File("src/test/resources/temp/SampleFeature.feature");
        Assert.assertTrue(expectedFile.exists());
        Assert.assertEquals(expected, FileUtils.readFileToString(expectedFile));
        Assert.assertFalse(writer.hasNext());
    }

    @Test
    public void testFileWriterShouldHandleEmptyDocument() throws Exception {
        Document document = new Document();
        writer.open(document);
        Assert.assertFalse(writer.hasNext());
        Assert.assertNull(writer.writeNext());
    }

    @Test
    public void testFileWriterShouldHandleNullDocument() throws Exception {
        writer.open(null);
        Assert.assertFalse(writer.hasNext());
        Assert.assertNull(writer.writeNext());
    }

    @Test
    public void testClosedFileWriterShouldBehaveAsEmptyWriter() throws Exception {
        writer.close();
        Assert.assertFalse(writer.hasNext());
        Assert.assertNull(writer.writeNext());
    }
}
