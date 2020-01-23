package pageobjects.CS;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

public class HPV_Vaccination extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public static String HPVFilePath = System.getProperty("user.dir")+ConfigurationData.HPVUploadFilePath+"\\HPVUpload.csv";
	
	@FindBy(linkText="Uploaded HPV Records")
	WebElement recordsTab;
	
	@FindBy(id="NHSNumber")
	WebElement NHSNumber;
	
	@FindBy(id="Forename")
	WebElement forename;
	
	@FindBy(id="Surname")
	WebElement surname;
	
	@FindBy(id="DOB")
	WebElement dateOfBirth;
	
	@FindBy(css="select#VaccinationType")
	WebElement vaccinationType;
	
	@FindBy(id="VaccinationDate")
	WebElement vaccinationDate;
	
	@FindBy(id="btnBatchCode")
	WebElement addButton;
	
	@FindBy(id="UploadCsv")
	WebElement fileUpload;
	
	@FindBy(id="UploadBtn")
	WebElement uploadHPVFile;
	
	@FindBy(css="input[title='Search']")
	WebElement searchPatients;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement deleteModalWindow;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[1]")
	WebElement cancelDelete;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[2]")
	WebElement confirmDelete;
	
	@FindBy(css="button.btn.btn-default.submit-hpvvaccinations")
	WebElement submitButton;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement submitModalWindow;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[1]")
	WebElement cancelSubmit;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[2]")
	WebElement confirmSubmit;
	
	
	public HPV_Vaccination(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void clickOnAddButton() throws InterruptedException {
		scrolltoElement(driver, addButton);
		wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
		Thread.sleep(3000);
	}
	
	public void clickOnSubmitButton() throws InterruptedException {
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(3000);
	}
	
	public void clickOnConfirmSubmitButton() throws InterruptedException {
		scrolltoElement(driver, confirmSubmit);
		wait.until(ExpectedConditions.visibilityOf(submitModalWindow));
		wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
		Thread.sleep(3000);
	}
	
	public UploadedHPVRecords clickOnRecordsTab() throws InterruptedException {
		Thread.sleep(5000);
		scrolltoElement(driver, recordsTab);
		wait.until(ExpectedConditions.elementToBeClickable(recordsTab)).click();
		Thread.sleep(2000);
		return new UploadedHPVRecords(driver);
	}

	public void captureHPVScreenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, forename);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
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

	public boolean deleteExistingRecords() throws InterruptedException, IOException {
		boolean oldData= false;
		int afterDeletecount=0;
		try{
			
			int recordsCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
			if(recordsCount>0){
				for(int i=1;i<=recordsCount;i=1){
					WebElement element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[11]/button"));
					element.click();
					Thread.sleep(3000);
					confirmDelete.click();
					Thread.sleep(5000);
					wait.until(ExpectedConditions.elementToBeClickable(uploadHPVFile));
					afterDeletecount= afterDeletecount+1;
					System.out.println("Deleted record count is "+afterDeletecount+", amongst total record count "+recordsCount);
					
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
			
		}catch(Exception e){
			scrolltoElement(driver,recordsTab);
			captureHPVScreenshot("Error message on deletion");
		}

		return oldData;
	}

	public TreeMap<String, Boolean> enterHPVDataAndVerifyValidation(int startIndex, int endIndex, String key) throws InterruptedException, ParseException, IOException {
		
		TreeMap<String,Boolean> manualEntries= new TreeMap<String,Boolean>();
		for(int i=startIndex;i<=endIndex;i++){
			String nhsNumber= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", i);
			String forename= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Forename", i);
			String surname= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Surname", i);
			String dateOfBirth= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "DateofBirth", i);
			String vaccinationType= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Vaccination Type", i);
			/*String vaccinationDate= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NextTestDueDate", i);*/
			enterPatientData(nhsNumber,forename,surname,dateOfBirth,vaccinationType);
			captureHPVScreenshot(key+"_Field validation messages");
			boolean isValidation= validateFieldMessage("NHSNumber");
			clickOnAddButton();
			System.out.println("Data is entered for patient: "+forename+" "+surname+" having NHS Number: "+nhsNumber);
			manualEntries.put(nhsNumber, isValidation);
		}
		return manualEntries;
	}
	
	public HPV_Vaccination enterHPVData(int startIndex, int endIndex) throws InterruptedException, ParseException, IOException {
		
		for(int colNum=startIndex;colNum<=endIndex;colNum++){
			
			String nhsNumber= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NHSNo", colNum);
			String forename= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Forename", colNum);
			String surname= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Surname", colNum);
			String dateOfBirth= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "DateofBirth", colNum);
			String vaccinationType= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "Vaccination Type", colNum);
			/*String vaccinationDate= ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "HPVTestData", "NextTestDueDate", i);*/
			enterPatientData(nhsNumber,forename,surname,dateOfBirth,vaccinationType);
			clickOnAddButton();
			boolean recordStatus= getRecordStatus(colNum);
			SoftAssert softAssert= new SoftAssert();
			softAssert.assertEquals(recordStatus, true, "Failure reason is not matched for column index: "+colNum);
			System.out.println("Data is entered for patient: "+forename+" "+surname+" having NHS Number: "+nhsNumber);
			Thread.sleep(3000);
			}
		return new HPV_Vaccination(driver);
	}


	private boolean validateFieldMessage(String fieldName) throws IOException {
		boolean isValidation=false;
		String expMessage="";
		List<String> MessageList= AcutalErrormessage();
		if(MessageList.isEmpty()){
			isValidation=false;
		}else{
			//
			switch(fieldName){
			case "NHSNumber":
				expMessage= ExcelUtilities.getExcelCellValue("CSTESTDATA.xlsx", "ValidationMessages", 1, 3);
				break;
			case "fileType":
				expMessage= ExcelUtilities.getExcelCellValue("CSTESTDATA.xlsx", "ValidationMessages", 2, 3);
				break;
			default:
				Verify.fail(fieldName+" not found");
			}
			//
			for(String message:MessageList){
				if(expMessage.equalsIgnoreCase(message)){
					isValidation=true;
				}
			}
			
		}

		return isValidation;
	}
	
	private void enterPatientData(String nhsNumber2, String forename2, String surname2, String dateOfBirth2,
			String strVaccinationType) throws ParseException {
		Date date=null;
		String strDate="";
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
		date= CommonFunctions.convertStringtoCalDate(dateOfBirth2, "MM/dd/yyyy");
		strDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		dateOfBirth.sendKeys(strDate);
		
		wait.until(ExpectedConditions.visibilityOf(vaccinationType));
		CommonFunctions.selectOptionFromDropDown(vaccinationType, strVaccinationType);
				
		wait.until(ExpectedConditions.visibilityOf(vaccinationDate));
		wait.until(ExpectedConditions.elementToBeClickable(vaccinationDate)).clear();
		date= CommonFunctions.getDateBeforeDays(2);
		strDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		//date= CommonFunctions.convertStringtoCalDate(strVaccinationDate, "MM/dd/yyyy");
		vaccinationDate.sendKeys(strDate);
		
	}

	public boolean validateClaimTypes(int colNumber) throws IOException {
		boolean isTypeVerified;
		scrolltoElement(driver, vaccinationType);
		List<String> finalClaimTypes= new ArrayList<String>();
		List<String> strclaimTypes = ExcelUtilities.getCellValuesInExcel("CSTESTDATA.xlsx", "CSOptions", colNumber);
		String[] claimArray = strclaimTypes.get(0).split(",");
		
		List<String> expclaims = Arrays.asList(claimArray);
		int expClaimCount = expclaims.size();
		int actClaimCount = driver.findElements(By.xpath("//select[@id='VaccinationType']/option")).size();
		
		for (int i = 0; i < expClaimCount; i++) 
		{
			String expected = expclaims.get(i).toString().trim();
			for (int j = 1; j < actClaimCount+1; j++) 
			{
				WebElement actVaccinationType = driver.findElement(By.xpath("//select[@id='VaccinationType']/option["+j+"]"));
				String actual = actVaccinationType.getText().trim();
				if (actual.equalsIgnoreCase(expected)) 
				{
					System.out.println("Option is matched for " + actual);
					finalClaimTypes.add(actual);
					break;
				}

			}
		}
		
		if(expclaims.size()==finalClaimTypes.size()){
			isTypeVerified=true;
		}else{
			isTypeVerified=false;
		}

		return isTypeVerified;
	}
	
	public boolean getRecordStatus(int colNum) throws InterruptedException {
		boolean isValid=false;
			WebElement element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr[1]/td[9]"));
			String failureReason= element.getText().toString();
			if(failureReason.length()==0){
				failureReason="Valid";
				isValid=true;
			}else{
				isValid=false;
			}
			boolean isVerified= CommonFunctions.verifyMessageWithExcel(failureReason, "CSTESTDATA.xlsx", "HPVTestData", "PatientStatus",colNum );
			SoftAssert softAssert= new SoftAssert();
			softAssert.assertEquals(isVerified, true, "Failure reason is not matched for column index: "+colNum);
		return isValid;
	}
	
	public List<Integer> getCountFromPatientGrid() throws InterruptedException {
		List<Integer> fileCount= new ArrayList<Integer>();
		int unmatchedCount=0,matchedCount=0;
		int columnCount= ExcelUtilities.getColumnCountFromExcel("CSTESTDATA.xlsx", "HPVTestData");
		int recordsCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
		System.out.println("Column count is: "+columnCount);
		// this will capture the failure reason from UI in a list
		for(int i=1;i<=recordsCount;i++){
			WebElement element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[9]"));
			String failureReason= element.getText().toString();
			if(failureReason.length()==0){
				matchedCount= matchedCount+1;
			}else{
				unmatchedCount= unmatchedCount+1;
			}
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
		int gridCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
		recordCountList.add(gridCount);
		
		String strPortalMatchedCount= driver.findElement(By.xpath("//table[@class='table']//tbody/tr/td[2]")).getText().toString();
		int portalMatchedCount= Integer.parseInt(strPortalMatchedCount);
		recordCountList.add(portalMatchedCount);
		
		String strPortalUnmatchedCount= driver.findElement(By.xpath("//table[@class='table']//tbody/tr/td[3]")).getText().toString();
		int portalUnmatchedCount= Integer.parseInt(strPortalUnmatchedCount);
		recordCountList.add(portalUnmatchedCount);
		
		return recordCountList;
	}

	public boolean verifyPatientDetailsFromPatientSearch() throws InterruptedException, IOException {
		boolean isVerified=false;
		List<String> vaccineData= new ArrayList<String>();
		int gridCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
		
		for(int i=1;i<=gridCount;i++){
			WebElement element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[9]"));
			String failureReason= element.getText().toString();
			if(failureReason.length()==0){
				WebElement NHSNumber= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[2]"));
				String strNHSNumber= NHSNumber.getText().toString();
				vaccineData.add(strNHSNumber);
				
				WebElement vaccineType= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[6]"));
				String strVaccineType= vaccineType.getText().toString();
				vaccineData.add(strVaccineType);
				
				WebElement vaccineDate= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+i+"]/td[7]"));
				String strVaccineDate= vaccineDate.getText().toString();
				vaccineData.add(strVaccineDate);
				break;
			}
			
		}
		String NHSNumber= vaccineData.get(0);
		captureHPVScreenshot("HPV data for NHS number "+NHSNumber);
		clickOnSubmitButton();
		clickOnConfirmSubmitButton();
		PortalHome objPortalHome= new PortalHome(driver);
		PatientsearchPortal objPatientsearchPortal= objPortalHome.clickPatientsearchClick();
		objPatientsearchPortal=  objPatientsearchPortal.EnterNHSNumberandclickOnresult(NHSNumber);
		PatientpersonaldetailPortal objPatientpersonaldetailPortal= objPatientsearchPortal.FirstResultofsearchOnSubmitted(NHSNumber);
		objPatientpersonaldetailPortal= objPatientpersonaldetailPortal.ClickOnHPVTab();
		isVerified= objPatientpersonaldetailPortal.getHPVDataStatus(vaccineData);
		return isVerified;
	}

	public List<String> getMatchedPatientData() {
		
		List<String> inputData= new ArrayList<String>();
		int gridCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
		for(int rowNum=1;rowNum<=gridCount;rowNum++){
			WebElement reason= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[9]"));
			String failureReason= reason.getText().toString();
			if(failureReason.length()==0){
				inputData= getHPVGridDetails(rowNum);
				break;
			}
		}
		return inputData;
	}
	
	public List<String> getUnmatchedPatientData() {
		
		List<String> inputData= new ArrayList<String>();
		int gridCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr")).size();
		String unmatchedMessage= "Unable to match NHS No and DOB.";
		for(int rowNum=1;rowNum<=gridCount;rowNum++){
			WebElement reason= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[9]"));
			String failureReason= reason.getText().toString();
			if(failureReason.equalsIgnoreCase(unmatchedMessage)){
				inputData= getHPVGridDetails(rowNum);
				break;
			}
		}
		return inputData;
	}

	private List<String> getHPVGridDetails(int rowNum) {
		WebElement element=null;
		List<String> inputData= new ArrayList<String>();
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[2]"));
		String strNHSNumber= element.getText().toString();
		inputData.add(strNHSNumber);
		
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[3]"));
		String forname= element.getText().toString();
		inputData.add(forname);
		
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[4]"));
		String surname= element.getText().toString();
		inputData.add(surname);
		
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[5]"));
		String dateOfBirth= element.getText().toString();
		inputData.add(dateOfBirth);
		
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[6]"));
		String strVaccineType= element.getText().toString();
		inputData.add(strVaccineType);
		
		element= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr["+rowNum+"]/td[7]"));
		String strVaccineDate= element.getText().toString();
		inputData.add(strVaccineDate);
		return inputData;
	}
	
	public HPV_Vaccination uploadHPVFile(String fileType) throws InterruptedException {
		String path= "";
		scrolltoElement(driver, fileUpload);
		wait.until(ExpectedConditions.elementToBeClickable(fileUpload)).clear();
		switch(fileType){
		case "Valid":
			path= HPVFilePath;
			break;
		case "NonCSV":
			path= System.getProperty("user.dir") + "\\Upload\\sample.pdf";
			break;
		case "InvalidCount":
			path= System.getProperty("user.dir") + "\\Upload\\CSTestFiles\\HPVVaccination_invalid count.csv";
			break;
		case "MandatoryField":
			path= System.getProperty("user.dir") + "\\Upload\\CSTestFiles\\MandatoryFieldMissing.csv";
			break;
		default:
			System.out.println(fileType+" filetype is not found");
			break;
		}
		CommonFunctions.Uploadfile(path, driver);
		if(!(fileType.equalsIgnoreCase("MandatoryField"))){
			wait.until(ExpectedConditions.elementToBeClickable(uploadHPVFile)).click();
			Thread.sleep(9000);
		}

		return new HPV_Vaccination(driver);
	}

	public boolean verifyUploadMessage() {
		boolean uploadMessage= false;
		String expMessage= "File Upload is in progress in CRM. Please check after some time.";
		String nonCSVMessage= "Only CSV document types are allowed";
		String ColumnCountMessage= "columns count not proper.";
		String mandatoryFieldsMessage= "Mandatory fields can't be blank or empty.";
		WebElement element= driver.findElement(By.cssSelector("div.alert.alert-danger.pcss-insert-failed-upload"));
		String actualMessage= element.getText().toString();
		if(expMessage.equalsIgnoreCase(actualMessage)){
			uploadMessage=true;
		}else if(nonCSVMessage.equalsIgnoreCase(actualMessage)){
			uploadMessage=true;
		}else if(ColumnCountMessage.equalsIgnoreCase(actualMessage)){
			uploadMessage=true;
		}else if(mandatoryFieldsMessage.equalsIgnoreCase(actualMessage)){
			uploadMessage=true;
		}else{
			uploadMessage= false;
		}
		return uploadMessage;
	}


}
