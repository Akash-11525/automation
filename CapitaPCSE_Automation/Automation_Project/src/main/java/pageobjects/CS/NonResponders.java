package pageobjects.CS;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
//import CommonFunctions.*;
import helpers.*;

public class NonResponders {

	WebDriver driver;
	WebDriverWait wait;
	int activeRow = 0;
	
	String fileFolderPath = System.getProperty("user.dir") + "/Upload/";
	
	String DeferOption1 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason1");
	String DeferOption2 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason2");
	String DeferOption3 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason3");
	//String DeferOption4	= utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "DeferOptions", "Option4");
	String DeferDelayDuration1 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration1");
	String DeferDelayDuration2 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration2");
	String DeferDelayDuration3 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration3");
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']/tbody")
	WebElement nonRespondersTable;
	
	@FindBy(xpath="//h2")
	WebElement notificationListHeader;
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[8]")
	WebElement nonRespondersTableAction;
	
	@FindBy(css="button[id='newRegistrationCSV']")
	WebElement newRegCSVBtn;
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/button")
	WebElement submitButton;
	
	@FindBy(css="div[class*='modal fade in']")
	WebElement ceaseModalWindowActive;

	@FindBy(xpath="//table[@id='pcss-patient-new-registration']/tbody")
	WebElement tblNewReg;
	
	@FindBy(id="newRegistrationsBadge")
	WebElement newRegBadge; 
	
	@FindBy(id="nonRespondersBadge")
	WebElement nonRespondersBadge; 
	
	@FindBy(id="modalCeasePopup")
	WebElement modalCeasePopup;
	
	@FindBy(xpath="//table[@class='table table-responsive table-striped']/tbody//select[@id='CeaseReason']")
	WebElement selectReasonsOptions;
	
	@FindBy(xpath="//table[@class='table table-responsive table-striped']/tbody//select[@id='delayReason']")
	WebElement selectDeferReasonsOptions;
	
	@FindBy(id="ceaseFileUpload")
	WebElement ceaseFileUpload;
	
	@FindBy(id="deferFileUpload")
	WebElement deferFileUpload;
	
	@FindBy(id="ceaseSubmit")
	WebElement ceasePatientSubmitButton;
	
	@FindBy(id="deferSubmit")
	WebElement deferPatientSubmitButton;
	
	@FindBy(id="printDeferForm")
	WebElement deferPrintForm;
	
	@FindBy(id="cancelCeasing")
	WebElement ceasePatientCancelButton;
	
	@FindBy(id="cancelDefer")
	WebElement deferPatientCancelButton;
	
	@FindBy(id="ceaseNHSNumber")
	WebElement ceaseNHSNumber;
	
	@FindBy(id="deferNHSNumber")
	WebElement deferNHSNumber;
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']//tr[1]/td[@id='fullname']")
	WebElement ceasedPatientNameofFirstRecord;
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']//tr[1]/td[@class='patientNHSField']/a")
	WebElement ceasedPatientNHSofFirstRecord;
	
	@FindBy(xpath = "//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/span")
	WebElement ceasedTickMarkOnNRL;
	
	@FindBy(id="ceasePatientName")
	WebElement ceasedPatientName;
	
	@FindBy(id="deferPatientName")
	WebElement deferPatientName;
	
	@FindBy(css="input[id='printCeaseForm']")
	WebElement ceasedPrintForm;
	
	@FindBy(id="CeaseReason")
	WebElement ceasedReasonDD;
	
	@FindBy(id="delayReason")
	WebElement delayReasonDD;
	
	@FindBy(id="delayDuration")
	WebElement delayDurationDD;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement ModalWindow;
	
	@FindBy(id="nonResponderCSV")
	WebElement nonResponderCSV;
	
