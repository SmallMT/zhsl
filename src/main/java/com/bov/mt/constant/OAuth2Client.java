package com.bov.mt.constant;

public enum OAuth2Client {

    USER("zhsl"),PASSWORD("1TJTYMkdNOqg9uS9UMVT");

    private String value;

    OAuth2Client(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
