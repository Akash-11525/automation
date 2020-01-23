package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenNewPatientsScreeningRecords {
	
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
	
	@FindBy(xpath = "//div[contains(@id, 'commandContainer')]")
	//@FindBy(id*="commandContainer")
	WebElement commandContainer3;
	
	@FindBy(xpath = "//div[@id='crmRibbonManager']/div/ul/li[1]")
	WebElement launchJobBtn;
	
	@FindBy(id="Tab1")
	WebElement dyCRM;
	
	@FindBy(id="Settings")
	WebElement settings;
	
	@FindBy(id="Screening")
	WebElement screening;
	
	@FindBy(id="crmRibbonManager")
	WebElement rbnMgr;
	
	@FindBy(id="TabHome")
	WebElement homeTab;
	
	@FindBy(id="TabSettings")
	WebElement TabSettings;
	
	@FindBy(id = "Tabscr_job-main")
	WebElement mainJob;
	
	@FindBy(xpath="//*[@id='Tabscr_job-main']/a/span/span")
	WebElement Adminbutton;
	
	public GenNewPatientsScreeningRecords(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	

	
	public GenNewPatientsScreeningRecords launchJob() throws InterruptedException{
		
		launchJobBtn.click();
		//rbnMgr.click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJobButton)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJob));
		
		driver.switchTo().defaultContent();
		return new GenNewPatientsScreeningRecords(driver);
	}
	
	public CrmHome launchJob_CRM() throws InterruptedException{
		
		launchJobBtn.click();
		//rbnMgr.click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJobButton)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJob));
		
		driver.switchTo().defaultContent();
		return new CrmHome(driver);
	}
	
	public CrmHome clickSettings(){

		Actions actions = new Actions(driver);
		actions.moveToElement(dyCRM);
		actions.click().build().perform();

		wait.until(ExpectedConditions.visibilityOf(settings));
		wait.until(ExpectedConditions.elementToBeClickable(settings)).click();


		return new CrmHome(driver);
	}
	
	public CrmHome clickTabSettings(){

		
		wait.until(ExpectedConditions.elementToBeClickable(TabSettings)).click();


		return new CrmHome(driver);
	}
	
	public CrmHome clickScreening(){

		Actions actions = new Actions(driver);
		actions.moveToElement(dyCRM);
		actions.click().build().perform();

		wait.until(ExpectedConditions.visibilityOf(screening));
		wait.until(ExpectedConditions.elementToBeClickable(screening)).click();


		return new CrmHome(driver);
	}
	
	public CrmHome clickHome(){

		wait.until(ExpectedConditions.visibilityOf(homeTab));
		wait.until(ExpectedConditions.elementToBeClickable(homeTab)).click();

		return new CrmHome(driver);
	}
	
	public AdminJobs clickAdminJobs(){
		
		wait.until(ExpectedConditions.elementToBeClickable(mainJob)).click();
		return new AdminJobs(driver);
	}
	
	public AdminJobs Adminbutton()
	{
		wait.until(ExpectedConditions.visibilityOf(Adminbutton));
		wait.until(ExpectedConditions.elementToBeClickable(Adminbutton)).click();
		driver.switchTo().defaultContent();
	
		return new AdminJobs(driver);
	}
	


}
