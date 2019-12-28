package xyz.yunzhongyan.www.domain.po;/**
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
import java.util.Date;
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
@org.springframework.data.mongodb.core.mapping.Document(collection = "task_proposal")
public class TaskProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String userId;
    private String rewardTaskId;
    private String scheme;
    private List<String> accessory;//附件
    private Date time;
    private Integer state;//0:审核中 1:进行中 2:已完成
}
