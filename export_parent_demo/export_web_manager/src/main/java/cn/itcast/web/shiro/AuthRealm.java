package cn.itcast.web.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm
 */
public class AuthRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;
    /**
     * 用户授权
     *  *查询当前用户所具有的所有权限
     *  *shiro内部会自动的根据所有权限以及配置的所需权限进行比对
     *  *获取当前用户可操作的所有权限，以模块名称构造数组
     *  参数：
     *      principalCollection 安全数据的集合（存放了所有的安全数据）
     *  返回值：
     *      AuthorizationInfo 权限数据
     *
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取当前登陆的用户
        User user = (User) principalCollection.getPrimaryPrincipal(); //获取唯一的安全数据
        //2.调用moduleService查询当前用户的所有权限
        List<Module> mouduleList = moduleService.findByUserId(user.getId());
        //3.构造返回值
        Set<String> stringPermissions = new HashSet<>(); //参访所有的模块名称
        for (Module module : mouduleList) {
            stringPermissions.add(module.getName());
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions); //将所有的模块名称存入info对象
        return info;
    }

    /**
     * 用户认证
     *      参数：authenticationToken，subject.login中的参数
     *       * 用户登陆输入的邮箱和密码
     *       返回值：AuthenticationInfo
     *          认证数据：
     *              如果返回值为null：自动的抛出异常（登陆失败）
     *          * 查询用户
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.将authenticationToken转化为email和password
        UsernamePasswordToken upToken= (UsernamePasswordToken) authenticationToken;
        String email = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.调用service根据邮箱查询用户
        User user = userService.findByEmail(email);
        //3.判断用户名是否存在
        if (user!=null){
            //3.1：如果用户存在：构造返回值
            //参数1.安全数据（用户对象）2.密码 3.自定义realm域的名称
            return new SimpleAuthenticationInfo(user,password,this.getName());
        }else {
            //3.2：如果用户不存在：返回null
            return null;
        }
    }
}
