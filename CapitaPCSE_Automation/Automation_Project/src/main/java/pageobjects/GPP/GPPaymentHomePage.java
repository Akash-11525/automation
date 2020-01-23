package pageobjects.GPP;

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
import pageobjects.Adjustments.AdjustmentHomePage;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.QOF.QOFPaymentScreen;

public class GPPaymentHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public GPPaymentHomePage(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public <T>T ClickonTab(String TabName,Class<T>expectedPage) throws InterruptedException {
		Thread.sleep(2000);
		List<WebElement> division =driver.findElements(By.xpath("//div[@id='divMainContainer']/div[2]/div"));
		divisionLoop:
			//loop started with 2 as qst div is for margin
		for(int i=2;i<=division.size();i++){
			List<WebElement> divElements= driver.findElements(By.xpath("//div[@id='divMainContainer']/div[2]/div["+i+"]/div"));
			for(int j=1;j<=divElements.size();j++){
				try{
					WebElement tab= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div["+i+"]/div["+j+"]/button/div"));
					wait.until(ExpectedConditions.visibilityOf(tab));
					wait.until(ExpectedConditions.elementToBeClickable(tab));
					String ActualTabName= tab.getText().toString();
					scrolltoElement(driver, tab);
					 if(ActualTabName.equalsIgnoreCase(TabName))
					 {
					 	Actions action = new Actions(driver);
						action.moveToElement(tab);
						action.doubleClick().build().perform();
						break divisionLoop;
					 }
				}catch(Exception e){
					System.out.println("Tab is not clicked for division row index "+i+" and subindex "+j);
				}
				
			}

		}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	public QOFPaymentScreen ClickOnQOFTab() throws InterruptedException {
		WebElement commApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div[3]/div[4]/a"));
		scrolltoElement(driver, commApproval);
		wait.until(ExpectedConditions.elementToBeClickable(commApproval)).click();
		Thread.sleep(2000);
		return new QOFPaymentScreen(driver);
	}
	
	public AdjustmentHomePage ClickOnAdjustmentTab() throws InterruptedException {
		WebElement commApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div[4]/div[2]/a"));
		scrolltoElement(driver, commApproval);
		wait.until(ExpectedConditions.elementToBeClickable(commApproval)).click();
		Thread.sleep(2000);
		return new AdjustmentHomePage(driver);
	}
	
	public ClaimsHomePage ClickOnClaimsTab_Commissioner() throws InterruptedException {
		WebElement claims= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div[3]/div[4]/button/div"));
		scrolltoElement(driver, claims);
		wait.until(ExpectedConditions.elementToBeClickable(claims)).click();
		Thread.sleep(2000);
		return new ClaimsHomePage(driver);
	}
	
	public ClaimsHomePage ClickOnClaimsTab() throws InterruptedException {
		WebElement claims= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div[3]/div/div/div/button/div"));
		scrolltoElement(driver, claims);
		wait.until(ExpectedConditions.elementToBeClickable(claims)).click();
		Thread.sleep(2000);
		return new ClaimsHomePage(driver);
	}
	
	public ClaimsHomePage ClickOnClaimsTab_Clerk() throws InterruptedException {
		WebElement claims= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div[4]/div[1]/button/div"));
		scrolltoElement(driver, claims);
		wait.until(ExpectedConditions.elementToBeClickable(claims)).click();
		Thread.sleep(2000);
		return new ClaimsHomePage(driver);
	}

}
