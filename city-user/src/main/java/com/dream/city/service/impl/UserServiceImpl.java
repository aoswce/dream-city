package com.dream.city.service.impl;

import com.dream.city.entity.User;
import com.dream.city.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Wvv
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public boolean saveUser(User user) {
        return false;
    }

    @Override
    public void deleteUser(Integer uId) {
        System.out.println("["+appName+"]Delete user"+uId);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
