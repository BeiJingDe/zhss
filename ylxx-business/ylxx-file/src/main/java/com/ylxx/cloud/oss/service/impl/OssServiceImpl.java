package com.ylxx.cloud.oss.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.file.consts.FileConsts;
import com.ylxx.cloud.file.model.FileDO;
import com.ylxx.cloud.file.model.FileDTO;
import com.ylxx.cloud.file.model.FileVO;
import com.ylxx.cloud.file.service.IFileService;
import com.ylxx.cloud.oss.service.IOssService;
import com.ylxx.cloud.oss.util.OssUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class OssServiceImpl implements IOssService {

    @Resource
    private IFileService fileService;

    @Override
    public List<FileVO> upload(FileVO fileVo, MultipartFile[] file) {
        if (ObjectUtil.isEmpty(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        OSS ossClient = OssUtil.buildClient();
        String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);
        List<FileVO> fileVos = CollUtil.newArrayList();
        List<String> ossFileNames = CollUtil.newArrayList();
        try {
            // 1. 文件上传到oss
            for (int i = 0; i < file.length; i++) {
                String id = IdUtil.fastSimpleUUID();
                String fileName = file[i].getOriginalFilename();
                String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                String ossFileName = id + fileSuffix;

                fileVo.setId(id);
                fileVo.setFileName(fileName); // 文件原始名称
                fileVo.setFilePath(bucketName + "/" + ossFileName); // OSS存储路径
                fileVo.setStatus(FileConsts.STATUS_TYPE_1);
                fileVo.setDownloadCount(0);
                fileVo.setCreateBy(HttpServletUtil.getUsername());
                fileVo.setCreateTime(DateUtil.date());
                fileVo.setUpdateBy(HttpServletUtil.getUsername());
                fileVo.setUpdateTime(DateUtil.date());
                try {
                    InputStream is = file[i].getInputStream();
                    // 创建上传Object的Metadata
//                    ObjectMetadata objectMetadata = new ObjectMetadata();
//                    objectMetadata.setContentLength(is.available());
//                    objectMetadata.setCacheControl("no-cache");
//                    objectMetadata.setHeader("Pragma", "no-cache");
//                    objectMetadata.setContentType("multipart/form-data");
//                    objectMetadata.setContentDisposition("attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//                    PutObjectResult putResult = ossClient.putObject(bucketName, ossFileName, is, objectMetadata);
                    log.info("########## 文件【{}】正在上传...", fileName);
                    PutObjectResult putResult = ossClient.putObject(bucketName, ossFileName, is);
                    log.info("########## 文件【{}】上传完毕。", fileName);
                    String ret = putResult.getETag();
                    log.info("文件上传OSS返回结果：" + ret);
					/*// 设置权限(公开读)
					ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicReadWrite);*/
                    //System.out.println("Object：" + fileName + "存入OSS成功。");
                } catch (Exception e) {
                    throw new ServiceException("文件上传到OSS失败", e);
                }
                fileVos.add(fileVo);
                ossFileNames.add(ossFileName);
            }
            // 3. 记录到数据库
            if (ObjectUtil.isNotEmpty(fileVos)) {
                fileService.insertBatch(fileVos);
            }
            return fileVos;
        } catch (Exception e) {
            // 只要有一个出现异常，删除所有已上传文件
            for (String ossFileName : ossFileNames) {
                try {
                    ossClient.deleteObject(bucketName, ossFileName);
                } catch (Exception ex) {
                    // 不处理
                }
            }
            throw new ServiceException("文件上传失败", e);
        } finally {
            OssUtil.shutdown(ossClient);
        }
    }

    @Override
    public void upload64(FileVO fileVo) {
        OSS ossClient = OssUtil.buildClient();
        String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);

        String id = IdUtil.fastSimpleUUID();
        String fileName = fileVo.getFileName();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        String ossFileName = id + fileSuffix;

        fileVo.setId(id);
//        fileVo.setFileName(fileName); // 前端传入
        fileVo.setFilePath(bucketName + "/" + ossFileName); // OSS存储路径
        fileVo.setStatus(FileConsts.STATUS_TYPE_1);
//        fileVo.setExtraProp(extraProp); // 前端传入，base64头部
        fileVo.setCreateBy(HttpServletUtil.getUsername());
        fileVo.setCreateTime(DateUtil.date());
        fileVo.setUpdateBy(HttpServletUtil.getUsername());
        fileVo.setUpdateTime(DateUtil.date());

        ByteArrayInputStream is = null;
        try {
            byte[] bytes = Base64Utils.decodeFromString(fileVo.getBase64());
            is = new ByteArrayInputStream(bytes);

            PutObjectResult putResult = ossClient.putObject(bucketName, ossFileName, is);
            log.info("文件上传OSS返回结果：" + putResult.getETag());

            fileService.insert(fileVo);
        } catch (Exception e) {
            throw new ServiceException("文件上传OSS失败", e);
        } finally {
            OssUtil.shutdown(ossClient);
            IoUtil.close(is);
        }
    }

    @Override
    public void deleteBatchIds(List<String> ids) {
        if(ObjectUtil.isNotEmpty(ids)) {
            FileDTO fileDto = new FileDTO();
            fileDto.setIds(ids);
            List<FileVO> fileVos = fileService.selectVos(fileDto);
            if(ObjectUtil.isNotEmpty(fileVos)) {
                OSS ossClient = OssUtil.buildClient();
                String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);
                for(FileDO fileDo : fileVos) {
                    if(StrUtil.isNotBlank(fileDo.getFilePath())) {
                        try {
                            String ossFileName = fileDo.getFilePath().substring(fileDo.getFilePath().indexOf("/") + 1);
                            ossClient.deleteObject(bucketName, ossFileName);
                        } catch (Exception e) {
                            log.error(StrUtil.format("删除oss文件【{}】失败", fileDo.getFilePath()), e);
                        }
                    }
                }
                OssUtil.shutdown(ossClient);
            }
        }
    }

    @Override
    public void download(String id) {
        FileVO fileVo = fileService.selectById(id);
        if(ObjectUtil.isNull(fileVo)) {
            throw new ServiceException("下载的文件不存在");
        }
        OSS ossClient = OssUtil.buildClient();
        String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);
        InputStream is = null;
        OutputStream os = null;
        try {
            log.info("########## 文件【{}】正在下载...", fileVo.getFileName());
            String ossFileName = fileVo.getFilePath().substring(fileVo.getFilePath().indexOf("/") + 1);
            OSSObject ossObject = ossClient.getObject(bucketName, ossFileName);

            HttpServletResponse response = HttpServletUtil.getResponse();
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileVo.getFileName(), "utf-8"));

            is = ossObject.getObjectContent();
            os = response.getOutputStream();

            IoUtil.copy(is, os);

            log.info("########## 文件【{}】下载完毕。", fileVo.getFileName());
        } catch (Exception e) {
            throw new ServiceException("文件下载失败", e);
        } finally {
            IoUtil.close(os);
            IoUtil.close(is);
            OssUtil.shutdown(ossClient);
        }
    }

    @Override
    public FileVO download64(String id) {
        FileVO fileVo = fileService.selectById(id);
        if(ObjectUtil.isNull(fileVo)) {
            throw new ServiceException("下载的文件不存在");
        }
        OSS ossClient = OssUtil.buildClient();
        String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            String ossFileName = fileVo.getFilePath().substring(fileVo.getFilePath().indexOf("/") + 1);
            OSSObject ossObject = ossClient.getObject(bucketName, ossFileName);

            is = ossObject.getObjectContent();
            os = new ByteArrayOutputStream();

            IoUtil.copy(is, os);

            byte[] bytes = os.toByteArray();
            String base64 = Base64Utils.encodeToString(bytes);

            fileVo.setBase64(base64);

            return fileVo;
        } catch (Exception e) {
            throw new ServiceException("文件下载失败", e);
        } finally {
            IoUtil.close(os);
            IoUtil.close(is);
            OssUtil.shutdown(ossClient);
        }
    }

    @Override
    public String getViewUrl(String id) {
        FileVO fileVo = fileService.selectById(id);
        if(ObjectUtil.isNull(fileVo)) {
            throw new ServiceException("预览的文件不存在");
        }
        OSS ossClient = OssUtil.buildClient();
        String bucketName = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_OSS, ConfigConsts.OSS_KEY_BUCKET_NAME);
        try {
            String ossFileName = fileVo.getFilePath().substring(fileVo.getFilePath().indexOf("/") + 1);
            // 设置URL过期时间为10年  3600l* 1000*24*365*10
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            String url = ossClient.generatePresignedUrl(bucketName, ossFileName, expiration).toString();
            return url;
        } catch (Exception e) {
            throw new ServiceException("文件预览失败", e);
        } finally {
            OssUtil.shutdown(ossClient);
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param fileSuffix 文件后缀
     * @return String
     */
    private String getContentType(String fileSuffix) {
        if (fileSuffix.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (fileSuffix.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (fileSuffix.equalsIgnoreCase(".jpeg") ||
                fileSuffix.equalsIgnoreCase(".jpg") ||
                fileSuffix.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (fileSuffix.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (fileSuffix.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (fileSuffix.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (fileSuffix.equalsIgnoreCase(".pptx") ||
                fileSuffix.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (fileSuffix.equalsIgnoreCase(".docx") ||
                fileSuffix.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (fileSuffix.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "application/octet-stream";
    }

}
