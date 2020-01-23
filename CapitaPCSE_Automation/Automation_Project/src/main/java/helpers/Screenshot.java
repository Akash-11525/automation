package helpers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshot {
	
	public static void TakeSnap(WebDriver driver, String Note) throws InterruptedException, IOException 
	{
		
		try{
				
		/*	String className = getClass().getSimpleName();
			System.out.println(className);*/
			
			   File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        //    String filePath = System.getProperty("user.dir") + "\\Evidence\\";
	            String filePath = createDirectory() + "\\";
	            String filepathwithName = filePath + Note+".png";
	            System.out.println(filepathwithName);
	            FileUtils.copyFile(scrfile,new File(filepathwithName),true );
			
		}

		catch(NoSuchElementException e)
		{
			System.out.println("The Screenshots are not captured" +e);
		}
	 
	}
	
	private static String createDirectory()
	{
		File dir = new File(System.getProperty("user.dir") + "/Evidence/" + new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));

		if(dir.exists())
		{
			System.out.println("A folder with name '"+new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime())+"' already exist");
		}
		else
		{
			dir.mkdir();
		}
		return dir.getPath();
	}

}
