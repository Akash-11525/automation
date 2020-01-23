package pageobjects.PL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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



public class NewAppPersonalDetail extends Support{
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="TitleCode")
	WebElement Title;
	
	@FindBy(id="FirstName")
	WebElement FirstName;
	
	@FindBy(id="Surname")
	WebElement SurName;
	
	@FindBy(id="PreviousSurname")
	WebElement PreviousSurname;
	
	@FindBy(xpath="//*[@id='DivContainer']/form/div[6]/div/div")
	WebElement NoteOnPersonalDetails;
	
	@FindBy(name="DateOfBirth")
	WebElement DOB;
	
	@FindBy(id="NationalInsuranceNumber")
	WebElement NationInsuranceNo;
	
	@FindBy(id="TelephoneNumber")
	WebElement TelephoneNo;
	
	@FindBy(id="AlternateTelephoneNumber")
	WebElement AltTelephoneNo;
	
/*	@FindBy(xpath="//button[@data-book-id='postcodeRecAddress']")
	WebElement Resaddress;*/
/*	
	@FindBy(xpath="//div[@id='divPersonalDetailsSection']/div[7]/div[2]/button")
	WebElement Resaddress;*/
	
/*	@FindBy(xpath="//*[@id='divPersonalDetailsSection']//div[6]//div[2]/button")
	WebElement Resaddress;
	
	@FindBy(xpath="//div[@id='divPersonalDetailsSection']/div[9]/div[2]/button")
	WebElement GMCaddress;*/
	
	@FindBy(xpath="//button[@data-modal-title='RESIDENTIAL ADDRESS']")
	WebElement Resaddress;
	
	@FindBy(xpath="//button[@data-modal-title='GMC/GDC/GOC Address']")
	WebElement GMCaddress;
	
	
/*	@FindBy(xpath="//div[@id='divPersonalDetailsSection']/div[10]/div[2]/button")
	WebElement GMCaddress;*/
	
