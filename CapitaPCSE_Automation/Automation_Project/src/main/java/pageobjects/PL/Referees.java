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

public class Referees extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="FirstReferee_TitleCode")
	WebElement Title;
	
	@FindBy(id="Referees")
	WebElement RefereesTab;
	
	@FindBy(id="FirstReferee_FirstName")
	WebElement FirstName;
	
	@FindBy(id="FirstReferee_Surname")
	WebElement SurName;
	
	@FindBy(id="FirstReferee_TelephoneNumber")
	WebElement Telephone;
	
	@FindBy(id="FirstReferee_AlternativeTelephoneNumber")
	WebElement AltTelephone;
	
	@FindBy(id="FirstReferee_EmailAddress")
	WebElement EmailAddress;
	
	@FindBy(xpath="//button[@data-target='#Referee1AddressModal']")
	WebElement AddressManullay;	
	
	@FindBy(id="FirstReferee_CapacityInWhichKnown")
	WebElement Relationship;
	
	@FindBy(id="FirstReferee_LengthOfTimeKnown")
	WebElement LenghofTime;
	
	@FindBy(id="FirstReferee_RelatedJobRole")
	WebElement RelatedJobRole;
	
	@FindBy(id="FirstReferee_HasConsentedForContact")
	WebElement RefereeAgreeCheckbox_Ref1;
	
	@FindBy(id="SecondReferee_HasConsentedForContact")
	WebElement RefereeAgreeCheckbox_Ref2;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="SecondReferee_TitleCode")
	WebElement Ref2_Title;
	
	@FindBy(id="SecondReferee_FirstName")
	WebElement Ref2_Name;
	
	@FindBy(id="SecondReferee_Surname")
	WebElement Ref2_Surname;
	
	@FindBy(id="SecondReferee_TelephoneNumber")
	WebElement Ref2_Telephone;
	
	@FindBy(id="SecondReferee_AlternativeTelephoneNumber")
	WebElement Ref2_AltTelephone;
	
	@FindBy(id="SecondReferee_EmailAddress")
	WebElement Ref2_Email;	
	
	@FindBy(id="SecondReferee_CapacityInWhichKnown")
	WebElement Ref2_Relation;
	
	@FindBy(id="SecondReferee_LengthOfTimeKnown")
	WebElement Ref2_time;
	
	@FindBy(id="SecondReferee_RelatedJobRole")
	WebElement Ref2_Related;
	
	
	@FindBy(id="FirstReferee_IsClinicalPractitioner")
	WebElement FirstClinicalRadio;
	
	@FindBy(xpath="(//*[@id='SecondReferee_IsClinicalPractitioner'])[2]")
	WebElement SecondClinicalRadio;
	
/*	@FindBy(id="btnReferee1AddressModal1")
	WebElement FirstReferaddress;*/
	
/*	@FindBy(xpath="//*[@id='dvReferee1']/div/div/div[5]/div[2]/button")
	WebElement FirstReferaddress;
	
	@FindBy(xpath="//*[@id='dvReferee2']/div/div/div[5]/div[2]/button")
	WebElement SecondReferaddress;
	*/
	@FindBy(xpath="//button[@data-modal-title='Referee 1 Address']")
	WebElement FirstReferaddress;
	
	@FindBy(xpath="//button[@data-modal-title='Referee 2 Address']")
	WebElement SecondReferaddress;
	
	
	
