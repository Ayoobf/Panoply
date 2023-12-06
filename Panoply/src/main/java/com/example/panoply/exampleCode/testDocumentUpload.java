package com.example.panoply.exampleCode;

import com.example.panoply.Document;
import com.example.panoply.User;
import com.example.panoply.handlers.GoogleCloudHandler;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class testDocumentUpload {
	final static String PROJECTID = "ayoob-florival-capstone";
	final static String BUCKETNAME = "dms-get-files";

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\rfloo\\Desktop\\Test2.txt";
		new Document().uploadDocument(path, "RED", "1");
	}
}
