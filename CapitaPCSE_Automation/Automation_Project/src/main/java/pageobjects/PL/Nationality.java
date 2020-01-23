package pageobjects.PL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class Nationality extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(name="CountryOfBirthCode")
	WebElement Country_birth;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(name="PhotoIdentificationDocument")
	WebElement PhotoUploadbutton;	
	
/*	@FindBy(xpath="//*[@id='divPassportImage']/div/div[2]/div[1]/label/span")
	WebElement PhotoUploadbutton;*/
	
	@FindBy(name="HandwrittenSignatureDocument")
	WebElement SignatureuploadButton;
	
/*	@FindBy(xpath="//*[@id='divSignatureImage']/div/div[1]/label/span")
	WebElement SignatureuploadButton;*/
	
	@FindBy(xpath = "//*[@id='tblUploadedPhotoIdentificationDocument']//tbody")
	WebElement Uploadtable;
		
	@FindBy(xpath = "//*[@id='tblUploadedHandwrittenSignatureDocument']//tbody")
	WebElement UploadtableSignature;
	
	@FindBy(xpath = "(//*[@id='HasEvidenceOfEntitilementToWorkInUK'])[1]")
	WebElement EEANO_Radio;
	
	@FindBy(id = "EvidenceOfEntitlementToWorkInUK")
	WebElement BrowseEEANO_Radio;
	
	@FindBy(xpath = "//*[@id='tblUploadedEvidenceOfEntitlement']//tbody")
	WebElement UploadtableEEA;
	
	
	
	
	
	String CountryBirth = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","Nationality","Country", 1);
	String DeselectCountryBirth = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","Nationality","Country", 2);
	
	
	public Nationality(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public Nationality selectbirth() throws InterruptedException {

		try
		{
			System.out.println(CountryBirth);
			wait.until(ExpectedConditions.elementToBeClickable(Country_birth));
		scrolltoElement(driver, Country_birth);
		Select dropdown = new Select(Country_birth);
		dropdown.selectByVisibleText(CountryBirth);	
		helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Proper data is not filled on Nationality tab " +e);
		}	
		return new Nationality(driver);
	}
	
	public Nationality selectbirth_EEA() throws InterruptedException, IOException {

		try
		{
		System.out.println(CountryBirth);
		wait.until(ExpectedConditions.elementToBeClickable(Country_birth));
		scrolltoElement(driver, Country_birth);
		Select dropdown = new Select(Country_birth);
		dropdown.selectByVisibleText(CountryBirth);	
		helpers.CommonFunctions.ClickOnRadioButton("No", driver);
		wait.until(ExpectedConditions.elementToBeClickable(EEANO_Radio)).click();
		List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
		for(String Extension : Extensions)
		{
		scrolltoElement(driver, BrowseEEANO_Radio);
		JavascriptExecutor jse = (JavascriptExecutor)driver; 
		jse.executeScript("arguments[0].scrollIntoView();", BrowseEEANO_Radio);
	//	jse.executeScript("arguments[0].click();", BrowseEEANO_Radio);
		String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
		//helpers.CommonFunctions.Uploadfile(filePath, driver);
		BrowseEEANO_Radio.sendKeys(filePath);
		ClickOnupload();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		
		
		
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Proper data is not filled on Nationality tab " +e);
		}	
		return new Nationality(driver);
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			scrolltoElement(driver, Save_Submit);
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton" +e);
		}	
		return new CreateNewApp(driver);
	}

	public Nationality uploadpassport() throws InterruptedException, IOException {
		try{
			scrolltoElement(driver, PhotoUploadbutton);
			
	/*	wait.until(ExpectedConditions.elementToBeClickable(PhotoUploadbutton)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(PhotoUploadbutton)).clear();
		Thread.sleep(3000);
		String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
		System.out.println(filePath);
		Runtime.getRuntime().exec(filePath);
		String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";		
		helpers.CommonFunctions.Uploadfile(filePath, driver);*/

			Thread.sleep(2000);
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
			scrolltoElement(driver, PhotoUploadbutton);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", PhotoUploadbutton);
			jse.executeScript("arguments[0].click();", PhotoUploadbutton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			PhotoUploadbutton.sendKeys(filePath);
			ClickOnupload();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			}

			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The photo upload button is not clicked " +e);
		}	
		return new Nationality(driver);
	}

	public Nationality uploadSignature() throws InterruptedException, IOException {
		try{
			scrolltoElement(driver, SignatureuploadButton);
		/*	wait.until(ExpectedConditions.elementToBeClickable(SignatureuploadButton)).click();
		//	wait.until(ExpectedConditions.elementToBeClickable(SignatureuploadButton)).clear();
			Thread.sleep(3000);
			String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePath);
			Runtime.getRuntime().exec(filePath);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";		
			
			helpers.CommonFunctions.Uploadfile(filePath, driver);*/
			
			Thread.sleep(2000);
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
			scrolltoElement(driver, SignatureuploadButton);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", SignatureuploadButton);
			jse.executeScript("arguments[0].click();", SignatureuploadButton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			SignatureuploadButton.sendKeys(filePath);
			ClickOnupload();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			}

			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Signature upload button is not clicked " +e);
			}	
			return new Nationality(driver);
	}

	public Nationality ClickOnupload() throws InterruptedException {
		try{
			
		 List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
         System.out.println(Uploadbuttons.size());
         for (WebElement Uploadbutton:Uploadbuttons)
         {
         
         	scrolltoElement(driver,Uploadbutton);
         	if(Uploadbutton.isDisplayed())
         	{
         		Uploadbutton.click();
         		Thread.sleep(3000);
         		//helpers.CommonFunctions.PageLoadExternalwait(driver);
         	}
         }
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Signature upload button is not clicked " +e);
		}	
		return new Nationality(driver);
	}
	public  List<String> AcutalErrormessage() 
	{
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error']"));
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
	public  List<String> AcutalErrormessageFirstRefereeaddress(List<String> ArrMessage) throws InterruptedException
	{
		List<WebElement> ActualErrorMesageList = null;
		
		
		try 
		{
		
			 Thread.sleep(5000);
			 ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error']"));
			System.out.println(ActualErrorMesageList);
			
			 for (WebElement ActualErrorMesage:ActualErrorMesageList)
			 {
				 scrolltoElement(driver, ActualErrorMesage);
				 String ActErr = ActualErrorMesage.getText();
				 if(!(ActErr.equalsIgnoreCase("")) && (!(ArrMessage.contains(ActErr))))
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
	
	public List<String> ExpectedErrorMessage(String Sheet ,int Position) throws IOException {
		List<String> ExpectedErrorMessageList = null;
		try{
		  	 ExpectedErrorMessageList = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", Sheet, Position);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		// TODO Auto-generated method stub
		return ExpectedErrorMessageList;
	}

	public Nationality deselectBirth() {
		try{
		System.out.println(CountryBirth);
		scrolltoElement(driver, Country_birth);
		Select dropdown = new Select(Country_birth);
		dropdown.selectByVisibleText(DeselectCountryBirth);	
	//	helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Proper data is not filled on Nationality tab " +e);
		}	
		return new Nationality(driver);
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Country_birth);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public Boolean verifyCountIdentification(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = Uploadtable.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}
				
		return Uploadcount;
	}
	
	public Boolean verifyCountSignature(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableSignature.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}
				
		return Uploadcount;
	}
	
	public Boolean verifyCountEEA(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = UploadtableEEA.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured for EEA upload functionality");
		}
				
		return Uploadcount;
	}

	
}
