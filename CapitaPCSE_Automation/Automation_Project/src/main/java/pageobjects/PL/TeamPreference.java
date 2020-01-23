package pageobjects.PL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class TeamPreference extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="NetTeamPreference")
	WebElement TeamPreferenceTab;
	
	@FindBy(id="NETTeamPreferenceCode")
	WebElement TeamPreferenceSelect;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	String Location_TeamRef = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","TeamPreference","Location", 1);
	

	public TeamPreference(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public String EnterTeamPreferencekdetails() {
		String ActualTablename_TeamReference = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(TeamPreferenceTab));
			ActualTablename_TeamReference = TeamPreferenceTab.getAttribute("id");
			scrolltoElement(driver, TeamPreferenceSelect);
			Select dropdown = new Select(TeamPreferenceSelect);
			List<WebElement> dropdownLists =dropdown.getOptions();
			for (WebElement dropdownList :dropdownLists)
			{
				System.out.println(dropdownList.getText());
				if(dropdownList.getText().equalsIgnoreCase(Location_TeamRef))
				{
					dropdownList.click();
				}
			}
		//	dropdown.selectByVisibleText(Location_TeamRef);
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PoliceCheck details not filled properly  " +e);
		}
		return ActualTablename_TeamReference;
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
