package pageobjects.GPP.Pensions.PracticeJoiner;

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
import pageobjects.GPP.Pensions.AVC.AVCHomePage;
import pageobjects.GPP.Pensions.BreakInService.BreakInServiceEntryForm;
import pageobjects.GPP.Pensions.PracticeLeaver.GPPracticeLeaver;
import pageobjects.GPP.Pensions.SalaryChange.GPSalaryChangeForm;
import pageobjects.GPP.Pensions.Solo.SoloWorkHomePage;

public class GPPensionsHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[2]/div[2]/a")
	WebElement breakInServiceTab;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div/div/div[1]/button/div")
	WebElement practiceJoinerTab;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div/div/div[2]/button/div")
	WebElement practiceLeaverTab;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[2]/div/div[1]/button/div")
	WebElement practiceJoinerTab_Commissioner;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[2]/div/div[2]/button/div")
	WebElement practiceLeaverTab_Commissioner;
	
	@FindBy(xpath="//button[contains(@data-ajax-url,'Solo')]/div")
	WebElement soloWorkTab;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[3]/div[3]/button/div")
	WebElement AVCTab_Commissioner;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[2]/div[4]/a")
	WebElement salaryChangeTab;
	
	public GPPensionsHomePage(WebDriver driver)
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
		List<WebElement> division =driver.findElements(By.xpath("//div[@id='divMainContainer']/div[2]"));
		divisionLoop:
			try{
				for(int i=2;i<=division.size()+1;i++){
					List<WebElement> divElement= driver.findElements(By.xpath("//div[@id='divMainContainer']/div[2]/div["+i+"]/div"));
					for(int j=1;j<=divElement.size();j++){
						try{
							WebElement tab= driver.findElement(By.xpath("//div[@id='divMainContainer']/div[2]/div["+i+"]/div["+j+"]/button/div"));
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
			}catch(Exception e){
				System.out.println("An exception has been catched for locating element");
			}

		return PageFactory.initElements(driver, expectedPage);
	}
	
	public BreakInServiceEntryForm clickOnBreakInserviceTab() throws InterruptedException {
		
		scrolltoElement(driver, breakInServiceTab);
		wait.until(ExpectedConditions.elementToBeClickable(breakInServiceTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on break in service button on joiner approval form");
		return new BreakInServiceEntryForm(driver);
	}
	
	public GPPracticeJoiner clickOnPracticeJoinerTab() throws InterruptedException {
		Thread.sleep(2000); 
		scrolltoElement(driver, practiceJoinerTab);
		wait.until(ExpectedConditions.elementToBeClickable(practiceJoinerTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on practice joiner button on joiner approval form");
		return new GPPracticeJoiner(driver);
	}
	
	public GPPracticeLeaver clickOnPracticeLeaverTab() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, practiceLeaverTab);
		wait.until(ExpectedConditions.elementToBeClickable(practiceLeaverTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on practice leaver button on joiner approval form");
		return new GPPracticeLeaver(driver);
	}
	
	public GPPracticeJoiner clickOnPracticeJoinerTab_Commissioner() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, practiceJoinerTab_Commissioner);
		wait.until(ExpectedConditions.elementToBeClickable(practiceJoinerTab_Commissioner)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on commissioner practice joiner button on joiner approval form");
		return new GPPracticeJoiner(driver);
	}
	
	public GPPracticeLeaver clickOnPracticeLeaverTab_Commissioner() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, practiceLeaverTab_Commissioner);
		wait.until(ExpectedConditions.elementToBeClickable(practiceLeaverTab_Commissioner)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on commissioner practice leaver button on joiner approval form");
		return new GPPracticeLeaver(driver);
	}
	
	public SoloWorkHomePage clickOnSoloWorkTab() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, soloWorkTab);
		wait.until(ExpectedConditions.elementToBeClickable(soloWorkTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on SOLO Work Tab");
		return new SoloWorkHomePage(driver);
	}
	
	public AVCHomePage clickOnAVCTab_Commissioner() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, AVCTab_Commissioner);
		wait.until(ExpectedConditions.elementToBeClickable(AVCTab_Commissioner)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on AVC commissioner Tab");
		return new AVCHomePage(driver);
	}
	
	public GPSalaryChangeForm clickOnSalaryChangeTab() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, salaryChangeTab);
		wait.until(ExpectedConditions.elementToBeClickable(salaryChangeTab)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on Salary Change Tab");
		return new GPSalaryChangeForm(driver);
	}

}
