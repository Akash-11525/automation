package pageobjects.OP;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS3SupplierDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;


/*	@FindBy(css="button[value='Draft']")
	WebElement saveButton;

	@FindBy(css="button[value='Save']")
	WebElement saveAndNextButton;*/
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[value='Save and Next']")
	WebElement saveAndNextButton;
	
	//@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;

	@FindBy(css="select[id='SupplierModel_FirstPairPrism']")
	WebElement selectFirstPairPrism;
	
	@FindBy(css="select[id='SupplierModel_FirstPairTint']")
	WebElement selectFirstPairTint;
	
	@FindBy(css="select[id='SupplierModel_SecondPairPrism']")
	WebElement selectSecondPairPrism;
	
	@FindBy(css="select[id='SupplierModel_SecondPairTint']")
	WebElement selectSecondPairTint;
	
	@FindBy(css="input[id='txtSuppliedDateOfFirstPair']")
	WebElement selectSuppliedDateOfFirstPair;
	
	@FindBy(css="input[id='txtSuppliedDateOfSecondPair']")
	WebElement selectSuppliedDateOfSecondPair;
	
	@FindBy(css="input[id='FirstPairGlassValue']")
	WebElement firstPairGlassValueTxtBox;
	
	@FindBy(css="input[id='SecondPairGlassValue']")
	WebElement secondPairGlassValueTxtBox;
	
	@FindBy(css="input[id='SupplierModel_ActualRetailPriceForFirstPair']")
	WebElement actualRetailPriceForFirstPairTxtBox;
	
	@FindBy(css="input[id='SupplierModel_ActualRetailPriceForSecondPair']")
	WebElement actualRetailPriceForSecondPairTxtBox;
	
	@FindBy(css="input[id='SupplierModel_TotalOfVoucherAndSupplementForFirstPair']")
	WebElement totalOfVoucherAndSupplementForFirstPairTxtBox;
	
	@FindBy(css="input[id='SupplierModel_TotalOfVoucherAndSupplementForSecondPair']")
	WebElement totalOfVoucherAndSupplementForSecondPairTxtBox;
	
	@FindBy(xpath="//div[@class='panel panel-default'][1]/ul/li")
	WebElement dateofPrescriptionField;
	
	@FindBy(xpath="//div[@class='row margin-top-10']'[4]")
	WebElement PrescriptionField;
	
	@FindBy(css="input[id='SupplierModel_TotalClaimForGlassesOrContactLenses']")
	WebElement totalClaimForGlassesOrContactLensesTxtBox;
	
	@FindBy(xpath="//span[contains(@class, 'field-validation-error')]")
	WebElement valError;
	
	@FindBy(css="input[id='SupplierModel_IsGlasses']")
	WebElement IsGlassesChkBox;
	
	@FindBy(css="input[id='FirstPairGlassValue']")
	WebElement FirstPairGlassValueTxtBox;
	
	@FindBy(css="input[id='SecondPairGlassValue']")
	WebElement SecondPairGlassValueTxtBox;
	
	@FindBy(css="input[id='IsSecondPairSmallGlasses']")
	WebElement SecondPairGlassesChkBox;
	
	@FindBy(css="input[id='IsNearPair']")
	WebElement IsNearPairCheckBox;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(xpath="//div[@class='loader hide-spinner']")
	WebElement hiddenSpinner;

	@FindBy(css="input#IsDistancePair")
	WebElement isDistancePair;
	
	@FindBy(css="input#IsNearPair")
	WebElement isNearPair;


	public OPGOS3SupplierDeclaration(WebDriver driver){

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

	public OPGOS3PatientDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
		
		
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		//Thread.sleep(10000); Commented by Akshay to optimize execution
		return new OPGOS3PatientDeclaration(driver);
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
	
	public void selectFirstPairPrism(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectFirstPairPrism, value);
	}
	
	public void selectSecondPairPrism(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectSecondPairPrism, value);
	}
	
	public void selectFirstPairTint(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectFirstPairTint, value);
	}
	
	public void selectSecondPairTint(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectSecondPairTint, value);
	}
	
	public void setDateofFirstPairSupplied() throws ParseException
	{
		if (!(CommonFunctions.isAttribtuePresent(selectSuppliedDateOfFirstPair, "disabled"))){
			String date = CommonFunctions.getTodayDate();
			scrolltoElement(driver, selectSuppliedDateOfFirstPair);
			Support.enterDataInTextField(selectSuppliedDateOfFirstPair, date, wait);
		}
	}
	
	public void setDateofSecondPairSupplied()
	{
		if (!(CommonFunctions.isAttribtuePresent(selectSuppliedDateOfSecondPair, "disabled"))){
			String date = CommonFunctions.getTodayDate();
			scrolltoElement(driver, selectSuppliedDateOfSecondPair);
			Support.enterDataInTextField(selectSuppliedDateOfSecondPair, date, wait);
		}
	}
	
	public void enterGOS3SupplierDetails(String colName)
	{
		String FIRSTPAIRGLASSVALUE = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "FIRSTPAIRGLASSVALUE", colName);
		String SECONDPAIRGLASSVALUE = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "SECONDPAIRGLASSVALUE", colName);
		
		//Double DbACTUALCOSTOFGLASSFIRSTPAIR = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "ACTUALCOSTOFGLASSFIRSTPAIR", colName);
		//String ACTUALCOSTOFGLASSFIRSTPAIR = String.format ("%.2f", DbACTUALCOSTOFGLASSFIRSTPAIR);
		
		String ACTUALCOSTOFGLASSFIRSTPAIR = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "ACTUALCOSTOFGLASSFIRSTPAIR", colName);
		
		//Double DbACTUALCOSTOFGLASSSECONDPAIR = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "ACTUALCOSTOFGLASSSECONDPAIR", colName);
		//String ACTUALCOSTOFGLASSSECONDPAIR = String.format ("%.2f", DbACTUALCOSTOFGLASSSECONDPAIR);
		String ACTUALCOSTOFGLASSSECONDPAIR = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "ACTUALCOSTOFGLASSSECONDPAIR", colName);
		
		//Double DbTOTALFORFIRSTPAIR = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "TOTALFORFIRSTPAIR", colName);
		//String TOTALFORFIRSTPAIR = String.format ("%.2f", DbTOTALFORFIRSTPAIR);
		String TOTALFORFIRSTPAIR = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "TOTALFORFIRSTPAIR", colName);
		
		//Double DbTOTALFORSECONDPAIR = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "TOTALFORSECONDPAIR", colName);
		//String TOTALFORSECONDPAIR = String.format ("%.2f", DbTOTALFORSECONDPAIR);
		String TOTALFORSECONDPAIR = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "TOTALFORSECONDPAIR", colName);
		//String ACTUALCOSTOFGLASSSECONDPAIR = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLRDETAILS", "ACTUALCOSTOFGLASSSECONDPAIR", colName);
		
		scrolltoElement(driver, firstPairGlassValueTxtBox);
		Support.enterDataInTextField(firstPairGlassValueTxtBox, FIRSTPAIRGLASSVALUE, wait);
		
		scrolltoElement(driver, secondPairGlassValueTxtBox);
		Support.enterDataInTextField(secondPairGlassValueTxtBox, SECONDPAIRGLASSVALUE, wait);
		
		scrolltoElement(driver, actualRetailPriceForFirstPairTxtBox);
		Support.enterDataInTextField(actualRetailPriceForFirstPairTxtBox, ACTUALCOSTOFGLASSFIRSTPAIR, wait);
		
		//If condition has been added by Akshay on 31st Oct 2018 as fields are being displayed based on the option selected
		if(isDistancePair.isSelected() && isNearPair.isSelected()){
			scrolltoElement(driver, actualRetailPriceForSecondPairTxtBox);
			wait.until(ExpectedConditions.elementToBeClickable(actualRetailPriceForSecondPairTxtBox)).click();
			actualRetailPriceForSecondPairTxtBox.clear();
			actualRetailPriceForSecondPairTxtBox.sendKeys(ACTUALCOSTOFGLASSSECONDPAIR);
			Support.enterDataInTextField(actualRetailPriceForSecondPairTxtBox, ACTUALCOSTOFGLASSSECONDPAIR, wait);
			
			scrolltoElement(driver, totalOfVoucherAndSupplementForSecondPairTxtBox);
			System.out.println(totalOfVoucherAndSupplementForSecondPairTxtBox.getText());
			Support.enterDataInTextField(totalOfVoucherAndSupplementForSecondPairTxtBox, TOTALFORSECONDPAIR, wait);
		}

		scrolltoElement(driver, totalOfVoucherAndSupplementForFirstPairTxtBox);
		System.out.println(totalOfVoucherAndSupplementForFirstPairTxtBox.getText());
		Support.enterDataInTextField(totalOfVoucherAndSupplementForFirstPairTxtBox, TOTALFORFIRSTPAIR, wait);
	}

	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "SUPPLIERDCLROPTIONS", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		wait.until(ExpectedConditions.elementToBeClickable(ele));
		scrolltoElement(driver, ele);
		if(!(ele.isSelected())){
			ele.click();
		}
		//ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	public void GOS3ClaimDetailssnaps(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, dateofPrescriptionField);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		/*scrolltoElement(driver, PrescriptionField);
		Screenshot.TakeSnap(driver, note+"_2");*/	
		
	}
	
	public void enterGOS3SupplierDeclarationDetails(int colNumber, String colName) throws IOException, InterruptedException, ParseException
	{	
		boolean isSpinnerVisible= verifyPageLoading();
		if(!isSpinnerVisible){
			selectProvidedOptions(colNumber);
			enterGOS3SupplierDetails(colName);
			setDateofFirstPairSupplied();
			setDateofSecondPairSupplied();
		}
	}
	
	public void GOS3ErrorsnapOnSupDclFirstPair(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, selectFirstPairPrism);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void clickOnSaveNNextButton() throws InterruptedException
	{
		enterSignatory(signatureCanvasbox, driver);
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(10000);
		
	}
	
	
	public void GOS3ErrorsnapOnSupDcltotalClaimForGlasses(String note) throws InterruptedException, IOException
	{
		//scrolltoElement(driver, totalClaimForGlassesOrContactLensesTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public void GOS3ErrorsnapOnSuppValidationError(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, IsGlassesChkBox);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
				
	}
	
	public void GOS3snapOnSuppValidationError(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, IsGlassesChkBox);
		Screenshot.TakeSnap(driver, note);
		
				
	}
	
	public void clearFirstPairGlassValue()
	{
		wait.until(ExpectedConditions.elementToBeClickable(FirstPairGlassValueTxtBox)).clear();
	}
	
	public void setValueInFirstPairGlassValue(String val)
	{
		Support.enterDataInTextField(FirstPairGlassValueTxtBox, val, wait);
	}
	
	public void setValueInSecondPairGlassValue(String val)
	{
		Support.enterDataInTextField(SecondPairGlassValueTxtBox, val, wait);
	}
	
	public void selectSecondPairGlassCheckBox()
	{
		scrolltoElement(driver, SecondPairGlassesChkBox);
		if(!SecondPairGlassesChkBox.isSelected()){
			wait.until(ExpectedConditions.elementToBeClickable(SecondPairGlassesChkBox)).click();
		}
	}
	
	public void selectnearPairCheckbox()
	{
		scrolltoElement(driver, IsNearPairCheckBox);
		if(!SecondPairGlassesChkBox.isSelected()){
			wait.until(ExpectedConditions.elementToBeClickable(IsNearPairCheckBox)).click();
		}
	}
	
	public boolean verifyPageLoading() throws InterruptedException
	{	
		boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent)
		{
			Thread.sleep(2000);
			ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}
		return ispresent;
	}
	
}
