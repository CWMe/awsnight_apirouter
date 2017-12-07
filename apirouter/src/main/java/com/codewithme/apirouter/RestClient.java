package com.codewithme.apirouter;

import java.io.IOException;
import javax.ws.rs.client.Entity;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class RestClient {

    private static final ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
    private static final ResteasyClient RESTEASY_CLIENT;

    static {
        RegisterBuiltin.register(instance);
        instance.registerProvider(ResteasyJackson2Provider.class);
        RESTEASY_CLIENT = new ResteasyClientBuilder().build();
    }

    public Article resteasy(URLRequest urlRequest) throws IOException {
        ResteasyWebTarget target = RESTEASY_CLIENT.target("https://urjtpjq5t8.execute-api.us-east-1.amazonaws.com").path("Prod/cleanarticle");
        return target.request().put(Entity.entity(urlRequest, "application/json"), Article.class);
    }

}
