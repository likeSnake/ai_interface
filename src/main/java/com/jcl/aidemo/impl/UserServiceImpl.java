package com.jcl.aidemo.impl;

import com.jcl.aidemo.bean.TextTemplate;
import com.jcl.aidemo.bean.User;
import com.jcl.aidemo.mapper.UserMapper;
import com.jcl.aidemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> selectAll() {
        return userMapper.selectAllUser();
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public User loginPassword(User user) {
        return userMapper.loginPassword(user);
    }

    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }
    @Override
    public List<TextTemplate> getAllTemplate() {
        return userMapper.getTextTemplates();
    }
}
