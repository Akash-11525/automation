package pageobjects.GMP;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class PaymentInstructionFile extends Support{

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//table[@id='PaymentInstructionFilesGrid']//tr/td[7]/div/a")
	WebElement Download;	

	
	public PaymentInstructionFile(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);	
		PageFactory.initElements(this.driver, this);
	}


	public PaymentInstructionFile ClickOnDownload(WebDriver driver) {
		try{
			helpers.Support.waitTillProcessing(driver);
			wait.until(ExpectedConditions.elementToBeClickable(Download)).click();	
		}
		   catch(Exception e)
	    {
	           System.out.println("The download button is not clicked " + e);
	           
	    }
		return new PaymentInstructionFile (driver);
	}


	public void takescreenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Download);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}
	
	


}
