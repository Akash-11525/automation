package pageobjects.ProcessPL;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import pageobjects.LoginScreen;


public class PCSECheck extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="PCSEChecks")
	WebElement PCSEchecktab;

	@FindBy(xpath="//input[@name='IsApplicationComplete']")
	WebElement StatusRadio;	
	
	@FindBy(xpath="//div[@class='col-sm-12 col-xs-12']//fieldset//div[2]/label/input")
	WebElement ApplicationIncomplete;	
	
	@FindBy(id="ReturnReason")
	WebElement ReturnReasonText;
	
	@FindBy(id="btnRtntoApplicant")
	WebElement ReturnApplicant;
	
	
	@FindBy(id="CaseOfficerName")
	WebElement CaseofficerName;
	
	@FindBy(name="Assign Case Officer")
	WebElement AssignCaseOfficerName;	
	
	//@FindBy(xpath="//*[@id='divMainContainer']/div[2]/div/div[2]/div[1]/div[2]/strong")
	@FindBy(xpath="//div[@id='divApplicantDetails']/div/div[2]/div/div[1]/Strong")
	WebElement ApplicationStatus;
		

	@FindBy(id="ThirdPartyChecks")
	WebElement ThirdPartyCheck;
	
	@FindBy(xpath="//*[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement ContinueConfirmation;
	
	@FindBy(css="input[id='IsUpdRefInfoReq']")
	WebElement UpdateRefreeCheckBox;
	
	@FindBy(css="div[id='divIsUpdRefInfoReq']")
	WebElement updateRefreebox;
	
	
	@FindBy(xpath = "//input[@id='IsRefInfoComplete']")
	WebElement RefInfoComplete;
	
	@FindBy(xpath = "//input[@id='IsUpdRefInfoReq']")
	WebElement UpdateRefreeCheckBox1;
	
	@FindBy(name="IsRefInfoComplete")
	WebElement RefCheckBox;
	
	//@FindBy(xpath="//*[@id='divEditReferees']/div/input")
	@FindBy(xpath="//input[@value='Edit Referees']")
	WebElement EditRefreebutton;
	
	@FindBy(xpath="//*[@name='FirstReferee']")
	WebElement Choosereferee1;
	
	@FindBy(id="browseBtn")
	WebElement Upload1;
	
	@FindBy(xpath="//*[@id='accordion']/div[2]/div[1]/h4/a")
	WebElement Referee2;
	
	@FindBy(xpath="//*[@name='UploadSecondReferee']")
	WebElement Choosereferee2;
	
	@FindBy(xpath="//button[@data-target-id='tblUploadSecondReferee']")
	WebElement Upload2;
	
	
	@FindBy(name="btnSave")
	WebElement SaveButtonReferee;
	
	
	@FindBy(name="Save")
	WebElement Savebutton;
	

	@FindBy(id="LocalOfficeName")
	WebElement Localoffice;
	
	
	@FindBy(name="CaseOfficerReview")
	WebElement Caseofficereviewtab;
	
	  @FindBy(partialLinkText="Log out")
	    WebElement Logout;
	
	
	String OfficerName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "PCSECheck", "OfficerName",1);
	String LocalofficeExcel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "PCSECheck", "LocalOffice",1);
	String LocalOffice = testdata.ConfigurationData.localOffice;
	public PCSECheck(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}


	
	public PCSECheck selectCaseofficerAppComplete() {
		try{
					Thread.sleep(1000);
					scrolltoElement(driver, StatusRadio);
					wait.until(ExpectedConditions.elementToBeClickable(StatusRadio)).click();
					Actions actions = new Actions(driver);
			    	actions.moveToElement(StatusRadio);
			    	actions.doubleClick().build().perform();
			    	Select dropdown = new Select(CaseofficerName);
					dropdown.selectByVisibleText(OfficerName);	
					Actions actions1 = new Actions(driver);
			    	actions1.moveToElement(AssignCaseOfficerName);
			    	actions1.doubleClick().build().perform();
			    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			
					
		}
		catch(Exception e)
		{
			System.out.println("The PCSECheck is not clicked");
		}
		return new PCSECheck(driver);
	}
	
	public String verifyApplicationStatus() {
		String AppStatus = null;
		try{
		helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus));
		AppStatus =wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus)).getText();
			
		}
		catch(Exception e)
		{
			System.out.println("The Application Status is not captured");
		}


		return AppStatus;
	}
	
	
	public ThirdPartyCheck clickonThirdParty() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(ThirdPartyCheck));
			scrolltoElement(driver, ThirdPartyCheck);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(ThirdPartyCheck);
	    	actions.doubleClick().build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(ThirdPartyCheck));	
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not clicked");
		}
		return new ThirdPartyCheck(driver);
	}



	public PCSECheck selectCaseofficerAppInComplete()
	{
	try
	{
		Thread.sleep(3000);
		Actions actions = new Actions(driver);
    	actions.moveToElement(ApplicationIncomplete);
    	actions.doubleClick().build().perform();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(ReturnReasonText)).sendKeys("Automation Purpose");
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(ReturnApplicant);
    	actions1.click().build().perform();
    	Thread.sleep(2000);
    	//wait.until(ExpectedConditions.elementToBeClickable(ContinueConfirmation)).click();
    	Actions actions2 = new Actions(driver);
    	actions2.moveToElement(ContinueConfirmation);
    	actions2.doubleClick().build().perform();
    	
		//wait.until(ExpectedConditions.elementToBeClickable(AssignCaseOfficerName)).click();	
		driver.navigate().refresh();
		helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
	}
	catch(Exception e)
	{
		System.out.println("The PCSECheck is not clicked");
	}
	return new PCSECheck(driver);
	}



	public PCSECheck ClickonEditRef() {
	try{
		Thread.sleep(4000);
		wait.until(ExpectedConditions.elementToBeClickable(UpdateRefreeCheckBox));
		scrolltoElement(driver, UpdateRefreeCheckBox);
		Actions actions = new Actions(driver);
    	actions.moveToElement(updateRefreebox);
    	actions.click().build().perform();
    	Thread.sleep(2000);
		//updateRefreebox.click();
	//	UpdateRefreeCheckBox.click();
		//wait.until(ExpectedConditions.elementToBeClickable(UpdateRefreeCheckBox)).click();
		/*Actions actions7 = new Actions(driver);
    	actions7.moveToElement(UpdateRefreeCheckBox);
    	actions7.doubleClick().build().perform();*/
    //	wait.until(ExpectedConditions.elementToBeClickable(UpdateRefreeCheckBox)).click();
	/*	scrolltoElement(driver, UpdateRefreeCheckBox1);
		UpdateRefreeCheckBox1.click();
		UpdateRefreeCheckBox1.click();*/
	//	UpdateRefreeCheckBox1.click();
		scrolltoElement(driver, EditRefreebutton);
	//	helpers.CommonFunctions.ClickOnCheckBox(driver, "Update to referee information requested");
		wait.until(ExpectedConditions.elementToBeClickable(EditRefreebutton)).click();
	 	/*Actions actions = new Actions(driver);
    	actions.moveToElement(EditRefreebutton);
    	actions.doubleClick().build().perform();*/
    	helpers.CommonFunctions.PageLoadExternalwait(driver);
    	List<WebElement> Refreebuttons=driver.findElements(By.xpath("//div[@id='dvProcessAppContainer']//div[@class='panel panel-default']"));
        System.out.println("total Tabs "+Refreebuttons.size());
        for (WebElement Refreebutton : Refreebuttons) 
        {
        	scrolltoElement(driver, Refreebutton);
        	System.out.println(Refreebutton.findElement(By.tagName("a")).getText());
        	if(Refreebutton.findElement(By.tagName("a")).getText().equalsIgnoreCase("Referee 1"))
        	{
        		
        		Actions actions1 = new Actions(driver);
            	actions1.moveToElement(Refreebutton.findElement(By.tagName("a")));
            	actions1.doubleClick().build().perform();
           /* 	wait.until(ExpectedConditions.elementToBeClickable(Choosereferee1));
            	scrolltoElement(driver, Choosereferee1);       
            	wait.until(ExpectedConditions.elementToBeClickable(Choosereferee1)).clear();
		    	Thread.sleep(2000);
				String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";				
				helpers.CommonFunctions.Uploadfile(filePath, driver);
				Thread.sleep(3000);*/
            	
            	
            	Thread.sleep(1000);
    			scrolltoElement(driver, Choosereferee1);
    			JavascriptExecutor jse = (JavascriptExecutor)driver; 
    			jse.executeScript("arguments[0].scrollIntoView();", Choosereferee1);
    			jse.executeScript("arguments[0].click();", Choosereferee1);
    			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
    			//helpers.CommonFunctions.Uploadfile(filePath, driver);
    			Choosereferee1.sendKeys(filePath);
    		//	wait.until(ExpectedConditions.elementToBeClickable(Choosereferee1)).click();
				
				
			    /* 	Actions actions2 = new Actions(driver);
		    	actions2.moveToElement(Choosereferee1);
		    	actions2.doubleClick().build().perform();*/
				
				
    			/*	Actions actions4 = new Actions(driver);
            	actions4.moveToElement(Choosereferee2);
            	actions4.doubleClick().build().perform();*/
    			Actions actions8 = new Actions(driver);
            	actions8.moveToElement(Upload1);
            	actions8.doubleClick().build().perform();
			//	wait.until(ExpectedConditions.elementToBeClickable(Upload1)).click();
            	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
				
        	}
        	else
        	{
				//scrolltoElement(driver, Referee2);
        		Actions actions3 = new Actions(driver);
            	actions3.moveToElement(Refreebutton.findElement(By.tagName("a")));
            	actions3.doubleClick().build().perform();
            	
            /*	wait.until(ExpectedConditions.elementToBeClickable(Choosereferee2));
            	scrolltoElement(driver, Choosereferee2);        
            	wait.until(ExpectedConditions.elementToBeClickable(Choosereferee2)).clear();
            	Thread.sleep(2000);
				String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.pdf";				
				helpers.CommonFunctions.Uploadfile(filePath1, driver);
				Thread.sleep(3000);*/
				
             	Thread.sleep(2000);
    			scrolltoElement(driver, Choosereferee2);
    			JavascriptExecutor jse1 = (JavascriptExecutor)driver; 
    			jse1.executeScript("arguments[0].scrollIntoView();", Choosereferee2);
    			jse1.executeScript("arguments[0].click();", Choosereferee2);
    			String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
    			//helpers.CommonFunctions.Uploadfile(filePath, driver);
    			Choosereferee2.sendKeys(filePath1);
    			//wait.until(ExpectedConditions.elementToBeClickable(Choosereferee2)).click();
    			
    			
				Actions actions9 = new Actions(driver);
            	actions9.moveToElement(Upload2);
            	actions9.doubleClick().build().perform();
			//	wait.until(ExpectedConditions.elementToBeClickable(Upload2)).click();
            	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
        	}
        }
        		//helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
        		wait.until(ExpectedConditions.elementToBeClickable(SaveButtonReferee)).click();
				scrolltoElement(driver, SaveButtonReferee);
				Actions actions5 = new Actions(driver);
            	actions5.moveToElement(SaveButtonReferee);
            	actions5.doubleClick().build().perform();
            	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
				
				
				
            	
				
        	

        
	}
	
	catch(Exception e)
	{
		System.out.println("The Edit Refree on PCSE chekc is not done");
	}
		return new PCSECheck(driver);
	}



	public PCSECheck clickonPCSECheck() {
		try 
		{
			scrolltoElement(driver, PCSEchecktab);		
			Actions actions = new Actions(driver);
        	actions.moveToElement(PCSEchecktab);
        	actions.doubleClick().build().perform();
        	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The PCSE check is not happen");
		}
		return new PCSECheck(driver);

	}



	public PCSECheck clickonRefinfocomplete(String environment) {
		try{
			scrolltoElement(driver, RefInfoComplete);
			wait.until(ExpectedConditions.elementToBeClickable(RefInfoComplete)).click();
		/*	Actions actions = new Actions(driver);
	    	actions.moveToElement(RefInfoComplete);
	    	actions.click().build().perform();*/
			scrolltoElement(driver, Localoffice);
			wait.until(ExpectedConditions.elementToBeClickable(Localoffice));
			System.out.println(LocalofficeExcel);
			String ProcessLocaloffice = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
	    	Select dropdown = new Select(Localoffice);
	    	dropdown.selectByVisibleText(ProcessLocaloffice);
	    //	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
	    	//dropdown.selectByVisibleText(LocalofficeExcel);	
	    	scrolltoElement(driver, Savebutton);
			wait.until(ExpectedConditions.elementToBeClickable(Savebutton));	
	    	scrolltoElement(driver, Savebutton);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Savebutton);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
	
	    	
			
		}
		catch(Exception e)
		{
			System.out.println("The Referee information is not clicked");
		}
		return new PCSECheck(driver);
	}



	public void Applicationstatus(String note) throws InterruptedException, IOException {
		
		scrolltoElement(driver, ApplicationStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		/*scrolltoElement(driver, PrescriptionField);
		Screenshot.TakeSnap(driver, note+"_2");*/	
		

	}



	public void screenshotAppStatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, PCSEchecktab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	public LoginScreen logout() throws InterruptedException
	{
	/*	boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent)
		{
			Thread.sleep(2000);
			ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}*/
		wait.until(ExpectedConditions.elementToBeClickable(Logout)).click();
		return new LoginScreen(driver);
	}
	
	public CaseofficerReview clickonCaseofficerreview() {
		try{
		//	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			scrolltoElement(driver, Caseofficereviewtab);
			wait.until(ExpectedConditions.elementToBeClickable(Caseofficereviewtab));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(Caseofficereviewtab);
	    	actions.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The case officer review is not clicked");
		}
		// TODO Auto-generated method stub
		return new CaseofficerReview(driver);
	}
	
}
