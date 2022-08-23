package com.ylxx.cloud.system.validate.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.service.impl.CommonCoreServiceImpl;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.dict.consts.DictConsts;
import com.ylxx.cloud.system.dict.util.DictCacheUtil;
import com.ylxx.cloud.system.runner.ValidateModelRunner;
import com.ylxx.cloud.system.validate.util.ValidateModelCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.validate.mapper.ValidateMapper;
import com.ylxx.cloud.system.validate.model.ValidateDTO;
import com.ylxx.cloud.system.validate.model.ValidateVO;
import com.ylxx.cloud.system.validate.service.IValidateService;
import com.ylxx.cloud.system.validate.util.ValidateCacheUtil;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author caixiaopeng
 * @Description 前后端一致校验规则表 服务实现类
 * @since 2020-07-05
 */
@Service
@Transactional
@Slf4j
public class ValidateServiceImpl implements IValidateService {

    @Resource
    private ValidateMapper validateMapper;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ValidateModelRunner validateModelRunner;

    @Override
    public Page<ValidateVO> selectPageVo(ValidateDTO validateDto) {
        Page<ValidateDTO> page = new Page<ValidateDTO>(validateDto.getCurrent(), validateDto.getSize());
        if (StrUtil.isNotBlank(validateDto.getOrders())) {
            page.addOrder(CommonCoreServiceImpl.parseOrders(validateDto.getOrders()));
        }
        return validateMapper.selectPageVo(page, validateDto);
    }

    @Override
    public List<ValidateVO> selectVos(ValidateDTO validateDto) {
        return validateMapper.selectPageVo(validateDto);
    }

    @Override
    public ValidateVO selectById(String id) {
        if (StrUtil.isNotBlank(id)) {
            ValidateDTO validateDto = new ValidateDTO();
            validateDto.setId(id);
            List<ValidateVO> vos = validateMapper.selectPageVo(validateDto);
            if (ObjectUtil.isNotEmpty(vos) && vos.size() == 1) {
                return vos.get(0);
            }
        }
        return null;
    }

    @Override
    public void insert(ValidateVO validateVo) {
        validateVo.setId(IdUtil.fastSimpleUUID());
        validateVo.setCreateTime(DateUtil.date());
        validateVo.setUpdateTime(DateUtil.date());
        validateVo.setCreateBy(HttpServletUtil.getUsername());
        validateVo.setUpdateBy(HttpServletUtil.getUsername());
        validateMapper.insert(validateVo);
    }

    @Override
    public void insertBatch(List<ValidateVO> validateVos) {
        if (ObjectUtil.isNotEmpty(validateVos)) {
            validateMapper.insertBatch(validateVos);
        }
    }

    @Override
    public void update(ValidateVO validateVo) {
        validateVo.setUpdateTime(DateUtil.date());
        validateVo.setUpdateBy(HttpServletUtil.getUsername());
        validateMapper.updateById(validateVo);
    }

    @Override
    public void deleteBatchIds(List<String> ids) {
        if (ObjectUtil.isNotEmpty(ids)) {
            validateMapper.deleteBatchIds(ids);
        }
    }

	@Override
	public void refreshCache() {
		List<ValidateVO> vos = validateMapper.selectPageVo(new ValidateDTO());
		log.info("将校验规则从redis中删除");
		ValidateCacheUtil.deleteCache();
		log.info("将校验规则缓存到redis中#start");
		vos.forEach(item -> {
			ValidateCacheUtil.setRegex(item.getValidatorName(), item.getType(), item.getRegex());
			ValidateCacheUtil.setRegexMessage(item.getValidatorName(), item.getType(), item.getMessage());
		});
		log.info("将校验规则缓存到redis中#end");
	}

    @Override
    public void refreshCacheModel() {
        log.info("将所有的前后端一致校验model从redis中删除");
        ValidateModelCacheUtil.deleteCache();
        log.info("刷新各个模块的前后端一致校验model#start");
        List<String> services = discoveryClient.getServices();
        if (ObjectUtil.isNotEmpty(services)) {
            // 多服务模式
            services.forEach(item -> {
                String url = StrUtil.format("http://{}/api/v1/common/refresh-validate-model", item);
                try {
                    restTemplate.getForObject(url, ApiResult.class, new Object());
                    log.info(StrUtil.format("调用{}成功", url));
                } catch (Exception e) {
                    throw new ActionException(StrUtil.format("调用{}失败", url), e);
                }
            });
        } else {
            // 独立服务模式
            validateModelRunner.run(null);
        }
        log.info("刷新各个模块的前后端一致校验model#end");
    }

}
