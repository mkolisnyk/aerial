/**
 * 
 */
package com.github.mkolisnyk.aerial.readers;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialFileReaderTest {

    private AerialFileReader reader;

    @Before
    public void setUp() {
        reader = new AerialFileReader();
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }

    @Test
    public void testFullFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        reader.open(fileName);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testSimpleFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        String regExp = "resources" + File.separator + "SampleDocument1.document";
        reader.open(regExp);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertEquals(1, reader.getFiles().size());
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testRegExpUnixPathFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        String regExp = "(.*)src/(.*)/SampleDocument1.document";
        reader.open(regExp);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testRegExpPathFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        String regExp = "(.*)src(.*)Document1\\.document";
        reader.open(regExp);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testEmptyFileListShouldReturnNoContent() throws Exception {
        Assert.assertEquals(0, reader.getFiles().size());
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testClosedFileListShouldReturnNoContent() throws Exception {
        reader.close();
        Assert.assertEquals(0, reader.getFiles().size());
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }
}
