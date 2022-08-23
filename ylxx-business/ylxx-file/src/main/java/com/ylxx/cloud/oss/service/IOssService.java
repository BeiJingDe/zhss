package com.ylxx.cloud.oss.service;

import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileOperateService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOssService extends IFileOperateService {

    String getViewUrl(String id);

    void upload64(FileVO fileVo);

    FileVO download64(String id);
}
