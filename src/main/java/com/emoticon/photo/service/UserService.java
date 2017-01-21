package com.emoticon.photo.service;

import com.emoticon.photo.dao.ImageMapper;
import com.emoticon.photo.dao.UserDao;
import com.emoticon.photo.dao.UserMapper;
import com.emoticon.photo.domain.User;
import com.emoticon.photo.util.DBUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * Created by L'Accordeur on 2016/12/20.
 */
public class UserService {

    public User getUserByUsername(String username) {
        UserDao userDao = new UserDao();
        return userDao.getUserByUsername(username);
    }

    public void addUser(User user) {
        UserDao userDao = new UserDao();
        userDao.addUser(user);
    }
}
