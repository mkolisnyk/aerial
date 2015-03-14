package com.github.mkolisnyk.aerial;

import java.io.File;

import org.junit.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;

import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;
import com.github.mkolisnyk.aerial.core.AerialRunner;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

import cucumber.api.CucumberOptions;

public class AerialRunnerTest {

    private String[] outputFiles = {
            "target/cucumber-html-report",
            "target/cucumber.json",
            "target/cucumber-pretty.txt",
            "target/cucumber-usage.json",
            "output/"
    };

    private String[] expectedMethods = {
            "theseAreOurPreRequisites",
            "sampleAction",
            "thisIsWhatWeSeeOnSuccess",
            "thisIsWhatWeSeeOnError",
            "setUp",
            "tearDown"
    };

    @CucumberOptions(
            format = {"html:target/cucumber-html-report",
                      "json:target/cucumber.json",
                      "pretty:target/cucumber-pretty.txt",
                      "usage:target/cucumber-usage.json"
                     },
            features = {"output/" },
            glue = {"com/github/mkolisnyk/aerial" },
            tags = { }
    )
    @Aerial(
            inputType = AerialSourceType.FILE,
            source = "src/test/resources",
            additionalParams = { },
            destination = "output/")
    public static class TestSubClass {
        @AerialBeforeSuite
        public static void setUp() {
            AerialGluCode.getCallsList().add("setUp");
        }
        @AerialAfterSuite
        public static void tearDown() {
            AerialGluCode.getCallsList().add("tearDown");
        }
    }

    @After
    @Before
    public void setUp() throws Exception {
        for (String item : this.outputFiles) {
            File file = new File(item);
            if (!file.exists()) {
                continue;
            }
            FileUtils.deleteQuietly(file);
        }
        AerialGluCode.getCallsList().clear();
    }

    @Test
    public void testRunSampleClass() throws Exception {
        AerialRunner runner = new AerialRunner(TestSubClass.class);
        runner.run(new RunNotifier());
        for (String item : this.outputFiles) {
            File file = new File(item);
            Assert.assertTrue(
                    "The element '" + file.getAbsolutePath() + "' wasn't found",
                    file.exists());
        }
        for (String method : this.expectedMethods) {
            Assert.assertTrue(
                    "Unable to find the call of method: " + method,
                    AerialGluCode.getCallsList().contains(method));
        }
    }
}
