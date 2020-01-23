package pageobjects.CS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class ColposcopyDischargeReport extends Support {
	WebDriver driver;
	WebDriverWait wait;
	//public static String coloscopyFilePath = System.getProperty("user.dir") + "\\Upload\\CSTestFiles\\ColposcopyUploadFile.csv";
	public static String coloscopyFilePath = System.getProperty("user.dir")+ConfigurationData.ColposcopyUploadFilePath+"\\ColposcopyUpload.csv";
	
	@FindBy(linkText="Colposcopy List")
	WebElement colposcopyTab;
	
	@FindBy(linkText="Uploaded Colposcopy Records")
	WebElement recordsTab;
	
	@FindBy(id="NHSNumber")
	WebElement NHSNumber;
	
	@FindBy(id="Forename")
	WebElement forename;
	
	@FindBy(id="Surname")
	WebElement surname;
	
	@FindBy(id="DOB")
	WebElement dateOfBirth;
	
	@FindBy(id="DateSeenInClinic")
	WebElement dateSeen;
	
	@FindBy(id="DateOfNextTest")
	WebElement nextDate;
	
	@FindBy(xpath="//div[@class='btn-group']/button[1]")
	WebElement addButton;
	
	@FindBy(id="colposcopyFileUpload")
	WebElement fileUpload;
	
	@FindBy(id="colposcopySubmit")
	WebElement uploadColposcopyFile;
	
	@FindBy(css="input[title='Search']")
	WebElement searchPatients;
	
	@FindBy(xpath="//div[@class='pull-right']/button")
	WebElement submitColposcopy;
	
	@FindBy(xpath="//form[@id='form0']/div[2]/div[3]/div/div/button[2]")
	WebElement cancelButton;
	
	@FindBy(xpath="//form[@id='form0']/div[2]/div[3]/div/div/button[1]")
	WebElement updateButton;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement deleteModalWindow;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[1]")
	WebElement cancelDelete;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[2]")
	WebElement confirmDelete;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement submitModalWindow;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[2]")
	WebElement confirmSubmit;
	
	public ColposcopyDischargeReport(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error']"));
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
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}

	public List<String> enterColposcopyData() throws InterruptedException {
		List<String> manualEntries= new ArrayList<String>();
		for(int i=1;i<=3;i++){
			String nhsNumber= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "NHSNo", i);
			String forename= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "Forename", i);
			String surname= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "Surname", i);
			String dateOfBirth= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "DateofBirth", i);
			String dateLastSeen= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "DateSeeninClinic", i);
			String nextDueDate= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "ColposcopyTestData", "NextTestDueDate", i);
			enterPatientData(nhsNumber,forename,surname,dateOfBirth,dateLastSeen,nextDueDate);
			clickOnAddButton();
			System.out.println("Data is entered for patient: "+forename+" "+surname+" having NHS Number: "+nhsNumber);
			manualEntries.add(nhsNumber);
			
		}
		return manualEntries;
	}

