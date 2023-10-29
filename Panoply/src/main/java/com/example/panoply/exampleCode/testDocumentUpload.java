package com.example.panoply.exampleCode;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class testDocumentUpload {
    final static String PROJECTID = "ayoob-florival-capstone";
    final static String BUCKETNAME = "dms-get-files";

    public static void main(String[] args) {
        // test file variables

        String fileName = "hello world 06";
        String filePath = "C:\\Users\\rfloo\\OneDrive - Brookdale Community College\\Brookdale Courses\\COMP296\\panoply\\Panoply\\src\\main\\java\\com\\example\\panoply\\folder\\Test.txt";
        // Initialize a Storage object
        Storage storage = StorageOptions.newBuilder().setProjectId(PROJECTID).build().getService();

        // Get a reference to the bucket
        Bucket bucket = storage.get(BUCKETNAME);

        // Create a BlobId and BlobInfo to specify the file to be uploaded
        BlobId blobId = BlobId.of(BUCKETNAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload the file
        try {
            Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("File " + fileName + " uploaded to bucket " + BUCKETNAME);
    }
}

