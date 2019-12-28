package xyz.yunzhongyan.www.domain.vo.RewardTask;

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
public class RewardTaskVoSave {
    private String id;
    private String title;
    private String bonus;
    private String scheme;
    private String accessory;
    private String deadline;
}
