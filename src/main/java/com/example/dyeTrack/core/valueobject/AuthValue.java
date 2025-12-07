package com.example.dyeTrack.core.valueobject;

import com.example.dyeTrack.core.entity.User;

public final class AuthValue {
    private final TokenVO token;
    private final User user;

    public AuthValue(TokenVO token, User user) {
        this.token = token;
        this.user = user;
    }

    public TokenVO getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
