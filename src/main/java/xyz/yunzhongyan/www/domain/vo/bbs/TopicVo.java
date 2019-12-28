package xyz.yunzhongyan.www.domain.vo.bbs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
public class TopicVo {
    private String id;
    private String tagId;
    private String tagName;
    private String tagIcon;

    private String title;
    private String content;
    private String modifyTime;
    private String topicIcon;
    //作者
    private String userId;
    private String userName;
    // 评论数
    private Integer commentCount=0;
    // 收藏数
//    private Integer collectCount;
    // 浏览数
    private Integer view=0;
    //赞同数
    private Integer up=0;

    private TopicCommentVo topicCommentVo;
}
