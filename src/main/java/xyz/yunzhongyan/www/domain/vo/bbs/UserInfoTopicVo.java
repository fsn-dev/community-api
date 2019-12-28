package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
public class UserInfoTopicVo {
    private String id;
    private String title;
    private String modifyTime;
    // 评论数
    private Integer commentCount = 0;
    // 收藏数
//    private Integer collectCount;
    // 浏览数
    private Integer view = 0;
    //赞同数
    private Integer up = 0;
}
