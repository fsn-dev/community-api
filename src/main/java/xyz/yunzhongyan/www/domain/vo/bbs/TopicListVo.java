package xyz.yunzhongyan.www.domain.vo.bbs;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @program: api
 * @description: 话题
 * @author: wander
 * @create: 2019-11-22 13:59
 **/
@Data
public class TopicListVo {
    private String tagName;
    private String tagid;
    private Page<TopicVo> topicVos;
}
