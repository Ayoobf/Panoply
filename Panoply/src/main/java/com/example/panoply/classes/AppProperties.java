package com.example.panoply.classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.RuleBasedCollator;
import java.util.Properties;

/**
 * @author 68Doom68 (stackoverflow)
 */
public class AppProperties {

	private static final AppProperties config_file = new AppProperties();
	private final Properties prop = new Properties();

	/**
	 * Singleton constructor for returning a properties file's contents
	 */
	private AppProperties() {
		InputStream input = null;

		try {

//			// Store and protect it where ever you want
//			String FILENAME = "src/main/resources/com/example/panoply/app.properties";
//			input = new FileInputStream(FILENAME);
//
//			// Load a properties
//			prop.load(input);
			input = getClass().getClassLoader().getResourceAsStream("app.properties");
			if (input == null) {
				throw new IOException("app.properties file not found in classpath");
			}
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error Loading app.properties", ex);
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

	/**
	 * Singleton design pattern
	 * <p>
	 * Where ever you call this method, you will always get the same and oly one instance of config_file
	 * </p>
	 *
	 * @return config file in our instance
	 * @since 1.0
	 */
	public static AppProperties getInstance() {
		return config_file;
	}

	/**
	 * getter for property file
	 *
	 * @param key The key in which the caller is looking for
	 * @return value of the key specified. If no key is returned
	 */
	public String getProperty(String key) {
		return prop.getProperty(key);
	}

}