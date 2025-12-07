package com.example.dyeTrack.core.valueobject;

public final class TokenVO {
    private final String token;
    private final int expireIn;
    private final String tokenType;


    public TokenVO(String token, int expireIn, String tokenType) {
        this.token = token;
        this.expireIn = expireIn;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public String getTokenType() {
        return tokenType;
    }
}
