package pageobjects.GPP.Pensions.PracticeJoiner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class GPPracticeJoiner extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public GPPracticeJoiner(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public <T>T ClickonTab(String TabName,Class<T>expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			Thread.sleep(2000);
			ActualTabNames =driver.findElements(By.xpath("//div[@id='divMainContainer']/div[3]/div/div"));
			
			 for (WebElement ActualTabName:ActualTabNames)
			 {
				 wait.until(ExpectedConditions.elementToBeClickable(ActualTabName));
				 scrolltoElement(driver, ActualTabName);
				 String TabNameOnPortal = ActualTabName.getText();
				 if(TabNameOnPortal.equalsIgnoreCase(TabName))
				 {
				 	Actions action = new Actions(driver);
					action.moveToElement(ActualTabName);
					action.doubleClick().build().perform(); 
					break;
				 }
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Sub menu tab is not clicked"+TabName);
		}
			return PageFactory.initElements(driver, expectedPage);
		}
	
	public CommissionerAppListing ClickOnAppCommissionerApproval() throws InterruptedException {
		Thread.sleep(2000);
		WebElement commApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[3]/div/div/div/div/a")); //Changed by akash
		//WebElement commApproval= driver.findElement(By.xpath("//div[contains(text(),'Application Listing Commissioner Approval')]"));
		scrolltoElement(driver, commApproval);
		wait.until(ExpectedConditions.elementToBeClickable(commApproval)).click();
		Thread.sleep(2000);
		return new CommissionerAppListing(driver);
	}

}
