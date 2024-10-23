package com.fw.service;

import org.apache.ibatis.annotations.*;

import java.util.Map;

@Mapper
public interface UserMapper  {

    Map<String, Object> login(Map<String, Object> map) throws Exception;

    Map<String, Object> testLogin(Map<String,Object> map) throws Exception;

    boolean isValidUserSession(String userToken) throws Exception;
}
