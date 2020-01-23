package pageobjects.OP;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class OPCreateGOS6PVN extends Support{
	WebDriver driver;
	WebDriverWait wait;


	@FindBy(id="PvnDetail_ContractorContactName")
	WebElement contractorContactNameTxt;

	@FindBy(id="txtDateOfVisit")
	WebElement dateOfVisittxt;

	@FindBy(id="txtTimeOfVisit")
	WebElement timeOfVisittxt;

	@FindBy(css="select[id='VenueMaintainance_PremisesTypeCode']")
	WebElement premisesType;
	
	@FindBy(css="select[id='RLTArea']")
	WebElement rltArea;

	@FindBy(id="VenueMaintainance_PremisesContactName")
	WebElement contactNameTxt;

	@FindBy(css="button[class*='btn btn-info']")
	WebElement enterAddressButton;


	@FindBy(css="div[id='AddressModal']")
	WebElement AddressModalWindow;

	@FindBy(css="input[id='AddressLine1']")
	WebElement addressLine1;

	@FindBy(css="input[id='AddressLine2']")
	WebElement addressLine2;

	@FindBy(css="input[id='AddressLine3']")
	WebElement addressLine3;

	@FindBy(css="input[id='City']")
	WebElement addressCity;

	@FindBy(css="input[id='County']")
	WebElement addressCounty;

	@FindBy(css="input[id='Postcode']")
	WebElement addressPostcode;

	@FindBy(name="BtnSaveAdd")
	WebElement saveAddressBtn;

	@FindBy(css="button[class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	
	//@FindBy(xpath="//div[@class='col-xs-12 col-sm-4']/strong[3]")
	//@FindBy(xpath="//div/strong[2]")
	/*FindBy(xpath="//div[@class='row table-borderPVN']/div[2]//strong")
	WebElement pvnReferenceNumberTxt;*/

	@FindBy(id="FirstName")
	WebElement firstNameTxt;

	@FindBy(id="Surname")
	WebElement surnameTxt;

	@FindBy(id="txtDateOfBirth")
	WebElement dOBTxt;

	@FindBy(id="txtNhsNumber")
	WebElement nhsNumberTxt;

	@FindBy(id="DateOfLastSightInWords")
	WebElement dateOfLastSightTxt;

	@FindBy(id="IsFirstTest")
	WebElement isFirstTestChkBox;

	@FindBy(name="BtnSavePatient")
	WebElement savePatientBtn;
	
	@FindBy(id="btnSubmit")
	WebElement submitButton;

	@FindBy(id="modalResult")
	WebElement modalResultPopUp;
	
	@FindBy(xpath="//div[@id='modalResult']//div[@class='modal-body']")
	WebElement modalResultPopupText;


	@FindBy(xpath = "//div[@id='modalResult']//button[@class='btn btn-default']")
	WebElement modalResultCloseBtn;
	
	@FindBy(xpath = "//div[@id='modalPeriodWeek']//button[@class='btn btn-default']")
	WebElement modalPeriodWeekCloseBtn;
	
	@FindBy(css="input[id='DateOfLastSight']")
	WebElement DateOfLastSightTxtBox;
	
	@FindBy(css="select[id='RetestCode']")
	WebElement retestCodeList;
	
	@FindBy(id="modalPeriodWeek")
	WebElement modalMessagePopup;
	
	@FindBy(xpath="//div[@id='modalPeriodWeek']//div[@class='modal-body']")
	WebElement modalMessagePopupText;

	@FindBy(id="IsOther")
	WebElement isOther;

	@FindBy(css="input[id='txtNhsNumber']")
	WebElement NHSNumberPortal;

	@FindBy(id="ReasonForSubmitPVN")
	WebElement ReasonSubmit;
	
	@FindBy(xpath="//div[@id='modalPeriod']//div[@class='modal-content']")
	WebElement modalPeriofWindow;
	
	@FindBy(xpath="//div[@id='modalPeriod']//div[@class='modal-body']")
	WebElement modalPeriodPopUpText;
	

	public OPCreateGOS6PVN(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		//WebElement pvnReferenceNumberTxt = driver.findElement(By.xpath("//div[@class='row table-borderPVN']/div[2]//strong"));
		PageFactory.initElements(this.driver, this);
	}


	public OPCreateGOS6PVN enterPVNDetails(int days,String environment) throws ParseException, InterruptedException
	{
		Thread.sleep(1000);
		String contractorcontactName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PVNDETAILS", "ContractorName", 1);
		Support.enterDataInTextField(contractorContactNameTxt, contractorcontactName, wait);
		
		String premiseTypeValue = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PVNDETAILS", "PremiseType", 1);
		CommonFunctions.selectOptionFromDropDown(premisesType, premiseTypeValue);

		Date dt = CommonFunctions.getDateAfterDays(days);
		String dateOfVisit = CommonFunctions.convertDateToString(dt, "dd-MM-yyyy");
		Support.enterDataInTextField(dateOfVisittxt, dateOfVisit, wait);
		Support.enterDataInTextField(timeOfVisittxt, "11:00", wait);


		enterAddressManually(1);

		//String rltAreaValue = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PVNDETAILS", "RLTArea", 1);
		String rltAreaValue= ConfigurationData.getRefDataDetails(environment, "OPGOS6RLTArea");
		CommonFunctions.selectOptionFromDropDown(rltArea, rltAreaValue);

		wait.until(ExpectedConditions.elementToBeClickable(contactNameTxt)).click();
		contactNameTxt.clear();
		String contractorName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PVNDETAILS", "ContactName", 1);
		contactNameTxt.sendKeys(contractorName);

		wait.until(ExpectedConditions.elementToBeClickable(saveAddressBtn)).click();
		System.out.println("Create GOS6 PVN details entered successfully.");
		return new OPCreateGOS6PVN(driver);
		


	}
	
	public OPCreateGOS6PVN enterDateOfVisit(int days) throws InterruptedException, ParseException
	{
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(dateOfVisittxt)).click();
		dateOfVisittxt.clear();
		Date dt = CommonFunctions.getDateAfterDays(days);
		String dateOfVisit = CommonFunctions.convertDateToString(dt, "dd-MM-yyyy");
		dateOfVisittxt.sendKeys(dateOfVisit);
		
		wait.until(ExpectedConditions.elementToBeClickable(saveAddressBtn)).click();
		System.out.println("Create GOS6 PVN details entered successfully.");
		return new OPCreateGOS6PVN(driver);
	}

	public String getPVNReferenceNumber() throws InterruptedException
	{
		String refNumber = null;
	//	scrolltoElement(driver, pvnReferenceNumberTxt);
		Thread.sleep(8000);
		System.out.println("Thread ended");
		WebElement pvnReferenceNumberTxt = driver.findElement(By.xpath("//div[@class='row table-borderPVN']/div[2]//strong"));
		System.out.println("Element located");
		wait.until(ExpectedConditions.elementToBeClickable(pvnReferenceNumberTxt));
		wait.until(ExpectedConditions.visibilityOf(pvnReferenceNumberTxt));
		refNumber = pvnReferenceNumberTxt.getText();
		return refNumber;

	}

	public OPCreateGOS6PVN enterPersonalDetails(int colNumber) throws ParseException, InterruptedException
	{

		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(firstNameTxt)).click();
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		firstNameTxt.clear();
		firstNameTxt.sendKeys(firstname);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxt)).click();
		String surnameTxtValue = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxt.clear();
		surnameTxt.sendKeys(surnameTxtValue);
		Thread.sleep(1000);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		//int pAge = Integer.parseInt(patientAge);
		String birthDate = CommonFunctions.getDOB(patientAge);
		wait.until(ExpectedConditions.elementToBeClickable(dOBTxt)).click();
		dOBTxt.clear();
		dOBTxt.sendKeys(birthDate);
		Thread.sleep(1000);
		//txtDateOfBirthTxtBox.sendKeys(Keys.TAB);
		//wait.until(ExpectedConditions.elementToBeClickable(DateOfLastSightTxtBox)).click();
		/*String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		DateOfLastSightTxtBox.clear();
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);*/
		scrolltoElement(driver,isFirstTestChkBox );
		isFirstTestChkBox.click();
		Thread.sleep(1000);

		wait.until(ExpectedConditions.elementToBeClickable(savePatientBtn)).click();
		Thread.sleep(2000);
		return new OPCreateGOS6PVN(driver);


	}
	
	public OPCreateGOS6PVN enterPersonalDetailsWithLastSightDate(int colNumber, int days) throws ParseException, InterruptedException
	{

		wait.until(ExpectedConditions.elementToBeClickable(firstNameTxt)).click();
		String firstname = "AutoFirstName" +CommonFunctions.getCurrentHourMin();
				//ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)
				
		firstNameTxt.clear();
		Thread.sleep(2000);
		firstNameTxt.sendKeys(firstname);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Firstname",firstname, colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxt)).click();
		String surnameTxtValue = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxt.clear();
		Thread.sleep(2000);
		surnameTxt.sendKeys(surnameTxtValue);

		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		//int pAge = Integer.parseInt(patientAge);
		String birthDate = CommonFunctions.getDOB(patientAge);
		System.out.println("The DOB on Portal" + birthDate);
		wait.until(ExpectedConditions.elementToBeClickable(dOBTxt)).click();
		dOBTxt.clear();
		dOBTxt.sendKeys(birthDate);
		//txtDateOfBirthTxtBox.sendKeys(Keys.TAB);
		//wait.until(ExpectedConditions.elementToBeClickable(DateOfLastSightTxtBox)).click();
	/*	String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		DateOfLastSightTxtBox.clear();
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		scrolltoElement(driver,isFirstTestChkBox );
		isFirstTestChkBox.click();
		Thread.sleep(2000);*/
		
		Date dt = CommonFunctions.getDateBeforeDays(days);
		String lastsightdate = CommonFunctions.convertDateToString(dt, "dd-MM-yyyy");
		System.out.println(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		String NHSNUmber = helpers.CommonFunctions.generateValidNHSNo();
		wait.until(ExpectedConditions.elementToBeClickable(NHSNumberPortal)).sendKeys(NHSNUmber);
		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("OPGOS6TESTDATA.xlsx", "PATIENTDETAILS",  "NHSNumber", NHSNUmber,colNumber);
		
		
		wait.until(ExpectedConditions.elementToBeClickable(retestCodeList));
		CommonFunctions.selectOptionFromDropDown(retestCodeList, "1");
		Thread.sleep(1000);
		
		wait.until(ExpectedConditions.elementToBeClickable(ReasonSubmit)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(ReasonSubmit)).sendKeys("Test");
		

		wait.until(ExpectedConditions.elementToBeClickable(savePatientBtn)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPCreateGOS6PVN(driver);


	}

	public void enterAddressManually(int colNumber) throws InterruptedException
	{
		try{
			wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
			wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
			String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline1", colNumber);
			String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline2", colNumber);
			String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline3", colNumber);
			String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCity", colNumber);
			String addresscounty = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCounty", colNumber);
			String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressPostCode", colNumber);
			if(AddressModalWindow.isDisplayed())
			{
				Support.enterDataInTextField(addressLine1,addressline1,wait);
				Support.enterDataInTextField(addressLine2,addressline2,wait);
				Support.enterDataInTextField(addressLine3,addressline3,wait);
				Support.enterDataInTextField(addressCity,addresscity,wait);
				Support.enterDataInTextField(addressCounty,addresscounty,wait);
				Support.enterDataInTextField(addressPostcode,addresspostcode,wait);
				wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
				Thread.sleep(2000);
				// helpers.CommonFunctions.ClickOnButton("Save", driver);

			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on GMC Address Pop up box" +e);
		}


	}
	
	public Boolean clickOnSubmit() throws InterruptedException
	{
		Thread.sleep(2000);
		Boolean flag = false;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(submitButton));
			scrolltoElement(driver, submitButton);
			submitButton.click();
			//Thread.sleep(2000);
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			//wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn));
			Thread.sleep(5000);
			if (modalResultPopUp.isDisplayed() || modalMessagePopup.isDisplayed()||modalPeriofWindow.isDisplayed() )
			{
				flag = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Exception has occurred on clicking Submit button");
		}
		
		return flag;
		//return new OPGOS6PatientDetails(driver);
		
	}
	
	public String getMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText)).getText();

		return msg;
	}
	
	public String getMsgTxtOnMsgPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalMessagePopupText)).getText();
		System.out.println("Message Captured: "+msg);
		return msg;
	}
	
	public OPHomePage clickCloseOnResultPopup()
	{
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn)).click();
		return new OPHomePage(driver);
	}
	
	public OPHomePage clickCloseOnMessageResultPopup()
	{
		wait.until(ExpectedConditions.elementToBeClickable(modalPeriodWeekCloseBtn)).click();
		return new OPHomePage(driver);
	}
	
	public void GOS6CreatePVNsnaps(String string) throws InterruptedException, IOException {
		WebElement pvnReferenceNumberTxt = driver.findElement(By.xpath("//div[@class='row table-borderPVN']/div[2]//strong"));
		scrolltoElement(driver,pvnReferenceNumberTxt);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string+"_2");
		
		
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
		Screenshot.TakeSnap(driver, string+"_3");
		
	}
	
	public OPCreateGOS6PVN TickOnOthersAndEnterReason()
	{	scrolltoElement(driver, isOther);
		wait.until(ExpectedConditions.elementToBeClickable(isOther)).click();
		WebElement reason= driver.findElement(By.id("OtherReason"));
		scrolltoElement(driver, reason);
		wait.until(ExpectedConditions.elementToBeClickable(reason)).click();
		reason.clear();
		reason.sendKeys("Date updated");
		return new OPCreateGOS6PVN(driver);
	}


	public OPCreateGOS6PVN addreasonforSubmit() {
		try{
				Support.enterDataInTextField(ReasonSubmit, "Test", wait);
		}
		catch(Exception e)
		{
			System.out.println("The Reason Text is not filled");
		}
		return new OPCreateGOS6PVN(driver);
	}
	
	public String getPeriodMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalPeriodPopUpText)).getText();
		System.out.println("Message Captured: "+msg);
		return msg;
	}
	
}
