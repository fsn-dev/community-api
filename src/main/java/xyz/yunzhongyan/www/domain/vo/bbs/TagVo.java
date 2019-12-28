package xyz.yunzhongyan.www.domain.vo.bbs;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: api
 * @description: 标签、模块
 * @author: wander
 * @create: 2019-11-22 14:11
 **/
@Data
public class TagVo {
    private String id;
    private String title;
    private String description;
    private String icon;

    // 当前标签下的话题个数
    private Integer topicCount;

    // 评论数
    private Integer commentCount;

    // 浏览数
    private Integer view;

    private TagTopicVo tagTopicVo;
}
