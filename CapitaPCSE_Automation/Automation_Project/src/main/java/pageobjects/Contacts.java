package pageobjects;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import helpers.WindowHandleSupport;
	

public class Contacts extends Support{
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (id="scr_title_i")
	WebElement Title;
	
	@FindBy (id="scr_nhs_number")
	WebElement nhsNumber;
	
	@FindBy (id="scr_nhs_number_i")
	WebElement nhsNumberField;
	
	@FindBy (id="firstname_d")
	WebElement firstName;
	
	@FindBy (id="firstname_i")
	WebElement firstNameField;
	
	@FindBy (id="lastname")
	WebElement lastName;
	
	@FindBy (id="lastname_i")
	WebElement lastNameField;
	
	@FindBy (id="birthdate_d")
	WebElement birthDate;
	
	@FindBy (id="DateInput")
	WebElement birthDateField;
	
	@FindBy (id = "address1_line1")
	WebElement addressLine1;
	
	@FindBy (id = "address1_line1_i")
	WebElement addressLine1Input;
	
	@FindBy (id = "address1_line2")
	WebElement addressLine2;
	
	@FindBy (id = "address1_line2_i")
	WebElement addressLine2Input;
	
	@FindBy (id = "address1_line3")
	WebElement addressLine3;
	
	@FindBy (id = "address1_line3_i")
	WebElement addressLine3Input;
	
	@FindBy (id = "address1_stateorprovince")
	WebElement addressLine4;
	
	@FindBy (id = "address1_stateorprovince_i")
	WebElement addressLine4Input;
	
	@FindBy (id = "address1_country")
	WebElement addressLine5;
	
	@FindBy (id = "address1_country_i")
	WebElement addressLine5Input;
		
	@FindBy (id="address1_postalcode")
	WebElement postCode;
	
	@FindBy (id="address1_postalcode_i")
	WebElement postCodeField;
	
	@FindBy (id="scr_sex")
	WebElement sex;
	
	@FindBy (id="scr_sex_i")
	WebElement sexField;
	
	@FindBy (id="scr_gppractice")
	WebElement gpPractice;
	
	@FindBy (id="scr_gppractice_ledit")
	WebElement gpPracticeField;
	
	@FindBy (id="scr_gppractice_i")
	WebElement gpPracticeSelector;
	
	@FindBy (id ="commandContainer2")
	WebElement ribbonMenu;
	
	@FindBy (css="li[id*='contact.Save']")
	WebElement contactSave;
	
	@FindBy(css="li[id*='contact.RunWorkflow']")
	WebElement contactRunWorkflow;
	
	@FindBy (id="FormTitle")
	WebElement formTitle;
	
	@FindBy (id="formselectorcontainer")
	WebElement formSelectorContainer;
	
	@FindBy (xpath="//*[@id='formSelectorDropdown']")
	WebElement formSelctorDropdown;
	
	@FindBy (xpath="//div[@id='Dialog_0']//ul/li[1]/a[2]")
	WebElement selectPatient;
	
	@FindBy (id="scr_newregistrationdate1")
	WebElement newRegDate;
	
	//@FindBy (css="input[id='DateInput']")
	@FindBy (xpath="//table[@id='scr_newregistrationdate1_i']//td/input[@id='DateInput']")
	WebElement newRegDateField;
	
	@FindBy(id="scr_newregistrationflag1")
	WebElement newRegFlag;
	
	@FindBy(css="div[jawsreadlable*='scr_newregistrationflag1_c']")
	WebElement newRegFlagState;
	
	
	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;
	
	@FindBy(xpath="//table[@id='gridBodyTable']//td[3]")
	WebElement firstRecord;
	
	@FindBy(xpath="//table[@id='gridBodyTable']")
	WebElement gridTable;
	
	@FindBy(xpath="//table[@ologicalname='scr_screening']")
	WebElement scrScreening;
	
/*	@FindBy(xpath="//table[@id='screeningrecordsgrid_gridBar']")
	WebElement scrScreening;*/
	
	@FindBy(css = "li[id*='contact.NewRecord']")
	WebElement contactNewRecord;
	
	@FindBy(id="Tab1")
	WebElement dyCRM;
	
	@FindBy(id="Settings")
	WebElement settings;
	
	@FindBy(id="address1_postalcode")
	WebElement Addressfield;
	
	@FindBy(xpath="//*[@id='address1_postalcode']/div[1]/span")
	WebElement Address;
	
	@FindBy(id="birthdate")
	WebElement BirthdateField;
	
	@FindBy(xpath="//*[@id='birthdate']/div[1]/span")
	WebElement Birthdate;
	
	@FindBy(tagName="iframe")
	List<WebElement> iframes;
	
	@FindBy(xpath="//*[@id='FormTitle']/h1")
	WebElement InlineNhsnoH1;
	
	@FindBy(id="telephone1")
	WebElement TelephoneField_PL;
	
	@FindBy(xpath="//*[@id='telephone1']//span/a")
	WebElement TelephoneText_PL;

	@FindBy(id="pcs_ninumber")
	WebElement NINumberfield_PL;
	
	@FindBy(xpath="//*[@id='pcs_ninumber_i']")
	WebElement NINumberText_PL;
	
