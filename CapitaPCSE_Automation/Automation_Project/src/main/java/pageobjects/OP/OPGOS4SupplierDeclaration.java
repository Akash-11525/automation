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
import helpers.OPHelpers;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS4SupplierDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;


/*	@FindBy(css="button[value='Draft']")
	WebElement saveButton;

	@FindBy(css="button[value='Save']")
	WebElement saveAndNextButton;*/
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[id='btnSaveNext']")
	WebElement saveAndNextButton;
	
	//@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(css="select[id='IsSmallGlasses']")
	WebElement selectSmallGlasses;

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
	
	@FindBy(css="input[id*='txtPMPrescriptionRightDistanceSph']")
	WebElement pmPrescRightDistanceSphTxtBox;

	@FindBy(css="input[id*='txtPrescriptionRightDistanceSph']")
	WebElement prescRightDistanceSphTxtBox;
	
	@FindBy(css="input[id*='txtPMPrescriptionRightDistanceCyl']")
	WebElement pmPrescRightDistanceCylTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionRightDistanceCyl']")
	WebElement prescRightDistanceSCylTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionRightDistanceAxis']")
	WebElement prescRightDistanceAxisTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionRightDistancePrism']")
	WebElement prescRightDistancePrismTxtBox;
	
	@FindBy(css="select[id='txtPrescriptionRightDistanceBase']")
	WebElement prescRightDistanceBaseTxtBox;
	
	@FindBy(css="input[id*='txtPMPrescriptionRightNearSph']")
	WebElement pmPrescRightNearSphTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionRightNearSph']")
	WebElement prescRightNearSphTxtBox;
	
	@FindBy(css= "input[id*='txtPrescriptionRightNearPrism']")
	WebElement prescRightNearPrismTxtBox;
	
	@FindBy(css= "select[id='txtPrescriptionRightNearBase']")
	WebElement prescRightNearBaseTxtBox;
	
	// Left
	
	@FindBy(css="input[id*='txtPMPrescriptionLeftDistanceSph']")
	WebElement pmPrescLeftDistanceSphTxtBox;

	@FindBy(css="input[id*='txtPrescriptionLeftDistanceSph']")
	WebElement prescLeftDistanceSphTxtBox;
	
	@FindBy(css="input[id*='txtPMPrescriptionLeftDistanceCyl']")
	WebElement pmPrescLeftDistanceCylTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionLeftDistanceCyl']")
	WebElement prescLeftDistanceSCylTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionLeftDistanceAxis']")
	WebElement prescLeftDistanceAxisTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionLeftDistancePrism']")
	WebElement prescLeftDistancePrismTxtBox;
	
	@FindBy(css="select[id='txtPrescriptionLeftDistanceBase']")
	WebElement prescLeftDistanceBaseTxtBox;
	
	@FindBy(css="input[id*='txtPMPrescriptionLeftNearSph']")
	WebElement pmPrescLeftNearSphTxtBox;
	
	@FindBy(css="input[id*='txtPrescriptionLeftNearSph']")
	WebElement prescLeftNearSphTxtBox;
	
	@FindBy(css= "input[id*='txtPrescriptionLeftNearPrism']")
	WebElement prescLeftNearPrismTxtBox;
	
	@FindBy(css= "select[id='txtPrescriptionLeftNearBase']")
	WebElement prescLeftNearBaseTxtBox;
	
	@FindBy(css="input[id*='txtVoucherValue']")
	WebElement txtVoucherValueTxtBox;
	
	@FindBy(css="input[id*='txtLenseValue']")
	WebElement txtLenseValueTxtBox;
	
	@FindBy(css="input[id*='txtFrameValue']")
	WebElement txtFrameValueTxtBox;
	
	@FindBy(css="input[id*='txtPrismSupplement']")
	WebElement txtPrismSupplementTxtBox;
	
	@FindBy(css="input[id*='txtTintSupplement']")
	WebElement txtTintSupplementTxtBox;
	
	@FindBy(css="input[id*='SupplierModel_SmallGlassesMeasurement']")
	WebElement SmallGlassesMeasurementTxtBox;
	
	@FindBy(css="input[id*='txtSupplementValue']")
	WebElement txtSupplementValueTxtBox;
	
	@FindBy(css="input[id*='txtRetailCostTotal']")
	WebElement txtRetailCostTotalTxtBox;
	
	@FindBy(css="select[id*='VoucherType']")
	WebElement selectFirstVoucherType;
	
	@FindBy(css="select[id*='SupplierModel_GlassesOrContactLense']")
	WebElement selectActualRetailCostType;
	
	@FindBy(xpath="//ul[@class='list-group row']/li[1]")
	WebElement ContractorNameField;
	
	@FindBy(xpath="//ul[@class='list-group row']/li[17]")
	WebElement patientDlrField;
	
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	




	public OPGOS4SupplierDeclaration(WebDriver driver){

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

	public OPGOS4PatientDeclaration2 clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
		
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(10000);
		return new OPGOS4PatientDeclaration2(driver);
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
		String date = CommonFunctions.getTodayDate();
		scrolltoElement(driver, selectSuppliedDateOfFirstPair);
		Support.enterDataInTextField(selectSuppliedDateOfFirstPair, date, wait);
	}
	
	public void setDateofSecondPairSupplied()
	{
		String date = CommonFunctions.getTodayDate();
		scrolltoElement(driver, selectSuppliedDateOfSecondPair);
		Support.enterDataInTextField(selectSuppliedDateOfSecondPair, date, wait);
	}
	
	public void enterGOS4SupplierDetails(String colName)
	{
		String PMDISTRGTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescRightDistanceSphTxtBox", colName);
		
		String RGTDISTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceSph", colName);
		
	/*	Double DbRGTDISTSPH = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceSph", colName);
		String RGTDISTSPH = String.format ("%.2f", DbRGTDISTSPH);*/
		
		String PMRGTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionRightDistanceCyl", colName);
		String RGTDISTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceCyl", colName);
		/*Double DbRGTDISTCYL = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceCyl", colName);
		String RGTDISTCYL = String.format ("%.2f", DbRGTDISTCYL);*/
		
		String RGTDISTAXIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceAxis", colName);
		String RGTDISTPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistancePrism", colName);
		String RGTDISTBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceBase", colName);
		//String PMNRRGTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionRightNearSph", colName);
		String RGTNRSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearSph", colName);
	/*	Double DbRGTNRSPH = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearSph", colName);
		String RGTNRSPH = String.format ("%.2f", DbRGTNRSPH);*/
		
		
		String RGTNRPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearPrism", colName);
		String RGTNRBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearBase", colName);
		
		String PMDISTLFTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescLeftDistanceSphTxtBox", colName);
		String LFTDISTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceSph", colName);
	/*	Double DbLFTDISTSPH = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceSph", colName);
		String LFTDISTSPH = String.format ("%.2f", DbLFTDISTSPH);*/
		
		String PMLFTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionLeftDistanceCyl", colName);
		String LFTDISTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceCyl", colName);
		/*Double DbLFTDISTCYL = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceCyl", colName);
		String LFTDISTCYL = String.format ("%.2f", DbLFTDISTCYL);*/
		
		
		String LFTDISTAXIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceAxis", colName);
		String LFTDISTPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistancePrism", colName);
		String LFTDISTBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceBase", colName);
		//String PMNRLFTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionLeftNearSph", colName);
		String LFTNRSPH = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearSph", colName);
		/*Double DbLFTNRSPH = ExcelUtilities.getNumValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearSph", colName);
		String LFTNRSPH = String.format ("%.2f", DbLFTNRSPH);*/
		
		
		String LFTNRPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearPrism", colName);
		String LFTNRBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearBase", colName);
		
		String VchrValue = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "VoucherValue", colName);
		String LNSValue = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "LenseValue", colName);
		String FRMValue = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "FrameValue", colName);
		String PRSMSupp = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PrismSuppliment", colName);
		String TINTSupp = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "TintSuppliment", colName);
		String SMGLMSMT = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "SmallGlassMeasurement", colName);
		String SUPPVAL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "SupplimentValue", colName);
		String RTLCOSTTOTAL = ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "RetailCostTotal", colName);
		
		
		scrolltoElement(driver, pmPrescRightDistanceSphTxtBox);
		Support.enterDataInTextField(pmPrescRightDistanceSphTxtBox, PMDISTRGTSPH, wait);
		
		scrolltoElement(driver, prescRightDistanceSphTxtBox);
		Support.enterDataInTextField(prescRightDistanceSphTxtBox, RGTDISTSPH, wait);
		
		scrolltoElement(driver, pmPrescRightDistanceCylTxtBox);
		Support.enterDataInTextField(pmPrescRightDistanceCylTxtBox, PMRGTCYL, wait);
		
		scrolltoElement(driver, prescRightDistanceSCylTxtBox);
		Support.enterDataInTextField(prescRightDistanceSCylTxtBox, RGTDISTCYL, wait);
		
		scrolltoElement(driver, prescRightDistanceAxisTxtBox);
		Support.enterDataInTextField(prescRightDistanceAxisTxtBox, RGTDISTAXIS, wait);
		
		scrolltoElement(driver, prescRightDistancePrismTxtBox);
		Support.enterDataInTextField(prescRightDistancePrismTxtBox, RGTDISTPRISM, wait);
		
		scrolltoElement(driver, prescRightDistanceBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescRightDistanceBaseTxtBox));
		if(!RGTDISTBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescRightDistanceBaseTxtBox, RGTDISTBASIS);
		}
		/*prescRightDistanceBaseTxtBox.sendKeys(RGTDISTBASIS);*/
		
		