/*	@FindBy(id="btnReferee1AddressModal2")
	WebElement SecondReferaddress;*/
	
	@FindBy(xpath="//*[@class='modal fade in']/div/div/div[3]/div/div/button")
	WebElement Close_Address;
	
	@FindBy(xpath="//*[@class='modal fade in']/div/div/div[3]/div/div/button")
	WebElement Close_Address_Ref1;
	
	@FindBy(name="SecondReferee.ReasonWhyNotClinicalPractioner")
	WebElement ReasonforNonClinical;
	
	
	
	
	
	
	
	String Title_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TITLE",1);
	String FirstName_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "FIRST NAME",1);	
	String Surname_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "SURNAME",1);	
	String Telephone_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TELEPHONE",1);	
	String Email_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "EMAILADDRESS",1);	
	String Relationship_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "RELATIONSHIP",1);	
	String Time_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TIME KNOWN",1);	
	String Relate_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "RELATE",1);	
	String Address_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "ADDRESS1",1);
	String City_Ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "CITY", 1);
	String Postalcode_ref1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "POSTALCODE", 1);
	
	String Title_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TITLE",2);
	String FirstName_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "FIRST NAME",2);	
	String Surname_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "SURNAME",2);	
	String Telephone_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TELEPHONE",2);	
	String Email_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "EMAILADDRESS",2);	
	String Relationship_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "RELATIONSHIP",2);	
	String Time_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "TIME KNOWN",2);	
	String Relate_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "RELATE",2);	
	String Address_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "ADDRESS1",2);
	String City_Ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "CITY",2);
	String Postalcode_ref2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Referees", "POSTALCODE",2);
	
	public Referees(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public Referees EnterReferee1() throws InterruptedException 
	{
		try {
			
			scrolltoElement(driver, Title);
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(Title));
			Select dropdown = new Select(Title);
			dropdown.selectByVisibleText(Title_Ref1);
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).sendKeys(FirstName_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).sendKeys(Surname_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(Telephone)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Telephone)).sendKeys(Telephone_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(EmailAddress)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(EmailAddress)).sendKeys(Email_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(Relationship)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Relationship)).sendKeys(Relationship_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(LenghofTime)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LenghofTime)).sendKeys(Time_Ref1);	
			wait.until(ExpectedConditions.elementToBeClickable(RelatedJobRole)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(RelatedJobRole)).sendKeys(Relate_Ref1);	
			
			
			
			}
		catch(NoSuchElementException e)
			{
				System.out.println("The element is not found on Referee" +e);
			}	
			return new Referees(driver);
	}

	public Referees clickonAgree_ref1() {
		
		try{
			
			scrolltoElement(driver, RefereeAgreeCheckbox_Ref1);
		
			wait.until(ExpectedConditions.elementToBeClickable(RefereeAgreeCheckbox_Ref1)).click();
			/*Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform(); */
			
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Save or check box" +e);
		}	
		return new Referees(driver);
	}

	public Referees clickonRefreree(String Text) throws InterruptedException {
	try{
		Thread.sleep(1000); 
	//	List<WebElement> RefereeName=driver.findElements(By.xpath("//*[@class='accordion-toggle']"));
		List<WebElement> RefereeName=driver.findElements(By.xpath("//*[@class='accordion-toggle remove-hyperlink']"));
		  System.out.println("total buttons "+RefereeName.size());
          for (WebElement Refname : RefereeName)
          {                   
        	   String RefereeName_Ref = Refname.getText();  
        	 
        	  if (RefereeName_Ref.equalsIgnoreCase(Text))
        	  {  
        		 scrolltoElement(driver, Refname);
        		Actions action = new Actions(driver);
      			action.moveToElement(Refname);
      			action.click().build().perform();
      			Thread.sleep(2000);
      		
        		break;
        		  
        	  }
          }
          
	}
	catch(NoSuchElementException e)
	{
		System.out.println("The element is not found on Refree 2" +e);
	}	
		return new Referees(driver);
	}

	public Referees EnterReferee2() throws InterruptedException 
	{
		try {
			
			scrolltoElement(driver, Ref2_Title);
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Title));
			Select dropdown = new Select(Ref2_Title);
			dropdown.selectByVisibleText(Title_Ref2);
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Name)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Name)).sendKeys(FirstName_Ref2);	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Surname)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Surname)).sendKeys(Surname_Ref2);	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Telephone)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Telephone)).sendKeys(Telephone_Ref2);	
		/*	wait.until(ExpectedConditions.elementToBeClickable(Ref2_AltTelephone)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_AltTelephone)).sendKeys(Email_Ref2);*/	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Email)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Email)).sendKeys(Email_Ref2);	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Relation)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Relation)).sendKeys(Relationship_Ref2);	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_time)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_time)).sendKeys(Time_Ref2);	
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Related)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Ref2_Related)).sendKeys(Relate_Ref2);	
			
			}
		catch(NoSuchElementException e)
			{
				System.out.println("The element is not found on Referee" +e);
			}	
			return new Referees(driver);
	}

	public Referees clickonRadio_Agree_ref2() {

		try{
			scrolltoElement(driver, SecondClinicalRadio);
			wait.until(ExpectedConditions.elementToBeClickable(SecondClinicalRadio)).click();
			wait.until(ExpectedConditions.elementToBeClickable(ReasonforNonClinical)).sendKeys("Automation Purpose");
			if(!RefereeAgreeCheckbox_Ref2.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(RefereeAgreeCheckbox_Ref2)).click();
			}
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Save or check box" +e);
		}	

		return new Referees(driver);
	}
	
	
	public Referees clickonRadio_Agree_ref1() {

		try{
			scrolltoElement(driver, FirstClinicalRadio);
			wait.until(ExpectedConditions.elementToBeClickable(FirstClinicalRadio)).click();
			if(!RefereeAgreeCheckbox_Ref1.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(RefereeAgreeCheckbox_Ref1)).click();
			}
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Save or check box" +e);
		}	
		return new Referees(driver);
	}
	
	
	
	public Referees ClickOnSave_Submit() throws InterruptedException
	{
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
		return new Referees(driver);
	}

	public EnterAddressManually clickOnReferaddress1() throws InterruptedException 
	{
		try
		{
		helpers.Support.PageLoadExternalwait(driver);
		scrolltoElement(driver, FirstReferaddress);
		wait.until(ExpectedConditions.elementToBeClickable(FirstReferaddress)).click();
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The First Referee address manually button is not found " +e);
		}	
		return new EnterAddressManually(driver);
	}

	public EnterAddressManually clickOnReferaddress2() throws InterruptedException {
		try
		{
		helpers.Support.PageLoadExternalwait(driver);
		scrolltoElement(driver, SecondReferaddress);
		wait.until(ExpectedConditions.elementToBeClickable(SecondReferaddress)).click();
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Second Referee address manually button is not found " +e);
		}	
		return new EnterAddressManually(driver);
	}

	public Referees clickonSecondRefreree(String Text) throws InterruptedException {
		try{
			  List<WebElement> RefereeName=driver.findElements(By.xpath("//*[@class='accordion-toggle remove-hyperlink collapsed']"));
			  System.out.println("total buttons "+RefereeName.size());
	          for (WebElement Refname : RefereeName)
	          {    
	        	 scrolltoElement(driver, Refname);
	        	   String RefereeName_Ref = Refname.getText();  
	        	 
	        	  if (RefereeName_Ref.equalsIgnoreCase(Text))
	        	  {        		 
	        		Actions action = new Actions(driver);
	      			action.moveToElement(Refname);
	      			action.click().build().perform();
	      			Thread.sleep(2000);
	        		break;
	        		  
	        	  }
	          }
	          
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Refree 2" +e);
		}	
			return new Referees(driver);
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
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}
	
	
	public  List<String> AcutalErrormessageOnRes(List<String> ArrMessage) throws InterruptedException
	{
		List<WebElement> ActualErrorMesageList = null;
		
		
		try 
		{
			 scrolltoElement(driver, SecondReferaddress); 
			 wait.until(ExpectedConditions.elementToBeClickable(SecondReferaddress)).click();
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
			 wait.until(ExpectedConditions.elementToBeClickable(Close_Address)).click();
			
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

	public Referees KeepblankasReferee1() throws InterruptedException {
try {
			
			scrolltoElement(driver, Title);
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(Title));
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(Telephone)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(EmailAddress)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(Relationship)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(LenghofTime)).clear();		
			wait.until(ExpectedConditions.elementToBeClickable(RelatedJobRole)).clear();		
			
			}
		catch(NoSuchElementException e)
			{
				System.out.println("The element is not found on Referee" +e);
			}	
			return new Referees(driver);
	}

	public Referees UncheckRadio_Agree_ref1() {
		try{
			scrolltoElement(driver, FirstClinicalRadio);
			wait.until(ExpectedConditions.elementToBeClickable(FirstClinicalRadio)).click();
			wait.until(ExpectedConditions.elementToBeClickable(RefereeAgreeCheckbox_Ref1)).click();
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Save or check box" +e);
		}	
		return new Referees(driver);
	}
	
	public  List<String> AcutalErrormessageFirstRefereeaddress(List<String> ArrMessage) throws InterruptedException
	{
		List<WebElement> ActualErrorMesageList = null;
		
		
		try 
		{
			 scrolltoElement(driver, FirstReferaddress); 
			 wait.until(ExpectedConditions.elementToBeClickable(FirstReferaddress)).click();
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
			 wait.until(ExpectedConditions.elementToBeClickable(Close_Address_Ref1)).click();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, RefereesTab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

}


