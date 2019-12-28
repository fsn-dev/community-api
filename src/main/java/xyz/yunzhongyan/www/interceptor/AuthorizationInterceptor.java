package xyz.yunzhongyan.www.interceptor;

import xyz.yunzhongyan.www.annotation.Authorization;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.service.RedisTokenService;
import xyz.yunzhongyan.www.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，判断此次请求是否有权限
 */
@Component
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTokenService tokenService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 如果未注明Authorization
        if (method.getAnnotation(Authorization.class) == null) return true;

        // 从header中得到token
        String token = request.getHeader(Constants.AUTHENTICATION);
        AuthenticatedUser authenticatedUser = tokenService.checkToken(token);

        //处理默认账号
        if (authenticatedUser==null && "76fcd84c-818f-4c60-9553-fb91c5969ee9".equals(token)){
            authenticatedUser=new AuthenticatedUser();
            authenticatedUser.setToken("76fcd84c-818f-4c60-9553-fb91c5969ee9");
            authenticatedUser.setUserId("5dcfbc5b8ef6d314f07a41e3");
        }

        // 验证该用户是否已登录
        if (authenticatedUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 验证该角色用户是否有权限进行该项操作
        Authorization authorizationAnnotation = method.getAnnotation(Authorization.class);
        /*if (!Arrays.stream(authorizationAnnotation.roleIds()).anyMatch(authenticatedUser.getRole().getId()::equals)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }*/

        //如果token验证成功，将token对应的用户存在request中，便于之后注入
        request.setAttribute(Constants.CURRENT_USER_ID, authenticatedUser);
        return true;
    }
}
