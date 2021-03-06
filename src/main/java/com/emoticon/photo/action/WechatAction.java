package com.emoticon.photo.action;

import com.emoticon.photo.domain.wechat.AccessToken;
import com.emoticon.photo.domain.User;
import com.emoticon.photo.util.CheckUtil;
import com.emoticon.photo.util.MessageUtil;
import com.emoticon.photo.util.WechatUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by L'Accordeur on 2017/1/6.
 */
@WebServlet(value = "/wechat.action")
@MultipartConfig
public class WechatAction extends HttpServlet{

    private static final long serialVersionUID = 1L;

    /**
     * 接入验证
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");

        PrintWriter out = resp.getWriter();
        if(CheckUtil.checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        }
    }

    /**
     * 消息的接收与响应
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            //获取微信后台发来的消息数据
            Map<String, String> map = MessageUtil.xmlToMap(req);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            //返回信息
            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {

                String imageIdFromUser = content.substring(5,content.length());
                System.out.println("imageId " + imageIdFromUser);
                AccessToken accessToken = WechatUtil.getAccessToken();
                message = WechatUtil.sendImageToUser(accessToken.getToken(),imageIdFromUser,toUserName,fromUserName);
            } else if (MessageUtil.MESSAGE_IMAGE.equals(msgType)) {
                String mediaId = map.get("MediaId");
                AccessToken accessToken = WechatUtil.getAccessToken();
                User user = WechatUtil.getImageToCould(accessToken.getToken(),mediaId,fromUserName);
                message = MessageUtil.initText(toUserName,fromUserName,"图片上传成功! 您的用户名和密码均为" + user.getUsername());
            }

            System.out.println(message);
            out.print(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }


    }

}
