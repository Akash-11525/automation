package pageobjects.OP;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class OPGOS5PatientDetails extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="titleCode")
	WebElement titleDropdown;
	
	@FindBy(css="input[id='FirstName']")
	WebElement firstNameTxtBox;
	
	@FindBy(css="input[id='Surname']")
	WebElement surnameTxtBox;
	
	@FindBy(css="input[id='PreviousSurname']")
	WebElement previousSurnameTxtBox;
	
	@FindBy(css="button[class*='btn btn-info']")
	WebElement enterAddressButton;
	
	@FindBy(css="input[id='txtDateOfBirth']")
	WebElement txtDateOfBirthTxtBox;
	
	@FindBy(css="input[id='DateOfLastSightInWords']")
	WebElement DateOfLastSightTxtBox;
	
	@FindBy(css="input[id='IsFirstTest']")
	WebElement IsFirstTestChkBox;
	
	@FindBy(css="input[id='IsTestDateUnknow']")
	WebElement IsTestDateUnknowChkBox;
	
	@FindBy(css="input[id='txtNhsNumber']")
	WebElement nhsNumberTxtBox;
	
	@FindBy(css="input[id='txtNiNumber']")
	WebElement nINumberTxtBox;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[id='btnSaveNext']")
	WebElement saveAndNextButton;
	
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
	
	@FindBy(css="input[id='txtSightTestDate']")
	WebElement sightTestDateTxt;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="PerformerName")
	WebElement performerName;
	
	@FindBy(css="ul[id='PerformerNameResult']")
	WebElement PerformerNameResult;
	
	@FindBy(id="ContractorName")
	WebElement contractorName;
	
	@FindBy(css="ul[id='ContractorNameResult']")
	WebElement ContractorNameResult;
	
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(id="DivPatientDetailsli")
	WebElement PatDetMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	public OPGOS5PatientDetails(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, saveButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(2000);
	}
	
	public void clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
	}
	
	public OPGOS5PatientEligibility PatientDetailsEntered(int colNumber, String environment) throws InterruptedException
	{
		enterPerformerDetails(environment);
		enterPatientDetails(colNumber);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPGOS5PatientEligibility(driver);
	}
	
	public OPGOS5PatientEligibility PatientDetailsEnteredwithUnknownLastSightDate(int colNumber, String environment) throws InterruptedException
	{
		enterPerformerDetails(environment);
		enterPatientDetailsLastSightDateUnknown(colNumber);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPGOS5PatientEligibility(driver);
	}
	
	public OPGOS5PatientEligibility PatientDetailsEnteredwithlastSightTest(int colNumber, int days, String environment) throws InterruptedException, ParseException
	{
		enterPerformerDetails(environment);
		enterPatientDetailswithLastSightTest(colNumber, days);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPGOS5PatientEligibility(driver);
	}
	
	public void enterPatientDetailswithLastSightTest(int colNumber, int days) throws InterruptedException, ParseException {
		
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxtBox)).click();
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxtBox.sendKeys(surname);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(previousSurnameTxtBox)).click();
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		previousSurnameTxtBox.sendKeys(previoussurname);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		txtDateOfBirthTxtBox.sendKeys(age);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		
		//txtDateOfBirthTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		/*String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPTESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);*/
		
		Date dt = CommonFunctions.getDateBeforeDays(days);
		String lastsightdate = CommonFunctions.convertDateToString(dt, "ddMMyyyy");
		System.out.println(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(lastsightdate);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		
	}

	public void enterPatientDetails(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxtBox)).click();
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxtBox.sendKeys(surname);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(previousSurnameTxtBox)).click();
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		previousSurnameTxtBox.sendKeys(previoussurname);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		txtDateOfBirthTxtBox.sendKeys(age);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);

		String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		scrolltoElement(driver, IsFirstTestChkBox);
		IsFirstTestChkBox.click();
		Thread.sleep(2000);	
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException
	{
        try{
            wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
            String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Addressline1", colNumber);
    		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Addressline2", colNumber);
    		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Addressline3", colNumber);
    		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "AddressCity", colNumber);
    		String addresscounty = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "AddressCounty", colNumber);
    		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "AddressPostCode", colNumber);
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
	
	public void enterPatientDetailswithoutLastSightTest(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxtBox)).click();
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxtBox.sendKeys(surname);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(previousSurnameTxtBox)).click();
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		previousSurnameTxtBox.sendKeys(previoussurname);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		txtDateOfBirthTxtBox.sendKeys(age);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		Thread.sleep(2000);
	}
	
	public void enterPatientDetailsLastSightDateUnknown(int colNumber) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Firstname", colNumber)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(surnameTxtBox)).click();
		String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Surname", colNumber);
		surnameTxtBox.sendKeys(surname);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(previousSurnameTxtBox)).click();
		String previoussurname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "PreviousSurname", colNumber);
		previousSurnameTxtBox.sendKeys(previoussurname);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		enterAddressManually(colNumber);
		String patientAge = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "Age", colNumber);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		txtDateOfBirthTxtBox.sendKeys(age);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		
		//txtDateOfBirthTxtBox.sendKeys(Keys.TAB);
		String lastsightdate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTDETAILS", "LastSightDate", colNumber);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		scrolltoElement(driver, IsTestDateUnknowChkBox);
		IsTestDateUnknowChkBox.click();
		Thread.sleep(2000);
	}
	
	public void enterPerformerDetails(String environment) throws InterruptedException
	{
		//Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOf(performerName));
		if (!(CommonFunctions.isAttribtuePresent(performerName, "readonly")))
		{
			String performer = ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
			/*String contractor = ConfigurationData.OPContractor;
			wait.until(ExpectedConditions.elementToBeClickable(contractorName));
			CommonFunctions.setText(contractorName, contractor);
			helpers.CommonFunctions.selectOptionFromList(ContractorNameResult, contractor );*/ //Commented by Akshay as contractor field will be disabled
			wait.until(ExpectedConditions.elementToBeClickable(performerName)).clear();
			CommonFunctions.setText(performerName, performer);//performer
			helpers.CommonFunctions.selectOptionFromList(PerformerNameResult, performer);
		}
	}
	

	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error text-danger']"));
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
	
	public void clearDateOfSightTestField() 
	{
		scrolltoElement(driver, sightTestDateTxt);
		wait.until(ExpectedConditions.elementToBeClickable(sightTestDateTxt)).click();
		sightTestDateTxt.clear();
	}
	
	public boolean verifyTickMarkOnPatientDetails() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatDetMenu);
		return tickMark;
	}
	
	public OPGOS5PatientEligibility PatientDetailsEntered(String colName, String environment) throws InterruptedException
	{
		OPHelpers.enterPerformerDetails(driver,performerName,PerformerNameResult,environment);
		enterPatientDetails(colName);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPGOS5PatientEligibility(driver);
	}
	
	public void enterPatientDetails(String colName) throws InterruptedException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Title", colName);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Firstname", colName)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Surname", colName);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "PreviousSurname", colName);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		OPHelpers.enterAddressManually(driver);
		String patientAge = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Age", colName);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);

		String lastsightdate = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "LastSightDate", colName);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		scrolltoElement(driver, IsFirstTestChkBox);
		IsFirstTestChkBox.click();
		Thread.sleep(2000);	
	}
	
	public OPGOS5PatientEligibility PatientDetailsEnteredwithlastSightTest(String colName, int days, String environment) throws InterruptedException, ParseException
	{
		enterPerformerDetails(environment);
		enterPatientDetailswithLastSightTest(colName, days);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPGOS5PatientEligibility(driver);
	}
	
	public void enterPatientDetailswithLastSightTest(String colName, int days) throws InterruptedException, ParseException {
		
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Title", colName);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		String firstname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Firstname", colName)+CommonFunctions.getCurrentHourMin();
		Support.enterDataInTextField(firstNameTxtBox, firstname, wait);
		
		String surname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Surname", colName);
		Support.enterDataInTextField(surnameTxtBox, surname, wait);
		
		String previoussurname = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "PreviousSurname", colName);
		Support.enterDataInTextField(previousSurnameTxtBox, previoussurname, wait);
		
		OPHelpers.enterAddressManually(driver);
		String patientAge = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTDETAILS", "Age", colName);
		System.out.println(patientAge);
		String age = CommonFunctions.getDOB(patientAge);
		Support.enterDataInTextField(txtDateOfBirthTxtBox, age, wait);
		Thread.sleep(2000);
		Date dt = CommonFunctions.getDateBeforeDays(days);
		String lastsightdate = CommonFunctions.convertDateToString(dt, "ddMMyyyy");
		System.out.println(lastsightdate);
		Support.enterDataInTextField(DateOfLastSightTxtBox, lastsightdate, wait);
		DateOfLastSightTxtBox.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		
	}

}