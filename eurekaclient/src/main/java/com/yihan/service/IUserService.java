package com.yihan.service;

import com.yihan.entity.User;

import java.util.List;

public interface IUserService {
    public List<User> findAll();

    public User findUserById(Long id);

    public void saveOrUpdateUser(User user);

    public void deleteUserById(User user);
}
