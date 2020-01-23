package pageobjects.CS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.*;

public class NotificationLists {

	WebDriver driver;
	WebDriverWait wait;

	
	@FindBy(css="table[id='pcss-patient-new-registration']")
	WebElement newRegTable;
	
	@FindBy(css="button[id='newRegistrationCSV']")
	WebElement newRegCSVBtn;

	@FindBy(xpath="//table[@id='pcss-patient-new-registration']/tbody")
	WebElement tblNewReg;
	
	@FindBy(id="newRegistrationsBadge")
	WebElement newRegBadge; 
	
	@FindBy(id="nonRespondersBadge")
	WebElement nonRespondersBadge; 
	
	@FindBy(xpath="//a[contains(@href,'reregistered')]")
	WebElement registrationTab;
	
	@FindBy(xpath="//a[contains(@href,'nonResponders')]")
	WebElement nonRespondersTab;
	
	@FindBy(linkText="Cease Confirmation")
	WebElement CeaseconfirmationLink;
	
	@FindBy(id="ceaseConfirmationsBadge")
	WebElement CeaseconfirmationLinkNumber;
	
	@FindBy(id="ceaseConfirmationCSV")
	WebElement CSVButton;
	
	@FindBy(css="button[id='ceaseConfirmationCSV']")
	WebElement ceaseConfirmationCSVBtn;
	
	@FindBy(xpath="//table[@id='pcss-patient-cease-confirmation']/tbody")
	WebElement ceasedConfirmationTable;
	
	@FindBy(xpath="//*[@id='nonResponders']/div[1]/div/div/div[1]/input")
	WebElement Searchbutton;
	
	public NotificationLists(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public NewRegistration clickRegTab()
	{
		wait.until(ExpectedConditions.elementToBeClickable(registrationTab)).click();
		
		return new NewRegistration(driver);
	}
	
	public NonResponders clickNonRespondersTab()
	{
		wait.until(ExpectedConditions.elementToBeClickable(nonRespondersTab)).click();
		
		return new NonResponders(driver);
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
	

	
	public boolean newRegPatients()
	{
		Boolean fl = false;
		String NewRegCount = newRegBadge.getText();
		//String PC = CurrentWeekPatientsToDo.getText();
		System.out.println("New Reg Count: "+NewRegCount);
				
		int count = Integer.parseInt(NewRegCount);
		System.out.println(count);
		
		if(count > 0)
			fl = true;
		
		return fl;
				
		//return new PNLPage(driver);
	}
	
	



	public int CalculateCeaseconfirmationNumnber() throws InterruptedException {
		
		
		Thread.sleep(3000);
		String StringCeaseconfrimationnumber = wait.until(ExpectedConditions.visibilityOf(CeaseconfirmationLinkNumber)).getText();	
	
		
		int Ceaseconfrimationnumber = Integer.parseInt(StringCeaseconfrimationnumber);
		
		return Ceaseconfrimationnumber;
	}

	public NotificationLists clickCeaseConfirmation() throws InterruptedException {
		
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(CeaseconfirmationLinkNumber)).click();
		Thread.sleep(2000);
		return new NotificationLists (driver);
	}

	public Boolean getColumnnameOnCeaseconfirmation() {
		// TODO Auto-
		boolean ColumnName = true;
		try 
		{
			
			String ExpectedColumnName[] = {"NHS Number","Patient Name","DOB","Patient Address","Last Test Due Date","Previous Call/Recall Status","Cease Request Date","Cease Approval Date","Acknowledge Receipt"," "};
			
			WebElement table = driver.findElement(By.id("pcss-patient-cease-confirmation"));
			List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
			List<WebElement> listOfCols = listOfRows.get(0).findElements(By.tagName("th"));
					//If first row is header row

			System.out.println("Columns: "+listOfCols.size());
			int i =0;
			for (WebElement we:listOfCols)
			{
				System.out.println(we.getText());
				System.out.println(ExpectedColumnName[i]);
				if (i==9){
					break;
				}
				if((we.getText()).equalsIgnoreCase(ExpectedColumnName[i]))
				{
				System.out.println("Expected Column name is matched with Actual Column Name:" +ExpectedColumnName[i]);	
				}
				else
				{
					ColumnName = false;
				}
				i++;
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ColumnName;
	}

	public NotificationLists Selectacknowledge(String CRMNHSNo) {

		try{
			System.out.println(CRMNHSNo);
			WebElement table = driver.findElement(By.id("pcss-patient-cease-confirmation"));
			List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
			System.out.println("Rows: "+listOfRows.size());
			for (int i =1 ;i<(listOfRows.size());i++)
			{
				
				
				String ActualNHSNumber = driver.findElement(By.xpath("//*[@id='pcss-patient-cease-confirmation']/tbody/tr["+i+"]/td[1]")).getText();
				if (ActualNHSNumber.equalsIgnoreCase(CRMNHSNo))
				{
					  WebElement Dropdownfocus = driver.findElement(By.xpath("//*[@id='pcss-patient-cease-confirmation']/tbody/tr["+i+"]/td[9]"));
					Select dropdown = new Select(Dropdownfocus.findElement(By.id("CeaseSelect")));
					dropdown.selectByVisibleText("Acknowledge");
					driver.findElement(By.xpath("//*[@id='pcss-patient-cease-confirmation']/tbody/tr["+i+"]/td[10]")).click();
					break;
					
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new NotificationLists(driver);
	}
	
	
	

	public NotificationLists clickONCSV() {
		wait.until(ExpectedConditions.elementToBeClickable(CSVButton)).click();
		return null;
	}
	
	public NotificationLists clickOnCeaseConfirmationCSVButton() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(ceaseConfirmationCSVBtn)).click();
		Thread.sleep(10000);
		return new NotificationLists(driver);
	}
	
	public int SelectRowNumberOnResult(String InlineNhsNo) {
		int RowNumberOnResult = 0;
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
					
					RowNumberOnResult = i; 
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

	public int getresultonCeaseconfirmation(int i) {
		//boolean Resultdata = false;
		int count = 0;
		//  DateFormat formatter = null;
	      //  Date convertedDate = null;


		try 
		{
			System.out.println(i);
			String NHSNumberOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "NHSNumber");
			System.out.println(NHSNumberOut);
			ArrayList<String> mylist = new ArrayList<String>();
			mylist.add(NHSNumberOut);
			String FullNameOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "FullName");
			System.out.println(FullNameOut);			
			mylist.add(FullNameOut);
			String DOBOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "DateofBirth");
			Date d = CommonFunctions.convertStringtoCalDate(DOBOut,"MM/dd/yyyy");
			String ExpectedDOBOut = CommonFunctions.convertDateToString(d, "dd-MM-yyyy");
			System.out.println(ExpectedDOBOut);
			mylist.add(ExpectedDOBOut);
			String AddressOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "Address");
			System.out.println(AddressOut);			
			mylist.add(AddressOut);
			String TestDuedateOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "TestDueDate");
			System.out.println(TestDuedateOut);			
			Date d1 = CommonFunctions.convertStringtoCalDate(TestDuedateOut,"MM/dd/yyyy");
			String ExpectedTestDuedateOut = CommonFunctions.convertDateToString(d1, "dd-MM-yyyy");
			System.out.println(ExpectedTestDuedateOut);
			mylist.add(ExpectedTestDuedateOut);
			String PreCallRecallstatusOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "Previouscallrecall");
			System.out.println(PreCallRecallstatusOut);			
			mylist.add(PreCallRecallstatusOut);
			String CeaseRequestOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "CeaseRequestdate");
			System.out.println(CeaseRequestOut);
			Date d2 = CommonFunctions.convertStringtoCalDate(CeaseRequestOut,"MM/dd/yyyy");
			String ExpectedCeaseRequestOut = CommonFunctions.convertDateToString(d2, "dd-MM-yyyy");
			System.out.println(ExpectedCeaseRequestOut);
			mylist.add(ExpectedCeaseRequestOut);
			String CeaseApproveOut = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTOUTDATA.xlsx", "Contact", "CeaseApprovedate");
			System.out.println(CeaseApproveOut);
			Date d3 = CommonFunctions.convertStringtoCalDate(CeaseApproveOut,"MM/dd/yyyy");
			String ExpectedCeaseApproveOut = CommonFunctions.convertDateToString(d3, "dd-MM-yyyy");
			System.out.println(ExpectedCeaseApproveOut);
			mylist.add(ExpectedCeaseApproveOut);
			
