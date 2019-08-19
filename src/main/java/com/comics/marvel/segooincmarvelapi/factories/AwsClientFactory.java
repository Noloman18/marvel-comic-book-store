package com.comics.marvel.segooincmarvelapi.factories;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsClientFactory {

    @Bean
    public DynamoDB createDynamoConnection() {
        DynamoDB dynamoDB = new DynamoDB(
                AmazonDynamoDBClientBuilder
                        .standard()
                        .withCredentials(new EnvironmentVariableCredentialsProvider())
                        .withRegion(Regions.US_EAST_2)
                        .build());
        return dynamoDB;
    }

    @Bean
    public AmazonS3 createSimpleStorageServiceClient() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
