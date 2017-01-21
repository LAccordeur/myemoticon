import com.emoticon.photo.domain.wechat.AccessToken;
import com.emoticon.photo.util.WechatUtil;

/**
 * Created by L'Accordeur on 2017/1/13.
 */
public class Test {
    public static void main(String[] args) {
        try {
            AccessToken token = WechatUtil.getAccessToken();
           /* WechatUtil.sendImageToUser(token.getToken(),"4");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
