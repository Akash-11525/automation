package reporting;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ReportBaseClass 
{
	
	/*
    static WebDriver driver;
    
    public static WebDriver getDriver(){
 
        if(driver==null){
 
            
 
            driver = new FirefoxDriver();
 
        }
 
        return driver;
 
    }
    */
 
    
 
    /**
 
     * This function will take screenshot
 
     * @param webdriver
 
     * @param fileWithPath
 
     * @throws Exception
 
     */
 
    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception
    {
        try{
    	//Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
        }
        catch(NullPointerException e )
        {
        	System.out.println("The Null Pointer Exception " +e);
        }
        catch (Exception e)
        {
        	System.out.println("Something wrong with Sanp shots getting " +e);
        }
        
    }

	public static void takeSnapShot(String fileWithPath) throws Exception
	{
		Robot robot = new Robot();
		BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(screenShot, "JPG", new File(fileWithPath));
	}
}
