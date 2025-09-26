package com.smhrd.ncp_obj_storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpStorageConfig {

    @Value("${ncp.s3.region}")
    private String region;

    @Value("${ncp.s3.endpoint}")
    private String endPoint;

    @Value("${ncp.s3.accessKey}")
    private String accessKey;

    @Value("${ncp.s3.secretKey}")
    private String secretKey;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withPathStyleAccessEnabled(true)
                .build();
    }

}
