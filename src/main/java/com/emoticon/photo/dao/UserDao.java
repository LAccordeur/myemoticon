package com.emoticon.photo.dao;

import com.emoticon.photo.domain.User;
import com.emoticon.photo.util.DBUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * Created by L'Accordeur on 2017/1/6.
 */
public class UserDao {
    public User getUserByUsername(String username) {
        User user = null;

        //得到SQLSession对象
        try {
            DBUtils dbUtils = new DBUtils();
            SqlSession sqlSession = dbUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.getUserByUsername(username);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void addUser(User user) {
        //得到SQLSession对象

        try {
            DBUtils dbUtils = new DBUtils();
            SqlSession sqlSession = dbUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.addUser(user);
            //必须要commit
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
