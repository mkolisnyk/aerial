package com.github.mkolisnyk.aerial;

import java.io.File;

import org.junit.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.AerialMain;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

public class AerialMainTest {

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/resources/output/");
        FileUtils.deleteDirectory(file);
    }

    @After
    public void tearDown() throws Exception {
        File file = new File("src/test/resources/output/");
        FileUtils.deleteDirectory(file);
    }

    @Test
    public void testMainForNullArgumentListShouldReturnNoErrors() throws Exception {
        AerialMain.main(null);
    }

    @Test
    public void testMainForStringInput() throws Exception {
        AerialMain.main(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.STRING.toString(),
                AerialParamKeys.SOURCE.toString(), "This is the test document",
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.DESTINATION.toString(), "src/test/resources/output/"
        });
    }

    @Test
    public void testMainForJiraInput() throws Exception {
        AerialMain.main(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.JIRA.toString(),
                AerialParamKeys.SOURCE.toString(), "This is the test document",
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.DESTINATION.toString(), "src/test/resources/output/"
        });
    }

    @Test
    public void testMainForFileInput() throws Exception {
        AerialMain.main(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.SOURCE.toString(), "src/test/resources/",
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.DESTINATION.toString(), "src/test/resources/output/"
        });
        File file = new File("src/test/resources/output/");
        Assert.assertTrue(file.exists());
    }
}

