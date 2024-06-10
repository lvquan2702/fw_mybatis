package com.fw.controller;

import com.fw.payload.request.LoginRequest;
import com.fw.payload.response.ResponseData;
import com.fw.payload.response.UserInfoResponse;
import com.fw.security.jwt.JwtUtils;
import com.fw.service.UserMapper;
import com.fw.model.User;
import com.fw.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

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
        System.out.printf("Start to update user "+ userId);
        try {
            long tmp = userMapper.updateById(userId, user);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId()  +   " updated successfully in database!", tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @PostMapping("/addUser")
    public ResponseData<Long> addUser(@RequestBody User user)  {
        System.out.printf("Start to insert user "+ user.getId() +" into database!");
        try {
            long userId = userMapper.addNewUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User " + user.getId()  +   " added successfully in database!", userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }
    @DeleteMapping("/deleteId/{userId}")
    public ResponseData<Long> deleteUserById(@PathVariable int userId ,@RequestBody User user)  {
        System.out.printf("Start to delete user "+ user.getId() +" in database!");
        try {
            long tmp = userMapper.deleteUserById(userId,user);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User " + user.getId()  +   " deleted successfully in database!", tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl)  authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(), roles));
    }

}
