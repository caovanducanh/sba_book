package com.example.demologin.dto.request.login;

public class FacebookLoginRequest {
    private String accessToken;

    public FacebookLoginRequest() {
    }

    public FacebookLoginRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
