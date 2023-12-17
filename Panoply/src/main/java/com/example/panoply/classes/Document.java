package com.example.panoply.classes;

import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;

import org.bson.BsonDateTime;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;

/**
 * @author Ayoob Florival
 */
public class Document {
	private String documentId;
	private String documentName;
	private String documentPath;
	private double documentSize;
	private Object uploadDate;
	private Object lastModified;
	private Object lastEditor;
	private boolean isCheckOut;

	/**
	 * Handle cases where a document would interact with Google and MongoDB
	 */
	public Document() {
	}

	/**
	 * Constructor for when document path is provided
	 *
	 * @param documentPath path to file in which needs to be uploaded
	 */
	public Document(String documentPath) {
		this.documentPath = documentPath;
		this.documentName = Paths.get(documentPath).getFileName().toString();
	}

	/**
	 * Uploads File
	 * <p>
	 * Interacts with MongoDB and Google to upload a file from local to the cloud
	 * </p>
	 *
	 * @param documentPath        path to where document is on the local machine
	 * @param currentUserTeamName team name of current user
	 * @param userName            username/email of the current user
	 * @throws IOException ioexception from file handling
	 */
	public void uploadDocument(String documentPath, String currentUserTeamName, String userName) throws IOException {

		String fileName = Paths.get(documentPath).getFileName().toString();
		MongoDBHandler md = new MongoDBHandler();
		GoogleCloudHandler gc = new GoogleCloudHandler();

		if (md.fileExists(fileName, currentUserTeamName)) {
			throw new IOException();
		} else {
			md.uploadFile(fileName, documentPath, currentUserTeamName, 0, userName);
			gc.uploadFile(fileName, documentPath, currentUserTeamName);

			// set document size
			double newSizeBytes = gc.getFileSize(fileName, currentUserTeamName);
			double newSizeKB = newSizeBytes / 1024;
			md.updateFileSize(fileName, currentUserTeamName, newSizeKB);
		}
	}

	/**
	 * getter for the current document's path
	 *
	 * @return document path
	 */
	public String getDocumentPath() {
		return documentPath;
	}

	/**
	 * setter for upload date. upload date usually is always time of upload
	 *
	 * @param uploadDate date snapshot to set document date
	 */
	public void setUploadDate(BsonDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
}
