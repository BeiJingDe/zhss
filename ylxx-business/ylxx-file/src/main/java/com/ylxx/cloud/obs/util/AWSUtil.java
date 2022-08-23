package com.ylxx.cloud.obs.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.MD5;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO 文件上传后，访问路径待确定
 * <a href="https://blog.csdn.net/qq_18303491/article/details/102457403">工具类</a>
 * <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/examples-s3-buckets.html">官方文档</a>
 * @author Z-7
 */
@Slf4j
@Component
public class AWSUtil {

    private String accessKey = "emhzcy1wcmU";
    private String secretKey = "7331f1a773a267c610bc11829eaa3e17";
    private String endpoint = "zhss.hcp.sh.sgcc.com.cn";
    private String bucketName = "obs-pre";

    /**
     * MultipartFile to File
     * @param file
     * @return
     * @throws IOException
     */
    public File multipartFileToFile(MultipartFile file) throws IOException {
        File newFile = new File(file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(),newFile);
        String name = newFile.getName();
        File reNameFile = new File(new MD5().digestHex16(name));
        if ( !newFile.renameTo(reNameFile) ) {
            log.error("文件重命名失败");
        }
        return reNameFile;
    }

    /**
     * <a href="https://blog.csdn.net/zhanglf02/article/details/78502175">工具类</a>
     * <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/examples-s3-buckets.html">官方文档</a>
     * @param key 指定的上传目录 示例值 key = xxx （目录）
     * @throws FileNotFoundException 未找到文件异常
     */
    public String uploadObject(String key,File file) throws FileNotFoundException {
        AmazonS3 conn = getConn();
        // 要存储文件的元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.addUserMetadata("name", "zth");
        metadata.addUserMetadata("time", DateUtil.now());

        FileInputStream fi = new FileInputStream(file);
        // 上传文件
        conn.putObject(bucketName, key, fi, metadata);
        // 文件访问前缀 （不确定）
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, key);
        URL url = conn.generatePresignedUrl(urlRequest);
        log.info("文件访问前缀：{}",url.toString());
        return url.toString() + file.getName();
    }

    /**
     * 从s3桶删除一个文件
     * 注意：如果删除的是最后一个文件，那么，上层文件夹也会被同时删除
     * @param key 删除指定文件
     * 示例值： key = 目录/文件名
     */
    public void deleteFile(String key){
        AmazonS3 conn = getConn();
        // 删除
        conn.deleteObject(bucketName, key);

    }

    /**
     * 下载文件
     * @param key 删除指定文件 示例值：key = 目录/文件名
     * @return 待定
     */
    public String downloadFile(String key) {
        // 获取连接
        AmazonS3 conn = getConn();

        S3Object s3Object = conn.getObject(bucketName, key);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        OutputStream os = null;
        try {
            //文件名
            String fileName = s3Object.getKey().substring(s3Object.getKey().lastIndexOf("/"));
            os = new FileOutputStream(new File("D://" + fileName));
            byte[] buffer = new byte[1024];
            while ((s3ObjectInputStream.read(buffer)) != -1) {
                os.write(buffer);
            }
            os.flush();
            return "下载完成";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                IoUtil.close(os);
            }
            if (s3ObjectInputStream != null) {
                IoUtil.close(s3ObjectInputStream);
            }
        }
        return "下载失败";
    }

    /**
     * 列出一个桶内的所有文件
     * @param prefix 文件前缀 可为空
     * @return 桶内所有文件的文件名称
     */
    public List<String> selectFiles(String prefix) {
        List<String> files = new ArrayList<>();
        // 获取连接
        AmazonS3 conn = getConn();
        //可以加入文件名前缀当作搜索条件
        ObjectListing objectListing = conn.listObjects(bucketName, prefix);
        List<S3ObjectSummary> s3ObjectSummaryList = objectListing.getObjectSummaries();
        s3ObjectSummaryList.forEach(item -> {
            //文件路径及名称-如果是文件夹，以"/"标识路径
            System.out.print(item.getKey() + ",");
            files.add(item.getKey());
            //文件UUID
            System.out.println(item.getETag());
        });
        return files;
    }

    public AmazonS3 getConn() {
        // 获取连接
        AWSCredentials credentials=new BasicAWSCredentials(accessKey,secretKey);
        ClientConfiguration clientConfig=new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3ClientBuilder builder=AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials));
        EndpointConfiguration endpointConfiguration=new EndpointConfiguration(endpoint,Regions.AP_NORTHEAST_1.getName());
        builder.setEndpointConfiguration(endpointConfiguration);

        return builder.build();
    }
}