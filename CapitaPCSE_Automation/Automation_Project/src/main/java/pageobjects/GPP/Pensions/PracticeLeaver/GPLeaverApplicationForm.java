package pageobjects.GPP.Pensions.PracticeLeaver;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

public class GPLeaverApplicationForm extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="select#ddlApplicantId")
	WebElement selectApplicant;
	
	@FindBy(id="txtLeavingDate")
	WebElement leavingDate;
	
	@FindBy(id="confirmationCheckFinal")
	WebElement tickToConfirm;
	
	@FindBy(id="submitbtn")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divLeaveSubmission']/div/div/div[3]/div[2]/button")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='divLeaveSubmission']/div/div/div[3]/div[1]/button")
	WebElement cancelSubmit;
	
	public GPLeaverApplicationForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public GPLeaverApplicationForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on leaver entry form");
		return new GPLeaverApplicationForm(driver);
	}
	
	public GPLeaverApplicationForm clickOnCancelSubmit() throws InterruptedException {
		scrolltoElement(driver, cancelSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on cancel submit on leaver entry form");
		return new GPLeaverApplicationForm(driver);
	}
	
	public GPLeaverApplicationForm clickOnConfirmSubmit() throws InterruptedException {
		scrolltoElement(driver, confirmSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on confirm submit on leaver entry form");
		return new GPLeaverApplicationForm(driver);
	}

	public GPLeaverApplicationForm enterLeaverData(int colNumber,String environment,String refDataKey) throws ParseException {
		
		String strLeavingDateAfterDays= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "LeavingDateAfterDays", colNumber);
		int leavingDateAfterDays= Integer.parseInt(strLeavingDateAfterDays);
		
		String applicantName= ConfigurationData.getRefDataDetails(environment, refDataKey);
		String pensionSchemeNumber= ConfigurationData.getRefDataDetails(environment, "PensionEnrollmentNo");
		String finalApplicantName= applicantName+"("+pensionSchemeNumber+")";
		
		wait.until(ExpectedConditions.elementToBeClickable(selectApplicant));
		CommonFunctions.selectOptionFromDropDown(selectApplicant, finalApplicantName);
		
		String todayDate= CommonFunctions.getTodayDate();
		String strLeavingDate= CommonFunctions.Tomorrowdate(todayDate,leavingDateAfterDays,"dd/MM/yyyy");
		enterDataInTextField(leavingDate, strLeavingDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new GPLeaverApplicationForm(driver);
	}
	
	public String getApplicationRefNo(String key) {
		String appRefNo="";
		WebElement refNo= driver.findElement(By.xpath("//div[@id='dvLeaverForm']/div[3]/div[2]//tbody/tr/td[2]"));
		appRefNo= refNo.getText().toString();
		ExcelUtilities.setKeyValueByPosition("GPPPensionsTestData.xlsx", "ApplicationKeyMapping", appRefNo, key, "CLAIMID");
		return appRefNo;
	}

}
