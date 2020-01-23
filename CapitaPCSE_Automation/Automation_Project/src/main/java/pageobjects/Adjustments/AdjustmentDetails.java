package pageobjects.Adjustments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import utilities.ExcelUtilities;

public class AdjustmentDetails extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="Contractor")
	WebElement contractorTxtBox;
	
	@FindBy(id="BatchRefNoOrName")
	WebElement adjNameTxtBox;
	
	@FindBy(id="CreationFromDate")
	WebElement createdFromDate;
	
	@FindBy(id="CreationToDate")
	WebElement createdToDate;
	
	@FindBy(css="select#AdjustmentStatus")
	WebElement adjStatus;
	
	@FindBy(css="input.btn.btn-default")
	WebElement clearSearchBtn;
	
	@FindBy(css="input.btn.btn-success")
	WebElement searchBtn;

	public AdjustmentDetails(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 45);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public AdjustmentDetails searchBatch(int daysBeforeFromDate,int daysBeforeToDate) throws InterruptedException, ParseException{
		//String date= CommonFunctions.getTodayDate();
		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		date= CommonFunctions.getDateBeforeDays(daysBeforeToDate);
		String toDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		Support.enterDataInTextField(createdToDate, toDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentDetails(driver);
	}

	// while passing days count, daysBeforeToDate should be less than daysBeforeFromDate.
	public AdjustmentDetails searchBatch(int daysBeforeFromDate,int daysBeforeToDate,String batchStatus) throws InterruptedException, ParseException{

		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		date= CommonFunctions.getDateBeforeDays(daysBeforeToDate);
		String toDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		Support.enterDataInTextField(createdToDate, toDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(adjStatus));
		CommonFunctions.selectOptionFromDropDown(adjStatus, batchStatus);
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentDetails(driver);
	}
	
	public AdjustmentDetails searchBatch(int daysBeforeFromDate,String batchName) throws InterruptedException, ParseException{
		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(adjNameTxtBox));
		CommonFunctions.setText(adjNameTxtBox,batchName);
		selectFirstRecord("BatchRefNoOrName");
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentDetails(driver);
	}
	
	private void sortRecords() throws InterruptedException{
		List<WebElement> recordGrid= driver.findElements(By.xpath("//table[@id='tblAdjustmentDetailsRecord']//tbody/tr"));
		wait.until(ExpectedConditions.visibilityOfAllElements(recordGrid));
		
		WebElement batchNo= driver.findElement(By.xpath("//div[@class='dataTables_scrollHeadInner']//tr/th[1]"));
		scrolltoElement(driver, batchNo);
		wait.until(ExpectedConditions.visibilityOf(batchNo));
		wait.until(ExpectedConditions.elementToBeClickable(batchNo));
		batchNo.click();
		Thread.sleep(4000);
	}
	
	public AdjustmentBatch clickOnFirstRecordFromResult() {
		WebElement firstRecord= driver.findElement(By.xpath("//table[@id='tblAdjustmentDetailsRecord']//tbody/tr[1]/td[1]/a"));
		wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		firstRecord.click();
		return new AdjustmentBatch(driver);
	}
	
	private void selectFirstRecord(String startsWith) throws InterruptedException {
		WebElement firstRecord= driver.findElement(By.xpath("//li[starts-with(@id,'"+startsWith+"')]/a"));
		Thread.sleep(3000);
		firstRecord.click();
	}
	
	public void captureAdjBatchPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, contractorTxtBox);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}

}
