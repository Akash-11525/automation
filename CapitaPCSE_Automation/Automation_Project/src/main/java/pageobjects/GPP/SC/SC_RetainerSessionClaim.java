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
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class SC_RetainerSessionClaim extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input#txtRetClaim_GMCCode")
	WebElement ret_GmcCodeTxtBox;
	
	@FindBy(css="input#txtRetainerClaimStartDate")
	WebElement ret_StartDate;
	
	@FindBy(css="input#txtRetainerClaimEndtDate")
	WebElement ret_EndDate;
	
	@FindBy(css="input#RetainerClaim_NumberofSessions")
	WebElement ret_NoOfSessionstxtbox;
	
	@FindBy(css="select[id='ddlContractorCode'][name='RetainerClaim.ContractorCode']")
	WebElement ret_ContractorCode;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement ret_SelectFileButton;
	
	@FindBy(xpath="//div[@id='dvRetainerClaim']/div[8]/div/div/div/button")
	WebElement ret_FileUploadButton;
	
	@FindBy(css="div.popover.confirmation.fade.top.in")
	WebElement ret_UploadWindow;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement ret_ConfirmUpload;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[1]")
	WebElement ret_CancelUpload;
	
	@FindBy(css="input#RetainerClaim_TotalClaimAmount")
	WebElement ret_ClaimAmountTxtBox;
	
	@FindBy(css="textarea#RetainerClaim_AdditionalComment")
	WebElement ret_EnterDetails;
	
	@FindBy(css="input#CheckToConfirm")
	WebElement ret_TickToConfirm;
	
	@FindBy(xpath="//*[@id='dvRetainerClaim']/div[16]/div[1]/button")
	WebElement ret_CancelButton;
	
	@FindBy(xpath="//div[@id='divConfCancelRetainerClaim']/div/div/div[3]/div[2]/button")
	WebElement ret_CancelClaimConfirmButton;
	
	@FindBy(xpath="//div[@id='divConfCancelRetainerClaim']/div/div/div[3]/div[1]/button")
	WebElement ret_CancelClaimButton;
	
	@FindBy(xpath="//*[@id='dvRetainerClaim']/div[16]/div[2]/button[1]")
	WebElement ret_saveForLaterButton;
	
	@FindBy(xpath="//div[@id='divConfSaveReatinerClaim']/div/div/div[3]/div[2]/input")
	WebElement ret_SaveClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSaveReatinerClaim']/div/div/div[3]/div[1]/button")
	WebElement ret_CancelSavedClaimButton;
	
	@FindBy(xpath="//div[@id='dvRetainerClaim']/div[16]/div[2]/button[2]")
	WebElement ret_submitButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitReatinerClaim']/div/div/div[3]/div[2]/input")
	WebElement ret_SubmitClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitReatinerClaim']/div/div/div[3]/div[1]/button")
	WebElement ret_CancelSubmittedClaimButton;
	
	@FindBy(css="tr.item")
	List<WebElement> uploadedRecords;
	
	public SC_RetainerSessionClaim(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public SC_RetainerSessionClaim createRetainerClaim(int colNumber, String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
		uploadFile();
		scrolltoElement(driver, ret_submitButton);
		return new SC_RetainerSessionClaim(driver);
	}
	
	public SC_RetainerSessionClaim createRetainerClaimWithoutFieldValidations(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		String contractorText= ret_GmcCodeTxtBox.getText().toString();
		contractorText= contractorText+"1";
		enterDataInTextField(ret_GmcCodeTxtBox, contractorText, wait);
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
		uploadFile();
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
		scrolltoElement(driver, ret_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(ret_submitButton));
		Thread.sleep(2000);
		ret_submitButton.click();
		return new SC_RetainerSessionClaim(driver);
	}
	
	public StdClaimsPortal cancelRetainerClaim(int colNumber, String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
		scrolltoElement(driver, ret_CancelButton);
		clickOnCancelButton(colNumber);
		return new StdClaimsPortal(driver);
	}
	
	public boolean validateFileFormatAndClaimData(int colNumber, String environment) throws InterruptedException, ParseException, IOException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
		boolean allUploaded=attachMultipleFiles(colNumber);
		deleteFile();
		Thread.sleep(3000);
		return allUploaded;
	}
	
	private void uploadFile() throws InterruptedException {
			
			scrolltoElement(driver, ret_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(ret_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(StdClaimsPortal.filePath, driver);
			WebElement uploadWindow = driver.switchTo().activeElement();
			uploadWindow.sendKeys(StdClaimsPortal.filePath);
			wait.until(ExpectedConditions.elementToBeClickable(ret_FileUploadButton)).click();
			scrolltoElement(driver, ret_ConfirmUpload);
			wait.until(ExpectedConditions.elementToBeClickable(ret_ConfirmUpload)).click();
			Thread.sleep(3000);
			
		}
	
	private void deleteFile() {
		System.out.println("Entered in delete file method");
		WebElement elmFile= driver.findElement(By.xpath("//table[@id='tblUploadedRetainerClaimDocs']//tbody/tr[1]/td[2]/button"));
		elmFile.click();
		WebElement confirmDelete= driver.findElement(By.xpath("//div[@class='confirmation-buttons text-center']/div/a[2]"));
		confirmDelete.click();
		System.out.println("File deleted");
	}
	
	
	private void enterClaimDetails(int colNumber, String environment) throws InterruptedException, ParseException {
		
		String gmcCode = ConfigurationData.getRefDataDetails(environment, "StdClaimsGMCCode");
		enterDataInTextField(ret_GmcCodeTxtBox, gmcCode, wait);
		ret_GmcCodeTxtBox.sendKeys(Keys.TAB);
		
		scrolltoElement(driver, ret_NoOfSessionstxtbox);
		
		//String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "ClaimStartDate", colNumber);
		Date date= CommonFunctions.getDateBeforeDays(5);
		String startDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		enterDataInTextField(ret_StartDate, startDate, wait);
		
		//String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ClaimEndDate", colNumber);
		date= CommonFunctions.getDateBeforeDays(3);
		String endDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		enterDataInTextField(ret_EndDate, endDate, wait);
		
		String sessionCount = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "NoOfSessions", colNumber);
		enterDataInTextField(ret_NoOfSessionstxtbox, sessionCount, wait);
		ret_NoOfSessionstxtbox.sendKeys(Keys.TAB);
		
		scrolltoElement(driver, ret_ContractorCode);
		Thread.sleep(4000);
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode");
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "ContractorCode", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(ret_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(ret_ContractorCode, contractorCode);
		
		String claimAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "ClaimAmount", colNumber);
		enterDataInTextField(ret_ClaimAmountTxtBox, claimAmount, wait);
		
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(ret_EnterDetails, additionalDetails, wait);

	}
	
	public StdClaimsPortal clickOnSubmitButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, ret_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(ret_submitButton));
		Thread.sleep(2000);
		ret_submitButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSubmitReatinerClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RetainerClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(ret_SubmitClaimButton));
		Thread.sleep(2000);
		ret_SubmitClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}

	private boolean attachMultipleFiles(int colNumber) throws InterruptedException, IOException {
		File readFolder= new File(StdClaimsPortal.files);
		boolean allUploaded= false;
		File[] fileArray= readFolder.listFiles();
		Actions moveFocustoSelectFile = new Actions(driver);
		for(int i=0;i<fileArray.length;i++){
			String fileName= fileArray[i].toString();
			scrolltoElement(driver, ret_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(ret_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(fileName, driver);
			
			System.out.println("instance "+i );
			wait.until(ExpectedConditions.elementToBeClickable(ret_FileUploadButton)).click();
			System.out.println("instance "+i +" is passed");
			
			try{
				if(ret_UploadWindow.isDisplayed()){
					scrolltoElement(driver, ret_ConfirmUpload);
					String actualMsg= driver.findElement(By.cssSelector("p.confirmation-content")).getText().trim();
					boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RetainerClaimData","ExpUploadMessage",colNumber);
					System.out.println("Message validation status is "+verifyMessage);
					wait.until(ExpectedConditions.elementToBeClickable(ret_ConfirmUpload)).click();
					Thread.sleep(3000);
					moveFocustoSelectFile.moveToElement(ret_SelectFileButton).build().perform();
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
		captureStdClaimPortalSnap("Files uploaded in Retainer claim");
		
		return allUploaded;
	}
	

	public StdClaimsPortal clickOnSaveForLaterButton(int colNumber) throws InterruptedException {
		scrolltoElement(driver, ret_saveForLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(ret_saveForLaterButton));
		Thread.sleep(2000);
		ret_saveForLaterButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSaveReatinerClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RetainerClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(ret_CancelSavedClaimButton));
		Thread.sleep(2000);
		ret_CancelSavedClaimButton.click();
		Thread.sleep(2000);
		ret_saveForLaterButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(ret_SaveClaimButton));
		Thread.sleep(2000);
		ret_SaveClaimButton.click();
		return new StdClaimsPortal(driver);
	}
	
	public StdClaimsPortal clickOnCancelButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, ret_CancelButton);
		wait.until(ExpectedConditions.elementToBeClickable(ret_CancelButton));
		Thread.sleep(2000);
		ret_CancelButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfCancelRetainerClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RetainerClaimData","ExpCancelMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(ret_CancelClaimButton));
		Thread.sleep(2000);
		ret_CancelClaimButton.click();
		Thread.sleep(2000);
		ret_CancelButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(ret_CancelClaimConfirmButton));
		Thread.sleep(2000);
		ret_CancelClaimConfirmButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public void updateClaimDetails(int colNumber) {
		scrolltoElement(driver, ret_EnterDetails);
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData", "AdditionalDetails", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(ret_EnterDetails)).click();
		ret_EnterDetails.clear();
		ret_EnterDetails.sendKeys(additionalDetails+" added after reverting claim to draft");
		
		scrolltoElement(driver, ret_TickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(ret_TickToConfirm)).click();
	}
	
	public void captureValidationMessages(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, ret_GmcCodeTxtBox);
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
			 ArrMessage=captureOtherValidationMessages(ArrMessage);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("No such element is found. " +e);
		}
		return ArrMessage;
		
	}

	private List<String> captureOtherValidationMessages(List<String> arrMessage) {
		String performerMsg= driver.findElement(By.id("gmcerrorDiv")).getText();
		arrMessage.add(performerMsg);
		return arrMessage;
	}
	
	public void captureStdClaimPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, ret_GmcCodeTxtBox);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string + "_2");

	}

}
