package com.kz.coderscircle.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.expire-time}")
    private long expireTimeInSeconds;

    public String genToken(Map<String, Object> claims) {
        long expireTimeInMillis = expireTimeInSeconds * 1000;
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTimeInMillis))
                .sign(Algorithm.HMAC256(key));
    }

    public Map<String, Object> parseToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token);
        return decodedJWT.getClaim("claims").asMap();
    }
}