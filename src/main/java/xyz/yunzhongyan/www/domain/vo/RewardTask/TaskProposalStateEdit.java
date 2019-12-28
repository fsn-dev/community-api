package xyz.yunzhongyan.www.domain.vo.RewardTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-27 09:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProposalStateEdit {
    private String id;
    private String scheme;
    private String taskStatus;//任务状态
    private String taskStatusColor;//任务状颜色
    private Integer taskStatusId;//状态id
    private List<RewardTaskState> rewardTaskStates;
}
