package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
public class TopicDetailVo {
    private String id;
    private String title;
    private String content;
    private String contentMD;
    private String modifyTime;
    private String tagId;
    private String tagName;
    //作者
    private String userId;
    private String userName;
    private String userIcon;
    // 评论数
    private Integer commentCount=0;
    // 收藏数
//    private Integer collectCount;
    // 浏览数
    private Integer view=0;
    //赞同数
    private Integer up=0;
    //自己是否赞同
    private Boolean upSelf=false;

//    private Page<TopicCommentDetailVo> topicCommentDetailVo;
}
