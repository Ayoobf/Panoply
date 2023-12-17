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

/**
 * @author Ayoob Florival
 * @version 1.0
 */
public class GoogleCloudHandler {
	/**
	 * project_id is from the app.properties file under "google_projectId"
	 */
	private final static String PROJECT_ID = AppProperties.getInstance().getProperty("google_projectId");
	/**
	 * bucket_name is from the app.properties file under "google_bucketName"
	 */
	private final static String BUCKET_NAME = AppProperties.getInstance().getProperty("google_bucketName");
	/**
	 * storage object needed for uploading and downloading files
	 */
	private final Storage storage = StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();

	/**
	 * creates a team folder in cloud bucket
	 * <p>
	 * This method creates a folder in the google cloud bucket with the name of the current user's team name. This action is run when an admin creates their team at signup.
	 * </p>
	 *
	 * @param teamName current user's team name
	 */
	public void createTeamFolder(String teamName) {
		String folderName = teamName + "/";
		BlobId blobId = BlobId.of(BUCKET_NAME, folderName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		storage.create(blobInfo, new byte[0]); // Empty byte array creates a zero-byte object
	}

	/**
	 * downloads file from Google cloud bucket
	 * <p>
	 * This method downloads a file from the Google cloud bucket.
	 * The destination of that downloaded file is a folder on the local machine with the name of the current user's team.
	 * Usually, after the file is done downloading, it will get opened using system resources.
	 * </p>
	 *
	 * @param teamName team name of the current user
	 * @param fileName file name of target
	 * @return path of the downloaded fil. Usually used in conjunction with and openFile() method
	 * @throws NullPointerException thrown because of creating dirs problems
	 */
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

	/**
	 * Getter for files in Google cloud folder
	 * <p>
	 * Gets all the files in a specified team folder and returns them as a list of Blob objects.
	 * </p>
	 *
	 * @param teamName team name of current user
	 * @return list<Blob> of files
	 * @throws NullPointerException throws this sometimes :)
	 */
	public List<Blob> getFilesInTeamFolder(String teamName) throws NullPointerException {

		Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(teamName + "/")).iterateAll();

		List<Blob> listOfFiles = new ArrayList<>();
		for (Blob blob : blobs) {
			listOfFiles.add(blob);
		}
		return listOfFiles;
	}

	/**
	 * uploads a file from local into Google bucket
	 * <p>
	 * Uploads file from local with the preferred file name and current file path.
	 * Usually used in conjunction with a delete file method that deletes it from local.
	 * Might throw exception.
	 * </p>
	 *
	 * @param fileName file name of the file to be uploaded
	 * @param filePath file path of the file to be uploaded
	 * @param teamName team name of the current user
	 * @throws IOException caused by File class shenanigans
	 */
	public void uploadFile(String fileName, String filePath, String teamName) throws IOException {

		String appendedFile = teamName + "/" + fileName;
		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, appendedFile);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

	}

	/**
	 * Updates old file to a new version of that same file
	 * <p>
	 * Takes new file from the users local machine and overrides the old one on the cloud.
	 * </p>
	 *
	 * @param oldFileName file name of the old file
	 * @param newFile     File object of the new file
	 * @throws StorageException     storage might mess up
	 * @throws IOException          File might cause IO
	 * @throws InvalidPathException Bad file pathing might cause this
	 */
	public void updateFile(String oldFileName, File newFile) throws StorageException, IOException, InvalidPathException {
		// Create a BlobId and BlobInfo to specify the file to be uploaded
		BlobId blobId = BlobId.of(BUCKET_NAME, oldFileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		// Upload the file
		storage.create(blobInfo, Files.readAllBytes(Paths.get(newFile.getAbsolutePath())));

	}
}
