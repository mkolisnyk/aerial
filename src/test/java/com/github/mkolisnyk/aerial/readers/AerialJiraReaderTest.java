package com.github.mkolisnyk.aerial.readers;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.test.http.Method;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;

public class AerialJiraReaderTest {

    private static final int           PORT    = 51234;
    private static final String        baseUrl = "http://localhost:"
                                                       + PORT;
    private MockHttpServer             server;
    private SimpleHttpResponseProvider responseProvider;

    @Before
    public void setUp() throws Exception {
        responseProvider = new SimpleHttpResponseProvider();
        server = new MockHttpServer(PORT, responseProvider);
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testOpenJiraReaderValidQueryShouldFillContentProperly()
            throws Exception {
        String[] expectedContent = new String[] {
                "gg",
                "ddddd",
                "I wondered when the Wallboard Plugin will be compatible in the latest version of Jira 6.3.1."
                        + "For now we upgraded Jira in the latest version,and found this plugin is incompatible." };
        String mockOutput = "{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":8,"
                + "\"issues\":["
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\","
                    + "\"id\":\"384458\","
                    + "\"self\":\"https://jira.atlassian.com/rest/api/2/issue/384458\","
                    + "\"key\":\"WBS-193\","
                    + "\"fields\":{"
                    + "\"description\":\"gg\"}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"382505\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/382505\",\"key\":\"WBS-192\",\"fields\":{\"description\":\"ddddd\"}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350850\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350850\",\"key\":\"WBS-181\"},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350849\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350849\",\"key\":\"WBS-180\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350848\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350848\",\"key\":\"WBS-179\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350847\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350847\",\"key\":\"WBS-178\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350846\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350846\",\"key\":\"WBS-177\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"346867\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/346867\",\"key\":\"WBS-176\",\"fields\":{\"description\":\"I wondered when the Wallboard Plugin will be compatible in the latest version of Jira 6.3.1.For now we upgraded Jira in the latest version,and found this plugin is incompatible.\"}}]}";
        responseProvider
                .expect(Method.GET,
                        "/rest/api/2/search?jql=project%3DWallboards%20AND%20status%3DOpen&fields=key,description")
                .respondWith(200, "application/json", mockOutput);
        AerialJiraReader reader = new AerialJiraReader(baseUrl,
                "someuser", "password", "description");
        reader.open("project=Wallboards AND status=Open");
        Assert.assertTrue(reader.hasNext());
        ArrayList<String> actual = new ArrayList<String>();
        while (reader.hasNext()) {
            actual.add(reader.readNext());
        }
        for (String expected : expectedContent) {
            actual.remove(expected);
        }
        Assert.assertEquals(0, actual.size());
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testOpenJiraReaderForQueryWithoutExpectedField()
            throws Exception {
        String mockOutput = "{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":8,"
                + "\"issues\":["
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\","
                    + "\"id\":\"384458\","
                    + "\"self\":\"https://jira.atlassian.com/rest/api/2/issue/384458\","
                    + "\"key\":\"WBS-193\","
                    + "\"fields\":{"
                    + "\"description\":\"gg\"}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"382505\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/382505\",\"key\":\"WBS-192\",\"fields\":{\"description\":\"ddddd\"}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350850\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350850\",\"key\":\"WBS-181\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350849\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350849\",\"key\":\"WBS-180\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350848\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350848\",\"key\":\"WBS-179\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350847\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350847\",\"key\":\"WBS-178\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"350846\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/350846\",\"key\":\"WBS-177\",\"fields\":{\"description\":null}},"
                + "{\"expand\":\"operations,editmeta,changelog,transitions,renderedFields\",\"id\":\"346867\",\"self\":\"https://jira.atlassian.com/rest/api/2/issue/346867\",\"key\":\"WBS-176\",\"fields\":{\"description\":\"I wondered when the Wallboard Plugin will be compatible in the latest version of Jira 6.3.1.For now we upgraded Jira in the latest version,and found this plugin is incompatible.\"}}]}";
        responseProvider
                .expect(Method.GET,
                        "/rest/api/2/search?jql=project%3DWallboards%20AND%20status%3DOpen&fields=key,some_field")
                .respondWith(200, "application/json", mockOutput);
        AerialJiraReader reader = new AerialJiraReader(baseUrl,
                "someuser", "password", "some_field");
        reader.open("project=Wallboards AND status=Open");
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testOpenJiraReaderForEmptyQueryShouldReturnEmptyData()
            throws Exception {
        String mockOutput = "{\"expand\":\"schema,names\",\"startAt\":0,\"maxResults\":50,\"total\":0,"
                + "\"issues\":[]}";
        responseProvider
                .expect(Method.GET,
                        "/rest/api/2/search?jql=project%3DWallboards%20AND%20status%3DOpen&fields=key,some_field")
                .respondWith(200, "application/json", mockOutput);
        AerialJiraReader reader = new AerialJiraReader(baseUrl,
                "someuser", "password", "some_field");
        reader.open("project=Wallboards AND status=Open");
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testClosedJiraReaderShouldReturnNullValues()
            throws Exception {
        AerialJiraReader reader = new AerialJiraReader(baseUrl,
                "someuser", "password", "description");
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }
}
