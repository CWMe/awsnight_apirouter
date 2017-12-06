package com.codewithme.apirouter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestClient {

    private static final ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
    private static final Client REST_CLIENT;

    static {
        RegisterBuiltin.register(instance);
        instance.registerProvider(ResteasyJackson2Provider.class);
        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
        REST_CLIENT = new ResteasyClientBuilder().httpEngine(engine).connectionPoolSize(125).build();
    }

    public JsonNode resteasy(String url) throws IOException {
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", url);
        WebTarget target = REST_CLIENT.target("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com").path("Prod/cleanarticle");
        Invocation.Builder invoke = target.request();
        ObjectNode node = JsonUtil.OBJECT_MAPPER.createObjectNode();
        node = node.put("url", url);
        return invoke.put(Entity.json(node), JsonNode.class);
    }

}
