package pageobjects;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pageobjects.CS.PortalHome;

public class LoginPageCS {
	
	WebDriver driver;
    WebDriverWait wait;
    
	@FindBy(name="UserName")
	WebElement UserNameLogin;	
	
	@FindBy(name="Password")
	WebElement PasswordLogin;
	
	@FindBy(xpath="//*[@class='submit']")
	WebElement SignIN;
	
	
	
	   public LoginPageCS(WebDriver driver)
	    {
	           this.driver = driver;
	           this.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	           wait = new WebDriverWait(this.driver, 15);
	           //this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	           PageFactory.initElements(this.driver, this);
	    }
	   
	   public PortalHome loginpage(int Position )
	   {
		try{
			
			String UsernameExcel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "LoginPage", "UserName",Position);
			String PasswordExcel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CSTESTDATA.xlsx", "Capacity", "Password",Position);
			
		    wait.until(ExpectedConditions.visibilityOf(UserNameLogin));
		    wait.until(ExpectedConditions.elementToBeClickable(UserNameLogin)).sendKeys(UsernameExcel);
		    wait.until(ExpectedConditions.elementToBeClickable(PasswordLogin)).sendKeys(PasswordExcel);
		    wait.until(ExpectedConditions.elementToBeClickable(SignIN)).click();
			
		}
		catch(Exception e)
		{
			
		}
	   
	   return new PortalHome(driver);
	   }
	   

}
