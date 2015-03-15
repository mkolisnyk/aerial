package com.github.mkolisnyk.aerial.readers;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;

import com.github.kristofa.test.http.Method;
import com.github.kristofa.test.http.MockHttpServer;
import com.github.kristofa.test.http.SimpleHttpResponseProvider;
import com.github.mkolisnyk.aerial.core.AerialTagList;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

public class AerialGitHubReaderTest {

    private static final int PORT = 51234;
    private static final String BASE_URL = "http://localhost:" + PORT;
    private MockHttpServer             server;
    private SimpleHttpResponseProvider responseProvider;
    private AerialGitHubReader reader = null;
    private AerialParams params;

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

    private void initReader() throws Exception {
        params = new AerialParams();
        params.parse(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.JIRA.toString(),
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.FILE.toString(),
                AerialParamKeys.SOURCE.toString(), BASE_URL,
                AerialParamKeys.DESTINATION.toString(), "output",
                "repo:mkolisnyk/aerial state:open"
        });
        reader = new AerialGitHubReader(params, new AerialTagList());
        reader.open(params);
    }

    @Test
    public void testOpenGitHubReaderValidQueryShouldFillContentProperly()
            throws Exception {
        String[] expectedContent = new String[] {
                "Value 1",
                "Value 1-1",
                "Value 2",
                "Value 3"
        };
        String mockOutput = FileUtils.readFileToString(new File("src/test/resources/json/github_valid_output.json"));
        responseProvider
                .expect(Method.GET,
                        "/search/issues?q=repo:mkolisnyk/aerial+state:open&sort=created&order=asc")
                .respondWith(HttpStatus.SC_OK, "application/json", mockOutput);
        initReader();
        Assert.assertTrue(reader.hasNext());
        ArrayList<String> actual = new ArrayList<String>();
        while (reader.hasNext()) {
            actual.add(reader.readNext());
        }
        Assert.assertEquals(expectedContent.length, actual.size());
        for (String expected : expectedContent) {
            actual.remove(expected);
        }
        Assert.assertEquals(0, actual.size());
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testOpenGitHubReaderForWrongQuery()
            throws Exception {
        String mockOutput = FileUtils.readFileToString(new File("src/test/resources/json/github_error_output.json"));
        responseProvider
                .expect(Method.GET,
                        "/search/issues?q=repo:mkolisnyk/aerial+state:open&sort=created&order=asc")
                .respondWith(HttpStatus.SC_OK, "application/json", mockOutput);
        initReader();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testOpenGitHubReaderForEmptyQueryShouldReturnEmptyData()
            throws Exception {
        String mockOutput = FileUtils.readFileToString(new File("src/test/resources/json/github_empty_output.json"));
        responseProvider
                .expect(Method.GET,
                        "/search/issues?q=repo:mkolisnyk/aerial+state:open&sort=created&order=asc")
                .respondWith(HttpStatus.SC_OK, "application/json", mockOutput);
        initReader();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testClosedGitHubReaderShouldReturnNullValues()
            throws Exception {
        String mockOutput = FileUtils.readFileToString(new File("src/test/resources/json/github_valid_output.json"));
        responseProvider
                .expect(Method.GET,
                        "/search/issues?q=repo:mkolisnyk/aerial+state:open&sort=created&order=asc")
                .respondWith(HttpStatus.SC_OK, "application/json", mockOutput);
        initReader();
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }
}
