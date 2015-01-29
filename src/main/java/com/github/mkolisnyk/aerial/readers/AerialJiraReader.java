/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.core.params.AerialParams;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialJiraReader extends AerialReader {

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

    public AerialJiraReader(AerialParams params) throws Exception {
        super(params);
        this.url = params.getSource();
        this.userName = params.getNamedParams().get("user");
        this.password = params.getNamedParams().get("password");
        this.fieldName = params.getNamedParams().get("field");
        this.open(params);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#open(java.lang.Object[])
     */
    public void open(AerialParams params) throws Exception {
        content = new ArrayList<String>();

        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));

        for (String query : params.getValueParams()) {
            URI uri = UriBuilder.fromUri(url)
                    .path("/rest/api/2/search")
                    .queryParam("jql", query)
                    .queryParam("fields", "key," + fieldName).build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            String responseText = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(responseText);
            JSONArray array = json.getJSONArray("issues");
            for (int i = 0; i < array.length(); i++) {
                String value = null;
                if (!array.getJSONObject(i).has("fields")) {
                    continue;
                }
                if (!array.getJSONObject(i).getJSONObject("fields").has(fieldName)) {
                    continue;
                }
                value = array.getJSONObject(i).getJSONObject("fields").getString(fieldName);
                if (value != null && !value.equals("null")) {
                    content.add(value);
                }
            }
        }
        client.getConnectionManager().shutdown();
        this.iterator = content.iterator();
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
