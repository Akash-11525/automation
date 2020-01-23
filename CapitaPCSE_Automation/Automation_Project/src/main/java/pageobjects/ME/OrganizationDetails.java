package pageobjects.ME;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrganizationDetails {
	WebDriver driver;
	WebDriverWait wait;
		
	public OrganizationDetails(WebDriver driver) 
		{
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 60);
			PageFactory.initElements(this.driver, this);
		}
	

}
