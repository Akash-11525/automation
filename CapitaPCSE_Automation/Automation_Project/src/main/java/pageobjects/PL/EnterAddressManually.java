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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class EnterAddressManually extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	
	
	
	
/*	@FindBy(xpath="//*[contains(@id,'_AddressLine1')]")
	WebElement Address1;
	
	@FindBy(xpath="//*[contains(@id,'_City')]")
	WebElement City;
	
	@FindBy(xpath="//*[contains(@id,'_Postcode')]")
	WebElement PostCode;*/
	
	@FindBy(id="ResidentialAddress_AddressLine1")
	WebElement Address1;
	
	@FindBy(id="ResidentialAddress_City")
	WebElement City;
	
	@FindBy(id="ResidentialAddress_Postcode")
	WebElement PostCode;
	
	@FindBy(id="//button[@class='btn btn-success']")
	WebElement Savebutton;
	
	@FindBy(id="ProfessionalCouncilRecordAddress_AddressLine1")
	WebElement GMCAddress1;
	
	@FindBy(id="ProfessionalCouncilRecordAddress_City")
	WebElement GMCCity;
	
	@FindBy(id="ProfessionalCouncilRecordAddress_Postcode")
	WebElement GMCPostCode;
	
	@FindBy(xpath="//*[@id='ResidentialAddressModal']/div/div/div[3]/div/div/button[1]")
	WebElement SaveonResidental;
	
	@FindBy(xpath="//*[@id='ProfessionalCouncilRecordAddressModal']/div/div/div[3]/div/div/button[1]")
	WebElement SaveonGMS;
	
	@FindBy(id="FirstReferee_Address_AddressLine1")
	WebElement RefAddress1;
	
	@FindBy(id="FirstReferee_Address_City")
	WebElement RefCity1;
	
	@FindBy(id="FirstReferee_Address_Postcode")
	WebElement RefPostCode1;	
	
