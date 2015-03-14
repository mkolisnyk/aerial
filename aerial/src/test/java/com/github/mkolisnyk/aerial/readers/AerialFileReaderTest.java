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

import com.github.mkolisnyk.aerial.core.AerialTagList;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialFileReaderTest {

    private AerialFileReader reader;
    private AerialParams params;

    @Before
    public void setUp() throws Exception {
        params = new AerialParams();
        params.parse(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.SOURCE.toString(), "src/test/resources/",
                AerialParamKeys.DESTINATION.toString(), "output"
        });
        reader = new AerialFileReader(params, new AerialTagList());
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }

    @Test
    public void testFullFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        params.getValueParams().clear();
        params.getValueParams().add(fileName);
        reader.open(params);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testSimpleFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        String regExp = "resources" + File.separator + "SampleDocument1.document";
        params.getValueParams().clear();
        params.getValueParams().add(regExp);
        reader.open(params);
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
        params.getValueParams().clear();
        params.getValueParams().add(regExp);
        reader.open(params);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testRegExpPathFileNameShouldReturnSingleFileContent() throws Exception {
        String fileName = "src/test/resources/SampleDocument1.document";
        String regExp = "(.*)src(.*)Document1\\.document";
        params.getValueParams().clear();
        params.getValueParams().add(regExp);
        reader.open(params);
        Assert.assertTrue(reader.getFiles().size() > 0);
        Assert.assertTrue(reader.hasNext());
        Assert.assertEquals(FileUtils.readFileToString(new File(fileName)), reader.readNext());
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testEmptyFileListShouldReturnNoContent() throws Exception {
        params.getValueParams().clear();
        params.getValueParams().add("Non-existing");
        reader.open(params);
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
