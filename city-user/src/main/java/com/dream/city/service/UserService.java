package com.dream.city.service;

import com.dream.city.entity.User;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Wvv
 */
public interface UserService {

    boolean saveUser(User user);

    void deleteUser(Integer uId);

    User updateUser(User user);

}
