package xyz.yunzhongyan.www.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.yunzhongyan.www.annotation.Authentication;
import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.service.impl.IntegralServiceImpl;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-12-16 14:51
 **/
@Api("积分服务")
@RestController
@RequestMapping("/integral")
@Slf4j
@CrossOrigin
public class IntegralController {

    @Autowired
    private IntegralServiceImpl integralService;

    @ApiOperation(value = "用户中心--积分页面", notes = "")
    @GetMapping("")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result getUserIntegral(@CurrentUser AuthenticatedUser authenticatedUser) {
        return integralService.getUserIntegral(authenticatedUser);
    }

    @ApiOperation(value = "用户中心--签到", notes = "")
    @GetMapping("/signIn")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result getSignIn(@CurrentUser AuthenticatedUser authenticatedUser) {
        return integralService.getSignIn(authenticatedUser);
    }

    @ApiOperation(value = "按键状态", notes = "")
    @GetMapping("/button")
    @Authentication
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "type", value = "0:判断是否可以看后台管理   1：/判断立刻报名按钮是否可见(需要参数id：悬赏任务id)  2：是否签到按钮", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "可选参数，",  dataType = "int", paramType = "query"),
    })
    public Result getbutton(@CurrentUser AuthenticatedUser authenticatedUser, Integer type,String id) {
        return integralService.getbutton(authenticatedUser,type,id);
    }
}
