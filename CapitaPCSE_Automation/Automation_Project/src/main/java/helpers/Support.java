package helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import browsersetup.Setup;
import utilities.JavaScriptExecutor;

public class Support {
	
	private WebDriver driver;
	
	@FindBy(tagName="iframe")
	List<WebElement> iframes;
	
	@FindBy(xpath ="//div[@class='loader hide-spinner']")
	static	WebElement Spinner;
	
	@FindBy(xpath ="//*[@class='spinner-visible']")
	static	WebElement SpinnerNew;
	
	
	@FindBy(id ="GmpRunDataTable_processing")
	static	WebElement ProcessingTab;
	
	
	
	
	public void switchToFrame(WebElement ele, WebDriver driver) throws InterruptedException, IOException 
	{
		this.driver = driver;
		driver = switchToRequiredIframe(ele, driver);
	}
	
	
		
	
	public WebDriver switchToRequiredIframe(WebElement ele, WebDriver driver){
		  	
		   //	this.driver = driver;
			int iframeCount = 0;
	       String iframeName = null;
	       int count = 0;
	       String iframeName1 = null;  
	       
	       iframeCount = iframes.size();
	       System.out.println("Count of frames: "+iframeCount);
	       //iframeCount = iframeCount-1;
	       try{
	              List<WebElement> frames = driver.findElements(By.xpath("//iframe[@title='Content Area']"));
	              Iterator<WebElement> iter = frames.iterator();
	           
	               
	       for(WebElement frame:frames)
	             // while(iter.hasNext())
	              {
	            	//  WebElement we = iter.next();
	            	 System.out.println(frame);
	            	  iframeName1 = frame.getAttribute("name");
	            	  System.out.println(iframeName1); 
	                           driver.switchTo().frame(iframeName1);
	                           try
	                           {
	                        		                        	   
	                           if (ele.isDisplayed())
	                           {
	                               System.out.println("We have switched into iframes : " +iframeName1);
	                        	   break;                     
	                           }
	                           }
	                           
	                           catch (Exception e)
	                           {
	                                  System.out.println("The element does not found under iframe: contentIFrame"+iframeName1 );
	                           } 
	                           driver.switchTo().defaultContent(); 
	                     }              
	              }                  
	                     catch (Exception e)
	                     {
	                           System.out.println("The element does not found under iframe: contentIFrame"+iframeCount+"");
	                           
	                     }
	             
	              return driver;
	       }
	
	
	
	
	public WebDriver scrolltoElement(WebDriver driver, WebElement ele)
	{
		//this.driver = driver;
		
		Coordinates coordinate = ((Locatable)ele).getCoordinates(); 
		coordinate.onPage(); 
		coordinate.inViewPort();
		
		//JavaScript changes commented by Akshay to resolve GRID issues
/*		JavascriptExecutor jse = (JavascriptExecutor)driver;
	
		jse.executeScript("arguments[0].scrollIntoView();", ele);*/
		
		return driver;
	}
	
	public WebDriver getDriver() 
	{
		return driver;
	}

