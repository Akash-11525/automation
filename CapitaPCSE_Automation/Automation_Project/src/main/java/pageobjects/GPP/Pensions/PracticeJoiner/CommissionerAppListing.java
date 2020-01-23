package pageobjects.GPP.Pensions.PracticeJoiner;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;

public class CommissionerAppListing extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="JoinerLeaverApprovalFilter_GPName")
	WebElement GPSearchTxtBox;
	
	@FindBy(css="select#JoinerLeaverApprovalFilter_ApplicationTypeId")
	WebElement applicationType;
	
	@FindBy(css="select#JoinerLeaverApprovalFilter_StatusId")
	WebElement status;
	
	@FindBy(id="txtApplicationDate")
	WebElement applicationDate;
	
	@FindBy(id="btnJoinerLeaverSearch")
	WebElement searchBtn;
	
	public CommissionerAppListing(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public CommissionerAppListing searchApplication(String appType, String appstatus,int days) throws InterruptedException, ParseException {
		scrolltoElement(driver, applicationType);
		wait.until(ExpectedConditions.elementToBeClickable(applicationType));
		CommonFunctions.selectOptionFromDropDown(applicationType, appType);
		
		scrolltoElement(driver, status);
		wait.until(ExpectedConditions.elementToBeClickable(status));
		CommonFunctions.selectOptionFromDropDown(status, appstatus);
		
		scrolltoElement(driver, applicationDate);
		wait.until(ExpectedConditions.elementToBeClickable(applicationDate)).clear();
		Date futureDate= CommonFunctions.getDateBeforeDays(days);
		String finalDate= CommonFunctions.convertDateToString(futureDate, "dd/MM/yyyy");
		
		applicationDate.sendKeys(finalDate);
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(3000);
		
		return new CommissionerAppListing(driver);
	}

	public void sortApplicationNumber() throws InterruptedException {
		WebElement appHeader= driver.findElement(By.xpath("//table[@class='table GPPCI-table dataTable no-footer']//thead/tr[1]/th[7]"));
		appHeader.click();
		Thread.sleep(2000);
		appHeader.click();
	}

	public JoinerApprovalForm clickOnFirstRecord(String appRefNo) {
		WebElement elmAppRefNo= driver.findElement(By.xpath("//table[@id='tblJoinerLeaver']//tbody/tr[1]/td[7]/a"));
		String name= elmAppRefNo.getText().toString();
		if(name.equalsIgnoreCase(appRefNo)){
			elmAppRefNo.click();
		}
		return new JoinerApprovalForm(driver);
	}
	
	public void captureCommissionerListingSnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}

	public boolean isApplicationPresent(String appRefNo) {
		boolean isNoPresent=false;
		int rows= driver.findElements(By.xpath("//table[@id='tblJoinerLeaver']//tbody/tr")).size();
		for(int i=1;i<=rows;i++){
			WebElement elmAppRefNo= driver.findElement(By.xpath("//table[@id='tblJoinerLeaver']//tbody/tr["+i+"]/td[7]/a"));
			String name= elmAppRefNo.getText().toString();
			if(name.equalsIgnoreCase(appRefNo)){
				isNoPresent=true;
				break;
			}
		}
		return isNoPresent;
	}
	
}
