package com.emoticon.photo.util;

/**
 * Created by L'Accordeur on 2016/12/17.
 */
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;


/**
 * 图片工具类（使用七牛云存储服务）
 *
 */
public class FileUtils {
    private static final String ACCESS_KEY = "RprQ9ifB9-LL4LyjAcfqaNLz6o8SrWNKxuvgCpC6";//这里填上面我们讲到的，密钥管理里面的密钥
    private static final String SECRET_KEY = "x7Q7F_miiDdyR2RpC6FpGDEN2BEWF4bkey8ClnZV";
    private static final String BUCKET_NAME = "photo";//填我们在七牛云创建的 Bucket 名字

    //密钥配置
    private static Auth auth = Auth.create(ACCESS_KEY,SECRET_KEY);

    /**
     * 上传图片到七牛云存储
     *
     * @param fileURI
     */
    public static void upload(byte[] bytes,String fileURI) {
        //创建上传对象
        UploadManager uploadManager = new UploadManager();
        //简单上传，使用默认策略，只需要设置上传的空间即可
        String token = auth.uploadToken(BUCKET_NAME);

        try {
            //调用put方法上传
            Response response = uploadManager.put(bytes,fileURI,token);
            //打印返回的信息
            System.out.println(response.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.toString());
            try {
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //
            }
        }
    }

    /**
     * 删除七牛云存储上的图片
     * @param fileName
     */
    public static void delete(String fileName) {
        //实例化一个管理对象
        BucketManager bucketManager = new BucketManager(auth);
        try {
            bucketManager.delete(BUCKET_NAME,fileName);
        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
