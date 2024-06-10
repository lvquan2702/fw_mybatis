package com.fw.security.jwt;

import com.fw.service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;

import static io.jsonwebtoken.Jwts.*;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${fw.app.jwtSecret}")
    private String jwtSecret;
    @Value("${fw.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${fw.app.jwtCookieName}")
    private String jwtCookie;
    public static final String AUTHORIZATION = "myfirstmybatisproject";

    private static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(AUTHORIZATION),
            SignatureAlgorithm.HS256.getJcaName());
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie(HttpServletRequest request) {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    public String getUsernameFromJwtToken(String token) {
        return parser().setSigningKey(key()).parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            parser().setSigningKey(key()).parseClaimsJws(authToken);
            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    /**
     * Valid and Parser Jwt Token String
     */
    public static Map<String, Object> parseJwt(String jwtString)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        Claims jwtClaims;

        try {
            jwtClaims = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString).getBody();
        } catch (ExpiredJwtException e) {
            // Get Claims from expired token
            jwtClaims = e.getClaims();
        }

        Map<String, Object> map = (Map<String, Object>) jwtClaims;

        return map;
    }
    public String generateTokenFromUsername(String username) {
        return builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }
}
