package pageobjects.GPP.Pensions.SalaryChange;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;


public class GPSalaryChangeForm extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="select#GPListDropDown")
	WebElement applicantList;
	
	@FindBy(id="txtNewIncomeStartDate")
	WebElement incomeStatDate;
	
	@FindBy(id="txtEstimatedNewPensionableIncome")
	WebElement pensionIncome;
	
	@FindBy(id="CheckToConfirm")
	WebElement tickToConfirm;
	
	@FindBy(css="button#submitbtn")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divConfSalaryChange']//div[@class='modal-content']")
	WebElement confirmSubmitWindow;
	
	@FindBy(css="input#confirmSubmitBtn")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='divConfSalaryChange']//div[@class='modal-footer']//button")
	WebElement cancelSubmit;
	
	public GPSalaryChangeForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public GPSalaryChangeForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on Salary Change form");
		return new GPSalaryChangeForm(driver);
	}
	
	public GPSalaryChangeForm clickOnCancelSubmit() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(confirmSubmitWindow));
		if(confirmSubmitWindow.isDisplayed()){
			scrolltoElement(driver, cancelSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel submit on Salary Change form");
		}
		return new GPSalaryChangeForm(driver);
	}
	
	public GPSalaryChangeForm clickOnConfirmSubmit() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(confirmSubmitWindow));
		if(confirmSubmitWindow.isDisplayed()){
			scrolltoElement(driver, confirmSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on confirm submit on Salary Change form");
		}
		return new GPSalaryChangeForm(driver);
	}

	public GPSalaryChangeForm enterSalaryChangeDetails(String file, String sheet, String colName,
			String environment, String refDataKey) throws ParseException, InterruptedException {
		String applicantName= ConfigurationData.getRefDataDetails(environment, refDataKey);
		String pensionSchemeNumber= ConfigurationData.getRefDataDetails(environment, "PensionEnrollmentNo");
		String finalApplicantName= applicantName+" "+"("+pensionSchemeNumber+")";
		String pensionableIncome= ExcelUtilities.getKeyValueByPosition(file, sheet, "PensionableIncome", colName);
		wait.until(ExpectedConditions.elementToBeClickable(applicantList));
		CommonFunctions.selectOptionFromDropDown(applicantList, finalApplicantName);
		Thread.sleep(1000);
		String strMonthsToAdd= ExcelUtilities.getKeyValueByPosition(file, sheet, "MonthsToAdd", colName);
		int monthsToAdd= Integer.parseInt(strMonthsToAdd);
		Thread.sleep(1000);
		String fromDate= CommonFunctions.getFirstDayOfMonth("dd/MM/yyyy", monthsToAdd);
		enterDataInTextField(incomeStatDate, fromDate, wait);
		Thread.sleep(1000);
		enterDataInTextField(pensionIncome, pensionableIncome, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new GPSalaryChangeForm(driver);
	}
}
