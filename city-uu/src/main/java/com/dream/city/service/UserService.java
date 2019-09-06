package com.dream.city.service;

import com.dream.city.domain.entity.User;
import com.dream.city.domain.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getById(int id){
        return userMapper.getById(id);
    }
    public List<User> getUsers(){
        return userMapper.getUsers();
    }
}
