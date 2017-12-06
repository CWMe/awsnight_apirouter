package com.codewithme.apirouter;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class NewMain {
    
    private static final RestClient REST_CLIENT = new RestClient();

    public static void main(String[] args) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
        Article article = REST_CLIENT.resteasy(new URLRequest("http://www.cnn.com/2017/12/02/politics/trump-tweet-flynn-firing-fbi-reaction/index.html"));
        System.out.println(JsonUtil.OBJECT_MAPPER.writeValueAsString(article));
    }
    
}