			int j = 1;
			
			for (String ExpectedResult:mylist)
			{
				
				System.out.println("The Value from Excel" +ExpectedResult);
				WebElement table = driver.findElement(By.id("pcss-patient-cease-confirmation"));
				
				String ActualValue = driver.findElement(By.xpath("//*[@id='pcss-patient-cease-confirmation']/tbody/tr["+i+"]/td["+j+"]")).getText();
				System.out.println("The value from portal" +ActualValue);
				if (ExpectedResult.contains(ActualValue))
				{
					count = 0;
				}
				else 
				{
					
					count++;
				}
				
				j++;
				
				
			}
		}
		// TODO Auto-generated method stub
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return count;
	}
	


	public boolean VerifydecraseofNRL(String  strNRLcountexpected,String strNRLcountafterdefer) {
		// TODO Auto-generated method stub
		boolean VerifyNRLcount = false;
		if (strNRLcountexpected.equalsIgnoreCase(strNRLcountafterdefer))
		{
			VerifyNRLcount = true;
		}
		return VerifyNRLcount;
	}
	
	public boolean ceaseConfirmationListPresent()
	{
		Boolean fl = false;
		String ceaseConfirmCount = CeaseconfirmationLinkNumber.getText();
		//String PC = CurrentWeekPatientsToDo.getText();
		System.out.println(ceaseConfirmCount);
			
		int count = Integer.parseInt(ceaseConfirmCount);
		System.out.println(count);
		
		if(count > 0)
			fl = true;
		
		return fl;
				
		//return new PNLPage(driver);
	}
	
	public List<ArrayList<String>> getCeasedConfirmationData()
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
		 colnums.add(8);

		tblData = UITableDataSupport.getSpecificColumnDataInTable(ceasedConfirmationTable,colnums );
		
		
		
		return tblData;
	}

	public NotificationLists EnterpatientonSearch(String nHSNumberForPatientDetail) {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Searchbutton)).click();
			Searchbutton.sendKeys(nHSNumberForPatientDetail);
			Searchbutton.sendKeys(Keys.ENTER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new NotificationLists(driver);
	}
	
	}








