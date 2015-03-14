package com.github.mkolisnyk.aerial.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.test.http.Method;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;
import com.github.mkolisnyk.aerial.readers.AerialJiraReader;

public class AerialMavenGeneratorPluginTest {
    private AerialMavenGeneratorPlugin plugin;
    
    private String destinationPath = "output";
    
    private String lineSeparator = System.lineSeparator();

    private String sampleDocumentDescription = "This is a sample document" + lineSeparator
            + "With multiline description";
    private String sampleFeatureDescription = "This is a sample feature" + lineSeparator
            + "With multiline description";
    private String sampleCaseDescription = "This is a sample test case" + lineSeparator
            + "With multiline description";
    private String sampleCaseAction = "Sample action";
    private String sampleCaseInput = "| Name | Type | Value   |" + lineSeparator
            + "| Test | Int  | [0;100) |";
    private String sampleCaseValidOutput = "This is what we see on success";
    private String sampleCaseErrorOutput = "This is what we see on error";
    private String samplePrerequisites = "These are our pre-requisites";

    private String sampleFeatureText =
            sampleDocumentDescription + lineSeparator
            + "Feature: Sample Feature" + lineSeparator
            + sampleFeatureDescription + lineSeparator
            + "Case: Sample Case 001" + lineSeparator
            + sampleCaseDescription + lineSeparator
            + "Action:" + lineSeparator
            + sampleCaseAction + lineSeparator
            + "Input:" + lineSeparator
            + sampleCaseInput + lineSeparator
            + "On Success:" + lineSeparator
            + sampleCaseValidOutput + lineSeparator
            + "On Failure:" + lineSeparator
            + sampleCaseErrorOutput + lineSeparator
            + "Pre-requisites:" + lineSeparator
            + samplePrerequisites + lineSeparator
            + "Additional Scenarios:" + lineSeparator
            + "Scenario: Sample Scenario 1";
    private MockHttpServer             server;
    private SimpleHttpResponseProvider responseProvider;

    private int port = 51234;

    @Before
    public void setUp() throws Exception {
        plugin = new AerialMavenGeneratorPlugin();
        FileUtils.deleteDirectory(destinationPath);

        responseProvider = new SimpleHttpResponseProvider();
        server = new MockHttpServer(port, responseProvider);
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(destinationPath);
        server.stop();
    }

    @Test
    public void testExecuteStringReader() throws MojoExecutionException, MojoFailureException {
        plugin.setInputType(AerialSourceType.STRING);
        plugin.setSource("");
        plugin.setOutputType(AerialSourceType.FILE);
        plugin.setDestination(destinationPath);
        plugin.setValueParams(new ArrayList<String>() {
            {
                add(sampleFeatureText);
            }
        });
        plugin.execute();
        File resultFile = new File(destinationPath + File.separator + "SampleFeature.feature");
        Assert.assertTrue(resultFile.exists());
    }

    @Test
    public void testExecuteJiraReader()
            throws Exception {
        String mockOutput = "{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":8,"
                + "\"issues\":["
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\","
                    + "\"id\":\"384458\","
                    + "\"self\":\"https://jira.atlassian.com/rest/api/2/issue/384458\","
                    + "\"key\":\"WBS-193\","
                    + "\"fields\":{"
                    + "\"description\":" + JSONObject.quote(this.sampleFeatureText) + "}}"
                    + "]}";
        responseProvider
                .expect(Method.GET,
                        "/rest/api/2/search?jql=project%3DWallboards+AND+status%3DOpen&fields=key,description")
                .respondWith(HttpStatus.SC_OK, "application/json", mockOutput);
        plugin.setInputType(AerialSourceType.JIRA);
        plugin.setSource("http://localhost:" + port);
        plugin.setOutputType(AerialSourceType.FILE);
        plugin.setDestination(destinationPath);
        plugin.setNamedParams(new HashMap<String, String>() {
            {
                {
                    put("user", "username");
                    put("password", "password");
                    put("field", "description");
                }
            }
        });
        plugin.setValueParams(new ArrayList<String>() {
            {
                add("project\\=Wallboards AND status\\=Open");
            }
        });
        plugin.execute();
        File resultFile = new File(destinationPath + File.separator + "SampleFeature.feature");
        Assert.assertTrue(resultFile.exists());
    }

    @Test(expected=NullPointerException.class)
    public void testExecuteMissingParametersShouldCauseError() throws Exception {
        plugin.execute();
    }

    @Test(expected=MojoFailureException.class)
    public void testExecuteWrongParametersShouldCauseError() throws Exception {
        plugin.setInputType(AerialSourceType.JIRA);
        plugin.setSource("https://localhost:" + port);
        plugin.setOutputType(AerialSourceType.FILE);
        plugin.setDestination(destinationPath);
        plugin.execute();
    }
}
