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
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class ERRBOApplication extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	static String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";
	
	@FindBy(id="PensionDate")
	WebElement pensionDate;
	
	@FindBy(id="QuotationNumber")
	WebElement quotationNumber;
	
	@FindBy(css="input.form-control.fileupload")
	WebElement selectFileButton;
	
	@FindBy(xpath="//button[contains(@data-ajax-url,'UploadJoinerClaimDocs')]")
	WebElement browseButton;
	
	@FindBy(css="div[id*='confirmation']")
	WebElement confirmUploadWindow;
	
	@FindBy(css="a.btn.btn-success")
	WebElement confirmUpload;
	
	@FindBy(id="Years")
	WebElement years;
	
	@FindBy(id="Days")
	WebElement days;
	
	@FindBy(id="ErrboContributioneRate")
	WebElement contributionRate;
	
	@FindBy(id="ErrboAgreementStartdate")
	WebElement errboStartDate;
	
	@FindBy(id="ErrboAgreementEnddate")
	WebElement errboEndDate;
	
	@FindBy(css="select#EmployerName")
	WebElement employerName;
	
	@FindBy(id="TickToConfirmErrbo")
	WebElement tickToConfirm;
	
	@FindBy(id="submitbtn")
	WebElement submitButton;
	
	@FindBy(xpath="//div[@id='divSubmission']//div[@class='modal-content']")
	WebElement submitModalWindow;
	
	@FindBy(xpath="//div[@id='divSubmission']//button[@value='Confirm Submit']")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='divSubmission']//button[@class='btn btn-default']")
	WebElement cancelSubmit;
	
	@FindBy(css="button#btnErrboAgreement")
	WebElement endDateButton;
	
	@FindBy(css="button#ErrboAgreement")
	WebElement startDateButton;
	
	@FindBy(css="button#ErrboAgreement")
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
	
	@FindBy(xpath="//table[@class='table-Inline']//td[2]")
	WebElement refNo;
	
	public ERRBOApplication(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public ERRBOApplication enterERRBODetails(String file, String sheet, String colName,
			String environment) throws ParseException, InterruptedException {
		//Actions actions = new Actions(driver);
		String strYears= ExcelUtilities.getKeyValueByPosition(file, sheet, "PensionDateAfterYears", colName);
		String strContributionRate= ExcelUtilities.getKeyValueByPosition(file, sheet, "ContributionRate", colName);
		int afterYears= Integer.parseInt(strYears);
		int totalDays= afterYears*365;
		Date date= CommonFunctions.getDateAfterDays(totalDays);
		String strPensionDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, pensionDate);
		enterDataInTextField(pensionDate, strPensionDate, wait);
		Thread.sleep(1000);
		String ERRBONumber= CommonFunctions.generateRandomNo(6);
		scrolltoElement(driver, quotationNumber);
		enterDataInTextField(quotationNumber, ERRBONumber, wait);
		Thread.sleep(1000);
		scrolltoElement(driver, years);
		enterDataInTextField(years, "2", wait);
		enterDataInTextField(days, "2", wait);
		Thread.sleep(1000);
		scrolltoElement(driver, contributionRate);
		enterDataInTextField(contributionRate, strContributionRate, wait);
		Thread.sleep(1000);
		String strAfterDays= ExcelUtilities.getKeyValueByPosition(file, sheet, "StartAfterDays", colName);
		int days= Integer.parseInt(strAfterDays);
		date= CommonFunctions.getDateAfterDays(days);
		String strAgreementStartDate= CommonFunctions.convertDateToString(date,"M/dd/yyyy");
		scrolltoElement(driver, errboStartDate);
		
		selectDate(strAgreementStartDate,startDateButton);
		Thread.sleep(1000);
		date= CommonFunctions.getDateAfterDays(365);
		String strAgreementEndDate= CommonFunctions.convertDateToString(date,"M/dd/yyyy");
		
		scrolltoElement(driver, errboEndDate);
		selectDate(strAgreementEndDate,endDateButton);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(employerName));
		String strEmployerName= ConfigurationData.getRefDataDetails(environment, "GPPContractor");
		CommonFunctions.selectOptionFromDropDown(employerName, strEmployerName);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		uploadFile();
		return new ERRBOApplication(driver);
	}
	
	private void uploadFile() throws InterruptedException {
		
		scrolltoElement(driver, selectFileButton);
		wait.until(ExpectedConditions.elementToBeClickable(selectFileButton)).clear();
		CommonFunctions.Uploadfile(ERRBOApplication.filePath, driver);
		WebElement uploadWindow = driver.switchTo().activeElement();
		uploadWindow.sendKeys(ERRBOApplication.filePath);
		wait.until(ExpectedConditions.elementToBeClickable(browseButton)).click();
		if(confirmUploadWindow.isDisplayed()){
			Thread.sleep(1000);
			scrolltoElement(driver, confirmUpload);
			wait.until(ExpectedConditions.elementToBeClickable(confirmUpload)).click();
			Thread.sleep(3000);
		}
	}
	
	public ERRBOApplication clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button under ERRBO purchase form");
		return new ERRBOApplication(driver);
	}
	
	public ERRBOApplication clickOnCancelSubmit() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(submitModalWindow));
		if(submitModalWindow.isDisplayed()){
			scrolltoElement(driver, cancelSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel submit under ERRBO purchase form");
		}
		return new ERRBOApplication(driver);
	}
	
	public ERRBOApplication clickOnConfirmSubmit() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(submitModalWindow));
		if(submitModalWindow.isDisplayed()){
			scrolltoElement(driver, confirmSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on confirm submit under ERRBO purchase form");
		}
		return new ERRBOApplication(driver);
	}
	
	public String getApplicationRefNo() throws InterruptedException {
		Thread.sleep(2000);
		String strRefNo="";
		wait.until(ExpectedConditions.visibilityOf(refNo));
		strRefNo= refNo.getText().toString();
		System.out.println("ERRBO Ref document number is: "+strRefNo);
		return strRefNo;
	}
	
	public void captureERRBOEntrySnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
	
	private void selectDate(String pensionStartDate, WebElement element) throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(element);
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
