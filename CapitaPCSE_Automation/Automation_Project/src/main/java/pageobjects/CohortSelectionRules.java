package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.UITableDataSupport;

public class CohortSelectionRules {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;
	
	@FindBy(xpath="//table[@id='gridBodyTable']/tbody")
	WebElement gridTable;
	
	public CohortSelectionRules(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public boolean rulePresent(String name)
	{
		Boolean flag = false;
		try{
		driver.switchTo().frame("contentIFrame0");
		srcCriTxt.clear();
		
		srcCriTxt.sendKeys(name);
		srcCriIcon.click();
		
				
		//flag = CommonFunctions.TableData.getDataFromColumnInTable(gridTable, 1, name);
		flag = UITableDataSupport.getDataFromColumnInTable(gridTable, 1, name);
				
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}
		return flag;
	}

}
