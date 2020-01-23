package pageobjects.OP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS4NHSEnglandApproval extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
/*	@FindBy(css="button[value='Draft']")
	WebElement saveButton;
	
	@FindBy(css="button[value='Save']")
	WebElement saveAndNextButton;*/
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[value='Save for Later']")
	WebElement saveAndNextButton;
	
//	@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	

   @FindBy(css="input[id*='ApprovalCode']")
   WebElement approvalCodeTxtBox;
	
	
	
	public OPGOS4NHSEnglandApproval(WebDriver driver){

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
	
	public OPGOS4SupplierDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPGOS4SupplierDeclaration(driver);
	}
	
	/*public void selectDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		
		
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPTESTDATA.xlsx", "NHSENGLANDAPPROVALOPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		scrolltoElement(driver, ele);
		wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
		//ele.click();
		Thread.sleep(1000);
		
		}

	}*/
	
	public OPGOS4SupplierDeclaration NHSEnglandApprovalDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		String appCode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS4TESTDATA.xlsx", "NHSENGLANDAPPROVAL", "ApprovalCode", colNumber);
		Support.enterDataInTextField(approvalCodeTxtBox, appCode, wait);
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return new OPGOS4SupplierDeclaration(driver);
	}
	
	public  List<String> AcutalErrormessage()
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//span[contains(@class, 'field-validation-error')]"));
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
	
	public OPPatientEligibility clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientEligibility(driver);
	}
	
	public void snapApprovalCodeRequired(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, approvalCodeTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public OPGOS4SupplierDeclaration NHSEnglandApprovalDetailsEntered(String colName,String file) throws InterruptedException, IOException
	{
		Thread.sleep(1000);
		String appCode = ExcelUtilities.getKeyValueByPosition(file, "NHSENGLANDAPPROVAL", "ApprovalCode", colName);
		wait.until(ExpectedConditions.elementToBeClickable(approvalCodeTxtBox)).sendKeys(appCode);
		clickOnSaveandNextButton();
		Thread.sleep(2000);
		
		return new OPGOS4SupplierDeclaration(driver);
	}

	
}
