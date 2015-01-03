package com.github.mkolisnyk.aerial.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

@RunWith(Parameterized.class)
public class AerialParamsTest {

    private String[] commandLine;
    private AerialSourceType expectedInputType;
    private String expectedSource;
    private AerialSourceType expectedOutputType;
    private String expectedDestination;
    private Map<String, String> expectedExtraParams;
    private boolean validationPass;

    private AerialParams params;

    public AerialParamsTest(
            String description,
            String[] commandLineValue,
            AerialSourceType expectedInputTypeValue,
            String expectedSourceValue,
            AerialSourceType expectedOutputTypeValue,
            String expectedDestinationValue,
            Map<String, String> expectedExtraParamsValue,
            boolean validationPassValue) {
        this.commandLine = commandLineValue;
        this.expectedInputType = expectedInputTypeValue;
        this.expectedSource = expectedSourceValue;
        this.expectedOutputType = expectedOutputTypeValue;
        this.expectedDestination = expectedDestinationValue;
        this.expectedExtraParams = expectedExtraParamsValue;
        this.validationPass = validationPassValue;
    }

    @Parameters(name = "Test parse params: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                    "No input defined",
                    new String[] {},
                    AerialSourceType.NONE,
                    "",
                    AerialSourceType.NONE,
                    "",
                    new HashMap<String, String>(),
                    false
                },
                {
                    "String input defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.SOURCE.toString(),
                            "This is the sample String"
                            },
                    AerialSourceType.STRING,
                    "This is the sample String",
                    AerialSourceType.NONE,
                    "",
                    new HashMap<String, String>(),
                    false
                },
                {
                    "String input/file output defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.SOURCE.toString(),
                            "This is the sample String",
                            AerialParamKeys.OUTPUT_TYPE.toString(),
                            AerialSourceType.FILE.toString(),
                            AerialParamKeys.DESTINATION.toString(),
                            "src/test/resources/sample"
                            },
                    AerialSourceType.STRING,
                    "This is the sample String",
                    AerialSourceType.FILE,
                    "src/test/resources/sample",
                    new HashMap<String, String>(),
                    true
                },
                {
                    "Valid extra param defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.SOURCE.toString(),
                            "This is the sample String",
                            AerialParamKeys.OUTPUT_TYPE.toString(),
                            AerialSourceType.FILE.toString(),
                            AerialParamKeys.DESTINATION.toString(),
                            "src/test/resources/sample",
                            "Test=Value",
                            "TestEmpty="
                            },
                    AerialSourceType.STRING,
                    "This is the sample String",
                    AerialSourceType.FILE,
                    "src/test/resources/sample",
                    new HashMap<String, String>() {
                        {
                            put("Test", "Value");
                            put("TestEmpty", "");
                        }
                    },
                    true
                },
                {
                    "Inalid extra param defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.SOURCE.toString(),
                            "This is the sample String",
                            AerialParamKeys.OUTPUT_TYPE.toString(),
                            AerialSourceType.FILE.toString(),
                            AerialParamKeys.DESTINATION.toString(),
                            "src/test/resources/sample",
                            "TestValue"
                            },
                    AerialSourceType.STRING,
                    "This is the sample String",
                    AerialSourceType.FILE,
                    "src/test/resources/sample",
                    new HashMap<String, String>() {
                        {
                            //put("TestValue", "");
                        }
                    },
                    true
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        params = new AerialParams();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() {
        params.parse(commandLine);
        Assert.assertEquals(expectedInputType, params.getInputType());
        Assert.assertEquals(expectedOutputType, params.getOutputType());
        Assert.assertEquals(expectedSource, params.getSource());
        Assert.assertEquals(expectedDestination, params.getDestination());
        Assert.assertEquals(this.expectedExtraParams.size(), params.getNamedParams().size());
        for (Entry<String, String> item : params.getNamedParams().entrySet()) {
            Assert.assertTrue(this.expectedExtraParams.containsKey(item.getKey()));
            Assert.assertEquals(this.expectedExtraParams.get(item.getKey()), item.getValue());
        }
    }

    @Test
    public void testValidate() {
        params.parse(commandLine);
        try {
            params.validate();
        } catch (Throwable e) {
            Assert.assertFalse(validationPass);
            return;
        }
        Assert.assertTrue(validationPass);
    }

}
