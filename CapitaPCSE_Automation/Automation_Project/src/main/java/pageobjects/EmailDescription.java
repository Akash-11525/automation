package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import helpers.WindowHandleSupport;
import utilities.ExcelUtilities;

public class EmailDescription extends Support{
	
	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(xpath="//td[@class='ms-crm-Email-Body']/div[1]")
	WebElement EmailBodyTableBody;
	
	@FindBy(xpath="//div/p[2]")
	WebElement EmailActivationLink;
	
	@FindBy(xpath="//div/p")
	WebElement ActivationRespsone;
	
	
		public EmailDescription(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		
		}
		
		public void getEmailActivationLink() throws InterruptedException 
		{
			String link = null;
			try{
			Thread.sleep(3000);
			String winHandleBefore = driver.getWindowHandle();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Email");
			//switchToRequiredIframe(uniqueClmIdValue, driver);
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(EmailBodyTableBody)).click();
			
			driver.switchTo().frame("descriptionIFrame");
			System.out.println("The driver to description Frame successfully.");
			
			
			link = wait.until(ExpectedConditions.visibilityOf(EmailActivationLink)).getText();
			System.out.println(link);
			driver.switchTo().defaultContent();
			driver.close();
						
			/*scrolltoElement(driver, clmStatusValue);
			String clmStatus = clmStatusValue.getText();
			//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "OPCLAIM", "Claim Status", clmStatus);
*/			
		//	driver.switchTo().window(winHandleBefore);
		//	driver.close();
			//return clmStatus;
			//ExcelUtilities.setKeyValueByPosition(fileName, sheetName, value, keyName, columnName);
			ExcelUtilities.setKeyValueinExcel("PerformerPortal.xlsx", "Register", "ActivationLink", link);
			String TestCaseName = ExcelUtilities.getTestCaseName();
			 ExcelUtilities.setValueByKeyInColumn("ReadWrite_TestData_PL.xlsx", "PLTestData", link, TestCaseName, "ActivationLink"); 
//			ExcelUtilities.setValueByKeyInColumn("ConfigurationDetails.xlsx", "PLTestData", link, TestCaseName, "ActivationLink"); 
			//ExcelUtilities.setKeyValueinExcel("ConfigurationDetails.xlsx", "PLTestData", userNameText, TestCaseName, "UserEmail");// Commented by Rupesh - For Selenium GRID
			//ExcelUtilities.setKeyValueinExcel("ConfigurationDetails.xlsx", "Config", "ActivationLink", link);// Commented by Rupesh - For Selenium GRID
	//		Thread.sleep(3000); Commented for Optimization 
			
			}
			catch (NullPointerException e)
			{
				System.out.println("The null pointer exception is occured");
			}
			catch(Exception e)
			{
				System.out.println("The somethings wrong on activation link " +e ); 
				
			}
		//	return link;

		}
		
		public void browseEmailActivationLink() throws InterruptedException
		{

			
		
		/*	String driverFilePath =  System.getProperty("user.dir") + File.separator + "exe";
			String lnk = getEmailActivationLink();
			((JavascriptExecutor)driver).executeScript("window.open()");
		    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(1));
		   // driver.get("http://google.com");
			String driverPathChrome = ""+driverFilePath+"\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPathChrome);
			driver = new ChromeDriver();
			driver.navigate().to(lnk);*/
			String res = wait.until(ExpectedConditions.visibilityOf(ActivationRespsone)).getText();
			System.out.println(res);
			//Thread.sleep(3000);
			
			//driver.close();

		}

	
	
		
	
	
	
	


}
