package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleSearch 
{

	WebDriver driver;
    WebDriverWait wait;

	public GoogleSearch(WebDriver driver)
	{
        this.driver = driver;
        this.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(this.driver, 15);
		//PageFactory.initElements( driver, this);
		 PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
	}

	@FindBy(xpath="//div[@id='ssc']/span[1]/div")
	public WebElement SafeSearch;
	
	@FindBy(xpath="//div[@id='instant-radio']/div[2]/span")
	public WebElement instanceResult;
	
	
	@FindBy(xpath="//div[@id='nwc']/span[1]/div")
	public WebElement  resultsopen;
	
	@FindBy(xpath="//div[@id='form-buttons']/div[1]")
	public WebElement  save;
	
	public GoogleSearch SafeSearchClick() {
		SafeSearch.click();
		return new GoogleSearch(driver);
	}

	public GoogleSearch instanceResultClick() {
		instanceResult.click();
		return new GoogleSearch(driver);
	}

	public GoogleSearch ResultOpen() {
		resultsopen.click();
		return new GoogleSearch(driver);
	}
	
	public GoogleSearch Save()
	{
		save.click();
		return new GoogleSearch(driver);
	}

	public boolean acceptAlertMessage() 
	{
		boolean settingsSaved = false;
				
		 Alert alert=driver.switchTo().alert();		
	 		
         // Capturing alert message.    
         String alertMessage=driver.switchTo().alert().getText();		
         		
         // Displaying alert message		
         System.out.println(alertMessage);			
         		
         // Accepting alert		
         alert.accept();
         
         settingsSaved = true;
         
         return settingsSaved;
		
	}
}
