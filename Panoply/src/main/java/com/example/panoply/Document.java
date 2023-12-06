package com.example.panoply;

import com.example.panoply.handlers.GoogleCloudHandler;
import com.example.panoply.handlers.MongoDBHandler;

import org.bson.BsonDateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Document {
	private String documentId;
	private String documentName;
	private String documentPath;
	private double documentSize;
	private Object uploadDate;
	private Object lastModified;
	private Object lastEditor;
	private boolean isCheckOut;

	public Document() {
	}

	public Document(String documentPath) {
		this.documentPath = documentPath;
		this.documentName = Paths.get(documentPath).getFileName().toString();
		this.documentSize = (double) new File(documentPath).length() / (1024 * 1024);

	}

	public Document(String documentId,
					String documentName,
					String documentPath,
					double documentSize,
					Object uploadDate,
					Object lastModified,
					Object lastEditor,
					boolean isCheckOut) {

		this.documentId = documentId;
		this.documentName = documentName;
		this.documentPath = documentPath;
		this.documentSize = documentSize;
		this.uploadDate = uploadDate;
		this.lastModified = lastModified;
		this.lastEditor = lastEditor;
		this.isCheckOut = isCheckOut;
	}


	// TODO: uplaod method where it uploads the doc in two places [Mongo and Google]
	public void uploadDocument(String documentPath, String currentUserTeamName, String userName) throws IOException {

		String fileName = Paths.get(documentPath).getFileName().toString();
		new MongoDBHandler().uploadFile(fileName, documentPath, currentUserTeamName, documentSize, userName);
		new GoogleCloudHandler().uploadFile(fileName, documentPath, currentUserTeamName);

	}




	public String getDocumentPath() {
		return documentPath;
	}

	public String getDocumentName() {
		return documentName;
	}

	public Object getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(BsonDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
}
