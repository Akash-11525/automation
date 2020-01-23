package pageobjects;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

import helpers.Support;



public class ContactPatient extends Support {
	WebDriver driver;

	WebDriverWait wait;
	
	@FindBy (xpath="//table[@ologicalname='incident']//td[2]")
	WebElement RecallFirstRow;
	
	@FindBy(xpath = "//div[@id='scr_testdate']")
	WebElement TestDateField;	
	@FindBy(id = "DateInput")
	WebElement TestDate;
	
	@FindBy(id = "scr_source") 
	WebElement SourceField;	
	@FindBy(id = "scr_source_i")
	WebElement Source;
	
	@FindBy(id = "scr_lab") 
	WebElement LabField;	
	@FindBy(id = "scr_lab_ledit")
	WebElement Lab;
	
	@FindBy(id = "scr_slideno") 
	WebElement SlideNumberField;	
	@FindBy(id = "scr_slideno_i")
	WebElement SlideNumber;
	

	@FindBy(id = "scr_result") 
	WebElement ResultField;	
	@FindBy(id = "scr_result_i")
	WebElement Resultselect;
	
	@FindBy(id = "scr_infection") 
	WebElement InfectionField;	
	@FindBy(id = "scr_infection_i")
	WebElement Infection;
	
	@FindBy(id = "scr_action") 
	WebElement Actionfield;	
	@FindBy(id = "scr_action_i")
	WebElement Action;

	@FindBy(id = "scr_sendercode") 
	WebElement Sendercodefield;	
	@FindBy(id = "scr_sendercode_i")
	WebElement Sendercode;
	
	@FindBy(id = "scr_forenames") 
	WebElement Forenamefield;	
	@FindBy(id = "scr_forenames_i")
	WebElement Forename;
	
	@FindBy(id = "scr_surname") 
	WebElement Surnamefield;	
	@FindBy(id = "scr_surname_i")
	WebElement Surname;
	

	@FindBy(id = "scr_address") 
	WebElement Addressfield;	
	@FindBy(id = "scr_address_i")
	WebElement Address;
	
	@FindBy(xpath = "//*[@id='scr_cervicaltestresult|NoRelationship|Form|Mscrm.Form.scr_cervicaltestresult.Save']")		
	WebElement SaveButton;
	
	@FindBy(xpath = "//*[@id='scr_cervicaltestresult|NoRelationship|Form|Mscrm.Form.scr_cervicaltestresult.SaveAndClose']")		
	WebElement Save_CloseButton;
	
	
	@FindBy(xpath="//*[@id='DateInput']]")
	//@FindBy(xpath="//div[@id='scr_testdate']/div")
	WebElement NewTestdate;
	@FindBy(xpath="//div[@id='scr_testdate']")
	WebElement NewTestdatefield;
	
	@FindBy(xpath="//select[@id='scr_source_i']")
	WebElement Sourcedropdown;
	@FindBy(xpath="//div[@id='scr_source']")
	WebElement Sourcedropdownfield;
	

	@FindBy(xpath="//*[@id='header_crmFormSelector']/nobr/span")
	WebElement Openwindowclick;
	
	@FindBy(xpath="//table[@id='gridBodyTable']/tbody/tr[1]/td[3]")
	WebElement firstRecord1;
	
	@FindBy (xpath="//table[@class='ms-crm-List-Data']")
	WebElement Existingresult1;
	
	@FindBy (xpath="//*[@id='CervicalTestResultsGrid_addImageButtonImage']")
	WebElement AddingbuttonCervialResult;
	
	
	@FindBy (id="scr_patient")
	WebElement PatientFullNamefield;
	
	@FindBy(xpath ="//*[@id='scr_patient']/div[1]/span")
	WebElement PatientFullName;
	
	@FindBy (id="scr_testduedate")
	WebElement TestDuedatefield;
	
	@FindBy(xpath ="//*[@id='scr_testduedate']/div[1]/span")
	WebElement TestDuedate;
	
	@FindBy(id= "scr_delayduration")
	WebElement DelayDurationField;
	
	@FindBy(id= "scr_delayduration_i")
	WebElement DelayDuration;
	
	@FindBy (id="scr_screeningstatus")
	WebElement ScreeningStatusfield;
	
