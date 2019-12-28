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

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-16 16:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document(collection = "user_integral_record")
public class UserIntegralRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String userId;
    private Date time;
    private Integer count;
    private Integer type;
}
