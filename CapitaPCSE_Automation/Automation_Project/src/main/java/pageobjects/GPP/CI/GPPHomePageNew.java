package pageobjects.GPP.CI;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class GPPHomePageNew extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//ul[@class='row']//li")
	WebElement tabNames;
	
	public GPPHomePageNew(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public <T> T ClickonTab(String TabName,Class<T> expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			Thread.sleep(2000);
			ActualTabNames =driver.findElements(By.xpath("//div[@id='divMainContainer']/div[2]/div/div"));
			 for (WebElement ActualTabName:ActualTabNames)
			 {
				 wait.until(ExpectedConditions.visibilityOf(ActualTabName));
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
			System.out.println("The Sub menu tab is not clicked: "+TabName);
		}
			return PageFactory.initElements(driver, expectedPage);
	}
	
	//This is used to click on an option under main header tab
	public <T> T ClickonHeaderTab(String TabName,Class<T> expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//*[@id='nav-main']/div/div/div/ul/li"));
			System.out.println(ActualTabNames);
			 for (WebElement ActualTabName:ActualTabNames)
			 {
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
			 Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("The Sub menu tab is not clicked"+TabName);
		}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	//This is used to click on a text present on breadcrumb
	public <T> T ClickonBreadcrumbTab(String TabName,Class<T> expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//div[contains(@class,'breadcrumb')]/span"));
			System.out.println(ActualTabNames);
			 for (int i=1;i<=ActualTabNames.size();i++)
			 {
				 WebElement ActualTabName= driver.findElement(By.xpath("//div[contains(@class,'breadcrumb')]/span["+i+"]"));
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
			 Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("The Sub menu tab is not clicked: "+TabName);
		}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	//This is used to click on an option on header tab present on top side
	public <T> T ClickOnPageHeader(String TabName,Class<T> expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			Thread.sleep(6000);
			wait.until(ExpectedConditions.visibilityOf(tabNames));
			//	wait.until(ExpectedConditions.elementToBeClickable(tabNames));
			System.out.println("TAB NAMES: "+tabNames);
			ActualTabNames =driver.findElements(By.xpath("//ul[@class='row']//li"));
			System.out.println(ActualTabNames);
			 for (WebElement ActualTabName:ActualTabNames)
			 {
				 scrolltoElement(driver, ActualTabName);
				 String TabNameOnPortal = ActualTabName.getText();
				 System.out.println(TabNameOnPortal);
				 if(TabNameOnPortal.contains(TabName))
				 {
				 	Actions action = new Actions(driver);
					action.moveToElement(ActualTabName);
					System.out.println("Moved to desired Element: "+ActualTabName.getText());

					if(TabName.equalsIgnoreCase("Messages")){
						Thread.sleep(10000);
						action.doubleClick().build().perform();
					}else{
						action.doubleClick().build().perform();
						System.out.println("double click action is performed on element");
					}
					
					break;
				 }
			 }
	}
	catch(Exception e)
	{
		System.out.println("The Sub menu tab is not clicked: "+TabName);
	}
		return PageFactory.initElements(driver, expectedPage);
	}

}
