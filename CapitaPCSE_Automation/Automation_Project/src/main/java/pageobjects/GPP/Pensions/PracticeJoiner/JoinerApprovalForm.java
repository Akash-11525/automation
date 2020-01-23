package pageobjects.GPP.Pensions.PracticeJoiner;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import helpers.CommonFunctions;
import helpers.Support;
import utilities.ExcelUtilities;

public class JoinerApprovalForm extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="TickConfirm")
	WebElement tickToConfirm;
	
	@FindBy(xpath="//div[@id='divApproveBtn']/div[2]/div[2]/button")
	WebElement approveBtn;
	
	@FindBy(xpath="//div[@id='divApproveBtn']/div[2]/div[1]/button")
	WebElement rejectBtn;
	
	@FindBy(id="btnConfirmDate")
	WebElement joiningDateConfirmWindow;
	
	@FindBy(xpath="//div[@id='divAction']/div[2]/button[2]")
	WebElement submitBtn;
	
	@FindBy(id="submitbtn")
	WebElement modalConfirmSubmit;
	
	@FindBy(xpath="//*[@id='divJoinerSubmission']/div/div/div[3]/div[1]/button")
	WebElement cancelSubmit;
	
	//date selection
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
	
	@FindBy(xpath="//button[@id='btnApprovedJoiningDate']/em")
	WebElement DatePicker;
	
	public JoinerApprovalForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public JoinerApprovalForm performAction(String action,int colNumber) throws InterruptedException, ParseException {
		String strJoiningDateAfterDays= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "JoiningDateAfterDays", colNumber);
		int joiningDateAfterDays= Integer.parseInt(strJoiningDateAfterDays);
		switch(action){
		
		case "Approve":
			scrolltoElement(driver, approveBtn);
			wait.until(ExpectedConditions.elementToBeClickable(approveBtn)).click();
			//WebElement approvedDate= driver.findElement(By.id("txtApprovedJoiningDate"));
			String appliedDate= Support.getValueByJavaScript(driver, "txtAppliedJoiningDate");
			appliedDate= CommonFunctions.Tomorrowdate(appliedDate, joiningDateAfterDays, "dd/MM/yyyy");
			Date date= CommonFunctions.convertStringtoCalDate(appliedDate, "dd/MM/yyyy");
			appliedDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
			selectDate(appliedDate);
			boolean flag= false;
			int count=0;
			while(!flag){
				System.out.println("Entered in loop for count: "+count);
				try{
					if(joiningDateConfirmWindow.isDisplayed()){
						wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
						Thread.sleep(2000);
						flag= true;
						System.out.println("Window has appeared after count: "+count);
					}
					count++;
					//flag= joiningDateConfirmWindow.isDisplayed();
					System.out.println("Next iteration in loop for count: "+count);
				}catch(Exception e){
					System.out.println("Confirmation window is not displayed");
				}
			}
			/*try{
				if(joiningDateConfirmWindow.isDisplayed()){
					wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
					Thread.sleep(2000);
				}
			}catch(Exception e){
				System.out.println("Confirmation window is not displayed");
			}*/
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
			break;
		case "Reject":
			wait.until(ExpectedConditions.elementToBeClickable(rejectBtn)).click();
			WebElement reason= driver.findElement(By.id("ReasonForRejection"));
			enterDataInTextField(reason, "Rejected", wait);
			wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
			break;
		default:
			Assert.fail(action+" action is missing.");
		}
		return new JoinerApprovalForm(driver);
	}
	
	private void selectDate(String appliedDate) throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(DatePicker);
		action.click().build().perform();
		String date = helpers.CommonFunctions.getdate(appliedDate);
		String Month = helpers.CommonFunctions.getMonth(appliedDate);
		String Year = helpers.CommonFunctions.getYear(appliedDate);
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

	public JoinerApprovalForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on joiner approval form");
		return new JoinerApprovalForm(driver);
	}
	
	public JoinerApprovalForm clickOnCancelSubmit() throws InterruptedException {
		scrolltoElement(driver, cancelSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on cancel submit on joiner approval form");
		return new JoinerApprovalForm(driver);
	}
	
	public CommissionerAppListing clickOnConfirmSubmit() throws InterruptedException {
		scrolltoElement(driver, modalConfirmSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(modalConfirmSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on confirm submit on joiner approval form");
		return new CommissionerAppListing(driver);
	}
	
	public JoinerApprovalForm performAction(String action,String file, String sheet,String colName) throws InterruptedException, ParseException {
		String strJoiningDateAfterDays= ExcelUtilities.getKeyValueByPosition(file, sheet, "JoiningDateAfterDays", colName);
		int joiningDateAfterDays= Integer.parseInt(strJoiningDateAfterDays);
		switch(action){
		
		case "Approve":
			scrolltoElement(driver, approveBtn);
			wait.until(ExpectedConditions.elementToBeClickable(approveBtn)).click();
			String appliedDate= Support.getValueByJavaScript(driver, "txtAppliedJoiningDate");
			appliedDate= CommonFunctions.Tomorrowdate(appliedDate, joiningDateAfterDays, "dd/MM/yyyy");
			Date date= CommonFunctions.convertStringtoCalDate(appliedDate, "dd/MM/yyyy");
			appliedDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
			selectDate(appliedDate);
			boolean flag= false;
			int count=0;
			while(!flag){
				System.out.println("Entered in loop for count: "+count);
				try{
					if(joiningDateConfirmWindow.isDisplayed()){
						wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
						Thread.sleep(2000);
						flag= true;
						System.out.println("Window has appeared after count: "+count);
					}
					count++;
					//flag= joiningDateConfirmWindow.isDisplayed();
					System.out.println("Next iteration in loop for count: "+count);
				}catch(Exception e){
					System.out.println("Confirmation window is not displayed");
				}
			}
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
			break;
		case "Reject":
			wait.until(ExpectedConditions.elementToBeClickable(rejectBtn)).click();
			WebElement reason= driver.findElement(By.id("ReasonForRejection"));
			enterDataInTextField(reason, "Rejected", wait);
			wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
			break;
		default:
			Assert.fail(action+" action is missing.");
		}
		return new JoinerApprovalForm(driver);
	}

}
