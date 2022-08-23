package com.ylxx.cloud.file.service;

import com.ylxx.cloud.file.model.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileOperateService {

    List<FileVO> upload(FileVO fileVo , MultipartFile[] file);

    void deleteBatchIds(List<String> ids);

    void download(String id);

}