	@FindBy(xpath ="//*[@id='scr_screeningstatus']/div[1]/span")
	WebElement ScreeningStatus;
	
	@FindBy (id="scr_previousscreeningstatus")
	WebElement PreviousScreeningStatusfield;
	
	@FindBy(xpath ="//*[@id='scr_previousscreeningstatus']/div[1]/span")
	WebElement PreviousScreeningStatus;

	@FindBy(xpath="//*[@id='CallRecallHistory_addImageButtonImage']")
	WebElement AddCallRecallPlusSign;
	
	@FindBy(xpath="//*[@id='scr_recalltype']")
	WebElement CallRecallTypefield;
	
	@FindBy(xpath="//select[@id='scr_recalltype_i']")
	WebElement CallRecallType;
	
	@FindBy(xpath="//*[@id='scr_pnlenddate']")
	WebElement PNLDatefield;
	
	@FindBy(xpath="//*[@aria-labelledby='scr_pnlenddate_c scr_pnlenddate_w']")
	WebElement PNLDate;
	
	@FindBy(xpath="//*[@id='incident|NoRelationship|Form|Mscrm.Form.incident.SaveAndClose']/span/a/span")
	WebElement Save_close_CallRecallPage;
	
	@FindBy(xpath="//*[@id='scr_recalltype']/div/div']")
	WebElement CallRecallTypeDash;
	
	@FindBy(xpath="//table[@ologicalname='incident']")
	WebElement CervialTestcallTable;
	
	@FindBy(xpath="//table[@oname='10005']")
	WebElement CervialTestTResultTable;
	
	@FindBy(xpath="//table[@ologicalname='incident']//tr[1]/td[2]/nobr")
	WebElement CervialTestcallTable_FirstRecallType;
	
	@FindBy(xpath="//table[@ologicalname='incident']//tr[1]/td[7]/div")
	WebElement CervialTestcallTable_FirstStatus;
	
	@FindBy(xpath ="//*[@id='navTabGlobalCreateImage']")
	WebElement Quicksearchicon;
	
	@FindBy(xpath ="//*[@id='10005']")
	WebElement CervialResult_QuickSearch;
	
	
	
	public ContactPatient(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
// TODO Auto-generated constructor stub
	}
	
	
	
	public ScreeningInfo screeningrecordclick() {
		
		int records = 0;
		driver.switchTo().frame("contentIFrame1");
		try {
		
		List<WebElement> trCount = (List<WebElement>) Existingresult1.findElements(By.tagName("tr"));
		records = trCount.size();
		System.out.println(records);
		
		System.out.println("Table found.");
		if (records >= 1){
			System.out.println(firstRecord1.getText());
			String linktextname = firstRecord1.getText();
			wait.until(ExpectedConditions.elementToBeClickable(firstRecord1)).click();
		
			
		}
		   
		
		}
		catch (NoSuchElementException e){
			System.out.println("No table found second.");
			
			
		}
		driver.switchTo().defaultContent();
		return new ScreeningInfo(driver);
		

	}
	
public CrmHome AddCervicalResult() throws IOException, InterruptedException {
		
		try {
			Thread.sleep(5000);
			driver.switchTo().frame("contentIFrame1");
			Actions actions = new Actions(driver);
			actions.moveToElement(AddingbuttonCervialResult);
		//	System.out.println("First");
			actions.click().build().perform();
		//	actions.moveToElement(AddingbuttonCervialResult);
		//	System.out.println("second");
			
			//AddingbuttonCervialResult.click();
			System.out.println("Clicked on Add Button for Cervical Screening Result.");
			
			Thread.sleep(2000);
			
			//driver.switchTo().defaultContent();
			
			
			String winHandleBefore = driver.getWindowHandle();
			System.out.println(winHandleBefore);
			
			// Perform the click operation that opens new window

			// Switch to new window opened
			for(String winHandle : driver.getWindowHandles()){
			    driver.switchTo().window(winHandle);
			    System.out.println(winHandle);
			    driver.switchTo().frame("contentIFrame0");
			    
			}
				
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(Openwindowclick)).click();
		
			Thread.sleep(500);
	
			String Forenameexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Forename");
			System.out.println(Forenameexcel);
			wait.until(ExpectedConditions.elementToBeClickable(Forenamefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Forename)).sendKeys(Forenameexcel);
			
			String Surnameexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Surname");
			System.out.println(Surnameexcel);
			wait.until(ExpectedConditions.elementToBeClickable(Surnamefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Surname)).sendKeys(Surnameexcel);
			
			String Addressexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Address");
			System.out.println(Addressexcel);
			wait.until(ExpectedConditions.elementToBeClickable(Addressfield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Address)).sendKeys(Addressexcel);

			String SlideNumberexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Slidenumber");
			System.out.println(SlideNumberexcel);
			wait.until(ExpectedConditions.elementToBeClickable(SlideNumberField)).click();
			wait.until(ExpectedConditions.elementToBeClickable(SlideNumber)).sendKeys(SlideNumberexcel);
			
			String Sendercodeexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Slidenumber");
			System.out.println(Sendercodeexcel);
			wait.until(ExpectedConditions.elementToBeClickable(Sendercodefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Sendercode)).sendKeys(Sendercodeexcel);
			
			wait.until(ExpectedConditions.elementToBeClickable(Actionfield)).click();
			new Select(Action).selectByIndex(2);
			
			wait.until(ExpectedConditions.elementToBeClickable(InfectionField)).click();
			new Select(Infection).selectByIndex(2);
			
			wait.until(ExpectedConditions.elementToBeClickable(ResultField)).click();
			new Select(Resultselect).selectByIndex(1);
			
			wait.until(ExpectedConditions.elementToBeClickable(SourceField)).click();
			new Select(Source).selectByIndex(5);
			
			String Testdateexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "TestDate");
			System.out.println(Testdateexcel);
			wait.until(ExpectedConditions.elementToBeClickable(TestDateField)).click();
			wait.until(ExpectedConditions.elementToBeClickable(TestDate)).sendKeys(Testdateexcel);
			
