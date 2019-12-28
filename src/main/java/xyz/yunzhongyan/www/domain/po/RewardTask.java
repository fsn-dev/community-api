package xyz.yunzhongyan.www.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-15 18:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document(collection = "reward_task")
public class RewardTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;//标题
    private String bonus;//奖金
    private String scheme;//方案
    private List<String> accessory;//附件
    private Date deadline;//截止日期
    private Date creatDate;//发布日期
    private Integer state;//任务状态 0：报名中  1：进行中  2：已完成 3：过期
    private Integer participant;//参与者
    private String userId;
    private Boolean hoot = false;
}
