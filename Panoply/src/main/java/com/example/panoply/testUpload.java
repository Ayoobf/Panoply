package com.example.panoply;

import com.google.cloud.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class testUpload {

    public static void main(String[] args) {
        // test file variables
        String projectId = "ayoob-florival-capstone";
        String bucketName = "dms-get-files";
        String objectName = "test";
        String filePath = "C:\\Users\\rfloo\\OneDrive - Brookdale Community College\\Brookdale Courses\\COMP296\\panoply\\Panoply\\src\\main\\java\\com\\example\\panoply\\folder\\Test.txt";

        // Initialize a Storage object
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        // Get a reference to the bucket
        Bucket bucket = storage.get(bucketName);

        // Create a BlobId and BlobInfo to specify the file to be uploaded
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload the file
        try {
            Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("File " + objectName + " uploaded to bucket " + bucketName);
    }
}

