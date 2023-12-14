package com.example.panoply.exampleCode;

import com.example.panoply.handlers.MongoDBHandler;

import java.io.IOException;

public class testDocumentUpload {
	final static String PROJECTID = "ayoob-florival-capstone";
	final static String BUCKETNAME = "dms-get-files";

	public static void main(String[] args) throws IOException {
		String md = new MongoDBHandler().findFileLastEditor("Obsidian.lnk", "Florival");
		System.out.println(md);
	}
}
