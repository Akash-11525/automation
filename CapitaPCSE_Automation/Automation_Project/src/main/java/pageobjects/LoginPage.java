package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage 
{
    WebDriver driver;
    
    @FindBy(css="input[name='USER']")
    WebElement UserName;
    
    @FindBy(css="input[name='PASSWORD']")
    WebElement Password;
    
    @FindBy(id="submit1")
    WebElement loginbutton;
    
    @FindBy(id="openglobal_privacy_widget")
    WebElement cookiesPopUp;
    
    @FindBy(id="openglobal_privacy_accept")
    WebElement acceptButton;
    
    WebDriverWait wait;
    
    public LoginPage(WebDriver driver)
    {
           this.driver = driver;
           this.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
           wait = new WebDriverWait(this.driver, 15);
           //this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
           PageFactory.initElements(this.driver, this);
    }
    
    
    public WebDriver returndriver()
    {
           return this.driver;
    }
    
/*    public WebDriver acceptCookiesPopUp()
    {
    	try 
    	{
			if(cookiesPopUp.isDisplayed())
			{
				acceptButton.click();
			}
		} 
    	catch (Exception e) 
    	{		
    		System.out.println(e);
    	}
    	return this.driver;
    }*/
    
    public void EnterLoginID(String username) throws InterruptedException
    {
/*    	try 
    	{
			if(cookiesPopUp.isDisplayed())
			{
				acceptButton.click();
			}
		} 
    	catch (Exception e) 
    	{		
    		System.out.println(e);
    	}*/
        wait.until(ExpectedConditions.visibilityOf(UserName));
        Thread.sleep(4000);
        UserName.clear();
        UserName.sendKeys(username);
    }
    
    public void EnterPassword(String password)
    {
           Password.clear();
           Password.sendKeys(password);
    }
    
/*    public HomePage ClickLoginButton() throws InterruptedException
    {
           loginbutton.click();
           Thread.sleep(10000);
           return new HomePage(driver);
    }*/



}
