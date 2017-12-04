package com.codewithme.apirouter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class URLRequest {
    
    @JsonProperty(required = true, value = "url")
    private String url;

    public URLRequest() {
    }

    public URLRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "URLRequest{" + "url=" + url + '}';
    }
}
