package com.bov.mt.entity;

public class Token {

    private String code;//请求状态码
    private String token;//请求成功后获取的状态码
    private String error;//请求错误提示信息

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
