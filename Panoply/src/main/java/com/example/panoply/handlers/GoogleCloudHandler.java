package com.example.panoply.handlers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GoogleCloudHandler {
    final static String PROJECT_ID = "ayoob-florival-capstone";
    final static String BUCKET_NAME = "dms-get-files";
    private final Storage storage = StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();

    public void GoogleCloudHandler() {
    }

    public void createTeamFolder(String teamName) {
        String folderName = teamName + "/";
        BlobId blobId = BlobId.of(BUCKET_NAME, folderName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, new byte[0]); // Empty byte array creates a zero-byte object
    }

    public void downloadFile(String teamName, String fileName) {
        // The ID of your GCS object
        teamName = teamName + "/";

        final String initPath = "C:\\Panoply";
        try {
            Files.createDirectories(Paths.get(initPath + "\\" + teamName));
            // The path to which the file should be downloaded
            String destFilePath = initPath + "\\" + teamName + fileName;


            BlobId blobId = BlobId.of(BUCKET_NAME, teamName + fileName);
            Blob blob = storage.get(blobId);

            blob.downloadTo(Paths.get(destFilePath));

        } catch (IOException ignored) {
        }
    }
}
