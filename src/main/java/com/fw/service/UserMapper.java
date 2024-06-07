package com.fw.service;

import com.fw.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper  {

    List<User> findAll() throws Exception;


    long updateById(int userId, User user) throws Exception;


    long addNewUser(User user) throws Exception;

    long deleteUserById(int userId, User user) throws Exception;
}
