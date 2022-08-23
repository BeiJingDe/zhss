package com.ylxx.cloud.file.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;

/**
 * 文件管理表 服务类
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
public interface IFileService extends IFileOperateService {

	Page<FileVO> selectPageVo(FileDTO fileDto);

	List<FileVO> selectVos(FileDTO fileDto);

	FileVO selectById(String id);

	void insert(FileVO fileVo);
	
	void insertBatch(List<FileVO> fileVos);

	void update(FileVO fileVo);

}
