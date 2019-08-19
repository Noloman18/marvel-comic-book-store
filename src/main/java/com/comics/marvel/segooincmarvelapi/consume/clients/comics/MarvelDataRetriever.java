package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.comics.marvel.segooincmarvelapi.consume.clients.comics.model.ComicTitle;
import com.comics.marvel.segooincmarvelapi.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MarvelDataRetriever {
    @Autowired
    private Persistence persistence;

    @Autowired
    private ComicDataDownloader comics;

    @Autowired
    private ImageDownloadAndSave imageDownloadAndSave;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private AmazonSNS amazonSNS;

    public void retrieveData() {
        try {
            do {
                ReceiveMessageRequest receiveMessageRequest =
                        new ReceiveMessageRequest();
                receiveMessageRequest.setQueueUrl("https://sqs.us-east-2.amazonaws.com/073655690990/segoo-inc-marvel-download-instruction");
                receiveMessageRequest.setMaxNumberOfMessages(10);

                ReceiveMessageResult instructions = amazonSQS.receiveMessage(receiveMessageRequest);
                List<Message> messageList = instructions.getMessages();
                if (messageList.size() == 0)
                    break;

                for (Message message : messageList) {
                    String[] beginEnd = message.getBody().split("\\,");
                    String begin = beginEnd[0].trim();
                    String end = beginEnd[1].trim();

                    extract(begin, end);

                    amazonSQS.deleteMessage("segoo-inc-marvel-download-instruction", message.getReceiptHandle());
                    sendNotification(
                            "Successfully downloaded comic book titles",
                            String.format("Successfully downloaded comic book titles between %s and %s", begin, end));
                }
            } while (true);
        } catch (Exception e) {
            String errorMessage = Arrays.toString(e.getStackTrace());
            sendNotification("Error occurred while downloading comic book titles", errorMessage);
        }
    }

    private void sendNotification(String subject, String message) {
        try {
            PublishRequest publishReq = new PublishRequest()
                    .withTopicArn("arn:aws:sns:us-east-2:073655690990:segoo-inc-marvel-events")
                    .withMessage(message)
                    .withSubject(subject);
            amazonSNS.publish(publishReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extract(String begin, String end) {
        ComicTitle[] comicList = comics.downloadTitleInformation(begin, end);

        String[] listOfImages =
                Arrays.stream(comicList)
                        .map(item -> item.getImagePath())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                        .toArray(new String[0]);

        imageDownloadAndSave.saveImageAsync(listOfImages);
        persistence.saveComicBooks(comicList);
    }
}
