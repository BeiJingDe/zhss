package com.ylxx.cloud.localfs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.poi.PoiUtil;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.file.consts.FileConsts;
import com.ylxx.cloud.file.model.FileDO;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileService;
import com.ylxx.cloud.localfs.service.ILocalFsService;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
@Transactional
public class LocalFsServiceImpl implements ILocalFsService {

    @Resource
    private IFileService fileService;

    @Override
    public List<FileVO> upload(FileVO fileVo , MultipartFile[] file) {
        if(ObjectUtil.isEmpty(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        List<FileVO> fileVos = CollUtil.newArrayList();
        try {
            // 1. 文件先保存在本地
            for(int i=0; i<file.length; i++) {
                String uploadPath = ConfigCacheValue.FILE_KEY_UPLOAD_PATH();

                String id = IdUtil.fastSimpleUUID();
                String fileName = file[i].getOriginalFilename();
                String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
                // 文件保存路径，例：/temp/202011/abc12ef54.doc
                String filePath = StrUtil.format("{}/{}/{}.{}", uploadPath, DateUtil.date().toString("yyyyMM"), id, fileSuffix);
                file[i].transferTo(FileUtil.touch(filePath));


                fileVo.setId(id);
                fileVo.setFileName(fileName); // 文件原始名称
                fileVo.setFilePath(filePath);
                fileVo.setStatus(FileConsts.STATUS_TYPE_1);
                fileVo.setDownloadCount(0);
                fileVo.setCreateBy(HttpServletUtil.getUsername());
                fileVo.setCreateTime(DateUtil.date());
                fileVo.setUpdateBy(HttpServletUtil.getUsername());
                fileVo.setUpdateTime(DateUtil.date());
                fileVos.add(fileVo);
            }
            // 2. 记录到数据库
            if(ObjectUtil.isNotEmpty(fileVos)) {
                fileService.insertBatch(fileVos);
            }
        } catch (Exception e) {
            // 出现异常要把已上传的文件都删了
            fileVos.forEach(item -> {
                FileUtil.del(item.getFilePath());
            });
            throw new ServiceException("文件上传到本地失败", e);
        }
        return fileVos;
    }

    @Override
    public void deleteBatchIds(List<String> ids) {
        if(ObjectUtil.isNotEmpty(ids)) {
            FileDTO fileDto = new FileDTO();
            fileDto.setIds(ids);
            List<FileVO> fileVos = fileService.selectVos(fileDto);
            if(ObjectUtil.isNotEmpty(fileVos)) {
                for(FileDO fileDo : fileVos) {
                    if(StrUtil.isNotBlank(fileDo.getFilePath())) {
                        if(fileDo.getFilePath().indexOf(fileDo.getId()) > -1) {
                            // 按照文件上传规则判断文件路径是否包含文件ID，避免出现误删系统文件
                            FileUtil.del(fileDo.getFilePath());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void download(String id) {
        FileVO fileVo = fileService.selectById(id);
        if(ObjectUtil.isNull(fileVo)) {
            throw new ServiceException("下载的文件不存在");
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            File file = new File(fileVo.getFilePath());
            if(!FileUtil.exist(file)) {
                throw new ServiceException("本地文件系统找不到下载的文件");
            }
            is = new FileInputStream(file);

            HttpServletResponse response = HttpServletUtil.getResponse();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileVo.getFileName(), "utf-8"));
            os = response.getOutputStream();

            IoUtil.copy(is, os);
        } catch (Exception e) {
            throw new ServiceException("文件下载失败", e);
        } finally {
            IoUtil.close(os);
            IoUtil.close(is);
        }
    }

}
