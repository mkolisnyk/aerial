package ${package};

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ${artifactId}Steps {

    @Given("^These are our pre-requisites$")
    public void theseAreOurPreRequisites() throws Throwable {
        System.out.println("Sample pre-requisites");
    }

    @When("^Sample action$")
    public void sampleAction() throws Throwable {
        System.out.println("Sample action");
    }

    @Then("^This is what we see on success$")
    public void thisIsWhatWeSeeOnSuccess() throws Throwable {
        System.out.println("Sample on success event");
    }

    @Then("^This is what we see on error$")
    public void thisIsWhatWeSeeOnError() throws Throwable {
        System.out.println("Sample on failure event");
    }
}
