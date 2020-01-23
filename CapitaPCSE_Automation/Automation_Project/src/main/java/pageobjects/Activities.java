package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Activities {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath = "//*[@id ='crmGrid_SavedNewQuerySelector']/span[1]")
	WebElement AllActivies;
	
	@FindBy(xpath = "//li[@id='ViewSelector_activitypointer']")
	WebElement AllActiviesoption;

	@FindBy(xpath = "//*[@id='{00000000-0000-0000-00AA-000010001902}']/a[2]/span/nobr")
	WebElement AllActiviesoptionsubmenu;
	
	@FindBy(xpath = "//*[@id='filterButtonImage']")
	WebElement Filterbutton;
	
	@FindBy(xpath = "//*[@id='crmGrid_gridBar']/tbody/tr/th[2]/table/tbody/tr/td[2]/table")
	WebElement Regardingdropdown;
	
	@FindBy(xpath = "//*[@id='_MIcrmGridactivitypointerregardingobjectidCustomFilters']/a[2]/span/nobr")
	WebElement Customfilter;
	
	@FindBy(id = "dialogHeaderTitle")
	WebElement Customfilterdialogue;
	
	@FindBy(xpath = "//*[@id='butBegin']")
	WebElement Okbuttonondialgue;
	
	
	@FindBy(id = "primaryValue_txt")
	WebElement NHSentertext;
	
	@FindBy(xpath = "//table[@class='ms-crm-List-Data']//tr[1]//td[6]")
	WebElement Activitystatus;

	
	
	
	public Activities(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public Activities allactivitywithfilter() throws InterruptedException {
		
		driver.switchTo().frame("contentIFrame0");
		Actions action = new Actions(driver);
		action.moveToElement(AllActivies);
		action.click().build().perform();
		Actions action1 = new Actions(driver);
		action1.moveToElement(AllActiviesoption);
		action1.click().build().perform();
		
		Actions action2 = new Actions(driver);
		action2.moveToElement(AllActiviesoptionsubmenu);
		action2.click().build().perform();		
		Actions action3 = new Actions(driver);
		action3.moveToElement(Filterbutton);
		action3.click().build().perform();
		
		// TODO Auto-generated method stub
		return new Activities(driver);
	}

	public Activities clickoncustonfilter(String text, String CRMNHSNo) throws InterruptedException {
		
		
		Actions action3 = new Actions(driver);
		action3.moveToElement(Regardingdropdown);
		action3.click().build().perform();
		
		Actions action1 = new Actions(driver);
		action1.moveToElement(Customfilter);
		action1.click().build().perform();
		 Thread.sleep(2000);
		 driver.switchTo().defaultContent();
		 Thread.sleep(1000);
		 System.out.println("End");
		 driver.switchTo().frame("InlineDialog_Iframe");
		 
		 
		if(Customfilterdialogue.isDisplayed())
		{
			
			Select dropdown = new Select(driver.findElement(By.id("primaryOperator")));
			dropdown.selectByVisibleText(text);
			wait.until(ExpectedConditions.elementToBeClickable(NHSentertext));
			NHSentertext.sendKeys(CRMNHSNo);
			Okbuttonondialgue.click();
		}
		driver.switchTo().defaultContent();
		
		// TODO Auto-generated method stub
		return new Activities(driver);
	}

	public Boolean verifyactivitystatus() {
		driver.switchTo().frame("contentIFrame0");
		
		boolean activitystatus = false;
		String Activitystatusonfirstresult =  wait.until(ExpectedConditions.visibilityOf(Activitystatus)).getText();
		if(Activitystatusonfirstresult.equalsIgnoreCase("Canceled"))
		{
			activitystatus = true;
		}
		else
		{
			activitystatus = false;
		}
		// TODO Auto-generated method stub
		return activitystatus;
	}

}