			String Labexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Lab");
			System.out.println(Labexcel);
			wait.until(ExpectedConditions.elementToBeClickable(LabField)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Lab)).sendKeys(Labexcel);
			System.out.println(Labexcel);
			Lab.sendKeys(Keys.ENTER);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//a[@title='"+Labexcel+"']")).click();
			
			Thread.sleep(1000);
			driver.switchTo().defaultContent();
			
			Thread.sleep(1000);
			SaveButton.click();
			
			Thread.sleep(5000);
			 
			 
			// Perform the actions on new window

			// Close the new window, if that window no more required
			driver.close();

			// Switch back to original browser (first window)
			driver.switchTo().window(winHandleBefore);
			
			Thread.sleep(2000);

			// Continue with original browser (first window)
			driver.findElement(By.id("TabHome")).click();
		
		
			
		}
		   
		
		
		catch (NoSuchElementException e){
			System.out.println("No element found." + e);		
			
		}

		return new CrmHome(driver);
		

	}


public CallRecallRecord CallRecallHistory() throws InterruptedException {
    
    
    
    
    try {
           Thread.sleep(5000);
          driver.switchTo().frame("contentIFrame1");
           /*
           for (int i =0;i<3;i++)
           {
                                      
                  if (RecallFirstRow.isDisplayed())
                  {
                        break; //driver.navigate().refresh();
                  }
                  else
                  {
                  Thread.sleep(30000);
           
           
                  Actions action4 = new Actions(driver);
                  action4.moveToElement(RecallResultTable);
           action4.contextClick(RecallResultTable).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
                  //action4.contextClick(RecallResultTable);
           //     Refreshlist.click();
                  //div[@id='Dialog_3']//a[@title='Refresh List']
                  Thread.sleep(5000); 
                  }
           }
           */

    /*     Actions action = new Actions(driver);
           action.moveToElement(RecallResultTable);
           action.contextClick(RecallResultTable).build().perform();
           Thread.sleep(5000);*/
           
/*                         WebElement elementOpen = driver.findElement(By.xpath("//*[@id='item31']/a[2]/span/nobr/span"));
           Actions action3 = new Actions(driver);
           action3.click(elementOpen).build().perform();*/
    //action.contextClick(RecallResultTable).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).sendKeys(Keys.RETURN).sendKeys(Keys.RETURN).sendKeys(Keys.RETURN).build().perform();
/*            Thread.sleep(6000);
           String FirstRowName = wait.until(ExpectedConditions.elementToBeClickable(firstRecord1)).getText();
           //String FirstRowName = RecallFirstRow.getText();
           System.out.println(FirstRowName);*/
		    wait.until(ExpectedConditions.elementToBeClickable(RecallFirstRow));
           String Name = RecallFirstRow.getText();
           System.out.println(Name);
           Actions action2 = new Actions(driver);
           action2.doubleClick(RecallFirstRow).perform();
           Thread.sleep(5000);
           
           /*Actions action1 = new Actions(driver);
           action1.doubleClick(RecallFirstRow).perform();*/
           
           
/*     


    List<WebElement> trCount = (List<WebElement>) RecallTable.findElements(By.tagName("tr"));
    records = trCount.size();
    System.out.println(records);
    
    System.out.println("Recall result found.");
    if (records >= 1){
           System.out.println(Firstrecall.getText());
    wait.until(ExpectedConditions.elementToBeClickable(Firstrecall));
    Actions action = new Actions(driver);
    //Double click
    action.doubleClick(Firstrecall).perform();*/
                               
    //}
       
    
    }
    catch (NoSuchElementException e){
           System.out.println("No table found Third.");
           
           
    }
    driver.switchTo().defaultContent();
    
    


return new CallRecallRecord(driver);



}

