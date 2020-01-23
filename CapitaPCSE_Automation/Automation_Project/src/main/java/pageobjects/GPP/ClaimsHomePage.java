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
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;
import pageobjects.GPP.QOF.QOFPaymentScreen;
import pageobjects.GPP.SC.StdClaimsApprovalWindow;
import pageobjects.GPP.SC.StdClaimsPortal;

public class ClaimsHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public ClaimsHomePage(WebDriver driver)
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
		List<WebElement> division =driver.findElements(By.xpath("//div[@id='divMainContainer']/div[3]/div"));
		divisionLoop:
		for(int i=1;i<=division.size();i++){
			List<WebElement> divElements= driver.findElements(By.xpath("//div[@id='divMainContainer']/div[3]/div["+i+"]/div"));
			for(int j=1;j<=divElements.size();j++){
				try{
					WebElement tab= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[3]/div/div["+j+"]/a"));
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
	
	public StdClaimsApprovalWindow ClickOnClaimsApprovalTab(String loginType) throws InterruptedException {
		WebElement claimsApproval= null;
		switch(loginType){
		case "PCSEClerk":
			claimsApproval= driver.findElement(By.xpath("//*[@id='divMainContainer']/div[3]/div[2]/div[2]/a"));
			break;
		case "NHSECommissioner":
			claimsApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[3]/div[2]/div/div/a"));
			break;
		default:
			System.out.println("User login type is not found.");
		}
		//WebElement claimsApproval= driver.findElement(By.xpath("//*[@id='divMainContainer']/div[3]/div[2]/div[2]/a"));
		scrolltoElement(driver, claimsApproval);
		wait.until(ExpectedConditions.elementToBeClickable(claimsApproval)).click();
		Thread.sleep(2000);
		return new StdClaimsApprovalWindow(driver);
	}
	
	public StdClaimsPortal ClickOnClaimsPortalTab() throws InterruptedException {
		WebElement claimsApproval= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[3]/div[2]/div/a"));
		scrolltoElement(driver, claimsApproval);
		wait.until(ExpectedConditions.elementToBeClickable(claimsApproval)).click();
		Thread.sleep(2000);
		return new StdClaimsPortal(driver);
	}
}
