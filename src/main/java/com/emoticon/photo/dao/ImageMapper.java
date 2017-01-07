package com.emoticon.photo.dao;

import com.emoticon.photo.domain.Image;
import java.util.List;

/**
 * Created by L'Accordeur on 2017/1/6.
 */
public interface ImageMapper {

    List<Image> getByUserId(int userId);

    void addImage(Image image);

    void delByIdsAndUrls(int id);

    List<Image> getAllImages();
}
