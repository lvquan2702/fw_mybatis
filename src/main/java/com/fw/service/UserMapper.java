package com.fw.service;

import com.fw.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper  {

    List<User> findAll() throws Exception;


    long updateById(int userId, User user) throws Exception;


    long addNewUser(User user) throws Exception;

    long deleteUserById(int userId, User user) throws Exception;

    User findByName(String usernm) throws Exception;


    List<User> login(String usernm) throws Exception;
}
