package pageobjects.ProcessPL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
public class NHSCHome extends Support {

	WebDriver driver;
	WebDriverWait wait;	

	@FindBy(xpath="//*[@id='DivContainer']/div[2]/div[2]/p/a")
	WebElement Localoffice;

	public NHSCHome(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public NHSCViewApp clickonLocaloffice()
	{
		try{
			scrolltoElement(driver, Localoffice);
			wait.until(ExpectedConditions.elementToBeClickable(Localoffice));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Localoffice);
	    	actions1.doubleClick().build().perform();
	    	Thread.sleep(1000);
			
		}
		catch(Exception e)
		{
			System.out.println("The Local office link is not clicked"); 
			
		}
		return new NHSCViewApp(driver) ;
	}

	
	
	
	
	

}
