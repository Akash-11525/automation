package pageobjects.PL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;


public class Undertaking extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="PLI_Undertakings")
	WebElement UndertakingTab;
	@FindBy(name="HasConfirmedUndertakingsInRole")
	WebElement ParagraphCheckBox;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	String Statement = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","Undertaking","Statement Applies", 1);
	
	public Undertaking(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public String EnterUndertakingdetails() {
		String ActualTablename_Undertaking = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(UndertakingTab));
			ActualTablename_Undertaking = UndertakingTab.getAttribute("id");
			List<WebElement> StatementRadioButtons = driver.findElements(By.xpath("//*[@class='radio margin-top-8']"));
	        System.out.println(StatementRadioButtons.size());
	        for (WebElement StatementRadioButton:StatementRadioButtons)
            {
	        	scrolltoElement(driver, StatementRadioButton);
            	if((StatementRadioButton.getText()).contains(Statement))
            			{
            		StatementRadioButton.click();
            		break;
            			}
            }
	        scrolltoElement(driver, ParagraphCheckBox);
	        
	   //     wait.until(ExpectedConditions.elementToBeClickable(ParagraphCheckBox)).click();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PoliceCheck details not filled properly  " +e);
		}
		return ActualTablename_Undertaking;
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
		
			scrolltoElement(driver, Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}
}
