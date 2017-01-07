package com.emoticon.photo.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by L'Accordeur on 2016/12/20.
 */
public class DBUtils {

    public SqlSession getSqlSession() throws IOException {
        //读取配置信息
        Reader reader = Resources.getResourceAsReader("mybatis/Configuration.xml");
        //创建工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //获取SQLSession对象
        return sqlSessionFactory.openSession();
    }

}
