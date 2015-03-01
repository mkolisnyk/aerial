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
        features = {"src/test/resources" },
        glue = {"${packageInPathFormat}"},
        tags = { }
)
@RunWith(Cucumber.class)
public class ${artifactId}Test {
}