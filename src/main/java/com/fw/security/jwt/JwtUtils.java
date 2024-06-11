package com.fw.security.jwt;

import com.fw.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "myfirstmybatisapiappabababababaababababa";

    private static Key hmacKey = new SecretKeySpec(Base64.getEncoder().encode("myfirstmybatisapiappabababababaababababa".getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256.getJcaName());


    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY).build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
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
