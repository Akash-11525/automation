package pageobjects.Adjustments;

import java.io.IOException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

import utilities.ReadCSV;
import utilities.WriteCSV;
import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import junit.framework.Assert;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class AdjustmentBatch extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public static String GPPAdjUploadFilePath= System.getProperty("user.dir")+ConfigurationData.GPPAdjUploadFilePath+"\\GPPAdjUpload.csv";
	public static String OPAdjUploadFilePath= System.getProperty("user.dir")+ConfigurationData.OPAdjUploadFilePath+"\\OPAdjUpload.csv";
	
	@FindBy(id="BatchRecord_BatchRefNo")
	WebElement batchNumber;
	
	@FindBy(id="BatchRecord_BatchName")
	WebElement batchName;
	
	@FindBy(id="BatchRecord_RequestingOrg")
	WebElement organisation;
	
	@FindBy(css="input#BatchRecord_Status")
	WebElement batchStatus;
	
	@FindBy(css="input.btn.btn-success")
	WebElement saveBtn;
	
	@FindBy(css="input.btn.btn-success")
	WebElement updateBtn;
	
	@FindBy(id="SearchCriteria_Contractor")
	WebElement searchContractor;
	
	@FindBy(id="SearchCriteria_Commissioner")
	WebElement searchCommissioner;
	
	@FindBy(id="SearchCriteria_AmountFrom")
	WebElement searchAmountFrom;
	
	@FindBy(id="SearchCriteria_AmountTo")
	WebElement searchAmountTo;
	
	@FindBy(id="SearchCriteria_PaymentType")
	WebElement searchPaymentType;
	
	@FindBy(id="PaymentFromDate")
	WebElement searchFromDate;
	
	@FindBy(id="PaymentToDate")
	WebElement searchToDate;
	
	@FindBy(xpath="//div[@id='divSearchBatch']//input[@class='btn btn-success']")
	WebElement searchBtn;
	
	@FindBy(css="input.btn.btn-link")
	WebElement uploadAdjLink;
	
	@FindBy(xpath="//div[@id='divAddOrSearchNewAdjustment']/div/div/div[2]/input[1]")
	WebElement enterBulkAdj;
	
	@FindBy(xpath="//div[@id='divAddOrSearchNewAdjustment']/div/div/div[2]/input[2]")
	WebElement bulkAdjReq;
	
	@FindBy(xpath="//div[@id='divAddOrSearchNewAdjustment']/div/div/div[2]/input[3]")
	WebElement CSVUploadReq;
	
	@FindBy(css="button.btn.btn-info.marginleft")
	WebElement addNewAdj;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement createBatchModalWindow;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/div/input")
	WebElement modalWindowOKButton;
	
	@FindBy(xpath="//form[@id='frmAdjustmentBatch']/div[5]//div[3]/div[1]/input[1]")
	WebElement cancelButton;
	
	@FindBy(xpath="//form[@id='frmAdjustmentBatch']/div[5]//div[3]/div[1]/input[2]")
	WebElement backButton;
	
	@FindBy(css="input#btnRevertToDraft")
	WebElement revertToDraftButton;
	
	@FindBy(css="input#btnSaveAsDraft")
	WebElement saveLaterButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/input")
	WebElement windowOKButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/input")
	WebElement browseWindowOKButton;
	
	@FindBy(css="input#btnSubmit")
	WebElement submitButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/button")
	WebElement modalCancelButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/input")
	WebElement modalConfirmButton;
	
	@FindBy(css="select#BulkPaymentGroup")
	WebElement bulkPaymentGroup;
	
	@FindBy(css="input#BulkPaymentType")
	WebElement bulkPaymentType;

	@FindBy(xpath="//div[@id='accordion']/div[1]/div/div/a")
	WebElement bulkContractorCodeText;
	
	@FindBy(css="input#OrganizationName")
	WebElement bulkContractorCodeTxtBox;
	
	@FindBy(css="input.btn.btn-default.btn-sm")
	WebElement bulkAddContractorButton;
	
	@FindBy(xpath="//div[@id='accordion']/div[2]/div/div/a")
	WebElement bulkPracticeText;
	
	@FindBy(id="ContractorPaymentDate")
	WebElement bulkIsContractorDate;
	
	@FindBy(css="select#ContractorPaymentMonth")
	WebElement bulkStartMonth;
	
	@FindBy(css="input#CustomPaymentDate")
	WebElement bulkPaymentDate;
	
	@FindBy(css="select#PaymentEndMonth")
	WebElement bulkPaymentEndMonth;
	
	@FindBy(css="select#PaymentEndYear")
	WebElement bulkPaymentEndYear;
	
	@FindBy(id="QuaterlyPayment")
	WebElement isQuarterlyPayment;
	
	@FindBy(id="ValuePerMonth")
	WebElement bulkValuePerMonth;
	
	@FindBy(id="TotalValue")
	WebElement bulkTotalValue;
	
	@FindBy(id="BulkDescription")
	WebElement bulkDescription;
	
	@FindBy(xpath="//div[@class='modal-footer']//button")
	WebElement bulkCancelButton;
	
	@FindBy(xpath="//div[@class='modal-footer']//input")
	WebElement bulkAddToBatchButton;
	
	public AdjustmentBatch(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public AdjustmentBatch createAdjustmentBatch(String fileName, String sheet, String batchNameKey, int colIndex, String environment,String module) throws InterruptedException {
		String strbatchName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName,sheet,batchNameKey,colIndex);
		String finalName= CommonFunctions.generateTS(strbatchName);
		String organisationCode= "";
		Support.enterDataInTextField(batchName, finalName, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(organisation)).click();
		if(module.equalsIgnoreCase("GPP")){
			organisationCode = ConfigurationData.getRefDataDetails(environment, "GPPAdjOrgCode");
		}else{
			organisationCode = ConfigurationData.getRefDataDetails(environment, "OPAdjOrgCode");
		}
		CommonFunctions.setText(organisation,organisationCode);
		selectFirstRecord("BatchRecord_RequestingOrg");

		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch saveBatch() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("Entered in save batch method");
		wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(batchName));
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch confirmRecordCreation() throws InterruptedException {
		System.out.println("Entered in confirm batch creation method");
		scrolltoElement(driver, modalWindowOKButton);
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(modalWindowOKButton)).click();
		System.out.println("Clicked on OK button under confirmation pop up.");
		return new AdjustmentBatch(driver);
	}

	public boolean verifySaveBatchMessage() {
		boolean saveMessage= false;
		String expMessage= "Adjustment Batch record has been created successfully.";
		WebElement message= driver.findElement(By.xpath("//div[@id='modal-container-body']/span"));
		saveMessage= getValueforMessage(message,expMessage);
		return saveMessage;
	}

	public boolean validateBatchNoState() throws InterruptedException {
		boolean batchNoDisabled= false;
		Thread.sleep(2000);
		scrolltoElement(driver, batchNumber);
		
		boolean value= batchNumber.getAttribute("readonly") != null;
		if(value==true)
		{
			batchNoDisabled=true;
		}else{
			batchNoDisabled=false;
		}
		return batchNoDisabled;
	}

	public boolean verifyButtonVisibility() throws InterruptedException {
		boolean isBtnVisible= false;
		Thread.sleep(2000);
		scrolltoElement(driver, updateBtn);
		List<WebElement> buttons= Arrays.asList(updateBtn,searchBtn,enterBulkAdj);
		isBtnVisible= getVisibleElementStatus(buttons);
		return isBtnVisible;
	}

	public boolean verifyWarningMessage() throws InterruptedException {
		boolean warningMessage= false;
		Thread.sleep(2000);
		String expectedMessage= "For new changes to apply to Adjustment Batch. Please click on Update.";
		WebElement warning= driver.findElement(By.xpath("//div[@id='divAddNewBatch']/div[1]/div[4]/div/div"));
		scrolltoElement(driver, warning);
		warningMessage= getValueforMessage(warning,expectedMessage);
		return warningMessage;
	}

	public void captureAdjBatchPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, batchNumber);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}

	public boolean validateBatchHeader() throws InterruptedException {
		boolean headerVerified= false;
		//int count=0;
		Thread.sleep(2000);
		List<WebElement> headerFields= Arrays.asList(batchName,batchNumber,batchStatus,organisation,saveBtn);
		headerVerified= getElementState(headerFields);
		String status= getInnerTextByJavaScript("BatchRecord_Status");
		if(status.equalsIgnoreCase("Draft")){
			headerVerified= true;
		}else{
			headerVerified= false;
		}
		return headerVerified;
	}
	
	private String getInnerTextByJavaScript(String Id) {
		String string= Support.getValueByJavaScript(driver,Id);
		return string;
	}

	public AdjustmentBatch clickOnAddAdjustment() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("Entered in add adj method");
		wait.until(ExpectedConditions.elementToBeClickable(addNewAdj)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnEnterBulkAdjustment() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("Entered in add adj method");
		wait.until(ExpectedConditions.elementToBeClickable(enterBulkAdj)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnAddToBatchButton() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("Entered in add to batch method");
		wait.until(ExpectedConditions.elementToBeClickable(bulkAddToBatchButton)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}

	public AdjustmentBatch enterGPPAdjustmentData(int monthIndex, int colIndex, String environment, String module) throws InterruptedException, ParseException {
		String strcontractorCode= "";
		//String strPaymentGroup= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "PaymentGroup", colIndex);
		String strPaymentType= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "PaymentType", colIndex);
		String strAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "Amount", colIndex);
		if(module.equalsIgnoreCase("GPP")){
			strcontractorCode= ConfigurationData.getRefDataDetails(environment, "GPPAdjContractorCode");
		}else{
			strcontractorCode= ConfigurationData.getRefDataDetails(environment, "OPAdjContractorCode");
		}
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, monthIndex);
		monthIndex= cal.get(Calendar.MONTH);
		String strPaymentMonth= getMonth(monthIndex);
		
		WebElement contractorCode= driver.findElement(By.cssSelector("input#ContractorCode"));
		wait.until(ExpectedConditions.elementToBeClickable(contractorCode));
		scrolltoElement(driver, contractorCode);
		contractorCode.clear();
		CommonFunctions.setText(contractorCode,strcontractorCode);
		selectFirstRecord("ContractorCode");
		
		//Akshay S: Payment group is not in use now
