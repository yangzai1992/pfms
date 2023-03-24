package cn.org.xinke.entity;

import java.util.Set;

/**
 * @description 账户实体
 * @author cinco
 * @date 2019-1-21
 */
public class User {

    /**
     * 账户
     */
    private String uname;

    /**
     * 密码
     */
    private String pwd;
    /**
     * 角色
     */
    private Set<String> roleSet;
    /**
     * 权限
     */
    private Set<String> permsSet;


    public User() {
    }

    public User(String uname, String pwd) {
        this.uname = uname;
        this.pwd = pwd;
    }

    public User(String uname, String pwd, Set<String> roleSet, Set<String> permsSet) {
        this.uname = uname;
        this.pwd = pwd;
        this.roleSet = roleSet;
        this.permsSet = permsSet;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<String> roleSet) {
        this.roleSet = roleSet;
    }

    public Set<String> getPermsSet() {
        return permsSet;
    }

    public void setPermsSet(Set<String> permsSet) {
        this.permsSet = permsSet;
    }
}
