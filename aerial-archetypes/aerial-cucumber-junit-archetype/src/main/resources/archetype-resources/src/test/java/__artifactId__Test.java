package ${package};

import org.junit.runner.RunWith;
import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;
import com.github.mkolisnyk.aerial.core.AerialRunner;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;
import cucumber.api.CucumberOptions;

@CucumberOptions(
        format = {"html:target/cucumber-html-report",
                  "json:target/cucumber.json",
                  "pretty:target/cucumber-pretty.txt",
                  "usage:target/cucumber-usage.json"
                 },
        features = {"${features-path}" },
        glue = {"${packageInPathFormat}"},
        tags = { }
)
@Aerial(
    inputType = AerialSourceType.FILE,
    source = "src/test/resources",
    additionalParams = { "" },
    destination = "${features-path}")
@RunWith(AerialRunner.class)
public class ${artifactId}Test {
    @AerialBeforeSuite
    public static void setUp() {
        System.out.println("setUp");
    }
    @AerialAfterSuite
    public static void tearDown() {
        System.out.println("tearDown");
    }
}