	@FindBy(id="birthdate")
	WebElement DOBfield_PL;
	
	@FindBy(xpath="//*[@id='birthdate']/div/span")
	WebElement DOBText_PL;
	
	
	@FindBy(id="advancedFindImage")
	WebElement advancedFind;
	
	@FindBy(id="slctPrimaryEntity")
	WebElement primaryEntity;	
	
	@FindBy(xpath="//div[@id='fullname']")
	WebElement FullNamefield_PL;
	
	@FindBy(xpath="//div[@id='fullname']/div/span")
	WebElement FullNameText_PL;
	
	@FindBy(xpath="//*[@id='Tab1']/a/span[2]/img")
	WebElement CRMLogoICON;
	
	@FindBy(id="SFA")
	WebElement SalesButton;
	
	
	@FindBy(id="contact|NoRelationship|Form|Mscrm.Form.contact.SaveAndClose")
	WebElement Save_Close;
	
	@FindBy(xpath="//*[@class='ms-crm-Inline-Validation']")
	WebElement ErrMessage;
	
	@FindBy(xpath="//table[@id='gridBodyTable']//td[4]/div")
	WebElement TestDuedateonScreeningRecord;
	
	@FindBy(xpath="//table[@id='gridBodyTable']//td[2]/nobr")
	WebElement ScreeningTypeOnScreeningRecord;
	
	@FindBy(xpath="//table[@id='gridBodyTable']//td[5]/div")
	WebElement statusOnScreeningRecord;
	
	@FindBy(id="scr_birthdateestimated")
	WebElement BirthEstimatedfield;
	
	@FindBy(xpath="//div[@id='scr_birthdateestimated']/div/span")
	WebElement BirthEstimated;
	
	@FindBy(xpath="//*[@id='crmGrid_gridBodyTable_checkBox_Image_All']")
	WebElement CheckallResult;
	
	@FindBy(xpath="//*[@id='contact|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.contact.DeleteMenu']")
	WebElement DeleteButton;
	
	@FindBy(xpath="//button[@id='cmdDialogDelete']")
	WebElement Delete;	
	
	@FindBy(xpath="//button[@id='butBegin']")
	WebElement Confirmfordelete;
	
	@FindBy(xpath="//div[@jawsreadlabel='fullname_c']")
	WebElement FullNameField;
	
	@FindBy(xpath="//div[@jawsreadlabel='fullname_c']/span")
	WebElement FullName;
	
	@FindBy(id="fullname_compositionLinkControl_firstname_i")
	WebElement FirstNamePL;
	
	@FindBy(id="fullname_compositionLinkControl_lastname_i")
	WebElement LastNamePL;
	
	@FindBy(xpath="//*[@id='fullname_compositionLinkControl_flyoutLoadingArea-confirm']/div/div/span")
	WebElement DonePL;
		
	@FindBy(id="emailaddress1")
	WebElement EmailaddressField;
	
	@FindBy(xpath="//*[@id='emailaddress1_i']")
	WebElement Emailaddress;
	
	@FindBy(id="contact|NoRelationship|Form|Mscrm.Form.contact.SaveAndClose")
	WebElement Save_closePL;
	
	//@FindBy (xpath="//*[@id='contact|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.contact.NewRecord']")
	@FindBy(css="li[id*='contact.NewRecord']")
	WebElement NewPLContanct;
	
	//@FindBy (xpath="//li[@id='contact|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.contact.NewRecord']")
	@FindBy(xpath = "//*[@id='contact|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.contact.NewRecord']/span/a/span")
	WebElement NewPLContanctfield;
	
	@FindBy (xpath="//*[@id='crmTopBar']")
	WebElement NewPLContanctContainer;
	
	

	

	String DOB_CRMPL = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PersonalDetail", "DATE OF BIRTH",1);
//	String Telephone_CRMPL = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PersonalDetail", "CONTACT TELEPHONE NUMBER",1);
	String NINumber_CRMPL = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PersonalDetail", "NATIONAL INSURANCE NUMBER",1);
	
	
	
	
	public Contacts(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}
	
	public Contacts clickOnContactNewRecord() throws InterruptedException{

		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOf(contactNewRecord));
		wait.until(ExpectedConditions.elementToBeClickable(contactNewRecord)).click();



