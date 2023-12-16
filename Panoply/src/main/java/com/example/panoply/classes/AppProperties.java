package com.example.panoply.classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

	private static final AppProperties config_file = new AppProperties();
	private final String FILENAME = "app.properties";
	private Properties prop = new Properties();
	private String msg = "";

	private AppProperties() {
		InputStream input = null;

		try {
			input = new FileInputStream(FILENAME);

			prop.load(input);
		} catch (IOException e) {
			msg = "Can't Find/open Property File";
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static AppProperties getInstance() {
		return config_file;
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public String getMsg() {
		return msg;
	}

}
