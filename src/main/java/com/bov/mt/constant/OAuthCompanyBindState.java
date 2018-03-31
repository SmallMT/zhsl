package com.bov.mt.constant;

public enum  OAuthCompanyBindState {

    OK("已验证"),FAIL("FAIL");
    private String value;
    OAuthCompanyBindState(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