		return new Contacts(driver);

	}

	
	public CrmHome EnterContactDetails(String NHSNo, int position) throws IOException, InterruptedException
	{
		//driver.switchTo().frame("contentIFrame1");
		
		// Suraj G : Comment this block because this field is removed
/*		switchToRequiredIframe(nhsNumberField);
		wait.until(ExpectedConditions.elementToBeClickable(nhsNumberField));
		nhsNumberField.sendKeys(NHSNo);*/
		
		
		
		//wait.until(ExpectedConditions.elementToBeClickable(formSelectorContainer)).click();
	/*	wait.until(ExpectedConditions.elementToBeClickable(formSelctorDropdown)).click();
		//formSelctorDropdown.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(selectPatient)).click();*/
		
		//String NHSNo = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "NHSNo");
		//String NHSNo =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "NHSNo");
		//wait.until(ExpectedConditions.elementToBeClickable(nhsNumber));
	//	wait.until(ExpectedConditions.elementToBeClickable(nhsNumber)).click();
		
		String TitlePatient = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Title", position);
		switchToRequiredIframe(Title);
		wait.until(ExpectedConditions.elementToBeClickable(Title));
		Title.sendKeys(TitlePatient);
		
		//String Fname = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Forename");
		String Fname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Forename", position);
		//String Forename = CommonFunctions.CommonFunc.generateTS(Fname);
		//String Forename = helper..generateTS(Fname);
		String Forename = helpers.CommonFunctions.generateTS(Fname);
		firstName.click();
		firstNameField.sendKeys(Forename);
		
		//String Surname = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Surname");
		String Surname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Surname",position);
		lastName.click();
		lastNameField.sendKeys(Surname);
		
		//String Bdate = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Birthdate");
		String Age = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Age",position);
		//String Bdate = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Birthdate");
		String date = CommonFunctions.getDOB(Age);
		birthDate.click();
		System.out.println(date);
	//	birthDate.clear();
		//birthDateField.sendKeys("01/01/1980");
		birthDateField.sendKeys(date);
		
	/*	wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).click();
		String EstimatedValue =wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).getText();
		if(EstimatedValue.equalsIgnoreCase("NO"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).click();
		}*/
		
		
		//String Sex = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Sex");
		String Sex = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Sex",position);
		sex.click();
		Select sex = new Select(sexField);
		sex.selectByVisibleText(Sex);
		
		String AddressLine1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine1",position);
		addressLine1.click();
		addressLine1Input.sendKeys(AddressLine1);
		
		String AddressLine2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine2",position);
		addressLine2.click();
		addressLine2Input.sendKeys(AddressLine2);
		
		String AddressLine3 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine3",position);
		addressLine3.click();
		addressLine3Input.sendKeys(AddressLine3);
		
		String AddressLine4 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine4",position);
		addressLine4.click();
		addressLine4Input.sendKeys(AddressLine4);
		
		String AddressLine5 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine5",position);
		addressLine5.click();
		addressLine5Input.sendKeys(AddressLine5);
		
		//String Postcode = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Postcode");
		String Postcode = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Postcode",position);
		postCode.click();
		postCodeField.sendKeys(Postcode);
		
		
		//String GP = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "GPPractice");
		String GP = testdata.ConfigurationData.GP;
		gpPractice.click();
		gpPracticeField.sendKeys(GP);
		gpPracticeSelector.click();
		
		driver.findElement(By.xpath("//a[@title=\""+GP+"\"]")).click();
		

/*		String Regdate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Regdate",position);
		wait.until(ExpectedConditions.elementToBeClickable(newRegDate)).click();
		
		String todayDate = CommonFunctions.getDate();
		newRegDateField.sendKeys(todayDate);
		
		
		//wait.until(ExpectedConditions.elementToBeClickable(formTitle)).click();
		
	
		newRegFlag.click();*/
		//wait.until(ExpectedConditions.elementToBeClickable(newRegFlagState)).click();
		
		driver.switchTo().defaultContent();
		System.out.println("Switched backed to top window.");
		
		
		//wait.until(ExpectedConditions.elementToBeClickable(ribbonMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(contactSave)).click();
		//contactSave.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(contactRunWorkflow));
		
				
		return new CrmHome(driver);
	}
	
	
	public CrmHome EnterContactDetails_Duplicate(String NHSNo, int position) throws IOException, InterruptedException
	{
		//driver.switchTo().frame("contentIFrame1");
		
		switchToRequiredIframe(nhsNumberField);
	//	wait.until(ExpectedConditions.elementToBeClickable(nhsNumber)).click();
		wait.until(ExpectedConditions.elementToBeClickable(nhsNumberField));
		nhsNumberField.sendKeys(NHSNo);
		String Fname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Forename", position);
		firstName.click();
		firstNameField.sendKeys(Fname);
		
		//String Surname = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Surname");
		String Surname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Surname",position);
		lastName.click();
		lastNameField.sendKeys(Surname);
		
		String Age = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Age",position);
		String date = CommonFunctions.getDOB(Age);
		birthDate.click();
		System.out.println(date);
		birthDateField.sendKeys(date);
		
		wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).click();
		String EstimatedValue =wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).getText();
		if(EstimatedValue.equalsIgnoreCase("NO"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(BirthEstimatedfield)).click();
		}
		
		
		//String Sex = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Sex");
		String Sex = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Sex",position);
		sex.click();
		Select sex = new Select(sexField);
		sex.selectByVisibleText(Sex);
		
		String AddressLine1 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine1",position);
		addressLine1.click();
		addressLine1Input.sendKeys(AddressLine1);
		
		String AddressLine2 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine2",position);
		addressLine2.click();
		addressLine2Input.sendKeys(AddressLine2);
		
		String AddressLine3 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine3",position);
		addressLine3.click();
		addressLine3Input.sendKeys(AddressLine3);
		
		String AddressLine4 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine4",position);
		addressLine4.click();
		addressLine4Input.sendKeys(AddressLine4);
		
		String AddressLine5 = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "AddressLine5",position);
		addressLine5.click();
		addressLine5Input.sendKeys(AddressLine5);
		
		//String Postcode = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "Postcode");
		String Postcode = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Postcode",position);
		postCode.click();
		postCodeField.sendKeys(Postcode);
		
		
		//String GP = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Contact", "GPPractice");
		String GP = testdata.ConfigurationData.GP;
		gpPractice.click();
		gpPracticeField.sendKeys(GP);
		gpPracticeSelector.click();
		
		driver.findElement(By.xpath("//a[@title=\""+GP+"\"]")).click();
		

		String Regdate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Regdate",position);
		wait.until(ExpectedConditions.elementToBeClickable(newRegDate)).click();
		
		String todayDate = CommonFunctions.getDate();
		newRegDateField.sendKeys(todayDate);
		
		
		//wait.until(ExpectedConditions.elementToBeClickable(formTitle)).click();
		
	
		newRegFlag.click();
		//wait.until(ExpectedConditions.elementToBeClickable(newRegFlagState)).click();
		
		driver.switchTo().defaultContent();
		System.out.println("Switched backed to top window.");
		
		
		//wait.until(ExpectedConditions.elementToBeClickable(ribbonMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(contactSave)).click();
		//contactSave.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(contactRunWorkflow));
		
				
		return new CrmHome(driver);
	}
	
	
	public Contacts searchAndSelectPatient(String nhsNumber) throws InterruptedException {
		
		driver.switchTo().frame("contentIFrame0");
		srcCriTxt.clear();
		
		srcCriTxt.sendKeys(nhsNumber);
		Thread.sleep(2000);
		srcCriIcon.click();
		
		Thread.sleep(2000);
		Actions action = new Actions(driver);
		action.doubleClick(firstRecord).perform(); 
		
		/*wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		
		firstRecord.click();*/
		
		
		return new Contacts(driver);
	}
	
	public Contacts searchPatient(String PatientName) throws InterruptedException {
		
		driver.switchTo().frame("contentIFrame0");
		srcCriTxt.clear();
		
		srcCriTxt.sendKeys(PatientName);
		srcCriIcon.click();
	
		/*wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		
		firstRecord.click();*/
		
		
		return new Contacts(driver);
	}
	
	
