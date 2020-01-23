package pageobjects.OP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
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

public class OPGOS4PatientDeclaration2 extends Support {
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
	
//	@FindBy(css="button[value='Back']")
	@FindBy(css="input[value='Previous']")
	WebElement previousButton;
	
	@FindBy(xpath="//div[@id='sidebar']//a[@class='list-group-item active']")
	WebElement activeSideMenuItem;
	
	@FindBy(id="DivPatientDeclarationli")
	WebElement PatDeclMenu;
	
	@FindBy(id="DivPatientEligibilityli")
	WebElement PatElgMenu;
	
	@FindBy(css="button[name='btnSaveNext']")
	WebElement submitButton;

	@FindBy(css="button[id='btnClose']")
	WebElement closeButton;

	@FindBy(css="button[id='btnCancelClaim']")
	WebElement cancelClaimButton;

	@FindBy(xpath="//form[@id='myform']//strong")
	WebElement claimNumberHeader;

	@FindBy(id="modalResult")
	WebElement modalResultPopUp;

	//@FindBy(xpath="("//div[@id='modalResult']//div[@class='modal-body']")") == btn btn-default
	@FindBy(xpath="//div[@id='modalResult']//div[@class='modal-body']")
	WebElement modalResultPopupText;

	@FindBy(css = "button[class='btn btn-default']")
	WebElement modalResultCloseBtn;

	@FindBy(xpath="//div[@id='modalCancelClaim']//div[@class='modal-body']")
	WebElement modalCancelPopupBody;

	@FindBy(xpath="//div[@id='modalCancelClaim']//button[@class='btn btn-success']")
	WebElement modalCancelPopupYesBtn;

	@FindBy(xpath="//div[@id='modalCancelClaim']//button[@class='btn btn-default']")
	WebElement modalCancelPopupNoBtn;
	
	@FindBy(xpath="//*[contains(@id, 'IsDistance')]")
	WebElement IsDistanceChkBox;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(id="IsPatientParent")
	WebElement patientCheckBox;
	
	@FindBy(id="DeclarationName")
	WebElement declarationNameTxt;
	
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
	
	@FindBy(xpath="//div[@id='AddressModal']//button[@class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	public OPGOS4PatientDeclaration2(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void clickOnSaveButton() throws InterruptedException
	{
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(2000);
	}
	
	public void clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
	}
	
