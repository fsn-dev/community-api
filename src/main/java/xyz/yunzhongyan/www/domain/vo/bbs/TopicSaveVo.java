package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;

@Data
public class TopicSaveVo {

    private String topicId;
    //模块id
    private String tagId;
    //标题
    private String title;
    //内容
    private String content;
    private String contentMD;
}
