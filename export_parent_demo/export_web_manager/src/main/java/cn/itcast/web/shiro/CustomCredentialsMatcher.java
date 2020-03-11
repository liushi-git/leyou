package cn.itcast.web.shiro;

import cn.itcast.common.utils.Encrypt;
import cn.itcast.domain.system.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 密码比较器
 *  重写里面的密码比较方法
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{
    /**
     * 密码比较
     *      参数：
     *      token：用户输入的用户名和密码
     *      info：认证数据 （安全数据（用户对象），用户登陆输入的密码，realm域的名称）
     *      返回值：
     *          true：密码一致 登陆成功
     *          false：密码不一致 自动的抛出异常
     */
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.获取用户输入的密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String password = (String) info.getCredentials();
        //2.对用户输入的密码进行加密
        password = Encrypt.md5(password,upToken.getUsername());
        //3.获取用户数据库中的密码
        PrincipalCollection principals = info.getPrincipals();//得到一个安全数据的集合（多个安全数据）
        User user = (User) principals.getPrimaryPrincipal();
        String dbPassword = user.getPassword();
        //4.比较两个密码
        return password.equals(dbPassword);
    }
}
