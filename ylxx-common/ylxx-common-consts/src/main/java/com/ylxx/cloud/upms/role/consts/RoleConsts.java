package com.ylxx.cloud.upms.role.consts;

import com.ylxx.cloud.core.system.SystemConsts;

public class RoleConsts {

    /**
     * redis缓存key前缀
     */
    public static final String PREFIX = SystemConsts.PREFIX + "ROLE";

    /**
     * 最高角色等级（数值越小等级越高）
     */
    public static final int HIGHEST_LEVEL = 1; // 系统管理员

    /**
     * 角色编码
     */
    public static final String SYS_ADMIN = "SYS_ADMIN"; // 系统管理员
    public static final String SYS_YWADMIN = "SYS_YWADMIN"; // 系统运维人员
    public static final String SYS_PTADMIN = "SYS_PTADMIN"; // 平台管理人员
    public static final String SYS_YN = "SYS_YN"; // 烟农


}
