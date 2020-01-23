package pageobjects.OP;

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
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class OPGOS5PatientEligibility  extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="PersonPatient")
	WebElement patientCheckbox;
	
	@FindBy(id="PersonPartner")
	WebElement patientPartnerCheckbox;
	
	@FindBy(id="HCNumber")
	WebElement certificateNumber;
	
	@FindBy(id="PrivateTestAmount")
	WebElement privateTestAmount;
	
	@FindBy(id="IsPatientAgree")
	WebElement isPatientAgree;
	
	@FindBy(id="IsPatientPresent")
	WebElement PatientPresentStatus;
	
	@FindBy(id="reason")
	WebElement reason;
	
	@FindBy(css="select#ethinicity")
	WebElement ethinicity;
	
	@FindBy(css="input[name='btnBack']")
	WebElement previousButton;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
	@FindBy(id="btnSaveNext")
	WebElement saveAndNextButton;
	
	@FindBy(css="li[id='DivPatientEligibilityli']")
	WebElement PatElgMenu;
	
	@FindBy(id="DivPatientDeclarationli")
	WebElement PatDecMenu;
	
	@FindBy(css="select[id='unAttendCode']")
	WebElement unAttendCodeSelect;
	
	public OPGOS5PatientEligibility(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public OPGOS5PatientDeclaration PatientEligibilityDetailsEntered(int colNumber) throws IOException, InterruptedException 
	{
		selectProvidedOptions(colNumber);
		enterValuesInTextField(colNumber);
		selectUnattendedCode();
		clickOnSaveandNextButton();
		
		Thread.sleep(2000);
		
		return new OPGOS5PatientDeclaration(driver);
	}

	public void enterValuesInTextField(int colNumber) 
	{
		String strCertificateNumber=  ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTSELE", "CertificateNumber", colNumber);
		String strPrivateTestFee = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTSELE", "PrivateSightTestFee", colNumber);
		//String strEthinicity= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTSELE", "Ethinicity", colNumber);
		String strReason= ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS5TESTDATA.xlsx", "PATIENTSELE", "Reason", colNumber);
		
		Support.enterDataInTextField(certificateNumber, strCertificateNumber, wait);
		Support.enterDataInTextField(privateTestAmount, strPrivateTestFee, wait);
		
		/*wait.until(ExpectedConditions.elementToBeClickable(ethinicity));*/
		/*helpers.CommonFunctions.selectOptionFromDropDown(ethinicity, strEthinicity);*/

		Support.enterDataInTextField(reason, strReason, wait);
		
	}

	public void selectProvidedOptions(int colNumber) throws IOException, InterruptedException 
	{
		
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS5TESTDATA.xlsx", "PATIENTELEOPTIONS", colNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		scrolltoElement(driver, ele);
		ele.click();
		Thread.sleep(1000);
		
		}
		
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
	
	public OPPatientDetails clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientDetails(driver);
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
			System.out.println("No element found " +e);
		}
		return ArrMessage;
		
	}
	
	public boolean verifyTickMarkOnPatientEligibility() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatElgMenu);
		return tickMark;
	}
	
	public void selectUnattendedCode()
	{
		CommonFunctions.selectOptionFromDropDown(unAttendCodeSelect, "Angina");
	}
	
	public OPGOS5PatientDeclaration PatientEligibilityDetailsEntered(String colName,String file,String sheet) throws IOException, InterruptedException 
	{
		OPHelpers.selectProvidedOptions(colName,file,sheet,driver);
		enterValuesInTextField(colName);
		selectUnattendedCode();
		clickOnSaveandNextButton();
		
		Thread.sleep(2000);
		
		return new OPGOS5PatientDeclaration(driver);
	}
	
	public void enterValuesInTextField(String colName) 
	{
		String strCertificateNumber=  ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTSELE", "CertificateNumber", colName);
		String strPrivateTestFee = ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTSELE", "PrivateSightTestFee", colName);
		//String strEthinicity= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTSELE", "Ethinicity", colName);
		String strReason= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "PATIENTSELE", "Reason", colName);
		
		Support.enterDataInTextField(certificateNumber, strCertificateNumber, wait);
		Support.enterDataInTextField(privateTestAmount, strPrivateTestFee, wait);
		
		/*wait.until(ExpectedConditions.elementToBeClickable(ethinicity));*/
		/*helpers.CommonFunctions.selectOptionFromDropDown(ethinicity, strEthinicity);*/

		Support.enterDataInTextField(reason, strReason, wait);
	}
	
	public void GOS5ClaimDetailssnaps(String string) throws InterruptedException, IOException {
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}

}
