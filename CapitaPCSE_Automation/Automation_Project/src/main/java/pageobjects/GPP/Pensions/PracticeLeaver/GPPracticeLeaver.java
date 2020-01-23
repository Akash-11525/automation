package pageobjects.GPP.Pensions.PracticeLeaver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;

public class GPPracticeLeaver extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div/div[1]/a")
	WebElement leaverEntry;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div/div[2]/a")
	WebElement leaverAppListing;
	
	public GPPracticeLeaver(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public GPLeaverApplicationForm ClickOnLeaverEntryForm() throws InterruptedException {
		scrolltoElement(driver, leaverEntry);
		wait.until(ExpectedConditions.elementToBeClickable(leaverEntry)).click();
		Thread.sleep(2000);
		return new GPLeaverApplicationForm(driver);
	}
	
	public CommissionerAppListing ClickOnAppCommissionerApproval() throws InterruptedException {
		WebElement commApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[3]//a"));
		scrolltoElement(driver, commApproval);
		wait.until(ExpectedConditions.elementToBeClickable(commApproval)).click();
		Thread.sleep(2000);
		return new CommissionerAppListing(driver);
	}

}
