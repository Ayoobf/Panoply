package com.example.panoply.classes;

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

	}


	public void uploadDocument(String documentPath, String currentUserTeamName, String userName) throws IOException {

		String fileName = Paths.get(documentPath).getFileName().toString();
		new MongoDBHandler().uploadFile(fileName, documentPath, currentUserTeamName, documentSize, userName);
		new GoogleCloudHandler().uploadFile(fileName, documentPath, currentUserTeamName);

	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setUploadDate(BsonDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}
}