/*	@FindBy(xpath="//*[@id='Referee1AddressModal']/div/div/div[3]/div/div/button[1]")
	WebElement SaveonReferee1;*/
	@FindBy(xpath="//*[@class='modal fade in']/div/div/div[3]/div/div/button")
	WebElement SaveonReferee1;
	
	
	
	@FindBy(id="SecondReferee_Address_AddressLine1")
	WebElement RefAddress2;
	
	@FindBy(id="SecondReferee_Address_City")
	WebElement RefCity2;
	
	@FindBy(id="SecondReferee_Address_Postcode")
	WebElement RefPostCode2;
	
	@FindBy(xpath="//*[@id='Referee2AddressModal']/div/div/div[3]/div/div/button[1]")
	WebElement SaveonReferee2;
	
	
	


	String Address1_Patientdetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "ADDRESS1");
	String CITY_Patientdetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "CITY");
	String PostCode_Patientdetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "POSTALCODE");
	
	String Address1_Referee = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Referees", "ADDRESS1");
	String CITY_Referee = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Referees", "CITY");
	String PostCode_Referee = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Referees", "POSTALCODE");
	
	
	public EnterAddressManually(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public NewAppPersonalDetail EnterResAddressManually() throws InterruptedException {
		
		try{
		
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(Address1));
			if(Address1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).sendKeys(Address1_Patientdetail);
				wait.until(ExpectedConditions.elementToBeClickable(City)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(City)).sendKeys(CITY_Patientdetail);
				wait.until(ExpectedConditions.elementToBeClickable(PostCode)).clear();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.elementToBeClickable(PostCode)).sendKeys(PostCode_Patientdetail);
				helpers.CommonFunctions.ClickOnButton("Save", driver);
				//wait.until(ExpectedConditions.elementToBeClickable(SaveonResidental)).click();
				
				
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Res Address Pop up box" +e);
		}
		
		return new NewAppPersonalDetail(driver);
	}

	public NewAppPersonalDetail EnterGMCAddressManually() throws InterruptedException {
		try{
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(GMCAddress1));
			if(GMCAddress1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(GMCAddress1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(GMCAddress1)).sendKeys(Address1_Patientdetail);
				wait.until(ExpectedConditions.elementToBeClickable(GMCCity)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(GMCCity)).sendKeys(CITY_Patientdetail);
				wait.until(ExpectedConditions.elementToBeClickable(GMCPostCode)).clear();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(GMCPostCode)).sendKeys(PostCode_Patientdetail);
				helpers.CommonFunctions.ClickOnButton("Save", driver);
				//wait.until(ExpectedConditions.elementToBeClickable(SaveonGMS)).click();
				
				//helpers.CommonFunctions.ClickOnButton("Save", driver);
				
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on GMC Address Pop up box" +e);
		}
		
		return new NewAppPersonalDetail(driver);
	}
	
	public Referees EnterFirstRefAddressManually() throws InterruptedException {
		try{
			Thread.sleep(1500);	
		
			wait.until(ExpectedConditions.elementToBeClickable(RefAddress1));
			if(RefAddress1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(RefAddress1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefAddress1)).sendKeys(Address1_Referee);
				wait.until(ExpectedConditions.elementToBeClickable(RefCity1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefCity1)).sendKeys(CITY_Referee);
				wait.until(ExpectedConditions.elementToBeClickable(RefPostCode1)).clear();
				Thread.sleep(500);
				wait.until(ExpectedConditions.elementToBeClickable(RefPostCode1)).sendKeys(PostCode_Referee);
		
				wait.until(ExpectedConditions.elementToBeClickable(SaveonReferee1)).click();
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on GMC Address Pop up box" +e);
		}
		
		return new Referees(driver);
	}

	public Referees EnterSecondRefAddressManually() throws InterruptedException {
		try{
			Thread.sleep(1500);	
		
			wait.until(ExpectedConditions.elementToBeClickable(RefAddress2));
			if(RefAddress2.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(RefAddress2)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefAddress2)).sendKeys(Address1_Referee);
				wait.until(ExpectedConditions.elementToBeClickable(RefCity2)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefCity2)).sendKeys(CITY_Referee);
				wait.until(ExpectedConditions.elementToBeClickable(RefPostCode2)).clear();
				Thread.sleep(500);
				wait.until(ExpectedConditions.elementToBeClickable(RefPostCode2)).sendKeys(PostCode_Referee);
				wait.until(ExpectedConditions.elementToBeClickable(SaveonReferee1)).click();
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Referee second Address Pop up box" +e);
		}
		
		return new Referees(driver) ;
	}

	public NewAppPersonalDetail KeepBlankResAddress() throws InterruptedException {
		try{
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(Address1));
			if(Address1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).clear();		
				wait.until(ExpectedConditions.elementToBeClickable(City)).clear();		
				wait.until(ExpectedConditions.elementToBeClickable(PostCode)).clear();
				helpers.CommonFunctions.ClickOnButton("Save", driver);
				//wait.until(ExpectedConditions.elementToBeClickable(SaveonResidental)).click();		
				
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Res Address Pop up box" +e);
		}
		
		return new NewAppPersonalDetail(driver);
	}

	public NewAppPersonalDetail KeepblankGMCaddress() {
		try{
			
			wait.until(ExpectedConditions.elementToBeClickable(GMCAddress1));
			if(GMCAddress1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(GMCAddress1)).clear();			
				wait.until(ExpectedConditions.elementToBeClickable(GMCCity)).clear();		
				wait.until(ExpectedConditions.elementToBeClickable(GMCPostCode)).clear();	
				helpers.CommonFunctions.ClickOnButton("Save", driver);
				//wait.until(ExpectedConditions.elementToBeClickable(SaveonGMS)).click();
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on GMC Address Pop up box" +e);
		}
		
		return new NewAppPersonalDetail(driver);
	}

	public Referees KeepBlankFirstRefAddressManually() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(RefAddress1));
			if(RefAddress1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(RefAddress1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefCity1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(RefPostCode1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(SaveonReferee1)).click();
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on GMC Address Pop up box" +e);
		}
		
		return new Referees(driver);
	}

	public List<String> getActualLabelList() {
		List<WebElement> ActualErrorMesageList = null;
		List<String> ArrMessage = null;
		
		try 
		{
			ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='col-xs-6 col-sm-6']/label"));
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

	public List<String> getExpectedList(String Sheet, int Position) throws IOException {
		List<String> ExpectedLabel_Address = null;
		try{
			ExpectedLabel_Address = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", Sheet, Position);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		// TODO Auto-generated method stub
		return ExpectedLabel_Address;
	}

	public int VerifyLabelonAddress(List<String> expectedLabel_Address, List<String> actualLabel_Address) {
		int count = 0;
		try{
			
			for(int i=0;i<actualLabel_Address.size();i++)
			{
				if(actualLabel_Address.contains(expectedLabel_Address.get(i)))
				{
					System.out.println("Exist : "+expectedLabel_Address.get(i));
				}
				else 
				{
					count = 1;
					System.out.println("Does not Exist : "+expectedLabel_Address.get(i));
				}
			}
			
			
           
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The label is not captured correctly  " +e);
		}
		catch(Exception e)
		{
			System.out.println("The label is not captured correctly  " +e);
		}
		// TODO Auto-generated method stub
		return count;
	}

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Address1);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	}
	
	
	
	

