package pageobjects.PL;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PerformerHome 
{
	WebDriver driver;
	WebDriverWait wait;
	

	@FindBy(id="SelectPerformer")
	WebElement SelectPerfomerdropdown;
	
	@FindBy(xpath="//button[@class='btn btn-success']")
	WebElement Submitbutton;
	
	
	
	public PerformerHome(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

	public PerformerHome SelectPerformer(String reason) throws InterruptedException 
	{
		Select dropdown = new Select(SelectPerfomerdropdown);
		dropdown.selectByVisibleText(reason);
	
	
		return new PerformerHome(driver);
	}
	
	public PerformerList ClickOnSubmit() throws InterruptedException 
	{
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Submitbutton)).click();
			}
		catch (NoSuchElementException e)
		{
		System.out.println("The Submit button is not found on Performer page."+e);
		}
	
	
		return new PerformerList(driver);
	}
	
}