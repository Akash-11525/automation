package pageobjects.ME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class ServiceConfirmations extends Support {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath=ConstantOR.Providingservices_Checkbx)
	List<WebElement> providingservices_Checkbx;
	
	public ServiceConfirmations(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public FinalDeclaration confirmChangeServiceDeclarations(String key) {
		// TODO Auto-generated method stub
		try{
			
			String Exl_ChangeService = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ServiceConfirmations", "ServiceDeclarations", key);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(providingservices_Checkbx, Exl_ChangeService, driver);		
			
			helpers.CommonFunctions.ClickOnButton("Save & Next", driver);				
			
			helpers.Support.PageLoadExternalwait(driver);
			System.out.println("Added the Service Confirmations");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new FinalDeclaration(driver);
	}

}
