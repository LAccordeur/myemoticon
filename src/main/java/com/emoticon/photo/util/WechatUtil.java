package com.emoticon.photo.util;

import com.emoticon.photo.domain.wechat.AccessToken;
import com.emoticon.photo.domain.Image;
import com.emoticon.photo.domain.User;
import com.emoticon.photo.service.ImageService;
import com.emoticon.photo.service.UserService;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by L'Accordeur on 2017/1/7.
 */
public class WechatUtil {
    private static final String APPID = "wxb30b3b954a329735";
    private static final String APPSECRET = "62949610b0d004a96a99610f313cde37";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String GET_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    private static final String UPDATE_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

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

    /**
     * 将微信后台中的临时图片上传到七牛云中
     * @param accessToken,mediaId,username
     * @return User
     * @throws IOException
     */
    public static User getImageToCould(String accessToken, String mediaId, String username) throws IOException {
        String url = GET_IMAGE_URL.replace("ACCESS_TOKEN",accessToken).replace("MEDIA_ID",mediaId);

        //获取图片资源
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        UserService userService = new UserService();
        String returnName = username.substring(6,11); //从用户名字密文中截取六位做返回名
        User user = userService.getUserByUsername(returnName); //检测该微信用户是否已经存在

        if (user == null) {
            String returnPassword = returnName;
            user = new User();
            user.setUsername(returnName);
            user.setPassword(returnPassword);
            userService.addUser(user);
        }

        //封装图片信息
        Image image = new Image();
        image.setUserID(user.getId() + "");
        image.setName("wx-" + UUID.randomUUID().toString().substring(0,5));
        image.setDate(new Date());
        image.setUrl(user.getUsername() + "/" + UUID.randomUUID());

        //上传图片至云中
        ImageService imageService = new ImageService();
        imageService.addImage(image,httpEntity.getContent());

        return user;
    }

    /*public static byte[] getImageFromCould(String imageIdFromUser) throws IOException {

        //获取指定图片的url
        String url = "http://of6xakfsc.bkt.clouddn.com/imageURL";
        ImageService imageService = new ImageService();
        List<Image> images = imageService.getAllImages();
        if (Integer.valueOf(imageIdFromUser) > 0 && Integer.valueOf(imageIdFromUser) <= images.size()) {
            Image image = images.get(Integer.valueOf(imageIdFromUser) - 1);
            System.out.println("所请求获取的图片ID为" + image.getId());
            url = url.replace("imageURL", image.getUrl());
        }

        //获取图片资源
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        byte[] bytes = new byte[1024*48];
        InputStream inputStream = httpEntity.getContent();
        while (inputStream.read(bytes) != -1){}
        inputStream.close();

        return bytes;
    }*/

    /**
     * 文件上传
     * @param filePath
     * @param accessToken
     * @param type
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPDATE_IMAGE_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if(!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }


    public static void getImageFromCould(String imageIdFromUser) throws IOException {

        //获取指定图片的url
        String url = "http://of6xakfsc.bkt.clouddn.com/imageURL";
        ImageService imageService = new ImageService();
        List<Image> images = imageService.getAllImages();
        if (Integer.valueOf(imageIdFromUser) > 0 && Integer.valueOf(imageIdFromUser) <= images.size()) {
            Image image = images.get(Integer.valueOf(imageIdFromUser) - 1);
            System.out.println("所请求获取的图片ID为" + image.getId());
            url = url.replace("imageURL", image.getUrl());
        }

        //获取图片资源
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        savePicToDisk(httpEntity.getContent(),"D:\\imagetemp\\","temp.jpg");

    }

    public static String sendImageToUser(String accessToken,String imageIdFromUser,String toUserName,String fromUserName) throws Exception {
        getImageFromCould(imageIdFromUser);
        String mediaId = upload("D:\\imagetemp\\temp.jpg",accessToken,"image");
        return MessageUtil.initWechatImageMessage(toUserName,fromUserName,mediaId);
    }

    /*public static void sendImageToUser(String accessToken,String imageIdFromUser) throws IOException {
        //设置url
        String url = UPDATE_IMAGE_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE","image");

        //设置请求参数
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        byte[] bytes = getImageFromCould(imageIdFromUser);
        multipartEntityBuilder.addBinaryBody("image",bytes);
        *//*multipartEntityBuilder.addTextBody("filename",imageIdFromUser);
        multipartEntityBuilder.addTextBody("filelength",bytes.length+"");
        multipartEntityBuilder.addTextBody("content-type","image/jpeg");*//*

        //请求
        HttpEntity httpEntity = multipartEntityBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        httpPost.setHeader("filename",imageIdFromUser);
        httpPost.setHeader("filelength",bytes.length+"");
        httpPost.setHeader("content-type","image/jpeg");

        //获取响应结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonObject = null;
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        System.out.println(jsonObject.getString("media_id"));

    }*/

    /*public static void sendImageToUser(String accessToken,String imageIdFromUser) throws IOException {

        //设置请求参数
        MultipartEntityBuilder multipartEntityBuilder = get_COMPATIBLE_Builder("UTF-8");

        *//*multipartEntityBuilder.addTextBody("type","image");
        multipartEntityBuilder.addTextBody("access_token",accessToken);*//*
        String url = UPDATE_IMAGE_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE","image");
        byte[] bytes = getImageFromCould(imageIdFromUser);
        multipartEntityBuilder.addBinaryBody("image",bytes, ContentType.APPLICATION_OCTET_STREAM,imageIdFromUser);
        multipartEntityBuilder.addTextBody("filename",imageIdFromUser);
        multipartEntityBuilder.addTextBody("content-type","image/jpeg");
        multipartEntityBuilder.addTextBody("filelength",bytes.length+"");

        //请求
        HttpEntity httpEntity = multipartEntityBuilder.build();
        HttpPost httpPost = getMultipartPost(url);
        httpPost.setEntity(httpEntity);

        //获取响应结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonObject = null;
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        System.out.println(jsonObject.getString("media_id"));


    }*/

    private static MultipartEntityBuilder get_COMPATIBLE_Builder(String charSet) {
        MultipartEntityBuilder result = MultipartEntityBuilder.create();
        result.setBoundary(getBoundaryStr("7da2e536604c8"))
                .setCharset(Charset.forName(charSet))
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        return result;
    }

    private static HttpPost getMultipartPost(String url) {
		// 这里设置一些post的头部信息，具体求百度吧
        HttpPost post = new HttpPost(url);
        post.addHeader("Connection", "keep-alive");
        post.addHeader("Accept", "*/*");
        post.addHeader("Content-Type", "multipart/form-data;boundary="
                + getBoundaryStr("7da2e536604c8"));
        post.addHeader("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        return post;
    }


    private static String getBoundaryStr(String str) {
        return "------------" + str;
    }


    /**
     * 将图片写到 硬盘指定目录下
     * @param in
     * @param dirPath
     * @param filePath
     */
    private static void savePicToDisk(InputStream in, String dirPath,
                                      String filePath) {

        try {
            File dir = new File(dirPath);
            if (dir == null || !dir.exists()) {
                dir.mkdirs();
            }

            //文件真实路径
            String realPath = dirPath.concat(filePath);
            File file = new File(realPath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
