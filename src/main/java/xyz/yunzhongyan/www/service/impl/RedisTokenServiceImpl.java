package xyz.yunzhongyan.www.service.impl;

import lombok.extern.slf4j.Slf4j;
import xyz.yunzhongyan.www.service.RedisTokenService;
import xyz.yunzhongyan.www.util.Constants;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 */
@Component
@Slf4j
public class RedisTokenServiceImpl implements RedisTokenService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String createToken(User user) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(authenticatedUser.generateRedisKey()).set(authenticatedUser, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return token;
    }

    public AuthenticatedUser checkToken(String token) {
        if (token == null) return null;

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) redisTemplate.opsForValue().get(new AuthenticatedUser().generateRedisKey(token));
        if (authenticatedUser == null) return null;
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisTemplate.boundValueOps(authenticatedUser.generateRedisKey()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return authenticatedUser;
    }

    public void deleteToken(String token) {
        redisTemplate.delete(new AuthenticatedUser().generateRedisKey(token));
    }
}