public int getScreeningRecords() throws InterruptedException, IOException {
	
		int records = 0;
		switchToFrame(nhsNumber, driver);
		try {
			//System.out.println(driver.findElements(By.xpath("//table[@ologicalname='scr_screening']")).size());
			boolean ispresent = driver.findElements(By.xpath("//table[@ologicalname='scr_screening']")).size() != 0;
			System.out.println(ispresent);
			if(ispresent)
			{

		List<WebElement> trCount = (List<WebElement>) scrScreening.findElements(By.tagName("tr"));
		records = trCount.size() -1 ;
		System.out.println("Record found in screening is "+records);
			}
			else
			{
				records = 0;
			}
		
		}
		catch (NoSuchElementException e){
			System.out.println("No table found.");
			
			
		}
		driver.switchTo().defaultContent();
		return records;
		
	}

public ContactPatient getScreeningRecords1() throws InterruptedException {
	
	Thread.sleep(2000);
	//driver.navigate().refresh();
	
	int records = 0;	
	driver.switchTo().frame("contentIFrame1");
	try {

	List<WebElement> trCount = (List<WebElement>) scrScreening.findElements(By.tagName("tr"));
	records = trCount.size();
	System.out.println(records);
	
	System.out.println("Table found first .");
	if (records > 1){
		System.out.println(firstRecord.getText());
		Actions action = new Actions(driver);
		action.doubleClick(firstRecord).perform();

		Thread.sleep(2000);
		//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='header_crmFormSelector']"))));
	// wait.until(ExpectedConditions.elementToBeClickable(firstRecord)).click();
	// firstRecord.click();
	 //firstRecord1.click();	
	 
	// driver.findElement(By.partialLinkText("")).click();


	}


	}
	catch (NoSuchElementException e){
		System.out.println("No table found first.");


	}
	driver.switchTo().defaultContent();
	return new ContactPatient(driver);
	
}



public CrmHome clickSettings(){

	Actions actions = new Actions(driver);
	actions.moveToElement(dyCRM);
	actions.click().build().perform();

	wait.until(ExpectedConditions.visibilityOf(settings));
	wait.until(ExpectedConditions.elementToBeClickable(settings)).click();


	return new CrmHome(driver);
}

