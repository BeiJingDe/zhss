package com.ylxx.cloud.upms.login.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sgcc.isc.core.orm.complex.FunctionNode;
import com.sgcc.isc.core.orm.complex.FunctionTree;
import com.sgcc.isc.core.orm.identity.Department;
import com.sgcc.isc.core.orm.resource.Function;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IIdentityService;
import com.sgcc.isc.service.adapter.helper.IResourceService;
import com.sgcc.isc.ualogin.client.IscServiceTicketValidator;
import com.sgcc.isc.ualogin.client.util.IscSSOResourceUtil;
import com.sgcc.isc.ualogin.client.vo.IscSSOUserBean;
import com.ylxx.cloud.business.base.util.BusinessConfigCacheValue;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.enums.ReportOperateEnum;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.isc.util.IscCacheUtil;
import com.ylxx.cloud.upms.login.mapper.LoginMapper;
import com.ylxx.cloud.upms.login.model.BoardModuleEnum;
import com.ylxx.cloud.upms.login.model.IscRule;
import com.ylxx.cloud.upms.login.model.UserIscInfo;
import com.ylxx.cloud.upms.login.service.ILoginService;
import com.ylxx.cloud.upms.menu.mapper.MenuMapper;
import com.ylxx.cloud.upms.menu.model.MenuVOTmp;
import com.ylxx.cloud.upms.menu.service.IMenuService;
import com.ylxx.cloud.upms.menu.util.MenuCacheUtil;
import com.ylxx.cloud.upms.org.model.OrgVO;
import com.ylxx.cloud.upms.org.service.IOrgService;
import com.ylxx.cloud.upms.org.util.OrgCacheUtil;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.ylxx.cloud.upms.role.service.IRoleService;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import com.ylxx.cloud.upms.user.consts.UserConsts;
import com.ylxx.cloud.upms.user.mapper.UserMapper;
import com.ylxx.cloud.upms.user.model.BaseOrg;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.service.IUserService;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
import com.ylxx.cloud.util.SM4Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Resource
    private LoginMapper loginMapper;
    @Resource
    private IUserService userService;
    @Resource
    private IRoleService roleService;
    @Resource
    private IMenuService menuService;
    @Resource
    private IOrgService orgService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserVO getUserByMmpd(String username, String mmpd) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(mmpd)) {
            throw new ServiceException("获取用户信息失败：用户名密码不能为空");
        }
        /*String sm4Key = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_SYSTEM, ConfigConsts.SYSTEM_KEY_SM4_KEY, "00112233445566778899AABBCCDDEEFF");
        String rsaPrivateKey = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_SYSTEM, ConfigConsts.SYSTEM_KEY_RSA_PRIVATE_KEY, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJWDtEUMswYzg73x3XNYojlFBqeNyLeJjiqWmCwfvp9Sfcadm2qNtGTfuUJAO/oCGA81dE+m3LzX6D3T39zjLPJIpo8JwNm/sp0JEHWZXGSr7TPgS0rH0ShNcqcVIMFpiLayt1qXW0zeAY6w1MM3nwLNg9ecMY9fv4QdLUybhpUNAgMBAAECgYADHTQjAAm2hlonGaOIJH90e+iQktZsZDQO1QYkXSDmRpySB3RWOs9YDoTYSWg2Y9bGclGS2yHb2Q9gQkp2WlD0TpNOXkh7kMY7TY8UtFdy1L4SfSSZj8vbhpgbt5XGHFem9k9FhZWWH42N/2TkaHn6pIeoBYtFwC3Kz3fG8GVxDQJBAPIKZEqi9k1UHQAHHlkI947S7Ne/BkWM2Bi7TcVFLx5PevmAuZK/RYLc0TauM4ULVv+T+2D/vG0FZXxzxiHHA88CQQCeIzXjh1FM5eTJYvA1m2lTslVvHmvRtSfMgmcPb/osIquyCvZg3JltlVoWZZ0huAhbSldpRpYZQnepzeQ3SSRjAkEAtnVW/Vyzx79szSSQoBW+aRBgMpNyKuPt01b/XYK17meEEKoVOqatw+SqYkoX1GekAa6QhaLDbHvw8UxlQ/k+yQJAP7bGDo9FtWrm3DENwTuFm2A9xdCG9m47w81CgtjF/iRV8x0xmWMoOhuzuVRlk+FXFJkGUPdMoI7EMdkvuwqwKwJAG2WoZrSNTKj4ZSG6ULpPi2tsj9Fg6RKE3Ed/ABbNgtfKce2aYopzXfscsCUpbniRURVFYaGth1fAdWEIYXGHJw==");
        try {
            // RSA解密，SM4加密
            mmpd = SM4Util.encrypt(sm4Key, RSAUtil.decrypt(mmpd, rsaPrivateKey));
        } catch (Exception e) {
            throw new ServiceException("获取用户信息失败：密码加解密失败", e);
        }*/
        //mmpd = MD5Utils.getMd5(RSAUtil.decrypt(mmpd, rsaPrivateKey));
        UserVO user = userService.selectUser(username, mmpd);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("获取用户信息失败：用户名或密码错误");
        }
        if (!StrUtil.equals(UserConsts.STATUS_TYPE_1, user.getStatus())) {
            throw new ServiceException("该用户账号状态异常！");
        }
        return user;
    }

    @Override
    public UserVO getUserByTicket(String ticket) {
        if (StrUtil.isBlank(ticket)) {
            throw new ServiceException("获取用户信息失败：ticket不能为空");
        }
        // 获取isc用户
        IscSSOUserBean iscUser = this.getIscUser(ticket);
        log.info("########## 获取到的isc用户信息：{}", JSON.toJSONString(iscUser));

        // 转换成本地用户
        UserVO userVo = userService.selectUser(iscUser.getIscUserSourceId());
        if (ObjectUtil.isNull(userVo)) {
            String defaultMmpd = SM4Util.encrypt(ConfigCacheValue.SYSTEM_KEY_SM4_KEY(), ConfigCacheValue.SYSTEM_KEY_DEFAULT_MMPD());

            userVo = new UserVO();
            userVo.setId(iscUser.getIscUserId());
            userVo.setUsername(iscUser.getIscUserSourceId());
            userVo.setRealname(iscUser.getIscAdCode());
            userVo.setMmpd(defaultMmpd);
            userVo.setStatus(UserConsts.STATUS_TYPE_1);
            userVo.setCreateTime(DateUtil.date());
            userVo.setUpdateTime(DateUtil.date());
            userMapper.insert(userVo);

            try {
                this.getSubDepartment(iscUser.getIscUserId(),iscUser.getBaseOrgId());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return userVo;
    }

    /**
     * 返回数据：
     * 用户信息：user
     * 角色信息：roles
     * 菜单信息：menus
     * 组织信息：orgs
     * 首页看板管理：board
     */
    @Override
    public Map<String, Object> getInfo() {
        // 权限相关信息
        Map<String, Object> result = CollUtil.newHashMap();
        String username = HttpServletUtil.getUsername();
        if (StrUtil.isNotBlank(username)) {
            UserVO user = userService.selectUser(username);
            result.put("user", user);
            List<RoleVO> roles = roleService.selectRoleVos(username);
            result.put("roles", roles);

            List<OrgVO> orgs = new ArrayList<>();
            //List<OrgVO> orgs = orgService.selectOrgVos(username);
            try {
                OrgVO org = userMapper.selectOrgVos(username);
                orgs.add(org);
                if(org != null){
                    orgs = queryOrg(org.getParentId(),orgs);
                }else{
                    orgs = orgService.selectOrgVos(username);
                }
                result.put("orgs", orgs);
            }catch (Exception e){
                e.printStackTrace();
            }
            // List<MenuVO> menus = menuService.selectMenuVos(username);
            List<MenuVOTmp> menus = new ArrayList<>();
            List<String> buttons = new ArrayList<>();
            String iscRule = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_ISC, "ISCRule");
            if (ConfigConsts.VALUE_YES_1.equals(iscRule)) {
                log.info("==> 生效权限：isc权限");

                UserIscInfo userIscInfo = this.getMenuByUserId(user.getId());
                menus = userIscInfo.getMenus();
                buttons = userIscInfo.getButtons();
            } else {
                log.info("==> 生效权限：系统权限");
                menus = menuMapper.selectMenuTmp(username);
                buttons = menuMapper.selectButtonTmp(username);
            }

            menus = menuService.selectMenuTmp(menus);

            result.put("menus", menus);
            result.put("buttons", buttons);
            //用户信息返回看板
            List<String> codeList = new ArrayList<>();
            BoardModuleEnum[] codes = BoardModuleEnum.values();
            for (BoardModuleEnum reportOperateEnum : codes) {
                codeList.add(reportOperateEnum.getCode());
            }
            codeList.removeAll(loginMapper.selectBoardModule(username));
            result.put("board", codeList);

            // 用户权限信息缓存到redis
            UserCacheUtil.setUser(username, user);
            user.setMmpd("");
            RoleCacheUtil.setRoles(username, roles);
            MenuCacheUtil.setMenuCodes(username, menus.stream().map(item -> item.getName()).collect(Collectors.toList()));
            OrgCacheUtil.setOrgCodes(username, orgs.stream().map(item -> item.getOrgCode()).collect(Collectors.toList()));
        }
        // 用户权限限制：如果关闭了权限验证，要能查看所有菜单
        if (StrUtil.equals(ConfigConsts.VALUE_NO_0, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
            /*List<MenuVO> menus = menuService.selectMenuVos();
            result.put("menus", menus);*/
        }
        // 系统配置相关信息
        result.put("isDeployProd", ConfigCacheValue.SYSTEM_KEY_IS_DEPLOY_PROD());
        result.put("terminalWarrantyDesc", BusinessConfigCacheValue.CTMS_KEY_TERMINAL_WARRANTY_DESC());
        result.put("elecToastSingleCost", BusinessConfigCacheValue.CTMS_KEY_ELEC_TOAST_SINGLE_COST());
        result.put("carbonToastSingleCost", BusinessConfigCacheValue.CTMS_KEY_CARBON_TOAST_SINGLE_COST());
        result.put("biomassToastSingleCost", BusinessConfigCacheValue.CTMS_KEY_BIOMASS_TOAST_SINGLE_COST());
        result.put("reduceEmissionsCO2", BusinessConfigCacheValue.CTMS_KEY_REDUCE_EMISSIONS_CO2());
        result.put("reduceEmissionsSO2", BusinessConfigCacheValue.CTMS_KEY_REDUCE_EMISSIONS_SO2());
        return result;
    }

    public List<OrgVO> queryOrg(String id,List<OrgVO> orgs) {
        OrgVO org =   userMapper.selectOrgVo(id);
        if(org != null){
            orgs.add(org);
            queryOrg(org.getParentId(),orgs);
        }
       return  orgs;
    }


    /**
     * 根据isc登录成功后的ticiet获取isc用户
     *
     * @param ticket isc登录成功后的ticket
     * @return 成功返回isc对象, 否则返回null
     */
    private IscSSOUserBean getIscUser(String ticket) {
        try {
            String iscService = ConfigCacheValue.ISC_KEY_SERVICE();
            String iscServiceValidateUrl = ConfigCacheValue.ISC_KEY_SERVICE_VALIDATE_URL();

            IscServiceTicketValidator sv = new IscServiceTicketValidator();
            sv.setServiceTicket(ticket);
            sv.setService(iscService);
            sv.setCasValidateUrl(iscServiceValidateUrl);
            log.info("iscService: {}", iscService);
            log.info("iscServiceValidateUrl: {}", iscServiceValidateUrl);
            log.info("ticket: {}", ticket);
            log.info("##### start sv.validate");
            sv.validate();
            if (!sv.isAuthenticationSuccesful()) {
                throw new ActionException("使用ticket验证isc登录失败");
            }
            // 获取当前用户登录信息
            IscSSOUserBean iscUser = IscSSOResourceUtil.transferIscUserBean(sv.getUser());
            return iscUser;
        } catch (Exception e) {
            throw new ActionException("获取isc用户失败", e);
        }
    }

    /**
     * 根据基准组织ID、基准组织属性获取当前基准组织下一级基准组织的信息（部门信息/单位信息/全部信息）
     */
    @Override
    public Department getSubDepartment(String userId, String orgId) {
        try {
            IIdentityService service = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
            //orgId 210000000030727 个人组织ID
            Department department = service.getDepartmentById(orgId);
            log.info("基准组织信息:"+department.getName());

            //获取基准组织 插入t_base_org表里面
            if(StrUtil.isNotEmpty(department.getId())){
                BaseOrg bo = userMapper.selectBaseOrg(department.getId());
                if(bo == null){
                    BaseOrg newBaseOrg = new BaseOrg();
                    newBaseOrg.setId(department.getId());
                    newBaseOrg.setOrgName(department.getName());
                    newBaseOrg.setOrgType(department.getNatureCode());
                    newBaseOrg.setParentId(department.getParentId());
                    newBaseOrg.setOrgCode(department.getCode());
                    newBaseOrg.setRemarks(department.getRemark());
                    newBaseOrg.setLocation(department.getPostalAddress());
                    newBaseOrg.setFax(department.getContactFax());
                    newBaseOrg.setPhone(department.getContactPhone());
                    userMapper.insertBaseOrg(newBaseOrg);
                }
                userMapper.insertBaseUserOrg(userId,orgId);
            }
            return department;
        } catch (Exception e) {
            throw new ActionException("获取isc用户组织信息失败", e);
        }
    }


    /**
     * 根据用户ID获取组织角色和功能
     *//*
    @Override
    public List<MenuVOTmp> getMenuByUserId(String userId) {

        List<MenuVOTmp> menuVOTmps = new ArrayList<>();
        //遍历获取组织角色
        IIdentityService identityService = AdapterFactory.getIdentityService();
        List<Map<String, Object>> permissionInfoByUserId = null;
        try {
            permissionInfoByUserId = identityService.getPermissionInfoByUserId(userId);

        for (int i = 0 ; i <permissionInfoByUserId.size();i++){
            HashMap map = (HashMap) permissionInfoByUserId.get(i);
            //”systemId”作为key值获取systemId
            //String systemId = (String) map.get("systemId");
            //通过key值” permission”获取该系统对应权限集
            List<Map<String, Object>> permList = (List<Map<String, Object>>) map.get("permission");
            //迭代
            for( int j = 0; j <permList.size() ; j++ ){
                HashMap hm = (HashMap) permList.get(j);
            //获取组织ID
                String s1 = (String) hm.get("orgId");
            //获取组织角色ID
                String s2 = (String) hm.get("orgRoleId");
                //遍历角色功能
                IResourceService resourceService = AdapterFactory.getResourceService();
                List<Function> funcs = null;
                    funcs = resourceService.getFuncsByOrgRoleId(s2, null, null);

                System.out.println("2121eqw"+funcs.get(0).getBusiCode());
                for(int g = 0 ; g <funcs.size();g++){
                    String busiCode = funcs.get(g).getBusiCode();
                    MenuVOTmp menuVOTmp = menuMapper.selectMenuTmpByBusiCode(busiCode);
                    if(menuVOTmp != null){
                        menuVOTmps.add(menuVOTmp);
                    }
                }

                //获取业务角色ID
                String s3 = (String) hm.get("spRoleId");
            //是否为缺省角色
                boolean idd = (boolean) hm.get("isdeflaultRole");
            //以上为获取到的全部权限数据获取以及KEY值。
            }
        }
        } catch (Exception e) {
            log.error("isc权限");
            e.printStackTrace();
        }

        return menuVOTmps;
    }*/


    /**
     * 根据用户ID获取该系统功能树
     */
    @Override
    public UserIscInfo getMenuByUserId(String userId) {

        UserIscInfo userIscInfo = new UserIscInfo();
        String systemId = ConfigCacheUtil.getConfig("ISC", "SYSTEMID");

        //遍历获取组织角色
        IResourceService resourceService = AdapterFactory.getResourceService();
        List<FunctionNode> funcNode = null;
        try {
            FunctionTree funcTree = resourceService.getFuncTree(userId, systemId, null);
            if (funcTree == null) {
                log.error("清空isc数据权限");
                for (ReportOperateEnum one : ReportOperateEnum.values()) {
                    IscCacheUtil.delPermission(one.getIscCode());
                }
                return userIscInfo;
            }
            funcNode = funcTree.getFuncNode();
            IscRule iscRule = new IscRule();

            //时间戳
            long beginTime = System.currentTimeMillis();

            this.getFuncNode(funcNode, iscRule);

            //时间戳
            long endTime = System.currentTimeMillis();
            System.out.println("==消耗总共时间=="+(endTime-beginTime)+"ms");

            List<String> menuIds = iscRule.getMenus();
            List<String> buttonIds = iscRule.getButtons();
            List<String> dpIds = iscRule.getDps();
            if (menuIds != null && !menuIds.isEmpty()) {
                userIscInfo.setMenus(menuMapper.selectMenuTmpByBusiCode(menuIds));
                //menuVOTmps = menuMapper.selectMenuTmpByBusiCode(busiCode);
            }
            if (buttonIds != null && !buttonIds.isEmpty()) {
                userIscInfo.setButtons(menuMapper.selectButtonTmpByBusiCode(buttonIds));
                //ids = menuMapper.selectButtonTmpByBusiCode(buttonIds);
            }

            if (dpIds != null && !dpIds.isEmpty()) {
                Map<String, List<String>> permissionMap = dpIds.stream().filter(one -> one.split("_").length == 3).collect(
                        Collectors.groupingBy(one -> one.split("_")[1], Collectors.mapping(one -> one.split("_")[2], Collectors.toList())));
                log.info("==> permissionMap,{}", permissionMap);
                permissionMap.forEach((k, v) -> {
                    IscCacheUtil.setPermission(k, v);
                });
            } else {
                for (ReportOperateEnum one : ReportOperateEnum.values()) {
                    IscCacheUtil.delPermission(one.getIscCode());
                }
            }

        } catch (Exception e) {
            log.error("isc权限" + e.getMessage());
            e.printStackTrace();
        }

        return userIscInfo;
    }
    /**
     * 根据用户ID获取该系统功能树
     */
/*    @Override
    public List<String> getButtonByUserId(String userId) {
        String systemId = ConfigCacheUtil.getConfig("ISC", "SYSTEMID");
        List<String> busiCode = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        //遍历获取组织角色
        IResourceService resourceService = AdapterFactory.getResourceService();
        List<FunctionNode> funcNode = null;
        try {
            funcNode = resourceService.getFuncTree(userId, systemId, null).getFuncNode();
            busiCode = this.getButtonFuncNode(funcNode, busiCode);
            if(busiCode != null && !busiCode.isEmpty()){
                ids = menuMapper.selectButtonTmpByBusiCode(busiCode);
            }

        } catch (Exception e) {
            log.error("isc权限"+e.getMessage());
            e.printStackTrace();
        }

        return ids;
    }*/


    /**
     * 递归功能树
     */
    @Override
    public void getFuncNode(List<FunctionNode> funcNode, IscRule iscRule) {
        try {
            for (int i = 0; i < funcNode.size(); i++) {
                //时间戳
                long beginTime = System.currentTimeMillis();

                Function currentNode = funcNode.get(i).getCurrentNode();
                String busiCode = currentNode.getBusiCode();
                if (busiCode.startsWith("button_")) {
                    iscRule.getButtons().add(busiCode);
                } else if (busiCode.startsWith("dp_")) {
                    iscRule.getDps().add(busiCode);
                } else {
                    iscRule.getMenus().add(busiCode);
                }
                List<FunctionNode> nextNode = funcNode.get(i).getNextNode();
                if (nextNode.size() != 0) {
                    this.getFuncNode(nextNode, iscRule);
                }

                //时间戳
                long endTime = System.currentTimeMillis();
                //System.out.println("==消耗递归时间第"+i+"次=="+(endTime-beginTime)+"ms");
            }
        } catch (Exception e) {
            log.error("isc权限" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 递归按钮功能树
     */
    /*public List<String> getButtonFuncNode(List<FunctionNode> funcNode,List<String> ids) {
        try {

            for (int i = 0 ; i <funcNode.size();i++){
                Function currentNode = funcNode.get(i).getCurrentNode();
                String busiCode = currentNode.getBusiCode();

                if("button".equals(busiCode.split("_")[0])){
                    busiCode.startsWith("button_");
                    ids.add(busiCode);
                }
                List<FunctionNode> nextNode = funcNode.get(i).getNextNode();
                //MenuVOTmp menuVOTmp = menuMapper.selectMenuTmpByBusiCode(busiCode);
                *//*if(menuVOTmp != null){
                    menuVOTmps.add(menuVOTmp);
                }*//*
                if(nextNode.size() !=0){
                    this.getButtonFuncNode(nextNode,ids);
                }

            }

        } catch (Exception e) {
            log.error("isc权限"+e.getMessage());
            e.printStackTrace();
        }
        return ids;
    }*/
    public static void main(String[] args) {
        List<String> dpIds = new ArrayList<>();
        dpIds.add("dp_re_all");
        dpIds.add("dp_re_bm001");
        dpIds.add("dp_re_bm002");
        dpIds.add("dp_rc_bm001");
        dpIds.add("dprc_bm001");
        Map<String, List<String>> permissionMap = dpIds.stream().filter(one -> one.split("_").length == 3).collect(
                Collectors.groupingBy(one -> one.split("_")[1], Collectors.mapping(one -> one.split("_")[2], Collectors.toList())));
        System.out.println(permissionMap);
    }

    @Override
    public ApiResult iscOrgUpdate() {
        try {
            IIdentityService service = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
            //删除部门
            userMapper.deleteBaseOrgDept();
            //拿出所有单位
            List<BaseOrg> baseOrgs = userMapper.selectBaseOrgCorp();
            //循环单位
            for (int i = 0; i<baseOrgs.size();i++){
                String id = baseOrgs.get(i).getId();

                /*List<Department> subDepartment = service.getSubDepartment(id, Constants.DEPARTMENT_PROPERTY_DEPARTMENT);

                for (int y = 0; y<subDepartment.size();y++){

                    if(StrUtil.isNotEmpty(subDepartment.get(y).getId())){
                        BaseOrg bo = userMapper.selectBaseOrg(subDepartment.get(y).getId());
                        if(bo == null){
                            this.iscOrgInsert(subDepartment.get(y));
                            List<Department> newSubDepartment = service.getSubDepartment(subDepartment.get(y).getId(), Constants.DEPARTMENT_PROPERTY_DEPARTMENT);
                            newSubDepartment.forEach(one -> {
                                if(StrUtil.isNotEmpty(one.getId())){
                                    BaseOrg bo2 = userMapper.selectBaseOrg(one.getId());
                                    if(bo2 == null){
                                        this.iscOrgInsert(one);
                                    }
                                }
                            });
                        }
                    }

                }*/
                this.iscOrgUpdate2(id,service);
            }
            return ApiResultBuilder.success();
        } catch (Exception e) {
            throw new ActionException("更新isc用户组织信息失败", e);
        }
    }

    public void iscOrgInsert(Department department) {
        if(department != null) {
            BaseOrg newBaseOrg = new BaseOrg();
            newBaseOrg.setId(department.getId());
            newBaseOrg.setOrgName(department.getName());
            newBaseOrg.setOrgType(department.getNatureCode());
            newBaseOrg.setParentId(department.getParentId());
            newBaseOrg.setOrgCode(department.getCode());
            newBaseOrg.setRemarks(department.getRemark());
            newBaseOrg.setLocation(department.getPostalAddress());
            newBaseOrg.setFax(department.getContactFax());
            newBaseOrg.setPhone(department.getContactPhone());
            userMapper.insertBaseOrg(newBaseOrg);
        }
    }

    public void iscOrgUpdate2(String id,IIdentityService service) {
        try {
            List<Department> subDepartment = service.getSubDepartment(id, Constants.DEPARTMENT_PROPERTY_DEPARTMENT);
            if(subDepartment!=null && subDepartment.size()>=0){
                for (int y = 0; y<subDepartment.size();y++){
                    if(StrUtil.isNotEmpty(subDepartment.get(y).getId())){
                        BaseOrg bo = userMapper.selectBaseOrg(subDepartment.get(y).getId());
                        if(bo == null){
                            this.iscOrgInsert(subDepartment.get(y));
                        /*List<Department> newSubDepartment = service.getSubDepartment(subDepartment.get(y).getId(), Constants.DEPARTMENT_PROPERTY_DEPARTMENT);
                        newSubDepartment.forEach(one -> {
                            if(StrUtil.isNotEmpty(one.getId())){
                                BaseOrg bo2 = userMapper.selectBaseOrg(one.getId());
                                if(bo2 == null){
                                    this.iscOrgInsert(one);
                                }
                            }
                        });*/
                            this.iscOrgUpdate2(subDepartment.get(y).getId(),service);
                        }
                    }
                }
            }


        } catch (Exception e) {
            throw new ActionException("更新isc用户组织信息失败", e);
        }
    }
}
