package com.ylxx.cloud.upms.menu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.upms.menu.mapper.MenuMapper;
import com.ylxx.cloud.upms.menu.model.MenuDTO;
import com.ylxx.cloud.upms.menu.model.MenuVO;
import com.ylxx.cloud.upms.menu.model.MenuVOTmp;
import com.ylxx.cloud.upms.menu.model.Meta;
import com.ylxx.cloud.upms.menu.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author caixiaopeng
 * @Description 菜单（按钮）信息表 服务实现类
 * @since 2020-08-26
 */
@Service
@Transactional
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Page<MenuVO> selectPageVo(MenuDTO menuDto) {
        Page<MenuDTO> page = new Page<MenuDTO>(menuDto.getCurrent(), menuDto.getSize());
        if (StrUtil.isNotBlank(menuDto.getOrders())) {
            page.addOrder(CommonCoreServiceImpl.parseOrders(menuDto.getOrders()));
        }
        return menuMapper.selectPageVo(page, menuDto);
    }

    @Override
    public List<MenuVO> selectVos(MenuDTO menuDto) {
        return menuMapper.selectPageVo(menuDto);
    }

    @Override
    public MenuVO selectById(String id) {
        if (StrUtil.isNotBlank(id)) {
            MenuDTO menuDto = new MenuDTO();
            menuDto.setId(id);
            List<MenuVO> vos = menuMapper.selectPageVo(menuDto);
            if (ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
                return vos.get(0);
            }
        }
        return null;
    }

    @Override
    public void insert(MenuVO menuVo) {
        menuVo.setId(IdUtil.fastSimpleUUID());
        menuVo.setCreateTime(DateUtil.date());
        menuVo.setUpdateTime(DateUtil.date());
        menuVo.setCreateBy(HttpServletUtil.getUsername());
        menuVo.setUpdateBy(HttpServletUtil.getUsername());

        menuMapper.insert(menuVo);
    }

    @Override
    public void insertBatch(List<MenuVO> menuVos) {
        if (ObjectUtil.isNotEmpty(menuVos)) {
            menuVos.forEach(item -> {
                item.setId(IdUtil.fastSimpleUUID());
            });
            menuMapper.insertBatch(menuVos);
        }
    }

    @Override
    public void update(MenuVO menuVo) {
        menuVo.setUpdateTime(DateUtil.date());
        menuVo.setUpdateBy(HttpServletUtil.getUsername());

        menuMapper.updateById(menuVo);
    }

    @Override
    public void deleteBatchIds(List<String> ids) {
        if (ObjectUtil.isNotEmpty(ids)) {
            menuMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public List<MenuVO> selectMenuVos(String username) {
        if (StrUtil.isNotEmpty(username)) {
            MenuDTO menuDto = new MenuDTO();
            menuDto.setCurrent(1);
            menuDto.setSize(SystemConsts.MAX_PAGE_SIZE);
            menuDto.setOrders("[{column:'T1.sort_no',asc:true}]");
            menuDto.setUsername(username);
            Page<MenuVO> result = this.selectPageVo(menuDto);
            return result.getRecords();
        }
        return null;
    }

    @Override
    public List<MenuVOTmp> selectMenuTmp(List<MenuVOTmp> menuVOTmpList) {
        //List<MenuVOTmp> menuVOTmpList = menuMapper.selectMenuTmp(username);
        if (menuVOTmpList == null || menuVOTmpList.isEmpty()) {
            menuVOTmpList = menuMapper.selectMenuTmp("default");
        }

        menuVOTmpList.forEach(one -> {
            Meta meta = new Meta();
            String title = one.getTitle();
            meta.setTitle(title);
            meta.setIcon(one.getIcon());
            if ("每周数说".equalsIgnoreCase(title)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "weekly");
                meta.setParams(jsonObject.toJSONString());
            }
            if ("每月数说".equalsIgnoreCase(title)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "monthly");
                meta.setParams(jsonObject.toJSONString());
            }
            if ("经济分析数说".equalsIgnoreCase(title)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "economic");
                meta.setParams(jsonObject.toJSONString());
            }
            one.setMeta(meta);
        });

        Map<String, List<MenuVOTmp>> menuMap = menuVOTmpList.stream().collect(
                Collectors.groupingBy(MenuVOTmp::getParentId));

        List<MenuVOTmp> parentMenu = menuMap.get("0");

        List<MenuVOTmp> menuVOTmpListRes = new ArrayList<>();
        menuVOTmpListRes.addAll(parentMenu);

        Set<String> keySet = menuMap.keySet();
        for (String key : keySet) {
            if (!"0".equalsIgnoreCase(key)) {
                MenuVOTmp menuVOTmp = menuVOTmpListRes.stream().
                        filter(one -> key.equalsIgnoreCase(one.getId()))
                        .findFirst().orElse(null);
                menuVOTmp.setChildren(menuMap.get(key));
            }
        }
        return menuVOTmpListRes;
    }

    @Override
    public List<MenuVO> selectMenuVos() {
        MenuDTO menuDto = new MenuDTO();
        menuDto.setCurrent(1);
        menuDto.setSize(SystemConsts.MAX_PAGE_SIZE);
        menuDto.setOrders("[{column:'T1.sort_no',asc:true}]");
        Page<MenuVO> result = this.selectPageVo(menuDto);
        return result.getRecords();
    }

}
