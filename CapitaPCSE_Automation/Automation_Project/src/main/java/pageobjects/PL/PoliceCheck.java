package pageobjects.PL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class PoliceCheck extends Support {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id="HasSubscribedToDBSUpdateService")
	WebElement DBS_online;
	
	@FindBy(id="PLI_DBS")
	WebElement PoliceCheckTab;
	
	@FindBy(id="DBSApplicationReferenceNumber")
	WebElement DBSRefNumber;
	
	@FindBy(id="HasSubscribedToDBSUpdateService")
	WebElement DBSupdatedservices;
	
	@FindBy(xpath="//*[@id='DivContainer']/form/div[4]/div/label")
	WebElement DBSCertificateText;	
	
	@FindBy(xpath="//*[@id='PoliceCheck']/div[1]/div/span")
	WebElement Abroadfivetext;
	
	@FindBy(id="upload2")
	WebElement Uploadfilebutton2;
	
/*	@FindBy(xpath="//div[@id='PoliceCheck']//div[2]/div/div/label/span")
	WebElement Uploadfilebutton2;*/
	
	
	@FindBy(id="upload1")
	WebElement Uploadfilebutton1;
	
/*	@FindBy(xpath="//*[@id='dvFileControl']/div/div[1]/label/span")
	WebElement Uploadfilebutton1;*/
	
	@FindBy(xpath="//table[@id='tblUploadedDBSCertificate']//tr[1]/td[2]/button")
	WebElement Deletebutton;
	
	@FindBy(xpath="//button[@data-source-id='upload1']")
	WebElement Upload1button;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement Continuebutton;
	
	@FindBy(xpath="//*[@data-target-id='tblUploadedDBSCertificate']")
	WebElement UploadDBSCertificate;
	
	@FindBy(xpath="//*[@data-target-id='tblUploadedEvidenceOfForeignCountryPolicyChecks']")
	WebElement UploadForeignchecks;
	
	@FindBy(xpath ="//*[@id='tblUploadedDBSCertificate']//tbody")
	WebElement UploadtableDBSCheck;
	
	@FindBy(xpath ="//*[@id='tblUploadedEvidenceOfForeignCountryPolicyChecks']//tbody")
	WebElement UploadtableForeign;
	
	

	
	String AppRefNumber_Police = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","PoliceCheckRef","RefNumber", 1);
	String DBSCerText_Police = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","PoliceCheckRef","DBSCerText", 1);
	String ResidentAbroad_Yes_Text_Police = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","PoliceCheckRef","ResidentAbroad_Yes_Text", 1);
	

	public PoliceCheck(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public String EnterPolicecheckdetails() throws InterruptedException, IOException {
		String ActualTablename_PoliceCheck = null;
		try {
			/*scrolltoElement(driver, Uploadfilebutton1);
		  wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton1)).click();
		  Thread.sleep(3000);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			helpers.CommonFunctions.Uploadfile(filePath, driver);
			Thread.sleep(2000);*/
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
    		for(String Extension : Extensions)
    		{
			scrolltoElement(driver, Uploadfilebutton1);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton1);
			//jse.executeScript("arguments[0].click();", Uploadfilebutton1);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton1.sendKeys(filePath);
			 List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		helpers.CommonFunctions.PageLoadExternalwait(driver); 
	            	}
	            }
    		}

			
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton1)).clear();
			
		/*	String filePathFirst = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePathFirst);	
			Runtime.getRuntime().exec(filePathFirst);*/
			
			wait.until(ExpectedConditions.elementToBeClickable(PoliceCheckTab));
			ActualTablename_PoliceCheck = PoliceCheckTab.getAttribute("id");
			System.out.println(AppRefNumber_Police);
			if(DBSRefNumber.isEnabled())
			{
			scrolltoElement(driver, DBSRefNumber);
			wait.until(ExpectedConditions.elementToBeClickable(DBSRefNumber)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DBSRefNumber)).sendKeys(AppRefNumber_Police);
			wait.until(ExpectedConditions.elementToBeClickable(DBSupdatedservices)).click();
			}
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
		/*	scrolltoElement(driver, Uploadfilebutton2);
			wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton2)).click();
			Thread.sleep(2000);
			helpers.CommonFunctions.Uploadfile(filePath, driver);
			Thread.sleep(2000);*/
		//	List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
    		for(String Extension : Extensions)
    		{
			scrolltoElement(driver, Uploadfilebutton2);
			JavascriptExecutor jse1 = (JavascriptExecutor)driver; 
			jse1.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton2);
		//	jse1.executeScript("arguments[0].click();", Uploadfilebutton2);
			String filePath1 = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton2.sendKeys(filePath1);
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
    		}

			
		/*	String filePathsecond = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePathsecond);
			Runtime.getRuntime().exec(filePathsecond);*/
		//	String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			
		
			/*  List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(500);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		helpers.CommonFunctions.PageLoadExternalwait(driver);
	            	}
	            }*/
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PoliceCheck details not filled properly  " +e);
		}
		return ActualTablename_PoliceCheck;
		
	}


	public boolean verifytextmessageonpage() {
		boolean TextMessageonpage = false;
		try{
		String DBScerText = DBSCertificateText.getText();
		if(DBSCerText_Police.equalsIgnoreCase(DBScerText))
		{
			String abroadfive = Abroadfivetext.getText();
			if (abroadfive.equalsIgnoreCase(ResidentAbroad_Yes_Text_Police))
			{
				TextMessageonpage = true;
				
			}
		}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PoliceCheck details not filled properly  " +e);
		}
		return TextMessageonpage;
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(1500);
			scrolltoElement(driver, Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}


	public PoliceCheck deletePdf() {
		try{
			scrolltoElement(driver, Deletebutton);
			wait.until(ExpectedConditions.elementToBeClickable(Deletebutton));
			Actions action = new Actions(driver);
			action.moveToElement(Deletebutton);
			action.doubleClick().build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(Continuebutton));
			Actions action1 = new Actions(driver);
			action1.moveToElement(Continuebutton);
			action1.doubleClick().build().perform();
			helpers.Support.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Document is not deleted sucessfully");
		}
		return new PoliceCheck(driver);
	}


	public PoliceCheck AddPdf() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, Uploadfilebutton1);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton1);
			jse.executeScript("arguments[0].click();", Uploadfilebutton1);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";	
			Uploadfilebutton1.sendKeys(filePath);
			Thread.sleep(2000);
			Actions action1 = new Actions(driver);
			action1.moveToElement(Upload1button);
			action1.doubleClick().build().perform();
			helpers.Support.PageLoadExternalwait(driver);
			
			
		/*	scrolltoElement(driver, Uploadfilebutton1);
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton1)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton1)).clear();
			Thread.sleep(1000);			
			String filePath = System.getProperty("user.dir") + "\\Upload\\Suraj.pdf";			
			helpers.CommonFunctions.Uploadfile(filePath, driver);*/
		}
		catch(Exception e)
		{
			System.out.println("The new pdf is not added sucessfully");
		}
	return new PoliceCheck(driver);
	}
	
	public String EnterPolicecheckdetailsUpload() throws InterruptedException, IOException {
		String ActualTablename_PoliceCheck = null;
		try {
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
			scrolltoElement(driver, Uploadfilebutton1);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton1);
			jse.executeScript("arguments[0].click();", Uploadfilebutton1);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			Uploadfilebutton1.sendKeys(filePath);
			wait.until(ExpectedConditions.elementToBeClickable(UploadDBSCertificate)).click();
			Thread.sleep(7000);
			}
			wait.until(ExpectedConditions.elementToBeClickable(PoliceCheckTab));
			ActualTablename_PoliceCheck = PoliceCheckTab.getAttribute("id");
			System.out.println(AppRefNumber_Police);
			if(DBSRefNumber.isEnabled())
			{
			scrolltoElement(driver, DBSRefNumber);
			wait.until(ExpectedConditions.elementToBeClickable(DBSRefNumber)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DBSRefNumber)).sendKeys(AppRefNumber_Police);
			wait.until(ExpectedConditions.elementToBeClickable(DBSupdatedservices)).click();
			}
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			for(String Extension : Extensions)
			{
			scrolltoElement(driver, Uploadfilebutton2);
			JavascriptExecutor jse1 = (JavascriptExecutor)driver; 
			jse1.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton2);
			jse1.executeScript("arguments[0].click();", Uploadfilebutton2);
			String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton2.sendKeys(filePath1);
			wait.until(ExpectedConditions.elementToBeClickable(UploadForeignchecks)).click();
			Thread.sleep(7000);
			}
						
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PoliceCheck details not filled properly  " +e);
		}
		return ActualTablename_PoliceCheck;
		
	}


	public Boolean verifycountDBSCheck(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableDBSCheck.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}					
		return Uploadcount;	
	}
	
	public Boolean verifycountForeign(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableForeign.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}					
		return Uploadcount;	
	}
	
}
