/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.github.mkolisnyk.aerial.AerialReader;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialJiraReader implements AerialReader {

    private String userName;
    private String password;
    private String url;
    private String fieldName;

    /**
     * .
     */
    private List<String> content;
    /**
     * .
     */
    private Iterator<String> iterator;

    /**
     * .
     */
    public AerialJiraReader(String urlValue, String userNameValue, String passwordValue, String fieldNameValue) {
        this.userName = userNameValue;
        this.password = passwordValue;
        this.url = urlValue;
        this.fieldName = fieldNameValue;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#open(java.lang.Object[])
     */
    public void open(Object... params) throws Exception {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI uri = new URI(url);
        JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, userName, password);
        SearchRestClient searchClient = client.getSearchClient();
        content = new ArrayList<String>();
        for (Object query : params) {
            Promise<SearchResult> result = searchClient.searchJql((String) query);
            Iterable<Issue> issues = result.get().getIssues();
            for (Issue issue : issues) {
                IssueField field = issue.getField(fieldName);
                content.add((String) field.getValue());
            }
        }
        this.iterator = content.iterator();
        client.close();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#close()
     */
    public final void close() throws Exception {
        if (content != null) {
            content.clear();
            content = null;
        }
        iterator = null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#readNext()
     */
    public final String readNext() {
        if (hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#hasNext()
     */
    public final boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }
}
