package pageobjects.ProcessPL;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import pageobjects.LoginScreen;
public class ThirdPartyCheck extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	String DocumentTypeExcel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "ThirdPartyCheck", "DocumentType",1);
	
	 @FindBy(partialLinkText="Log out")
	    WebElement Logout;

	@FindBy(id="Note")
	WebElement OfficerNotes;
	
	@FindBy(id="uploadDoc")
	WebElement ChooseFile;
	
	@FindBy(id="browseBtn")
	WebElement Uploadbutton;
	
/*	@FindBy(xpath="//*[@id='dvUploadNewDocument']/div[4]/div[2]/input")
	WebElement AddDocument;*/
	
	@FindBy(xpath="//input[@data-target-id='dvUploadDocuments']")
	WebElement AddDocument;
	

	@FindBy(id="IsThirdPartyChecksComplete")
	WebElement Thirdpartycheck;	
	
	@FindBy(id="DocumentType")
	WebElement DocumentDropdown;
	
	@FindBy(xpath="//input[@name='Save']")
	WebElement SaveButton;
	
	
	@FindBy(id="NetTeamAppointment")
	WebElement NetTeamCheck;

	
	
	public ThirdPartyCheck(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	
	public ThirdPartyCheck FillThirdPartyCheck() throws InterruptedException {
		try{
		Thread.sleep(2000);
		scrolltoElement(driver, DocumentDropdown);
		List<String> options = helpers.CommonFunctions.getAllOptions(DocumentDropdown);
		//System.out.println(options);
		  for (String option : options) 
		  { 
			  System.out.println(option);
			  if(!option.equalsIgnoreCase("Please Select"))
			  {
				scrolltoElement(driver, DocumentDropdown);
				System.out.println(option);
				Select dropdown = new Select(DocumentDropdown);
				dropdown.selectByVisibleText(option);
				wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(OfficerNotes)).sendKeys("Test");
			
			/*	Actions actions = new Actions(driver);
		    	actions.moveToElement(ChooseFile);
		    	actions.doubleClick().build().perform();*/
				/*scrolltoElement(driver, ChooseFile);
				wait.until(ExpectedConditions.elementToBeClickable(ChooseFile)).clear();
				Thread.sleep(2000);
				String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";				
				helpers.CommonFunctions.Uploadfile(filePath, driver);
				Thread.sleep(1000);*/
				
				
				Thread.sleep(2000);
    			scrolltoElement(driver, ChooseFile);
    			JavascriptExecutor jse = (JavascriptExecutor)driver; 
    			jse.executeScript("arguments[0].scrollIntoView();", ChooseFile);
    			jse.executeScript("arguments[0].click();", ChooseFile);
    			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";	
    			ChooseFile.sendKeys(filePath);
				Actions actions2 = new Actions(driver);
		    	actions2.moveToElement(Uploadbutton);
		    	actions2.doubleClick().build().perform();
		    
		    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
				Actions actions1 = new Actions(driver);
		    	actions1.moveToElement(AddDocument);
		    	actions1.doubleClick().build().perform();
		    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
		//		wait.until(ExpectedConditions.elementToBeClickable(AddDocument)).click();
				
			  }
		  }
		}
		catch(Exception e)
		{
		System.out.println("The Third party is not filled properly");
		}
		// TODO Auto-generated method stub
		return new ThirdPartyCheck(driver);
	}
	public boolean verifyThirdpartyCheck() {
		boolean Thirdpartycheckflag = false;
		try{
			Thread.sleep(2000);
			if(Thirdpartycheck.isEnabled())
			{
				Thirdpartycheckflag = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Third party is not captured");
		}
		return Thirdpartycheckflag;
	}
	public boolean verifyselectoptions() {
		boolean ScoNIwales = false;
		try
		{
			scrolltoElement(driver, DocumentDropdown);
			List<String> options = helpers.CommonFunctions.getAllOptions(DocumentDropdown);
			 for (String option : options) 
			  { 
				 if(option.equalsIgnoreCase(DocumentTypeExcel))
				 {
					 ScoNIwales = true;
				 }
			  }
		}
		catch(Exception e)
		{
			System.out.println("The document drop down is not captured");
		}
		return ScoNIwales;
	}
	public int verifydateThirdpartycheck() {
		int count = 0;
		try{
		  List<WebElement> TodoList = driver.findElements(By.xpath("//div[@class='row panel-heading upload-docs-panel-heading margin-top-0']/h5/a"));
		  System.out.println(TodoList.size());
		  int i = 1;
          for (WebElement Todo : TodoList) 
          {  
        	scrolltoElement(driver, Todo);
        	Actions actions = new Actions(driver);
  	    	actions.moveToElement(Todo);
  	    	actions.doubleClick().build().perform();
  	    	Thread.sleep(3000);
  	    	String date = driver.findElement(By.xpath(" //*[@id='divAccordionbody"+i+"']/div/div/table/tbody/tr/td[3]/p")).getText();
  	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  	    	String today = sdf.format(new Date()); 
  	    //	System.out.println(today);
  	    	i = i+1;
  	    	if(date.equalsIgnoreCase(today))
  	    	{
  	    		count =1;
  	    	}
  	      	   
  	    	
          }
		}
		catch(Exception e)
		{
			System.out.println("The Date verififcation is not done on Third party check");
		}
		return count;
	}

	public ThirdPartyCheck clickonThirdPartycheck() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Thirdpartycheck);
			wait.until(ExpectedConditions.elementToBeClickable(Thirdpartycheck));
			Thread.sleep(2000);
		/*	Actions actions = new Actions(driver);
	    	actions.moveToElement(Thirdpartycheck);
	    	actions.doubleClick().build().perform();*/
			wait.until(ExpectedConditions.elementToBeClickable(Thirdpartycheck)).click();
			Actions actions = new Actions(driver);
	    	actions.moveToElement(SaveButton);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
			//wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			
		}
		catch(Exception e)
		{
			System.out.println("The Third party clicked is not happen");
		}
		return new ThirdPartyCheck(driver);
	}
	
	public NetTeamCheck clickonnetTeamcheck() {
		try{
			
			scrolltoElement(driver, NetTeamCheck);
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(NetTeamCheck);
	    	actions.doubleClick().build().perform();
			//wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck)).click();	
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not clicked");
		}
		return new NetTeamCheck(driver);
	}
	
	public LoginScreen logout()
	{
		wait.until(ExpectedConditions.elementToBeClickable(Logout)).click();
		return new LoginScreen(driver);
	}

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, AddDocument);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

}
