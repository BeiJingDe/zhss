package com.ylxx.cloud.fdfs.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.fdfs.service.IFdfsService;
import com.ylxx.cloud.fdfs.util.FastDFSUtil;
import com.ylxx.cloud.file.consts.FileConsts;
import com.ylxx.cloud.file.model.FileDO;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileService;
import com.ylxx.cloud.oss.util.OssUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class FdfsServiceImpl implements IFdfsService {

    @Resource
    private IFileService fileService;

    @Override
    public List<FileVO> upload(FileVO fileVo, MultipartFile[] file) {
        if(ObjectUtil.isEmpty(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        String[] localFiles = new String[file.length];
        List<FileVO> fileVos = CollUtil.newArrayList();
        try {
            // 1. 文件先保存在本地
            for(int i=0; i<file.length; i++) {
                try {
                    String uploadPath = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_UPLOAD_PATH);
                    File localFile = new File(uploadPath, IdUtil.fastSimpleUUID() + "_" + file[i].getOriginalFilename());
                    file[i].transferTo(localFile);
                    localFiles[i]= localFile.getAbsolutePath();
                } catch (Exception e) {
                    throw new ServiceException("上传文件保存到本地失败", e);
                }
            }
            // 2. 文件上传到fastdfs
            for(int i=0; i<file.length; i++) {
                fileVo.setId(IdUtil.fastSimpleUUID());
                fileVo.setFileName(file[i].getOriginalFilename()); // 文件原始名称
                fileVo.setStatus(FileConsts.STATUS_TYPE_1);
                fileVo.setDownloadCount(0);
                fileVo.setCreateBy(HttpServletUtil.getUsername());
                fileVo.setCreateTime(DateUtil.date());
                fileVo.setUpdateBy(HttpServletUtil.getUsername());
                fileVo.setUpdateTime(DateUtil.date());
                try {
                    String fdfsPath = FastDFSUtil.upload(localFiles[i], JSON.parseObject(JSON.toJSONString(fileVo)));
                    fileVo.setFilePath(fdfsPath); // FastDFS存储路径
                } catch (Exception e) {
                    throw new ServiceException("文件上传到fastdfs失败", e);
                }
                fileVos.add(fileVo);
            }
            // 3. 记录到数据库
            if(ObjectUtil.isNotEmpty(fileVos)) {
                fileService.insertBatch(fileVos);
            }
            return fileVos;
        } catch (Exception e) {
            // 只要有一个出现异常，删除所有已上传文件
            for(FileVO vo : fileVos) {
                try {
                    FastDFSUtil.delete(vo.getFilePath());
                } catch (Exception ex) {
                    // 不处理
                }
            }
            throw new ServiceException("文件上传失败", e);
        } finally {
            // 删除所有本地保存的文件
            for(String localFile : localFiles) {
                FileUtil.del(localFile);
            }
        }
    }

    /**
     * 删除fasfdfs文件：实际上只修改fastdfs文件元数据
     * @param ids
     */
    @Override
    public void deleteBatchIds(List<String> ids) {
        if(ObjectUtil.isNotEmpty(ids)) {
            FileDTO fileDto = new FileDTO();
            fileDto.setIds(ids);
            List<FileVO> fileVos = fileService.selectVos(fileDto);
            if(ObjectUtil.isNotEmpty(fileVos)) {
                for(FileDO fileDo : fileVos) {
                    if(StrUtil.isNotBlank(fileDo.getFilePath())) {
                        try {
                            FastDFSUtil.delete(fileDo.getFilePath());
                        } catch (Exception e) {
                            log.error(StrUtil.format("删除fastdfs文件【{}】失败", fileDo.getFilePath()), e);
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
        String tempFile = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            String downloadPath = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FILE, ConfigConsts.FILE_KEY_DOWNLOAD_PATH);
            tempFile = downloadPath + "/" + IdUtil.fastSimpleUUID() + "_" + fileVo.getFileName();
            FastDFSUtil.download(fileVo.getFilePath(), tempFile);
            is = new FileInputStream(new File(tempFile));

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
            FileUtil.del(tempFile);
        }
    }

}
