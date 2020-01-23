package pageobjects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PatientDetails {

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(linkText="PCSE")
	WebElement linkPCSE;

	@FindBy(linkText="Cervical")
	WebElement linkCervical;

	@FindBy(linkText="Help")
	WebElement linkHelp;

	@FindBy(linkText="Patient Search")
	WebElement linkPatientSearch;

	@FindBy(linkText="PNL")
	WebElement linkPNL;

	@FindBy(linkText="Notification Lists")
	WebElement linkNotificationLists;

	@FindBy(linkText="Colposcopy")
	WebElement linkColposcopy;

	@FindBy(linkText="HPV")
	WebElement linkHPV;

	@FindBy(id="NHSNumber")
	WebElement NHSNo;

	@FindBy(id="Search")
	WebElement searchBtn;

	@FindBy(css="table[id='pcss-patients']")
	WebElement searchResulttbl;

	@FindBy(xpath="//table[@id='pcss-patients']/tbody/tr/td[1]/a")
	WebElement firstSearchResultItem;
	
	@FindBy(xpath="//td[@id='nhsNumber']/b")
	WebElement nhsNo;

	public PatientDetails(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public String getPatientNHSFromPatientDetails()
	{
		String nhs = null;
		nhs = nhsNo.getText();	
		
		return nhs;
		
		
	}








}
