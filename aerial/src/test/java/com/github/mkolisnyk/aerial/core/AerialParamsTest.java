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

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
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
    private String expectedConfiguration;
    private AerialOutputFormat format;
    private boolean validationPass;

    private AerialParams params;

    public AerialParamsTest(
            String description,
            String[] commandLineValue,
            AerialSourceType expectedInputTypeValue,
            String expectedSourceValue,
            AerialSourceType expectedOutputTypeValue,
            String expectedDestinationValue,
            String expectedConfigurationValue,
            Map<String, String> expectedExtraParamsValue,
            AerialOutputFormat formatValue,
            boolean validationPassValue) {
        this.commandLine = commandLineValue;
        this.expectedInputType = expectedInputTypeValue;
        this.expectedSource = expectedSourceValue;
        this.expectedOutputType = expectedOutputTypeValue;
        this.expectedDestination = expectedDestinationValue;
        this.expectedConfiguration = expectedConfigurationValue;
        this.expectedExtraParams = expectedExtraParamsValue;
        this.format = formatValue;
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
                    "main/resources/aerial.properties",
                    new HashMap<String, String>(),
                    AerialOutputFormat.CUCUMBER,
                    false
                },
                {
                    "No input defined. Check for Configuration",
                    new String[] {
                            AerialParamKeys.CONFIGURATION.toString(),
                            "main/resources/input/plain.properties"
                    },
                    AerialSourceType.NONE,
                    "",
                    AerialSourceType.NONE,
                    "",
                    "main/resources/input/plain.properties",
                    new HashMap<String, String>(),
                    AerialOutputFormat.CUCUMBER,
                    false
                },
                {
                    "String input defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.FORMAT.toString(),
                            AerialOutputFormat.JBEHAVE.toString(),
                            AerialParamKeys.SOURCE.toString(),
                            "This is the sample String"
                            },
                    AerialSourceType.STRING,
                    "This is the sample String",
                    AerialSourceType.NONE,
                    "",
                    "main/resources/aerial.properties",
                    new HashMap<String, String>(),
                    AerialOutputFormat.JBEHAVE,
                    false
                },
                {
                    "String input/file output defined",
                    new String[] {
                            AerialParamKeys.INPUT_TYPE.toString(),
                            AerialSourceType.STRING.toString(),
                            AerialParamKeys.FORMAT.toString(),
                            AerialOutputFormat.JUNIT.toString(),
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
                    "main/resources/aerial.properties",
                    new HashMap<String, String>(),
                    AerialOutputFormat.JUNIT,
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
                    "main/resources/aerial.properties",
                    new HashMap<String, String>() {
                        {
                            put("Test", "Value");
                            put("TestEmpty", "");
                        }
                    },
                    AerialOutputFormat.CUCUMBER,
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
                    "main/resources/aerial.properties",
                    new HashMap<String, String>() {
                        {
                            //put("TestValue", "");
                        }
                    },
                    AerialOutputFormat.CUCUMBER,
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
    public void testParse() throws Exception {
        params.parse(commandLine);
        params.apply();
        Assert.assertEquals(expectedInputType, params.getInputType());
        Assert.assertEquals(expectedOutputType, params.getOutputType());
        Assert.assertEquals(expectedSource, params.getSource());
        Assert.assertEquals(expectedDestination, params.getDestination());
        Assert.assertEquals(format, params.getFormat());
        Assert.assertEquals(this.expectedExtraParams.size(), params.getNamedParams().size());
        for (Entry<String, String> item : params.getNamedParams().entrySet()) {
            Assert.assertTrue(this.expectedExtraParams.containsKey(item.getKey()));
            Assert.assertEquals(this.expectedExtraParams.get(item.getKey()), item.getValue());
        }
    }

    @Test
    public void testValidate() throws Exception {
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
