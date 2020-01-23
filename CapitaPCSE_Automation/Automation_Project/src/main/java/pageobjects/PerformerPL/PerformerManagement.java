package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
public class PerformerManagement extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="GMCGDCGOCNumber")
	WebElement GMCField;
	
	@FindBy(name="Search")
	WebElement Search;	
	
	@FindBy(xpath ="//table[@class='table PCSE-WrapTable margin-bottom-15']")
	WebElement ResultGMCtable;
	
	@FindBy(xpath ="//*[@id='DivContainer']/div[4]/div/div/div/strong")
	WebElement PerformerStatus_Performer;
	
	@FindBy(id="btnSuspendPerformer")
	WebElement SuspendPerformerButton;
	
	@FindBy(id="btnVaryConditions")
	WebElement VaryConButton;
	
	@FindBy(id="btnRemovePerformer")
	WebElement RemovePerformerButton;
	
	@FindBy(id="SuspendPerformerComments")
	WebElement SuspendComment;
	
	@FindBy(id="SuspendPerformerDecisionDate")
	WebElement Suspenddate;
	
	@FindBy(id="uploadDeclaration_SuspendPerformer")
	WebElement SuspendUpload;	
	
	@FindBy(id="browseBtn")
	WebElement uploadButton;
	
	@FindBy(xpath ="//*[@id='SuspendPerformer']/form/div[5]/div[2]/input")
	WebElement Confirm_Suspend;
	
	@FindBy(id="btnConfirming/VaryingSuspension")
	WebElement ConfirmingSuspension;
	
	@FindBy(id="ConfirmVaryPerformerComments")
	WebElement ConfirmVaryComment;
	
	@FindBy(id="ConfirmVaryPerformerDecisionDate")
	WebElement ConfirmVarydate;
	
	@FindBy(id="uploadDeclaration_ConfirmVaryPerformer")
	WebElement ConfirmVaryUpload;	

	@FindBy(xpath ="//*[@id='ConfirmVaryPerformer']/form/div[5]/div[2]/input")
	WebElement ConfirmVary_Suspend;
	
	@FindBy(id="RemovePerformerComments")
	WebElement RemoveComment;
	
	@FindBy(id="RemovePerformerDecisionDate")
	WebElement Removedate;
	
	@FindBy(id="uploadDeclaration_RemovePerformer")
	WebElement RemoveUpload;	

	@FindBy(xpath ="//*[@id='RemovePerformer']/form/div[5]/div[2]/input")
	WebElement ConfirmRemove;
	
	@FindBy(id="btnReincludePerformer")
	WebElement ReincludeButton;
	
	@FindBy(id="ReincludePerformerComments")
	WebElement ReincludeComment;
	
	@FindBy(id="ReincludePerformerDecisionDate")
	WebElement Reincludedate;
	
	@FindBy(id="uploadDeclaration_ReincludePerformer")
	WebElement ReincludeUpload;	

	@FindBy(xpath ="//*[@id='ReincludePerformer']/form/div[5]/div[2]/input")
	WebElement ConfirmReinclude;
	
	@FindBy(xpath ="//input[@value='Change Performer Local Office']")
	WebElement ChangeLocalofficebutton;	
	
	@FindBy(xpath ="//*[@id='divNHSELo']/div[1]/div/div/div/strong")
	WebElement Localoffice_NHSE;
	
	@FindBy(xpath ="//select[@id='NewLocalOfficeCode']")
	WebElement NewLocalofficeselect;
	
	@FindBy(id ="btnConfirm")
	WebElement ConfirmButton;
	
	@FindBy(id ="LocalOfficeEndDate")
	WebElement LocaloffEndDate;
	
	@FindBy(id ="Comments")
	WebElement OldLocalComment;
	
	@FindBy(id ="IsFurtherDiscussionNotesRequired")
	WebElement FurtherDiscussion;
	
	@FindBy(id ="FurtherDiscussionNotes")
	WebElement FurtherDiscussionNotes;
	
	
	@FindBy(id ="LeavingOfficeContactName")
	WebElement LeavingOfficeContactName;
	
	@FindBy(id ="LeavingOfficeContactNumber")
	WebElement LeavingOfficeContactNumber;
	
	@FindBy(id ="LeavingOfficeContactEmail")
	WebElement LeavingOfficeContactEmail;
	
	@FindBy(id ="btnSubmitChangePerformer")
	WebElement ChangeLocalOfficeButton;	

	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(id ="DeathDecisionDate")
	WebElement DeathDecisionDate;
	
	@FindBy(id ="btnDeathDate")
	WebElement btnDeathDate;	

	@FindBy(xpath="//span[@class='field-validation-error']")
	WebElement ActualErrorDeath;
	
	@FindBy(name ="PracticeCode")
	WebElement Practicecode;
	
	@FindBy(id ="BtnSearchCounAdd")
	WebElement Searchpracticecode;
	
	@FindBy(id ="BtnPostCodeSearch")
	WebElement Postalcodesearch;
	
	@FindBy(xpath ="//ul[@id='PracticeAddressResult']//li[1]/a")
	WebElement FirstRowResult;
		
	@FindBy(xpath="//table[@class='table PCSE-WrapTable margin-bottom-15']//tbody")
	WebElement taskTableBody;
	
	
	
	
	
	
	public PerformerManagement(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	
	public PerformerManagement FillGMCnumber(String gMCNumber) {
	try{
		wait.until(ExpectedConditions.elementToBeClickable(GMCField)).sendKeys(gMCNumber);
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(Search);
    	actions1.doubleClick().build().perform();
    	helpers.Support.PageLoadExternalwait_Performer(driver);
	}
	catch(Exception e)
	{
		System.out.println("The GMC field is not filled properly");
	}
		return new PerformerManagement(driver);
	}

	public PerformerManagement clickonEditPerformer() {
		try{
				Thread.sleep(1000);
				WebElement Refperfommer = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tbody/tr[1]/td[5]/input"));
				scrolltoElement(driver, Refperfommer);
				String ButtonText = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tbody/tr[1]/td[5]/input")).getAttribute("value");
				if(ButtonText.equalsIgnoreCase("Edit Performer Record"))
				{
					Refperfommer.click();
					helpers.Support.PageLoadExternalwait_Performer(driver);
				}
			
					
		}
		catch(Exception e)
		{
			System.out.println("The Edit Performer Record is not clicked");
		}
		return new PerformerManagement(driver);
	}

	public Boolean verifyperformerstatus(String ExpectedValue) {
		boolean Performerstatus = false;
		try
		{
			helpers.Support.PageLoadExternalwait_Performer(driver);
			scrolltoElement(driver, PerformerStatus_Performer);
			
		String PerformerStatus_Portal = wait.until(ExpectedConditions.elementToBeClickable(PerformerStatus_Performer)).getText();
		if(PerformerStatus_Portal.equalsIgnoreCase(ExpectedValue))
		{
			Performerstatus = true;
		}
		}
		catch(Exception e)
		{
			System.out.println("The Performer status is not captured");
		}
		
		return Performerstatus;
	}

	public PerformerManagement clickonSuspendPerformer() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, SuspendPerformerButton);
			Actions actions = new Actions(driver);
			actions.moveToElement(SuspendPerformerButton);
	    	actions.doubleClick().build().perform();
	    //	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.err.println("The Suspent Performer is not clicked"); 
			
		}
		return new PerformerManagement(driver) ;
	}

	public PerformerManagement FillSuspendPerformer() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, SuspendComment);
			wait.until(ExpectedConditions.elementToBeClickable(SuspendComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(SuspendComment)).sendKeys("Automation Purpose");
			String todayDate = CommonFunctions.getDate_UK();
			Suspenddate.sendKeys(todayDate);
			scrolltoElement(driver, SuspendUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", SuspendUpload);
			jse.executeScript("arguments[0].click();", SuspendUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			SuspendUpload.sendKeys(filePath);
			Thread.sleep(2000);
			   List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		Thread.sleep(7000);
	            	}
	            }
        	Thread.sleep(2000);
        	Actions actions = new Actions(driver);
        	actions.moveToElement(Confirm_Suspend);
        	actions.doubleClick().build().perform();
        	helpers.Support.PageLoadExternalwait_Performer(driver);
        	
		}
		catch(Exception e)
		{
			System.out.println("The Suspend Performer is not filled properly");
		}
		return new PerformerManagement(driver) ;
	}

	public PerformerManagement clickonConfirmingSuspension() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, ConfirmingSuspension);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmingSuspension);
	    	actions.doubleClick().build().perform();
	    //	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.err.println("The Confirming Suspension is not clicked"); 
			
		}
		return new PerformerManagement(driver);
	}

	public PerformerManagement FillConfirmingSuspension() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ConfirmVaryComment);
			wait.until(ExpectedConditions.elementToBeClickable(ConfirmVaryComment)).sendKeys("Automation Purpose");
			String todayDate = CommonFunctions.getDate_UK();
			ConfirmVarydate.sendKeys(todayDate);
			scrolltoElement(driver, ConfirmVaryUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", ConfirmVaryUpload);
			jse.executeScript("arguments[0].click();", ConfirmVaryUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			ConfirmVaryUpload.sendKeys(filePath);
			Thread.sleep(1000);
			   List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		helpers.Support.PageLoadExternalwait_Performer(driver);
	            	}
	            }
	            Thread.sleep(2000);
        	Actions actions = new Actions(driver);
        	actions.moveToElement(ConfirmVary_Suspend);
        	actions.doubleClick().build().perform();
        	helpers.Support.PageLoadExternalwait_Performer(driver);
        	
		}
		catch(Exception e)
		{
			System.out.println("The Suspend Performer is not filled properly");
		}
		return new PerformerManagement(driver) ;
	}

	public PerformerManagement clickonRemovePerformer() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, RemovePerformerButton);
			Actions actions = new Actions(driver);
			actions.moveToElement(RemovePerformerButton);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(7000);
		}
		catch(Exception e)
		{
			System.err.println("The RemovePerformer Button  is not clicked"); 
			
		}
		return new PerformerManagement(driver);
	}

	public PerformerManagement fillRemovePerformer() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, RemoveComment);
			wait.until(ExpectedConditions.elementToBeClickable(RemoveComment)).sendKeys("Automation Purpose");
			String todayDate = CommonFunctions.getDate_UK();
			Removedate.sendKeys(todayDate);
			scrolltoElement(driver, RemoveUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", RemoveUpload);
			jse.executeScript("arguments[0].click();", RemoveUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			RemoveUpload.sendKeys(filePath);
			Thread.sleep(1000);
			   List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		Thread.sleep(7000);
	            	}
	            }
	        	Thread.sleep(2000);
        	Actions actions = new Actions(driver);
        	actions.moveToElement(ConfirmRemove);
        	actions.doubleClick().build().perform();
        	helpers.Support.PageLoadExternalwait_Performer(driver);
        	
		}
		catch(Exception e)
		{
			System.out.println("The Remove Performer is not filled properly");
		}
		return new PerformerManagement(driver) ;
	}

	public PerformerManagement clickonReincludePef() {
		try{
		scrolltoElement(driver, ReincludeButton);
		Actions actions = new Actions(driver);
		actions.moveToElement(ReincludeButton);
    	actions.doubleClick().build().perform();
    //	helpers.Support.PageLoadExternalwait_Performer(driver);
	}
	catch(Exception e)
	{
		System.err.println("The RemovePerformer Button  is not clicked"); 
		
	}
	return new PerformerManagement(driver);
	}

	public PerformerManagement fillReinclude() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ReincludeComment);
			wait.until(ExpectedConditions.elementToBeClickable(ReincludeComment)).sendKeys("Automation Purpose");
			String todayDate = CommonFunctions.getDate_UK();
			Reincludedate.sendKeys(todayDate);
			scrolltoElement(driver, ReincludeUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", ReincludeUpload);
			jse.executeScript("arguments[0].click();", ReincludeUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			ReincludeUpload.sendKeys(filePath);
			Thread.sleep(1000);
			   List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		Thread.sleep(7000);
	            	}
	            }
	        Thread.sleep(2000);
	        
        	Actions actions = new Actions(driver);
        	actions.moveToElement(ConfirmReinclude);
        	actions.doubleClick().build().perform();
        	helpers.Support.PageLoadExternalwait_Performer(driver);
        	
		}
		catch(Exception e)
		{
			System.out.println("The Reinclude Performer is not filled properly");
		}
		return new PerformerManagement(driver) ;
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, PerformerStatus_Performer);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	

		
	}
	
	public void Screenshots_Postalcode (String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Postalcodesearch);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	

		
	}

	public String getLocalOffice() {
		String CorrectLocaloffice = null;
				try{
				helpers.Support.PageLoadExternalwait_Performer(driver);
					CorrectLocaloffice = wait.until(ExpectedConditions.elementToBeClickable(Localoffice_NHSE)).getText();
					
				}
		catch(Exception e)
		{
			System.out.println("The Local office name is not captured");
		}
		return CorrectLocaloffice ;
	}

	public PerformerManagement clickonchangeLocaloffice() {
		try{
			helpers.Support.PageLoadExternalwait_Performer(driver);
			Actions actions = new Actions(driver);
        	actions.moveToElement(ChangeLocalofficebutton);
        	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The changed Local office button is not clicked");
		}
		return new PerformerManagement(driver) ;
	}

	public PerformerManagement AddNewLocaloffice(String expectedupdatedLocaloffice) {
		try{
			Thread.sleep(2000);
			Select dropdown = new Select(NewLocalofficeselect);
			dropdown.selectByVisibleText(expectedupdatedLocaloffice);
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
        	actions.moveToElement(ConfirmButton);
        	actions.doubleClick().build().perform();
        	Thread.sleep(1000);
        	String Date = helpers.CommonFunctions.getDate_UK();
        	String EndDate = helpers.CommonFunctions.Tomorrowdate(Date, 5);
        	wait.until(ExpectedConditions.elementToBeClickable(LocaloffEndDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LocaloffEndDate)).sendKeys(EndDate);
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(OldLocalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(OldLocalComment)).sendKeys("Automation Purpose");
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(FurtherDiscussion)).click();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(FurtherDiscussionNotes)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FurtherDiscussionNotes)).sendKeys("AutomationPurpose");
			Thread.sleep(500);
			
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactName)).sendKeys("AutomationName");
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactNumber)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactNumber)).sendKeys("1234567890");
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactEmail)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LeavingOfficeContactEmail)).sendKeys("test@test.com");
			Thread.sleep(500);
		}
		catch(Exception e)
		{
			System.out.println("The new updated local office is not added on NHSE");
		}
		return new PerformerManagement(driver);
	}

	public PerformerManagement clickonSubmitLocaloffice() {
		try{
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
        	actions.moveToElement(ChangeLocalOfficeButton);
        	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Submit button Local office change is not clicked");
		}
		return new PerformerManagement(driver);
	}
	
	public PerformerManagement clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Ok button on Submit is not captured");
		}
		return new PerformerManagement (driver);
	}

	public Boolean verifyChangeLocaloffice() {
		boolean changelocaloffice = false;
		try{
		boolean ispresent1 = driver.findElements(By.xpath("//input[@value='Change Performer Local Office']")).size() != 0;
		if(ispresent1)
		{
			changelocaloffice = true;
		}
		}
		catch(Exception e)
		{
			System.out.println("The Confirm button is not captured");
		}
		
		return changelocaloffice;
	}

	public PerformerManagement RecordDeath() {
		try{
			boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			if(ispresent1)
			{
				Thread.sleep(500);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio']/label"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				String RadioValue = Radiobutton.getText(); 
				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase("Yes"))
				{
					Radiobutton.click();
				}
			}
			String Currentdate = helpers.CommonFunctions.getDate_UK();
			String Deathdate = helpers.CommonFunctions.Tomorrowdate(Currentdate, 2);
			wait.until(ExpectedConditions.elementToBeClickable(DeathDecisionDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DeathDecisionDate)).sendKeys(Deathdate);
			Thread.sleep(500);
			
			
		}
		catch(Exception e)
		{
			System.out.println("The Recording Future death is not captured"); 
			
		}
		return new PerformerManagement(driver);
	}

	public PerformerManagement clickonOK_Death() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(btnDeathDate));
			scrolltoElement(driver, btnDeathDate);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(btnDeathDate);
	    	actions1.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Ok button for Death is not clicked");
		}
		return new PerformerManagement(driver);
	}

	public Boolean verifyErrorDeath() {
		boolean ErrorDeath = false;
		try{
			
			Thread.sleep(2000);
			String ActualErrorMessage = wait.until(ExpectedConditions.elementToBeClickable(ActualErrorDeath)).getText();
			String ExpectedErrorMessage = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerList.xlsx", "FutureDeath", "Error",1);
			
			if(ActualErrorMessage.equalsIgnoreCase(ExpectedErrorMessage))
			{
				ErrorDeath = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message for death is not captured");
		}
		return ErrorDeath;
	}

	public void Screenshots_Performer(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Localoffice_NHSE);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public PerformerManagement addpracticecode() {
		// TODO Auto-generated method stub
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Practicecode)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Practicecode)).sendKeys("pl1 1dn");
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Searchpracticecode);
	    	actions1.doubleClick().build().perform();
	    	wait.until(ExpectedConditions.elementToBeClickable(FirstRowResult));
	    	Actions actions = new Actions(driver);
	    	actions.moveToElement(FirstRowResult);
	    	actions.doubleClick().build().perform();
	       	wait.until(ExpectedConditions.elementToBeClickable(Postalcodesearch));
	    	Actions actions2 = new Actions(driver);
	    	actions2.moveToElement(Postalcodesearch);
	    	actions2.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
	    	
			
		}
		catch(Exception e)
		{
			System.out.println("The Practice code is not added sucessfully");
		}
		return new PerformerManagement(driver);
	}

	public boolean verifypresentdataResultPostalcode() {
		boolean presentdata = true;
		String Data = null;
		try{
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> colItems = row.findElements(By.tagName("td"));


				
				for(int j = 0 ; j<colItems.size()-1; j++ )
				{
					Data = row.findElements(By.tagName("td")).get(j).getText();
					System.out.println(Data);
					if(Data == null || Data.isEmpty())
					{
						presentdata = false;
						break outerloop;
					}
					else 
					{
						presentdata = true;
					}
				}
			
			}
		}
			
		
		catch(Exception e)
		{
			System.out.println("The postal code result table some field are blank"); 
			
		}
		return presentdata;
	}
	
	

}
