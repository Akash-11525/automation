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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPPerformerDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;


/*	@FindBy(css="button[value='Draft']")
	WebElement saveButton;

	@FindBy(css="button[value='Save']")
	WebElement saveAndNextButton;*/
	
	@FindBy(css="input[value='Save for Later']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[value='Save and Next']")
	WebElement saveAndNextButton;
	
	@FindBy(css="input[value='Save awaiting Contractor Signatory']")
	WebElement SaveAwaitingContrator;
	
	//@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;

	@FindBy(id="outcomemodel_RetestCode")
	WebElement selectRetestCode;

	@FindBy(id="outcomemodel_FirstVoucherTypeCode")
	WebElement selectFirstVoucherTypeCode;

	@FindBy(id="outcomemodel_SecondVoucherTypeCode")
	WebElement selectSecondVoucherTypeCode;

	@FindBy(css="input[id='outcomemodel_IsFirstVoucherComplex']")
	WebElement firstVoucherComplexChkBox;

	@FindBy(css="input[id='outcomemodel_IsSecondVoucherComplex']")
	WebElement secondVoucherComplexChkBox;

	@FindBy(linkText="Ophthalmic")
	WebElement ophthalmicLink;
	
	@FindBy(id="outcomemodel_PatientReferred")
	WebElement patientReferredchekbox;
	
	@FindBy(tagName="h3")
	WebElement PerformerDeclarationHeader;

	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	WebElement claimNumberHeader;
	
	@FindBy(xpath="//select[@id='outcomemodel_RetestCode']")
	WebElement RetestCode;
	

	public OPPerformerDeclaration(WebDriver driver){

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
		//Thread.sleep(6000);
		wait.until(ExpectedConditions.visibilityOf(saveAndNextButton)); //Added by Akshay to optimize code execution
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait_OP(driver);
	}
	

	public void selectPeformerDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPTESTDATA.xlsx", "PERFORMERDECLOPTIONS", columnNumber);
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

	//@SuppressWarnings("unused")
	public OPContractorDeclaration PeformerDeclarationDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		Select dropdown = new Select(RetestCode);
		dropdown.selectByVisibleText("1");
		selectPeformerDeclarationOptions(colNumber);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		Thread.sleep(2000);

		return new OPContractorDeclaration(driver);
	}
	
	public void enterSignatoryDetails() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);
		enterSignatory(signatureCanvasbox, driver);
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
	
	public OPPatientDeclaration clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientDeclaration(driver);
	}
	
	public OPPerformerDeclaration PeformerDeclarationDetailsSaved(int colNumber) throws InterruptedException, IOException
	{
		selectPeformerDeclarationOptions(colNumber);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}

		//clickOnSaveandNextButton();
		clickOnSaveButton();
		Thread.sleep(2000);

		return new OPPerformerDeclaration(driver);
	}
	
	public OPPerformerDeclaration PerformerDeclarationDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		selectPeformerDeclarationOptions(colNumber);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}

		clickOnSaveandNextButton();
		Thread.sleep(2000);

		return new OPPerformerDeclaration(driver);
	}
	
	public OPHomePage clickOnOpththalmicLink()
	{
		scrolltoElement(driver, ophthalmicLink);
		wait.until(ExpectedConditions.elementToBeClickable(ophthalmicLink)).click();
		return new OPHomePage(driver);
	}
	
	public void GOS1ErrorsnapOnPerformerDcl(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, selectRetestCode);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void GOS1ProgressIndicatorSnap(String note) throws InterruptedException, IOException
	{
		//scrolltoElement(driver, PerformerDeclarationHeader);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}

	public void screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String HrMin = CommonFunctions.getCurrentHourMin();
		Screenshot.TakeSnap(driver, note+"_1_"+HrMin);
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2_"+HrMin);
	}
	
	public String getClaimNumber(String key ,int position) throws InterruptedException
	{
		Thread.sleep(1000);
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueinExcelWithPosition("OPTESTDATA.xlsx", "ClaimNumber", key, clmNo, position);
		return clmNo;
	}
	
	public OPPerformerDeclaration enterDetailsAndClickOnAwaitingContractor(int colNumber) throws InterruptedException, IOException
	{
		Select dropdown = new Select(RetestCode);
		dropdown.selectByVisibleText("1");	
		selectPeformerDeclarationOptions(colNumber);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}
		enterSignatoryDetails();
		clickOnSaveawaitingContrator();
		Thread.sleep(2000);

		return new OPPerformerDeclaration(driver);
	}
	
	public void clickOnSaveawaitingContrator() throws InterruptedException
	{
		scrolltoElement(driver, SaveAwaitingContrator);
		wait.until(ExpectedConditions.elementToBeClickable(SaveAwaitingContrator)).click();
		Support.verifyPageLoading(driver);
		Thread.sleep(2000);
	}
	
	public OPContractorDeclaration PeformerDeclarationDetailsEntered(String colName,String file,String sheet) throws InterruptedException, IOException
	{
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}
		enterSignatoryDetails();
		clickOnSaveandNextButton();
		Thread.sleep(2000);

		return new OPContractorDeclaration(driver);
	}
	
	public OPPerformerDeclaration PeformerDeclarationDetailsSaved(String colName,String file,String sheet) throws InterruptedException, IOException
	{
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		scrolltoElement(driver, selectFirstVoucherTypeCode);
		String voucher1 = selectFirstVoucherTypeCode.getAttribute("disabled");
		
		if(voucher1 != null && !voucher1.isEmpty())
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(firstVoucherComplexChkBox)).click();
		}
		else{
			
			CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, "A");
		}

		String voucher2 = selectSecondVoucherTypeCode.getAttribute("disabled");
		if(voucher2 != null && !voucher2.isEmpty() )
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(secondVoucherComplexChkBox)).click();
		}
		else{
		
			CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, "A");
		}

		//clickOnSaveandNextButton();
		enterSignatoryDetails();
		clickOnSaveButton();
		Thread.sleep(2000);

		return new OPPerformerDeclaration(driver);
	}

}
