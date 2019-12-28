package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

@Data
public class CommentSaveVo {

    private String topicId;

    private String commentId;
    //内容
    private String content;
    private String contentMD;
}
