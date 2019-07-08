
package model;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class HttpHandler {

    private HttpClient client;

    public HttpHandler() {
        client = HttpClientBuilder.create().build();
    }

    public String executeGetRequest(String url) {

        try {
            HttpGet get = new HttpGet(url);
            HttpResponse resp = client.execute(get);
            String respString = EntityUtils.toString(resp.getEntity());
            return respString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
































