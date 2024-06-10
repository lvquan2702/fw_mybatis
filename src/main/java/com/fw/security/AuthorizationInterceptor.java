package com.fw.security;

import com.fw.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String auth = request.getHeader("Authorization");

            if (request.getHeader("Authorization") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;

            }

            if (request.getHeader("Authorization") == null) {
                response.sendError(HttpServletResponse.SC_ACCEPTED);
                return false;

            }

            Map<String, Object> authenInfo = JwtUtils.parseJwt(auth);

            logger.info("----Login by: " + authenInfo.get("user_id"));

            return true;

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			response.sendError(HttpServletResponse.SC_ACCEPTED);
            return false;
        }
    }
}
