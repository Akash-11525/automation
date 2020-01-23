package pageobjects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrgPaymentdetails {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="crmGrid_clearCriteriaImg")
	WebElement clearImg;

	public OrgPaymentdetails(WebDriver driver){
		this.driver = driver;		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
		
		}
	

}
