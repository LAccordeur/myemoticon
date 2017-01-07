package com.emoticon.photo.util;

import com.emoticon.photo.domain.AccessToken;
import com.emoticon.photo.domain.Image;
import com.emoticon.photo.service.ImageService;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by L'Accordeur on 2017/1/7.
 */
public class WechatUtil {
    private static final String APPID = "wxb30b3b954a329735";
    private static final String APPSECRET = "62949610b0d004a96a99610f313cde37";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String GET_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    /**
     * get请求
     * @param url
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doGetStr(String url) throws ParseException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        HttpResponse httpResponse = client.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        return jsonObject;
    }

    /**
     * 获取accessToken
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessToken() throws ParseException, IOException{
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if(jsonObject!=null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        return token;
    }

    public static void getImageToCould(String accessToken, String mediaId) throws IOException {
        String url = GET_IMAGE_URL.replace("ACCESS_TOKEN",accessToken).replace("MEDIA_ID",mediaId);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        //封装图片信息
        Image image = new Image();
        image.setUserID("10001"); //10001表示是微信用户上传的
        image.setName("wechat" + new Random().nextInt());
        image.setDate(new Date());
        image.setUrl("10001/" + UUID.randomUUID());

        //上传图片至云中
        ImageService imageService = new ImageService();
        imageService.addImage(image,httpEntity.getContent());

    }
}