/*		WebElement paymentGroup= driver.findElement(By.cssSelector("select#PaymentGroup"));
		wait.until(ExpectedConditions.elementToBeClickable(paymentGroup));
		scrolltoElement(driver, paymentGroup);
		CommonFunctions.selectOptionFromDropDown(paymentGroup, strPaymentGroup);*/
		
		WebElement paymentType= driver.findElement(By.cssSelector("input#PaymentType"));
		wait.until(ExpectedConditions.elementToBeClickable(paymentType));
		scrolltoElement(driver, paymentType);
		paymentType.clear();
		CommonFunctions.setText(paymentType,strPaymentType);
		selectFirstRecord("PaymentType");
		
		WebElement amount= driver.findElement(By.cssSelector("input#Amount"));
		wait.until(ExpectedConditions.elementToBeClickable(amount));
		scrolltoElement(driver, amount);
		amount.clear();
		amount.sendKeys(strAmount);
		
		WebElement paymentMonth= driver.findElement(By.cssSelector("select#PaymentMonth"));
		wait.until(ExpectedConditions.elementToBeClickable(paymentMonth));
		scrolltoElement(driver, paymentMonth);
		CommonFunctions.selectOptionFromDropDown(paymentMonth, strPaymentMonth);
		Support.PageLoadExternalwait(driver);
		
		String strPaymentDate= Support.getValueByJavaScript(driver, "PaymentDate");
		strPaymentDate= CommonFunctions.Tomorrowdate(strPaymentDate, 0, "dd/M/yyyy");
		monthIndex= (Integer.parseInt(strPaymentDate.split("/")[1]))-1;
		strPaymentMonth= getMonth(monthIndex);
		wait.until(ExpectedConditions.elementToBeClickable(paymentMonth));
		scrolltoElement(driver, paymentMonth);
		CommonFunctions.selectOptionFromDropDown(paymentMonth, strPaymentMonth);
		Support.PageLoadExternalwait(driver);
		clickOnAddButton();
		
		return new AdjustmentBatch(driver);
	}

	private void selectFirstRecord(String ID) throws InterruptedException {
		WebElement firstRecord= driver.findElement(By.xpath("//li[starts-with(@id,'"+ID+"')]/a"));
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		firstRecord.click();
	}

	private void clickOnAddButton() {
		WebElement addButton= driver.findElement(By.xpath("//div[@id='tblNewBatch']/div/div[3]//button"));
		wait.until(ExpectedConditions.elementToBeClickable(addButton));
		addButton.click();
	}

	private String getMonth(int monthIndex) {
		String month="";
		List<String> months= Arrays.asList("January","February","March","April","May","June","July","August","September","October","November","December");
		for(int i=0;i<months.size();i++){
			if(i==monthIndex){
				month= months.get(i).toString();
				break;
			}
		}
		return month;
	}

	public AdjustmentBatch clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}

	public AdjustmentBatch confirmAction() throws InterruptedException {
		System.out.println("Entered in confirm method");
		Thread.sleep(3000);
		scrolltoElement(driver,modalConfirmButton );
		wait.until(ExpectedConditions.elementToBeClickable(modalConfirmButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on Confirm button under confirmation pop up.");
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch cancelAction() throws InterruptedException {
		System.out.println("Entered in cancel method");
		Thread.sleep(3000);
		scrolltoElement(driver,modalCancelButton );
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on Cancel button under confirmation pop up.");
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnRevertToDraftButton() throws InterruptedException {
		scrolltoElement(driver, revertToDraftButton);
		wait.until(ExpectedConditions.elementToBeClickable(revertToDraftButton)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}

	public AdjustmentBatch clickOnSaveForLater() {
		scrolltoElement(driver, saveLaterButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveLaterButton)).click();;
		return new AdjustmentBatch(driver);
	}

	public AdjustmentBatch clickOnOKButton() throws InterruptedException {
		System.out.println("Entered in confirm draft method");
		Thread.sleep(3000);
		scrolltoElement(driver,windowOKButton);
		wait.until(ExpectedConditions.elementToBeClickable(windowOKButton)).click();
		System.out.println("Clicked on OK button under confirmation pop up.");
		Thread.sleep(8000);
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnBrowseOKButton() throws InterruptedException {
		System.out.println("Entered in csv request OK method");
		Thread.sleep(3000);
		scrolltoElement(driver,browseWindowOKButton);
		wait.until(ExpectedConditions.elementToBeClickable(browseWindowOKButton)).click();
		System.out.println("Clicked on OK button under browse confirmation pop up.");
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnCSVRequestButton() throws InterruptedException {
		System.out.println("Entered in confirm draft method");
		Thread.sleep(3000);
		scrolltoElement(driver,CSVUploadReq );
		wait.until(ExpectedConditions.elementToBeClickable(CSVUploadReq)).click();
		System.out.println("Clicked on CSV request pop up.");
		Thread.sleep(7000);
		return new AdjustmentBatch(driver);
	}

	public boolean verifyActionButtons() {
		boolean actionButtons= false;
		List<WebElement> buttons= Arrays.asList(revertToDraftButton,saveLaterButton,submitButton);
		actionButtons= getVisibleElementStatus(buttons);
		return actionButtons ;
	}

	private boolean getVisibleElementStatus(List<WebElement> elements) {
		boolean visible= false;
		int count=0;
		for(int i=0;i<elements.size();i++){
			WebElement element= elements.get(i);
			scrolltoElement(driver, element);
			if(element.isDisplayed()){
				count= count+1;
			}
		}
		if(elements.size()==count){
			visible= true;
		}else{
			visible= false;
		}
		return visible;
	}
	
	private boolean getElementState(List<WebElement> elements) {
		boolean enabled= false;
		int count=0;
		for(int i=0;i<elements.size();i++){
			WebElement element= elements.get(i);
			scrolltoElement(driver, element);
			if(element.isEnabled()){
				count= count+1;
			}
		}
		if(elements.size()==count){
			enabled= true;
		}else{
			enabled= false;
		}
		return enabled;
	}

	public boolean verifyEmptyGrid() {
		boolean isEmptyGrid= false;
		int gridCount= driver.findElements(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr")).size();
		if(gridCount==3){
			isEmptyGrid= true;
		}else{
			isEmptyGrid= false;
		}
		return isEmptyGrid;
	}

	public boolean verifySubmitMessage() {
		boolean message= false;
		String expectedMessage= "Are you sure you want to submit ?";
		WebElement submitMessage= driver.findElement(By.xpath("//div[@id='modal-container-body']"));
		message=getValueforMessage(submitMessage,expectedMessage);
		return message;
	}
	
	public boolean verifyRevertMessage() {
		boolean message= false;
		String expectedMessage= "Are you sure you want to revert to draft ?";
		WebElement revertMessage= driver.findElement(By.xpath("//div[@id='modal-container-body']"));
		message=getValueforMessage(revertMessage,expectedMessage);
		return message;
	}

	private boolean getValueforMessage(WebElement elmMessage, String expectedMessage) {
		boolean message= false;
		String actualMessage= elmMessage.getText().toString();
		if(actualMessage.equalsIgnoreCase(expectedMessage)){
			message= true;
		}else{
			message= false;
		}
		return message;
	}

	public boolean verifyBatchStatus(String expStatus) throws InterruptedException {
		boolean isStatusUpdated= false;
		Thread.sleep(3000);
		String status= getInnerTextByJavaScript("BatchRecord_Status");
		if(status.equalsIgnoreCase(expStatus)){
			isStatusUpdated=true;
		}else{
			isStatusUpdated=false;
		}
		return isStatusUpdated;
	}

	public boolean verifyGridActionButtons() throws InterruptedException {
		boolean gridActionButtons= false;
		Thread.sleep(3000);
		WebElement editButton= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr[1]/td[9]/button"));
		WebElement deleteButton= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr[1]/td[10]/button"));
		List<WebElement> buttons= Arrays.asList(editButton,deleteButton);
		gridActionButtons= getVisibleElementStatus(buttons);
		return gridActionButtons;
	}

	public boolean generateGPPAdjCSVFile(String feature, List<Integer>rows, char character, int colIndex) throws IOException, ParseException {
		boolean CSVCreated= false;
		String[][] finalAdjData;
		try{
			finalAdjData= getCSVFileData(feature,rows,colIndex);
			String destFilePath= WriteCSV.getDestPathForModule(feature);
			char separator= character;
			String fileName= feature+"Upload";
			WriteCSV.writeFileData(finalAdjData,destFilePath,separator,fileName);
			CSVCreated= true;
		}catch(Exception e){
			CSVCreated= false;
			System.out.println("CSV file creation has been failed");
		}

		return CSVCreated;
	}

	private String[][] getCSVFileData(String feature, List<Integer>rows,int colIndex) throws IOException, ParseException {
		String[][]sourceData = null;
		String[][] loopData;
		String [][]finalAdjData = null;
		
		String sourcePath= ReadCSV.getSourcePathForModule(feature);
		int lineCount= ReadCSV.calculateLines(sourcePath);
		
		List<String> adjValues= new ArrayList<String>();
		List<Integer> colIndexValues= Arrays.asList(3,4);
		List<Integer> days= Arrays.asList(0,32,65);
		
		String contractorCode= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "SampleCSVData", "ContractorCode", colIndex);
		String paymentType= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "SampleCSVData", "PaymentType", colIndex);
		String amount= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "SampleCSVData", "Amount", colIndex);
		String description= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "SampleCSVData", "Description", colIndex);
		adjValues.add(contractorCode);
		adjValues.add(paymentType);
		adjValues.add(amount);
		for(int i=0;i<rows.size();i++){
			int rowIndex= rows.get(i);
			Date date= CommonFunctions.getDateAfterDays(days.get(i));
			String strMonth= CommonFunctions.convertDateToString(date,"MMMM");
			System.out.println("Month is: "+strMonth);
			adjValues.add(strMonth);
			String strDate= CommonFunctions.convertDateToString(date,"dd.MM.yyyy");
			System.out.println("date is: "+strDate);
			adjValues.add(strDate);
			adjValues.add(description);
			if(i==0){
				sourceData= ReadCSV.readCSVData(sourcePath,lineCount);
			}
			loopData= WriteCSV.appendRowForIndex(sourceData, adjValues, rowIndex, colIndexValues);
			//ReadCSV.generateGPPAdjustmentFile(adjValues,rowIndex,colIndexValues,sourcePath,"GPPAdj",',');
			sourceData=loopData;
			adjValues.remove(strMonth);
			adjValues.remove(strDate);
			adjValues.remove(description);
		}
		finalAdjData=sourceData;
		return finalAdjData;
	}

	public AdjustmentBatch uploadFile(String fileType) throws InterruptedException {
		String path= "";
		Thread.sleep(2000);
		scrolltoElement(driver, uploadAdjLink);
		wait.until(ExpectedConditions.elementToBeClickable(uploadAdjLink)).click();
		WebElement filePath= driver.findElement(By.cssSelector("input#upload1"));
		WebElement browseButton= driver.findElement(By.cssSelector("button#browseBtn"));
		Actions focus= new Actions(driver);
		focus.moveToElement(filePath).build().perform();
		filePath.clear();
		switch(fileType){
		case "GPPValid":
			path= GPPAdjUploadFilePath;
			break;
		case "GPPInvalidData":
			path= System.getProperty("user.dir")+"\\Upload\\AdjTestFiles\\GPP\\InvalidPaymentTypeContCode.csv";
			break;
		case "OPValid":
			path= OPAdjUploadFilePath;
			break;
		default:
			System.out.println(fileType+" filetype is not found");
			break;
		}
		CommonFunctions.Uploadfile(path, driver);
		wait.until(ExpectedConditions.elementToBeClickable(browseButton)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}

	public boolean verifyCompletedDataOnCSVRequest() throws InterruptedException {
		boolean isCompleted= false;
		//clickOnCSVRequestButton();
		Thread.sleep(6000);
		String expectedStatus="Completed";
		WebElement status= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[1]/td[2]"));
		String actualStatus= status.getText().toString();
		while(!(expectedStatus.equalsIgnoreCase(actualStatus))){
			clickOnBrowseOKButton();
			CSVUploadReq.click();
			Thread.sleep(2000);
			if(expectedStatus.equalsIgnoreCase(actualStatus)){
				break;
			}
			clickOnBrowseOKButton();
		}
		WebElement total= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[1]/td[5]"));
		int totalCount= Integer.parseInt(total.getText());
		WebElement success= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[1]/td[6]"));
		int successCount= Integer.parseInt(success.getText());
		if(expectedStatus.equalsIgnoreCase(actualStatus)){
			if(totalCount==successCount){
				isCompleted=true;
			}
		}else{
			isCompleted= false;
		}
		return isCompleted;
	}

	public boolean verifyReasonOnFailure(String module, String fileType) throws InterruptedException {
		//"GPP","GPPInvalidData"
		boolean isVerified= false;
		String paymentTypeMsg="";
		String contractorMsg="";
		switch(module){
		case "GPP":
		{
			switch(fileType){
			case "GPPInvalidData":
				paymentTypeMsg= "Payment Type does not exist.";
				contractorMsg= "Contractor Code does not exist.";
				Thread.sleep(4000);
				WebElement viewReportLink= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[1]/td[8]/input"));
				viewReportLink.click();
				Thread.sleep(2000);
				WebElement paymentType= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[1]/td[7]"));
				String actualPaymentTypeMsg= paymentType.getText().toString();
				
				WebElement contractorCode= driver.findElement(By.xpath("//div[@id='modal-container-body']//tbody/tr[2]/td[7]"));
				String actualContCodeMsg= contractorCode.getText().toString();
				//Thread.sleep(3000);
				if(actualPaymentTypeMsg.contains(paymentTypeMsg)){
					Thread.sleep(3000);
					System.out.println("payment msg is passed");
					if(actualContCodeMsg.contains(contractorMsg)){
						Thread.sleep(3000);
						System.out.println("contractor msg is passed");
						isVerified= true;
					}
				}else{
					isVerified= false;
				}
				break;
			default:
				Assert.fail(fileType+" is not found");
				break;
			}
		}
			break;
		default:
			Assert.fail(module+" is not found");
			break;
		}


		return isVerified;
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

	public AdjustmentBatch enterBulkAdjustmentData(String fileName, String sheet, int colIndex, boolean isContractorDate,boolean isQuarterlyPaymt,String environment,String module) throws InterruptedException, ParseException {
		//String strPaymentGroup= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "PaymentGroup", colIndex);
		String strPaymentType= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "PaymentType", colIndex);
		String strContractorCode= "";
		String strValuePerMonth= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "ValuePerMonth", colIndex);
		String strDescription= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "Description", colIndex);
		String strStartMonth= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "StartMonth", colIndex);
		String strEndMonth= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, sheet, "EndMonth", colIndex);
		if(module.equalsIgnoreCase("GPP")){
			strContractorCode= ConfigurationData.getRefDataDetails(environment, "GPPAdjContractorCode");
		}else{
			strContractorCode= ConfigurationData.getRefDataDetails(environment, "OPAdjContractorCode");
		}
		Actions focus= new Actions(driver);
		//CommonFunctions.selectOptionFromDropDown(bulkPaymentGroup, strPaymentGroup); This has been commented by Akshay as this web element does not exist
		CommonFunctions.setText(bulkPaymentType,strPaymentType);
		selectFirstRecord("BulkPaymentType");
		
		wait.until(ExpectedConditions.elementToBeClickable(bulkContractorCodeText)).click();				
		focus.moveToElement(bulkContractorCodeTxtBox).build().perform();
		focus.click();
		Thread.sleep(3000);
		CommonFunctions.setText(bulkContractorCodeTxtBox,strContractorCode);
		selectFirstRecord("OrganizationName");
		wait.until(ExpectedConditions.elementToBeClickable(bulkAddContractorButton)).click();
		Thread.sleep(3000);
		
		Calendar cal = Calendar.getInstance();
		int startMonth= Integer.parseInt(strStartMonth);
		int endMonth= Integer.parseInt(strEndMonth);
		cal.add(Calendar.MONTH, startMonth);
		startMonth= cal.get(Calendar.MONTH);
		if(isContractorDate){
			scrolltoElement(driver, bulkIsContractorDate);
			wait.until(ExpectedConditions.elementToBeClickable(bulkIsContractorDate)).click();
			String month= getMonth(startMonth);
			CommonFunctions.selectOptionFromDropDown(bulkStartMonth, month);
		}else{
			Date date= CommonFunctions.getDateAfterDays(7);
			String strDate= CommonFunctions.convertDateToString(date,"dd/MM/yyyy");
			scrolltoElement(driver, bulkPaymentDate);
			wait.until(ExpectedConditions.elementToBeClickable(bulkPaymentDate)).click();
			bulkPaymentDate.sendKeys(strDate);
		}
		wait.until(ExpectedConditions.elementToBeClickable(bulkPaymentEndMonth)).click();
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, endMonth);
		endMonth= cal.get(Calendar.MONTH);
		String month= getMonth(endMonth);
		scrolltoElement(driver, bulkPaymentEndMonth);
		CommonFunctions.selectOptionFromDropDown(bulkPaymentEndMonth, month);
		if(startMonth>endMonth){
			cal = Calendar.getInstance();
			int currentYear = cal.get(Calendar.YEAR);
			currentYear= currentYear+1;
			String strCurrentyear= Integer.toString(currentYear);
			CommonFunctions.selectOptionFromDropDown(bulkPaymentEndYear,strCurrentyear);
		}else{
			cal = Calendar.getInstance();
			int currentYear = cal.get(Calendar.YEAR);
			String strCurrentyear= Integer.toString(currentYear);
			CommonFunctions.selectOptionFromDropDown(bulkPaymentEndYear,strCurrentyear);
		}
		scrolltoElement(driver, bulkValuePerMonth);
		wait.until(ExpectedConditions.elementToBeClickable(bulkValuePerMonth)).click();
		bulkValuePerMonth.sendKeys(strValuePerMonth);
		
		if(isQuarterlyPaymt){
			wait.until(ExpectedConditions.elementToBeClickable(isQuarterlyPayment));
			isQuarterlyPayment.click();
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(bulkDescription)).click();
		bulkDescription.sendKeys(strDescription);
		
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnBulkAdjReqButton() throws InterruptedException {
		Thread.sleep(3000);
		System.out.println("Entered in add to batch method");
		wait.until(ExpectedConditions.elementToBeClickable(bulkAdjReq)).click();
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch clickOnBulkAdjReqOKButton() throws InterruptedException {
		System.out.println("Entered in csv request OK method");
		Thread.sleep(3000);
		scrolltoElement(driver,browseWindowOKButton);
		wait.until(ExpectedConditions.elementToBeClickable(browseWindowOKButton)).click();
		System.out.println("Clicked on OK button under browse confirmation pop up.");
		Thread.sleep(3000);
		return new AdjustmentBatch(driver);
	}

	public String getAdjustmentBatchNo() {
		String batchNo="";
		scrolltoElement(driver, batchNumber);
		wait.until(ExpectedConditions.elementToBeClickable(batchNumber));
		batchNo= getInnerTextByJavaScript("BatchRecord_BatchRefNo");
		return batchNo;
	}

	public boolean verifyAdjustmentTableData(List<String>formData) {
		boolean isBatchDataVerified= false;
		
		List<String> verifiedData= new ArrayList<String>();
		int expSize= driver.findElements(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr[1]/td")).size();
		
		for(int i=0;i<formData.size();i++){
			String expectedData= formData.get(i);
			for(int j=1; j<=expSize;j++){
				WebElement element= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr[1]/td["+j+"]"));
				String actualData= element.getText();
					if(expectedData.equalsIgnoreCase(actualData)){
					System.out.println("Grid is verified for value: "+actualData);
					verifiedData.add(actualData);
					//break;
				}
		}
	}
		if(formData.size()==verifiedData.size()){
			isBatchDataVerified=true;
		}else{
			isBatchDataVerified=false;
		}
		return isBatchDataVerified;
	}

	public List<String> getEnteredData(String environment, int colIndex) {
		List<String> expectedData= new ArrayList<String>();
		String strContractorCode= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tr[1]/td[1]/div")).getText().toString();
		String strContractorName= ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
		String strCommissioner= ConfigurationData.getRefDataDetails(environment, "PLContractor");
		//String strPaymentGroup= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "PaymentGroup", colIndex);
		String strPaymentType= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "PaymentType", colIndex);
		String strAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("AdjustmentTestData.xlsx", "GPPTestData", "Amount", colIndex);
		float amount= Float.parseFloat(strAmount);
		String.valueOf(amount);
		String newAmount= String.format("%,.2f", amount);
		String strPaymentMonth= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tr[1]/td[6]")).getText().toString();
		String date= driver.findElement(By.xpath("//table[@id='tblAdjustmentBatch']//tr[1]/td[7]")).getText().toString();
		expectedData= Arrays.asList(strContractorCode,strContractorName,strCommissioner,strPaymentType,newAmount,strPaymentMonth,date);
		return expectedData;
	}
	
	public AdjustmentHomePage clickOnAdjBreadCrumb(){
		WebElement element= driver.findElement(By.xpath("//form[@id='frmAdjustmentBatch']/div/div/div/span[2]/a"));
		element.click();
		return new AdjustmentHomePage(driver);
	}
	
	public String getAdjustmentBatchName() {
		String strBatchName="";
		scrolltoElement(driver, batchName);
		wait.until(ExpectedConditions.elementToBeClickable(batchName));
		strBatchName= getInnerTextByJavaScript("BatchRecord_BatchName");
		return strBatchName;
	}
	
	public String getAdjustmentOrgName() throws InterruptedException {
		String strOrgName="";
		Thread.sleep(3000);
		scrolltoElement(driver, organisation);
		wait.until(ExpectedConditions.elementToBeClickable(organisation));
		strOrgName= getInnerTextByJavaScript("BatchRecord_RequestingOrg");
		return strOrgName;
	}

	public AdjustmentBatch selectRLTForBulkAdj(String adjOrgName) throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(bulkPracticeText)).click();
		List<WebElement> RLTList= driver.findElements(By.xpath("//div[@id='jstreeDiv']/ul/li"));
		for(int i=1;i<=RLTList.size();i++){
			WebElement RLT= driver.findElement(By.xpath("//div[@id='jstreeDiv']/ul/li["+i+"]//a"));
			//Thread.sleep(2000);
			String RLTName= RLT.getText().toString();
			if(adjOrgName.equalsIgnoreCase(RLTName)){
				Thread.sleep(2000);
				WebElement element= driver.findElement(By.xpath("//div[@id='jstreeDiv']/ul/li["+i+"]//a/i"));
				element.click();
				break;
			}
		}
		Thread.sleep(3000);
		
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentBatch createAdjustmentBatch_NHSE(String fileName, String sheet, String batchNameKey, int colIndex) throws InterruptedException {
		String strbatchName= ExcelUtilities.getKeyValueFromExcelWithPosition(fileName,sheet,batchNameKey,colIndex);
		String finalName= CommonFunctions.generateTS(strbatchName);
		wait.until(ExpectedConditions.elementToBeClickable(batchName)).click();
		batchName.sendKeys(finalName);

		return new AdjustmentBatch(driver);
	}
	
	public int getRecordCount() throws InterruptedException {
		Thread.sleep(2000);
		int recordCount=0;
		Support.PageLoadExternalwait(driver);
		List<WebElement> records= driver.findElements(By.xpath("//table[@id='tblAdjustmentBatch']//tbody/tr"));
		wait.until(ExpectedConditions.visibilityOfAllElements(records));
		recordCount= records.size();
		return recordCount;
	}


}