/*		scrolltoElement(driver, pmPrescRightNearSphTxtBox);
		wait.until(ExpectedConditions.elementToBeClickable(pmPrescRightNearSphTxtBox)).click();
		pmPrescRightNearSphTxtBox.sendKeys(PMNRRGTSPH);*/
		
		scrolltoElement(driver, prescRightNearSphTxtBox);
		Support.enterDataInTextField(prescRightNearSphTxtBox, RGTNRSPH, wait);
		
		scrolltoElement(driver, prescRightNearPrismTxtBox);
		Support.enterDataInTextField(prescRightNearPrismTxtBox, RGTNRPRISM, wait);
		
		scrolltoElement(driver, prescRightNearBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescRightNearBaseTxtBox));
		if(!RGTNRBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescRightNearBaseTxtBox, RGTNRBASIS);
		}
		/*prescRightNearBaseTxtBox.sendKeys(RGTNRBASIS);*/
		
		scrolltoElement(driver, pmPrescLeftDistanceSphTxtBox);
		Support.enterDataInTextField(pmPrescLeftDistanceSphTxtBox, PMDISTLFTSPH, wait);
		
		scrolltoElement(driver, prescLeftDistanceSphTxtBox);
		Support.enterDataInTextField(prescLeftDistanceSphTxtBox, LFTDISTSPH, wait);
		
		scrolltoElement(driver, pmPrescLeftDistanceCylTxtBox);
		Support.enterDataInTextField(pmPrescLeftDistanceCylTxtBox, PMLFTCYL, wait);
		
		scrolltoElement(driver, prescLeftDistanceSCylTxtBox);
		Support.enterDataInTextField(prescLeftDistanceSCylTxtBox, LFTDISTCYL, wait);
		
		scrolltoElement(driver, prescLeftDistanceAxisTxtBox);
		Support.enterDataInTextField(prescLeftDistanceAxisTxtBox, LFTDISTAXIS, wait);
		
		scrolltoElement(driver, prescLeftDistancePrismTxtBox);
		Support.enterDataInTextField(prescLeftDistancePrismTxtBox, LFTDISTPRISM, wait);
		
		scrolltoElement(driver, prescLeftDistanceBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescLeftDistanceBaseTxtBox));
		if(!LFTDISTBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescLeftDistanceBaseTxtBox, LFTDISTBASIS);
		}
		/*prescLeftDistanceBaseTxtBox.sendKeys(LFTDISTBASIS);*/
		
