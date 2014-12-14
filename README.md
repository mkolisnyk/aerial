Aerial
======

**Aerial** is an engine implementing the approach of **Executable Requirements**. Mainly it is designed as an extension of **Cucumber** to provide the following possibilities:
* **More compact and structured representation of requirements and scenarios** - actually text instructions are targeted to look more like requirements or technical specifications rather than scenarios
* **Built-in mechanism for generating test scenarios** - main idea is that test scenarios are generated based on formal description of some feature and it's attributes. This minimises efforts on test data definition and data preparation. You just have to specify what is the data and how each data item is relevant to each other
* **Generalised approach for getting data from external resources** - requirements can be stored in any form and in any place (or system). The **Aerial** should provide extensible mechanism to retrieve requirements from various different data sources 
* **Ability to perform static checks on requirements** - since we expand requirements into tests using some rules there's an ability to find out requirement inconsistencies during initial processing stage
* **Tests and their automated implementation reacts on any requirement change** - most of test management systems simply store dedicated records for requirements, tests and automated tests. But they are just linked by abstract DB structure. Thus, if we do any modification into one of those items others wouldn't be reflected. Since **Aerial** generates tests and automated tests based on requirements as an input, any change to requirements will be immediately reflected. So, tests always correspond to requirements.
* **Simplify requirements and test coverage calculation** - mainly such coverage is 100% by design

Why using Aerial?
======

All the time when we design test scenarios we have a lot of typical and "routine" checks actions to perform and checkpoints to make. E.g. any time we encounter field containing an e-mail we should check:
* Value matching e-mail format ( <addredd name>@<company/organization>.<domain> )
* Value containing multiple @ characters
* Empty value
* Simple text value
* Spaces in the address
* Special characters in the address (E.g. multiple dots are acceptable in e-mail address)
For other data types there would be different checks depending on the data structure. 

Main thing is that in most cases we have to do typical cases for all those data. So, in such cases it's enough just to define the data structure and the flow to use in each specific case. Thus we'll get declarative representation of the business function which (representation) can be used for tests generation.
Thus, we'll have to use declarative representation of the functionality to test which looks pretty similar to requirements and technical specification. As the result, we have an ability to combine requirements, tests and their automated implementation within common resource. So, in this structure our tests are generated based on requirements and auto-tests are generated based on tests.

What does it give to us? Main benefits:
* We always sure requirements are linked to tests. Just by design.
* If we want to change requirements our tests will be changed as well, so we never care if our tests are in sync with requirements
* Since the system is targeted to generate test scenarios based on formal description we get an advantage it writing efforts as a lot of routine actions are simply generated. Thus, we minimise human factor on such tasks. 

How does it work? 
======

The engine is designed to generate [Cucumber](http://cukes.info) features based on specific document description. After generation is done it triggers Cucumber run the same way as we do for any other tests of that kind.

Example
-------

Main source for generation is the document. It defines rules to generate feature file. Here is an example of it:

**src/test/resources/SampleDocument.document**:

```
This is a sample document
With multiline description

Feature: Sample Feature
    This is a sample feature
    With multiline description

    Case: Sample Test
        This is a sample test case
        With multiline description

        Action:
            Sample action
        Input:
            | Name | Type | Value   |
            | Test | Int  | [0;100) |
        On Success:
            This is what we see on success
        On Failure:
            This is what we see on error
        Pre-requisites:
            These are our pre-requisites
    Additional Scenarios:
    Scenario: Sample Scenario 1

```

Then we define the test class which should be the runner for the above document description:

**src/test/java/com/sample/aerial/AerialSampleClass.java**:

``` java
package com.sample.aerial;

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
        features = {"output/" },
        glue = {"com/github/mkolisnyk/aerial" },
        tags = { }
)
@Aerial(
        inputType = AerialSourceType.FILE,
        source = "src/test/resources",
        additionalParams = { "" },
        destination = "output/")
@RunWith(AerialRunner.class)
public class AerialSampleClass {
}
```

It's empty class but most of the parameters are defined in annotations:
* **@CucumberOptions** - on the back it's Cucumber tests so the configuration parameters. This is Cucumber annotation and the parameters can be see on the [GitHub Source](https://github.com/cucumber/cucumber-jvm/blob/master/core/src/main/java/cucumber/api/CucumberOptions.java).
  Main items we are interested in are:
  * **features** - we should make sure that Aerial outputs generated features to the same folder so that Cucumber will pick them up and execute
  * **glue** - we should have reserved location where our glue code is stored
* **@Aerial** - main annotation for Aerial engine generation. Mainly it defines the source and the destination of the generated content. We should make sure that **destination** parameter of this annotation points to the same location as **features** option in the **@CucumberOptions** definition.
* **@RunWith(AerialRunner.class)** - this will drive all the magic. Initially it runs features generation and then it runs Cucumber.

Then we prepare Java glue code for text instructions binding:

**src/test/java/com/sample/aerial/AerialGluCode.java**:

``` java
package com.sample.aerial;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AerialGluCode {

    @Given("^These are our pre-requisites$")
    public void theseAreOurPreRequisites() throws Throwable {
    }

    @When("^Sample action$")
    public void sampleAction() throws Throwable {
    }

    @Then("^This is what we see on success$")
    public void thisIsWhatWeSeeOnSuccess() throws Throwable {
    }

    @Then("^This is what we see on error$")
    public void thisIsWhatWeSeeOnError() throws Throwable {
    }
}
```

After that we can run our test as ordinary JUnit test. On the background it will be executed via **Cucumber** runner.

Features
======

User Reference Guide
======

Releases
======

Documentation
======

Blog Posts & Live Demos
======

[Blog: Test Automation From Inside](http://mkolisnyk.blogspot.com)

Authors
======
Myk Kolisnyk (kolesnik.nickolay@gmail.com) 

<a href="http://ua.linkedin.com/pub/mykola-kolisnyk/14/533/903"><img src="http://www.linkedin.com/img/webpromo/btn_profile_bluetxt_80x15.png" width="80" height="15" border="0" alt="View Mykola Kolisnyk's profile on LinkedIn"></a>
<a href="http://plus.google.com/108480514086204589709?prsrc=3" rel="publisher" style="text-decoration:none;">
<img src="http://ssl.gstatic.com/images/icons/gplus-16.png" alt="Google+" style="border:0;width:16px;height:16px;"/></a>
