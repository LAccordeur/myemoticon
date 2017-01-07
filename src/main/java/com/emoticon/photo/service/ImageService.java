package com.emoticon.photo.service;

import com.emoticon.photo.dao.ImageDao;
import com.emoticon.photo.dao.ImageMapper;
import com.emoticon.photo.domain.Image;
import com.emoticon.photo.domain.User;
import com.emoticon.photo.util.DBUtils;
import com.emoticon.photo.util.FileUtils;
import com.emoticon.photo.util.InputStreamToByte;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by L'Accordeur on 2016/12/17.
 */
public class ImageService {
    /**
     * 通过用户ID获取图片列表
     * @param userId 用户ID
     * @return 图片列表
     */
    public List<Image> getByUserId(int userId) {

        ImageDao imageDao = new ImageDao();
        return imageDao.getByUserId(userId);
    }

    /**
     * 上传图片
     * @param image 图片
     * @param inputStream 输入流
     */
    public void addImage(Image image, InputStream inputStream) {
        ImageDao imageDao = new ImageDao();
        imageDao.addImage(image,inputStream);
    }

    /**
     * 通过图片ID数组和图片URL数组删除图片
     * @param ids 图片ID数组
     * @param urls 图片URL数组
     */
    public void delByIdsAndUrls(String ids, String urls) {
        ImageDao imageDao = new ImageDao();
        imageDao.delByIdsAndUrls(ids,urls);
    }

    /**
     * 获取所有图片列表
     * @param
     * @return 图片列表
     */
    public List<Image> getAllImages() {
        ImageDao imageDao = new ImageDao();
        return imageDao.getAllImages();
    }

}
