package pageobjects.GPP.Pensions.PracticeJoiner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class JoinerApplicationsListing extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="Employeepartnerlistingfilter_SearchBy")
	WebElement gmcTxtBox;
	
	@FindBy(css="select#Employeepartnerlistingfilter_ApplicationTypeId")
	WebElement applicationType;
	
	@FindBy(css="select#Employeepartnerlistingfilter_StatusId")
	WebElement status;
	
	@FindBy(id="txtApplicationDate")
	WebElement applicationDate;
	
	@FindBy(id="btnEmployeeListingSearch")
	WebElement searchBtn;
	
	public JoinerApplicationsListing(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public void ClickonTab(String TabName) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//ul[starts-with(@class,'nav nav-employee-partner')]/li"));
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
		}
		catch(Exception e)
		{
			System.out.println("The Sub menu tab is not clicked"+TabName);
		}
		
	}

}
