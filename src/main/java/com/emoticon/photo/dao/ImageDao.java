package com.emoticon.photo.dao;

import com.emoticon.photo.domain.Image;
import com.emoticon.photo.util.DBUtils;
import com.emoticon.photo.util.FileUtils;
import com.emoticon.photo.util.InputStreamToByte;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by L'Accordeur on 2017/1/6.
 */
public class ImageDao {
    /**
     * 通过用户ID获取图片列表
     * @param userId 用户ID
     * @return 图片列表
     */
    public List<Image> getByUserId(int userId) {

        List<Image> images = new ArrayList<Image>();
        SqlSession sqlSession = null;

        //得到SQLSession对象
        try {
            DBUtils dbUtils = new DBUtils();
            sqlSession = dbUtils.getSqlSession();
            ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
            images = imageMapper.getByUserId(userId);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return images;
    }

    /**
     * 上传图片
     * @param image 图片
     * @param inputStream 输入流
     */
    public void addImage(Image image, InputStream inputStream) {
        SqlSession sqlSession = null;

        try {
            //将图片实体上传到七牛云
            FileUtils.upload(InputStreamToByte.input2byte(inputStream), image.getUrl());
            //将图片的属性信息保存到数据库
            DBUtils dbUtils = new DBUtils();
            sqlSession = dbUtils.getSqlSession();
            ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
            imageMapper.addImage(image);

            sqlSession.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过图片ID数组和图片URL数组删除图片
     * @param ids 图片ID数组
     * @param urls 图片URL数组
     */
    public void delByIdsAndUrls(String ids, String urls) {
        String[] idArray = ids.split(",");
        String[] urlArray = urls.split(",");
        SqlSession sqlSession = null;

        if (!"".equals(idArray[0]) && !"".equals(urlArray[0])) {

            try {
                DBUtils dbUtils = new DBUtils();
                sqlSession = dbUtils.getSqlSession();
                ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
                for (int i = 0; i < idArray.length; i++) {
                    FileUtils.delete(urlArray[i]);
                    imageMapper.delByIdsAndUrls(Integer.parseInt(idArray[i]));
                    sqlSession.commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有图片列表
     * @param
     * @return 图片列表
     */
    public List<Image> getAllImages() {
        List<Image> images = null;

        try {
            DBUtils dbUtils = new DBUtils();
            SqlSession sqlSession = dbUtils.getSqlSession();
            ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
            images = imageMapper.getAllImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
    }
}
