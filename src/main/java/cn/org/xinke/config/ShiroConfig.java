package cn.org.xinke.config;

/*
 * @author yangyang
 * @date 2023/3/23 16:53
 * @概要：
 *      描述
 */
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro的内置过滤器
        /*anon:无需认证就可以访问
         * authc：必须认证了才能访问
         * user：必须有记住我功能才能使用
         * perms：拥有对某个资源的权限才能访问
         * role：拥有某个角色权限才能访问
         *
         * */

        Map<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        //授权，正常情况下，没有授权会跳到授权页面
   /*     filterChainDefinitionMap.put("/user/","perms[user:add]");
        filterChainDefinitionMap.put("/file/upload","perms[user:update]");;*/

        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/");
        //错误页面，认证不通过跳转
        //shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //跳转到未授权页面
        filterChainDefinitionMap.put("/auth", "anon");       // 允许匿名访问
        filterChainDefinitionMap.put("/logout", "logout");   //登出
        filterChainDefinitionMap.put("/assets/**/**", "anon");  // 允许匿名访问
        filterChainDefinitionMap.put("/share/**", "anon");  // 允许匿名访问
        filterChainDefinitionMap.put("/**", "authc");           // 进行身份认证后才能访问

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //关联userRealm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager defaultWebSessionManager=new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        return defaultWebSessionManager;
    }

    //创建realm 对象 ，需要自定义类
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    //整合shiro和thymeleaf模版
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

}