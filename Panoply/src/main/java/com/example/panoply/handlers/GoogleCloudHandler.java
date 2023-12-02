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

    public int getNumFilesInTeamFolder(String teamName) throws NullPointerException {

        Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(teamName + "/")).iterateAll();

        int count = 0;
        for (Blob blob :
                blobs) {
            count++;
        }

        // -1 because it counts parent dir as one
        count = count - 1;
        // a count or -1 means not found or no files
        if (count == -1) {
            throw new NullPointerException("Team has no file or does not exist");
        } else {
            return count;
        }
    }
}
