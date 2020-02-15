package xyz.yunzhongyan.www.util;


/**
 * 常量
 */
public class Constants {

    /**
     * 文件上传路径
     */
    public static final String IMAGE_PATH = "/mnt/image/";
    /**
     * 文件上传URL
     */


    /**public static final String IMAGE_SERVER = "https://fsn.dev:1186/";
     * 存储当前登录用户id的字段名
     */
    public static final String CURRENT_USER_ID = "currentUserId";

    /**
     * token有效期（小时）
     */
    public static final int TOKEN_EXPIRES_HOUR = 24;

    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHENTICATION = "authentication";

    // 这里填写在GitHub上注册应用时候获得 CLIENT ID
    public static final String CLIENT_ID = "1b537f502b9e647a9f82";
    //这里填写在GitHub上注册应用时候获得 CLIENT_SECRET
    public static final String CLIENT_SECRET = "8367b144f4783c60178b2f95d55c8beb02f76f40";
    // 回调路径
    public static final String CALLBACK = "https://fsn.dev/oauth/redirect";

    //获取code的url
    public static final String CODE_URL = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID + "&state=STATE&redirect_uri=" + CALLBACK + "";
    //获取token的url
    public static final String TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=CODE&redirect_uri=" + CALLBACK + "";
    //获取用户信息的url
    public static final String USER_INFO_URL = "https://api.github.com/user?access_token=TOKEN";
}
