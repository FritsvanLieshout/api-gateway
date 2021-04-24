package com.kwetter.frits.apigateway.entity;

public class EndpointAuthority {

    private String endpoint;
    private String authority;

    public EndpointAuthority(String endpoint, String authority) {
        this.endpoint = endpoint;
        this.authority = authority;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
