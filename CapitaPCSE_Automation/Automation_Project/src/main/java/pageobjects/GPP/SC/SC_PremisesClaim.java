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

public class SC_PremisesClaim extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="select[id='ddlClaimType'][name='premisesClaims.selectedClaimType']")
	WebElement pre_selectClaimType;
	
	@FindBy(css="input#txtPremisesClaimStartDate")
	WebElement pre_StartDate;
	
	@FindBy(css="input#txtPremisesClaimEndDate")
	WebElement pre_EndDate;

	@FindBy(css="select[id='ddlContractorCode'][name='premisesClaims.ContractorCode']")
	WebElement pre_ContractorCode;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement pre_SelectFileButton;
	
	@FindBy(xpath="//div[@id='dvPremisesClaim']/div[7]/div/div[2]/div/button")
	WebElement pre_FileUploadButton;
	
	@FindBy(css="textarea[name='premisesClaims.AdditionalDetails']")
	WebElement pre_EnterDetails;
	
	@FindBy(css="input[name='premisesClaims.TotalClaimAmount']")
	WebElement pre_ClaimAmountTxtBox;
	
	@FindBy(css="input[name='premisesClaims.IsConfirm'][id='CheckToConfirm']")
	WebElement pre_TickToConfirm;
	
	@FindBy(xpath="//div[@id='dvPremisesClaim']/div[15]/div[1]/button")
	WebElement pre_CancelButton;
	
	@FindBy(xpath="//div[@id='divConfCancelPremisesClaim']/div/div/div[3]/div[2]/button")
	WebElement pre_CancelClaimConfirmButton;
	
	@FindBy(xpath="//div[@id='divConfCancelPremisesClaim']/div/div/div[3]/div[1]/button")
	WebElement pre_CancelClaimButton;
	
	@FindBy(xpath="//button[@data-target='#divConfSavePremisesClaim']")
	WebElement pre_saveForLaterButton;
	
	@FindBy(xpath="//div[@id='divConfSavePremisesClaim']/div/div/div[3]/div[2]/input")
	WebElement pre_SaveClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSavePremisesClaim']/div/div/div[3]/div[1]/button")
	WebElement pre_CancelSavedClaimButton;
	
	@FindBy(xpath="//button[@data-target='#divConfSubmitPremisesClaim']")
	WebElement pre_submitButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitPremisesClaim']/div/div/div[3]/div[2]/input")
	WebElement pre_SubmitClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitPremisesClaim']/div/div/div[3]/div[1]/button")
	WebElement pre_CancelSubmittedClaimButton;
	// to be added on Friday for other claim types
	@FindBy(css="div.popover.confirmation.fade.top.in")
	WebElement pre_UploadWindow;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement pre_ConfirmUpload;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[1]")
	WebElement pre_CancelUpload;
	
	@FindBy(xpath="//div[@id='divConfSubmitPremisesClaim']//div[@class='modal-content']")
	WebElement pre_ModalWindow;
	
	@FindBy(css="tr.item")
	List<WebElement> uploadedRecords;
	
	public SC_PremisesClaim(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public SC_PremisesClaim createPremisesClaim(int colNumber, String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
		uploadFile();
		scrolltoElement(driver, pre_submitButton);
		return new SC_PremisesClaim(driver);
	}
	
	public SC_PremisesClaim createPremisesClaimWithoutFieldValidations(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
		uploadFile();
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
		scrolltoElement(driver, pre_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(pre_submitButton));
		Thread.sleep(2000);
		pre_submitButton.click();
		return new SC_PremisesClaim(driver);
	}
	
	public StdClaimsPortal cancelPremisesClaim(int colNumber, String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
		scrolltoElement(driver, pre_CancelButton);
		clickOnCancelButton(colNumber);
		return new StdClaimsPortal(driver);
	}
	
	public boolean validateFileFormatAndClaimData(int colNumber, String environment) throws InterruptedException, ParseException, IOException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
		boolean allUploaded= attachMultipleFiles(colNumber);
		deleteFile();
		Thread.sleep(3000);
		return allUploaded;
	}
	
	public StdClaimsPortal clickOnSubmitButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, pre_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(pre_submitButton));
		Thread.sleep(2000);
		pre_submitButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSubmitPremisesClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","PremisesClaimData","ExpSubmitMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(pre_SubmitClaimButton));
		Thread.sleep(2000);
		pre_SubmitClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnSaveForLaterButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, pre_saveForLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(pre_saveForLaterButton));
		Thread.sleep(2000);
		pre_saveForLaterButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSavePremisesClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","PremisesClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(pre_CancelSavedClaimButton));
		Thread.sleep(2000);
		pre_CancelSavedClaimButton.click();
		Thread.sleep(2000);
		pre_saveForLaterButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(pre_SaveClaimButton));
		Thread.sleep(2000);
		pre_SaveClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnCancelButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, pre_CancelButton);
		wait.until(ExpectedConditions.elementToBeClickable(pre_CancelButton));
		Thread.sleep(2000);
		pre_CancelButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfCancelPremisesClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","PremisesClaimData","ExpCancelMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(pre_CancelClaimButton));
		Thread.sleep(2000);
		pre_CancelClaimButton.click();
		Thread.sleep(2000);
		pre_CancelButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(pre_CancelClaimConfirmButton));
		Thread.sleep(2000);
		pre_CancelClaimConfirmButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	private void uploadFile() throws InterruptedException {
		
		scrolltoElement(driver, pre_SelectFileButton);
		wait.until(ExpectedConditions.elementToBeClickable(pre_SelectFileButton)).clear();
		CommonFunctions.Uploadfile(StdClaimsPortal.filePath, driver);
		WebElement uploadWindow = driver.switchTo().activeElement();
		uploadWindow.sendKeys(StdClaimsPortal.filePath);
		wait.until(ExpectedConditions.elementToBeClickable(pre_FileUploadButton)).click();
		scrolltoElement(driver, pre_ConfirmUpload);
		wait.until(ExpectedConditions.elementToBeClickable(pre_ConfirmUpload)).click();
		Thread.sleep(3000);
		
	}
	
	private boolean attachMultipleFiles(int colNumber) throws InterruptedException, IOException {
		File readFolder= new File(StdClaimsPortal.files);
		boolean allUploaded= false;
		File[] fileArray= readFolder.listFiles();
		Actions moveFocustoSelectFile = new Actions(driver);
		for(int i=0;i<fileArray.length;i++){
			String fileName= fileArray[i].toString();
			scrolltoElement(driver, pre_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(pre_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(fileName, driver);
			System.out.println("instance "+i );
			wait.until(ExpectedConditions.elementToBeClickable(pre_FileUploadButton)).click();
			System.out.println("instance "+i +" is passed");
			
			try{
				if(pre_UploadWindow.isDisplayed()){
					String actualMsg= driver.findElement(By.cssSelector("p.confirmation-content")).getText().trim();
					boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","PremisesClaimData","ExpUploadMessage",colNumber);
					System.out.println("Message validation status is "+verifyMessage);
					scrolltoElement(driver, pre_ConfirmUpload);
					wait.until(ExpectedConditions.elementToBeClickable(pre_ConfirmUpload)).click();					
					moveFocustoSelectFile.moveToElement(pre_SelectFileButton).build().perform();
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
		captureStdClaimPortalSnap("Files uploaded in Premises claim");
		
		return allUploaded;
				
	}
	
	private void enterClaimDetails(int colNumber,String environment) throws InterruptedException, ParseException {
		
		String claimType= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(pre_selectClaimType, claimType);
		
		//String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ClaimStartDate", colNumber);
		Date date= CommonFunctions.getDateBeforeDays(5);
		String startDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, pre_StartDate);
		enterDataInTextField(pre_StartDate, startDate, wait);
		
		//String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ClaimEndDate", colNumber);
		date= CommonFunctions.getDateBeforeDays(3);
		String endDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, pre_EndDate);
		enterDataInTextField(pre_EndDate, endDate, wait);
		
		scrolltoElement(driver, pre_ContractorCode);
		Thread.sleep(4000);
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode");
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ContractorCode", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(pre_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(pre_ContractorCode, contractorCode);
		
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(pre_EnterDetails, additionalDetails, wait);
		
		String claimAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "ClaimAmount", colNumber);
		enterDataInTextField(pre_ClaimAmountTxtBox, claimAmount, wait);
	}
	
	private void deleteFile() {
		WebElement elmFile= driver.findElement(By.xpath("//table[@id='tblUploadedPremisesClaimDocs']//tbody/tr[1]/td[2]/button"));
		elmFile.click();
		WebElement confirmDelete= driver.findElement(By.xpath("//div[@class='confirmation-buttons text-center']/div/a[2]"));
		confirmDelete.click();

	}
	
	public void updateClaimDetails(int colNumber) {
		scrolltoElement(driver, pre_EnterDetails);
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(pre_EnterDetails, additionalDetails+" added after reverting claim to draft", wait);
		
		scrolltoElement(driver, pre_TickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(pre_TickToConfirm)).click();
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

/*	private List<String> captureOtherValidationMessages(List<String> arrMessage) {
		String performerMsg= driver.findElement(By.id("gmcerrorDiv")).getText();
		arrMessage.add(performerMsg);
		return arrMessage;
	}*/
	
	public void captureValidationMessages(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, pre_selectClaimType);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string + "_2");
		
	}
	
	public void captureStdClaimPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, pre_selectClaimType);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string + "_2");

	}

	
}
