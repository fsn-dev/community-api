package xyz.yunzhongyan.www.controller;

import xyz.yunzhongyan.www.annotation.Authentication;
import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.vo.Authentication.*;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.yunzhongyan.www.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/authentications")
@CrossOrigin
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @ApiOperation(value = "测试使用，删除该邮箱的注册信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mailbox", value = "邮箱", required = true, dataType = "String", paramType = "query"),
    })
    @DeleteMapping("/user")
    public Result deleteUser(String mailbox) {
        return authenticationService.deleteUser(mailbox);
    }

    @ApiOperation(value = "查询用户名 邮箱是否可用", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "value", value = "查询是否重读", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "1：查询用户名是否重复  2：查询邮箱是否注册", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/duplicateCheck")
    public Result duplicateCheck(String value, Integer type) {
        return authenticationService.duplicateCheck(value, type);
    }


    @ApiOperation(value = "用户找回密码--确认修改密码", notes = "根据emailCode 和用户密码")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "emailCode", value = "邮件中的emailCode", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "password", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/forgotPassword")
    public Result forgotPassword(@RequestBody ForgotPasswordVo forgotPasswordVo) {
        log.info("forgotPassword确认修改密码" + forgotPasswordVo.getEmailCode() + forgotPasswordVo.getPassword());
        return authenticationService.forgotPassword(forgotPasswordVo);
    }

    @ApiOperation(value = "用户找回密码--发送邮件", notes = "发送邮件")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "mailbox", value = "邮箱", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/forgotPassword/email")
    public Result sendforGotPasswordEmail(@RequestBody MailboxVo mailboxVo) {
        log.info("/forgotPassword/email发送邮件" + mailboxVo.getMailbox());
        return authenticationService.sendforGotPasswordEmail(mailboxVo);
    }

    @ApiOperation(value = "用户注册--邮件确认--立即登录", notes = "根据邮箱确认接口返回的emailCode 进行登录")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "emailCode", value = "邮件中的emailCode", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/confirmEmail/login")
    public Result confirmEmaiLogin(@RequestBody EmailCode emailCode) {
        log.info("confirmEmail邮件确认" + emailCode.getEmailCode());
        return authenticationService.confirmEmailLogin(emailCode);
    }

    @ApiOperation(value = "用户注册--邮件确认", notes = "根据用用户名，邮箱，密码注册，发生邮件。")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "emailCode", value = "邮件中的emailCode", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/confirmEmail")
    public Result confirmEmai(@RequestBody EmailCode emailCode) {
        log.info("confirmEmail邮件确认" + emailCode.getEmailCode());
        return authenticationService.confirmEmai(emailCode);
    }

    @ApiOperation(value = "用户注册--重新发邮件", notes = "根据用户id 重发注册邮件")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "mailbox", value = "邮箱", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("/registerEmail")
    public Result sendRegisterEmail(@RequestBody RegisterEmail registerEmail) {
        log.info("registerEmail重新发邮件" + registerEmail.getUserId());
        return authenticationService.sendRegisterEmail(registerEmail);
    }

    @ApiOperation(value = "用户注册", notes = "根据用用户名，邮箱，密码注册，发生邮件。")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "mailbox", value = "邮箱", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping()
    public Result saveAuthentication(@RequestBody AuthenticationQuery authenticationQuery) {
        log.info("用户注册" + authenticationQuery.getUsername());
        return authenticationService.saveAuthentication(authenticationQuery);
    }


    @ApiOperation(value = "登录", notes = "根据用户名和密码获取用户认证，认证成功后返回token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping()
    public Result findAuthentication(HttpServletRequest request, String username, String password) {
        return authenticationService.findAuthentication(request, username, password);
    }

    @ApiOperation(value = "登录通过github", notes = "根据前端拿到的github授权code  后台完成登录 ，认证成功后返回token   参考文章（https://segmentfault.com/a/1190000018372841）可以使用： clientID = '1b537f502b9e647a9f82'\n" +
            " clientSecret = '8367b144f4783c60178b2f95d55c8beb02f76f40'")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/github")
    public Result findAuthenticationGithub(HttpServletRequest request, String code) {
        return authenticationService.findAuthenticationGithub(request, code);
    }


    @DeleteMapping
    @Authentication
    @ApiOperation(value = "退出登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result deleteAuthentication(@CurrentUser AuthenticatedUser authenticatedUser) {
        return authenticationService.deleteAuthentication(authenticatedUser);
    }

    @GetMapping("/info")
    @Authentication
    @ApiOperation(value = "获取个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result getUserInfo(@CurrentUser AuthenticatedUser authenticatedUser) {
        return authenticationService.getUserInfo(authenticatedUser);
    }

    @PostMapping("/info")
    @Authentication
    @ApiOperation(value = "修改个人资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    public Result updateUserInfo(@CurrentUser AuthenticatedUser authenticatedUser, @RequestBody User user) {
        return authenticationService.updateUserInfo(authenticatedUser,user);
    }
}
