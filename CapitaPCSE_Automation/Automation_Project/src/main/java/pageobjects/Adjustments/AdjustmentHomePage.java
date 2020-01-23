package pageobjects.Adjustments;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
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

public class AdjustmentHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="BatchRefNoORName")
	WebElement adjBatchTxtBox;
	
	@FindBy(id="RequestingOrganisationName")
	WebElement orgTxtBox;
	
	@FindBy(id="CreatedByName")
	WebElement createdByTxtBox;
	
	@FindBy(id="AmountFrom")
	WebElement amountFromTxtBox;
	
	@FindBy(id="AmountTo")
	WebElement amountToTxtBox;
	
	@FindBy(id="CreationFromDate")
	WebElement createdFromDate;
	
	@FindBy(id="CreationToDate")
	WebElement createdToDate;
	
	@FindBy(id="BatchStatus")
	WebElement status;
	
	@FindBy(css="input.btn.btn-default")
	WebElement clearSearchBtn;
	
	@FindBy(css="input.btn.btn-success")
	WebElement searchBtn;
	
	@FindBy(xpath="//div[@id='divSearchAdjustment']/form/div[2]/div/div[1]/input")
	WebElement viewAdjLink;
	
	@FindBy(css="input.btn.btn.btn-info")
	WebElement newAdjBtn;
	
	@FindBy(xpath="//div[@id='divSearchAdjustmentResult']//input[@class='btn btn-info']")
	WebElement applyButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/button")
	WebElement modalCancelButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/input")
	WebElement modalConfirmButton;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/input")
	WebElement windowOKButton;
	
	
	public AdjustmentHomePage(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}


	public AdjustmentBatch clickOnNewAdjustment() {
		System.out.println("Entered in Adjustment click method");
		scrolltoElement(driver, newAdjBtn);
		wait.until(ExpectedConditions.elementToBeClickable(newAdjBtn)).click();
		return new AdjustmentBatch(driver);
	}
	
	public AdjustmentHomePage clickOnApplyButton() throws InterruptedException {
		System.out.println("Entered in apply action method");
		Thread.sleep(3000);
		scrolltoElement(driver,applyButton );
		wait.until(ExpectedConditions.elementToBeClickable(applyButton)).click();
		return new AdjustmentHomePage(driver);
	}
	
	public AdjustmentHomePage confirmAction() throws InterruptedException {
		System.out.println("Entered in confirm method");
		Thread.sleep(3000);
		scrolltoElement(driver,modalConfirmButton );
		wait.until(ExpectedConditions.elementToBeClickable(modalConfirmButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on Confirm button under confirmation pop up.");
		return new AdjustmentHomePage(driver);
	}
	
	public AdjustmentHomePage cancelAction() throws InterruptedException {
		System.out.println("Entered in cancel method");
		Thread.sleep(3000);
		scrolltoElement(driver,modalCancelButton );
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on Cancel button under confirmation pop up.");
		return new AdjustmentHomePage(driver);
	}
	
	public AdjustmentHomePage clickOnOKButton() throws InterruptedException {
		System.out.println("Entered in confirm draft method");
		Thread.sleep(3000);
		scrolltoElement(driver,windowOKButton);
		wait.until(ExpectedConditions.elementToBeClickable(windowOKButton)).click();
		System.out.println("Clicked on OK button under confirmation pop up.");
		Thread.sleep(8000);
		return new AdjustmentHomePage(driver);
	}
	
	public AdjustmentHomePage searchBatch(int daysBeforeFromDate,int daysBeforeToDate) throws InterruptedException, ParseException{
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
		return new AdjustmentHomePage(driver);
	}

	// while passing days count, daysBeforeToDate should be less than daysBeforeFromDate.
	public AdjustmentHomePage searchBatch(int daysBeforeFromDate,int daysBeforeToDate,String batchStatus) throws InterruptedException, ParseException{

		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		date= CommonFunctions.getDateBeforeDays(daysBeforeToDate);
		String toDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		Support.enterDataInTextField(createdToDate, toDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(status));
		CommonFunctions.selectOptionFromDropDown(status, batchStatus);
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentHomePage(driver);
	}
	
	private void sortRecords() throws InterruptedException{
		List<WebElement> recordGrid= driver.findElements(By.xpath("//table[@id='tblAdjustmentSearchResults']//tbody/tr"));
		wait.until(ExpectedConditions.visibilityOfAllElements(recordGrid));
		
		WebElement batchNo= driver.findElement(By.xpath("//div[@class='dataTables_scrollHeadInner']//tr/th[1]"));
		scrolltoElement(driver, batchNo);
		wait.until(ExpectedConditions.visibilityOf(batchNo));
		wait.until(ExpectedConditions.elementToBeClickable(batchNo));
		batchNo.click();
		Thread.sleep(4000);
	}
	
	public void captureAdjBatchPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, adjBatchTxtBox);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}


	public AdjustmentBatch clickOnFirstRecordFromResult() throws InterruptedException {
		WebElement firstRecord= driver.findElement(By.xpath("//table[@id='tblAdjustmentSearchResults']//tbody/tr[1]/td[1]/a"));
		wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		Thread.sleep(4000);
		firstRecord.click();
		return new AdjustmentBatch(driver);
	}


	public AdjustmentHomePage changeStatusForMultipleAdjustments(List<String> actions, int recordsToUpdate, String key) throws InterruptedException, IOException {
		int actionsCount= actions.size();
		for(int i=1;i<=recordsToUpdate;i++){
			WebElement elmAction= driver.findElement(By.xpath("//table[@id='tblAdjustmentSearchResults']//tbody/tr["+i+"]/td[9]/select[contains(@id,'BatchList')]"));
			String action= actions.get(i%actionsCount);
			CommonFunctions.selectOptionFromDropDown(elmAction, action);
		}
		captureAdjBatchPortalSnap(key+"_Batch numbers with selected status");
		Thread.sleep(3000);
		return new AdjustmentHomePage(driver);
	}


	public AdjustmentHomePage changeStatusForAdjustment(String action, String note) throws InterruptedException, IOException {
		WebElement elmAction= driver.findElement(By.xpath("//table[@id='tblAdjustmentSearchResults']//tbody/tr[1]/td[9]/select[contains(@id,'BatchList')]"));
		CommonFunctions.selectOptionFromDropDown(elmAction, action);
		captureAdjBatchPortalSnap(note);
		return new AdjustmentHomePage(driver);
	}
	
	public AdjustmentDetails clickOnViewAdjLink() throws InterruptedException {
		System.out.println("Entered in view method");
		Thread.sleep(3000);
		scrolltoElement(driver,viewAdjLink);
		wait.until(ExpectedConditions.elementToBeClickable(viewAdjLink)).click();
		Thread.sleep(3000);
		System.out.println("Clicked on view link.");
		return new AdjustmentDetails(driver);
	}
	
	public AdjustmentHomePage searchBatch(int daysBeforeFromDate,String batchName) throws InterruptedException, ParseException{
		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(adjBatchTxtBox));
		CommonFunctions.setText(adjBatchTxtBox,batchName);
		selectFirstRecord("BatchRefNoORName");
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentHomePage(driver);
	}
	
	private void selectFirstRecord(String startsWith) throws InterruptedException {
		WebElement firstRecord= driver.findElement(By.xpath("//li[starts-with(@id,'"+startsWith+"')]/a"));
		Thread.sleep(3000);
		firstRecord.click();
	}
	
	public AdjustmentHomePage searchBatchWithStatus(int daysBeforeFromDate,String batchStatus) throws InterruptedException, ParseException{

		Date date= CommonFunctions.getDateBeforeDays(daysBeforeFromDate);
		
		String fromDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		scrolltoElement(driver, createdFromDate);
		Support.enterDataInTextField(createdFromDate, fromDate, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(status));
		CommonFunctions.selectOptionFromDropDown(status, batchStatus);
		
		wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
		Thread.sleep(6000);
		sortRecords();
		return new AdjustmentHomePage(driver);
	}


	public boolean verifyElementState() {
		boolean isApplyDisabled= false;
		List<WebElement> elements= Arrays.asList(applyButton);
		isApplyDisabled= getElementState(elements);
		return isApplyDisabled;
	}
	
	private boolean getElementState(List<WebElement> elements) {
		boolean isDisabled= false;
		int count=0;
		for(int i=0;i<elements.size();i++){
			WebElement element= elements.get(i);
			scrolltoElement(driver, element);
			if(!element.isEnabled()){
				count= count+1;
			}
		}
		if(elements.size()==count){
			isDisabled= true;
		}else{
			isDisabled= false;
		}
		return isDisabled;
	}


}
