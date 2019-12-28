package xyz.yunzhongyan.www.domain.vo;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import lombok.Data;

import java.util.List;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-16 16:02
 **/
@Data
public class UserIntergralVo {
    private Integer intergral;
    private List<UserIntergral2Vo> userIntergral2Vos;
}
