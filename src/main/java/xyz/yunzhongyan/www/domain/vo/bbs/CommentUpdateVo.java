package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

@Data
public class CommentUpdateVo {

    //要修改的评论的id
    private String commentId;
    //内容
    private String content;
    private String contentMD;
}
