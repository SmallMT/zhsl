package com.bov.mt.entity;

public class OAuthHeader {

    private String contentType;
    private String accept;
    private String authorization;
    private boolean hasSetContentType = false;
    private boolean hasSetAccept = false;
    private boolean hasSetAuthorization = false;
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
        hasSetContentType = true;
    }

    public boolean isHasSetContentType() {
        return hasSetContentType;
    }

    public boolean isHasSetAccept() {
        return hasSetAccept;
    }

    public boolean isHasSetAuthorization() {
        return hasSetAuthorization;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
        hasSetContentType = true;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
        hasSetAuthorization = true;
    }
}
