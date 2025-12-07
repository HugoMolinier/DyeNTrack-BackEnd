package com.example.dyeTrack.core.port.out;

import com.example.dyeTrack.core.valueobject.TokenVO;

public interface JwtServicePort {
    TokenVO generateToken(Long userId);

    Long extractUserId(String token);
}