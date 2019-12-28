package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;

/**
 * 对Token进行操作的接口
 */
public interface RedisTokenService {

    /**
     * 创建一个token关联上指定用户
     * @param user 用户
     * @return 生成的token
     */
    String createToken(User user);

    /**
     * 检查token是否有效
     * @param token 登录用户的token
     * @return 有效返回用户, 无效则返回null
     */
     AuthenticatedUser checkToken(String token);

    /**
     * 清除token
     * @param token 登录用户的token
     */
    void deleteToken(String token);

}
