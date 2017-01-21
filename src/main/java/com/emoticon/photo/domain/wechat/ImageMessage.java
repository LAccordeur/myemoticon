package com.emoticon.photo.domain.wechat;

/**
 * Created by L'Accordeur on 2017/1/14.
 */
public class ImageMessage extends BaseMessage {
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        this.Image = image;
    }
}
