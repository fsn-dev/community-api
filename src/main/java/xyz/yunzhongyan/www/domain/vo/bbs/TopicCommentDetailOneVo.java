package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

/**
 * @program: api
 * @description: 评论
 * @author: wander
 * @create: 2019-11-22 16:28
 **/
@Data
public class TopicCommentDetailOneVo {
    private String id;
    private String userName;
    private String userId;
    private String userIcon;
    private String content;
    private String contentMD;
    private String time;
    private Integer layer=0;//0:不缩进  1:缩进1格
    private String replyToUserName;
    private String replyToUserId;
    private Boolean edit=false;//是否可编辑
    private Boolean reply=false;//是否可回复
}
