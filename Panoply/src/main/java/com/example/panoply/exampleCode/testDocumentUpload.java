package com.example.panoply.exampleCode;

import com.example.panoply.handlers.MongoDBHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.stage.Stage;

public class testDocumentUpload {
	final static String PROJECTID = "ayoob-florival-capstone";
	final static String BUCKETNAME = "dms-get-files";

	public static void main(String[] args) throws IOException {


		String fileName = "Gerald.txt";
		String teamName = "FLorival";
		File selectedFile = new File("C:\\Panoply\\" + teamName + "\\" + fileName);
		System.out.println(selectedFile.exists());
	}
}
