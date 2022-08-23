package com.ylxx.cloud.fdfs.util;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import org.csource.common.IniFileReader;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ylxx.cloud.exception.ext.ServiceException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FastDFSUtil {

	private static Properties props = null;

	private static void init() {
		try {
			if(props == null) {
				props = new Properties();
				InputStream in = IniFileReader.loadFromOsFileSystemOrClasspathAsStream("fastdfs-client.properties");
				if (in != null) {
					props.load(in);
				}
			}
			String trackerServers = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_FDFS, ConfigConsts.FDFS_KEY_TRACKER_SERVERS);
			props.setProperty("fastdfs.tracker_servers", trackerServers);
			log.info("fastdfs客户端初始化参数：{}", props);
			ClientGlobal.initByProperties(props);
		} catch (IOException | MyException e) {
			throw new ServiceException("fastdfs客户端初始化失败", e);
		}
	}
	
	/**
	 * 
	 * @param localPath: 本地文件路径
	 * @return 返回fastdfs文件存储路径
	 */
	public static String upload(String localPath) {
		return upload(localPath, null);
	}
	
	/**
	 * 
	 * @param localPath
	 * @param metadata
	 * @return
	 */
	public static String upload(String localPath, JSONObject metadata) {
		init();
        try {
			File file = new File(localPath);
			if(!file.exists()) {
				throw new ServiceException(StrUtil.format("本地文件【{}】不存在", localPath));
			}
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            NameValuePair[] pairs = mapToPairs(metadata);
            String[] fileIds = storageClient.upload_file(localPath, null, pairs);
            return fileIds[0] + "/" + fileIds[1];
        } catch (Exception e) {
			throw new ServiceException(StrUtil.format("本地文件上传fastdfs失败"), e);
		}
	}
	
	/**
	 * 
	 * @param fdfsPath: fastdfs文件路径
	 * @param localPath: 本地文件路径
	 */
	public static void download(String fdfsPath, String localPath) {
		init();
		OutputStream os = null;
		try {
			File file = new File(localPath);
			if(file.exists()) {
				throw new ServiceException("本地文件【"+localPath+"】已存在，请更换存储位置");
			}
			String group_name = fdfsPath.substring(0, fdfsPath.indexOf("/"));
			String remote_filename = fdfsPath.substring(fdfsPath.indexOf("/") + 1);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] bytes = storageClient.download_file(group_name, remote_filename);
            os = new FileOutputStream(file);
            os.write(bytes);
            log.info(StrUtil.format("fastdfs文件【{}】下载成功【{}】", fdfsPath, localPath));
		} catch (Exception e) {
			throw new ServiceException(StrUtil.format("fastdfs文件【{}】下载失败", fdfsPath), e);
		} finally {
			IoUtil.close(os);
		}
	}
	
	/**
	 * 
	 * @param fdfsPath: fastdfs文件路径
	 */
	public static void delete(String fdfsPath) {
		init();
		try {
			String group_name = fdfsPath.substring(0, fdfsPath.indexOf("/"));
			String remote_filename = fdfsPath.substring(fdfsPath.indexOf("/") + 1);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            int i = storageClient.delete_file(group_name, remote_filename);
            if(i == 0) {
            	log.info(StrUtil.format("fastdfs文件【{}】删除成功", fdfsPath));
            } else {
            	log.error(StrUtil.format("fastdfs文件【{}】删除失败，错误码【{}】", fdfsPath, i));
            }
		} catch (Exception e) {
			throw new ServiceException(StrUtil.format("fastdfs文件【{}】删除失败", fdfsPath), e);
		}
	}
	
	/**
	 * 获取文件信息
	 * @param fdfsPath: fastdfs文件路径
	 * @return
	 */
	public static FileInfo getFileInfo(String fdfsPath) {
		init();
		try {
			String group_name = fdfsPath.substring(0, fdfsPath.indexOf("/"));
			String remote_filename = fdfsPath.substring(fdfsPath.indexOf("/") + 1);
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getTrackerServer();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			FileInfo fileInfo = storageClient.get_file_info(group_name, remote_filename);
			return fileInfo;
		} catch (Exception e) {
			throw new ServiceException(StrUtil.format("获取fastdfs文件【{}】信息失败", fdfsPath), e);
		}
	}

	/**
	 * 获取文件元数据
	 * @param fdfsPath: fastdfs文件路径
	 * @return
	 */
	public static NameValuePair[] getMetadata(String fdfsPath) {
		init();
		try {
			String group_name = fdfsPath.substring(0, fdfsPath.indexOf("/"));
			String remote_filename = fdfsPath.substring(fdfsPath.indexOf("/") + 1);
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getTrackerServer();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			NameValuePair[] pairs = storageClient.get_metadata(group_name, remote_filename);
			return pairs;
		} catch (Exception e) {
			throw new ServiceException(StrUtil.format("获取fastdfs文件【{}】元数据失败", fdfsPath), e);
		}
	}
	
	/**
	 * 修改文件元数据
	 * @param fdfsPath
	 */
	public static void setMetadata(String fdfsPath, JSONObject metadata) {
		init();
		try {
			String group_name = fdfsPath.substring(0, fdfsPath.indexOf("/"));
			String remote_filename = fdfsPath.substring(fdfsPath.indexOf("/") + 1);
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getTrackerServer();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			NameValuePair[] pairs = mapToPairs(metadata);
			storageClient.set_metadata(group_name, remote_filename, pairs, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
			log.info(StrUtil.format("修改fastdfs文件【{}】元数据成功", fdfsPath));
		} catch (Exception e) {
			throw new ServiceException(StrUtil.format("修改fastdfs文件【{}】元数据失败", fdfsPath), e);
		}
	}
	
	
	private static NameValuePair[] mapToPairs(JSONObject metadata) {
		NameValuePair[] pairs = null;
        if(ObjectUtil.isNotEmpty(metadata)) {
        	List<NameValuePair> pairList = CollUtil.newArrayList();
        	metadata.forEach((key, val) -> {
        		if(val != null) {
        			NameValuePair valuePair = new NameValuePair();
        			valuePair.setName(key);
        			valuePair.setValue(val.toString());
        			pairList.add(valuePair);
        		}
        	});
        	if(ObjectUtil.isNotEmpty(pairList)) {
        		pairs = new NameValuePair[pairList.size()];
        		for(int i=0; i<pairList.size(); i++) {
        			pairs[i] = pairList.get(i);
        		}
        	}
        }
        return pairs;
	}
	
}
