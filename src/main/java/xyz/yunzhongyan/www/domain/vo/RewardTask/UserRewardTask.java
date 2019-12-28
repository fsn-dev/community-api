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
 * @create: 2019-12-16 16:56
 **/
@Data
public class UserRewardTask {
    private String id;
    private String title;
    private String bonus;//奖金
    private String checkEnrollmentStatus;//报名状态
    private String checkEnrollmentStatusColor;//报名状态颜色
    private String taskStatus;//任务状态
    private String taskStatusColor;//任务状颜色
    private String registration;//报名信息 人数
    private String creatTime;
    private String scheme;//方案
    private Boolean hot;
}
