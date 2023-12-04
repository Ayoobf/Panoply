package com.example.panoply;

import com.example.panoply.handlers.MongoDBHandler;

import org.bson.BsonDateTime;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;

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
