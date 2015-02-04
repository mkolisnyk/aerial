package com.github.mkolisnyk.aerial.readers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpResponse;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.core.params.AerialParams;

public class AerialGitHubReader extends AerialReader {

    /**
     * .
     */
    private List<String> content;
    /**
     * .
     */
    private Iterator<String> iterator;

    public AerialGitHubReader(AerialParams params) throws Exception {
        super(params);
    }

    @Override
    public void open(AerialParams params) throws Exception {
        String apiUrl = params.getSource();

        //https://api.github.com/search/issues?q=repo%3Amkolisnyk%2Faerial+state%3Aopen&sort=created&order=asc
        DefaultHttpClient client = new DefaultHttpClient();
        /*client.getCredentialsProvider().setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));*/
        content = new ArrayList<String>();
        for (String query : params.getValueParams()) {
            URI uri = UriBuilder.fromUri(apiUrl)
                    .path("/search/issues")
                    .queryParam("q", query)
                    .queryParam("sort", "created")
                    .queryParam("order", "asc").build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);
            String responseText = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(responseText);
            if (json.has("items")) {
                JSONArray array = json.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    String value = null;
                    value = array.getJSONObject(i).getString("body");
                    if (value != null && !value.equals("null")) {
                        content.add(value);
                    }
                }
            }
        }
        client.getConnectionManager().shutdown();
        iterator = content.iterator();
    }

    @Override
    public void close() throws Exception {
        iterator = null;
        if (content != null) {
            content.clear();
        }
        content = null;
    }

    @Override
    public String readNext() throws Exception {
        if (iterator == null || !iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

}
