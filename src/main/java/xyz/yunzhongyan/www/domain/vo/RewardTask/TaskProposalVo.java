package xyz.yunzhongyan.www.domain.vo.RewardTask;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import lombok.Data;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-27 10:21
 **/
@Data
public class TaskProposalVo {
    private String id;
    private String userName;
    private String email;
    private String time;
//    private String bonus;//奖金
    private String checkEnrollmentStatus;//报名状态
    private String checkEnrollmentStatusColor;//报名状态颜色
}
