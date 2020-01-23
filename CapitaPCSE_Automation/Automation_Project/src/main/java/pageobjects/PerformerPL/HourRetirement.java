package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
public class HourRetirement extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="LastDayofService")
	WebElement LastDayService;
	
	@FindBy(id="ReasonForLessDaysNotice")
	WebElement ResaonLastDayService;
	
	@FindBy(id="ReturnToServiceDate")
	WebElement ReturntoService;
	
	@FindBy(id="ApplyForNHSPensionScheme")
	WebElement NHSpension;
	
	@FindBy(name="Submit_Decision")
	WebElement Submit;
	
	@FindBy(xpath="//div[@class='popover confirmation fade top in']")
	WebElement Popupmessage;
	
	@FindBy(xpath="//a[@class='btn btn-danger']")
	WebElement Cancel_Submit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(id="PerformerMain")
	WebElement HomeMaintab;
	
	
	public HourRetirement(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}


	public HourRetirement FillHourRetirement() {
		try{
			String Date = helpers.CommonFunctions.getDate_UK();
			String LastServiceday = helpers.CommonFunctions.Tomorrowdate(Date , 2);
			String ReturnServiceday = helpers.CommonFunctions.Tomorrowdate(LastServiceday , 1);
			Thread.sleep(2000);
			
			wait.until(ExpectedConditions.elementToBeClickable(LastDayService)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(LastDayService)).sendKeys(LastServiceday);
			if (isAlertPresent()) {
				driver.switchTo().alert();
				driver.switchTo().alert().accept();
				wait.until(ExpectedConditions.elementToBeClickable(LastDayService)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(LastDayService)).sendKeys(LastServiceday);
				}
			wait.until(ExpectedConditions.elementToBeClickable(LastDayService)).sendKeys(Keys.TAB);
			
			wait.until(ExpectedConditions.elementToBeClickable(ResaonLastDayService)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(ResaonLastDayService)).sendKeys("Automation Purpose");
			
			
			
			wait.until(ExpectedConditions.elementToBeClickable(ReturntoService)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(ReturntoService)).sendKeys(ReturnServiceday);
			if (isAlertPresent()) {
				driver.switchTo().alert();
				driver.switchTo().alert().accept();
				wait.until(ExpectedConditions.elementToBeClickable(ReturntoService)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(ReturntoService)).sendKeys(ReturnServiceday);
				}
			
			
			wait.until(ExpectedConditions.elementToBeClickable(ReturntoService)).sendKeys(Keys.TAB);
			
			wait.until(ExpectedConditions.elementToBeClickable(NHSpension)).click();			
		}
		catch(Exception e)
		{
			System.out.println("The 24 Hours Retirement information is not filled properly");
		}
		return new HourRetirement(driver) ;
	}
	
	public boolean isAlertPresent() {
		try {
		driver.switchTo().alert();
		return true;
		} // try
		catch (Exception e) {
		return false;
		} // catch
		}
	
	public HourRetirement clickonSubmit() {
		try{
			scrolltoElement(driver, Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Submit);
	    	actions1.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Submit button is not clicked");
		}
		return new HourRetirement(driver) ;
	}

	public boolean verifypopupmessage() {
		boolean popupmessage = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//div[@class='popover confirmation fade top in']")).size() != 0;
			System.out.println(ispresent);
			//scrolltoElement(driver, Popupmessage);
			if(ispresent)
			{
				popupmessage = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("The Pop up message is not captured");
		}
		return popupmessage;
	}

	public HourRetirement clickonCancel_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Cancel_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Cancel_Submit);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new HourRetirement (driver);
	}
	
	public HourRetirement clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new HourRetirement (driver);
	}


	public HomeTab clickonHometab() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, HomeMaintab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(HomeMaintab);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Home tab is not clicked");
		}
		return new HomeTab(driver);
	}


	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, LastDayService);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

}