/*	private String getActualFailureReason() {
		String failureReason="";
		WebElement reason= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[9]"));
		failureReason= reason.getText().toString();
		if(failureReason.length()==0){
			failureReason="Valid";
		}
		return failureReason;
	}*/

	public void clickOnAddButton() throws InterruptedException {
		scrolltoElement(driver, addButton);
		wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
		Thread.sleep(3000);
		
	}
	
	public UploadedColposcopyRecords clickOnUploadedRecordsTab() throws InterruptedException {
		scrolltoElement(driver,recordsTab );
		wait.until(ExpectedConditions.elementToBeClickable(recordsTab)).click();
		Thread.sleep(3000);
		return new UploadedColposcopyRecords(driver);
	}

	private void enterPatientData(String nhsNumber2, String forename2, String surname2, String dateOfBirth2,
			String dateLastSeen, String nextDueDate) {
		
		wait.until(ExpectedConditions.visibilityOf(NHSNumber));
		scrolltoElement(driver, NHSNumber);
		wait.until(ExpectedConditions.elementToBeClickable(NHSNumber)).clear();
		NHSNumber.sendKeys(nhsNumber2);
		
		wait.until(ExpectedConditions.visibilityOf(forename));
		wait.until(ExpectedConditions.elementToBeClickable(forename)).clear();
		forename.sendKeys(forename2);
		
		wait.until(ExpectedConditions.visibilityOf(surname));
		wait.until(ExpectedConditions.elementToBeClickable(surname)).clear();
		surname.sendKeys(surname2);
		
		wait.until(ExpectedConditions.visibilityOf(dateOfBirth));
		wait.until(ExpectedConditions.elementToBeClickable(dateOfBirth)).clear();
		dateOfBirth.sendKeys(dateOfBirth2);
		
		wait.until(ExpectedConditions.visibilityOf(dateSeen));
		wait.until(ExpectedConditions.elementToBeClickable(dateSeen)).clear();
		dateSeen.sendKeys(dateLastSeen);
		
		wait.until(ExpectedConditions.visibilityOf(nextDate));
		wait.until(ExpectedConditions.elementToBeClickable(nextDate)).clear();
		nextDate.sendKeys(nextDueDate);
		
	}

	public void captureColposcopyScreenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, forename);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public ColposcopyDischargeReport uploadColposcopyFile() throws InterruptedException {
		scrolltoElement(driver, fileUpload);
		wait.until(ExpectedConditions.elementToBeClickable(fileUpload)).clear();
		CommonFunctions.Uploadfile(coloscopyFilePath, driver);
		
		wait.until(ExpectedConditions.elementToBeClickable(uploadColposcopyFile)).click();
		Thread.sleep(3000);
		return new ColposcopyDischargeReport(driver);
	}

	public List<Integer> validateFailureReason() throws InterruptedException {
		List<Integer> fileCount= new ArrayList<Integer>();
		int unmatchedCount=0,matchedCount=0;

		String unmatchedMsg= "Unable to Match - NHS No, DOB, Forename and Surname do not match to an existing record.";
		String duplicateMsg= "Duplicate Colposcopy record- Colposcopy record already exists.";
		String lockedMsg= "The patient is locked.";
		int recordsCount= driver.findElements(By.xpath("//table[@id='colposcopytable']//tbody/tr")).size();
		// this will validate the failure reason with the expected one
		for(int i=1;i<=recordsCount;i++){
			WebElement element= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr["+i+"]/td[9]"));
			String failureReason= element.getText().toString();
			//new logic starts
			if(failureReason.length()==0){
				matchedCount= matchedCount+1;
			}else if(failureReason.equalsIgnoreCase(unmatchedMsg)){				
				unmatchedCount= unmatchedCount+1;
			}else if(failureReason.equalsIgnoreCase(duplicateMsg)){
				unmatchedCount= unmatchedCount+1;
			}else if(failureReason.equalsIgnoreCase(lockedMsg)){
				unmatchedCount= unmatchedCount+1;
			}
			// new logic ends
		}

		fileCount.add(matchedCount);
		fileCount.add(unmatchedCount);
		return fileCount;
	}

	public boolean verifyRecordCount(List<Integer> recordCount) {
		scrolltoElement(driver, fileUpload);
		boolean isCountVerified= false;
		
		List<Integer> recordCountList= getRecordCount();
		int gridCount= recordCountList.get(0);
		int portalMatchedCount= recordCountList.get(1);
		int portalUnmatchedCount= recordCountList.get(2);
		
		int expMatchedCount= recordCount.get(0);
		int expUnmatchedCount= recordCount.get(1);
		int totalExpCount= expMatchedCount+expUnmatchedCount;
		
		if(gridCount==totalExpCount){
			System.out.println("Total file count is verified");
			if(portalMatchedCount==expMatchedCount){
				System.out.println("Matched file count is verified");
				if(portalUnmatchedCount==expUnmatchedCount){
					System.out.println("Unmatched file count is verified");
					isCountVerified=true;
				}
			}
		}
		return isCountVerified;
	}

	public List<Integer> getRecordCount() {
		List<Integer> recordCountList= new ArrayList<Integer>();
		int gridCount= driver.findElements(By.xpath("//table[@id='colposcopytable']//tbody/tr")).size();
		recordCountList.add(gridCount);
		
		String strPortalMatchedCount= driver.findElement(By.xpath("//table[@class='table']//tbody/tr/td[2]")).getText().toString();
		int portalMatchedCount= Integer.parseInt(strPortalMatchedCount);
		recordCountList.add(portalMatchedCount);
		
		String strPortalUnmatchedCount= driver.findElement(By.xpath("//table[@class='table']//tbody/tr/td[3]")).getText().toString();
		int portalUnmatchedCount= Integer.parseInt(strPortalUnmatchedCount);
		recordCountList.add(portalUnmatchedCount);
		
		return recordCountList;
	}

	public boolean updateRecordAndCheckButtonVisibility() throws InterruptedException {
		boolean isButtonVisible=false;
		WebElement record=null; 
		record=driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[3]/td[11]/div/button[1]"));
		record.click();
		scrolltoElement(driver,colposcopyTab);
		wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
		wait.until(ExpectedConditions.elementToBeClickable(updateButton));
			if(cancelButton.isEnabled()){
			System.out.println("Cancel button is enabled after clicking on Edit button under the grid");
			if(updateButton.isEnabled()){
				System.out.println("Update button is enabled after clicking on Edit button under the grid");
				isButtonVisible=true;
			}
		}
		cancelButton.click();
		Thread.sleep(2000);
		record=driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[11]/div/button[1]"));
		record.click();
		scrolltoElement(driver,colposcopyTab);
		updateExistingDetails();
		Thread.sleep(2000);
		updateButton.click();
		return isButtonVisible;
	}

	private void updateExistingDetails() {
		wait.until(ExpectedConditions.visibilityOf(forename));
		wait.until(ExpectedConditions.elementToBeClickable(forename)).clear();
		forename.sendKeys("Forename updated");
		
		wait.until(ExpectedConditions.visibilityOf(surname));
		wait.until(ExpectedConditions.elementToBeClickable(surname)).clear();
		surname.sendKeys("Surname updated");
	}

	public boolean cancelDeleteAction() throws InterruptedException {
		boolean messageAndCancelDelete= false;
		WebElement record=null; 
		record=driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[11]/div/button[2]"));
		scrolltoElement(driver, record);
		WebElement gridNHSNumber= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[2]"));
		String strNHSNumber= gridNHSNumber.getText().toString();
		WebElement gridForename= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[3]"));
		String strGridForename= gridForename.getText().toString();
		record.click();
		Thread.sleep(3000);
		String actualDeleteMesg= driver.findElement(By.xpath("//div[@class='modal-body']")).getText().toString();
		String message1= "Are you sure you want to remove the record for ";
		String expDeleteMesg= message1+strNHSNumber+" "+strGridForename+"?";
				
		//if(deleteModalWindow.isDisplayed()){
			if(expDeleteMesg.equalsIgnoreCase(actualDeleteMesg)){
				cancelDelete.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(uploadColposcopyFile));
				System.out.println("Clicked on cancel delete after verifying message");
				messageAndCancelDelete=true;
			}
		//}
		return messageAndCancelDelete;
	}

	public boolean deleteRecord(List<Integer> recordCount) throws InterruptedException {
		boolean isCountUpdated= false;
		int matchedCount=0, unmatchedCount=0;
		WebElement record=null;
		int OldGridCount= recordCount.get(0)+recordCount.get(1);
		int oldMatchedCount= recordCount.get(0);
		int oldUnmatchedCount= recordCount.get(1);
		
		WebElement element= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[9]"));
		String failureReason= element.getText().toString();
		record=driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr[1]/td[11]/div/button[2]"));
		record.click();
		Thread.sleep(3000);
		
		//if(deleteModalWindow.isDisplayed()){
			confirmDelete.click();
			Thread.sleep(7000);
			wait.until(ExpectedConditions.elementToBeClickable(uploadColposcopyFile));
			
			System.out.println("Clicked on confirm delete");
			
			if(failureReason.length()==0){
				matchedCount= matchedCount+1;
			}else{
				unmatchedCount= unmatchedCount+1;
			}
			
			List<Integer> newCountList= getRecordCount();
			int newGridCount= newCountList.get(0);
			int newMatchedCount= newCountList.get(1);
			int newUnmatchedCount= newCountList.get(2);
			
			if((OldGridCount-newGridCount)==1){
				if((oldMatchedCount-newMatchedCount)==1){
					isCountUpdated=true;
				}else if((oldUnmatchedCount-newUnmatchedCount)==1){
					isCountUpdated=true;
				}
			}
		//}
		return isCountUpdated;
	}

	public boolean clickOnSubmitButton() throws InterruptedException {
		boolean isSubmit=false;
		List<Integer> recordCount= getRecordCount();
		String message= "You are about to submit "+recordCount.get(0)+" record(s) with "+recordCount.get(2)+" unmatched.";
		String expsubmitMessage= message+System.lineSeparator()+"Are you sure you want to submit the Colposcopy Discharge Report?";
		submitColposcopy.click();
		Thread.sleep(3000);
		String actualSubmitMesg= driver.findElement(By.xpath("//div[@class='modal-body']")).getText().toString();
		String message1= actualSubmitMesg.substring(0, (actualSubmitMesg.indexOf(".")+1));
		wait.until(ExpectedConditions.elementToBeClickable(submitColposcopy));
		//if(submitModalWindow.isDisplayed()){
		
			if(message.equalsIgnoreCase(message1)){
				confirmSubmit.click();
				Thread.sleep(3000);
				System.out.println("Colposcopy data has been submitted");
				
				Thread.sleep(3000);
				isSubmit=true;
			}
	//	}
		
		return isSubmit;
	}

	public boolean deleteExistingRecords() throws InterruptedException {
		boolean oldData= false;
		int afterDeletecount=0;
		int recordsCount= driver.findElements(By.xpath("//table[@id='colposcopytable']//tbody/tr")).size();
		if(recordsCount>0){
			for(int i=1;i<=recordsCount;i=1){
				WebElement element= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr["+i+"]/td[11]/div/button[2]"));
				element.click();
				Thread.sleep(3000);
				confirmDelete.click();
				Thread.sleep(5000);
				wait.until(ExpectedConditions.elementToBeClickable(uploadColposcopyFile));
				System.out.println("Deleted record count is "+i+", amongst total record count "+recordsCount);
				afterDeletecount= afterDeletecount+1;
				if(recordsCount==afterDeletecount){
					break;
				}
				
			}
			
			if(recordsCount==afterDeletecount){
				oldData=true;
				
			}else{
				oldData=false;
			}
		}else{
			System.out.println("Grid is empty");
			oldData=true;
		}
		new ColposcopyDischargeReport(driver);
		return oldData;
	}
	
	public List<String> getMatchedNHSNos() {
		
		List<String> inputData= new ArrayList<String>();
		List<String> finalInputData= new ArrayList<String>();
		int gridCount= driver.findElements(By.xpath("//table[@id='colposcopytable']//tbody/tr")).size();
		for(int rowNum=1;rowNum<=gridCount;rowNum++){
			WebElement reason= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr["+rowNum+"]/td[9]"));
			String failureReason= reason.getText().toString();
			if(failureReason.length()==0){
				inputData= getColposcopyGridDetails(rowNum);
				finalInputData.addAll(inputData);
			}
		}
		return finalInputData;
	}

	private List<String> getColposcopyGridDetails(int rowNum) {
		WebElement element=null;
		List<String> inputData= new ArrayList<String>();
		element= driver.findElement(By.xpath("//table[@id='colposcopytable']//tbody/tr["+rowNum+"]/td[2]"));
		String strNHSNumber= element.getText().toString();
		inputData.add(strNHSNumber);
		return inputData;
	}

		
}