package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenerateRecalls {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(id="crmQuickFindTD")
	WebElement srcRecords;
	
	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;
	
	//@FindBy(css="iframe[id*='contentIFrame']")
	@FindBy(xpath="//iframe[contains(@id, 'contentIFrame']")
	WebElement frame;
	
	@FindBy(css="table[id='gridBodyTable']")
	WebElement searchResulttbl;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody/tr/td[2]//a")
	WebElement firstSearchResultItem;
	
	@FindBy(xpath="//li[contains(@id,'scr_job.LaunchJob.Button')]")
	WebElement launchJobButton;
	
	@FindBy(xpath="//span[contains(@command,'LaunchWorkflow.Command')]")
	WebElement launchJob;
	
	@FindBy(xpath = "//*[contains(@id, 'commandContainer')]")
	//@FindBy(id*="commandContainer")
	WebElement commandContainer3;
	
	@FindBy(id="navTabModuleButtonTextId")
	WebElement screeningTab;
	
	@FindBy(xpath = "//div[@id='crmRibbonManager']/div/ul/li[1]")
	WebElement launchJobBtn;


	
	public GenerateRecalls(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	

	
	public GenerateRecalls launchJob() throws InterruptedException{
		
		
		launchJobBtn.click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJobButton)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJob));
		//Thread.sleep(2000);
		
		driver.switchTo().defaultContent();
		return new GenerateRecalls(driver);
	}
	
public CrmHome launchJobAndSwitchtoCRMHome() throws InterruptedException{
		
		
		launchJobBtn.click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJobButton)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJob));
		//Thread.sleep(2000);
		
		driver.switchTo().defaultContent();
		
		driver.findElement(By.id("TabHome")).click();
		return new CrmHome(driver);
	}
	
	public CrmHome clickScreening(){

		wait.until(ExpectedConditions.visibilityOf(screeningTab));
		Actions actions = new Actions(driver);
		actions.moveToElement(screeningTab).build().perform();
		//wait.until(ExpectedConditions.elementToBeClickable(screeningTab)).click();
		actions.moveToElement(screeningTab).click().build().perform();

		return new CrmHome(driver);
	}
	
	


}
