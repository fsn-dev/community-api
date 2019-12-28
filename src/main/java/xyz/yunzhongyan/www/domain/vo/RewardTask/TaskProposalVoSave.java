package xyz.yunzhongyan.www.domain.vo.RewardTask;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-15 21:38
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskProposalVoSave {
    private String id;//
    private String rewardTaskId;//悬赏任务id
    private String scheme;//方案
    private String accessory;//附件
}
