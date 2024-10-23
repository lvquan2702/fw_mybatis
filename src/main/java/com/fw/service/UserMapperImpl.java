package com.fw.service;

import com.fw.model.UserDao;
import com.fw.security.jwt.JwtUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserMapperImpl implements UserMapper {

    @Autowired
    UserDao userDao;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private String encryptPass(String plainText) {
        String cipherText;
        cipherText = DigestUtils.sha256Hex(plainText);
        return cipherText;
    }

    public boolean isValidName(String userName) {
        return userName != null && userName.matches("^[a-zA-Z0-9_]+$");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isValidUserSession(String userToken) throws Exception {
        boolean res = false;

        try {
            // Verify the JWT string format
            Map<String, Object> data = JwtUtils.validateToken(userToken, true);

            // Verify user and login time
            String verifyLoginTime = data.get("last_login_il").toString().concat(data.get("last_login_si").toString());
            logger.info("Request From: {} - {}", (String) data.get("user_id"), verifyLoginTime);

            data.put("s_user_id", data.get("user_id"));
            Map<String, Object> currentUser = (Map<String, Object>) userDao.selectOne("user.getUserDetailList", data);
            String currentLogin = currentUser.get("last_login").toString();
            String currentLogut = Objects.isNull(currentUser.get("last_logout")) ? null : currentUser.get("last_logout").toString();

            if (verifyLoginTime.equals(currentLogin)) {
                if (currentLogut == null) {
                    res = true;
                } else {
                    // The key only valid, if the user have not logout yet
                    LocalDateTime currentLoginTime = LocalDateTime.parse(currentLogin, DATE_TIME_FORMAT);
                    LocalDateTime currentLogutTime = LocalDateTime.parse(currentLogut, DATE_TIME_FORMAT);
                    if (currentLoginTime.isAfter(currentLogutTime)) {
                        res = true;
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> login(Map<String,Object> map) throws Exception {

        map.put("pw", encryptPass(map.getOrDefault("pw","").toString()));

        if (!isValidName(map.get("user_id").toString())) {
            throw new Exception("Authentication failed. User ID or password is incorrect.");
        }

        Map<String, Object> currentUser = (Map<String, Object>) userDao.selectOne("user.checkUserForLogin", map);

        Map<String, Object> result_map = new HashMap<String, Object>();

        if (currentUser != null) {

            // throw exception if the user was deactivated
            if (!currentUser.get("active_yn").equals("1")) {
                throw new Exception("User was deactivated. Please contact administrator.");
            } else {
                // prepare data to update login/logout information
                Map<String, Object> updatelogInfo = new HashMap<>();
                LocalDateTime currentDateTime = LocalDateTime.now();

                // Set user id and the new login time.
                updatelogInfo.put("user_id", currentUser.get("user_id"));
                updatelogInfo.put("last_login_il", DATE_FORMAT.format(currentDateTime));
                updatelogInfo.put("last_login_si", TIME_FORMAT.format(currentDateTime));

                // create JWT token to return for client
                String token = JwtUtils.generateToken(updatelogInfo, (String) currentUser.get("user_id"),
                        1);

                // Check login/logout time, the current user was logout or not
                Timestamp currentLogin = (Timestamp) currentUser.get("last_login");
                Timestamp currentLogout = (Timestamp) currentUser.get("last_logout");

                if (currentLogin != null) {

                    // if the user have not logout yet, need to update logout time firstly.
                    if (currentLogout != null) {
                        LocalDateTime currentLoginTime = currentLogin.toLocalDateTime();
                        LocalDateTime currentLogutTime = currentLogout.toLocalDateTime();
                        if (currentLoginTime.isAfter(currentLogutTime)) {
                            updatelogInfo.put("last_logout_il", DATE_FORMAT.format(currentDateTime.minusSeconds(1)));
                            updatelogInfo.put("last_logout_si", TIME_FORMAT.format(currentDateTime.minusSeconds(1)));
                        }
                    } else {
                        updatelogInfo.put("last_logout_il", DATE_FORMAT.format(currentDateTime.minusSeconds(1)));
                        updatelogInfo.put("last_logout_si", TIME_FORMAT.format(currentDateTime.minusSeconds(1)));
                    }
                }
                if(Objects.isNull(currentUser.get("pw_change_il"))) {
                    currentUser.put("first_login_yn", "1");
                }
                // update the user info
                userDao.update("user.updateInOutOfUser", updatelogInfo);

                logger.info("Login From: {} At: {}", (String) currentUser.get("user_id"),
                        updatelogInfo.get("last_login_il").toString()
                                .concat(updatelogInfo.get("last_login_si").toString()), "");

                // return data for client
                currentUser.remove("last_login");
                currentUser.remove("last_logout");
                currentUser.remove("active_yn");
                currentUser.remove("pw_change_il");
                currentUser.put("token", token);

                result_map.put("status", "ok");
                result_map.put("payload", currentUser);
            }

        } else {
            // update User (try time and active status) when login failed
            map.put("login_sts", false);
            userDao.update("user.setTryTimebyUser", map);

            throw new Exception("Authentication failed. User ID or password is incorrect.");
        }

        // update User (try time and active status) when login success
        map.put("login_sts", true);
        userDao.update("user.setTryTimebyUser", map);

        return result_map;
    }

    @Override
    public Map<String, Object> testLogin(Map map) throws Exception {
        Map<String, Object> result_map = new HashMap<>();
        String userId = (String) map.get("user_id");
        List<Map<String, Object>> users = userDao.selectList("User.checkUserForLogin",userId);
        result_map.put("payload",users);
        return result_map;
    }
}
