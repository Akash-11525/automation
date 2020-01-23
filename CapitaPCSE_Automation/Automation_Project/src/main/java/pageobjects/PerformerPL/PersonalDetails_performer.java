package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mysql.jdbc.SocketMetadata.Helper;

import helpers.Screenshot;
import helpers.Support;
public class PersonalDetails_performer extends Support{
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="btnChangeName")
	WebElement ChangenameButton;
	
	@FindBy(id="NewTitleCode")
	WebElement TitleCode;
	
	@FindBy(id="NewFirstName")
	WebElement FirstName;
	
	@FindBy(id="NewSurname")
	WebElement Surname;
	
	@FindBy(name="Submit_Decision")
	WebElement Submit;
	
	@FindBy(xpath="//div[@class='popover confirmation fade top in']")
	WebElement Popupmessage;
	
	@FindBy(xpath="//a[@class='btn btn-danger']")
	WebElement Cancel_Submit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;

	@FindBy(id="btnChangeTelephoneNumber")
	WebElement ChangeTelephoneNumber;
	
	@FindBy(id="NewTelephoneNumber")
	WebElement NewNumber;
	
	@FindBy(id="NewAlternateTelephoneNumber")
	WebElement AlternateNumber;
	
	@FindBy(id="TelephoneNumber")
	WebElement Telephone_Personal;
	
	@FindBy(id="AlternateTelephoneNumber")
	WebElement AltTelephone_Personal;
	
	@FindBy(id="SaveChangeTelephoneNumber")
	WebElement SubmitOnTelephone;
	
	@FindBy(xpath="//*[@id='SaveChangeAddress']")
	WebElement SubmitOnChangeAdd;
	
	@FindBy(id="btnChangeAddress")
	WebElement ChangeAddress;	

	@FindBy(xpath="//div[@class='radio ipad-radio-btn-margin']")
	WebElement SelectGMC;
	
	@FindBy(xpath="//button[@data-modal-title='GMC/GDC/GOC Address']")
	WebElement AddAddressManually;
	
	@FindBy(name="NewProfessionalCouncilRecordAddress.AddressLine1")
	WebElement Address1;
	
	@FindBy(name="NewProfessionalCouncilRecordAddress.City")
	WebElement City;
	
	@FindBy(name="NewProfessionalCouncilRecordAddress.Postcode")
	WebElement PostalCode;
	
	@FindBy(xpath="//div[@id='divProfessionalCouncilRecordAddress']//div[2]//div[1]//div[3]//div[1]//div[1]//div[3]//div[1]//div[1]//button[1]")
	WebElement SaveButton_GMC;
	
	@FindBy(id="FirstName")
	WebElement FirstName_personal;
	
	
	@FindBy(xpath="//div[@id='divPersonalDetailsSection']//div[9]//div[3]//span")
	WebElement UpdatedAddress_Personal;                                                                                                                                                               ;
	
	
	
	String Title_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "TITLE");
	String FirstName_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "FIRST NAME");
	String Surname_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "SURNAME");
	String Title_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "Change Title");
	String FirstName_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "Change ForeName");
	String Surname_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "Change Surname");
	String LabelTitle_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "LabelTiltle");
	String LabelFirstName_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "LabelFirstName");
	String LabelSurname_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "LabelSurname");
	String UpdateNewNumber_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdateNewTelephone");
	String UpdateAltNumber_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdateNewAltTelephone");
	String UpdateAddress1_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdateAddress1");
	String UpdateCity_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdateCity");
	String UpdatePostalcode_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdatePostalCode");
	String UpdateCountry_PatientDetail_Perfomer = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerList.xlsx", "PersonalDetail", "UpdateCountry");
	
	public PersonalDetails_performer(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public PersonalDetails_performer ClickOnChangeName() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(ChangenameButton));
			scrolltoElement(driver, ChangenameButton);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ChangenameButton);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Change name is not clicked");
		}
		return new PersonalDetails_performer(driver);
	}

	public PersonalDetails_performer ChangeName() {
		try{
			scrolltoElement(driver, TitleCode);
			Select dropdown = new Select(TitleCode);
			dropdown.selectByVisibleText(Title_PatientDetail_Perfomer);
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();	
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).sendKeys(FirstName_PatientDetail_Perfomer);	
			wait.until(ExpectedConditions.elementToBeClickable(Surname)).clear();	
			wait.until(ExpectedConditions.elementToBeClickable(Surname)).sendKeys(Surname_PatientDetail_Perfomer);
		
		}
		catch(Exception e)
		{
			System.out.println("The information is not filled for Change Name");
		}
		return new PersonalDetails_performer(driver) ;
	}

	public boolean VerifyCorrectLabel() {
		Boolean CorrectLabel_Performer = false;
		
		List<WebElement> rowElements = driver.findElements(By.xpath("//form[@id='frmChangeName']//div[@class='col-xs-12 col-sm-6']//label"));
		int records = rowElements.size();
		System.out.println(records);
		if(records>0)
		{
			for(WebElement rowElement:rowElements)
			{
				String LabelName = rowElement.getText();
				if((LabelName.equalsIgnoreCase(LabelTitle_Perfomer))||(LabelName.equalsIgnoreCase(LabelFirstName_PatientDetail_Perfomer))||(LabelName.equalsIgnoreCase(LabelSurname_PatientDetail_Perfomer)))
				{
					CorrectLabel_Performer = true;
				}
			}
		}
		
		return CorrectLabel_Performer;
	}

	public boolean VerifyCorrectValue() {
		Boolean CorrectValue_Performer = false;
		
		List<WebElement> rowElements = driver.findElements(By.xpath("//form[@id='frmChangeName']//div[@class='col-xs-12 col-sm-6']"));
		int records = rowElements.size();
		System.out.println(records);
		if(records>0)
		{
			for(WebElement rowElement:rowElements)
			{
				String value = rowElement.getText();
				if(value != null && !value.isEmpty())
				{
					if((value.equalsIgnoreCase(LabelTitle_Perfomer))||(value.equalsIgnoreCase(LabelFirstName_PatientDetail_Perfomer))||(value.equalsIgnoreCase(LabelSurname_PatientDetail_Perfomer)))
					{
					CorrectValue_Performer = true;
					}
				}
			}
		}
		
		return CorrectValue_Performer;
		
		
		
	}

	public PersonalDetails_performer clickonSubmit() {
		try{
			scrolltoElement(driver, Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Submit);
	    	actions1.doubleClick().build().perform();
	    //	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Submit button is not clicked");
		}
		return new PersonalDetails_performer(driver) ;
	}

	public boolean verifypopupmessage() {
		boolean popupmessage = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//div[@class='popover confirmation fade top in']")).size() != 0;
			System.out.println(ispresent);
			//scrolltoElement(driver, Popupmessage);
			if(ispresent)
			{
				popupmessage = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("The Pop up message is not captured");
		}
		return popupmessage;
	}

	public PersonalDetails_performer clickonCancel_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Cancel_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Cancel_Submit);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new PersonalDetails_performer (driver);
	}
	
	public PersonalDetails_performer clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new PersonalDetails_performer (driver);
	}

	public PersonalDetails_performer ClickChangeTelephone() {
		try{
			scrolltoElement(driver, ChangeTelephoneNumber);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ChangeTelephoneNumber);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Click on change Telephone number is not clicked");
		}
		return new PersonalDetails_performer(driver);
	}

	public PersonalDetails_performer updateNewTelephonenumber() {
		try{
			scrolltoElement(driver, NewNumber);
			wait.until(ExpectedConditions.elementToBeClickable(NewNumber));
			wait.until(ExpectedConditions.elementToBeClickable(NewNumber)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NewNumber)).sendKeys(UpdateNewNumber_PatientDetail_Perfomer);
			wait.until(ExpectedConditions.elementToBeClickable(AlternateNumber)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AlternateNumber)).sendKeys(UpdateAltNumber_PatientDetail_Perfomer);
		
		}
		catch(Exception e)
		{
			System.out.println("The change telephone number is not happed ");
		}
		return new PersonalDetails_performer(driver);
	}

	public boolean verifyTelephonenumber() {
		boolean UpdateTelephonenumber = false;
		try{
			String updatedTelephone_personal = Telephone_Personal.getAttribute("value");
			String altTelephone_personal = AltTelephone_Personal.getAttribute("value");
			if(updatedTelephone_personal.equalsIgnoreCase(UpdateNewNumber_PatientDetail_Perfomer))
			{
				if(altTelephone_personal.equalsIgnoreCase(UpdateAltNumber_PatientDetail_Perfomer))
						{
							UpdateTelephonenumber = true;
						}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Updated Telephone is not captured");
		}
		return UpdateTelephonenumber;
	}

	public PersonalDetails_performer clickonSubmit_Telephone() {
		try{
			scrolltoElement(driver, ChangeTelephoneNumber);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(SubmitOnTelephone);
	    	actions1.doubleClick().build().perform();
	    	
		}
		catch(Exception e)
		{
			System.out.println("The submit on Change Telephone is not clicked");
		}
		return new PersonalDetails_performer(driver);
	}

	public PersonalDetails_performer ChangeAddress() {
		try{
			scrolltoElement(driver, ChangeTelephoneNumber);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ChangeAddress);
	    	actions1.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Change address is not clicked");
		}
		return new PersonalDetails_performer(driver);
	}

	public PersonalDetails_performer SelectGMC() {
		try{
			scrolltoElement(driver, SelectGMC);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(SelectGMC);
	    	actions1.click().build().perform();
	    	Thread.sleep(3000);
			
		}
		catch(Exception e)
		{
			System.out.println("The GMC option is not selected"); 
			
		}
		return new PersonalDetails_performer(driver);
	}

	public PersonalDetails_performer updateaddressManually() {
		try{
			scrolltoElement(driver, AddAddressManually);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(AddAddressManually);
	    	actions1.doubleClick().build().perform();
	    	wait.until(ExpectedConditions.elementToBeClickable(Address1));
			if(Address1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).clear();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).sendKeys(UpdateAddress1_PatientDetail_Perfomer);
				wait.until(ExpectedConditions.elementToBeClickable(City)).clear();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.elementToBeClickable(City)).sendKeys(UpdateCity_PatientDetail_Perfomer);
				wait.until(ExpectedConditions.elementToBeClickable(PostalCode)).clear();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.elementToBeClickable(PostalCode)).sendKeys(UpdatePostalcode_PatientDetail_Perfomer);
				Actions actions2 = new Actions(driver);
		    	actions2.moveToElement(SaveButton_GMC);
		    	actions2.doubleClick().build().perform();
				
			}
	    	 
			
		}
		catch(Exception e)
		{
			System.out.println("The Add address manually is not done on update address");
		}
		return new PersonalDetails_performer(driver);
	}

	public boolean verifyupdatedaddress() {
		boolean updatedaddress = false;
		try{
			scrolltoElement(driver, UpdatedAddress_Personal);
			String modifyaddress = UpdatedAddress_Personal.getText();
			String[] fields = modifyaddress.split("[,]");
			String Address1_personal = fields[0];
			String City_personal = fields[1];
			String Country_personal = fields[2];
			String PostalCode_personal = fields[3];
			if(Address1_personal.equalsIgnoreCase(UpdateAddress1_PatientDetail_Perfomer))
			{
				if(City_personal.equalsIgnoreCase(UpdateCity_PatientDetail_Perfomer))
				{
					if(Country_personal.equalsIgnoreCase(UpdateCountry_PatientDetail_Perfomer))
					{
						if(PostalCode_personal.equalsIgnoreCase(UpdatePostalcode_PatientDetail_Perfomer))
						{
							updatedaddress = true;
						}
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Correct updated address is not captured");
			
		}
		return updatedaddress;
	}

	public PersonalDetails_performer clickonSubmit_Address() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, SubmitOnChangeAdd);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(SubmitOnChangeAdd);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Submit on Change address is not clicked");
		}
		return new PersonalDetails_performer(driver) ;
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, FirstName_personal);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

	
}