public Contacts getDOBandAddress() {

		driver.switchTo().frame("contentIFrame1");
		
		try {
					
			wait.until(ExpectedConditions.elementToBeClickable(Addressfield));
			String Addressonscreening = wait.until(ExpectedConditions.elementToBeClickable(Address)).getText();
			System.out.println(Addressonscreening);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "Address",Addressonscreening);
			
			wait.until(ExpectedConditions.elementToBeClickable(BirthdateField));
			String DOB = wait.until(ExpectedConditions.elementToBeClickable(Birthdate)).getText();
			System.out.println(DOB);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "DateofBirth",DOB);
			
			
			
		}
		catch (NoSuchElementException e)
			{
			System.out.println("The patient data is not stored properly."+e);
			}
	driver.switchTo().defaultContent();
		return new Contacts (driver); 
	}



	public void switchToRequiredIframe(WebElement ele){
	       int iframeCount = 0;
	       String iframeName = null;
	       int count = 0;
	       String iframeName1 = null;  
	       
	       iframeCount = iframes.size();
	       System.out.println("Count of frames: "+iframeCount);
	       //iframeCount = iframeCount-1;
	       try{
	              List<WebElement> frames = driver.findElements(By.xpath("//iframe[@title='Content Area']"));
	              Iterator<WebElement> iter = frames.iterator();
	           
	               
	       for(WebElement frame:frames)
	             // while(iter.hasNext())
	              {
	            	//  WebElement we = iter.next();
	            	 System.out.println(frame);
	            	  iframeName1 = frame.getAttribute("name");
	            	  System.out.println(iframeName1); 
	                           driver.switchTo().frame(iframeName1);
	                           try
	                           {
	                        		                        	   
	                           if (ele.isDisplayed())
	                           {
	                               System.out.println("We have switched into iframes : " +iframeName1);
	                        	   break;                     
	                           }
	                           }
	                           
	                           catch (Exception e)
	                           {
	                                  System.out.println("The element does not found under iframe: contentIFrame");
	                           } 
	                           driver.switchTo().defaultContent(); 
	                     }              
	              }                  
	                     catch (Exception e)
	                     {
	                           System.out.println("The element does not found under iframe: contentIFrame"+iframeCount+"");
	                           
	                     }
	             
	              
	       }

	public String getNHSnumberwithspace() {
		// TODO Auto-generated method stub
		String InlineNHSno = null;
		try{
			switchToRequiredIframe(nhsNumber);
			String ExpectedInlineNHSno = wait.until(ExpectedConditions.visibilityOf(InlineNhsnoH1)).getText();
			System.out.println(ExpectedInlineNHSno);
			String ExpInlineNHSno1 = ExpectedInlineNHSno.split("NHS No")[1].trim();
			InlineNHSno = ExpInlineNHSno1.replace(")", "").trim();
			System.out.println(InlineNHSno);
		//	InlineNHSno = InlineNHSno1.split(")")[0].trim();
			//System.out.println(InlineNHSno1);

		}
		  catch (Exception e)
        {
              System.out.println("The NHS number on Contanct si not getting properly");
              
        }
		driver.switchTo().defaultContent();
		return InlineNHSno ;
	}

	public boolean verifyCRMdata(String PerformerPortal , String Role) {
		boolean CRMdata = false;
		try{
			Thread.sleep(4000);
			//String Telephone_CRMPL = ExcelUtilities.getKeyValueByPosition("ConfigurationDetails.xlsx", "PLUSER", Role, "Phone Number");
			String Telephone_CRMPL = utilities.ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "PersonalDetail", "CONTACT TELEPHONE NUMBER");
			switchToFrame(TelephoneText_PL, driver);
		//	wait.until(ExpectedConditions.elementToBeClickable(TelephoneField_PL)).click();

			String Telephonenumber_PL = wait.until(ExpectedConditions.elementToBeClickable(TelephoneText_PL)).getText();
			System.out.println(Telephonenumber_PL);
		//	wait.until(ExpectedConditions.elementToBeClickable(FullNamefield_PL)).click();
		//	String FullName_PL = wait.until(ExpectedConditions.elementToBeClickable(FullNameText_PL)).getAttribute("title");
			String FullName_PL = wait.until(ExpectedConditions.elementToBeClickable(FullNameText_PL)).getText();
			System.out.println(FullName_PL);
			if(FullName_PL.equalsIgnoreCase(PerformerPortal))
			{		
			if(Telephonenumber_PL.equalsIgnoreCase(Telephone_CRMPL))
			{
				/*scrolltoElement(driver, NINumberfield_PL);
				wait.until(ExpectedConditions.elementToBeClickable(NINumberfield_PL)).click();
				String NINo_PL = wait.until(ExpectedConditions.elementToBeClickable(NINumberText_PL)).getAttribute("defaultvalue");
				System.out.println(NINo_PL);
				if(NINo_PL.equalsIgnoreCase(NINumber_CRMPL))
				{*/
					/*scrolltoElement(driver, DOBfield_PL);
					Thread.sleep(3000);
					wait.until(ExpectedConditions.elementToBeClickable(DOBfield_PL)).click();
					String DOB_PL = wait.until(ExpectedConditions.elementToBeClickable(DOBfield_PL)).getAttribute("title");
					System.out.println(DOB_PL);
					if(DOB_PL.equalsIgnoreCase(DOB_CRMPL))
					{*/
						CRMdata = true;
				//	}
					
				//}
			
			}
			}
		}
		 catch (Exception e)
        {
              System.out.println("The Element is not found on contanct page");
              
        }
		//driver.switchTo().defaultContent();
		return CRMdata ;
	}



	public CrmHome clickonCRM() {
		try{
			driver.switchTo().defaultContent();
			  Actions action = new Actions(driver);			  
		      action.moveToElement(CRMLogoICON).build().perform();		 
		      CRMLogoICON.click();
		      Actions action1 = new Actions(driver);			  
		      action1.moveToElement(SalesButton).build().perform();		 
		      SalesButton.click();
		
			
			
		}
		  catch (Exception e)
        {
              System.out.println("The sales button is not click at that moment");
              
        }
		return new CrmHome(driver);
	}

	public Contacts Enterforename(int position) {
		try {
			String Fname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Forename", position);
			String Forename = helpers.CommonFunctions.generateTS(Fname);
			firstName.click();
			firstNameField.sendKeys(Forename);
			
		}
		// TODO Auto-generated method stub
		  catch (Exception e)
	        {
	              System.out.println("The Forename field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}

	public Contacts DoubleClickOnSave_Close() {
		try {
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(Save_Close)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Save_Close)).click();
		}
	  catch (Exception e)
	        {
	              System.out.println("The Save and close button is not found");
	              
	        }
		  return new Contacts(driver);
	}

	public String geterrormessageonPatientpage() {
		String ErrorMessage = null;
		try {
			switchToFrame(firstName, driver);
			Thread.sleep(3000);
			 List<WebElement> Errmessages = driver.findElements(By.xpath("//*[@class='ms-crm-Inline-Validation']"));
			  for (WebElement Errmessage : Errmessages)
			  {		  
			  
			  String Stylevalue = Errmessage.getAttribute("style");
		       System.out.println(Stylevalue);
		       if (Stylevalue.contains("display: block;"))
		       {
		    	   ErrorMessage = Errmessage.getText();
		       }
		
		
			  }
			  }
		 catch (Exception e)
        {
              System.out.println("The error message is not found");
              
        }
	
	  return ErrorMessage;
	}

	public Contacts EnterSurname(int position) {
		try {
			
			String Surname = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Surname",position);
			lastName.click();
			lastNameField.sendKeys(Surname);
			
		}
		  catch (Exception e)
	        {
	              System.out.println("The Surname field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}
	
	public Contacts EnterDOB(int position) {
		try {
			Thread.sleep(3000);
		/*	String DOB = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Birthdate",position);
			System.out.println(DOB);*/
		//	String date = CommonFunctions.getDOB(Age);
		//	birthDate.click();
			//birthDate.clear();
		//	System.out.println(date);
			wait.until(ExpectedConditions.elementToBeClickable(birthDateField)).clear();
			birthDateField.sendKeys("07/02/1980");
			
		}
		  catch (Exception e)
	        {
	              System.out.println("The DOB field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}
	
	public Contacts EnterPostalcode(int position) {
		try {
			String Postcode = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Postcode",position);
			postCode.click();
			postCodeField.sendKeys(Postcode);
			
		}
		  catch (Exception e)
	        {
	              System.out.println("The Postal Code field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}
	
	public Contacts EnterSex(int position) {
		try {
			String Sex = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Sex",position);
			Thread.sleep(3000);
			sex.click();
			Select sex = new Select(sexField);
			sex.selectByVisibleText(Sex);
			
		}
		  catch (Exception e)
	        {
	              System.out.println("The Sex Code field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}

	public Contacts EnterNHSNo(String nhsNo) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(nhsNumber)).click();
			wait.until(ExpectedConditions.elementToBeClickable(nhsNumberField));
			nhsNumberField.sendKeys(nhsNo);
			
		}
		  catch (Exception e)
	        {
	              System.out.println("The NHS Code field is not captured by script");
	              
	        }
		  return new Contacts(driver);
	}

	public Boolean verifyNHSNO(String nhsNo) {
		boolean NHSNo = false;
		try{
			switchToFrame(nhsNumber, driver);
			wait.until(ExpectedConditions.elementToBeClickable(nhsNumber)).click();
			String ActualNHSNo =wait.until(ExpectedConditions.elementToBeClickable(nhsNumberField)).getText();
			if(ActualNHSNo.equalsIgnoreCase(nhsNo))
			{
				NHSNo = true;
			}
		}
		catch (Exception e)
        {
              System.out.println("The NHS Code field is not captured by script");
              
        }
		// TODO Auto-generated method stub
		return NHSNo;
	}

	public Boolean verifyTestDuedate() {
		boolean TestDuedate = false;
		try{
			switchToFrame(nhsNumber, driver);
		String TestDuedateScreening = 	wait.until(ExpectedConditions.elementToBeClickable(TestDuedateonScreeningRecord)).getText();
		System.out.println(TestDuedateScreening);
		String ExpectedTestDuedate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "TestDuedate",1);
		if(TestDuedateScreening.equalsIgnoreCase(ExpectedTestDuedate))
		{
			TestDuedate = true;
		}
		}
		
		catch (Exception e)
        {
              System.out.println("The Test Due date on screening is not getting ");
              
        }
		return TestDuedate;
	}

	public int verifyresultrecord() {
		int records = 0;
		try{
			switchToFrame(gridTable, driver);
			boolean ispresent = driver.findElements(By.xpath("//table[@id='gridBodyTable']")).size() != 0;
			System.out.println(ispresent);
			if(ispresent)
			{
				
		List<WebElement> trCount = (List<WebElement>) gridTable.findElements(By.tagName("tr"));
		records = trCount.size() -1 ;
		System.out.println("Record found in after search name is "+records);
			}
		}
		catch (Exception e)
        {
              System.out.println("The result of record is not found on contanct page");
              
        }
		driver.switchTo().defaultContent();
		return records;
	}

	public Contacts deleteallrecord() {
		
		try{
			switchToFrame(CheckallResult, driver);
			wait.until(ExpectedConditions.elementToBeClickable(CheckallResult)).click();
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(DeleteButton)).click();	
			Thread.sleep(3000);
			driver.switchTo().frame("InlineDialog_Iframe");
			wait.until(ExpectedConditions.elementToBeClickable(Delete)).click();	
			Thread.sleep(3000);
			driver.switchTo().frame("InlineDialog_Iframe1");
			wait.until(ExpectedConditions.elementToBeClickable(Confirmfordelete)).click();	
			driver.switchTo().defaultContent();
			
			
			
		}
		catch (Exception e)
        {
              System.out.println("The result of record is not delete on contanct page");
              
        }
	
		return new Contacts(driver);
	
	}

	public Boolean CalculatedTestDuedate(int Additionaldate) {
		boolean TestDuedate = false;
		try{
			switchToFrame(TestDuedateonScreeningRecord, driver);
		String TestDuedateScreening = 	wait.until(ExpectedConditions.elementToBeClickable(TestDuedateonScreeningRecord)).getText();
		System.out.println(TestDuedateScreening);
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + Additionaldate);
		System.out.println(cal.getTime());
		Date date = new Date();
		String ExpectedTestDuedate = new SimpleDateFormat("mm/dd/yyyy").format(cal.getTime());
		System.out.println(ExpectedTestDuedate);
		if(TestDuedateScreening.equalsIgnoreCase(ExpectedTestDuedate))
		{
			TestDuedate = true;
		}
		}
		
		catch (Exception e)
        {
              System.out.println("The Test Due date on screening is not getting ");
              
        }
		driver.switchTo().defaultContent();
		return TestDuedate;
		
	}

	public Boolean VerifyScreeningType(String ExpectedScreeningType) {
		boolean ScreeningType = false;
		try{
			switchToFrame(ScreeningTypeOnScreeningRecord, driver);
		String TestScreeningType = 	wait.until(ExpectedConditions.elementToBeClickable(ScreeningTypeOnScreeningRecord)).getText();

		if(TestScreeningType.equalsIgnoreCase(ExpectedScreeningType))
		{
			ScreeningType = true;
		}
		}
		
		catch (Exception e)
        {
              System.out.println("The Screening Type on screening is not getting ");
              
        }
		driver.switchTo().defaultContent();
		return ScreeningType;
		
	}

	public Boolean VerifyStatusOnScreening(String ExpectedStatus) {
		boolean Status = false;
		try{
			switchToFrame(statusOnScreeningRecord, driver);
		String TeststatusOnScreeningRecord = 	wait.until(ExpectedConditions.elementToBeClickable(statusOnScreeningRecord)).getText();

		if(TeststatusOnScreeningRecord.equalsIgnoreCase(ExpectedStatus))
		{
			Status = true;
		}
		}
		
		catch (Exception e)
        {
              System.out.println("The Screening Type on screening is not getting ");
              
        }
		driver.switchTo().defaultContent();
		return Status;
	}

	public String  createPLContanct() {
		String NewProcessMedical = null;
		try{
		
			String FirstNamePLRandom = null;
			String FirstRandomNumber = generateRandomString();
			switchToFrame(FirstNamePL, driver);
	/*		wait.until(ExpectedConditions.elementToBeClickable(FullNameField));	
			wait.until(ExpectedConditions.elementToBeClickable(FullNameField)).click();
			wait.until(ExpectedConditions.elementToBeClickable(FullName));	
			wait.until(ExpectedConditions.elementToBeClickable(FullName)).click();*/
			FirstNamePLRandom = "AutomationPL"+FirstRandomNumber;
			System.out.println(FirstNamePLRandom);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(FirstNamePL)).sendKeys(FirstNamePLRandom);
			wait.until(ExpectedConditions.elementToBeClickable(FirstNamePL)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(LastNamePL)).sendKeys("Medical");
			wait.until(ExpectedConditions.elementToBeClickable(DonePL)).click();
			NewProcessMedical = FirstNamePLRandom+" "+"Medical";
			scrolltoElement(driver, EmailaddressField);
			wait.until(ExpectedConditions.elementToBeClickable(EmailaddressField));
			wait.until(ExpectedConditions.elementToBeClickable(EmailaddressField)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Emailaddress)).sendKeys("TestAutomation@yahoo.com");
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(Save_closePL));
			wait.until(ExpectedConditions.elementToBeClickable(Save_closePL)).click();
			
		}
		catch(Exception e)
		{
			System.out.println("The New PL contanct is nit created");
		}
		return NewProcessMedical;
	}

	
	  public String generateRandomString()
	  {
		  String FirstRandomString = null;
		  int RANDOM_STRING_LENGTH = 3; 
		  String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        StringBuffer randStr = new StringBuffer();
	        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
	            int number = getRandomNumber();
	            char ch = CHAR_LIST.charAt(number);
	            randStr.append(ch);
	            FirstRandomString = randStr.toString();
	        }
	        return FirstRandomString;
	    }
	
	  
	   public int getRandomNumber() {
	        int randomInt = 0;
	        Random randomGenerator = new Random();
	        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
	        if (randomInt - 1 == -1) {
	            return randomInt;
	        } 
	        else {
	            return randomInt - 1;
	        }
	    }

		public Contacts clickOnNewPL() {
			try{
				//Thread.sleep(2000);
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			//	driver.switchTo().defaultContent();
			/*	wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctfield));	
				wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctfield)).click();
				wait.until(ExpectedConditions.elementToBeClickable(NewPLContanct)).click();*/
			//	wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctfield)).click();
			//	wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctContainer)).click();
				driver.navigate().refresh();
			
				Thread.sleep(2000);
				//wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctContainer));
				wait.until(ExpectedConditions.elementToBeClickable(NewPLContanct)).click();
			//	wait.until(ExpectedConditions.elementToBeClickable(NewPLContanctfield)).click();
				/*Actions action = new Actions(driver);
				action.moveToElement(NewPLContanctfield);
				action.click().build().perform();*/
			//	driver.switchTo().defaultContent();
				
				
			}
			catch(Exception e)
			{
				System.out.println("The New PL record is not clicked");
			}
			return new Contacts(driver) ;
		}

		public void screenshots(String note) throws InterruptedException, IOException {
			//scrolltoElement(driver, FullNameText_PL);
			
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			driver.switchTo().defaultContent();
			
		}

/*		public ContactPatient openScreeningRecord() throws InterruptedException {
			WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
			driver.switchTo().frame("contentIFrame0");
			WebElement screenDiv= driver.findElement(By.xpath("//div[@class='ms-crm-tabcolumn2']/div[1]"));
			scrolltoElement(driver, screenDiv);
			WebElement screenDiv2= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_ccDiv']"));
			wait.until(ExpectedConditions.elementToBeClickable(screenDiv2)).click();
			WebElement screenDiv3= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_compositeControl']"));
			wait.until(ExpectedConditions.elementToBeClickable(screenDiv3)).click();
			WebElement screenDiv4= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_crmGridTD']"));
			wait.until(ExpectedConditions.elementToBeClickable(screenDiv4)).click();
			WebElement screenDiv5= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid']"));
			wait.until(ExpectedConditions.elementToBeClickable(screenDiv5)).click();
			WebElement tableMain= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_divDataArea']//table[@id='gridBodyTable']"));
			wait.until(ExpectedConditions.elementToBeClickable(tableMain)).click();
			WebElement tableBody= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_divDataArea']//table[@id='gridBodyTable']//tbody"));
			wait.until(ExpectedConditions.elementToBeClickable(tableBody)).click();
			Actions action = new Actions(driver);
			action.doubleClick(firstRecord).perform();
			Thread.sleep(2000);			
			return new ContactPatient(driver);
		}*/
		
		public CallRecallRecord getActiveScreeningRecords() throws InterruptedException {
			WebElement element= null;
			Actions action = new Actions(driver);
			Thread.sleep(2000);
			int records=0;
			WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
			driver.switchTo().frame("contentIFrame1");
			try {
					WebElement tableMain= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_divDataArea']//table[@id='gridBodyTable']"));
					List<WebElement> trCount = (List<WebElement>) tableMain.findElements(By.tagName("tr"));
					records = trCount.size();
					System.out.println(records);
					
					System.out.println("Table found first .");
					if (records > 1){
						System.out.println(firstRecord.getText());
						action = new Actions(driver);
						action.doubleClick(firstRecord).perform();
						Thread.sleep(2000);
					
					element= driver.findElement(By.xpath("//div[@class='ms-crm-tabcolumn2']/div[1]"));
					scrolltoElement(driver, element);
					//,appGridQueryFilterContainer_screeningrecordsgrid
/*					element= driver.findElement(By.xpath("//div[@id='appGridQueryFilterContainer_CallRecallHistory']"));
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();*/
					element= driver.findElement(By.xpath("//div[@id='CallRecallHistory_d']"));
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					element= driver.findElement(By.xpath("//div[@id='titleContainer_CallRecallHistory']"));
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
/*					element= driver.findElement(By.xpath("//div[@id='appGridQueryFilterContainer_CallRecallHistory']/div[3]"));
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();*/
					element= driver.findElement(By.xpath("//div[@class='openAssociatedGridViewImageButton']/a/img"));
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					ScreeningInfo objScreeningInfo= new ScreeningInfo(driver);
					objScreeningInfo= objScreeningInfo.getActiveCaseTitle();
					
/*					WebElement tableMain= driver.findElement(By.xpath("//div[@id='screeningrecordsgrid_divDataArea']//table[@id='gridBodyTable']"));
					List<WebElement> trCount = (List<WebElement>) tableMain.findElements(By.tagName("tr"));
					records = trCount.size();
					System.out.println(records);
					
					System.out.println("Table found first .");
					if (records > 1){
						System.out.println(firstRecord.getText());
						Actions action = new Actions(driver);
						action.doubleClick(firstRecord).perform();
						Thread.sleep(2000);
					}*/
				}	

			}
			catch (NoSuchElementException e){
				System.out.println("No table found first.");
			}
			driver.switchTo().defaultContent();
			return new CallRecallRecord(driver);
			
		}

}
