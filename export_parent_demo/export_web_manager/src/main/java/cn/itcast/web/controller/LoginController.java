package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

//	@RequestMapping("/login")
//    public String login(String email,String password) {
//        //1.判断用户名和密码是否为空
//        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
//            return "forward:/login.jsp";
//        }
//        //2.根据邮箱查询用户
//        User user = userService.findByEmail(email);
//        //3.判断用户是否存在以及密码是否匹配
//        //对用户输入的密码进行加密
//        password = Encrypt.md5(password, email);
//        if(user!=null&&user.getPassword().equals(password)) {
//            //3.1用户存在且密码一致
//            //将用户数据保存到session
//            session.setAttribute("login",user);
//            //当用户登陆成功之后，调用moduleService查询所有模块
//            List<Module> modules=moduleService.findByUserId(user.getId());
//            session.setAttribute("modules",modules);
//            //跳转到主页
//            return "home/main";
//        }else {
//            //3.2不一致
//            //返回错误信息
//            request.setAttribute("error","用户名或密码错误");
//            //跳转到登陆页面并重新登陆
//            return "forward:/login.jsp";
//        }
//    }
    @RequestMapping("/login")
    public String login(String email,String password) {
        //1.判断用户名和密码是否为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
            return "forward:/login.jsp";
        }
        //2.获取subject 通过securityUtils工具类获取
        Subject subject = SecurityUtils.getSubject();
        //3.构造登陆的token对象 构造参数：1.用户名 2.密码
        UsernamePasswordToken token = new UsernamePasswordToken(email,password);
        //4.调用subject.login通过shiro进行登陆
        try {
            subject.login(token);
            //5.如果正常执行没有异常（登陆成功）
            //登陆成功之后，shiro自动将用户对象保存到内存中，从shiro中获取用户对象
            User user = (User) subject.getPrincipal(); //在shiro中表示安全数据（用户对象）
            //将用户数据保存到session
            session.setAttribute("loginUser",user);
            //当用户登陆成功之后，调用moduleService查询所有模块
            List<Module> modules=moduleService.findByUserId(user.getId());
            session.setAttribute("modules",modules);
            //跳转到主页
            return "home/main";
        }catch (Exception e){
            e.printStackTrace();
            //6.如果抛出异常（登陆失败）
            request.setAttribute("error","用户名或密码错误");
            //跳转到登陆页面并重新登陆
            return "forward:/login.jsp";
        }
    }

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