/*		scrolltoElement(driver, pmPrescLeftNearSphTxtBox);
		wait.until(ExpectedConditions.elementToBeClickable(pmPrescLeftNearSphTxtBox)).click();
		pmPrescLeftNearSphTxtBox.sendKeys(PMNRLFTSPH);*/
		
		scrolltoElement(driver, prescLeftNearSphTxtBox);
		Support.enterDataInTextField(prescLeftNearSphTxtBox, LFTNRSPH, wait);
		
		scrolltoElement(driver, prescLeftNearPrismTxtBox);
		Support.enterDataInTextField(prescLeftNearPrismTxtBox, LFTNRPRISM, wait);
		
		scrolltoElement(driver, prescLeftNearBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescLeftNearBaseTxtBox));
		if(!LFTNRBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescLeftNearBaseTxtBox, LFTNRBASIS);
		}
		/*prescLeftNearBaseTxtBox.sendKeys(LFTNRBASIS);*/
		
		scrolltoElement(driver, txtVoucherValueTxtBox);
		Support.enterDataInTextField(txtVoucherValueTxtBox, VchrValue, wait);
		
		scrolltoElement(driver, txtLenseValueTxtBox);
		Support.enterDataInTextField(txtLenseValueTxtBox, LNSValue, wait);
		
		scrolltoElement(driver, txtFrameValueTxtBox);
		Support.enterDataInTextField(txtFrameValueTxtBox, FRMValue, wait);
		
		scrolltoElement(driver, txtPrismSupplementTxtBox);
		Support.enterDataInTextField(txtPrismSupplementTxtBox, PRSMSupp, wait);
		
		scrolltoElement(driver, txtTintSupplementTxtBox);
		Support.enterDataInTextField(txtTintSupplementTxtBox, TINTSupp, wait);
		
		scrolltoElement(driver, SmallGlassesMeasurementTxtBox);
		Support.enterDataInTextField(SmallGlassesMeasurementTxtBox, SMGLMSMT, wait);
		
		scrolltoElement(driver, txtSupplementValueTxtBox);
		Support.enterDataInTextField(txtSupplementValueTxtBox, SUPPVAL, wait);
		
		scrolltoElement(driver, txtRetailCostTotalTxtBox);
		wait.until(ExpectedConditions.elementToBeClickable(txtRetailCostTotalTxtBox)).click();
		txtRetailCostTotalTxtBox.clear();
		txtRetailCostTotalTxtBox.sendKeys(RTLCOSTTOTAL);
		Support.enterDataInTextField(txtRetailCostTotalTxtBox, RTLCOSTTOTAL, wait);
	}

	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		//Thread.sleep(1000);
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "SUPPLIERDECLOPTIONS", columnNumber);
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
	
	/*public void GOS3ClaimDetailssnaps(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, dateofPrescriptionField);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		scrolltoElement(driver, PrescriptionField);
		Screenshot.TakeSnap(driver, note+"_2");	
		
	}*/
	
	public void enterGOS4SupplierDeclarationDetails(int colNumber, String colName) throws IOException, InterruptedException, ParseException
	{
		selectProvidedOptions(colNumber);
		enterGOS4SupplierDetails(colName);
		setDateofFirstPairSupplied();
		setDateofSecondPairSupplied();
	}
	
	public void GOS3ErrorsnapOnSupDclFirstPair(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, selectFirstPairPrism);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void clickOnSaveNNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(10000);
		
	}
	
	
	public void GOS3ErrorsnapOnSupDcltotalClaimForGlasses(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, totalClaimForGlassesOrContactLensesTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void selectVoucherType(String val)
	{
		scrolltoElement(driver, selectFirstVoucherType);
		helpers.CommonFunctions.selectOptionFromDropDown(selectFirstVoucherType, val);
		
	}
	
	public void selectActualRetailCostType(String val)
	{
		scrolltoElement(driver, selectActualRetailCostType);
		helpers.CommonFunctions.selectOptionFromDropDown(selectActualRetailCostType, val);
		
	}
		
	public void enterGOS4SupplierDetails(String colName, int ColNum) throws InterruptedException, IOException
	{
		selectProvidedOptions(ColNum);
		enterGOS4SupplierDetails(colName);
		//clickOnSaveandNextButton();
		//return new OPGOS4PatientDeclaration2(driver);
	}
	
	public void GOS4ClaimDetailssnaps(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, ContractorNameField);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		//scrolltoElement(driver, patientDlrField);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		/*scrolltoElement(driver, PrescriptionField);
		Screenshot.TakeSnap(driver, note+"_2");*/
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_3");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_4");
		Thread.sleep(1000);
		
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public void GOS4ErrorSupplierDeclaration(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, pmPrescRightDistanceSphTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void GOS4ErrorPrismSuppliement(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, txtPrismSupplementTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public void enterGOS4SupplierDetails(String colName,String recordCol, String file, String sheet) throws InterruptedException, IOException
	{
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		enterGOS4SupplierDetails(recordCol);
	}
	

}
