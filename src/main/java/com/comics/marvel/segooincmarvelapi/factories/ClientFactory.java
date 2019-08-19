package com.comics.marvel.segooincmarvelapi.factories;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;

public interface ClientFactory {
    DynamoDB createDynamoConnection();

    AmazonS3 createSimpleStorageServiceClient();

    AmazonSQS createQueueListener();

    AmazonSNS createSNSClient();
}
