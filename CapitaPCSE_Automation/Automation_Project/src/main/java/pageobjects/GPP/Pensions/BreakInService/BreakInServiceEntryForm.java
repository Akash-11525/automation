package pageobjects.GPP.Pensions.BreakInService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class BreakInServiceEntryForm extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="confirmationCheckFinal")
	WebElement isConfirm;
	
	@FindBy(id="txtServiceStartDate")
	WebElement serviceStartDate;
	
	@FindBy(id="txtServiceEndDate")
	WebElement serviceEndDate;
	
	@FindBy(id="submitbtn")
	WebElement submitBtn;
	
	@FindBy(css="input.btn.btn-success")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='divConfSubmitBrkInServic']/div/div/div[3]/div[1]/button")
	WebElement cancelSubmit;
	
	@FindBy(xpath="//div[@id='dvBrkInServic']//td[@id='appRefNo']")
	WebElement applicationRefNo;
	
	public BreakInServiceEntryForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public BreakInServiceEntryForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on break in service entry form");
		return new BreakInServiceEntryForm(driver);
	}
	
	public BreakInServiceEntryForm clickOnCancelSubmit() throws InterruptedException {
		scrolltoElement(driver, cancelSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on cancel submit on break in service entry form");
		return new BreakInServiceEntryForm(driver);
	}
	
	public BreakInServiceEntryForm clickOnConfirmSubmit() throws InterruptedException {
		scrolltoElement(driver, confirmSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on confirm submit on break in service entry form");
		return new BreakInServiceEntryForm(driver);
	}

	public List<String> enterBreakInServiceDataWithoutEndDate() throws ParseException {
		List<String> dates= new ArrayList<String>();
		String strBreakInServiceStartAfterDays = ConfigurationData.BreakInServiceStartAfterDays;
		int BreakInServiceStartAfterDays= Integer.parseInt(strBreakInServiceStartAfterDays);
		scrolltoElement(driver,isConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(isConfirm)).click();
		
		Date date= CommonFunctions.getDateAfterDays(BreakInServiceStartAfterDays);
		String startDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		enterDataInTextField(serviceStartDate, startDate, wait);
		dates.add(startDate);
		
		String endDate= CommonFunctions.getFinYearLastDate("dd/MM/yyyy");
		dates.add(endDate);
		return dates;
	}
	
	public List<String> enterBreakInServiceDataWithEndDate() throws ParseException {
		List<String> dates= new ArrayList<String>();
		String strBreakInServiceStartAfterDays = ConfigurationData.BreakInServiceStartAfterDays;
		String strBreakInServiceEndAfterDays = ConfigurationData.BreakInServiceEndAfterDays;
		int BreakInServiceStartAfterDays= Integer.parseInt(strBreakInServiceStartAfterDays);
		int BreakInServiceEndAfterDays= Integer.parseInt(strBreakInServiceEndAfterDays);
		scrolltoElement(driver,isConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(isConfirm)).click();
		
		Date date= CommonFunctions.getDateAfterDays(BreakInServiceStartAfterDays);
		String startDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		enterDataInTextField(serviceStartDate, startDate, wait);
		dates.add(startDate);
		
		date= CommonFunctions.getDateAfterDays(BreakInServiceEndAfterDays);
		String endDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		enterDataInTextField(serviceEndDate, endDate, wait);
		dates.add(endDate);
		return dates;
	}

	public String getApplicationRefNo() throws InterruptedException {
		String appRefNo="";
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOf(applicationRefNo));
		appRefNo= applicationRefNo.getText().toString();
		return appRefNo;
	}

}
