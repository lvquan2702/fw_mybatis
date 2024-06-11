package com.fw.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fw.payload.request.LoginRequest;
import com.fw.payload.response.ResponseData;
import com.fw.payload.response.UserInfoResponse;
import com.fw.security.jwt.JwtUtils;
import com.fw.service.UserMapper;
import com.fw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getAll")
    public List<User> getAll() {
        List<User> users = new ArrayList<User>();
        try {
            users = userMapper.findAll();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @PutMapping("/updateById/{userId}")
    public ResponseData<Long> updateById(@PathVariable int userId, @RequestBody User user) {
        System.out.printf("Start to update user " + userId);
        try {
            long tmp = userMapper.updateById(userId, user);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId() + " updated successfully in database!", tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/addUser")
    public ResponseData<Long> addUser(@RequestBody User user) {
        System.out.printf("Start to insert user " + user.getId() + " into database!");
        try {
            long userId = userMapper.addNewUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User " + user.getId() + " added successfully in database!", userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    @DeleteMapping("/deleteId/{userId}")
    public ResponseData<Long> deleteUserById(@PathVariable int userId, @RequestBody User user) {
        System.out.printf("Start to delete user " + user.getId() + " in database!");
        try {
            long tmp = userMapper.deleteUserById(userId, user);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId() + " deleted successfully in database!", tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    @PostMapping("/login")
    @ResponseBody
    public Map<Object, String> login(@RequestBody User user) throws Exception {

        List<User> users = userMapper.login(user.getUsernm());
        Map<Object,String > result_map = new HashMap<>();
        try {
            if (users != null&& users.get(0).getUsernm().equals(user.getUsernm()) && users.get(0).getPassword().equals(user.getPassword())) {
                String token = JwtUtils.generateToken(users.get(0));
                result_map.put("result_code", "000");
                result_map.put("result_message", "Login successful!");
                result_map.put("usernm", users.get(0).getUsernm());
                result_map.put("password", users.get(0).getPassword());
                result_map.put("token", token);
                return result_map;
            }
            else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        return new HashMap<>(HttpStatus.UNAUTHORIZED.value());
        }
        return new HashMap<>(HttpStatus.UNAUTHORIZED.value());
    }

}
