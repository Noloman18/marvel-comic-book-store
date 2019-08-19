package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.comics.marvel.segooincmarvelapi.factories.ClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ImageDownloadAndSaveToS3 implements ImageDownloadAndSave {

    @Autowired
    private ClientFactory clientFactory;

    @Override
    public void saveImageAsync(String... imageUrl) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Queue<String> queue = new ConcurrentLinkedQueue<>(Arrays.asList(imageUrl));


        executorService.execute(() -> {
            try {
                AmazonS3 s3Client = clientFactory.createSimpleStorageServiceClient();

                while (true) {
                    if (queue.size()==0)
                        break;

                    String image = queue.poll();

                    if (image != null && !image.matches(".*/.*/portrait_((small)|(medium)|(fantasy)|(uncanny)|(incredible)).jpg"))
                        continue;

                    if (image==null)
                        break;

                    String[] urlComponent = image.split("/");

                    String imageName = urlComponent[urlComponent.length - 2];

                    URL url = new URL(image);
                    URLConnection connection = url.openConnection();

                    try (InputStream input = connection.getInputStream()) {
                        byte[] content = IOUtils.toByteArray(input);

                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        objectMetadata.setContentType("image/jpeg");
                        objectMetadata.setContentLength(Long.valueOf(content.length));

                        PutObjectRequest putObjectRequest =
                                new PutObjectRequest(
                                        "segoo-inc-comic-book-store",
                                        String.format("images_titles/%s", imageName),
                                        new ByteArrayInputStream(content),
                                        objectMetadata);
                        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                        s3Client.putObject(putObjectRequest);

                        System.out.printf("Downloaded %s\n", image);
                    } catch (IOException io) {
                        System.err.println(io.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }
}
