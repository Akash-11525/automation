package logger;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class LoggerUtil extends Logger {

	private static final String LOGGER4J_PATH = "src/main/java/log4j.properties";

	public static void getConfig(){
		Properties properties = new Properties();
		InputStream log4jFile = null; 
		try {
			log4jFile = new FileInputStream(LOGGER4J_PATH);
		    properties.load(log4jFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		    try {
		        log4jFile.close();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	protected LoggerUtil(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
	
}
