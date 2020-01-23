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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;
public class ProfessionalDetails extends Support{
	WebDriver driver;
	WebDriverWait wait;
		
	@FindBy(id="ProfessionalDetails")
	WebElement ProfessionalDetailsTab;
	
	@FindBy(id="WasPreviouslyRemovedFromList")
	WebElement RemovedListCheckbox;
	
	@FindBy(id="PreviouslyRemovedFromListExplanation")
	WebElement RemovedListTextkbox;
	
	@FindBy(id="WasSubjectToSanctions")
	WebElement SubjectToSanctionsCheckbox;
	
	@FindBy(id="SubjectToSanctionsExplanation")
	WebElement  SubjectToSanctionsTextkbox;
	
	@FindBy(id="WasSubjectToNationalDisqualification")
	WebElement NationalDisQualificationCheckbox;
	
	@FindBy(id="SubjectToNationalDisqualificationExplanation")
	WebElement NationalDisQualificationkbox;
	
	@FindBy(id="WasPreviouslyOnPerformersList")
	WebElement PrevPerformerlist;
	
	@FindBy(id="PastPerformersListDetails_0__IsSelected")
	WebElement PerformerListCountryCheckbox;
	
	@FindBy(id="PastPerformersListDetails_1__IsSelected")
	WebElement PerformerListCountryCheckbox_Wales;
	
	@FindBy(id="PastPerformersListDetails_2__IsSelected")
	WebElement PerformerListCountryCheckbox_Scotalnd;
	
	@FindBy(id="PastPerformersListDetails_3__IsSelected")
	WebElement PerformerListCountryCheckbox_NI;
	
	/*@FindBy(id="PastPerformersListDetails_0__StartDate")
	WebElement StartdateonTable;*/
	@FindBy(name="PastPerformersListDetails[0].StartDate")
	WebElement StartdateonTable;
	
	@FindBy(name="PastPerformersListDetails[0].EndDate")
	WebElement EnddateonTable;
	
	@FindBy(name="PastPerformersListDetails[1].StartDate")
	WebElement StartdateonTable1;
	
	@FindBy(name="PastPerformersListDetails[1].EndDate")
	WebElement EnddateonTable1;
	
	@FindBy(name="PastPerformersListDetails[2].StartDate")
	WebElement StartdateonTable2;
	
	@FindBy(name="PastPerformersListDetails[2].EndDate")
	WebElement EnddateonTable2;
	
	@FindBy(name="PastPerformersListDetails[3].StartDate")
	WebElement StartdateonTable3;
	
	@FindBy(name="PastPerformersListDetails[3].EndDate")
	WebElement EnddateonTable3;
	
	@FindBy(id="PastPerformersListDetails_0__ResponsibleOfficer")
	WebElement OfficerNameonTable;	
	
	@FindBy(id="PastPerformersListDetails_1__ResponsibleOfficer")
	WebElement OfficerNameonTable1;
	
	@FindBy(id="PastPerformersListDetails_2__ResponsibleOfficer")
	WebElement OfficerNameonTable2;
	
	@FindBy(id="PastPerformersListDetails_3__ResponsibleOfficer")
	WebElement OfficerNameonTable3;
	
	@FindBy(id="PastPerformersListDetails_0__Organisation")
	WebElement LocalOfficeInfo;
	
	@FindBy(id="PastPerformersListDetails_0__PreviousOccuranceNotes")
	WebElement AdditionalComment;
	
	@FindBy(id="EvidenceOfRefusedInclusionDetails")
	WebElement BrowseReused;
	
	@FindBy(xpath="//*[@data-source-id='EvidenceOfRefusedInclusionDetails']")
	WebElement uploadReused;	
	
	@FindBy(id="EvidenceOfSubjectToSanction")
	WebElement BrowseSubjectToSanction;
	
	@FindBy(xpath="//*[@data-source-id='EvidenceOfSubjectToSanction']")
	WebElement uploadSubjectToSanction;
	
	@FindBy(id="EvidenceOfSubjectToNationalDisqualification")
	WebElement BrowseNationalDisqualification;
	
	@FindBy(xpath="//*[@data-source-id='EvidenceOfSubjectToNationalDisqualification']")
	WebElement uploadNationalDisqualification;
	
	@FindBy(xpath ="//*[@id='tblRefusedInclusionDetails']//tbody")
	WebElement UploadtableRefused;
	
