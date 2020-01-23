package pageobjects.CS;

import java.io.IOException;
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

public class UploadedHPVRecords extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input.pcss-searchboxhistory.form-control")
	WebElement reportNo;
	
	@FindBy(id="selectyear")
	WebElement selectYear;

	@FindBy(id="selectmonth")
	WebElement selectMonth;
	
	@FindBy(xpath="//table[@id='hpvvaccinationhistorytable']//tbody/tr[1]/td[8]/a/span")
	WebElement csvButton;
	
	public UploadedHPVRecords(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public boolean searchHPVRecords() throws InterruptedException {
		boolean isSorted=false;
		String firstRecord= driver.findElement(By.xpath("//table[@id='hpvvaccinationhistorytable']//tbody/tr[1]/td[3]")).getText().toString();
		scrolltoElement(driver, selectYear);
		
		String year= CommonFunctions.getYear(CommonFunctions.getTodayDate());
		CommonFunctions.selectOptionFromDropDown(selectYear, year);

		CommonFunctions.selectOptionFromDropDownAtIndex(selectMonth, 1);
		Thread.sleep(2000);
		String newFirstRecord= driver.findElement(By.xpath("//table[@id='hpvvaccinationhistorytable']//tbody/tr[1]/td[3]")).getText().toString();
		if(firstRecord!=newFirstRecord){
			isSorted=true;
		}else{
			isSorted=false;
		}
		return isSorted;
	}
	
	public void captureHPVTabScreenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, reportNo);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	public UploadedHPVRecords downloadHPVFile() throws InterruptedException {
		Thread.sleep(4000);
		wait.until(ExpectedConditions.elementToBeClickable(csvButton));
		csvButton.click();
		Thread.sleep(4000);
		return new UploadedHPVRecords(driver);
	}

}
