package pageobjects.ME;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import pageobjects.PL.PerformerHome;
public class MEAPP extends Support{
	WebDriver driver;
	WebDriverWait wait;
	

	@FindBy(id="SelectPerformer")
	WebElement SelectPerfomerdropdown;
	
	@FindBy(xpath="//button[@class='btn btn-success']")
	WebElement Submitbutton;
	
	
	public MEAPP(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public PerformerHome SelectPerformer(String reason) throws InterruptedException 
	{
		Select dropdown = new Select(SelectPerfomerdropdown);
		dropdown.selectByVisibleText(reason);
	
	
		return new PerformerHome(driver);
	}

	public CreatePharmacy clickonButton(String Text) {
		try{
			System.out.println(Text);
			helpers.CommonFunctions.ClickOnButton(Text, driver);
			}
			catch(Exception e)
			{
				System.out.println("The button is not present on ME create App page "+Text);
		
			}
			return new CreatePharmacy(driver);
	}

}
