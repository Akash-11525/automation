package pageobjects.PL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class PLHomepage extends Support{
	
	WebDriver driver;
	WebDriverWait wait;
	
    @FindBy(xpath="//div[@class='panel-body']//p[2]/a")
    WebElement PerformerlistButton;
    
	
	public PLHomepage(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		//wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public PerformerList clickonperformerList() {
		try
		{
		boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent)
		{
			Thread.sleep(2000);
			ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}
		wait.until(ExpectedConditions.elementToBeClickable(PerformerlistButton));
		wait.until(ExpectedConditions.elementToBeClickable(PerformerlistButton)).click();
		}
		catch(Exception e)
		{
			System.out.println("The Click on Performerlist is not happen");
		}
		return new PerformerList(driver);
	}
}