/*	@FindBy(xpath="//button[@data-book-id='postcodeProfessionalCouncilRecordAddress']")
	WebElement GMCaddress;*/
	
	@FindBy(id="HasNotConsentedToShareNHSDetails")
	WebElement AgreeTick;	

	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(xpath="//*[@class='modal fade']/div/div/div[3]/div/div/input")
	WebElement Close_Address;
	
	@FindBy(xpath="//div[@class='col-xs-12 col-sm-6']/div/div[2]/label/input")
	WebElement FemaleGender;
	

	
	String Title_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "TITLE");
	String FirstName_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "FIRST NAME");
	String Surname_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "SURNAME");
	String Prev_Surname_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "PREVIOUS SURNAME");
	String DOB_PatinentDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "DATE OF BIRTH");	
	String InsuranceNo_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "NATIONAL INSURANCE NUMBER");
	String Telephone_PatientDetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "CONTACT TELEPHONE NUMBER");
	String AltnarateTelephone_Patientdetail = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "ALTERNATE TELEPHONE NUMBER");

	
	public NewAppPersonalDetail(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	
	


	public NewAppPersonalDetail EnterPatientDetail() throws InterruptedException {
		try{

			
		//	String FirstName_portal = performer_PerformerPortal.split(" ")[0];
		//	String Surname_portal = performer_PerformerPortal.split(" ")[1];
			helpers.CommonFunctions.PageLoadExternalwait(driver); 
		//	wait.until(ExpectedConditions.elementToBeClickable(Title));
			Select dropdown = new Select(Title);
			dropdown.selectByVisibleText(Title_PatientDetail);
	/*		wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).sendKeys(FirstName_portal);			
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).sendKeys(Surname_portal);*/
			wait.until(ExpectedConditions.elementToBeClickable(PreviousSurname)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(PreviousSurname)).sendKeys(Prev_Surname_PatientDetail);
		//	Thread.sleep(4000);
			scrolltoElement(driver, DOB);
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).sendKeys(DOB_PatinentDetail);
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).sendKeys(Keys.TAB);
			helpers.CommonFunctions.ClickOnRadioButton("Female", driver);
		/*	scrolltoElement(driver, FemaleGender);
			wait.until(ExpectedConditions.elementToBeClickable(FemaleGender)).click();*/
			scrolltoElement(driver, NationInsuranceNo);
			wait.until(ExpectedConditions.elementToBeClickable(NationInsuranceNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NationInsuranceNo)).sendKeys(InsuranceNo_PatientDetail);
			scrolltoElement(driver, TelephoneNo);
			wait.until(ExpectedConditions.elementToBeClickable(TelephoneNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TelephoneNo)).sendKeys(Telephone_PatientDetail);
			/*wait.until(ExpectedConditions.elementToBeClickable(AltTelephoneNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AltTelephoneNo)).sendKeys(AltnarateTelephone_Patientdetail);*/
			
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Address Pop up box" +e);
		}	
		return new NewAppPersonalDetail(driver);
	}


	public EnterAddressManually ClickonResidentalAdd() throws InterruptedException {
		try{
			//Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(Resaddress)).click();
		Thread.sleep(1000);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident address manually button is not found " +e);
		}	
		return new EnterAddressManually(driver);
	}


	public EnterAddressManually clickOnGMCaddress() throws InterruptedException {
		try{
			scrolltoElement(driver, GMCaddress);
			wait.until(ExpectedConditions.elementToBeClickable(GMCaddress)).click();
			Thread.sleep(1000);
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Resident address manually button is not found " +e);
			}	
			return new EnterAddressManually(driver);
	}


	public CreateNewApp clickOnAgreeandSave() throws InterruptedException {
		try{	
			Thread.sleep(2000);
			scrolltoElement(driver, AgreeTick);
			if(!AgreeTick.isSelected())
			{
			wait.until(ExpectedConditions.elementToBeClickable(AgreeTick)).click();
			}
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			//ProcessorPLHelpers.PageLoadExternalwait();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		
				
		}
			catch(NoSuchElementException e)
			{
				System.out.println("The Resident address manually button is not found " +e);
			}	
			return new CreateNewApp(driver);
	}




	public NewAppPersonalDetail KeepBlankonallfield() {
		try{		
			Select dropdown = new Select(Title);
			dropdown.selectByValue("");
			
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();	
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
			scrolltoElement(driver, PreviousSurname);
			wait.until(ExpectedConditions.elementToBeClickable(PreviousSurname)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).clear();
			scrolltoElement(driver, NationInsuranceNo);
			wait.until(ExpectedConditions.elementToBeClickable(NationInsuranceNo)).clear();
			scrolltoElement(driver, TelephoneNo);
			wait.until(ExpectedConditions.elementToBeClickable(TelephoneNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AltTelephoneNo)).clear();
					
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Address Pop up box" +e);
		}	
		return new NewAppPersonalDetail(driver);
	}




	public NewAppPersonalDetail KeepBlankAddress() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Resaddress)).click();
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}	
		return new NewAppPersonalDetail(driver);
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
	
	public  List<String> AcutalErrormessageOnRes(List<String> ArrMessage) throws InterruptedException
	{
		List<WebElement> ActualErrorMesageList = null;
		
		
		try 
		{
			 scrolltoElement(driver, Resaddress); 
			 wait.until(ExpectedConditions.elementToBeClickable(Resaddress)).click();
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
			 
			 Actions action = new Actions(driver);
				action.moveToElement(Close_Address);
				action.doubleClick().build().perform();
			// wait.until(ExpectedConditions.elementToBeClickable(Close_Address)).click();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		return ArrMessage;
		
	}


	public int VerifyErrorMessage(List<String> ExpectedList,List<String> ActualList ) throws IOException {
		int count = 0;
		try{
			
			for(int i=0;i<ActualList.size();i++)
			{
				if(ActualList.contains(ExpectedList.get(i)))
				{
					System.out.println("Exist : "+ExpectedList.get(i));
				}
				else 
				{
					count = 1;
					System.out.println("Does not Exist : "+ExpectedList.get(i));
				}
			}
			
	    /*   List<WebElement> ActualErrorMesageList=driver.findElements(By.xpath("//*[@class='field-validation-error']"));
	       
	   	List<String> ExpectedErrorMessageList = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Expected Error", 1);
           System.out.println("total Actual Error Message "+ActualErrorMesageList.size());
           for (WebElement ActualErrorMesage:ActualErrorMesageList)
           {
        	   scrolltoElement(driver, ActualErrorMesage);
        	   String ActErr = ActualErrorMesage.getText();
        	   System.out.println("The Actula Message PPP "+ActErr);
               for (String ExpectedErrorMessage:ExpectedErrorMessageList)
                {
            	   if(ActErr.equalsIgnoreCase(ExpectedErrorMessage))
            	   {
            		   Count = 1;
            		   
            	   }
            	   else 
            	   {
            		   System.out.println("This is incorrect message" +ActErr);
            	   }
            	   
               }
        	   
           }*/
			
			
           
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident Address is not keep as blank " +e);
		}
		// TODO Auto-generated method stub
		return count;
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
	
	public String EnterPatientDetail_InvalidDOB(int position) 
	{
		String Invalid_DOB = null;
		try{

			Invalid_DOB = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PersonalDetail", "DATE OF BIRTH",position);	
			
		/*	String FirstName_portal = performer_PerformerPortal.split(" ")[0];
			String Surname_portal = performer_PerformerPortal.split(" ")[1];	*/	
			
			Select dropdown = new Select(Title);
			dropdown.selectByVisibleText(Title_PatientDetail);
		/*	wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FirstName)).sendKeys(FirstName_portal);			
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(SurName)).sendKeys(Surname_portal);*/
			wait.until(ExpectedConditions.elementToBeClickable(PreviousSurname)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(PreviousSurname)).sendKeys(Prev_Surname_PatientDetail);
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).sendKeys(Invalid_DOB);
			wait.until(ExpectedConditions.elementToBeClickable(DOB)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(NationInsuranceNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(NationInsuranceNo)).sendKeys(InsuranceNo_PatientDetail);
			wait.until(ExpectedConditions.elementToBeClickable(TelephoneNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TelephoneNo)).sendKeys(Telephone_PatientDetail);
			/*wait.until(ExpectedConditions.elementToBeClickable(AltTelephoneNo)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AltTelephoneNo)).sendKeys(AltnarateTelephone_Patientdetail);*/
			
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Address Pop up box" +e);
		}	
		return Invalid_DOB;
	
	}


	
	




	public String addDOBon_ExpectedError(String ExpectedErrormessage1, String invalid_DOB)
	{
		String FinalExpected_InvalidError = null;
		
		try{
			String FinalinvalidDOB = String.format("'"+invalid_DOB+"'" );
			System.out.println(FinalinvalidDOB);
			String FirstPart = ExpectedErrormessage1.split("is")[0];
			String SecondPart = ExpectedErrormessage1.split("is")[1];
		
			 FinalExpected_InvalidError = String.format(""+FirstPart+"%s is"+SecondPart+"",FinalinvalidDOB );
			 System.out.println(FinalExpected_InvalidError);
		}
		// TODO Auto-generated method stub
		catch(NoSuchElementException e)
		{
			System.out.println("The invalid DOB is not added Sucessfully " +e);
		}	
		return FinalExpected_InvalidError;
	
	}




	public NewAppPersonalDetail clickonSave() throws InterruptedException {
		try{
			scrolltoElement(driver, Save_Submit);
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton" +e);
		}	
		return new NewAppPersonalDetail(driver);
	}




	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Title);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

}
	




	
	
	


