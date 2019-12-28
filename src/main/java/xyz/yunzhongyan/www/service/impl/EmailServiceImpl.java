package xyz.yunzhongyan.www.service.impl;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.yunzhongyan.www.service.EmailService;
import xyz.yunzhongyan.www.util.HttpRequestUtil;

import java.util.HashMap;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {


    @Override
    public String sendRegisterEmail(String username, String mailbox, String url, String code) {
        String format = String.format("%s<%s>", username, mailbox);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("appid",15052);
        map.put("to",format);
        map.put("from_name","Fusion开源社区");
        //邮件模板
        map.put("project","X4qVS");
        map.put("signature","4ddebb1804f8b4d66059ef401ce135cf");
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("verification",url);
//        map1.put("verification2",url);
        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put("code",url);
        map.put("links",map1);
        map.put("vars",map2);
        String str = JSON.toJSONString(map);
        String result = HttpRequestUtil.sendPost("https://api.mysubmail.com/mail/xsend.json", str);
        log.info("EmailServiceImpl sendRegisterEmail{}",result);
        return result;
    }

    @Override
    public String sendPasswordEmail(String username, String mailbox, String url, String code) {
        String format = String.format("%s<%s>", username, mailbox);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("appid",15052);
        map.put("to",format);
        map.put("from_name","Fusion开源社区");
        //邮件模板
        map.put("project","LazWj1");
        map.put("signature","4ddebb1804f8b4d66059ef401ce135cf");
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("verification",url);
        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put("code",code);
        map.put("links",map1);
        map.put("vars",map2);
        String str = JSON.toJSONString(map);
        String result = HttpRequestUtil.sendPost("https://api.mysubmail.com/mail/xsend.json", str);
        log.info("EmailServiceImpl sendRegisterEmail{}",result);
        return result;
    }
}
