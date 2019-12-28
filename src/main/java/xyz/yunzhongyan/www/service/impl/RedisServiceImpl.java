package xyz.yunzhongyan.www.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.service.RedisService;


/**
 * @program: RedisService
 * @description:
 * @author: wander
 * @create:
 **/
@Component
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
}
