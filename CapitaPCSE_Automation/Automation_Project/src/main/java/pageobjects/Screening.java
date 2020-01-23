
	package pageobjects;

	import java.util.concurrent.TimeUnit;

	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
	import org.openqa.selenium.support.PageFactory;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;



	public class Screening{
		WebDriver driver;

		WebDriverWait wait;
		
		
		
		
		@FindBy(css="input[id*=crmGrid_findCriteria]")
		WebElement srcCriTxt;
		
		@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
		WebElement srcCriIcon;
		
		@FindBy(xpath="//table[@id='gridBodyTable']//td[3]")
		WebElement firstRecord;
		
		
		public Screening(WebDriver driver) {
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 30);

			//This initElements method will create all WebElements

			PageFactory.initElements(this.driver, this);
	// TODO Auto-generated constructor stub
		}
		
	
		public ContactPatient searchAndSelectPatient(String nhsNumber) throws InterruptedException {
			
			driver.switchTo().frame("contentIFrame0");
			srcCriTxt.clear();
			
			srcCriTxt.sendKeys(nhsNumber);
			srcCriIcon.click();
			
			Thread.sleep(3000);
		/*	Actions action = new Actions(driver);
			//action.doubleClick(firstRecord).perform();
			action.moveToElement(firstRecord).build().perform();
			action.click().build().perform();*/
			
			wait.until(ExpectedConditions.elementToBeClickable(firstRecord)).click();
			
			
			
			
			/*wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
			
			firstRecord.click();*/
			
			
			return new ContactPatient(driver);
		}


	}
	
