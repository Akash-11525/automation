package pageobjects.CS;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
public class PatientsearchPortal {

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath = "//*[contains(@id, 'contentIFrame')]")
	List<WebElement> iframes;
	
	@FindBy(id="NHSNumber")
	WebElement NHSNumber;
	
	//@FindBy(id="search") old value
	@FindBy(id="btnsearch")
	WebElement Search;
	
	
	public PatientsearchPortal(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		
		}


	public PatientsearchPortal EnterNHSNumberandclickOnresult(String CRMNHSNo) {
		// TODO Auto-generated method stub
		System.out.println(CRMNHSNo);
		wait.until(ExpectedConditions.elementToBeClickable(NHSNumber)).sendKeys(CRMNHSNo);
		wait.until(ExpectedConditions.elementToBeClickable(Search)).click();
		
		
		return new PatientsearchPortal(driver);	
	}


	public PatientpersonaldetailPortal FirstResultofsearchOnSubmitted(String InlineNhsNo) throws InterruptedException {
		Thread.sleep(1000);
		//driver.findElement(By.partialLinkText(InlineNhsNo)).click(); old locator
		WebElement firstRecord= driver.findElement(By.xpath("//table[@id='pcs-new-patient']//tbody//tr[1]/td[1]/a"));
		firstRecord.click();
		return new PatientpersonaldetailPortal(driver);
	}
	
	public String getNHSNoOfCeasePatientforcallRecall(String InlineNhsNo) {
		System.out.println(InlineNhsNo);
		
	    String NHSNo = InlineNhsNo.replaceAll("\\s+","");
	    System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
	      
	    return NHSNo;
		}

}
