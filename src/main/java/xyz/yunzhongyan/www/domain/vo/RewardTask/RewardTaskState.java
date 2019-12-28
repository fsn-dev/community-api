package xyz.yunzhongyan.www.domain.vo.RewardTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-27 09:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardTaskState {
    private Integer id;
    private String name;
    private boolean flag=false;

    public RewardTaskState(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
