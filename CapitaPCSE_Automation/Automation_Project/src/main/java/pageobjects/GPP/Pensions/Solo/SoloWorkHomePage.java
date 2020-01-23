package pageobjects.GPP.Pensions.Solo;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class SoloWorkHomePage extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div[2]/div/a")
	WebElement incomeEntryTab;
	
	public SoloWorkHomePage(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public SoloIncomeEntry clickOnIncomeEntryTab() throws InterruptedException {
		scrolltoElement(driver, incomeEntryTab);
		wait.until(ExpectedConditions.elementToBeClickable(incomeEntryTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on SOLO Income Entry Tab");
		return new SoloIncomeEntry(driver);
	}

}
