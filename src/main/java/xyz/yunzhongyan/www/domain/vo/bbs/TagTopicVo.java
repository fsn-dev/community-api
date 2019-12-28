package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
public class TagTopicVo {
    private String id;
    private String icon;
    private String title;
    private String userId;
    private String userName;
    private String content;
    private String contentMD;
    private String modifyTime;
}
