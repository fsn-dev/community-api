package xyz.yunzhongyan.www.domain.vo.RewardTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.yunzhongyan.www.domain.vo.Accessory;

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
public class RewardTaskDeatilVo {
    private String id;
    private String title;//标题
    private String bonus;//奖金
    private String scheme;//方案
    private List<Accessory> accessorys;//附件
    private Date deadline;//截止日期
    private String time;
    private Integer state;//任务状态 0：报名中  1：进行中  2：已完成
    private String statusInformation;
    private List<Participant> participants;
}
