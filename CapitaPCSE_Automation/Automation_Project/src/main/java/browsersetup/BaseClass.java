package browsersetup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
//import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.text.WordUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import reporting.ListenerClass;
import utilities.ExcelUtilities;

public class BaseClass 
{
	private WebDriver driver;
	private List<String> assertMessage;
	public String environmentPortal;
	String scriptfileFolderPath = "D:\\Product\\";
	private ITestContext result;

	//@BeforeMethod(timeOut = 300000, alwaysRun=true)
	@Parameters({ "browser","environment","clientName" })
	public void setup(String browser,String environment, String clientName, String module) throws InterruptedException, IOException
	{
		Setup setup = new Setup();
		driver = setup.setupBrowser(browser,environment,clientName,module,"PORTAL");
	
	}
	
	@Parameters({ "browser","environment","clientName" })
	public void setup(String browser,String environment, String clientName, String module, String user) throws InterruptedException, IOException
	{
		Setup setup = new Setup();
		driver = setup.setupBrowser(browser,environment,clientName,module,user);
	}	
	
	@AfterMethod(timeOut = 300000, alwaysRun=true)
	@Parameters({ "browser" })
	public void removeAssertMessage()
	{
		try{//assertMessage.removeAll(assertMessage);
		if((!assertMessage.isEmpty()) && (!(assertMessage==null)))
		{
			assertMessage.clear();
		}
		}
		catch (Exception e)
		{
			assertMessage = new ArrayList<String>();
			assertMessage.addAll(assertMessage);
		}
	}
	

	
	public void setAssertMessage(String message, int verificationNumber)
	{
		try
		{
			
			assertMessage.add(message);
		} 
		catch (Exception e) 
		{
			assertMessage = new ArrayList<String>();
			assertMessage.add(message);
		}
	}

	public List<String> getAssertMessage()
	{
		return assertMessage;
	}

	@AfterMethod(timeOut = 300000, alwaysRun=true)
	@Parameters({ "browser" })
	public void quit(String browser) throws InterruptedException 
	{
		if (driver!=null)
		{
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		 String browserName = cap.getBrowserName();
		 browserName = WordUtils.capitalize(browserName);
		 System.out.println(browserName);	
		 if(browserName != null && !browserName.isEmpty())
		 {
			 tearDown(browserName);
		 }
		}
	}

	public void tearDown(String browser)
	{
		try 
		{
			//driver.close();//Commented by Akshay to eliminate Registry Exception
			driver.quit();
		}
		catch(Exception n) 
		{
			n.printStackTrace();
			/*if(WindowsUtils.thisIsWindows() && browser.equalsIgnoreCase("Firefox")) 
				WindowsUtils.tryToKillByName("firefox.exe");
			else if(WindowsUtils.thisIsWindows() && browser.equalsIgnoreCase("chrome"))
				WindowsUtils.tryToKillByName("chrome.exe");
			else if(WindowsUtils.thisIsWindows() && browser.equalsIgnoreCase("InternetExplorer"))
				WindowsUtils.tryToKillByName("iexplore.exe");*/
		}
	}
	
	@BeforeMethod(timeOut = 300000, alwaysRun=true)
	public void killIEBrowser() throws InterruptedException
	{
		try {
			
		   Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
		    Thread.sleep(3000);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
	@BeforeMethod
	public void testCaseName(ITestContext result)
	{
		try {
	
			String testcasename=result.getName();
			System.out.println("=========== "+	testcasename);
			
		//	getITestContext().getName();
			
		    } 
			catch (Exception e)
			{
		      e.printStackTrace();
		    }
	}

	public WebDriver getDriver() 
	{
		return driver;
	}

	public void setDriver(WebDriver driver) 
	{
		this.driver = driver;
	}	
	
	public ITestContext getITestContext() 
	{
		return result;
	}

	public void setITestContext(ITestContext result) 
	{
		this.result = result;
	}	
	
	
	

	public static String getSessionIdFromDriver(WebDriver driver) {
		String strSessionId=null;
		SessionId sessionId;
		String name= driver.getClass().getName();
		if(name.contains("Chrome")){
			sessionId= ((ChromeDriver)driver).getSessionId();
			if(sessionId==null){
				strSessionId=null;
			}else{
				strSessionId= sessionId.toString();
			}
		}else if(name.contains("FireFox")){
			sessionId= ((FirefoxDriver)driver).getSessionId();
			if(sessionId==null){
				strSessionId=null;
			}else{
				strSessionId= sessionId.toString();
			}
		}else if(name.contains("InternetExplorer")){
			sessionId= ((InternetExplorerDriver)driver).getSessionId();
			if(sessionId==null){
				strSessionId=null;
			}else{
				strSessionId= sessionId.toString();
			}
		}
		return strSessionId;
	}
}