package xyz.yunzhongyan.www.domain.po.bbs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
@Document(collection = "topic")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Topic {
    @Id
    private String id;
    private String title;
    private String content;
    private String contentMD;
    private Date creatTime;
    private Date modifyTime;
    //作者
    private String userId;
    // 评论数
    private Integer commentCount=0;
    // 收藏数
    private Integer collectCount=0;
    // 浏览数
    private Integer view=0;
    // 置顶
    private Boolean top;
    // 加精
    private Boolean good;
    // 点赞用户的id英文,隔开的，要计算被多少人点赞过，可以通过英文,分隔这个字符串计算数量
    private String upIds="";
    //模块id
    private String tagId;
    //热度
    private Float hootValue=0f;
}
