package com.codewithme.apirouter;

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
        if (input != null && input.isEmpty() == false) {
            LOG.debug("Input is good");
            Object body = input.get("body");
            LOG.debug(body.toString());
            LOG.debug(body != null);
            LOG.debug(body instanceof Map);
            LOG.debug(body.getClass().getCanonicalName());
            LOG.debug(((Map) body).get("url") != null);
            if (body != null && body instanceof Map && ((Map) body).get("url") != null) {
                LOG.debug("Body is good");
                String url = (String) ((Map) body).get("url");
                LOG.debug("Received URL: " + url);
                try {
                    JsonNode response = REST_CLIENT.resteasy(url);

                    LOG.info(JsonUtil.OBJECT_MAPPER.writeValueAsString(response));

                    return ApiGatewayResponse.builder()
                            .setStatusCode(200)
                            .setObjectBody(new Response(JsonUtil.OBJECT_MAPPER.writeValueAsString(response)))
                            .setHeaders(HEADERS)
                            .build();
                } catch (IOException ex) {
                    LOG.error(ex);
                    return ApiGatewayResponse.builder()
                            .setStatusCode(502)
                            .setObjectBody(new Response(ex.getMessage()))
                            .setHeaders(HEADERS)
                            .build();
                }
            }
        }
        return ApiGatewayResponse.builder()
                            .setStatusCode(502)
                            .setObjectBody(new Response("Well, this is odd..."))
                            .setHeaders(HEADERS)
                            .build();
    }

}
