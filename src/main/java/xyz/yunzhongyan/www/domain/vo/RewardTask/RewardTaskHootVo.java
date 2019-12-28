package xyz.yunzhongyan.www.domain.vo.RewardTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-15 18:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardTaskHootVo {
    private String id;
    private String title;
    private String bonus;
    private String creater;
    private Integer participant;
    private String statusInformation;
}
