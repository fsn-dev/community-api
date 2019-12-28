package xyz.yunzhongyan.www.resolvers;

import xyz.yunzhongyan.www.annotation.CurrentUser;
import xyz.yunzhongyan.www.util.Constants;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 增加方法注入，将含有CurrentUser注解的方法参数注入当前登录用户
 */
@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果参数类型是AuthenticatedUser并且有CurrentUser注解则支持
        if (parameter.getParameterType().isAssignableFrom(AuthenticatedUser.class) &&
                parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) webRequest.getAttribute(Constants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST);
        if (authenticatedUser != null) return authenticatedUser;
        throw new MissingServletRequestPartException(Constants.CURRENT_USER_ID);
    }
}