	@FindBy(xpath ="//*[@id='tblSubjectToSanction']//tbody")
	WebElement UploadtableSanction;
	
	@FindBy(xpath ="//*[@id='tblSubjectToNationalDisqualification']//tbody")
	WebElement UploadtableNationalDisqualification;
	
	
	
	
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	String RemovedList_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "RemovedList",1);
	String Sanction_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "Sanction",1);
	String NatDisQualification_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "NatDisQualification",1);
	
	String StartDate_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "StartDate",1);
	String EndDate_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "EndDate",1);
	String OfficerName_Professional = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "OfficerName",1);
	String Additional_Comment = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Professional", "AdditionalTime",1);
	
	public ProfessionalDetails(WebDriver driver) 	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

	public String EnterProfessionalDetails(String environment) throws InterruptedException {
		String ActualTablename_ProfessionalDetails = null;
		try{
			
			scrolltoElement(driver, ProfessionalDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(ProfessionalDetailsTab));
			ActualTablename_ProfessionalDetails = ProfessionalDetailsTab.getAttribute("id");
			scrolltoElement(driver, RemovedListCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox));
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox)).click();
			scrolltoElement(driver, RemovedListTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).sendKeys(RemovedList_Professional);
			scrolltoElement(driver, SubjectToSanctionsCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).clear();
			scrolltoElement(driver, SubjectToSanctionsTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).sendKeys(Sanction_Professional);
			scrolltoElement(driver, NationalDisQualificationCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationCheckbox)).click();
			scrolltoElement(driver, NationalDisQualificationkbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).sendKeys(NatDisQualification_Professional);
			scrolltoElement(driver, PrevPerformerlist);
			wait.until(ExpectedConditions.elementToBeClickable(PrevPerformerlist)).click();
			scrolltoElement(driver, PerformerListCountryCheckbox);
			if(!PerformerListCountryCheckbox.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox)).click();
			}
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).clear();			
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable)).sendKeys(OfficerName_Professional);
			String LocalOfficeinfoText = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
			System.out.println("LocalOfficeinfoText++++++++++++++++++++++++++++++"+LocalOfficeinfoText);
			//String localOfficeCode = "Lenskart RLT";
			Select dropdown = new Select(LocalOfficeInfo);
			dropdown.selectByVisibleText(LocalOfficeinfoText);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			
		}
		catch(Exception e)
		{
			System.out.println("The element is not found Professional Details Tab" );
			e.printStackTrace();
		}	
		return ActualTablename_ProfessionalDetails;
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
			System.out.println("The element is not found Savebutton" +e);
		}	
		return new CreateNewApp(driver);
	}

	public String EnterProfessionalDetails_WithAllOption(String environment) throws InterruptedException {
		String ActualTablename_ProfessionalDetails = null;
		try{
			scrolltoElement(driver, ProfessionalDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(ProfessionalDetailsTab));
			ActualTablename_ProfessionalDetails = ProfessionalDetailsTab.getAttribute("id");
			Thread.sleep(3000);
			scrolltoElement(driver, RemovedListCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox));
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox)).click();
			
			scrolltoElement(driver, RemovedListTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).sendKeys(RemovedList_Professional);
			scrolltoElement(driver, SubjectToSanctionsCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).clear();
			scrolltoElement(driver, SubjectToSanctionsTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).sendKeys(Sanction_Professional);
			scrolltoElement(driver, NationalDisQualificationCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationCheckbox)).click();
			scrolltoElement(driver, NationalDisQualificationkbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).sendKeys(NatDisQualification_Professional);
			scrolltoElement(driver, PrevPerformerlist);
			wait.until(ExpectedConditions.elementToBeClickable(PrevPerformerlist)).click();
			scrolltoElement(driver, PerformerListCountryCheckbox);
			if(!PerformerListCountryCheckbox.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable)).sendKeys(OfficerName_Professional);
			String LocalOfficeinfoText = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
			Select dropdown = new Select(LocalOfficeInfo);
			dropdown.selectByVisibleText(LocalOfficeinfoText);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_Wales.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_Wales)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable1)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable1)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_Scotalnd.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_Scotalnd)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable2)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable2)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable2)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable2)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_NI.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_NI)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable3)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable3)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable3)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable3)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Professional Details Tab" +e);
		}	
		return ActualTablename_ProfessionalDetails;
	}
	
	public String EnterProfessionalDetails_WithAllOptionUpload(String environment) throws InterruptedException, IOException {
		String ActualTablename_ProfessionalDetails = null;
		try{
			scrolltoElement(driver, ProfessionalDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(ProfessionalDetailsTab));
			ActualTablename_ProfessionalDetails = ProfessionalDetailsTab.getAttribute("id");
			Thread.sleep(3000);
			scrolltoElement(driver, RemovedListCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox));
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListCheckbox)).click();
			scrolltoElement(driver, RemovedListTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(RemovedListTextkbox)).sendKeys(RemovedList_Professional);
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
				JavascriptExecutor jse = (JavascriptExecutor)driver; 
				jse.executeScript("arguments[0].scrollIntoView();", BrowseReused);
				jse.executeScript("arguments[0].click();", BrowseReused);
				String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
				BrowseReused.sendKeys(filePath);
				scrolltoElement(driver, uploadReused);
				wait.until(ExpectedConditions.elementToBeClickable(uploadReused)).click();
				helpers.CommonFunctions.PageLoadExternalwait(driver); 
			}
			scrolltoElement(driver, SubjectToSanctionsCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).clear();
			scrolltoElement(driver, SubjectToSanctionsTextkbox);
			wait.until(ExpectedConditions.elementToBeClickable(SubjectToSanctionsTextkbox)).sendKeys(Sanction_Professional);
			for(String Extension : Extensions)
			{
				JavascriptExecutor jse = (JavascriptExecutor)driver; 
				jse.executeScript("arguments[0].scrollIntoView();", BrowseSubjectToSanction);
				jse.executeScript("arguments[0].click();", BrowseSubjectToSanction);
				String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
				BrowseSubjectToSanction.sendKeys(filePath);
				scrolltoElement(driver, uploadSubjectToSanction);
				wait.until(ExpectedConditions.elementToBeClickable(uploadSubjectToSanction)).click();
				helpers.CommonFunctions.PageLoadExternalwait(driver); 
			}
			scrolltoElement(driver, NationalDisQualificationCheckbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationCheckbox)).click();
			scrolltoElement(driver, NationalDisQualificationkbox);
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NationalDisQualificationkbox)).sendKeys(NatDisQualification_Professional);
			for(String Extension : Extensions)
			{
				JavascriptExecutor jse = (JavascriptExecutor)driver; 
				jse.executeScript("arguments[0].scrollIntoView();", BrowseNationalDisqualification);
				jse.executeScript("arguments[0].click();", BrowseNationalDisqualification);
				String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
				BrowseNationalDisqualification.sendKeys(filePath);
				scrolltoElement(driver, uploadNationalDisqualification);
				wait.until(ExpectedConditions.elementToBeClickable(uploadNationalDisqualification)).click();
				helpers.CommonFunctions.PageLoadExternalwait(driver); 
			}
			scrolltoElement(driver, PrevPerformerlist);
			wait.until(ExpectedConditions.elementToBeClickable(PrevPerformerlist)).click();
			scrolltoElement(driver, PerformerListCountryCheckbox);
			if(!PerformerListCountryCheckbox.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable)).sendKeys(OfficerName_Professional);
			String LocalOfficeinfoText = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
			Select dropdown = new Select(LocalOfficeInfo);
			dropdown.selectByVisibleText(LocalOfficeinfoText);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_Wales.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_Wales)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable1)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable1)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_Scotalnd.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_Scotalnd)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable2)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable2)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable2)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable2)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			if(!PerformerListCountryCheckbox_NI.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(PerformerListCountryCheckbox_NI)).click();
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable3)).sendKeys(StartDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(StartdateonTable3)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable3)).sendKeys(EndDate_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(EnddateonTable1)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(OfficerNameonTable3)).sendKeys(OfficerName_Professional);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalComment)).sendKeys(Additional_Comment);
			}
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Professional Details Tab" +e);
		}	
		return ActualTablename_ProfessionalDetails;
	}

	public Boolean verifyCountRefused(int expecteddownload) {
		
			boolean Uploadcount = false;
			try{
				 List<WebElement> rows = UploadtableRefused.findElements(By.tagName("tr"));
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
	
	public Boolean verifyCountSanction(int expecteddownload) {
		
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableSanction.findElements(By.tagName("tr"));
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

	public Boolean verifyCountNational(int expecteddownload) {
		
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableNationalDisqualification.findElements(By.tagName("tr"));
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
		scrolltoElement(driver, RemovedListCheckbox);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	

}
