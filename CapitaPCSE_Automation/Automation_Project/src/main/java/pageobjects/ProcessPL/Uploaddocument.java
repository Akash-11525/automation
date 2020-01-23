package pageobjects.ProcessPL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class Uploaddocument extends Support {

	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(xpath="//div[@id='divAddUpdateNote']//div/div//div[2]//textarea")
	WebElement OfficerNotes;
	
	@FindBy(xpath="//div[@id='divAddUpdateNote']")
	WebElement CaseOfficerNotesWindow;
	
	@FindBy(xpath="//input[@data-target-id='divAddUpdateNote']")
	WebElement Saveondialogue;
	
	@FindBy(id="PCSEChecks")
	WebElement PCSEchecktab;
	
	
	public Uploaddocument(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
		
	}

	public Uploaddocument AddedCeaseofficerNotes() {
		try{
			Thread.sleep(1000);
			int i = 1;
			int j = 0;
			List<WebElement> Uploaddocuments = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/div/a"));
			System.out.println(Uploaddocuments.size());
			System.out.println(Uploaddocuments);
			for (WebElement Uploaddocument : Uploaddocuments) 
        	  {  
        		Thread.sleep(1000);
				List<WebElement> Uploaddocuments1 = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/div/a"));
      			System.out.println(Uploaddocuments1.size());
       			System.out.println(Uploaddocuments1.get(j).getText());
       			scrolltoElement(driver, Uploaddocuments1.get(j));
        			Actions actions = new Actions(driver);        			
        	    	actions.moveToElement(Uploaddocuments1.get(j));
        	    	actions.doubleClick().build().perform();
        	    	Thread.sleep(2000);
        	    	WebElement AddNotes =  driver.findElement(By.xpath("//div[@id='divAccordionbody"+i+"']//tbody//tr[1]/td[3]"));
        			wait.until(ExpectedConditions.elementToBeClickable(AddNotes));
        			scrolltoElement(driver, AddNotes);
          			Actions actions1 = new Actions(driver);
          	    	actions1.moveToElement(AddNotes);
          	    	actions1.doubleClick().build().perform();
          	    /*	wait.until(ExpectedConditions.elementToBeClickable(CaseOfficerNotesWindow));
          	    	if(CaseOfficerNotesWindow.isDisplayed())
          	    	{
          	    		wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).clear();
          	    		wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).sendKeys("Automation Purpose");
          	    		Actions actions2 = new Actions(driver);
              	    	actions2.moveToElement(Saveondialogue);
              	    	actions2.doubleClick().build().perform(); */
              	    	//wait.until(ExpectedConditions.elementToBeClickable(AddNotes));
              	    	Thread.sleep(2000);
              	       	i = i+ 1;
              	    	j = j+1;
              	    	//new Uploaddocument(driver);
              	    	if(j== Uploaddocuments1.size()+1)
              	    	{
              	    		break;
              	    	}
              	                 	    	
          	    	//}
          	    	
        	    
          	    	}       	    	
        		  
        	  
			
		}
		catch(Exception e)
		{
			System.out.println("The all document is not added Cease notes ");
		}
		
		return new Uploaddocument(driver);
	}

	public PCSECheck clickonPCSEcheck() {
		try 
		{
			scrolltoElement(driver, PCSEchecktab);
		
			Actions actions = new Actions(driver);
        	actions.moveToElement(PCSEchecktab);
        	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The PCSE check is not happen");
		}
		return new PCSECheck(driver);
	}

	public int downloaddocument() {
		int downloadtab = 0;
	try{
		int i = 1;
		int j = 0;
		Thread.sleep(3000);
		List<WebElement> Uploaddocuments = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/div/a"));
		downloadtab = Uploaddocuments.size();
		System.out.println(Uploaddocuments);
		for (WebElement Uploaddocument : Uploaddocuments) 
    	  {  
    		Thread.sleep(2000);
			List<WebElement> Uploaddocuments1 = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/div/a"));
  			System.out.println(Uploaddocuments1.size());
   			System.out.println(Uploaddocuments1.get(j).getText());
   			scrolltoElement(driver, Uploaddocuments1.get(j));
    			Actions actions = new Actions(driver);        			
    	    	actions.moveToElement(Uploaddocuments1.get(j));
    	    	actions.doubleClick().build().perform();
    	    	Thread.sleep(2000);
    	    	WebElement DocumentName =  driver.findElement(By.xpath("//div[@id='divAccordionbody"+i+"']//tbody//tr[1]/td[1]//a"));
    	    	Actions actions1 = new Actions(driver);
      	    	actions1.moveToElement(DocumentName);
      	    	actions1.doubleClick().build().perform();
      	    	wait.until(ExpectedConditions.elementToBeClickable(DocumentName));
      	    	i = i+ 1;
      	    	j = j+1;
  	    	   	if(j== Uploaddocuments1.size()+1)
      	    	{
      	    		break;
      	    	}
    	  }
	}
	
		catch(Exception e)
		{
			System.out.println(" The document is not downloaded properly");
		}
		return downloadtab;
	}

	public int getnumberoffilesdowload() {
		int nooffiles = 0;
	try{
		String filePath = System.getProperty("user.dir") + "\\download\\";
		nooffiles = new File(filePath).listFiles().length;
	}
	catch(Exception e)
	{
		System.out.println("The download folder document is not counted properly ");
	}
		return nooffiles;
	}

	public void AllDocumentLoaded(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, PCSEchecktab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

	/*public List<WebElement> getuploaddocuments()
	{
	List<WebElement> uploaddocuments = null;
	{
		try{
			Uploaddocument ObjUploaddocument = new Uploaddocument(getDriver());
			driver = ObjUploaddocument.getDriver();
	     	new Uploaddocument(driver);
	     	Uploaddocument ObjUploaddocument = new Uploaddocument(getDriver());
			uploaddocuments = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/h5/a"));
    			System.out.println(uploaddocuments.size());
		}
		catch(Exception e)
		{
			System.out.println("The Uploaddocuments tab is not captured");
		}
	}
	return uploaddocuments;
	}
	
	public Uploaddocument AddedCeaseofficerNotes(List<WebElement>uploaddocuments) {
		try{
			Thread.sleep(1000);
			int i = 1;
			int j = 0;
			  List<WebElement> Uploaddocuments1 = driver.findElements(By.xpath("//*[@class='row panel-heading upload-docs-panel-heading margin-top-0']/h5/a"));
  			System.out.println(Uploaddocuments1.size());
			for(j=0;j<uploaddocuments.size()+1;j++)
			{
			
       			scrolltoElement(driver, uploaddocuments.get(j));
        			Actions actions = new Actions(driver);        			
        	    	actions.moveToElement(uploaddocuments.get(j));
        	    	actions.doubleClick().build().perform();
         		  
        	    	WebElement AddNotes =  driver.findElement(By.xpath("//div[@id='divAccordionbody"+i+"']//tbody//tr[1]/td[3]"));
        			wait.until(ExpectedConditions.elementToBeClickable(AddNotes));
        			scrolltoElement(driver, AddNotes);
          			Actions actions1 = new Actions(driver);
          	    	actions1.moveToElement(AddNotes);
          	    	actions1.doubleClick().build().perform();
          	    	wait.until(ExpectedConditions.elementToBeClickable(CaseOfficerNotesWindow));
          	    	if(CaseOfficerNotesWindow.isDisplayed())
          	    	{
          	    		wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).clear();
          	    		wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).sendKeys("Automation Purpose");
          	    		Actions actions2 = new Actions(driver);
              	    	actions2.moveToElement(Saveondialogue);
              	    	actions2.doubleClick().build().perform(); 
              	    	
              	       	i = i+ 1;
              	       	new Uploaddocument(driver);
              	       	//driver = this.driver;
              	                	    	             	                 	    	
          	    	}
          	    	
        	    
          	    	}       	    	
        		  
        	  
			
		}
		catch(Exception e)
		{
			System.out.println("The all document is not downloaded");
		}
		
		return new Uploaddocument(driver);
	}*/
	
}