public String GetCallRecallStatus() throws InterruptedException {
    
    String Name = null;
    
    
    try {
           Thread.sleep(5000);
           driver.switchTo().frame("contentIFrame1");
        
           Name = RecallFirstRow.getText();
           System.out.println(Name);
       /*    Actions action2 = new Actions(driver);
           action2.doubleClick(RecallFirstRow).perform();
           Thread.sleep(5000);*/
           
      
       
    
    }
    catch (NoSuchElementException e){
           System.out.println("No table found Third.");
           
           
    }
    //driver.switchTo().defaultContent();
    
    


return Name;



}



public ContactPatient getFullNameandTestDuedate() {
	// TODO Auto-generated method stub
	driver.switchTo().frame("contentIFrame1");
	
	try {
		
		
		wait.until(ExpectedConditions.elementToBeClickable(PatientFullNamefield));
		String PatientFullNameonscreening = wait.until(ExpectedConditions.elementToBeClickable(PatientFullName)).getText();
		System.out.println(PatientFullNameonscreening);
		utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "FullName",PatientFullNameonscreening);
		
		
		wait.until(ExpectedConditions.elementToBeClickable(TestDuedatefield));
		String TestDuedateonscreening = wait.until(ExpectedConditions.elementToBeClickable(TestDuedate)).getText();
		System.out.println(TestDuedateonscreening);
		utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "TestDueDate",TestDuedateonscreening);
	
	}
	catch (NoSuchElementException e)
		{
		System.out.println("The patient data is not stored properly."+e);
		}

		driver.switchTo().defaultContent();
		
	return new ContactPatient (driver); 

}



public boolean verifyscreeningstatus() {
	boolean screeningstatusonpatient = false;
	driver.switchTo().frame("contentIFrame1");
	try{

		wait.until(ExpectedConditions.elementToBeClickable(ScreeningStatusfield));
		String ScreeingstatusOnNew = wait.until(ExpectedConditions.elementToBeClickable(ScreeningStatus)).getText();
		if(ScreeingstatusOnNew.equalsIgnoreCase("First Call"))
		{
			screeningstatusonpatient = true;
	}
	}
	catch (NoSuchElementException e)
	{
	System.out.println("The element is not found for screening status."+e);
	}

	driver.switchTo().defaultContent();
	// TODO Auto-generated method stub
	return screeningstatusonpatient;
}



