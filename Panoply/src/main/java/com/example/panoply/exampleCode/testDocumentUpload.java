package com.example.panoply.exampleCode;

import com.example.panoply.handlers.MongoDBHandler;

import java.io.File;
import java.io.IOException;

public class testDocumentUpload {
	final static String PROJECTID = "ayoob-florival-capstone";
	final static String BUCKETNAME = "dms-get-files";

	public static void main(String[] args) throws IOException {
		File file = new File("app.properties");
		System.out.println(file.exists());
	}
}
