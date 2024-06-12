package com.fw.service;

import com.fw.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper  {

    List<User> findAll() throws Exception;


    long updateById(int userId, User user) throws Exception;


    long addNewUser(User user) throws Exception;

    long deleteUserById(int userId) throws Exception;

    List<User> findByName(String usernm) throws Exception;
    List<User> findById(int id) throws Exception;

    List<User> login(String usernm) throws Exception;
}
