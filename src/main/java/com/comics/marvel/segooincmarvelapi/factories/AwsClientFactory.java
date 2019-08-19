package com.comics.marvel.segooincmarvelapi.factories;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AwsClientFactory implements ClientFactory {

    @Bean
    @Override
    public DynamoDB createDynamoConnection() {
        DynamoDB dynamoDB = new DynamoDB(
                AmazonDynamoDBClientBuilder
                        .standard()
                        //.withCredentials(new EnvironmentVariableCredentialsProvider())
                        .withRegion(Regions.US_EAST_2)
                        .build());
        return dynamoDB;
    }

    @Bean
    @Override
    public AmazonS3 createSimpleStorageServiceClient() {
        return AmazonS3ClientBuilder
                .standard()
               // .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    @Override
    @Bean
    public AmazonSQS createQueueListener() {
        return AmazonSQSClientBuilder
                .standard()
                //.withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    @Override
    @Bean
    public AmazonSNS createSNSClient() {
        return AmazonSNSClientBuilder
                .standard()
                //.withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.US_EAST_2)
                .build();
    }

}
