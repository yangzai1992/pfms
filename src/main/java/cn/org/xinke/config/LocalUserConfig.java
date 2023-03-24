package cn.org.xinke.config;

import cn.org.xinke.entity.User;
import cn.org.xinke.util.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/*
 * @author yangyang
 * @date 2023/3/23 21:37
 * @概要：
 *      本地配置文件用户权限
 */
public class LocalUserConfig {

    private static final String ROLES = "roles";
    private static final String PERMS_PREFIX = "perms.";
    private static final String USER_PREFIX = "user.";
    private static final String ALLOC_PREFIX = "alloc.";

    private static final String SPLIT_SEPARATOR = ",";

    private static Map<String,User> userHashMap = new HashMap<>();
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("user.properties");

    static {
        //初始化角色与权限
        String roles = propertiesLoader.getProperty(ROLES);
        HashMap<String,List<String>> rolePermsMap = new HashMap<>();
        Set<String> rolesSet = new HashSet<>();
        if(StringUtils.isNotBlank(roles)){
            rolesSet.addAll(Arrays.asList(roles.split(SPLIT_SEPARATOR)));
            for (String role : rolesSet) {
                String rolePerms = propertiesLoader.getProperty(PERMS_PREFIX + role);
                if(StringUtils.isNotBlank(rolePerms)){
                    rolePermsMap.put(role,Arrays.asList(rolePerms.split(SPLIT_SEPARATOR)));
                }else{
                    rolePermsMap.put(role,new ArrayList<>());
                }
            }
        }

        //初始化用户并分配角色
        Properties properties = propertiesLoader.getProperties();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            String name = (String) key;
            //如果是用户配置
            if(name.startsWith(USER_PREFIX)){
                String password = propertiesLoader.getProperty(name);
                String account = name.substring(USER_PREFIX.length());
                String roleOrPerms = propertiesLoader.getProperty(ALLOC_PREFIX + account);
                if(StringUtils.isNotBlank(roleOrPerms)){
                    Set<String> permsList = new HashSet<>();
                    Set<String> roleList = new HashSet<>();
                    for (String roleOrPerm : roleOrPerms.split(SPLIT_SEPARATOR)) {
                        List<String> permsMap = rolePermsMap.get(roleOrPerm);
                        //判断是否为角色
                        if(rolesSet.contains(roleOrPerm)){
                            //添加角色
                            roleList.add(roleOrPerm);
                            if(permsMap != null && !permsMap.isEmpty()){
                                permsList.addAll(permsMap);
                            }
                        }else{
                            //认为是权限标记
                            permsList.add(roleOrPerm);
                        }
                    }
                    userHashMap.put(account,new User(account,password,roleList,permsList));
                }else{
                    userHashMap.put(account,new User(account,password));
                }
            }
        }
    }

    public static User getUser(String name){
        return userHashMap.get(name);
    }
}
