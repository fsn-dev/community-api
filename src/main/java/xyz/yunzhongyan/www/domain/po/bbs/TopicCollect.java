package xyz.yunzhongyan.www.domain.po.bbs;

import lombok.Data;

import java.util.Date;

/**
 * @program: api
 * @description: 收藏
 * @author: wander
 * @create: 2019-11-22 14:16
 **/
@Data
public class TopicCollect {
    private String topicId;
    private String userId;
    private Date creatTime;
}
