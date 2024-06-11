package com.fw.service.impl;

import com.fw.model.User;
import com.fw.service.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.*;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public List<User> findAll() throws Exception {
        Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> users = sqlSession.selectList("User.findAll");
        sqlSession.close();
        return users;

    }

    @Override
    public long addNewUser(User user) throws Exception {
        new User();
        Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.insert("User.insertUser", user);
        sqlSession.close();
        return user.getId();
    }

    @Override
    public long updateById(int userId, User user) throws Exception {
        new User();
        user.setId(userId);
        Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int result = sqlSession.update("User.updateUserById", user);
        sqlSession.commit();
        sqlSession.close();
        return result;
    }

    @Override
    public long deleteUserById(int userId, User user) throws Exception {
        new User();
        user.setId(userId);
        Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int result = sqlSession.delete("User.deleteUserById", user);
        sqlSession.commit();
        System.out.println(result);
        sqlSession.close();
        return result;
    }

    @Override
    public User findByName(String usernm) throws Exception {
        return null;
    }


    @Autowired
    @Lazy
    private UserMapper userMapper;

    public List<User> login(String usernm) throws Exception {
        User user = new User();
        user.setUsernm(usernm);
        Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> users = sqlSession.selectList("User.findByName", usernm);
        sqlSession.close();
        return users;
    }


}
