package com.bit2025.mysite.security;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bit2025.mysite.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthUserHandlerMehtodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);

		// @AuthUser가 아닌 경우
		if (authUser == null) {
			return false;
		}

		// @AuthUser인데 파라미터 타입이 UserVo가 아닌 경우
		if (!parameter.getParameterType().equals(UserVo.class)) {
			return false;
		}

		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (supportsParameter(parameter)) {
			return WebArgumentResolver.UNRESOLVED;
		}

		// parameter : @AuthUser UserVo인 경우
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession();
		
		return session.getAttribute("authUser");
	}

}
