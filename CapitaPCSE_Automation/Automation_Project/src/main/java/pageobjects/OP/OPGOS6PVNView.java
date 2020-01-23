package pageobjects.OP;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OPGOS6PVNView {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input[id^='OpenGosSixPvn']")
	WebElement openGOSSixPvnButton;
	
	public OPGOS6PVNView(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}
	
	public OPGOS6PatientDetails clickONOpenGOSSixPVN() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(openGOSSixPvnButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPGOS6PatientDetails(driver);
	}

}
