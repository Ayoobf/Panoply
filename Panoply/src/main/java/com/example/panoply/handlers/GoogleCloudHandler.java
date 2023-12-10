package com.example.panoply.handlers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
//            if (!blob.getContentType().equals("application/octet-stream"))
			listOfFiles.add(blob);
		}
		return listOfFiles;
	}

	// TODO return path to file on server?
	public void uploadFile(String fileName, String filePath, String teamName) throws IOException {

		// Get a reference to the bucket
		Bucket bucket = storage.get(BUCKET_NAME);

		String appendedFile = teamName + "/" + fileName;
		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, appendedFile);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		try {
			Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}


	}

	public void updateFile(String oldFileName, File newFile) throws IOException {

		// Get a reference to the bucket
		Bucket bucket = storage.get(BUCKET_NAME);


		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, oldFileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		try {
			Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(newFile.getAbsolutePath())));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}


	}

}
