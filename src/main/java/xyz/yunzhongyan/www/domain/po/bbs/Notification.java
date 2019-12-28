package xyz.yunzhongyan.www.domain.po.bbs;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @program: api
 * @description: 通知信息
 * @author: wander
 * @create: 2019-11-22 14:14
 **/
@Data
@Document(collection = "notification")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Notification {

    @Id
    private String id;

    private String topicId;
    private String userId;
    // 通知对象ID
    private Integer targetUserId;
    // 动作: REPLY, COMMENT, COLLECT, TOPIC_UP, COMMENT_UP
    private String action;
    private Date inTime;
    private String content;
    // 是否已读
    private Boolean read;
}