	public void setDriver(WebDriver driver) 
	{
		this.driver = driver;
	}
	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//span[contains(@class, 'field-validation-error')]"));
			//ActualErrorMesageList=driver.findElements(By.xpath("+classPath+"));
			System.out.println(ActualErrorMesageList);
			ArrMessage = new ArrayList<String>();
			 for (WebElement ActualErrorMesage:ActualErrorMesageList)
			 {
				 scrolltoElement(driver, ActualErrorMesage);
				 String ActErr = ActualErrorMesage.getText();
				 if(!(ActErr.equalsIgnoreCase("")))
				 {
					 ArrMessage.add(ActErr);
				 }
				 
			 }
		}
		catch(NoSuchElementException e)
		{
			System.out.println("No such element is found. " +e);
		}
		return ArrMessage;
		
	}
	
	// This method returns a javascript value for inner text inside a tag
	public static String getValueByJavaScript(WebDriver driver,String id) {
		String value= JavaScriptExecutor.getValueForJavaScript(driver, "return document.getElementById('"+id+"').value");
		return value;
	}
	
	public static void enterSignatory(WebElement ele, WebDriver driver) throws InterruptedException
	{
		/*Point pt = signatureCanvasbox.getLocation();
		int x = pt.getX();
		int y = pt.getY();*/
		
		Actions actionBuilder=new Actions(driver);          
		Action drawOnCanvas=actionBuilder
		               // .contextClick(ele)
		                .moveToElement(ele,8,8)
		                .clickAndHold(ele)
		                .moveByOffset(120, 120)
		                .moveByOffset(60,70)
		                .moveByOffset(-140,-140)
		                .release(ele)
		                .build();
		drawOnCanvas.perform();
		Thread.sleep(2000);
		
	}
	
	public static boolean verifyPageLoading(WebDriver driver) throws InterruptedException
	{	
		
		boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent)
		{
			Thread.sleep(2000);
			ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}
		return ispresent;
	}
	
	public static void waitTillProcessing(WebDriver driver) {
		String ProcessingTabValue = null;
		try{
			boolean ispresent = helpers.Support.IsElementPresent(driver, ProcessingTab);
			System.out.println("The Processing tab is present  " +ispresent);
			if(ispresent)
			{
				ProcessingTabValue  = ProcessingTab.getAttribute("style");
				while (!ProcessingTabValue.equalsIgnoreCase("display: none;"))
				{
					Thread.sleep(2000);
					ProcessingTabValue = ProcessingTab.getAttribute("style");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The processing tab is not available " +e);
		}
		
	}
	public static boolean IsElementPresent (WebDriver driver, WebElement ele) throws InterruptedException
	{
		try{
			Support suport=new Support();
			suport.scrolltoElement(driver,ele);
						
		//	WebDriverWait wait = new WebDriverWait(driver, 1);
//			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			//jse.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='loader hide-spinner']")));
//			jse.executeScript("arguments[0].scrollIntoView();", ele );

		//	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='loader hide-spinner']")));
			return true;
		}
	 catch (org.openqa.selenium.NoSuchElementException
	        | org.openqa.selenium.StaleElementReferenceException
	        | org.openqa.selenium.TimeoutException e) 
	{
	    return false;
	}
		catch(Exception e)
		{
			return false;
				}
			}

	
	public static void PageLoadExternalwait(WebDriver driver) throws InterruptedException
	{
			boolean ispresent = IsElementPresent(driver,Spinner);	
		//	System.out.println("The spinner present =  " +ispresent);
			Thread.sleep(7000);
			while(!ispresent)
			{
				ispresent = IsElementPresent(driver,Spinner);
			//	System.out.println("The spinner present = " +ispresent);
				Thread.sleep(3000);
			}
		
		
	}
	
	public static void PageLoadExternalwait_Performer(WebDriver driver) throws InterruptedException
	{
			boolean ispresent = IsElementPresent(driver,SpinnerNew);	
		//	System.out.println("The spinner present =  " +ispresent);
		//	Thread.sleep(500);
			while(ispresent)
			{
				ispresent = IsElementPresent(driver,SpinnerNew);
			//	System.out.println("The spinner present = " +ispresent);
			//	Thread.sleep(500);
			}
		
	}
	
	public static void PageLoadExternalwait_ProcessApp(WebDriver driver) throws InterruptedException
	{
			boolean ispresent = IsElementPresent(driver,Spinner);	
		//	System.out.println("The spinner present =  " +ispresent);
		//	Thread.sleep(500);
			while(!ispresent)
			{
				ispresent = IsElementPresent(driver,Spinner);
			//	System.out.println("The spinner present = " +ispresent);
			//	Thread.sleep(500);
			}
		
	}

	public static void enterDataInTextField(WebElement element, String value, WebDriverWait wait) {
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		element.clear();
		element.sendKeys(value);
	}
	
	

}