public Boolean Verifypreviousscreeningstatus() {
	boolean previousscreeningstatusonPatient = false;
	driver.switchTo().frame("contentIFrame1");
	try{

		wait.until(ExpectedConditions.elementToBeClickable(PreviousScreeningStatusfield));
		String ScreeingstatusOnNew = wait.until(ExpectedConditions.elementToBeClickable(PreviousScreeningStatusfield)).getText();
		if(ScreeingstatusOnNew.equalsIgnoreCase("First Call")||ScreeingstatusOnNew.equalsIgnoreCase("Recall"))
		{
			previousscreeningstatusonPatient = true;
	}
	}
	catch (NoSuchElementException e)
	{
	System.out.println("The element is not found for previous screening status."+e);
	}

	driver.switchTo().defaultContent();
	// TODO Auto-generated method stub
	return previousscreeningstatusonPatient;
}

public ContactPatient AddManuallyCallRecallRecords(String CallRecallTypeText) {
	try{
		switchToFrame(AddCallRecallPlusSign, driver);
		Actions action = new Actions(driver);
		action.moveToElement(AddCallRecallPlusSign);
		action.click().build().perform();
		Thread.sleep(1000);
		helpers.WindowHandleSupport.getRequiredWindowDriver(driver,"Call / Recall:");
		driver.switchTo().defaultContent();
		switchToFrame(CallRecallTypefield, driver);
	
		wait.until(ExpectedConditions.elementToBeClickable(PNLDatefield)).click();			
		wait.until(ExpectedConditions.elementToBeClickable(PNLDatefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(PNLDate)).click();
		Date myDate = new Date();
		PNLDate.sendKeys(new SimpleDateFormat("MM/dd/yyyy").format(myDate));
		wait.until(ExpectedConditions.elementToBeClickable(CallRecallTypefield)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(CallRecallTypefield)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(CallRecallType)).click();
		Select dropdown = new Select(CallRecallType);
		System.out.println(CallRecallTypeText);
		dropdown.selectByVisibleText(CallRecallTypeText);
		
		driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.elementToBeClickable(Save_close_CallRecallPage)).click();	
		Thread.sleep(3000);
		
		if (isAlertPresent()) {
			driver.switchTo().alert();
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			}
		driver.close();
		helpers.WindowHandleSupport.getRequiredWindowDriver(driver,"Screening:");
	
		
	}
	catch (Exception e)
    {
          System.out.println("The call Recall record not add manually");
          
    }

	return new ContactPatient(driver);
}

public boolean isAlertPresent() {
try {
driver.switchTo().alert();
return true;
} // try
catch (Exception e) {
return false;
} // catch
}

public Boolean verifyStatus(String RecallType, String Status ,int i) {
	Boolean RecallType_Status = false;
	try
	{
		switchToFrame(CervialTestcallTable, driver);
		String FirstRecallType = driver.findElement(By.xpath("//table[@ologicalname='incident']//tr["+i+"]/td[2]/nobr")).getText();
	//	String FirstRecallType = CervialTestcallTable_FirstRecallType.getText();
		if(FirstRecallType.equalsIgnoreCase(RecallType))
		{
			String FirstStatus = driver.findElement(By.xpath("//table[@ologicalname='incident']//tr["+i+"]/td[7]/div")).getText();
			//String FirstStatus  = CervialTestcallTable_FirstStatus.getText();
			if(FirstStatus.equalsIgnoreCase(Status))
			{
				RecallType_Status = true;
			}
					
		}
		
		
	}
	catch (Exception e)
    {
          System.out.println("The Status of call recall is not captured");
          
    }
	driver.switchTo().defaultContent();
	return RecallType_Status;
}



public int gettotalnoOfRows_CallRecallHistory() {
	int TotalNoOfRows = 0;
	try
	{
		switchToFrame(CervialTestcallTable, driver);
		try {
			//System.out.println(driver.findElements(By.xpath("//table[@ologicalname='scr_screening']")).size());
			boolean ispresent = driver.findElements(By.xpath("//table[@ologicalname='incident']")).size() != 0;
			System.out.println(ispresent);
			if(ispresent)
			{

				List<WebElement> trCount = (List<WebElement>) CervialTestcallTable.findElements(By.tagName("tr"));
				TotalNoOfRows = trCount.size();
				System.out.println("Record found in Call Recall History"+TotalNoOfRows);
			}
			else
			{
				TotalNoOfRows = 0;
			}
		
		}
		catch (NoSuchElementException e){
			System.out.println("No table found.");
			
			
		}
		driver.switchTo().defaultContent();
		
		
	}
	catch (Exception e)
    {
          System.out.println("The Status of call recall is not captured");
          
    }
	driver.switchTo().defaultContent();
	return TotalNoOfRows;
}



