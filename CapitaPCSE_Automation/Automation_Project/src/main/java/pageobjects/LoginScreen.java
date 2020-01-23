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

import helpers.Support;
import pageobjects.CS.PortalHome;
import pageobjects.GMP.GMPHome;
import pageobjects.ME.MarketEntryApplication;
import pageobjects.OP.OPHomePage;
import pageobjects.PL.PerformerList;
import pageobjects.ProcessPL.NHSCViewApp;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class LoginScreen extends Support {
	
WebDriver driver;
WebDriverWait wait;
    
    @FindBy(id="Email")
    WebElement Emailaddress;
    
    @FindBy(id="Password")
    WebElement Password;
    
    @FindBy(id="ctl00_PlaceHolderMain_signInControl_UserName")
    WebElement EmailaddressPL;
    
    @FindBy(id="ctl00_PlaceHolderMain_signInControl_password")
    WebElement PasswordPL;
   
    @FindBy(xpath="//input[@type='submit']")
    WebElement Loginbutton;
    
    @FindBy(xpath="//div[@class='panel-body']//p[2]/a")
    WebElement PerformerlistButton;
  
    
  /*  @FindBy(linkText ="Register")
    WebElement RegisterLink;*/
    
/*    @FindBy(xpath ="//*[@id='loginForm']/form/div[5]/p/a[2]")
    WebElement RegisterLink;*/
    
    
    @FindBy(xpath ="//input[@id='registerbtn']")
    WebElement RegisterLink;
    
    @FindBy(xpath ="//select[@id='registerLink']")
    WebElement Registerdropdown;
    
  
    
    @FindBy(partialLinkText="Log out")
    WebElement Logout;
    
   String domain; 
   String worksheet;
    public LoginScreen(WebDriver driver)
    {
           this.driver = driver;
           this.driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
           wait = new WebDriverWait(this.driver, 15);
           //this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
           PageFactory.initElements(this.driver, this);
    }
    
    
	private <T> T logintoAccount(String Key, String env, Class<T> expectedPage)   {
		try{
			
			if(Key.equalsIgnoreCase("PLAPPLICANT"))
			{
				String TestCaseName = ExcelUtilities.getTestCaseName();
				//String UserName = ExcelUtilities.getKeyValueFromExcelWithPosition("ConfigurationDetails.xlsx", "PLTestData", TestCaseName, 4);
				String UserName = ExcelUtilities.getKeyValueByPosition("ReadWrite_TestData_PL.xlsx", "PLTestData", TestCaseName, "PLAPPLICANT_ID");
				String PWD = ConfigurationData.getUserDetails(env, Key+"_PASSWORD");
				String TitleName = driver.getTitle();
				if(TitleName.startsWith("PCSS"))
				{					  
					wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL));
					scrolltoElement(driver, EmailaddressPL);
					wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL)).sendKeys(UserName);
					wait.until(ExpectedConditions.elementToBeClickable(PasswordPL));
					scrolltoElement(driver, PasswordPL);
					wait.until(ExpectedConditions.elementToBeClickable(PasswordPL)).sendKeys(PWD);
					scrolltoElement(driver, Loginbutton);
					wait.until(ExpectedConditions.elementToBeClickable(Loginbutton)).click();
					//	driver.navigate().refresh();
					Thread.sleep(1500);
					boolean ispresent = driver.findElements(By.xpath("//div[@class='panel-body']//p[2]/a")).size() != 0;
					if(ispresent)
					{
						wait.until(ExpectedConditions.elementToBeClickable(PerformerlistButton));
						Actions action1 = new Actions(driver);
						action1.moveToElement(PerformerlistButton);
						action1.doubleClick().build().perform();
						driver.navigate().refresh();
		//				Thread.sleep(3000);  Commented by Rupesh - Added for the Optimization
						helpers.CommonFunctions.PageLoadExternalwait(driver);
					}
				}
				else
				{
					wait.until(ExpectedConditions.elementToBeClickable(Emailaddress));
					scrolltoElement(driver, Emailaddress);
					wait.until(ExpectedConditions.elementToBeClickable(Emailaddress)).sendKeys(UserName);
					wait.until(ExpectedConditions.elementToBeClickable(Password));
					scrolltoElement(driver, Password);
					wait.until(ExpectedConditions.elementToBeClickable(Password)).sendKeys(PWD);
					scrolltoElement(driver, Loginbutton);
					Actions action = new Actions(driver);
					action.moveToElement(Loginbutton);
					action.doubleClick().build().perform();	
					helpers.CommonFunctions.PageLoadExternalwait(driver);
				//	driver.navigate().refresh();
					
				}
				
			}
			else
			{		
				String UserName = ConfigurationData.getUserDetails(env, Key+"_ID");
				String PWD = ConfigurationData.getUserDetails(env, Key+"_PASSWORD");
				String TitleName = driver.getTitle();
				if(TitleName.startsWith("PCSS"))
				{					  
					wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL));
					scrolltoElement(driver, EmailaddressPL);
					wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL)).sendKeys(UserName);
					wait.until(ExpectedConditions.elementToBeClickable(PasswordPL));
					scrolltoElement(driver, PasswordPL);
					wait.until(ExpectedConditions.elementToBeClickable(PasswordPL)).sendKeys(PWD);
					scrolltoElement(driver, Loginbutton);
					wait.until(ExpectedConditions.elementToBeClickable(Loginbutton)).click();
					//	driver.navigate().refresh();
					Thread.sleep(1500);
					boolean ispresent = driver.findElements(By.xpath("//div[@class='panel-body']//p[2]/a")).size() != 0;
					if(ispresent)
					{
						wait.until(ExpectedConditions.elementToBeClickable(PerformerlistButton));
						Actions action1 = new Actions(driver);
						action1.moveToElement(PerformerlistButton);
						action1.doubleClick().build().perform();
						driver.navigate().refresh();
		//				Thread.sleep(3000);  Commented by Rupesh - Added for the Optimization
						helpers.CommonFunctions.PageLoadExternalwait(driver);
					}
				}
				else
				{
					wait.until(ExpectedConditions.elementToBeClickable(Emailaddress));
					scrolltoElement(driver, Emailaddress);
					wait.until(ExpectedConditions.elementToBeClickable(Emailaddress)).sendKeys(UserName);
					wait.until(ExpectedConditions.elementToBeClickable(Password));
					scrolltoElement(driver, Password);
					wait.until(ExpectedConditions.elementToBeClickable(Password)).sendKeys(PWD);
					scrolltoElement(driver, Loginbutton);
					Actions action = new Actions(driver);
					action.moveToElement(Loginbutton);
					action.doubleClick().build().perform();	
					helpers.CommonFunctions.PageLoadExternalwait(driver);
				//	driver.navigate().refresh();
					
				}
			
			}	
			
		}
		catch(Exception e)
		{
			System.out.println("The PL application is not log in sucessfully");
			e.printStackTrace();
		}
		return PageFactory.initElements(driver, expectedPage);
	}


	public PerformerList logintoPL(String Key, String env)  {
		
		
			return logintoAccount(Key, env, PerformerList.class);
		
		
	}
	
	public MarketEntryApplication logintoME(String Key, String env)  {
		
		
		return logintoAccount(Key, env, MarketEntryApplication.class);
	
	
}
	
	public SelectOrganisation logintoOP(String Key, String env)  {
		
		return logintoAccount(Key, env, SelectOrganisation.class);
	}
	
	public GMPHome logintoGMP(String Key, String env)  {
		
		return logintoAccount(Key, env, GMPHome.class);
	}
	
	public OPHomePage logintoOP_Home(String Key, String env)  {
		
		return logintoAccount(Key, env, OPHomePage.class);
	}
	
	public Registration clickOnRegistrationLink() throws InterruptedException
	{
		Select dropdown = new Select(Registerdropdown);
		dropdown.selectByVisibleText("Register as a Performers List Applicant");
		wait.until(ExpectedConditions.elementToBeClickable(RegisterLink)).click();
	//	Thread.sleep(5000);
	//	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
		
		return new Registration(driver);
	}
	
	public LoginScreen logout()
	{
		wait.until(ExpectedConditions.elementToBeClickable(Logout)).click();
		return new LoginScreen(driver);
	}
	
	public NHSCViewApp logintoNHSE(String Key, String env)  {
		
		return logintoAccount(Key, env, NHSCViewApp.class);
	}
    
	public PortalHome logintoCS(String Key, String env)  {
		
		return logintoAccount(Key, env, PortalHome.class);
	}
	
	public <T> T logintoGPP(String Key, String env,Class<T> expectedPage)  {
		
		return logintoAccount(Key, env, expectedPage);
	}
	public SelectOrganisation logintoGPP_Org(String Key, String env)  {
		
		return logintoAccount(Key, env, SelectOrganisation.class);
	}

	public MarketEntryApplication doLogintoME(String key,String env, String methodName)  {
		
		
		return doLogin(key,env,methodName, MarketEntryApplication.class);
	
	
}
	private <T> T doLogin(String Key,String env,String methodName,Class<T> expectedPage) 
	{
		try{
			String UserName = null;
			String PWD = null;
			if(Key.equalsIgnoreCase("MEPLAPPLICANT"))
			{
				UserName = ExcelUtilities.getKeyValueByPosition("ME_Register_User_Details.xlsx", "Details", methodName, "UserName");
				PWD = ExcelUtilities.getKeyValueByPosition("ME_Register_User_Details.xlsx", "Details", methodName, "Password");
			}
			else
			{
				UserName = ConfigurationData.getUserDetails(env, Key+"_ID");
				PWD = ConfigurationData.getUserDetails(env, Key+"_PASSWORD");
			}
			String TitleName = driver.getTitle();
			if(TitleName.startsWith("PCSS"))
			{					  
				wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL));
				scrolltoElement(driver, EmailaddressPL);
				wait.until(ExpectedConditions.elementToBeClickable(EmailaddressPL)).sendKeys(UserName);
				wait.until(ExpectedConditions.elementToBeClickable(PasswordPL));
				scrolltoElement(driver, PasswordPL);
				wait.until(ExpectedConditions.elementToBeClickable(PasswordPL)).sendKeys(PWD);
				scrolltoElement(driver, Loginbutton);
				wait.until(ExpectedConditions.elementToBeClickable(Loginbutton)).click();
				//	driver.navigate().refresh();
				Thread.sleep(1500);
				boolean ispresent = driver.findElements(By.xpath("//div[@class='panel-body']//p[2]/a")).size() != 0;
				if(ispresent)
				{
					wait.until(ExpectedConditions.elementToBeClickable(PerformerlistButton));
					Actions action1 = new Actions(driver);
					action1.moveToElement(PerformerlistButton);
					action1.doubleClick().build().perform();
					driver.navigate().refresh();
	//				Thread.sleep(3000);  Commented by Rupesh - Added for the Optimization
					helpers.CommonFunctions.PageLoadExternalwait(driver);
				}
			}
			else
			{
				wait.until(ExpectedConditions.elementToBeClickable(Emailaddress));
				scrolltoElement(driver, Emailaddress);
				wait.until(ExpectedConditions.elementToBeClickable(Emailaddress)).sendKeys(UserName);
				wait.until(ExpectedConditions.elementToBeClickable(Password));
				scrolltoElement(driver, Password);
				wait.until(ExpectedConditions.elementToBeClickable(Password)).sendKeys(PWD);
				scrolltoElement(driver, Loginbutton);
				Actions action = new Actions(driver);
				action.moveToElement(Loginbutton);
				action.doubleClick().build().perform();	
				helpers.CommonFunctions.PageLoadExternalwait(driver);
			//	driver.navigate().refresh();
				
			}			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		return PageFactory.initElements(driver, expectedPage);
	}


}
