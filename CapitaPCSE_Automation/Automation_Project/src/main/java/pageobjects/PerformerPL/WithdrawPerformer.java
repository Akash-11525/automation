package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.List;
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
public class WithdrawPerformer extends Support{
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(name="LastDayofService")
	WebElement LastDayServiceWithdraw;
	
	@FindBy(name="ApplyForNHSPensionScheme")
	WebElement PensionScheme;
	
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
	
	
	public WithdrawPerformer(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public WithdrawPerformer fillWithdrawPL(String Text) {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(LastDayServiceWithdraw)).clear();
			String Currentdate = helpers.CommonFunctions.getDate_UK();
			String LastDayWithdraw = helpers.CommonFunctions.Tomorrowdate(Currentdate, 30);
			
			wait.until(ExpectedConditions.elementToBeClickable(LastDayServiceWithdraw)).sendKeys(LastDayWithdraw);
			wait.until(ExpectedConditions.elementToBeClickable(LastDayServiceWithdraw)).sendKeys(Keys.TAB);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio ipad-radio-btn-margin']/label"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				String RadioValue = Radiobutton.getText();
				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase(Text))
				{
					Radiobutton.click();
					break;
				}
			}
			
			wait.until(ExpectedConditions.elementToBeClickable(PensionScheme)).click();
			
		}
		catch(Exception e)
		{
			System.out.println("The Withdraw PL is not filled properly");
		}
		return new WithdrawPerformer(driver) ;
	}
	
	
	public WithdrawPerformer clickonSubmit() {
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
		return new WithdrawPerformer(driver) ;
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

	public WithdrawPerformer clickonCancel_Submit() {
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
		return new WithdrawPerformer (driver);
	}
	
	public WithdrawPerformer clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(1000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new WithdrawPerformer (driver);
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
		scrolltoElement(driver, HomeMaintab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	
}
