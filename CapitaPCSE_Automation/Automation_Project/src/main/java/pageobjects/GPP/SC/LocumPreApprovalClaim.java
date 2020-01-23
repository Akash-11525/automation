package pageobjects.GPP.SC;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.GPPHelpers;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class LocumPreApprovalClaim extends Support {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(css="select#LocumCostPreApproval_ClaimType")
	WebElement locumpre_selectClaimType;
	
	@FindBy(css="input#txtPreApproval_GMCCode")
	WebElement locumpre_GmcCodeTxtBox;
	
	@FindBy(css="input#txtAbsenceStartDate")
	WebElement locumpre_StartDate;
	
	@FindBy(css="input#txtAbsenceEndDate")
	WebElement locumpre_EndDate;
	
	@FindBy(css="select[id='ddlContractorCode'][name='LocumCostPreApproval.ContractorCode']")
	WebElement locumpre_ContractorCode;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement locumpre_SelectFileButton;
	
	@FindBy(xpath="//div[@id='dvLocumCostPreApproval']/div[8]/div[2]/div/button")
	WebElement locumpre_FileUploadButton;
	
	@FindBy(css="div.popover.confirmation.fade.top.in")
	WebElement locumpre_UploadWindow;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement locumpre_ConfirmUpload;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[1]")
	WebElement locumpre_CancelUpload;
	
	@FindBy(css="textarea[id='LocumCostPreApproval_AdditionalComment']")
	WebElement locumpre_EnterDetails;
	
	@FindBy(css="input[id='CheckToConfirm']")
	WebElement locumpre_TickToConfirm;
	
	@FindBy(id="cancelbtn")
	WebElement locumpre_CancelButton;
	
	@FindBy(xpath="//div[@id='divConfCancelPreApprovalClaim']/div/div/div[3]/div[2]/button")
	WebElement locumpre_CancelClaimConfirmButton;
	
	@FindBy(xpath="//div[@id='divConfCancelPreApprovalClaim']/div/div/div[3]/div[1]/button")
	WebElement locumpre_CancelClaimButton;
	
	@FindBy(xpath="//div[@id='dvLocumCostPreApproval']/div[15]/div[2]/button[1]")
	WebElement locumpre_saveForLaterButton;
	
	@FindBy(xpath="//div[@id='divConfSavePreApprovalClaim']/div/div/div[3]/div[2]/input")
	WebElement locumpre_SaveClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSavePreApprovalClaim']/div/div/div[3]/div[1]/button")
	WebElement locumpre_CancelSavedClaimButton;
	
	@FindBy(id="submitbtn")
	WebElement locumpre_submitButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitPreApprovalClaim']/div/div/div[3]/div[2]/input")
	WebElement locumpre_SubmitClaimButton;
	
	@FindBy(xpath="//div[@id='divClaimSubmission']/div/div/div[3]/div[1]/button")
	WebElement locum_CancelSubmittedClaimButton;
	
	@FindBy(css="tr.item")
	List<WebElement> uploadedRecords;
	
	public LocumPreApprovalClaim(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public LocumPreApprovalClaim createLocumPreApprovalClaimApplication(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_TickToConfirm)).click();
		uploadFile();
		Thread.sleep(3000);
		scrolltoElement(driver, locumpre_submitButton);
		return new LocumPreApprovalClaim(driver);
	}
	
	public LocumPreApprovalClaim createPreApprovalWithoutFieldValidations(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_TickToConfirm)).click();
		uploadFile();
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_TickToConfirm)).click();
		scrolltoElement(driver, locumpre_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_submitButton));
		Thread.sleep(2000);
		locumpre_submitButton.click();
		return new LocumPreApprovalClaim(driver);
	}
	
	public StdClaimsPreApprovalPortal cancelPreApprovalClaim(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_TickToConfirm)).click();
		scrolltoElement(driver, locumpre_CancelButton);
		clickOnCancelButton(colNumber);
		return new StdClaimsPreApprovalPortal(driver);
	}

	private void enterClaimDetails(int colNumber,String environment) throws InterruptedException, ParseException {
		
		String claimType= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumPreApprovalClaimData", "ClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(locumpre_selectClaimType, claimType);
		
		String gmcCode = ConfigurationData.getRefDataDetails(environment, "StdClaimsGMCCode"); 

		enterDataInTextField(locumpre_GmcCodeTxtBox, gmcCode, wait);
		locumpre_GmcCodeTxtBox.sendKeys(Keys.TAB);
		scrolltoElement(driver, locumpre_ContractorCode);
		
		//String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumPreApprovalClaimData", "StartDate", colNumber);
		Date date= CommonFunctions.getDateAfterDays(0);
		String joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		String[]dateDetails= {joinDate,"dd/MM/yyyy","1"};
		String startDate= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		scrolltoElement(driver, locumpre_StartDate);
		enterDataInTextField(locumpre_StartDate, startDate, wait);
		
		//String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumPreApprovalClaimData", "EndDate", colNumber);
		date= CommonFunctions.getDateAfterDays(0);
		joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		dateDetails= new String[]{joinDate,"dd/MM/yyyy","3"};
		String endDate= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		scrolltoElement(driver, locumpre_EndDate);
		enterDataInTextField(locumpre_EndDate, endDate, wait);
		
		scrolltoElement(driver, locumpre_ContractorCode);
		Thread.sleep(4000);
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode"); 

		wait.until(ExpectedConditions.elementToBeClickable(locumpre_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(locumpre_ContractorCode, contractorCode);
		
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumPreApprovalClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(locumpre_EnterDetails, additionalDetails, wait);
	}
	
	public StdClaimsPreApprovalPortal clickOnSubmitButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locumpre_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_submitButton));
		Thread.sleep(3000);
		locumpre_submitButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSubmitPreApprovalClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumPreApprovalClaimData","ExpSubmitMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_SubmitClaimButton));
		Thread.sleep(2000);
		locumpre_SubmitClaimButton.click();
		return new StdClaimsPreApprovalPortal(driver);
		
	}
	
	public void uploadFile() throws InterruptedException{
		
		scrolltoElement(driver, locumpre_SelectFileButton);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_SelectFileButton)).clear();
		CommonFunctions.Uploadfile(StdClaimsPortal.filePath, driver);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_FileUploadButton)).click();
		scrolltoElement(driver, locumpre_ConfirmUpload);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_ConfirmUpload));
		Thread.sleep(3000);
		locumpre_ConfirmUpload.click();
		Thread.sleep(3000);
		
		}

	public boolean validateFileFormatAndClaimData(int colNumber,String environment) throws InterruptedException, ParseException, IOException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_TickToConfirm)).click();
		boolean allUploaded=attachMultipleFiles(colNumber);
		deleteFile();
		Thread.sleep(3000);
		return allUploaded;
	}

	private void deleteFile() {
		WebElement elmFile= driver.findElement(By.xpath("//table[@id='tblUploadedLCMCostPreApprovalDocs']//tbody/tr[1]/td[2]/button"));
		elmFile.click();
		WebElement confirmDelete= driver.findElement(By.xpath("//div[@class='confirmation-buttons text-center']/div/a[2]"));
		confirmDelete.click();

	}

	private boolean attachMultipleFiles(int colNumber) throws InterruptedException, IOException {
		File readFolder= new File(StdClaimsPortal.files);
		boolean allUploaded= false;
		File[] fileArray= readFolder.listFiles();
		Actions moveFocustoSelectFile = new Actions(driver);
		for(int i=0;i<fileArray.length;i++){
			String fileName= fileArray[i].toString();
			scrolltoElement(driver, locumpre_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(locumpre_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(fileName, driver);
			System.out.println("instance "+i );
			wait.until(ExpectedConditions.elementToBeClickable(locumpre_FileUploadButton)).click();
			System.out.println("instance "+i +" is passed");
			
			try{
				if(locumpre_UploadWindow.isDisplayed()){
					scrolltoElement(driver, locumpre_ConfirmUpload);
					String actualMsg= driver.findElement(By.cssSelector("p.confirmation-content")).getText().trim();
					boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumPreApprovalClaimData","ExpUploadMessage",colNumber);
					wait.until(ExpectedConditions.elementToBeClickable(locumpre_ConfirmUpload)).click();
					moveFocustoSelectFile.moveToElement(locumpre_SelectFileButton).build().perform();
				}
			}catch(Exception e){
				System.out.println("Modal window is not found");
			}

			System.out.println("File name "+fileName+" is attached on portal.");
		}
			int uploadedCount= uploadedRecords.size();
			
			if(fileArray.length==uploadedCount){
				allUploaded= true;
			}
			capturePreApprovalClaimPortalSnap("Files uploaded in Locum pre approval claim");
			
			return allUploaded;
		
	}
	
	public StdClaimsPreApprovalPortal clickOnSaveForLaterButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locumpre_saveForLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_saveForLaterButton));
		Thread.sleep(2000);
		locumpre_saveForLaterButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSavePreApprovalClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumPreApprovalClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_CancelSavedClaimButton));
		Thread.sleep(2000);
		locumpre_CancelSavedClaimButton.click();
		Thread.sleep(2000);
		locumpre_saveForLaterButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_SaveClaimButton));
		Thread.sleep(2000);
		locumpre_SaveClaimButton.click();
		return new StdClaimsPreApprovalPortal(driver);
		
	}
	
	public StdClaimsPreApprovalPortal clickOnCancelButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locumpre_CancelButton);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_CancelButton));
		Thread.sleep(2000);
		locumpre_CancelButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfCancelPreApprovalClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumPreApprovalClaimData","ExpCancelMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_CancelClaimButton));
		Thread.sleep(2000);
		locumpre_CancelClaimButton.click();
		Thread.sleep(2000);
		locumpre_CancelButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(locumpre_CancelClaimConfirmButton));
		Thread.sleep(2000);
		locumpre_CancelClaimConfirmButton.click();
		return new StdClaimsPreApprovalPortal(driver);
		
	}
	
	public void captureValidationMessages(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, locumpre_selectClaimType);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string + "_2");
		
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
			 //ArrMessage=captureOtherValidationMessages(ArrMessage);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("No such element is found. " +e);
		}
		return ArrMessage;
		
	}
	
	public void capturePreApprovalClaimPortalSnap(String string) throws InterruptedException, IOException {
		
		scrolltoElement(driver, locumpre_GmcCodeTxtBox);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
		
		
	}

/*	private List<String> captureOtherValidationMessages(List<String> arrMessage) {
		String performerMsg= driver.findElement(By.id("gmcerrorDiv")).getText();
		arrMessage.add(performerMsg);
		return arrMessage;
	}*/
	
}
