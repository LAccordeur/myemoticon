package com.emoticon.photo.dao;

import com.emoticon.photo.domain.User;

/**
 * Created by L'Accordeur on 2017/1/6.
 */
public interface UserMapper {
    User getUserByUsername(String username);

    void addUser(User user);
}