	public NonResponders(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public List<ArrayList<String>> GetNewRegData(){
		
		List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		data = UITableDataSupport.getUITableData(nonRespondersTable);
		
		return data;
		
	}
	
	public NewRegistration GetNewRegCSV() throws InterruptedException
	{
		
		newRegCSVBtn.click();
		Thread.sleep(10000);
		return new NewRegistration(driver);
	}
	
	public List<ArrayList<String>> getNewRegData()
	{
		List<ArrayList<String>> tblData = new ArrayList<ArrayList<String>>();
	//	Week4Tab.click();
		//tblData = UITableDataSupport.getUITableData(tblToDOWeek4);
		 List<Integer> colnums = new ArrayList<Integer>();
		 colnums.add(1);
		 colnums.add(2);
		 colnums.add(3);
		 colnums.add(4);
		 colnums.add(5);
		 colnums.add(6);
		 colnums.add(7);
		 
		

		tblData = UITableDataSupport.getSpecificColumnDataInTable(tblNewReg,colnums );
		
		
		
		return tblData;
	}
	
	public boolean nonRespondersPresent()
	{
		Boolean flNR = false;
		String NonRespCount = nonRespondersBadge.getText();
		//String PC = CurrentWeekPatientsToDo.getText();
		System.out.println("Non responders Count: "+NonRespCount);
				
		int countNR = Integer.parseInt(NonRespCount);
		System.out.println(countNR);
		
		if(countNR > 0)
			flNR = true;
		
		return flNR;
				
		//return new PNLPage(driver);
	}
	
	public void selectActionFromFirstRecord(String text) throws InterruptedException
	{
		//WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
		WebElement column = nonRespondersTableAction;
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
		
		//String fullname= ceasedPatientNameofFirstRecord.getText();
		
		Select dropdown = new Select(nonRespondersTableAction.findElement(By.id("ActionId")));
	
		
		WebElement option = dropdown.getFirstSelectedOption();
		System.out.println("The previous dropdown value selected: "+option.getText());
		
		dropdown.selectByVisibleText(text);
		Thread.sleep(3000);
		WebElement option1 = dropdown.getFirstSelectedOption();
		
		System.out.println("The dropdown value selected: "+option1.getText());
			
	}
	
	public void getSelectedActionFromFirstRecord()
	{
		Select dropdown = new Select(nonRespondersTableAction.findElement(By.id("ActionId")));
		WebElement option = dropdown.getFirstSelectedOption();
		System.out.println("The previous dropdown value selected: "+option.getText());
	}
	
	
	
	
	public boolean checkActiveRecord(String tableName, int columnNumber)
	{
		boolean activeRecordFlag = false;
		int rowNumber=1;
		
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='"+tableName+"']/tbody/tr"));
		int rowCount = rows.size();
		System.out.println(rowCount);
		
		for (int i=1; i<=rowCount;i++)

		{
			try{
			
				WebElement subCol = driver.findElement(By.xpath("//table[@id='"+tableName+"']/tbody/tr["+i+"]/td["+columnNumber+"]"));
				System.out.println(subCol.getText());
				String value = subCol.findElement(By.tagName("button")).getAttribute("disabled");
				System.out.println(value);
				System.out.println(subCol.findElement(By.tagName("button")).getAttribute("id"));
						if (value == null && rowNumber<=rowCount)
						{
			        	break;
						}
						else
							rowNumber++;
						
				}
			        catch (Exception e) {
			        	
			    	}
			
				
		       
		    } 
		
		if (rowNumber>rowCount)
		{
			System.out.println("No active records in table.");
			return false;
		}
			
			
		return true;
		
		
		//return activeRecordFlag;					
	}
	
	
	public void selectActionFromActiveRecord(String text,  int ColumnNumber) throws InterruptedException
	{
		//WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
		int activeRow = getSpecificRowNumberInTable(nonRespondersTable, ColumnNumber);
		if(activeRow>0)
		{
		ColumnNumber--;
		WebElement column1 = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr["+activeRow+"]/td["+ColumnNumber+"]"));
				
		WebElement column = nonRespondersTableAction;
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column1);
		
		//String fullname= ceasedPatientNameofFirstRecord.getText();
		
		Select dropdown = new Select(column1.findElement(By.id("ActionId")));
	
		
		WebElement option = dropdown.getFirstSelectedOption();
		System.out.println("The previous dropdown value selected: "+option.getText());
		
		dropdown.selectByVisibleText(text);
		Thread.sleep(3000);
		WebElement option1 = dropdown.getFirstSelectedOption();
		
		System.out.println("The dropdown value selected: "+option1.getText());
		}
		else
		{
			System.out.println("No Active Records found in table");
		}
	}
	
	public String getPatientNameFromActiveFirstRecord()
	{
		String name = null;
		//activeRow = getactiverRow();
		WebElement activePatientNameofActiveRecord = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+activeRow+"]/td[@id='fullname']"));
		name= activePatientNameofActiveRecord.getText();
		return name;		
	}
	
	public String getPatientNameFromActiveDeferRecord(int activeRowNum)
	{
		String name = null;
		//int activeRow = getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
		
		WebElement activePatientNameofActiveRecord = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+activeRowNum+"]/td[@id='fullname']"));
		name= activePatientNameofActiveRecord.getText();
		return name;		
	}
	
	public String getPatientNameFromFirstRecord()
	{
		String name = null;
		
		name= ceasedPatientName.getText();
		return name;		
	}
	
	public String getNHSFromFirstActiveRecord()
	{
		String nhs = null;
		
		WebElement activePatientNHSofActiveRecord = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+activeRow+"]/td[@id='fullname']/a"));
		nhs= activePatientNHSofActiveRecord.getText().replaceAll("\\s+","");
		return nhs;		
	}
	
	public String getNHSFromActiveDeferRecord(int activeRowNum)
	{
		String nhs = null;
		//int activeRow = getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
		WebElement activePatientNHSofActiveRecord = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+activeRowNum+"]/td[@class='patientNHSField']/a"));
		nhs= activePatientNHSofActiveRecord.getText().replaceAll("\\s+","");
		return nhs;		
	}
	
	public String getNHSFromFirstRecord()
	{
		String nhs = null;
		nhs= ceasedPatientNHSofFirstRecord.getText().replaceAll("\\s+","");
		return nhs;		
	}
	
	public String getNHSwithspace()
	{
		String NHSNo = null;
		NHSNo= ceasedPatientNHSofFirstRecord.getText();
		return NHSNo;		
	}
	
	public String getNHSwithspaceFromActiveRecord(int rowNum)
	{
		String NHSNo = null;
		WebElement ceasedPatientNHS = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+rowNum+"]/td[@class='patientNHSField']/a"));
		NHSNo= ceasedPatientNHS.getText();
		return NHSNo;		
	}
	
	
	public boolean clickSubmitAndCheckPopupUp() throws InterruptedException
	{
		boolean flag = false;
		try
		{
			
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(2000);
		
		if (ceaseModalWindowActive.isDisplayed())
			flag = true;
		
		//wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
		
				
		
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Either Submit button is not enabled or Cease Submit Modal window is not present.");
		}
		
		return flag;
	}
	
	public boolean clickActiveSubmitAndCheckPopupUp() throws InterruptedException
	{
		boolean flag = false;
		try
		{
		WebElement activeSubmitButton = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']//tr["+activeRow+"]/td[9]/button"));
		wait.until(ExpectedConditions.elementToBeClickable(activeSubmitButton)).click();
		Thread.sleep(2000);
		
		if (ceaseModalWindowActive.isDisplayed())
			flag = true;
		
		//wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
		
				
		
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Either Submit button is not enabled or Cease Submit Modal window is not present.");
		}
		
		return flag;
	}
	
	public DeferPopup ClickonSubmitForDefer()
	{
		//int activeRow = getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
		//WebElement submitBtnDefer = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr["+activeRow+"]/td[9]/button"));
	//	wait.until(ExpectedConditions.elementToBeClickable(submitBtnDefer)).click();
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		return new DeferPopup(driver);		
	}
	
	public DeferPopup ClickonSubmitForGPDefer(int activeRowNum)
	{
		//int activeRow = getRowNumberForCeaseDefer("pcss-patient-non-responders", 8);
		WebElement submitBtnDefer = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr["+activeRowNum+"]/td[9]/button"));
		wait.until(ExpectedConditions.elementToBeClickable(submitBtnDefer)).click();
		return new DeferPopup(driver);		
	}

	public boolean VerifydeferdiolgueBox() throws InterruptedException
	{
		boolean flag = false;
		try
		{
		
	
		if (ceaseModalWindowActive.isDisplayed())
			flag = true;
		
		//wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
		
				
		
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Either Submit button is not enabled or Cease Submit Modal window is not present.");
		}
		
		return flag;
	}
	
	
	public List<String> getReasonForCease()
	{
		/*List<String> options = new ArrayList<String>();
	    for (WebElement option : new Select(driver.findElement(by)).getOptions()) {
	        String txt = option.getText();
	        if (option.getAttribute("value") != "") options.add(option.getText());
	    }
	    return options;*/
		
		List<String> options = helpers.CommonFunctions.getAllOptions(selectReasonsOptions);
		
		return options;
		
		
	}
	
	public List<String> getReasonForDefer()
	{
		/*List<String> options = new ArrayList<String>();
	    for (WebElement option : new Select(driver.findElement(by)).getOptions()) {
	        String txt = option.getText();
	        if (option.getAttribute("value") != "") options.add(option.getText());
	    }
	    return options;*/
		
		List<String> options = helpers.CommonFunctions.getAllOptions(selectDeferReasonsOptions);
		
		return options;
		
		
	}
	
	public void selectCeasingReasonandUploadFile(String reason, String fileName) throws InterruptedException
	{
		//Select dropdown = new Select(driver.findElement(By.id("CeaseReason")));
		Select dropdown = new Select(ceasedReasonDD);
		dropdown.selectByVisibleText(reason);
		
		if (reason.contentEquals("Informed Consent"))
		{
		
		wait.until(ExpectedConditions.elementToBeClickable(ceaseFileUpload));
		String file = fileFolderPath+fileName;
		
		//String filePath = System.getProperty("user.dir") + "/Upload/CeasingReason1.jpg";
		ceaseFileUpload.sendKeys(file);
		}
		}
	
	
	public void selectDeferReason(String reason) throws InterruptedException
	{
		Select dropdown = new Select(delayReasonDD);
		dropdown.selectByVisibleText(reason);
		
		/*if (reason.contentEquals("Informed Consent"))
		{
		
		wait.until(ExpectedConditions.elementToBeClickable(ceaseFileUpload));
		
		String filePath = System.getProperty("user.dir") + "/Upload/CeasingReason1.jpg";
		ceaseFileUpload.sendKeys(filePath);
		}*/
		}
	
	public void selectDeferDuration(String duration)
	{
		Select dropdown = new Select(delayDurationDD);
		dropdown.selectByVisibleText(duration);
	}
	
	public void chooseDeferFile(String fileName)
	{
		wait.until(ExpectedConditions.elementToBeClickable(deferFileUpload));
		String file = fileFolderPath+fileName;
		deferFileUpload.sendKeys(file);
	}
	
	public void selectReasonAndUploadFile(String reason,String duration, String fileName ) throws InterruptedException
	{
		selectDeferReason(reason);
		selectDeferDuration(duration);
		chooseDeferFile(fileName);
	}
	
	public NotificationLists clickOnCancelButton() throws InterruptedException
	{
		
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(ceasePatientCancelButton)).click();
		Thread.sleep(2000);
		return new NotificationLists(driver);
		
		}
	
	public NotificationLists clickOnDeferCeaseCancelButton() throws InterruptedException
	{
		
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(deferPatientCancelButton)).click();
		wait.until(ExpectedConditions.visibilityOf(notificationListHeader));
		//Thread.sleep(2000);
		return new NotificationLists(driver);
		
		}
	
	
	
	public DeferPopup clickOnSubmitButton() throws InterruptedException
	{
		
		try{
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(ceasePatientSubmitButton)).click();
		Thread.sleep(2000);
		//wait.until(ExpectedConditions.attributeContains(ceasePatientSubmitButton, "disabled", "disabled"));
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
		return new DeferPopup(driver);
		}
	
	public NotificationLists clickOnDeferCeaseSubmitButton() throws InterruptedException
	{
		
		try{
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(deferPatientSubmitButton)).click();
		//Thread.sleep(5000);
		//wait.until(ExpectedConditions.attributeContains(ceasePatientSubmitButton, "disabled", "disabled"));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cancelDefer")));
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
		return new NotificationLists(driver);
		}
	
	public String getNHSNoOfCeasePatient() {
			String NHSNo = ceaseNHSNumber.getText().replaceAll("\\s+","");
			System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
			  
		
		// TODO Auto-generated method stub
		return NHSNo;
	}
	
	public String getNHSNoOfDeferPatient() {
		String NHSNo = deferNHSNumber.getText().replaceAll("\\s+","");
		System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
		  
	
	// TODO Auto-generated method stub
	return NHSNo;
}
	
	public String getCeasePatientName() {
		String patientName = ceasedPatientName.getText();
		System.out.println("The Patient name on Cease Modal Window: "+patientName);
		  
	
	// TODO Auto-generated method stub
	return patientName;
}
	
	public String getDeferPatientName() {
		String patientName = deferPatientName.getText();
		System.out.println("The Patient name on Cease Modal Window: "+patientName);
		  
	
	// TODO Auto-generated method stub
	return patientName;
}

	 public Boolean VerifyCeasedConfirmNRL() {
		  boolean f0 = false;
       try
       {
       Thread.sleep(3000);
       //wait.until(ExpectedConditions.elementToBeClickable(submitarea)).click();
       //String Stylevalue = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/span")).getAttribute("style");
       String Stylevalue = ceasedTickMarkOnNRL.getAttribute("style");
       System.out.println(Stylevalue);
       if (Stylevalue.equalsIgnoreCase("display: block;"))
       {
                   f0 = true;        
               
             
       }
       else
       {
     	  System.out.println("Ceasing is not performed sucessfully due to some error");
       }
       }
       
       catch(Exception e)
       {
              System.out.println("Found Exception : " + e);
       }
       
       return f0;
	}

	 
	 public Boolean Deferoptions()
	 {
		 Boolean optionFlag = false;
		 List<String> options = getReasonForDefer();
		 
		 if(options.contains(DeferOption1) && options.contains(DeferOption2) && options.contains(DeferOption3))
			{
			 optionFlag = true;
			}
				 
		 return optionFlag;
		 
	 }
	 
	 public Boolean TickMark() {
		  boolean flagforTickMark = false;
       try
       {
       Thread.sleep(3000);
       //wait.until(ExpectedConditions.elementToBeClickable(submitarea)).click();
       String Stylevalue = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/span")).getAttribute("style");
       System.out.println(Stylevalue);
       if (Stylevalue.equalsIgnoreCase("display: block;"))
       {
       	flagforTickMark = true;        
               
                    
       
       }
       else
       {
     	  System.out.println("Ceasing is not performed sucessfully due to some error");
       }
       }
       
       catch(Exception e)
       {
              System.out.println("Found Exception : " + e);
       }
       
       return flagforTickMark;
	}
	 
	 public String getNHSNoOfCeasePatientforcallRecall(String InlineNhsNo) {
			System.out.println(InlineNhsNo);
			
		    String NHSNo = InlineNhsNo.replaceAll("\\s+","");
		    System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
		      

		// TODO Auto-generated method stub
		    return NHSNo;
			}
	 
	 public boolean SelectRowNumberOnResult(String InlineNhsNo) {
			boolean RowNumberOnResult = false;
			try{
				System.out.println(InlineNhsNo);
				WebElement table = driver.findElement(By.id("pcss-patient-cease-confirmation"));
				List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
				System.out.println("Rows: "+listOfRows.size());
				for (int i =1 ;i<(listOfRows.size());i++)
				{
					
					
					String ActualNHSNumber = driver.findElement(By.xpath("//*[@id='pcss-patient-cease-confirmation']/tbody/tr["+i+"]/td[1]")).getText();
					if (ActualNHSNumber.equalsIgnoreCase(InlineNhsNo))
					{
						
						RowNumberOnResult = true; 
						break;
						
						
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return RowNumberOnResult;
		}
	 
		public int nonRespondersPresentcount()
		{
			int NRLcount = 0;
			String NonRespCount = nonRespondersBadge.getText();
			NRLcount = Integer.parseInt(NonRespCount);
			//String PC = CurrentWeekPatientsToDo.getText();
			System.out.println("Non responders Count: "+NonRespCount);
			
					
			return NRLcount;
					
			//return new PNLPage(driver);
		}
		
		public int getSpecificRowNumberInTable(WebElement tableID, int ColumnNumber)
		{
			
		
			int rowNumber=1;
			
			List<WebElement> rows = tableID.findElements(By.tagName("tr"));
			int rowCount = rows.size();
			System.out.println(rowCount);
			
			for (int i=1; i<=rowCount;i++)

			{
				try{
				
					WebElement subCol = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr["+i+"]/td["+ColumnNumber+"]"));
					System.out.println(subCol.getText());
					String value = subCol.findElement(By.tagName("button")).getAttribute("disabled");
					System.out.println(value);
					System.out.println(subCol.findElement(By.tagName("button")).getAttribute("id"));
							if (value == null && rowNumber<=rowCount)
							{
				        	break;
							}
							else
								rowNumber++;
							
					}
				        catch (Exception e) {
				        	
				    	}
				
					
			       
			    } 
			
			if (rowNumber>rowCount)
			{
				System.out.println("No active records in table. It has return last row number.");
				return 0;
			}
				
				
			return rowNumber;
		}
		
		
		public int getRowNumberForCeaseDefer(String tableName, int ColumnNumber)
		{
			
		
			int rowNumber=1;
			
			List<WebElement> rows = driver.findElements(By.xpath("//table[@id='"+tableName+"']/tbody/tr"));
			int rowCount = rows.size();
			System.out.println(rowCount);
			
			for (int i=1; i<=rowCount;i++)

			{
				try{
				
					WebElement actCol = driver.findElement(By.xpath("//table[@id='pcss-patient-non-responders']/tbody/tr["+i+"]/td["+ColumnNumber+"]"));
					Select dropdown = new Select(actCol.findElement(By.id("ActionId")));
					WebElement option = dropdown.getFirstSelectedOption();
					String selectedOption = option.getText();
						if (selectedOption.contains("GP Call/Recall") && rowNumber<=rowCount)
							{
				        	break;
							}
							else
								rowNumber++;
							
					}
				        catch (Exception e) {
				        	
				    	}
				
					
			       
			    } 
			
			if (rowNumber>rowCount)
			{
				System.out.println("No records found in NRL which can be marked as GP Defer.");
				return 0;
			}
				
				
			return rowNumber;
		}
		
		public void selectGPDeferActionFromRecord(String tableName, String text,  int ColumnNumber) throws InterruptedException
		{
			//WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
			int activeRow = getRowNumberForCeaseDefer(tableName, ColumnNumber);
			if(activeRow>0)
			{
			
			WebElement column1 = driver.findElement(By.xpath("//table[@id='"+tableName+"']/tbody/tr["+activeRow+"]/td["+ColumnNumber+"]"));
					
			//WebElement column = nonRespondersTableAction;
			
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column1);
			
			//String fullname= ceasedPatientNameofFirstRecord.getText();
			
			Select dropdown = new Select(column1.findElement(By.id("ActionId")));
		
			
			WebElement option = dropdown.getFirstSelectedOption();
			System.out.println("The previous dropdown value selected: "+option.getText());
			
			dropdown.selectByVisibleText(text);
			Thread.sleep(3000);
			WebElement option1 = dropdown.getFirstSelectedOption();
			
			System.out.println("The dropdown value selected: "+option1.getText());
			
			
			}
			else
			{
				System.out.println("No Records found in table which can be marked as GP Defer.");
			}
		}

	
		
		public boolean checkActiveDeferRecord(String tableName, int columnNumber)
		{
			int rowNumber = 1;
			//boolean activeRecordFlag = false;
			List<WebElement> rows = driver.findElements(By.xpath("//table[@id='"+tableName+"']/tbody/tr"));
			int rowCount = rows.size();
			System.out.println(rowCount);
			
			for (int i=1; i<=rowCount;i++)

			{
				try{
				
					WebElement actCol = driver.findElement(By.xpath("//table[@id='"+tableName+"']/tbody/tr["+i+"]/td["+columnNumber+"]"));
					Select dropdown = new Select(actCol.findElement(By.id("ActionId")));
					WebElement option = dropdown.getFirstSelectedOption();
					String selectedOption = option.getText();
						if (selectedOption.contains("GP Call/Recall") && rowNumber<=rowCount)
							{
				        	break;
							}
							else
								rowNumber++;
							
					}
				        catch (Exception e) {
				        	
				    	}
				
					
			       
			    } 
			
			if (rowNumber>rowCount)
			{
				System.out.println("No records found in NRL which can be marked as GP Defer.");
				return false;
			}
				
				
			return true;
				
					
			       
			    } 
			

			
			
	

}
