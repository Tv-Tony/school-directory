package com.tvtoner.schoolapp.security.dao;

import com.tvtoner.schoolapp.security.entity.User;
public interface UserDao {

    User findByUserName(String userName);

    void save(User theUser);

    void deleteUser(User user);

}
