package com.codewithme.apirouter;

import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestClient {

    private static final ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
//    private static final Client REST_CLIENT;
    private static final ResteasyClient RESTEASY_CLIENT;

    static {
        RegisterBuiltin.register(instance);
        instance.registerProvider(ResteasyJackson2Provider.class);
        RESTEASY_CLIENT = new ResteasyClientBuilder().build();

//        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
//        REST_CLIENT = new ResteasyClientBuilder().httpEngine(engine).connectionPoolSize(125).build();
    }

    public Article resteasy(URLRequest urlRequest) throws IOException {
        ResteasyWebTarget target = RESTEASY_CLIENT.target("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com").path("Prod/cleanarticle");
        return target.request().put(Entity.entity(urlRequest, "application/json"), Article.class);

//        WebTarget target = REST_CLIENT.target("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com").path("Prod/cleanarticle");
//        Invocation.Builder invoke = target.request();
//        return invoke.put(Entity.json(JsonUtil.OBJECT_MAPPER.writeValueAsString(urlRequest)), Article.class);
    }

}
