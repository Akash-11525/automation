package pageobjects.CS;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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

public class UploadedColposcopyRecords  extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input.pcss-searchboxhistory.form-control")
	WebElement reportNo;
	
	@FindBy(id="selectyear")
	WebElement selectYear;

	@FindBy(id="selectmonth")
	WebElement selectMonth;
	
	@FindBy(xpath="//table[@id='coloscopyhistorytable']//tbody/tr[1]/td[7]/a/span")
	WebElement csvButton;
	
	public UploadedColposcopyRecords(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public boolean searchColposcopyRecords() throws InterruptedException {
		boolean isSorted=false;
		String firstRecord= driver.findElement(By.xpath("//table[@id='coloscopyhistorytable']//tbody/tr[1]/td[3]")).getText().toString();
		scrolltoElement(driver, selectYear);
		
		String year= CommonFunctions.getYear(CommonFunctions.getTodayDate());
		CommonFunctions.selectOptionFromDropDown(selectYear, year);
		
/*		String month= getCurrentMonthName();
				//CommonFunctions.getMonth(CommonFunctions.getTodayDate());
		CommonFunctions.selectOptionFromDropDown(selectMonth, month);
		
		return new UploadedColposcopyRecords(driver);*/

		CommonFunctions.selectOptionFromDropDownAtIndex(selectMonth, 1);
		Thread.sleep(2000);
		String newFirstRecord= driver.findElement(By.xpath("//table[@id='coloscopyhistorytable']//tbody/tr[1]/td[3]")).getText().toString();
		if(firstRecord!=newFirstRecord){
			isSorted=true;
		}else{
			isSorted=false;
		}
		return isSorted;
	}

	public void captureColposcopyScreenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, reportNo);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	public String getCurrentMonthName(){
		String month;
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int monthIndex = cal.get(Calendar.MONTH); // 1
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		month= monthNames[monthIndex];
		
		System.out.println("Month is : "+month);
		return month;

	}

	public boolean verifyTabDetails(Integer integer) throws InterruptedException{
		CommonFunctions.selectOptionFromDropDownAtIndex(selectMonth, 0);
		Thread.sleep(2000);
		scrolltoElement(driver, reportNo);
		boolean verified=false;
		int verifiedCount=0;
		String todayDate =convertTodayDateFormat("dd/MM/yyyy");
		
		//String date= CommonFunctions.convertDateToString(todayDate, "dd-mm-yyyy");
		
		String actualCount= integer.toString();
		
		String userName= driver.findElement(By.xpath("//div[@class='container']/ul/li[1]")).getText().toString().trim();
		
		List<String> expectedData= Arrays.asList(todayDate,actualCount,userName);
		
		for(int i=0;i<expectedData.size();i++){
			
			String expText= expectedData.get(i);
			for(int j=4;j<=6;j++){
				WebElement rowDiv= driver.findElement(By.xpath("//table[@id='coloscopyhistorytable']//tbody/tr[1]/td["+j+"]"));
				String actualText= rowDiv.getText().toString();
				if(expText.equalsIgnoreCase(actualText)){
					verifiedCount= verifiedCount+1;
					break;
				}
			}
		}
		
		if(expectedData.size()==verifiedCount){
			verified= true;
		}else{
			verified= false;
		}
		
		return verified;
	}
	
	public static String convertTodayDateFormat(String dateFormat){
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		String format = formatter.format(date);
        System.out.println("Date is converted to given format- "+format);
        return format;
	}

	public UploadedColposcopyRecords downloadColposcopyFile() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(csvButton));
		csvButton.click();
		Thread.sleep(7000);
		return new UploadedColposcopyRecords(driver);
	}

	
	
}