public ContactPatient clickOnQuicksearch_CervialTestResult() {
			try{
			wait.until(ExpectedConditions.elementToBeClickable(Quicksearchicon)).click();
			wait.until(ExpectedConditions.elementToBeClickable(CervialResult_QuickSearch)).click();
			
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The quick search icon is not clicked "+e);
		}
		
		return new ContactPatient(driver);
	}



public ContactPatient AddCervicalResult_QuickSearch() throws InterruptedException, IOException {
	try{
		switchToFrame(Forenamefield, driver);
		String Forenameexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Forename");
		System.out.println(Forenameexcel);
		wait.until(ExpectedConditions.elementToBeClickable(Forenamefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Forenamefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Forename)).sendKeys(Forenameexcel);
		
		String Surnameexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Surname");
		System.out.println(Surnameexcel);
		wait.until(ExpectedConditions.elementToBeClickable(Surnamefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Surnamefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Surname)).sendKeys(Surnameexcel);
		
		String Addressexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Address");
		System.out.println(Addressexcel);
		wait.until(ExpectedConditions.elementToBeClickable(Addressfield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Addressfield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Address)).sendKeys(Addressexcel);

		String SlideNumberexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Slidenumber");
		System.out.println(SlideNumberexcel);
		wait.until(ExpectedConditions.elementToBeClickable(SlideNumberField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SlideNumberField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SlideNumber)).sendKeys(SlideNumberexcel);
		
		String Sendercodeexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Slidenumber");
		System.out.println(Sendercodeexcel);
		wait.until(ExpectedConditions.elementToBeClickable(Sendercodefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Sendercodefield)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Sendercode)).sendKeys(Sendercodeexcel);
		
		wait.until(ExpectedConditions.elementToBeClickable(Actionfield)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(Actionfield)).click();
		new Select(Action).selectByIndex(2);
		
		wait.until(ExpectedConditions.elementToBeClickable(InfectionField)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(InfectionField)).click();
		new Select(Infection).selectByIndex(2);
		
		wait.until(ExpectedConditions.elementToBeClickable(ResultField)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(ResultField)).click();
		new Select(Resultselect).selectByIndex(1);
		
		wait.until(ExpectedConditions.elementToBeClickable(SourceField)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(SourceField)).click();
		new Select(Source).selectByIndex(5);
		
		String Testdateexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "TestDate");
		System.out.println(Testdateexcel);
		wait.until(ExpectedConditions.elementToBeClickable(TestDateField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(TestDateField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(TestDate)).sendKeys(Testdateexcel);
		
		String Labexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Lab");
		System.out.println(Labexcel);
		wait.until(ExpectedConditions.elementToBeClickable(LabField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(LabField)).click();
		wait.until(ExpectedConditions.elementToBeClickable(Lab)).sendKeys(Labexcel);
		System.out.println(Labexcel);
		Lab.sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@title='"+Labexcel+"']")).click();
		
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
		Save_CloseButton.click();
	}
	catch (NoSuchElementException e)
	{
	System.out.println("The Add cervial Test Result is ont happened"+e);
	}
	
	return new ContactPatient(driver);
}



public int VerifyCervialTestResult() {
	int TotalNoOfRows = 0;
	
		try {
			switchToFrame(CervialTestTResultTable, driver);
			//System.out.println(driver.findElements(By.xpath("//table[@ologicalname='scr_screening']")).size());
			boolean ispresent = driver.findElements(By.xpath("//table[@oname='10005']")).size() != 0;
			System.out.println(ispresent);
			if(ispresent)
			{

				List<WebElement> trCount = (List<WebElement>) CervialTestTResultTable.findElements(By.tagName("tr"));
				TotalNoOfRows = trCount.size();
				System.out.println("Record found in Cervial Test Result "+TotalNoOfRows);
			}
			else
			{
				TotalNoOfRows = 0;
			}
		
		}
	
		
	
	catch (Exception e)
    {
          System.out.println("The Status of call recall is not captured");
          
    }
	driver.switchTo().defaultContent();
	return TotalNoOfRows;
}



	
}

	
