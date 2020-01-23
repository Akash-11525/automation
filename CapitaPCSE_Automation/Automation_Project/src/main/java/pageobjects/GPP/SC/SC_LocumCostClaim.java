package pageobjects.GPP.SC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import utilities.ExcelUtilities;

public class SC_LocumCostClaim extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input#LocumCostCover_ClaimStartDate")
	WebElement locum_StartDate;
	
	@FindBy(css="input#LocumCostCover_ClaimEndDate")
	WebElement locum_EndDate;
	
	@FindBy(css="select[id='LocumCostCover_ClaimType'][name='LocumCostCover.ClaimType']")
	WebElement locum_selectClaimType;
	
	@FindBy(css="input#LocumCostCover_GMCcode")
	WebElement locum_GmcCodeTxtBox;
	
	@FindBy(css="select[id='ddlContractorCode'][name='LocumCostCover.ContractorCode']")
	WebElement locum_ContractorCode;
	
	@FindBy(xpath="//div[@id='dvNewQualification']//div[2]/div/button[@id='btnNewRecord']")
	WebElement addLocumDetails;
	
	@FindBy(css="input[id='LocumDetails_GMCCode'][name='LocumDetails.GMCCode']")
	WebElement addLocumGMCCode;
	
	@FindBy(css="input[id='LocumDetails_CoverAmount']")
	WebElement addLocumClaimAmount;
	
	@FindBy(css="input#LocumDetails_StartDate")
	WebElement addLocumStartDate;
	
	@FindBy(css="input#LocumDetails_EndDate")
	WebElement addLocumEndDate;
	
	@FindBy(css="button[id='btnSave'][name='btnSaveRecord']")
	WebElement saveAdditionalLocum;
	
	@FindBy(css="input[id='btnCancel'][name='btnCancel']")
	WebElement cancelAdditionalLocum;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement locum_SelectFileButton;
	
	@FindBy(xpath="//div[@id='LocumCostApproval']/div[13]/div/div/div/div/button")
	WebElement locum_FileUploadButton;
	
	@FindBy(css="div.popover.confirmation.fade.top.in")
	WebElement locum_UploadWindow;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[2]")
	WebElement locum_ConfirmUpload;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']/div/a[1]")
	WebElement locum_CancelUpload;
		
	@FindBy(css="textarea[name='LocumCostCover.AdditionalInformation']")
	WebElement locum_EnterDetails;
	
	@FindBy(css="input[name='LocumCostCover.CoverAmount']")
	WebElement locum_ClaimAmountTxtBox;
	
	@FindBy(css="input[id='LocumCostCover_HasConfirm']")
	WebElement locum_TickToConfirm;
	
	@FindBy(xpath="//div[@id='LocumCostApproval']/div[20]/div[1]/button")
	WebElement locum_CancelButton;
	
	@FindBy(xpath="//div[@id='divConfCancelClaim']/div/div/div[3]/div[2]/button")
	WebElement locum_CancelClaimConfirmButton;
	
	@FindBy(xpath="//div[@id='divConfCancelClaim']/div/div/div[3]/div[1]/button")
	WebElement locum_CancelClaimButton;
	
	@FindBy(css="input#tbnClaimSaveltr")
	WebElement locum_saveForLaterButton;
	
	@FindBy(css="button[value='Confirm Draft']")
	WebElement locum_SaveClaimButton;
	
	@FindBy(xpath="//div[@id='divDraftClaim']/div/div/div[3]/div[1]/button")
	WebElement locum_CancelSavedClaimButton;
	
	@FindBy(css="input#tbnClaimSub")
	WebElement locum_submitButton;
	
	@FindBy(css="button[class='btn btn-success'][value='Confirm Submit']")
	WebElement locum_SubmitClaimButton;
	
	@FindBy(xpath="//div[@id='divClaimSubmission']/div/div/div[3]/div[1]/button")
	WebElement locum_CancelSubmittedClaimButton;
	
	@FindBy(css="tr.item")
	List<WebElement> uploadedRecords;
	
	public SC_LocumCostClaim(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public void createLocumCostClaimApplication(int colNumber) throws InterruptedException {
		enterClaimDetails(colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		uploadFile();
		Thread.sleep(3000);
		scrolltoElement(driver, locum_submitButton);
		Thread.sleep(4000);
		System.out.println("located submit locum cost claim button");
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		System.out.println("clicked on submit locum cost claim button");
		
	}
	
	public void createLocumCostClaimWithoutFieldValidations(int colNumber) throws InterruptedException {
		enterClaimDetails(colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		uploadFile();
		scrolltoElement(driver, locum_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(locum_submitButton));
		Thread.sleep(2000);
		locum_submitButton.click();
	}
	
	public boolean validateFileFormatAndClaimData(int colNumber) throws InterruptedException, IOException {
		enterClaimDetails(colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		boolean allUploaded=attachMultipleFiles(colNumber);
		deleteFile();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		Thread.sleep(3000);
		return allUploaded;
	}
	
	public StdClaimsPortal cancelLocumCostClaim(int colNumber) throws InterruptedException {
		enterClaimDetails(colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
		scrolltoElement(driver, locum_CancelButton);
		clickOnCancelButton(colNumber);
		return new StdClaimsPortal(driver);
	}
	
	public void updateClaimDetails(int colNumber) {
		scrolltoElement(driver, locum_EnterDetails);
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "AdditionalDetails", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_EnterDetails)).click();
		locum_EnterDetails.clear();
		locum_EnterDetails.sendKeys(additionalDetails+" added after reverting claim to draft");
		
		scrolltoElement(driver, locum_TickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(locum_TickToConfirm)).click();
	}
	

	private void enterClaimDetails(int colNumber) {
	
/*		String startDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "StartDate", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_StartDate)).click();
		locum_StartDate.sendKeys(startDate);
		
		String endDate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "EndDate", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_EndDate)).click();
		locum_EndDate.sendKeys(endDate);
	
		String claimType= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "ClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(locum_selectClaimType, claimType);
		
		String gmcCode = ConfigurationData.StdClaim_GMCCode;
		wait.until(ExpectedConditions.elementToBeClickable(locum_GmcCodeTxtBox)).click();
		locum_GmcCodeTxtBox.sendKeys(gmcCode);
		locum_GmcCodeTxtBox.sendKeys(Keys.TAB);
		scrolltoElement(driver, locum_ContractorCode);*/

/*		String contractorCode= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "ContractorCode", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_ContractorCode)).click();
		CommonFunctions.selectOptionFromDropDown(locum_ContractorCode, contractorCode);*/
		
		String additionalDetails= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "AdditionalDetails", colNumber);
		enterDataInTextField(locum_EnterDetails, additionalDetails, wait);
		
		String claimAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData", "ClaimAmount", colNumber);
		enterDataInTextField(locum_ClaimAmountTxtBox, claimAmount, wait);
	}

	private void uploadFile() throws InterruptedException {
		scrolltoElement(driver, locum_SelectFileButton);
		wait.until(ExpectedConditions.elementToBeClickable(locum_SelectFileButton)).clear();
		CommonFunctions.Uploadfile(StdClaimsPortal.filePath, driver);
		wait.until(ExpectedConditions.elementToBeClickable(locum_FileUploadButton)).click();
		scrolltoElement(driver, locum_ConfirmUpload);
		wait.until(ExpectedConditions.elementToBeClickable(locum_ConfirmUpload));
		Thread.sleep(3000);
		locum_ConfirmUpload.click();
		Thread.sleep(3000);
	}
	
	private boolean attachMultipleFiles(int colNumber) throws InterruptedException, IOException {
		File readFolder= new File(StdClaimsPortal.files);
		boolean allUploaded= false;
		File[] fileArray= readFolder.listFiles();
		Actions moveFocustoSelectFile = new Actions(driver);
		for(int i=0;i<fileArray.length;i++){
			String fileName= fileArray[i].toString();
			scrolltoElement(driver, locum_SelectFileButton);
			wait.until(ExpectedConditions.elementToBeClickable(locum_SelectFileButton)).clear();
			CommonFunctions.Uploadfile(fileName, driver);
			System.out.println("instance "+i );
			wait.until(ExpectedConditions.elementToBeClickable(locum_FileUploadButton)).click();
			System.out.println("instance "+i +" is passed");
			
			try{
				if(locum_UploadWindow.isDisplayed()){
					String actualMsg= driver.findElement(By.cssSelector("p.confirmation-content")).getText().trim();
					boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumCostClaimData","ExpUploadMessage",colNumber);
					System.out.println("Message validation status is "+verifyMessage);
					scrolltoElement(driver, locum_ConfirmUpload);
					wait.until(ExpectedConditions.elementToBeClickable(locum_ConfirmUpload));
					Thread.sleep(3000);
					locum_ConfirmUpload.click();				
					moveFocustoSelectFile.moveToElement(locum_SelectFileButton).build().perform();
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
		captureValidationMessages("Files uploaded in Locum cost claim");
		
		return allUploaded;		
	}
	
	private void deleteFile() {
		WebElement elmFile= driver.findElement(By.xpath("//table[@id='tblUploadedRetainerClaimDocs']//tbody/tr[1]/td[2]/button"));
		System.out.println("Delete button is located under locum cost claim module");
		elmFile.click();
		WebElement confirmDelete= driver.findElement(By.xpath("//div[@class='confirmation-buttons text-center']/div/a[2]"));
		confirmDelete.click();

	}
	
	public StdClaimsPortal clickOnSubmitButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locum_submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(locum_submitButton));
		Thread.sleep(2000);
		locum_submitButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divClaimSubmission']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumCostClaimData","ExpSubmitMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_SubmitClaimButton));
		Thread.sleep(2000);
		locum_SubmitClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnSaveForLaterButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locum_saveForLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(locum_saveForLaterButton));
		Thread.sleep(2000);
		locum_saveForLaterButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divDraftClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumCostClaimData","ExpDraftMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_CancelSavedClaimButton));
		Thread.sleep(2000);
		locum_CancelSavedClaimButton.click();
		Thread.sleep(2000);
		locum_saveForLaterButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(locum_SaveClaimButton));
		Thread.sleep(2000);
		locum_SaveClaimButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public StdClaimsPortal clickOnCancelButton(int colNumber) throws InterruptedException{
		
		scrolltoElement(driver, locum_CancelButton);
		wait.until(ExpectedConditions.elementToBeClickable(locum_CancelButton));
		Thread.sleep(2000);
		locum_CancelButton.click();
		Thread.sleep(2000);
		String actualMsg= driver.findElement(By.xpath("//div[@id='divConfCancelClaim']//div[@class='modal-body']")).getText().trim();
		boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","LocumCostClaimData","ExpCancelMessage",colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(locum_CancelClaimButton));
		Thread.sleep(2000);
		locum_CancelClaimButton.click();
		Thread.sleep(2000);
		locum_CancelButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(locum_CancelClaimConfirmButton));
		Thread.sleep(2000);
		locum_CancelClaimConfirmButton.click();
		return new StdClaimsPortal(driver);
		
	}
	
	public void captureValidationMessages(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, locum_selectClaimType);
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
			 
		}
		catch(NoSuchElementException e)
		{
			System.out.println("No such element is found. " +e);
		}
		return ArrMessage;
		
	}

}
