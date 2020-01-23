package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class WindowHandleSupport 
{
	public static String getCurrentWindowHandle(WebDriver driver)
	{
		return driver.getWindowHandle();
	}
	
	public static WebDriver getRequiredWindowDriver(WebDriver driver,String pageTitle) throws InterruptedException
	{
		WebDriver reuiredWindowDriver = null;
    	Thread.sleep(2000);
    	Set<String> windowHandles = driver.getWindowHandles();
    	System.out.println("The Windows are opened" + windowHandles);
    	for(String eachWindowHandle : windowHandles)
    	{
    		driver.switchTo().window(eachWindowHandle);
    		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    		String windowTitle = driver.getTitle();
    		System.out.println(windowTitle);
    		if( windowTitle.startsWith(pageTitle)  )
    		{   			
    			System.out.println("The Switched Window is" +windowTitle );
    			reuiredWindowDriver = driver;
    			driver.switchTo().window(eachWindowHandle);
    			System.out.println("The window is " +eachWindowHandle);
    			//helpers.CommonFunctions.PageLoadExternalwait(driver);
    			break;
    		}
    	}
    	return reuiredWindowDriver;
	}
	
	public static WebDriver getRequiredWindowDriverWithHandle(WebDriver driver,String pageHandle) throws InterruptedException
	{
		WebDriver reuiredWindowDriver = null;
    	Thread.sleep(2000);
    	Set<String> windowHandles = driver.getWindowHandles();
    	for(String eachWindowHandle : windowHandles)
    	{
    		driver.switchTo().window(eachWindowHandle);
    		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    		if( eachWindowHandle.equals(pageHandle)  )
    		{   			
    			reuiredWindowDriver = driver;
    		
    			break;
    		}
    	}
    	
    	
    	return reuiredWindowDriver;
	}
	
	
	public static WebDriver getRequiredWindowDriverWithPageTitle(WebDriver driver,String pageTitle) throws InterruptedException
	{
		WebDriver reuiredWindowDriver = null;
    	Thread.sleep(2000);
    	Set<String> windowHandles = driver.getWindowHandles();
    	for(String eachWindowHandle : windowHandles)
    	{
    		driver.switchTo().window(eachWindowHandle);
    		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    		String windowTitle = driver.getTitle();
    		if( windowTitle.equals(pageTitle)  )
    		{   			
    			reuiredWindowDriver = driver;
    			break;
    		}
    	}
    	return reuiredWindowDriver;
	}
	
	public static WebDriver switchToTheRequiredHandle(String handle,WebDriver driver)
	{
		return driver.switchTo().window(handle);
	}
	
	public static WebDriver getRequiredWindowDriverWithIndex(WebDriver driver,int index) throws InterruptedException
	{
		WebDriver requiredWindowDriver = null;
    	Thread.sleep(2000);
    	Set<String> windowHandles = driver.getWindowHandles();
    	List<String> windowHandleList= new ArrayList<String>(windowHandles);
    	int count= windowHandleList.size();
    	for(int i=0;i<count;i++){
    		String windowHandle= windowHandleList.get(i);
    		if(i==index){
    			driver.switchTo().window(windowHandle);
    			break;
    		}
    	}
    	return requiredWindowDriver;
	}

}
