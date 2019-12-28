package xyz.yunzhongyan.www.domain.vo.RewardTask;

import lombok.Data;
import xyz.yunzhongyan.www.domain.vo.PagedQueryParam;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-27 09:18
 **/
@Data
public class RewardTaskQuery extends PagedQueryParam {
    private Integer state;
    private Integer hot;
    private String id;
}
