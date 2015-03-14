package com.github.mkolisnyk.aerial;

import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AerialGluCode {
    private static List<String> callsList = new ArrayList<String>();
    /**
     * @return the callsList
     */
    public static final List<String> getCallsList() {
        return callsList;
    }

    @Given("^These are our pre-requisites$")
    public void theseAreOurPreRequisites() throws Throwable {
        getCallsList().add((new Exception()).getStackTrace()[0].getMethodName());
    }

    @When("^Sample action$")
    public void sampleAction() throws Throwable {
        getCallsList().add((new Exception()).getStackTrace()[0].getMethodName());
    }

    @Then("^This is what we see on success$")
    public void thisIsWhatWeSeeOnSuccess() throws Throwable {
        getCallsList().add((new Exception()).getStackTrace()[0].getMethodName());
    }

    @Then("^This is what we see on error$")
    public void thisIsWhatWeSeeOnError() throws Throwable {
        getCallsList().add((new Exception()).getStackTrace()[0].getMethodName());
    }
}
