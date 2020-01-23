package pageobjects.OP;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OPGOS6Options {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//button[contains(@data-ajax-url,'/OphthalmicGosSixPvn/GosSixPvn')]")
	WebElement createGOS6PVNBtn;
	
	@FindBy(xpath="//button[contains(@onclick,'PVNSearch')]")
	WebElement searchGOS6PVNBtn;
	
	@FindBy(xpath="//button[contains(@data-ajax-url,'CreateNonApprovedLocation')")
	WebElement createNonApprovedLocation;
	
	@FindBy(xpath="//button[contains(@data-ajax-url,'GOSSixSearchPVNNonApprovedLocation')")
	WebElement searchNonApprovedLocation;
	
	
	
	
	
	
	
	public OPGOS6Options(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}
	
	public OPCreateGOS6PVN clickOnCreateGOS6PVN() throws InterruptedException
	{
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(createGOS6PVNBtn)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPCreateGOS6PVN(driver);
	}
	
	public OPSearchGOS6PVN clickOnSearchGOS6PVN()
	{
		wait.until(ExpectedConditions.elementToBeClickable(searchGOS6PVNBtn)).click();
		return new OPSearchGOS6PVN(driver);
	}
	
	
	

}
