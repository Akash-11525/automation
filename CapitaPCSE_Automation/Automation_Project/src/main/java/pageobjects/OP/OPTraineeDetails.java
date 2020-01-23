package pageobjects.OP;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPTraineeDetails extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="titleCode")
	WebElement titleDropdown;
	
	@FindBy(css="input[id='FirstName']")
	WebElement firstNameTxtBox;
	
	@FindBy(css="ul[id='NameResult']")
	WebElement nameResult;
	
	@FindBy(css="input[id='Surname']")
	WebElement surnameTxtBox;
	
	@FindBy(css="input[id='txtStartDate']")
	WebElement trainngStartDate;
	
	@FindBy(css="input[id='txtEndDate']")
	WebElement trainngEndDate;
	
	@FindBy(css="input[id='PreviousSurname']")
	WebElement previousSurnameTxtBox;
	
	@FindBy(css="button[class='btn btn-info']")
	WebElement enterAddressButton;
	
	@FindBy(css="input[id='txtDateOfBirth']")
	WebElement txtDateOfBirthTxtBox;
	
	@FindBy(css="input[id='DateOfLastSight']")
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
	
	@FindBy(id="PatientEligibility_IsAboveSixty")
	WebElement patelgAbvSixtyChkBox;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	WebElement claimNumberHeader;
	
	@FindBy(css="input[id='IsSixMonths']")
	WebElement IsSixMonthsChkBox;
	
	@FindBy(css="input[id='IsOneYear']")
	WebElement IsOneYearChkBox;

	@FindBy(id="referenceNumber")
	WebElement refNumber;

	public OPTraineeDetails(WebDriver driver){

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
	
	public OPSupervisorsDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPSupervisorsDeclaration(driver);
	}
	
		
	public void enterTraineeDetails(int colNumber, int day, String traineeName) throws InterruptedException, ParseException
	{
		scrolltoElement(driver, titleDropdown);
		String title = ExcelUtilities.getKeyValueFromExcelWithPosition("OPPRTTESTDATA.xlsx", "TRAINEEDETAILS", "Title", colNumber);
		CommonFunctions.selectOptionFromDropDown(titleDropdown, title);
		wait.until(ExpectedConditions.elementToBeClickable(firstNameTxtBox)).click();
		//String firstname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPPRTTESTDATA.xlsx", "TRAINEEDETAILS", "Firstname", colNumber);
		//String surname = ExcelUtilities.getKeyValueFromExcelWithPosition("OPPRTTESTDATA.xlsx", "TRAINEEDETAILS", "Surname", colNumber);
		String ClaimType = ExcelUtilities.getKeyValueFromExcelWithPosition("OPPRTTESTDATA.xlsx", "TRAINEEDETAILS", "ClaimType", colNumber);
		CommonFunctions.setText(firstNameTxtBox, traineeName);
		helpers.CommonFunctions.selectOptometristFromList(nameResult,traineeName);
	
		wait.until(ExpectedConditions.elementToBeClickable(trainngStartDate)).click();
		String startDate = ExcelUtilities.getKeyValueFromExcelWithPosition("OPPRTTESTDATA.xlsx", "TRAINEEDETAILS", "StartDate", colNumber);
		//String startDate = CommonFunctions.getTodayDate();
		trainngStartDate.clear();
		trainngStartDate.sendKeys(startDate);
		
		wait.until(ExpectedConditions.elementToBeClickable(trainngEndDate)).click();

		Date date= CommonFunctions.getDateBeforeDays(1);
		String endDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		trainngEndDate.clear();
		trainngEndDate.sendKeys(endDate);
		System.out.println("ClaimType: "+ClaimType);
		if(ClaimType != null && !ClaimType.isEmpty())
		{
			WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+ClaimType+"')]"));
			Support support= new Support();
			support.scrolltoElement(driver, ele);
			ele.click();
			Thread.sleep(1000);
		}
	}

	public OPSupervisorsDeclaration TraineeDetailsEntered(int colNumber, int day, String environment,String traineeName) throws InterruptedException, ParseException
	{
		//enterPerformerDetails();
		OPHelpers.enterPerformerDetails(driver, performerName,PerformerNameResult, environment);
		enterTraineeDetails(colNumber, day,traineeName);
		clickOnSaveandNextButton();
		//Thread.sleep(3000);
		return new OPSupervisorsDeclaration(driver);
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
	
	
	
	
	public void PRTTraineeDetailsErrorsSnap(String note) throws InterruptedException, IOException
	{
		
		scrolltoElement(driver, contractorName);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
	
	public String getClaimNumber(String key) throws InterruptedException
	{
		Thread.sleep(1000);
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}
}
