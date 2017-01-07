package com.emoticon.photo.domain;

/**
 * Created by L'Accordeur on 2017/1/7.
 */
public class TextMessage extends BaseMessage {
    private String Content;
    private String MsgId;

    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
    public String getMsgId() {
        return MsgId;
    }
    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
