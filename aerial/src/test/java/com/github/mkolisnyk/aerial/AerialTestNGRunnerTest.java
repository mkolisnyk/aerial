package com.github.mkolisnyk.aerial;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;
import com.github.mkolisnyk.aerial.core.AerialTestNGRunner;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

import cucumber.api.CucumberOptions;

public class AerialTestNGRunnerTest {

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
    public static class TestSubClass extends AerialTestNGRunner {
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
    public void testRunSampleTestNGClass() throws Exception {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] {TestSubClass.class});
        testng.addListener(tla);
        testng.run();

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
