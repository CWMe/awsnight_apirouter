package com.codewithme.apirouter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.sun.xml.internal.stream.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Map<String, String> HEADERS = new HashMap<>();
    private static final Client REST_CLIENT;

    static {
        HEADERS.put("X-Powered-By", "AWS Lambda & Serverless");
        HEADERS.put("Content-Type", "application/json");

        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
        REST_CLIENT = new ResteasyClientBuilder().httpEngine(engine).connectionPoolSize(125).build();
    }

    private static final Logger LOG = Logger.getLogger(Handler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return null;
    }

    private ApiGatewayResponse javaRest(String inputUrl) {
        try {
            URL url = new URL("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com/Prod/cleanarticle");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(inputUrl.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(conn.getResponseCode())
                        .setObjectBody(new Response("Connection failed"))
                        .setHeaders(HEADERS)
                        .build();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
                String output;
                StringBuilder outputBuilder = new StringBuilder();
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    outputBuilder.append(output);
                }

                conn.disconnect();

                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(outputBuilder.toString())
                        .setHeaders(HEADERS)
                        .build();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setObjectBody("Could not download article")
                .setHeaders(HEADERS)
                .build();

    }

    private ApiGatewayResponse rest(String url) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException {
        try {

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

            // create HTTP Client
            HttpClient httpClient = HttpClientBuilder.create().setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build()).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            // Create new getRequest with below mentioned URL
            HttpPut putRequest = new HttpPut("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com/Prod/cleanarticle");

            // Add additional header to getRequest which accepts application/xml data
            putRequest.addHeader("accept", "application/json");
            List<NameValuePair> urlPair = new ArrayList<>();
            urlPair.add(new BasicNameValuePair("url", url));
            putRequest.setEntity(new UrlEncodedFormEntity(urlPair));

            // Execute your request and catch response
            HttpResponse response = httpClient.execute(putRequest);

            // Check for HTTP response code: 200 = success
            if (response.getStatusLine().getStatusCode() != 200) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(response.getStatusLine().getStatusCode())
                        .setObjectBody(new Response(response.getStatusLine().getReasonPhrase()))
                        .setHeaders(HEADERS)
                        .build();
            }

            // Get-Capture Complete application/json body response
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            StringBuilder outputBuilder = new StringBuilder();

            // Simply iterate through JSON response and show on console.
            String output;
            while ((output = br.readLine()) != null) {
                outputBuilder.append(output);
            }

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(new Response(outputBuilder.toString()))
                    .setHeaders(HEADERS)
                    .build();

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setObjectBody("Could not download article")
                .setHeaders(HEADERS)
                .build();
    }

    private ApiGatewayResponse resteasy(String url) throws IOException {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", url);
        WebTarget target = null;
        REST_CLIENT.target("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com").path("Prod/cleanarticle");
        Invocation.Builder invoke = target.request();
        StringEntity put = invoke.put(Entity.json(urlMap), StringEntity.class);
        try (BufferedReader br = new BufferedReader(new InputStreamReader((put.getContent())))) {
            StringBuilder outputBuilder = new StringBuilder();

            // Simply iterate through JSON response and show on console.
            String output;
            while ((output = br.readLine()) != null) {
                outputBuilder.append(output);
            }
            
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(new Response(outputBuilder.toString()))
                    .setHeaders(HEADERS)
                    .build();
        }
    }

    public static void main(String[] args) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
        Handler handler = new Handler();
        ApiGatewayResponse response = handler.resteasy("http://www.cnn.com/2017/12/02/politics/trump-tweet-flynn-firing-fbi-reaction/index.html");
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
    }
}
