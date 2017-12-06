package com.codewithme.apirouter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class NewMain {
    
    private static final RestClient REST_CLIENT = new RestClient();

    public static void main(String[] args) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
        Handler handler = new Handler();
        Map<String, Object> map = new HashMap<>();
        ObjectNode node = JsonUtil.OBJECT_MAPPER.createObjectNode();
        node.put("url", "http://www.cnn.com/2017/12/02/politics/trump-tweet-flynn-firing-fbi-reaction/index.html");
        
        map.put("body", JsonUtil.OBJECT_MAPPER.writeValueAsString(node));
        ApiGatewayResponse response = handler.handleRequest(map, null);
        System.out.println(response.getBody());
//        Article article = REST_CLIENT.resteasy(new URLRequest("http://www.cnn.com/2017/12/02/politics/trump-tweet-flynn-firing-fbi-reaction/index.html"));
//        System.out.println(JsonUtil.OBJECT_MAPPER.writeValueAsString(article));
    }
    
}
