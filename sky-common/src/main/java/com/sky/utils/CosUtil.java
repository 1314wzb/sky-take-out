package com.sky.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.model.PutObjectRequest;
import com.sky.properties.CosProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
@Slf4j
@Component
public class CosUtil {

    @Autowired
    private CosProperties cosProperties;  // 注入 CosProperties 配置类

    public String upload(byte[] bytes, String objectName) {
        // 从配置类中获取 COS 配置信息
        String secretId = cosProperties.getSecretId();
        String secretKey = cosProperties.getSecretKey();
        String region = cosProperties.getRegion();
        String bucketName = cosProperties.getBucketName();

        // 使用腾讯云 COS SDK 的 Region 类
        Region regionObj = new Region(region);  // 使用区域字符串构造 Region 对象

        // 创建 COS 客户端配置
        ClientConfig clientConfig = new ClientConfig(regionObj);

        // 创建 COS 凭证
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);

        // 创建 COS 客户端实例
        COSClient cosClient = new COSClient(credentials, clientConfig);

        File tempFile = null;
        try {
            // 创建临时文件并将字节数组写入
            tempFile = File.createTempFile("cos-upload-", ".tmp");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(bytes);
            }

            // 创建 PutObjectRequest 请求对象，设置存储桶、文件名和文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, tempFile);

            // 上传文件到 COS
            cosClient.putObject(putObjectRequest);

        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
        } finally {
            // 关闭 COS 客户端
            if (cosClient != null) {
                cosClient.shutdown();
            }
            // 删除临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        // 构建文件的访问路径
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder.append(bucketName)
                .append(".cos.")
                .append(regionObj.getRegionName())
                .append(".myqcloud.com/")
                .append(objectName);

        log.info("文件上传到: {}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}
