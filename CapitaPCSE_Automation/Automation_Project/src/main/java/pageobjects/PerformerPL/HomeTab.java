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
public class HomeTab extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="LastDayofService")
	WebElement LastDayService;
	
	@FindBy(id="ChangeHistory")
	WebElement ChangeHistorybutton;
	
	@FindBy(xpath="//table[@id='tblChangeHistorytable']//tr[1]//td[1]/a")
	WebElement RefNOOnChangeHistory;
	
	@FindBy(xpath="//table[@id='tblChangeHistorytable']")
	WebElement ResultChangeHistoryTable;
	
	
	public HomeTab(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public HomeTab clickonChangeHistory() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, ChangeHistorybutton);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ChangeHistorybutton);
	    	actions1.doubleClick().build().perform();	
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Change History button not clicked");
		}
		return new HomeTab(driver);
	}

	public String getRefNo() {
		String RefNo = null;
		try{
			Thread.sleep(1000);
			if(ResultChangeHistoryTable.isDisplayed())
			{
			RefNo = RefNOOnChangeHistory.getText();
			}
		}
		catch(Exception e)
		{
			System.out.println("The Ref No is not captured");
		}
		return RefNo;
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ChangeHistorybutton);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

}
