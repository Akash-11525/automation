package pageobjects.GPP.PMSAPMS;
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
import pageobjects.PL.Referees;
import utilities.ExcelUtilities;

public class BaselineEntryScreen extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(xpath="//*[@id='BulkEntryForm']/div[3]/div/h4")
	WebElement FinancialYearText;
	
	@FindBy(xpath="//div[@id='divUploadCSV']//div[@class='modal-content']")
	WebElement CSVmodelwindow;
	
	@FindBy(xpath="//*[@id='divUploadCSV']/div/div/div[2]/div[2]/h5")
	WebElement CSVMessage;
	
	
	@FindBy(id="FileUpload")
	WebElement Choosefile;
	
	@FindBy(xpath="//div[@id='successModal']//div[@class='modal-content']")
	WebElement NotificationUpload;
	
	@FindBy(xpath="//*[@id='successModal']/div/div/div[2]/p")
	WebElement NotificationUploadmessage;
	
	@FindBy(xpath="//*[@id='divUploadCSV']/div/div/div[3]/div[1]/button")
	WebElement CancalCSVbutton;
	
	@FindBy(xpath="//button[@id='submitbtn']")
	WebElement SubmitCSVbutton;	
	
	@FindBy(xpath="//select[@id='BulkRecordtbl_0__selectedInstructionType']")
	WebElement InstructionSelect;	
	
	@FindBy(id="NumberOfPatients0")
	WebElement Noofpatient;
	
	@FindBy(id="CustomValue0")
	WebElement CustomValue;
	
	@FindBy(id="AnnualValue0")
	WebElement AnnualValue;
	
	@FindBy(id="BulkRecordtbl_0__ContractorName")
	WebElement Contractorcode;
	
	@FindBy(xpath="//ul[@id='ContractorNameResult0']/li/a")
	WebElement FirstResultContractorcode;
	
	@FindBy(xpath="//div[@id='divConfSave']//div[@class='modal-content']")
	WebElement SaveModelWindow;	

	@FindBy(xpath="//div[@id='divConfSave']//div[@class='modal-body']")
	WebElement SaveMessageOnwindow;
	
	@FindBy(xpath="//*[@id='divConfSave']/div/div/div[3]/div[1]/button")
	WebElement CancelOnwindow;
	
	@FindBy(xpath="//input[@type='submit']")
	WebElement ConfirmOnWindow;
	
	
	String ContractorCode_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","ContractorCode", 1);
	String Instructiontype_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","Instructiontype", 1);
	String NoofPatient_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","NoofPatient", 1);
	String CustomValue_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","CustomValue", 1);
	String AnnualValue_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","AnnualValue", 1);
	String SaveMessage_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","SaveMessage", 1);
	
	String BaselineFor_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","BaselineFor", 1);
	public BaselineEntryScreen(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}



	public List<String> ActualBaselineEntryLabel() {
		List<WebElement> ActualBaselineentryList = null;
		List<String> LabelResult = null;
		try{
			Thread.sleep(3000);
		
			ActualBaselineentryList=driver.findElements(By.xpath("//table[@id='tblFinancialYr']//tr[1]//th"));
				System.out.println(ActualBaselineentryList);
				LabelResult = new ArrayList<String>();
				 for (WebElement Baselinelabel:ActualBaselineentryList)
				 {
					 scrolltoElement(driver, Baselinelabel);
					 String labelname = Baselinelabel.getText();
					 System.out.println(labelname);
					 if(!(labelname.equalsIgnoreCase("")))
					 {
						 LabelResult.add(labelname);
					 }
				 }
					
					 
				 
		}
		catch(Exception e)
		{
			System.out.println("The Baseline line entry screen is not captured");
		}
				

		return LabelResult;
	}



	public List<String> ExpectedResultLabel(String Sheet, int Position) throws IOException {
		List<String> ExpectedResultLabelList = null;
		try{
			ExpectedResultLabelList = ExcelUtilities.getCellValuesInExcel("GPPPMSTESTDATA.xlsx", Sheet, Position);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Expected Baselinescreen Label is not captured " +e);
		}
		// TODO Auto-generated method stub
		return ExpectedResultLabelList;

	}



	public int verifyResultLabel(List<String> expectedEntryScreenLabel, List<String> acutalEntryScreenLabel) {
		int count = 0;
		try{			
			
			for(int i=0;i<expectedEntryScreenLabel.size();i++)
			{
				if(acutalEntryScreenLabel.contains(expectedEntryScreenLabel.get(i)))
				{
					System.out.println("Exist : "+expectedEntryScreenLabel.get(i));
				}
				else 
				{
					count = 1;
					System.out.println("Does not Exist : "+expectedEntryScreenLabel.get(i));
				}
			}
		
	}
	catch(Exception e)
	{
		System.out.println("The result Base line label is not captured");
	}
		return count;
	}



	public boolean verifyfinancialyear() {
		boolean financialyear = false;
		try{
			scrolltoElement(driver, FinancialYearText);
			wait.until(ExpectedConditions.visibilityOf(FinancialYearText));
			String FinancialYearfullText = FinancialYearText.getText();
			System.out.println(FinancialYearfullText);
			String Year=FinancialYearfullText.split(":")[1].trim();
			if(BaselineFor_excel.equalsIgnoreCase(Year))
			{
				financialyear = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("The financial year is not captured");
		}
		return financialyear;
	}



	public boolean verifyCSVmessage() throws IOException {
		boolean CSVmessage = false;
		String CSVMessage_Excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","CSVMessage", 1);
		try{
			
			Thread.sleep(3000);
			if(CSVmodelwindow.isDisplayed())
			{
				String ActualCSVmessage = CSVMessage.getText();
				if(CSVMessage_Excel.equalsIgnoreCase(ActualCSVmessage))
				{
					CSVmessage = true;
				}
				
				
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The CSV message is not captured");
		}
		
		return CSVmessage;
	}



	public boolean verifyCSVPopUp() {
		boolean CSVPOPUP = false;
		try
		{
			Thread.sleep(3000);
			if(CSVmodelwindow.isDisplayed())
			{
				CSVPOPUP = true;
			}
			else
			{
				CSVPOPUP = false;
			}
		}
		catch(Exception e)
		{
			System.out.println("The CSV pop is not captured");
		}
		// TODO Auto-generated method stub
		return CSVPOPUP;
	}



	public BaselineEntryScreen uploadCSVfile() {
		try{
			wait.until(ExpectedConditions.visibilityOf(CSVmodelwindow));
			if(CSVmodelwindow.isDisplayed())
			{
				scrolltoElement(driver, Choosefile);
				wait.until(ExpectedConditions.elementToBeClickable(Choosefile)).clear();
				String filePath = System.getProperty("user.dir") + "\\Upload\\sample.csv";			
				helpers.CommonFunctions.Uploadfile(filePath, driver);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("The CSV file is not uploaded sucessfully");
		}
		return new BaselineEntryScreen(driver);
	}



	public boolean verifyCSVupload() {
		boolean CSVupload = false;
		String Notificationupload_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","NotificationUpload", 1);
		try{
			Thread.sleep(3000);
			if(NotificationUpload.isDisplayed())
			{
				String ActualNotificationuploadmessage = NotificationUploadmessage.getText();
				if(Notificationupload_excel.equalsIgnoreCase(ActualNotificationuploadmessage))
				{
					CSVupload = true;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The notification of upload screen is not captured");
		}
		// TODO Auto-generated method stub
		return CSVupload;
	}



	public BaselineEntryScreen clickonCancelCSVPopUp() {
		try{
			Thread.sleep(2000);
			if(CSVmodelwindow.isDisplayed())
			{
				Actions action = new Actions(driver);
				action.moveToElement(CancalCSVbutton);
				action.doubleClick().build().perform();
				
			}
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on CSV is not captured");
		}
		return new BaselineEntryScreen (driver);
	}
	
	public BaselineEntryScreen clickonSubmitCSVPopUp() {
		try{
			Thread.sleep(2000);
			if(CSVmodelwindow.isDisplayed())
			{
				Actions action = new Actions(driver);
				action.moveToElement(SubmitCSVbutton);
				action.doubleClick().build().perform();
				
			}
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on CSV is not captured");
		}
		return new BaselineEntryScreen (driver);
	}



	public List<String> getalloptioninstruction() {
		List<WebElement> Alloptions = null;
		List<String> Options = null;
		try{
			Thread.sleep(3000);
			 Select dropdown = new Select(InstructionSelect);
			 List<WebElement> allOptions = dropdown.getOptions();
			System.out.println(allOptions);
				Options = new ArrayList<String>();
				 for (WebElement option:allOptions)
				 {
					 scrolltoElement(driver, option);
					 String optionname = option.getText();
					 System.out.println(optionname);
					 if(!(optionname.equalsIgnoreCase("")))
					 {
						 Options.add(optionname);
					 }
				 }
		}
		
				 catch(Exception e)
				 {
					System.err.println("The Select option menu is not captured");
					
				 }
		return Options;
	}



	public BaselineEntryScreen selectinstructionType(String option) {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(InstructionSelect));
			Select dropdown = new Select(InstructionSelect);
			 dropdown.selectByVisibleText(option);
			 wait.until(ExpectedConditions.elementToBeClickable(InstructionSelect));
		}
		catch(Exception e)
		{
			System.out.println("The Instruction type is not captured");
		}
		return new BaselineEntryScreen(driver);
	}



	public boolean verifyNoofpatient_Custom() {
		boolean disabled = false;
		try{
			wait.until(ExpectedConditions.visibilityOf(Noofpatient));
			scrolltoElement(driver, Noofpatient);
	
			try{	
			if((Noofpatient.getAttribute("readonly").equalsIgnoreCase("true")))
			{
				wait.until(ExpectedConditions.visibilityOf(CustomValue));
				scrolltoElement(driver, CustomValue);
				if((CustomValue.getAttribute("readonly").equalsIgnoreCase("true")))
					{
					disabled = true;
					}
			}
			}
			catch(NullPointerException e)
			{
				disabled = false;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The No of patient and custom field is not captured");
		}
		// TODO Auto-generated method stub
		return disabled ;
	}



	public boolean verifyannualContract() throws InterruptedException {
		boolean disabled = false;
		try{
			Thread.sleep(1000);
			wait.until(ExpectedConditions.visibilityOf(AnnualValue));
			scrolltoElement(driver, AnnualValue);
			try{
					
			if((AnnualValue.getAttribute("readonly").equalsIgnoreCase("true")))
				{
				disabled = true;
				}
			}
			catch(NullPointerException e)
			{
				disabled = false;
			}
			
			}
					
		catch(Exception e)
		{
			System.out.println("The Annual Field is not captured");
			
		}
		
		
		return disabled;
	}



	public BaselineEntryScreen FillBaselineentry() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Contractorcode));
			scrolltoElement(driver, Contractorcode);
			wait.until(ExpectedConditions.elementToBeClickable(Contractorcode)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Contractorcode)).sendKeys(ContractorCode_excel);
			Thread.sleep(2000);
			Actions action = new Actions(driver);
			action.moveToElement(FirstResultContractorcode);
			action.doubleClick().build().perform();	
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(InstructionSelect));
			Select dropdown = new Select(InstructionSelect);
			 dropdown.selectByVisibleText(Instructiontype_excel);
			 Thread.sleep(2000);
			if(Instructiontype_excel.equalsIgnoreCase("Custom"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(Noofpatient)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(Noofpatient)).sendKeys(NoofPatient_excel);
				wait.until(ExpectedConditions.elementToBeClickable(CustomValue)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CustomValue)).sendKeys(CustomValue_excel);
				wait.until(ExpectedConditions.elementToBeClickable(CustomValue)).sendKeys(Keys.TAB);
			}
			if(Instructiontype_excel.equalsIgnoreCase("Flat Amount"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(AnnualValue)).sendKeys(AnnualValue_excel);
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("The Base line entry is not captured");
		}
		return new BaselineEntryScreen(driver) ;
	}



	public boolean verifySavemessage() {
		boolean Savemessage = false;
		try{
			wait.until(ExpectedConditions.visibilityOf(SaveModelWindow));
			if(SaveModelWindow.isDisplayed())
			{
				String ActualSavemessage = SaveMessageOnwindow.getText();
				if(SaveMessage_excel.equalsIgnoreCase(ActualSavemessage))
				{
					Savemessage = true;
				}
				
			}
		}
		catch(Exception e)
		{
			System.out.println("The Save message is not captured");
		}
		
		return Savemessage;
	}



	public BaselineEntryScreen clickoncancelonSavewindow() {
		try{
			if(SaveModelWindow.isDisplayed())
			{
				Actions action = new Actions(driver);
				action.moveToElement(CancelOnwindow);
				action.doubleClick().build().perform();
			}
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button is not clicked");
		}
		return new BaselineEntryScreen (driver);
	}



	public boolean verifysavewindow() {
		boolean Savewindow = false;
		try{
			Thread.sleep(3000);
			if(SaveModelWindow.isDisplayed())
			{
				Savewindow = true;
			}
			else
			{
				Savewindow = false;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Save window is not captured");
		}

		return Savewindow;
	}



	public PMSinstructionScreen clickonconfirm() {
	try{
		Thread.sleep(3000);
		if(SaveModelWindow.isDisplayed())
		{
			Actions action = new Actions(driver);
			action.moveToElement(ConfirmOnWindow);
			action.doubleClick().build().perform();
		}
	}
	catch(Exception e)
	{
		System.out.println("The confirm button is not clicked");
	}
		return new PMSinstructionScreen(driver);
	}



	public void BaselineScreenshots(String note)throws InterruptedException, IOException {
		scrolltoElement(driver, FinancialYearText);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}



	

	

}
