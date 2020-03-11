package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected HttpSession session;

	/**
	 * 公共的获取当前登录用户所属企业id
	 */
	public String getLoginCompanyId() {
		Object object = session.getAttribute("loginUser");
		if(object != null) {
			User user = (User) object;
			return user.getCompanyId();
		}
		return "1"; //模拟企业id=1 ,之后会改正
	}


	/**
	 * 公共的获取当前登录用户所属企业名称
	 */
	public String getLoginCompanyName() {
		Object object = session.getAttribute("loginUser");
		if(object != null) {
			User user = (User) object;
			return user.getCompanyName();
		}
		return "传智播客"; //模拟企业name=传智播客 ,之后会改正
	}

	/**
	 * 获取当前登录用户
	 */
	protected User getLoginUser() {
		Object object = session.getAttribute("loginUser");
		if(object != null) {
			return (User) object;
		}
		return null;
	}
}
