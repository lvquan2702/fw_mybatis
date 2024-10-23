package com.fw.security.jwt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

public class JwtUtils {
    private static final String SECRET_KEY = "myfirstmybatisapiappabababababaababababa";

    private static Key hmacKey = new SecretKeySpec(Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256.getJcaName());

    private static final Gson gson = new Gson();
    public static Map<String, Object> validateToken(String jwtString, boolean isCheckExp)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, Object> map;
        try {
            Claims jwtClaims = Jwts.parser().setSigningKey(hmacKey).parseClaimsJws(jwtString).getBody();
            map = (Map<String, Object>) jwtClaims;

        } catch (ExpiredJwtException ex) {
            if (!isCheckExp) {
                String[] parts = jwtString.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                return gson.fromJson(new String(decoder.decode(parts[1])), type);
            } else {
                throw ex;
            }
        }

        return map;
    }

    public static String generateToken(Map<String, Object> claims, String subject, int expDuration){
        Date createdDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(createdDate);
        c.add(Calendar.DATE, expDuration );
        Date expirationDate = c.getTime();

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwtToken;
    }
}
