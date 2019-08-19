package com.comics.marvel.segooincmarvelapi.consume.clients.comics;

import com.amazonaws.util.IOUtils;
import com.comics.marvel.segooincmarvelapi.factories.AwsClientFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ImageDownloadAndSaveToS3 implements ImageDownloadAndSave {
    @Override
    public void saveImageAsync(String... imageUrl) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<String> queue  = new ConcurrentLinkedQueue<>(Arrays.asList(imageUrl));

        executorService.execute(()-> {
            try {
                AwsClientFactory awsClientFactory = new AwsClientFactory();

                while (true) {
                    String image = queue.poll();
                    System.out.printf("Downloading %s\n",image);
                    URL url = new URL(image);
                    URLConnection connection = url.openConnection();

                    try (InputStream input = connection.getInputStream()) {
                        byte[] byteArray = IOUtils.toByteArray(input);
                        int length = byteArray.length;

                    }
                    catch(IOException io) {
                        System.err.println(io.getMessage());
                    }
                }
            }
            catch(Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }
}
