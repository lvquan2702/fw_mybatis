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
    @ResponseBody
    public List<User> findAll() {
        List<User> users = new ArrayList<User>();
        try {
            users = userMapper.findAll();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @PostMapping("/updateById")
    public ResponseData<Long> updateById( @RequestBody User user) {
        System.out.printf("Start to update user " + user.getId());
        try {
            if (!userMapper.findById(user.getId()).isEmpty()) {
                long tmp = userMapper.updateById(user.getId(),user);
                return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId() + " updated successfully in database!", tmp);
            }else {
                return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "User " + user.getId() + " not found in database!", 0L);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/addUser")
    public ResponseData<Long> addUser(@RequestBody User user) {
        System.out.printf("Start to insert user " + user.getId() + " into database!");
        try {
            if (userMapper.findByName(user.getUsernm()).isEmpty()) {
                long userId = userMapper.addNewUser(user);
                return new ResponseData<>(HttpStatus.CREATED.value(), "User " + user.getUsernm() + " added successfully in database!", userId);
            }else {
                return new ResponseData<>(HttpStatus.NOT_ACCEPTABLE.value(), "User " + user.getUsernm() + " already exists in database!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/deleteId")
    public ResponseData<Long> deleteUserById(@RequestBody User user) {
        System.out.printf("Start to delete user " + user.getId() + " in database!");
        try {
            if (!userMapper.findById(user.getId()).isEmpty()) {
                long tmp = userMapper.deleteUserById(user.getId());
                return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId() + " deleted successfully in database!", tmp);
            }else {
                return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "User " + user.getId() + " not found in database!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    @PostMapping("/login")
    @ResponseBody
    public Map<Object, String> login(@RequestBody User user) throws Exception {

        List<User> users = userMapper.login(user.getUsernm());
        Map<Object, String> result_map = new HashMap<>();
        try {
            if (!users.isEmpty() && users.get(0).getUsernm().equals(user.getUsernm()) && users.get(0).getPassword().equals(user.getPassword())) {
                String token = JwtUtils.generateToken(users.get(0));
                result_map.put("result_code", "000");
                result_map.put("result_message", "Login successful!");
                result_map.put("usernm", users.get(0).getUsernm());
                result_map.put("password", users.get(0).getPassword());
                result_map.put("token", token);
                return result_map;
            } else {
                result_map.put("result_code", "001");
                result_map.put("result_message", "Username or password incorrect!");
                return result_map;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
