package com.ylxx.cloud.system.permissionurl.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.permission.consts.PermissionUrlConsts;
import com.ylxx.cloud.system.permission.util.PermissionUrlCacheUtil;
import com.ylxx.cloud.system.permissionurl.mapper.PermissionUrlMapper;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlDTO;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlVO;
import com.ylxx.cloud.system.permissionurl.service.IPermissionUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author caixiaopeng
 * @Description HTTP请求白名单 服务实现类
 * @since 2020-07-05
 */
@Service
@Transactional
@Slf4j
public class PermissionUrlServiceImpl implements IPermissionUrlService {

    @Resource
    private PermissionUrlMapper permissionUrlMapper;

    @Override
    public Page<PermissionUrlVO> selectPageVo(PermissionUrlDTO permissionUrlDto) {
        Page<PermissionUrlDTO> page = new Page<PermissionUrlDTO>(permissionUrlDto.getCurrent(), permissionUrlDto.getSize());
        if (StrUtil.isNotBlank(permissionUrlDto.getOrders())) {
            page.addOrder(CommonCoreServiceImpl.parseOrders(permissionUrlDto.getOrders()));
        }
        return permissionUrlMapper.selectPageVo(page, permissionUrlDto);
    }

    @Override
    public List<PermissionUrlVO> selectVos(PermissionUrlDTO permissionUrlDto) {
        return permissionUrlMapper.selectPageVo(permissionUrlDto);
    }

    @Override
    public PermissionUrlVO selectById(String id) {
        if (StrUtil.isNotBlank(id)) {
            PermissionUrlDTO permissionUrlDto = new PermissionUrlDTO();
            permissionUrlDto.setId(id);
            List<PermissionUrlVO> vos = permissionUrlMapper.selectPageVo(permissionUrlDto);
            if (ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
                return vos.get(0);
            }
        }
        return null;
    }

    @Override
    public void insert(PermissionUrlVO permissionUrlVo) {
        permissionUrlVo.setId(IdUtil.fastSimpleUUID());
        permissionUrlVo.setCreateTime(DateUtil.date());
        permissionUrlVo.setUpdateTime(DateUtil.date());
        permissionUrlVo.setCreateBy(HttpServletUtil.getUsername());
        permissionUrlVo.setUpdateBy(HttpServletUtil.getUsername());
        permissionUrlMapper.insert(permissionUrlVo);
    }

    @Override
    public void insertBatch(List<PermissionUrlVO> permissionUrlVos) {
        if (ObjectUtil.isNotEmpty(permissionUrlVos)) {
            permissionUrlMapper.insertBatch(permissionUrlVos);
        }
    }

    @Override
    public void update(PermissionUrlVO permissionUrlVo) {
        permissionUrlVo.setUpdateTime(DateUtil.date());
        permissionUrlVo.setUpdateBy(HttpServletUtil.getUsername());
        permissionUrlMapper.updateById(permissionUrlVo);
    }

    @Override
    public void deleteBatchIds(List<String> ids) {
        if (ObjectUtil.isNotEmpty(ids)) {
            permissionUrlMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void refreshCache() {
        List<PermissionUrlVO> vos = permissionUrlMapper.selectPageVo(new PermissionUrlDTO());
        log.info("将HTTP请求白名单从redis中删除");
        PermissionUrlCacheUtil.deleteCache();
        log.info("将HTTP请求白名单缓存到redis中#start");
        vos.forEach(item -> {
            // 状态有效的才加入白名单
            if (StrUtil.equals(PermissionUrlConsts.IS_ACTIVE_TYPE_1, item.getIsActive())) {
                String reqMethod = item.getReqMethod();
                if (StrUtil.isNotBlank(reqMethod)) {
                    reqMethod = reqMethod.toUpperCase();
                }
                PermissionUrlCacheUtil.addUrl(item.getAppName(), reqMethod, item.getReqUrl());
            }
        });
        log.info("将HTTP请求白名单缓存到redis中#end");
    }

}
