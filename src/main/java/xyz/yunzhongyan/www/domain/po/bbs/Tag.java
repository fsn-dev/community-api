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
 * @description: 标签、模块
 * @author: wander
 * @create: 2019-11-22 14:11
 **/
@Data
@Document(collection = "tag")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Tag {
    @Id
    private String id;
    private String title;
    private String description;
    private String icon;
    // 当前标签下的话题个数
    private Integer topicCount=0;
    // 浏览数
    private Integer view=0;
    // 评论数
    private Integer commentCount=0;

    private Date creatTime;
}
