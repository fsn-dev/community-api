package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

import java.util.List;

/**
 * @program: api
 * @description: 评论
 * @author: wander
 * @create: 2019-11-22 16:28
 **/
@Data
public class TopicCommentDetailVo {
    private String id;
    private List<TopicCommentDetailOneVo> topicCommentDetailOneVo;
}
