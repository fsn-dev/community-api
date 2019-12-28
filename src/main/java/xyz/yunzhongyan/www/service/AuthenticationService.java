package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.vo.Authentication.*;
import xyz.yunzhongyan.www.domain.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */
public interface AuthenticationService {
    Result deleteUser(String mailbox);

    Result duplicateCheck(String value, Integer type);

    Result forgotPassword(ForgotPasswordVo forgotPasswordVo);

    Result sendforGotPasswordEmail(MailboxVo mailboxVo);

    Result confirmEmai(EmailCode emailCode);

    Result confirmEmailLogin(EmailCode emailCode);

    Result sendRegisterEmail(RegisterEmail registerEmail);

    Result saveAuthentication(AuthenticationQuery authenticationQuery);

    Result findAuthentication(HttpServletRequest request, String username, String password);

    Result findAuthenticationGithub(HttpServletRequest request, String code);

    Result deleteAuthentication(AuthenticatedUser authenticatedUser);

    Result getUserInfo(AuthenticatedUser authenticatedUser);

    Result updateUserInfo(AuthenticatedUser authenticatedUser, User user);
}
