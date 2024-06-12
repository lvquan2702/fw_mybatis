package com.fw.security.jwt;

import com.fw.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY = "myfirstmybatisapiappabababababaababababa";

    private static Key hmacKey = new SecretKeySpec(Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256.getJcaName());


    public static Map<String, Object> validateToken(String token)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Claims claims;
        try {
             claims = Jwts.parserBuilder()
                    .setSigningKey(hmacKey).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        Map<String, Object> map = (Map<String, Object>)claims;
        return map;
    }

    public static String generateToken(User user){
        long expirationTime = 5 * 60 * 1000; // 5 phút
        return Jwts.builder()
                .setSubject(user.getUsernm())
                .setSubject(user.getPassword())
                .setSubject(user.getEmail())
                .setSubject(user.getPhone())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(hmacKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