	public void selectDeclarationOptions(int columnNumber) throws IOException, InterruptedException
	{
		
		Thread.sleep(5000);
		List<String> CellValues = ExcelUtilities.getCellValuesInExcel("OPGOS4TESTDATA.xlsx", "PATIENTDECLOPTIONS2", columnNumber);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		wait.until(ExpectedConditions.visibilityOf(ele));
		scrolltoElement(driver, ele);
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
		//ele.click();
		Thread.sleep(1000);
		
		}

	}
	
	public boolean PatientDeclarationDetailsEntered(int colNumber) throws InterruptedException, IOException
	{
		Boolean subFlag = false;
		Thread.sleep(1000);
		selectDeclarationOptions(colNumber);
		enterSigntatoryDetails();
		subFlag = clickOnSubmitButton();
		Thread.sleep(1000);
		
		return subFlag;
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
	
	public boolean verifyTickMarkOnPatientDeclaration() throws InterruptedException
	{
		//Boolean tickMark = CommonFunctions.VerifyTickMarkPresent(activeSideMenuItem);
		Boolean tickMark = CommonFunctions.VerifyProgressIndicator(PatDeclMenu);
		return tickMark;
	}
	
	public boolean clickOnSubmitButton() throws InterruptedException
	{
		boolean flag = false;
/*		scrolltoElement(driver, signatureCanvasbox);		
		enterSignatory(signatureCanvasbox, driver);*/
		OPHelpers.enterPatDecSignatoryDetails(driver);
		
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(7000);
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn));

		if (modalResultPopUp.isDisplayed())
		{
			flag = true;
		}

		return flag;
	}

	public void submitAction() throws InterruptedException
	{

		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(3000);


	}


	public OPHomePage clickCloseOnResultPopup()
	{
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn)).click();
		return new OPHomePage(driver);
	}

	public String getMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText)).getText();

		return msg;
	}

	public OPHomePage clickOnCloseButton() throws InterruptedException
	{
		scrolltoElement(driver, closeButton);
		wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
		Thread.sleep(2000);
		return new OPHomePage(driver);
	}

	public String getClaimNumber(String key)
	{
		
		wait.until(ExpectedConditions.elementToBeClickable(patientCheckBox));
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPGOS4TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}

	public void clickOnCancelClaimButton() throws InterruptedException
	{
		scrolltoElement(driver, cancelClaimButton);
		wait.until(ExpectedConditions.elementToBeClickable(cancelClaimButton)).click();
		Thread.sleep(2000);
	}

	public String getCancelPopupMessage()
	{
		String msg = null;
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupBody));
		msg = modalCancelPopupBody.getText();

		return msg;

	}

	public OPContractorDeclaration clickOnNoButton() throws InterruptedException
	{
		scrolltoElement(driver, modalCancelPopupNoBtn);
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupNoBtn)).click();
		System.out.println("The Click on No Button Action is successful");
		Thread.sleep(2000);
		return new OPContractorDeclaration(driver);
	}

	public OPHomePage clickOnYesButton() throws InterruptedException
	{
		scrolltoElement(driver, modalCancelPopupYesBtn);
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupYesBtn)).click();
		System.out.println("The Click on Yes Button Action is successful");
		Thread.sleep(2000);
		return new OPHomePage(driver);
	}

	public boolean checkClaimNoLength(String clmNo)
	{
		boolean fl = false;
		int len = clmNo.length();
		if (len == 8)
		{
			fl = true;
		}

		return fl;
	}

	public boolean checkalphanum(String clmNo)
	{
		boolean flg = true;
		char ch;
		char ch2;
		String clmChar = StringUtils.substring(clmNo, 0, 3);
		System.out.println("The First 3 characters from Claim: "+clmChar);

		String clmNum = StringUtils.substring(clmNo, 3,9);
		System.out.println("The last 5 characters from Claim: "+clmNum);

		for (int i = 0; i < clmChar.length(); i++)
		{
			ch = clmChar.charAt(i);

			if (!Character.isLetter(ch))
			{
				flg = false;
				break;

			}
		}  


		for (int j = 0; j < clmNum.length(); j++)
		{
			ch2 = clmNum.charAt(j);

			if (!Character.isDigit(ch2))
			{
				flg = false;
				break;

			}
		}  


		return flg;
	}

	public boolean checkClaimNumberIncremented(String clmNo, String newClmNo)
	{
		boolean flg = false;

		//String clmChar = StringUtils.substring(clmNo, 0, 3);
		//String newClmChar = StringUtils.substring(newClmNo, 0, 3);
		//	System.out.println("The First 3 characters from Claim: "+clmChar);

		String clmNum = StringUtils.substring(clmNo, 3,9);
		int clmNumber = Integer.parseInt(clmNum);
		
		String newClmNum = StringUtils.substring(newClmNo, 3,9);
		int newClmNumber = Integer.parseInt(newClmNum);
		//	System.out.println("The last 5 characters from Claim: "+clmNum);
		
		if (!(clmNumber==99999))
		{
			clmNumber++;
			if (clmNumber == newClmNumber)
		{
			flg = true;
		}
		}
		
		else
		{
			if(newClmNumber == 00001)
			{
				flg = true;
			}
		}

		return flg;
	}
	
	public List<String> getErrors()
	{
		
		List<String> ActErrors = AcutalErrormessage();
		return ActErrors;
	}
	
	public void GOS4ErrorPatientDeclaration(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, IsDistanceChkBox);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
				
	}
	
	public boolean PatientDeclarationDetailsEntered(String colName, String file, String sheet) throws InterruptedException, IOException
	{
		Boolean subFlag = false;
		Thread.sleep(1000);
		OPHelpers.selectProvidedOptions(colName, file, sheet, driver);
		enterSigntatoryDetails();
		subFlag = clickOnSubmitButton();
		Thread.sleep(1000);
		
		return subFlag;
	}
	
	public String getClaimNumber(String key,String file)
	{
		String clmNo= OPHelpers.getClaimNumber(key, file,driver,claimNumberHeader);
		return clmNo;
	}
	
	public void enterSigntatoryDetails() throws InterruptedException
	{
		String declarationName = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTELEGIBILITYDETAILS", "DeclarationName", 1);
		wait.until(ExpectedConditions.elementToBeClickable(declarationNameTxt)).click();
		declarationNameTxt.clear();
		declarationNameTxt.sendKeys(declarationName);
		enterAddressManually(1);

		
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
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).sendKeys(addressline1);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).sendKeys(addressline2);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).sendKeys(addressline3);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).sendKeys(addresscity);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCounty)).sendKeys(addresscounty);
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).sendKeys(addresspostcode);
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

	
}
