package cn.org.xinke.config;

import cn.org.xinke.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;


/*
 * @author yangyang
 * @date 2023/3/23 16:58
 * @概要：
 *      描述
 */
public class UserRealm extends AuthorizingRealm {

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==>授权doGetAuthorizationInfo");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //授权add页面
        info.addStringPermission("user:add");
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //拿到student对象
        User currentStudent = (User) subject.getPrincipal();
        //添加权限访问
        info.addStringPermissions(currentStudent.getPermsList());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //获取登录用户的令牌，名字
        UsernamePasswordToken authenticationToken1 = (UsernamePasswordToken) authenticationToken;
        //连接数据库真实数据：用户名 密码
        User user = LocalUserConfig.getUser(authenticationToken1.getUsername());
        //判断数据库查询出来的不为空，为空则返回报错
        if(user==null){
            return null;
        }
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginStudent",user);

        //密码认证，shiro自己判断
        return new SimpleAuthenticationInfo(user,user.getPwd(),"");
    }
}
