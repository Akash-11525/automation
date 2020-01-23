package pageobjects.PL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class Appraisal extends Support {
	WebDriver driver;
	WebDriverWait wait;	

	@FindBy(id="NoAppraisalExplanation")
	WebElement ExplanationOnAppraisal;
	
	@FindBy(name="GmcRevalidationDate")
	WebElement RevalidationDate;
	
	@FindBy(id="GmcRevalidationOfficerName")
	WebElement OfficerName;
	
	@FindBy(id="GmcRevalidationDesignatedBody")
	WebElement DesignedBody;
	
	@FindBy(id="GmcRevalidationOnHoldExplanation")
	WebElement ExplanationOnHold;	
	
	@FindBy(id="AppraisalHistory")
	WebElement AppraisalTab;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="EvidenceOfAppraiserDocumentDetails")
	WebElement Uploadfilebutton;
	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(xpath = "//*[@id='tblAppraiserDocumentDetails']//tbody")
	WebElement Uploadtable;
	
	@FindBy(name="btnNewRecord")
	WebElement NewAppraisal;
	
	@FindBy(name="Date")
	WebElement AppraisalDate;
	
	@FindBy(name="OrganisationName")
	WebElement OrganizationName;
	
	@FindBy(name="AppraiserName")
	WebElement AppraisalName;
	
	@FindBy(name="btnNext")
	WebElement AddAppraisal;
	

	String Reason_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "Reason",1);
	String DateRevalidation_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "DateRevalidation",1);	
	String OfficerName_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "OfficerName",1);
	String DesignedBody_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "DesignedBody",1);
	String Status_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "Status",1);
	String InfoOnRevalidation_appraisal = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "InfoOnRevalidation",1);
	String New_AppraisalDate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "APPRAISALDATE",1);
	String New_OrganizationName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "Organization",1);
	String New_AppraisalName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Appraisal", "Name of Appraiser",1);
	
	public Appraisal(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}



	public String EnterApprisalDetails(int Position) throws InterruptedException {
		String ActualTablename_Apprisal = null;
		String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
		String MedicalPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",Position);
		try{
			Thread.sleep(1500);
			wait.until(ExpectedConditions.elementToBeClickable(AppraisalTab));
			ActualTablename_Apprisal = AppraisalTab.getAttribute("id");
			System.out.println(ActualTablename_Apprisal);
			scrolltoElement(driver, ExplanationOnAppraisal);
			wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnAppraisal)).sendKeys(Reason_appraisal);
			if(PerformerList_cap.startsWith("Medical"))
			{
			wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(DateRevalidation_appraisal);
			wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(OfficerName)).sendKeys(OfficerName_appraisal);
			wait.until(ExpectedConditions.elementToBeClickable(DesignedBody)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DesignedBody)).sendKeys(DesignedBody_appraisal);
			helpers.CommonFunctions.ClickOnRadioButton(Status_appraisal, driver);
			scrolltoElement(driver, ExplanationOnHold);
			wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).sendKeys(InfoOnRevalidation_appraisal);
			}
			if(PerformerList_cap.startsWith("Ophthalmic")&& MedicalPerformer_cap.equalsIgnoreCase("Ophthalmic medical practitioner"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(DateRevalidation_appraisal);
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(Keys.TAB);
				helpers.CommonFunctions.ClickOnRadioButton(Status_appraisal, driver);
				scrolltoElement(driver, ExplanationOnHold);
				wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).sendKeys(InfoOnRevalidation_appraisal);
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The data is not filled on Appraisal Tab " +e);
		}
		return ActualTablename_Apprisal;
		
	}



	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
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



	public Appraisal upload()throws InterruptedException {
		try{
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
				scrolltoElement(driver, Uploadfilebutton);
				JavascriptExecutor jse = (JavascriptExecutor)driver; 
				jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
	//	jse.executeScript("arguments[0].click();", Uploadfilebutton);
		String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
	//helpers.CommonFunctions.Uploadfile(filePath, driver);
		Uploadfilebutton.sendKeys(filePath);
		wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(BrowseButton));
		helpers.CommonFunctions.PageLoadExternalwait(driver); 
		//helpers.CommonFunctions.PageLoadExternalwait(driver);
			}
		}
		catch(Exception e)
		{
			System.out.println("The upload on appraisal is not captured" +e);
		}
		return new Appraisal(driver);
	}
	
	
	public Boolean verifyCount(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = Uploadtable.findElements(By.tagName("tr"));
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




		public void screenshot(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, RevalidationDate);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}



		public String AddNewAppraisal(int Position) throws InterruptedException {
			String ActualTablename_Apprisal = null;
			String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
			String MedicalPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",Position);
			try{
				
				Thread.sleep(1500);
				wait.until(ExpectedConditions.elementToBeClickable(AppraisalTab));
				ActualTablename_Apprisal = AppraisalTab.getAttribute("id");
				System.out.println(ActualTablename_Apprisal);
				scrolltoElement(driver, NewAppraisal);
				wait.until(ExpectedConditions.elementToBeClickable(NewAppraisal)).click();
				Thread.sleep(500);
				wait.until(ExpectedConditions.elementToBeClickable(AppraisalDate)).sendKeys(New_AppraisalDate);
				wait.until(ExpectedConditions.elementToBeClickable(OrganizationName)).sendKeys(New_OrganizationName);
				wait.until(ExpectedConditions.elementToBeClickable(AppraisalName)).sendKeys(New_AppraisalName);
				wait.until(ExpectedConditions.elementToBeClickable(AddAppraisal)).click();
				Thread.sleep(1000);
				
				if(PerformerList_cap.startsWith("Medical"))
				{
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(DateRevalidation_appraisal);
				wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(Keys.TAB);
				wait.until(ExpectedConditions.elementToBeClickable(OfficerName)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(OfficerName)).sendKeys(OfficerName_appraisal);
				wait.until(ExpectedConditions.elementToBeClickable(DesignedBody)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(DesignedBody)).sendKeys(DesignedBody_appraisal);
				helpers.CommonFunctions.ClickOnRadioButton(Status_appraisal, driver);
				scrolltoElement(driver, ExplanationOnHold);
				wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).sendKeys(InfoOnRevalidation_appraisal);
				}
				if(PerformerList_cap.startsWith("Ophthalmic")&& MedicalPerformer_cap.equalsIgnoreCase("Ophthalmic medical practitioner"))
				{
					wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).clear();
					wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(DateRevalidation_appraisal);
					wait.until(ExpectedConditions.elementToBeClickable(RevalidationDate)).sendKeys(Keys.TAB);
					helpers.CommonFunctions.ClickOnRadioButton(Status_appraisal, driver);
					scrolltoElement(driver, ExplanationOnHold);
					wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).clear();
					wait.until(ExpectedConditions.elementToBeClickable(ExplanationOnHold)).sendKeys(InfoOnRevalidation_appraisal);
				}
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The data is not filled on Appraisal Tab " +e);
			}
			return ActualTablename_Apprisal;
		}
		
	





}
