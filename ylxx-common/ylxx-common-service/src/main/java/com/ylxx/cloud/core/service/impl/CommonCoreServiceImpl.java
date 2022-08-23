package com.ylxx.cloud.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.ylxx.cloud.core.mapper.CommonCoreMapper;
import com.ylxx.cloud.core.service.ICommonCoreService;
import com.ylxx.cloud.exception.ext.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class CommonCoreServiceImpl implements ICommonCoreService {

    @Resource
    private CommonCoreMapper commonCoreMapper;

    @Override
    public Map<String, Object> now() {
        Map<String, Object> result = CollUtil.newHashMap();
        result.put("数据库时间", DateUtil.format(commonCoreMapper.now(), "yyyy-MM-dd HH:mm:ss"));
        result.put("服务器时间", DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    /**
     * 数据库查询排序
     * @param orders
     * @return
     */
    public static OrderItem[] parseOrders(String orders) {
        try {
            JSONArray orderArr = JSON.parseArray(orders);
            OrderItem[] orderItems = new OrderItem[orderArr.size()];
            for(int i=0; i<orderArr.size(); i++) {
                JSONObject orderObj = orderArr.getJSONObject(i);
                OrderItem order = new OrderItem();
                order.setColumn(orderObj.getString("column"));
                order.setAsc(orderObj.getBooleanValue("asc"));
                orderItems[i] = order;
            }
            return orderItems;
        } catch (Exception e) {
            throw new ServiceException("排序字段参数异常：orders="+orders, e);
        }
    }

}
