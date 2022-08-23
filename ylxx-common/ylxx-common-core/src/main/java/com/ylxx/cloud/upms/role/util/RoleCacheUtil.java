package com.ylxx.cloud.upms.role.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.role.consts.RoleConsts;
import com.ylxx.cloud.upms.role.model.RoleVO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息缓存管理
 */
public class RoleCacheUtil {

    // 缓存角色信息
    public static void setRoles(String username, List<RoleVO> roles) {
        if(ObjectUtil.isNotEmpty(roles)) {
            RedisUtil.set24Hour(RoleConsts.PREFIX + SystemConsts.CONNECTOR + username, JSON.toJSONString(roles));
        }
    }

    // 获取角色信息
    public static List<RoleVO> getRoles(String username) {
        String roles = RedisUtil.get(RoleConsts.PREFIX + SystemConsts.CONNECTOR + username);
        if(StrUtil.isNotBlank(roles) && JSONValidator.from(roles).validate()) {
            return JSONArray.parseArray(roles, RoleVO.class);
        }
        return null;
    }

    // 获取用户拥有的角色编码
    public static List<String> getRoleCodes(String username) {
        List<RoleVO> roles = getRoles(username);
        if(ObjectUtil.isNotEmpty(roles)) {
            return roles.stream().map(item -> item.getRoleCode()).collect(Collectors.toList());
        }
        return null;
    }

    // 获取用户的最高角色权限等级（数值越小等级越高）
    public static int getMinSortNo(String username) {
        int minSortNo = 99999999;
        List<RoleVO> roles = getRoles(username);
        if(ObjectUtil.isNotEmpty(roles)) {
            for (RoleVO role : roles) {
                Integer sortNo = role.getSortNo();
                if(ObjectUtil.isNotNull(sortNo) && sortNo < minSortNo) {
                    minSortNo = sortNo;
                }
            }
        }
        return minSortNo;
    }

}
