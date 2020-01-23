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

public class SC_RegistrarExpenseClaim extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="select#ddlClaimType")
	WebElement reg_selectClaimType;
	
	@FindBy(css="input#txtRegistrarExpenseStartDate")
	WebElement reg_StartDate;
	
	@FindBy(css="input#txtRegistrarExpenseEndDate")
	WebElement reg_EndDate;
	
	@FindBy(css="input#txtRegExClaim_GMCCode")
	WebElement reg_GmcCodeTxtBox;
	
	@FindBy(css="select[id='ddlContractorCode'][name='RegistrarExpenseClaims.ContractorCode']")
	WebElement reg_ContractorCode;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement reg_SelectFileButton;
	
	@FindBy(xpath="//div[@id='dvRegistrarExpensesClaim']/div[10]/div/div/div/button")
	WebElement reg_FileUploadButton;
	
	@FindBy(css="div.popover.confirmation.fade.top.in")
	WebElement reg_UploadWindow;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement reg_ConfirmUpload;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[1]")
	WebElement reg_CancelUpload;
	
	@FindBy(css="input[name='RegistrarExpenseClaims.TotalClaimAmount']")
	WebElement reg_ClaimAmountTxtBox;
	
	@FindBy(css="textarea[name='RegistrarExpenseClaims.AddDetails']")
	WebElement reg_EnterDetails;
	
	@FindBy(css="input[name='RegistrarExpenseClaims.IsConfirm'][id='CheckToConfirm']")
	WebElement reg_TickToConfirm;
	
	@FindBy(xpath="//div[@id='dvRegistrarExpensesClaim']/div[18]/div[1]/button")
	WebElement reg_CancelButton;
	
	@FindBy(xpath="//div[@id='divConfCancelRegistrarClaim']/div/div/div[3]/div[2]/button")
	WebElement reg_CancelClaimConfirmButton;
	
	@FindBy(xpath="//div[@id='divConfCancelRegistrarClaim']/div/div/div[3]/div[1]/button")
	WebElement reg_CancelClaimButton;
	
	@FindBy(xpath="//button[@data-target='#divConfSaveRegistrarClaim']")
	WebElement reg_saveForLaterButton;
	
	@FindBy(xpath="//div[@id='divConfSaveRegistrarClaim']/div/div/div[3]/div[2]/input")
	WebElement reg_SaveClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSaveRegistrarClaim']/div/div/div[3]/div[1]/button")
	WebElement reg_CancelSavedClaimButton;
	
	@FindBy(xpath="//button[@data-target='#divConfSubmitRegistrarClaim']")
	WebElement reg_submitButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitRegistrarClaim']/div/div/div[3]/div[2]/input")
	WebElement reg_SubmitClaimButton;
	
	@FindBy(xpath="//div[@id='divConfSubmitRegistrarClaim']/div/div/div[3]/div[1]/button")
	WebElement reg_CancelSubmittedClaimButton;
	
	@FindBy(css="tr.item")
	List<WebElement> uploadedRecords;
	
	public SC_RegistrarExpenseClaim(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public SC_RegistrarExpenseClaim createRegistrarClaim(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		uploadFile();
		scrolltoElement(driver, reg_submitButton);
		return new SC_RegistrarExpenseClaim(driver);
	}
	
	public List<String> createVerifyRegistrarClaimData(int colNumber,String environment) throws InterruptedException {
		List<String> claimData= new ArrayList<String>();
		claimData= enterVerifyClaimDetails(colNumber,claimData,environment);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		uploadFile();
		scrolltoElement(driver, reg_submitButton);
		return claimData;
	}
	
	public StdClaimsPortal cancelRegistrarClaim(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		scrolltoElement(driver, reg_CancelButton);
		clickOnCancelButton(colNumber);
		return new StdClaimsPortal(driver);
	}
	
	public boolean validateFileFormatAndClaimData(int colNumber,String environment) throws InterruptedException, ParseException, IOException {
		enterClaimDetails(colNumber,environment);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		boolean allUploaded= attachMultipleFiles(colNumber);
		deleteFile();
		Thread.sleep(3000);
		return allUploaded;
	}

	private void uploadFile() throws InterruptedException {
		
		scrolltoElement(driver, reg_SelectFileButton);
		wait.until(ExpectedConditions.elementToBeClickable(reg_SelectFileButton)).clear();
		CommonFunctions.Uploadfile(StdClaimsPortal.filePath, driver);
		WebElement uploadWindow = driver.switchTo().activeElement();
		uploadWindow.sendKeys(StdClaimsPortal.filePath);
		wait.until(ExpectedConditions.elementToBeClickable(reg_FileUploadButton)).click();
		scrolltoElement(driver, reg_ConfirmUpload);
		wait.until(ExpectedConditions.elementToBeClickable(reg_ConfirmUpload)).click();
		Thread.sleep(3000);
		
	}

	private void enterClaimDetails(int colNumber,String environment) throws InterruptedException, ParseException {
		
		String claimType= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(reg_selectClaimType, claimType);
		
		String gmcCode = ConfigurationData.getRefDataDetails(environment, "StdClaimsGMCCode");
		enterDataInTextField(reg_GmcCodeTxtBox, gmcCode, wait);
		reg_GmcCodeTxtBox.sendKeys(Keys.TAB);
		scrolltoElement(driver, reg_ContractorCode);
		
		//String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimStartDate", colNumber);
		Date date= CommonFunctions.getDateBeforeDays(5);
		String startDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, reg_StartDate);
		enterDataInTextField(reg_StartDate, startDate, wait);
		
		//String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimEndDate", colNumber);
		date= CommonFunctions.getDateBeforeDays(3);
		String endDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, reg_EndDate);
		enterDataInTextField(reg_EndDate, endDate, wait);
		
		scrolltoElement(driver, reg_ContractorCode);
		Thread.sleep(4000);
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode");
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ContractorCode", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(reg_ContractorCode, contractorCode);
		
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(reg_EnterDetails, additionalDetails, wait);
		
		String claimAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimAmount", colNumber);
		enterDataInTextField(reg_ClaimAmountTxtBox, claimAmount, wait);
	}
	
	private List<String> enterVerifyClaimDetails(int colNumber, List<String> claimData, String environment) throws InterruptedException {
		List<String> formData= new ArrayList<String>();
		String claimType= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(reg_selectClaimType, claimType);
		formData.add(claimType);
		
		String gmcCode = ConfigurationData.getRefDataDetails(environment, "StdClaimsGMCCode"); 
		enterDataInTextField(reg_GmcCodeTxtBox, gmcCode, wait);
		reg_GmcCodeTxtBox.sendKeys(Keys.TAB);
		
		scrolltoElement(driver, reg_ContractorCode);
		String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimStartDate", colNumber);
		enterDataInTextField(reg_StartDate, startDate, wait);
		formData.add(startDate);
		
		String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimEndDate", colNumber);
		enterDataInTextField(reg_EndDate, endDate, wait);
		formData.add(endDate);
		
		scrolltoElement(driver, reg_ContractorCode);
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode");
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ContractorCode", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(reg_ContractorCode, contractorCode);
		
		Thread.sleep(3000);
		String contName= getInnerTextByJavaScript("txtContractorName");
		/*String contName= JavaScriptExecutor.executeJavaScriptOnSpecificElement(driver, "return document.getElementById('txtContractorName').value", reg_ContractorName);*/
		formData.add(contName);
				
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "AdditionalDetails", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_EnterDetails)).click();
		reg_EnterDetails.sendKeys(additionalDetails);
		enterDataInTextField(reg_EnterDetails, additionalDetails, wait);
		
		String claimAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ClaimAmount", colNumber);
		enterDataInTextField(reg_ClaimAmountTxtBox, claimAmount, wait);
		
		return formData;
	}
	
	private String getInnerTextByJavaScript(String Id) {
		String string= Support.getValueByJavaScript(driver,Id);
		return string;
	}

	private boolean attachMultipleFiles(int colNumber) throws InterruptedException, IOException {
		File readFolder= new File(StdClaimsPortal.files);
		boolean allUploaded= false;
		File[] fileArray= readFolder.listFiles();
		Actions moveFocustoSelectFile = new Actions(driver);
		for(int i=0;i<fileArray.length;i++){
			String fileName= fileArray[i].toString();
			scrolltoElement(driver, reg_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(reg_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(fileName, driver);
			System.out.println("instance "+i );
			wait.until(ExpectedConditions.elementToBeClickable(reg_FileUploadButton)).click();
			System.out.println("instance "+i +" is passed");
			
			try{
				if(reg_UploadWindow.isDisplayed()){
					String actualMsg= driver.findElement(By.cssSelector("p.confirmation-content")).getText().trim();
					boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "ExpUploadMessage", colNumber);
					scrolltoElement(driver, reg_ConfirmUpload);
					wait.until(ExpectedConditions.elementToBeClickable(reg_ConfirmUpload)).click();					
					moveFocustoSelectFile.moveToElement(reg_SelectFileButton).build().perform();
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
		captureStdClaimPortalSnap("Files uploaded in Registrar claim");
		
		return allUploaded;		
	}
	
	private void deleteFile() {
		System.out.println("Entered in delete file method");
		WebElement elmFile= driver.findElement(By.xpath("//table[@id='tblUploadedRegistrarClaimDocs']//tbody/tr[1]/td[2]/button"));
		elmFile.click();
		WebElement confirmDelete= driver.findElement(By.xpath("//div[@class='confirmation-buttons text-center']/div/a[2]"));
		confirmDelete.click();
		System.out.println("File deleted");
	}
	
	public StdClaimsPortal clickOnSubmitButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, reg_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(reg_submitButton));
		Thread.sleep(2000);
		reg_submitButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSubmitRegistrarClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RegistrarExpenseClaimData","ExpSubmitMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_SubmitClaimButton));
		Thread.sleep(2000);
		reg_SubmitClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnSaveForLaterButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, reg_saveForLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(reg_saveForLaterButton));
		Thread.sleep(2000);
		reg_saveForLaterButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfSaveRegistrarClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RegistrarExpenseClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_CancelSavedClaimButton));
		Thread.sleep(2000);
		reg_CancelSavedClaimButton.click();
		Thread.sleep(2000);
		reg_saveForLaterButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(reg_SaveClaimButton));
		Thread.sleep(2000);
		reg_SaveClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnCancelButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, reg_CancelButton);
		wait.until(ExpectedConditions.elementToBeClickable(reg_CancelButton));
		Thread.sleep(2000);
		reg_CancelButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfCancelRegistrarClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","RegistrarExpenseClaimData","ExpCancelMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(reg_CancelClaimButton));
		Thread.sleep(2000);
		reg_CancelClaimButton.click();
		Thread.sleep(2000);
		reg_CancelButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(reg_CancelClaimConfirmButton));
		Thread.sleep(2000);
		reg_CancelClaimConfirmButton.click();
		return new StdClaimsPortal(driver);
		
	}

	public SC_RegistrarExpenseClaim createRegistrarClaimWithoutFieldValidations(int colNumber,String environment) throws InterruptedException, ParseException {
		enterClaimDetails(colNumber,environment);
		String contractorText= reg_GmcCodeTxtBox.getText().toString();
		contractorText= contractorText+"1";
		enterDataInTextField(reg_GmcCodeTxtBox, contractorText, wait);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		uploadFile();
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
		scrolltoElement(driver, reg_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(reg_submitButton));
		Thread.sleep(2000);
		reg_submitButton.click();
		return new SC_RegistrarExpenseClaim(driver);
	}

	public void updateClaimDetails(int colNumber) {
		scrolltoElement(driver, reg_EnterDetails);
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RegistrarExpenseClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(reg_EnterDetails, additionalDetails+" added after reverting claim to draft", wait);
		
		scrolltoElement(driver, reg_TickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(reg_TickToConfirm)).click();
	}

	public void captureValidationMessages(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, reg_selectClaimType);
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
	


	public boolean verifyElementState() {
		
		boolean allElementsDisabled=false;
		boolean iselementDisabled;
		
		iselementDisabled= StdClaimsPortal.verifyElementStateByTagName(driver,"select");
	       	if(iselementDisabled)
	    	{
	    		allElementsDisabled = true;
	    	}
	       	
		iselementDisabled = StdClaimsPortal.getElementStatebyAttribute(driver,"text");
	       	if(iselementDisabled)
	    	{
	    		allElementsDisabled = true;
	    	}
		       	
		iselementDisabled= StdClaimsPortal.verifyElementStateByTagName(driver,"textarea");
	       	if(iselementDisabled)
	    	{
	    		allElementsDisabled = true;
	    	} 
	       	
		iselementDisabled = StdClaimsPortal.getElementStatebyAttribute(driver,"checkbox");
       	if(iselementDisabled)
	    	{
	    		allElementsDisabled = true;
	    	}
       	
		iselementDisabled = StdClaimsPortal.verifyElementStateByTagName(driver,"button");
       	if(iselementDisabled)
	    	{
	    		allElementsDisabled = true;
	    	}
       	
       	return allElementsDisabled;
	}

	public boolean validateDateFormat() {
		String fromDate= getInnerTextByJavaScript("txtRegistrarExpenseStartDate");
		String toDate= getInnerTextByJavaScript("txtRegistrarExpenseEndDate");
		List<String> date= new ArrayList<String>();
		date.add(fromDate);
		date.add(toDate);
		boolean isDateValid= StdClaimsPortal.compareDateFormat(date,"dd/MM/yyyy");
		return isDateValid;
	}
	
	public void captureStdClaimPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, reg_selectClaimType);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string + "_2");

	}

}
