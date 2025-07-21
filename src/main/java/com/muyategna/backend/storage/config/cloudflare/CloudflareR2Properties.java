package com.muyategna.backend.storage.config.cloudflare;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cloudflare.r2")
public class CloudflareR2Properties {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucketName;

}
