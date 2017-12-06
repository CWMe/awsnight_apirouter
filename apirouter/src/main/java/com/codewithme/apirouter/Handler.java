package com.codewithme.apirouter;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Map<String, String> HEADERS = new HashMap<>();
    private static final Lock LOCK = new ReentrantLock(true);
    private static final RestClient REST_CLIENT = new RestClient();
    private static final Logger LOG = Logger.getLogger(Handler.class);
    private static final AWSS3Util s3 = new AWSS3Util(new DefaultAWSCredentialsProviderChain());

    static {
        LOCK.lock();
        if (HEADERS.isEmpty()) {
            try {
                HEADERS.put("X-Powered-By", "AWS Lambda");
                HEADERS.put("Content-Type", "application/json");
            } finally {
                LOCK.unlock();
            }
        }
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            if (input != null && input.isEmpty() == false) {
                String url = (String) input.get("body");
                URLRequest urlRequest = JsonUtil.OBJECT_MAPPER.readValue(url, URLRequest.class);
                LOG.debug("Received URL: " + urlRequest.getUrl());

                JsonNode response = REST_CLIENT.resteasy(urlRequest.getUrl());

                LOG.info(JsonUtil.OBJECT_MAPPER.writeValueAsString(response));
                
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(new Response(JsonUtil.OBJECT_MAPPER.writeValueAsString(response)))
                        .setHeaders(HEADERS)
                        .build();

            }
        } catch (IOException ex) {
            LOG.error(ex);
            return ApiGatewayResponse.builder()
                    .setStatusCode(502)
                    .setObjectBody(new Response(ex.getMessage()))
                    .setHeaders(HEADERS)
                    .build();
        }

        return ApiGatewayResponse.builder()
                        .setStatusCode(502)
                        .setObjectBody(new Response("Well, this is odd..."))
                        .setHeaders(HEADERS)
                        .build();
    }
}