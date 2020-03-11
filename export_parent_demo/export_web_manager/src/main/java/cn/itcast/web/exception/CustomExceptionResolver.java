package cn.itcast.web.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常处理器
 *   实现接口
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {


	/**
	 * 处理异常
	 *  1.跳转一个经过美化的页面
	 *  2.携带一些错误信息
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		//判断当前异常是否属于权限不足异常
		if(ex instanceof UnauthorizedException){
			//抛出权限不足异常，跳转到未授权页面
			ModelAndView mv = new ModelAndView();
			mv.setViewName("forward:/unauthorized.jsp");
			return mv;
		}else {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("error");
			mv.addObject("errorMsg","对不起,我错了");
			mv.addObject("ex",ex);
			return mv;
		}

	}
}
