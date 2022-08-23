package com.ylxx.cloud.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.file.model.FileDO;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;

/**
 * 文件管理表 Mapper 接口
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
public interface FileMapper extends BaseMapper<FileDO> {
	
	Page<FileVO> selectPageVo(Page<FileDTO> page, @Param("param") FileDTO fileDto);

	List<FileVO> selectPageVo(@Param("param") FileDTO fileDto);

	void insertBatch(List<FileVO> fileVos);

    void updateDownloadCount(String id);

}
