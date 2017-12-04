package com.codewithme.apirouter;

public class URLRequest {
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
