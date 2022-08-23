package com.ylxx.cloud.upms.login.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户登录 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-11-02
 */
public interface LoginMapper {

    List<String> selectBoardModule(@Param("userName") String userName);
}
