package pageobjects.GPP.Pensions.AVC;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.GPPHelpers;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class AVCPurchaseForm extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="Salaried/SOLO Employer")
	WebElement salariedBtn;
	
	@FindBy(id="Full time Locum Income")
	WebElement locumIncomeBtn;
	
	@FindBy(css="select#EmployerId")
	WebElement employerName;
	
	@FindBy(css="select#PensionSchemeId")
	WebElement pensionSchemeType;
	
	@FindBy(xpath="//div[@class='gpp-checkbox'][1]//input[@id='CoverTypeId']")
	WebElement selfBtn;
	
	@FindBy(xpath="//div[@class='gpp-checkbox'][2]//input[@id='CoverTypeId']")
	WebElement selfAndDependentBtn;
	
	@FindBy(xpath="//div[@class='gpp-checkbox'][1]//input[@id='PurchaseArrangementId']")
	WebElement installmentsBtn;
	
	@FindBy(xpath="//div[@class='gpp-checkbox'][2]//input[@id='PurchaseArrangementId']")
	WebElement lumpsumBtn;
	
	@FindBy(id="PurchaseAmount")
	WebElement purchaseAmt;
	
	@FindBy(id="PaymentPeriod")
	WebElement paymentPeriod;
	
	@FindBy(id="Monthlypayment")
	WebElement monthlyAmt;
	
	@FindBy(id="TotalAmount")
	WebElement totalAmt;
	
	@FindBy(id="PCStartDate")
	WebElement startDate;
	
	@FindBy(id="PCEndDate")
	WebElement endDate;
	
	@FindBy(id="TickConfirm")
	WebElement tickToConfirm;
	
	@FindBy(id="submitbtn")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divLeaveSubmission']//button[@class='btn btn-default']")
	WebElement cancelSubmit;
	
	@FindBy(xpath="//div[@id='divLeaveSubmission']//button[@class='btn btn-success']")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@class='AVCForm']//div[3]/div[1]//td[2]")
	WebElement refNo;
	
	@FindBy(xpath="//button[@id='btnPCStartDate']/em")
	WebElement DatePicker;
	
	@FindBy(xpath="//div[@class='datepicker-days']//table[@class='table-condensed']/thead//tr[2]/th[2]")
	WebElement Yearchangefield;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']/thead//tr[2]/th[2]")
	WebElement ActualYearoncalender;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']/thead//tr[2]/th[3]")
	WebElement ForwardYear;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']")
	WebElement MonthTable;
	
	@FindBy(xpath="//*[@class='datepicker-days']/table//tbody")
	WebElement DatePickerTable;
	
	public AVCPurchaseForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public AVCPurchaseForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button under AVC purchase form");
		return new AVCPurchaseForm(driver);
	}
	
	public AVCPurchaseForm clickOnCancelSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divLeaveSubmission']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, cancelSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel submit under AVC purchase form");
		}
		return new AVCPurchaseForm(driver);
	}
	
	public AVCPurchaseForm clickOnConfirmSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divLeaveSubmission']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, confirmSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on confirm submit under AVC purchase form");
		}
		return new AVCPurchaseForm(driver);
	}

	public AVCPurchaseForm enterDetailsForSalariedWithInstallment(String file, String sheet,String colName,String environment) throws ParseException, InterruptedException {
		Thread.sleep(3000);
		scrolltoElement(driver, salariedBtn);
		wait.until(ExpectedConditions.elementToBeClickable(salariedBtn)).click();
		wait.until(ExpectedConditions.elementToBeClickable(selfBtn)).click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(installmentsBtn)).click();;
		String strEmployerName= ConfigurationData.getRefDataDetails(environment, "GPPContractor");
		//ExcelUtilities.getKeyValueByPosition(file, sheet, "EmployerName", colName);
		CommonFunctions.selectOptionFromDropDown(employerName, strEmployerName);
		
		String strAdditionalAmt= ExcelUtilities.getKeyValueByPosition(file, sheet, "AdditionalAmt", colName);
		double additionalAmt= Double.parseDouble(strAdditionalAmt);
		additionalAmt= GPPHelpers.convertValueToDecimals(additionalAmt, 2);
		scrolltoElement(driver, purchaseAmt);
		Support.enterDataInTextField(purchaseAmt, strAdditionalAmt, wait);
		
		String strLengthOfPlan= ExcelUtilities.getKeyValueByPosition(file, sheet, "LengthOfPlan", colName);
		//int lengthOfPlan= Integer.parseInt(strLengthOfPlan);
		scrolltoElement(driver, paymentPeriod);
		Support.enterDataInTextField(paymentPeriod, strLengthOfPlan, wait);
		
		String strMonthlyAmt= ExcelUtilities.getKeyValueByPosition(file, sheet, "MonthlyContributionAmt", colName);
		double monthlyAmount= Double.parseDouble(strMonthlyAmt);
		monthlyAmount= GPPHelpers.convertValueToDecimals(monthlyAmount, 2);
		scrolltoElement(driver, monthlyAmt);
		Support.enterDataInTextField(monthlyAmt, strMonthlyAmt, wait);
		
		String strAfterDays= ExcelUtilities.getKeyValueByPosition(file, sheet, "StartAfterDays", colName);
		int days= Integer.parseInt(strAfterDays);
		Date date= CommonFunctions.getDateAfterDays(days);
		String pensionStartDate= CommonFunctions.convertDateToString(date,"M/dd/yyyy");
		scrolltoElement(driver, startDate);
		selectDate(pensionStartDate);
		//wait.until(ExpectedConditions.elementToBeClickable(startDate)).clear();
		//startDate.sendKeys(pensionStartDate);
		
		scrolltoElement(driver, tickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new AVCPurchaseForm(driver);
	}

	public String getApplicationRefNo() throws InterruptedException {
		Thread.sleep(2000);
		String strRefNo="";
		wait.until(ExpectedConditions.visibilityOf(refNo));
		strRefNo= refNo.getText().toString();
		System.out.println("AVC Ref document number is: "+strRefNo);
		return strRefNo;
	}
	
	public void captureAVCEntrySnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
	
	private void selectDate(String pensionStartDate) throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(DatePicker);
		action.click().build().perform();
		String date = helpers.CommonFunctions.getdate(pensionStartDate);
		String Month = helpers.CommonFunctions.getMonth(pensionStartDate);
		String Year = helpers.CommonFunctions.getYear(pensionStartDate);
		String MonthRequired = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx","Months",Month, 1);
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(Yearchangefield)).click();
		Thread.sleep(2000);
		String ActualYearondatepicker = ActualYearoncalender.getText();
		while(!ActualYearondatepicker.equalsIgnoreCase(Year))
		{
			wait.until(ExpectedConditions.elementToBeClickable(ForwardYear)).click();
			Thread.sleep(3000);
			ActualYearondatepicker = ActualYearoncalender.getText();
		}
		
		if(ActualYearondatepicker.equalsIgnoreCase(Year))
		{
			List<WebElement> MonthsCounts = (List<WebElement>) MonthTable.findElements(By.tagName("span"));
			int TotalMonths = MonthsCounts.size();
			System.out.println(TotalMonths);
			for (WebElement MonthsCount : MonthsCounts)
			{
				String spanclass = MonthsCount.getAttribute("class");
				if(spanclass.equalsIgnoreCase("month") ||spanclass.equalsIgnoreCase("month focused") )
				{
					if(MonthsCount.getText().equalsIgnoreCase(MonthRequired))
					{
						Actions action1 = new Actions(driver);
						action1.moveToElement(MonthsCount);
						action1.click().build().perform();
						Thread.sleep(2000);
						break;
					}
				}
			}
		}	
			
		if(DatePickerTable.isDisplayed())
		{
			List<WebElement> trCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("tr"));
			int TotalRows = trCount.size();
			System.out.println(TotalRows);
			for (int i = 1;i<=TotalRows; i++)
			{
				List<WebElement> tdCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("td"));
				//int Totalcolumns = tdCount.size();
				for(int j = 1;j<=7; j++)
				{
					WebElement Date1 = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]"));
					String ClassName = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]")).getAttribute("Class");
					if(ClassName.equalsIgnoreCase("day") && Date1.getText().equalsIgnoreCase(date))
					{
						Date1.click();
						Thread.sleep(3000);
						i = TotalRows + 1;
						break;
					}
				}	
	
			}
		}
	}		

}