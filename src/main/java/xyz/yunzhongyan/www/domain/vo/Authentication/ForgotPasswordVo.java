package xyz.yunzhongyan.www.domain.vo.Authentication;/**
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
 * @create: 2019-11-12 14:00
 **/
@Data
public class ForgotPasswordVo {
    private String emailCode;
    private String password;
}
