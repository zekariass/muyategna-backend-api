//package com.muyategna.backend.messaging.notification.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.ses.SesClient;
//
//@Configuration
//public class AWSConfig {
//
//    @Bean
//    public SesClient sesClient() {
//        return SesClient.builder()
//                .region(Region.AWS_GLOBAL)
//                .build();
//    }
//}


package com.muyategna.backend.messaging.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AWSConfig {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKey;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretKey;

    @Bean
    public SesClient sesClient() {

        if (accessKey == null || secretKey == null) {
            throw new RuntimeException("AWS credentials not set in environment variables.");
        }

        return SesClient.builder()
                .region(Region.EU_WEST_2) // OR your actual SES region, AWS_GLOBAL is not valid for most SES ops
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }
}
