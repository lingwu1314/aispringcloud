package com.yihan.service.impl;

import com.yihan.entity.User;
import com.yihan.service.IUserService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserServiceImp implements IUserService {
    private static Map<Long, User> userMap;

    static{
        userMap=new HashMap<Long, User>();
        userMap.put(1L,new User(1L,"张三",15));
        userMap.put(2L,new User(2L,"王五",16));
        userMap.put(3L,new User(2L,"赵柳",18));
    }

    @Override
    public List<User> findAll() {
        List<User> list=new ArrayList<>(userMap.values());
        return list;
    }

    @Override
    public User findUserById(Long id) {
        return userMap.get(id);
    }

    @Override
    public void saveOrUpdateUser(User user) {
        userMap.put(user.getUseId(),user);
    }

    @Override
    public void deleteUserById(User user) {
        userMap.remove(user.getUseId());
    }
}
