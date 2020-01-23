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

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import helpers.WindowHandleSupport;
import utilities.ExcelUtilities;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPGOS3Prescription extends Support {
	WebDriver driver;
	WebDriverWait wait;
	// Presscription Right
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
	
	
	@FindBy(css="select[id='outcomemodel_FirstVoucherTypeCode']")
	WebElement selectFirstVoucherTypeCode;
	
	@FindBy(css="select[id='outcomemodel_SecondVoucherTypeCode']")
	WebElement selectSecondVoucherTypeCode;
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		
	@FindBy(css="input[id*='btnSaveNext']")
	WebElement saveAndNextButton;
	
	
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(css="input[id='SupplierDeclaration_IsGlasses']")
	WebElement SupDeclIsGlassesChkBox;
	
	@FindBy(css="input[name='performerdeclaration.PerformerName']")
	WebElement PerfName;
	
	@FindBy(css="input[name='performerdeclaration.PerformerNumber")
	WebElement PerfNumber;
	
	@FindBy(css="input[id='txtVoucherCode']")
	WebElement VoucherCodeTxt;
	
	@FindBy(css="input[id='txtAuthorisationCode']")
	WebElement AuthorisationCodeTxt;
	
	@FindBy(css="input[value='Create GOS3 Voucher']")
	WebElement createGOS3VoucherButton;
	
	@FindBy(xpath="//form[@action='/OphthalmicGosThree/SubmitPatientEligibility']/div[22]/div/strong")
	WebElement claimNumberEle;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	
	
	
	
	public OPGOS3Prescription(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void enterPrescriptionDetails(String colName) throws InterruptedException
	{
		
		String PMDISTRGTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescRightDistanceSphTxtBox", colName);
		
		String RGTDISTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceSph", colName);
		
	/*	Double DbRGTDISTSPH = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceSph", colName);
		String RGTDISTSPH = String.format ("%.2f", DbRGTDISTSPH);*/
		
		String PMRGTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionRightDistanceCyl", colName);
		String RGTDISTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceCyl", colName);
		/*Double DbRGTDISTCYL = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceCyl", colName);
		String RGTDISTCYL = String.format ("%.2f", DbRGTDISTCYL);*/
		
		String RGTDISTAXIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceAxis", colName);
		String RGTDISTPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistancePrism", colName);
		String RGTDISTBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightDistanceBase", colName);
		//String PMNRRGTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionRightNearSph", colName);
		String RGTNRSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearSph", colName);
	/*	Double DbRGTNRSPH = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearSph", colName);
		String RGTNRSPH = String.format ("%.2f", DbRGTNRSPH);*/
		
		
		String RGTNRPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearPrism", colName);
		String RGTNRBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionRightNearBase", colName);
		
		String PMDISTLFTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescLeftDistanceSphTxtBox", colName);
		String LFTDISTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceSph", colName);
	/*	Double DbLFTDISTSPH = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceSph", colName);
		String LFTDISTSPH = String.format ("%.2f", DbLFTDISTSPH);*/
		
		String PMLFTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionLeftDistanceCyl", colName);
		String LFTDISTCYL = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceCyl", colName);
		/*Double DbLFTDISTCYL = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceCyl", colName);
		String LFTDISTCYL = String.format ("%.2f", DbLFTDISTCYL);*/
		
		
		String LFTDISTAXIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceAxis", colName);
		String LFTDISTPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistancePrism", colName);
		String LFTDISTBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftDistanceBase", colName);
		//String PMNRLFTSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "PMPrescriptionLeftNearSph", colName);
		String LFTNRSPH = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearSph", colName);
		/*Double DbLFTNRSPH = ExcelUtilities.getNumValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearSph", colName);
		String LFTNRSPH = String.format ("%.2f", DbLFTNRSPH);*/
		
		
		String LFTNRPRISM = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearPrism", colName);
		String LFTNRBASIS = ExcelUtilities.getKeyValueByPosition("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONDETAILS", "txtPrescriptionLeftNearBase", colName);
		
		
		scrolltoElement(driver, pmPrescRightDistanceSphTxtBox);
		Support.enterDataInTextField(pmPrescRightDistanceSphTxtBox, PMDISTRGTSPH, wait);
		
		scrolltoElement(driver, prescRightDistanceSphTxtBox);
		Support.enterDataInTextField(prescRightDistanceSphTxtBox, RGTDISTSPH, wait);
		
		scrolltoElement(driver, prescRightDistanceSCylTxtBox);
		Support.enterDataInTextField(prescRightDistanceSCylTxtBox, RGTDISTCYL, wait);
		
		scrolltoElement(driver, pmPrescRightDistanceCylTxtBox);
		Support.enterDataInTextField(pmPrescRightDistanceCylTxtBox, PMRGTCYL, wait);
		
		scrolltoElement(driver, prescRightDistanceAxisTxtBox);
		Support.enterDataInTextField(prescRightDistanceAxisTxtBox, RGTDISTAXIS, wait);
		
		scrolltoElement(driver, prescRightDistancePrismTxtBox);
		Support.enterDataInTextField(prescRightDistancePrismTxtBox, RGTDISTPRISM, wait);
		
		scrolltoElement(driver, prescRightDistanceBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescRightDistanceBaseTxtBox));
		if(!RGTDISTBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescRightDistanceBaseTxtBox, RGTDISTBASIS);
		}
/*		prescRightDistanceBaseTxtBox.clear();
		prescRightDistanceBaseTxtBox.sendKeys(RGTDISTBASIS);*/
		
		
/*		scrolltoElement(driver, pmPrescRightNearSphTxtBox);
		wait.until(ExpectedConditions.elementToBeClickable(pmPrescRightNearSphTxtBox)).click();
		pmPrescRightNearSphTxtBox.clear();
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
/*		prescRightNearBaseTxtBox.clear();
		prescRightNearBaseTxtBox.sendKeys(RGTNRBASIS);*/
		
		scrolltoElement(driver, pmPrescLeftDistanceSphTxtBox);
		Support.enterDataInTextField(pmPrescLeftDistanceSphTxtBox, PMDISTLFTSPH, wait);
		
		scrolltoElement(driver, prescLeftDistanceSphTxtBox);
		Support.enterDataInTextField(prescLeftDistanceSphTxtBox, LFTDISTSPH, wait);
		
		scrolltoElement(driver, prescLeftDistanceSCylTxtBox);
		Support.enterDataInTextField(prescLeftDistanceSCylTxtBox, LFTDISTCYL, wait);
		
		scrolltoElement(driver, pmPrescLeftDistanceCylTxtBox);
		Support.enterDataInTextField(pmPrescLeftDistanceCylTxtBox, PMLFTCYL, wait);
		
		scrolltoElement(driver, prescLeftDistanceAxisTxtBox);
		Support.enterDataInTextField(prescLeftDistanceAxisTxtBox, LFTDISTAXIS, wait);
		
		scrolltoElement(driver, prescLeftDistancePrismTxtBox);
		Support.enterDataInTextField(prescLeftDistancePrismTxtBox, LFTDISTPRISM, wait);
		
		scrolltoElement(driver, prescLeftDistanceBaseTxtBox);
		//wait.until(ExpectedConditions.elementToBeClickable(prescLeftDistanceBaseTxtBox)).click();
		if(!LFTDISTBASIS.isEmpty()){
			CommonFunctions.selectOptionFromDropDown(prescLeftDistanceBaseTxtBox, LFTDISTBASIS);
		}
		/*prescLeftDistanceBaseTxtBox.clear();
		prescLeftDistanceBaseTxtBox.sendKeys(LFTDISTBASIS);*/
		
/*		scrolltoElement(driver, pmPrescLeftNearSphTxtBox);
		wait.until(ExpectedConditions.elementToBeClickable(pmPrescLeftNearSphTxtBox)).click();
		pmPrescLeftNearSphTxtBox.clear();
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
		/*prescLeftNearBaseTxtBox.clear();
		prescLeftNearBaseTxtBox.sendKeys(LFTNRBASIS);*/
		
	}
	
	public void selectFirstVoucherType(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectFirstVoucherTypeCode, value);
	}
	
	public void selectSecondVoucherType(String value)
	{
		CommonFunctions.selectOptionFromDropDown(selectSecondVoucherTypeCode, value);
	}
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, saveButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(2000);
	}
	
	public OPGOS3PatientEligibility clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
		
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		//Thread.sleep(9000); Commented by Akshay to optimize execution
		return new OPGOS3PatientEligibility(driver);
		
	}
	
	public OPHomePage clickOnCreateVoucherButton(String key) throws InterruptedException, IOException
	{
		
		String winHandleBefore = driver.getWindowHandle();
		scrolltoElement(driver, createGOS3VoucherButton);
		wait.until(ExpectedConditions.elementToBeClickable(createGOS3VoucherButton)).click();
		Thread.sleep(12000);
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}
		Screenshot.TakeSnap(driver, key+"_Voucher");
		
		//driver.close();
		driver.switchTo().window(winHandleBefore);
		WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
		return new OPHomePage(driver);
		
	}
	
	public OPHomePage clickOnCreateVoucherButtonInvalid(String key) throws InterruptedException, IOException
	{
		
		String winHandleBefore = driver.getWindowHandle();
		scrolltoElement(driver, createGOS3VoucherButton);
		wait.until(ExpectedConditions.elementToBeClickable(createGOS3VoucherButton)).click();
		Thread.sleep(12000);
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		}
		Screenshot.TakeSnap(driver, key+"_Voucher");
		
		//driver.close();
		driver.switchTo().window(winHandleBefore);
		//WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
		return new OPHomePage(driver);
		
	}
	
	public OPPatientDetails clickOnPreviousButton() throws InterruptedException
	{
		scrolltoElement(driver, previousButton);
		wait.until(ExpectedConditions.elementToBeClickable(previousButton)).click();
		Thread.sleep(2000);
		return new OPPatientDetails(driver);
	}
	
	public void enterPatientPrescriptionDetails(String colName, String firstVoucherType, String secondVoucherType) throws InterruptedException
	{
		enterPrescriptionDetails(colName);
		selectFirstVoucherType(firstVoucherType);
		selectSecondVoucherType(secondVoucherType);
	}
	
	public void selectProvidedOptions(int columnNumber) throws IOException, InterruptedException
	{
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS3TESTDATA.xlsx", "PRESCRIPTIONOPTIONS", columnNumber);
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
	
	
	public void GOS3ErrorsnapOnPrescriptionRightDistance(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, pmPrescRightDistanceSphTxtBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public List<String> GOS3PerformerDetailsonPrescription()
	{
		List<String> PerfDetails = new ArrayList<>();
		scrolltoElement(driver, PerfName);
		String performerName = PerfName.getText();
		PerfDetails.add(performerName);
		
		String performerNumber = PerfNumber.getText();
		PerfDetails.add(performerNumber);
		
		return PerfDetails;
	}
	
	public void GOS3PerformerDetailsOnPrescription(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, PerfName);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public String getVoucherCode()
	{
		String vCode = null;
		scrolltoElement(driver, VoucherCodeTxt);
		vCode = VoucherCodeTxt.getAttribute("value");
		System.out.println("Vcode : "+vCode);
		return vCode;		
	}
	
	public String getAuthCode()
	{
		String aCode = null;
		scrolltoElement(driver, AuthorisationCodeTxt);
		aCode = AuthorisationCodeTxt.getAttribute("value");
		System.out.println("Acode : "+aCode);
		return aCode;		
	}
	
	public String getClaimNumber(String key) throws InterruptedException
	{
		Thread.sleep(1000);
		scrolltoElement(driver, claimNumberEle);
		String clmNo = claimNumberEle.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}

	public void enterPrescriptionSignatory() throws InterruptedException {
		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);
	}
	
	//this method is created by Akshay as after searching voucher, under prescription page, signatory is disabled
	public OPGOS3PatientEligibility clickOnSaveandNextButtonWithoutSignatory() throws InterruptedException
	{	
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(9000);
		return new OPGOS3PatientEligibility(driver);
	}
}
