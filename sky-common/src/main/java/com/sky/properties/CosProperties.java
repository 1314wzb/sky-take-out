package com.sky.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "sky.cos")
public class CosProperties {

    private String secretId;
    private String secretKey;
    private String region;  // 腾讯云 COS 所在的地域，比如 "ap-beijing"
    private String bucketName;

}
