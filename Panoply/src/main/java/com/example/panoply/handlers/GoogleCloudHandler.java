package com.example.panoply.handlers;

import com.example.panoply.classes.AppProperties;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GoogleCloudHandler {
	private final static String PROJECT_ID = AppProperties.getInstance().getProperty("google_projectId");
	private final static String BUCKET_NAME = AppProperties.getInstance().getProperty("google_bucketName");
	private final Storage storage = StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();


	public void createTeamFolder(String teamName) {
		String folderName = teamName + "/";
		BlobId blobId = BlobId.of(BUCKET_NAME, folderName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		storage.create(blobInfo, new byte[0]); // Empty byte array creates a zero-byte object
	}

	public String downloadFile(String teamName, String fileName) throws NullPointerException {
		// The ID of your GCS object
		teamName = teamName + "/";

		final String initPath = "C:\\Panoply";
		try {
			Files.createDirectories(Paths.get(initPath + "\\" + teamName));
			// The path to which the file should be downloaded
			String destFilePath = initPath + "\\" + fileName;


			BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
			Blob blob = storage.get(blobId);

			blob.downloadTo(Paths.get(destFilePath));

			return destFilePath;

		} catch (IOException ignored) {
		}

		return null;
	}

	public List<Blob> getFilesInTeamFolder(String teamName) throws NullPointerException {

		Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(teamName + "/")).iterateAll();

		List<Blob> listOfFiles = new ArrayList<>();
		for (Blob blob : blobs) {
			listOfFiles.add(blob);
		}
		return listOfFiles;
	}


	public void uploadFile(String fileName, String filePath, String teamName) throws IOException {

		String appendedFile = teamName + "/" + fileName;
		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, appendedFile);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

	}

	public void updateFile(String oldFileName, File newFile) throws StorageException, IOException, InvalidPathException {

		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, oldFileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		storage.create(blobInfo, Files.readAllBytes(Paths.get(newFile.getAbsolutePath())));

	}

}
