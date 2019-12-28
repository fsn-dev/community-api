package xyz.yunzhongyan.www.domain.po.bbs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @program: api
 * @description: 评论
 * @author: wander
 * @create: 2019-11-22 16:28
 **/
@Data
@Document(collection = "comment")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Comment {
    @Id
    private String id;
    private String topicId;
    private String userId;
    private String content;
    private String contentMD;
    private Date inTime;
    private String fistCommentId;//用于识别评论下面的评论
    private String commentId;
    // 点赞用户的id
    private String upIds;
}
