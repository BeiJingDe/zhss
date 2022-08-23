package com.ylxx.cloud.upms.menu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.upms.menu.model.MenuDTO;
import com.ylxx.cloud.upms.menu.model.MenuVO;
import com.ylxx.cloud.upms.menu.model.MenuVOTmp;

import java.util.List;

/**
 * 菜单（按钮）信息表 服务类
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
public interface IMenuService {

	Page<MenuVO> selectPageVo(MenuDTO menuDto);

	List<MenuVO> selectVos(MenuDTO menuDto);

	MenuVO selectById(String id);

	void insert(MenuVO menuVo);
	
	void insertBatch(List<MenuVO> menuVos);

	void update(MenuVO menuVo);

	void deleteBatchIds(List<String> ids);

    List<MenuVO> selectMenuVos(String username);

	List<MenuVOTmp> selectMenuTmp(List<MenuVOTmp> menuVOTmpList);

    List<MenuVO> selectMenuVos();

}
