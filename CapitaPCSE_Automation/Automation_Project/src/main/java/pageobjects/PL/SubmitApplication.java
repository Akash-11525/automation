package pageobjects.PL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class SubmitApplication extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SubmitApplication")
	WebElement SubmitAppTab;
	
	@FindBy(name="HasConfirmedUndertakingsInRole")
	WebElement ParagraphCheckBox;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	public SubmitApplication(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}


	public String EnterSubmitAppdetails() {
		String ActualTablename_Submitapplication = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(SubmitAppTab));
			ActualTablename_Submitapplication = SubmitAppTab.getAttribute("id");
			//wait.until(ExpectedConditions.elementToBeClickable(SubmitAppTab)).click();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Submit App not clicked at that moment  " +e);
		}
		return ActualTablename_Submitapplication;
	}




	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}
	
	

}
