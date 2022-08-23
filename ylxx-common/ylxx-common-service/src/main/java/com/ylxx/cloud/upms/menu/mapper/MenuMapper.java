package com.ylxx.cloud.upms.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.menu.model.MenuDO;
import com.ylxx.cloud.upms.menu.model.MenuDTO;
import com.ylxx.cloud.upms.menu.model.MenuVO;
import com.ylxx.cloud.upms.menu.model.MenuVOTmp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单（按钮）信息表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface MenuMapper extends BaseMapper<MenuDO> {

	Page<MenuVO> selectPageVo(Page<MenuDTO> page, @Param("param") MenuDTO menuDto);

	List<MenuVO> selectPageVo(@Param("param") MenuDTO menuDto);

	void insertBatch(List<MenuVO> menuVos);

	List<MenuVOTmp> selectMenuTmp(@Param("username") String username);

	List<String> selectButtonTmp(@Param("username") String username);

	List<MenuVOTmp> selectMenuTmpByBusiCode(@Param("ids") List<String> ids);

	List<String> selectButtonTmpByBusiCode(@Param("ids") List<String> ids);
}
