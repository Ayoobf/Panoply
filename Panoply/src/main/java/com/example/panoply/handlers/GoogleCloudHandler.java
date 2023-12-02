package com.example.panoply.handlers;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

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
}
