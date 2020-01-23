package pageobjects.OP;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
//import testscripts.OP_StatementDetails;


public class OPStatement extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (css="button[class='btn btn-success']")
	WebElement searchButton;
	
	@FindBy(id="StatementFromDate")
	WebElement fromDateField;
	
	@FindBy(id="NotificationPopupContainer")
	WebElement notificationPopup;
	
	@FindBy(css="div[id='NotificationPopupContainerMsg']")
	WebElement notificationTxt;
	
	@FindBy(id="BtnNotificationPopup")
	WebElement notificationPopupBtn;
	
	@FindBy(xpath="//table[@id='StatementDataTable']/tbody/tr[1]/td[1]/a")
	WebElement firstRecordLinkStatementTable;
	
	@FindBy(id="StatementToDate")
	WebElement toDateField;
	
	public OPStatement (WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}
	
	public void enterFromdate(int days) throws InterruptedException, ParseException
	{
		wait.until(ExpectedConditions.elementToBeClickable(fromDateField)).click();
		fromDateField.clear();
		Date dt = CommonFunctions.getDateBeforeDays(days);
		String fromDate = CommonFunctions.convertDateToString(dt, "dd/MM/yyyy");
		System.out.println(fromDate);
		fromDateField.sendKeys(fromDate);
		fromDateField.sendKeys(Keys.TAB);
		Thread.sleep(2000);
	}
	
	public void clickOnSearchButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
	}
	
	public void OPStatementSnaps(String note) throws InterruptedException, IOException
	{
		Thread.sleep(2000);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}
	
	public String getNotificationText(String note) throws InterruptedException, IOException{
		Thread.sleep(2000);
		String txt = null;
		if(notificationPopup.isDisplayed())
		{
			txt = notificationTxt.getText();
			OPStatementSnaps(note);
			notificationPopupBtn.click();
			
		}
		
		return txt;
	}
	
	public OP_StatementDetails clickOnStatementRefLink(String environment,String contCodeKey, String contNameKey) throws InterruptedException
	{
		Thread.sleep(2000);
		String strContractorCode= ConfigurationData.getRefDataDetails(environment, contCodeKey);
		String strContractorName= ConfigurationData.getRefDataDetails(environment, contNameKey);
		List<WebElement> records= driver.findElements(By.xpath("//table[@id='StatementDataTable']//tbody/tr"));
		wait.until(ExpectedConditions.visibilityOfAllElements(records));
		int recodCount= records.size();
		for(int i=1;i<=recodCount;i++){
			WebElement contractorCode= driver.findElement(By.xpath("//table[@id='StatementDataTable']//tbody/tr["+i+"]/td[2]"));
			wait.until(ExpectedConditions.visibilityOf(contractorCode));
			String actualContCode= contractorCode.getText().toString().trim();
			WebElement contractorName= driver.findElement(By.xpath("//table[@id='StatementDataTable']//tbody/tr["+i+"]/td[3]"));
			wait.until(ExpectedConditions.visibilityOf(contractorName));
			String actualContName= contractorName.getText().toString().trim();
			if((strContractorCode.equalsIgnoreCase(actualContCode))&&(strContractorName.equalsIgnoreCase(actualContName))){
				WebElement recordLink= driver.findElement(By.xpath("//table[@id='StatementDataTable']//tbody/tr["+i+"]/td[1]/a"));
				wait.until(ExpectedConditions.visibilityOf(recordLink));
				scrolltoElement(driver, recordLink);
				recordLink.click();
				Thread.sleep(5000);
				break;
			}
		}
		/*wait.until(ExpectedConditions.elementToBeClickable(firstRecordLinkStatementTable));
		firstRecordLinkStatementTable.click();*/
		return new OP_StatementDetails(driver);
	}

	public OPStatement enterFilterData() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(fromDateField)).click();
		fromDateField.clear();
		String date= CommonFunctions.getTodayDate();
		System.out.println(date);
		fromDateField.sendKeys(date);
		fromDateField.sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.elementToBeClickable(toDateField)).click();
		toDateField.clear();
		toDateField.sendKeys(date);
		toDateField.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		clickOnSearchButton();
		Thread.sleep(5000);
		Support.waitTillProcessing(driver);
		return new OPStatement(driver);
	}
	
	

}
