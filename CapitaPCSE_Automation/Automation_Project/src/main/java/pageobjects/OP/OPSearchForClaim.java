package pageobjects.OP;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class OPSearchForClaim extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div[1]/button/div")
	WebElement GOSClaimSearchButton;
	
	@FindBy(xpath="//button[contains(@data-ajax-url, 'VoucherOnlySearch')]")
	WebElement GOSTHREEVoucherClaimSearchButton;
	
	@FindBy(xpath="//button[contains(@class, 'button-align-search')]")
	WebElement GOSSIXPVNSearchButton;

	public OPSearchForClaim(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public OPSearchClaim clickOnGOSClaimSearch()
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSClaimSearchButton)).click();
		return new OPSearchClaim(driver);
	}
	
	public OPOpticalVoucherRetrieval clickOnVoucherClaimSearch()
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSTHREEVoucherClaimSearchButton)).click();
		return new OPOpticalVoucherRetrieval(driver);
	}

}
