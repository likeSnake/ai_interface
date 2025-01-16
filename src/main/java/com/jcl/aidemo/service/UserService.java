package com.jcl.aidemo.service;

import com.jcl.aidemo.bean.User;

import java.util.List;

public interface UserService {
    public List<User> selectAll();
    public User getUserById(int id);
    public int addUser(User user);
    public int updateUser(User user);
    public int deleteUser(int id);
    public User loginPassword(User user);
    public User getUserByName(String userName);
}
