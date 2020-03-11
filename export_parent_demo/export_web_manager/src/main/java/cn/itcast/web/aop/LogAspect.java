package cn.itcast.web.aop;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 切面类
 *      声明切面类
 */
@Component
@Aspect
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;

    /**
     * 环绕通知
     */
        @Around("execution(* cn.itcast.web.controller.*.*.*(..))")
        public Object around(ProceedingJoinPoint pjp) throws Throwable {
            SysLog sysLog = new SysLog();
            Object object = session.getAttribute("login");
            if(object!=null){
                User user = (User) object;
                sysLog.setUserName(user.getUserName());
                sysLog.setCompanyId(user.getCompanyId());
                sysLog.setCompanyName(user.getCompanyName());
            }
            sysLog.setTime(new Date());
        sysLog.setIp(request.getLocalAddr());
        sysLogService.save(sysLog);
        Object obj = null;
        //1.增强
        obj = pjp.proceed();
        return obj;
    }
}
