package xyz.yunzhongyan.www.domain.vo.bbs;

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
public class TopicCommentVo {
    private String id;
    private String userName;
    private String userId;
    private String icon;
    private String content;
    private String contentMD;
    private String time;
}
