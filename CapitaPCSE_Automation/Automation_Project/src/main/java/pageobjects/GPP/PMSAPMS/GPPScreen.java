package pageobjects.GPP.PMSAPMS;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.Screenshot;
import helpers.Support;
import pageobjects.PL.Referees;
public class GPPScreen extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="txtContractorNameCode")
	WebElement ContractorName;
	
	public GPPScreen(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public PMSinstructionScreen ClickonTab(String TabName) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//div[@class='row-fluid child-navbar']//li"));
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
		return new PMSinstructionScreen(driver);
	}

	
	
}
