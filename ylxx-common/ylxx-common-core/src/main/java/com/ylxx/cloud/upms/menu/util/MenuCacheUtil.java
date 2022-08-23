package com.ylxx.cloud.upms.menu.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.menu.consts.MenuConsts;
import com.ylxx.cloud.upms.role.consts.RoleConsts;

import java.util.List;

/**
 * 菜单信息缓存管理
 */
public class MenuCacheUtil {

    public static void setMenuCodes(String username, List<String> menuCodes) {
        RedisUtil.set24Hour(MenuConsts.PREFIX + SystemConsts.CONNECTOR + username, CollUtil.join(menuCodes, ","));
    }

    public static List<String> getMenuCodes(String username) {
        String menuCodes = RedisUtil.get(MenuConsts.PREFIX + SystemConsts.CONNECTOR + username);
        if(StrUtil.isNotBlank(menuCodes)) {
            return CollUtil.newArrayList(menuCodes.split(","));
        }
        return null;
    }

}
