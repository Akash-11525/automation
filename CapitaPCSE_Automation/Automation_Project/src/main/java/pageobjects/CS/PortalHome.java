package pageobjects.CS;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageobjects.PatientDetails;

public class PortalHome {

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
	
	@FindBy(linkText="PNL")
	WebElement pnlLink;

	@FindBy(linkText="Colposcopy")
	WebElement linkColposcopy;

	@FindBy(linkText="HPV")
	WebElement linkHPV;

	@FindBy(id="NHSNumber")
	WebElement NHSNo;

	@FindBy(id="search")
	WebElement searchBtn;

	@FindBy(css="table[id='pcss-patients']")
	WebElement searchResulttbl;

	@FindBy(xpath="//table[@id='pcss-patients']/tbody/tr/td[1]/a")
	WebElement firstSearchResultItem;
	
	@FindBy(xpath="//a[contains(@href,'reregistered')]")
	WebElement registrationTab;
	
	@FindBy(xpath="//a[contains(@href,'nonResponders')]")
	WebElement nonRespondersTab;
	
	@FindBy(xpath= "//button[@type='button'][@class='btn btn-success dropdown-toggle'][1]")
	WebElement CSVTOPButton;
	
	@FindBy(linkText="Patient Search")
	WebElement Patientsearch;
	
	@FindBy(id="NHSNumber")
	WebElement NHSNumber;	
	
	public PortalHome(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public PortalHome clickPatientSearch()
	{
		wait.until(ExpectedConditions.elementToBeClickable(linkPatientSearch)).click();

		return new PortalHome(driver);
	}

	public PortalHome patientSearchByNHS(String nhs)
	{

		wait.until(ExpectedConditions.elementToBeClickable(NHSNo)).click();
		NHSNo.sendKeys(nhs);
		searchBtn.click();
		return new PortalHome(driver);
	}

	public PatientDetails selectPatientRecord(String nhs)
	{
		try {
			if(searchResulttbl.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				
			}
			else{
				System.out.println("No Search results are displayed.");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return new PatientDetails(driver);
	}


	public NotificationLists clickNotifiationList()
	{
		wait.until(ExpectedConditions.elementToBeClickable(linkNotificationLists)).click();

		return new NotificationLists(driver);
	}
	
	
	
	public PNLPage clickPNL()
	{
		wait.until(ExpectedConditions.elementToBeClickable(pnlLink)).click();
		wait.until(ExpectedConditions.elementToBeClickable(CSVTOPButton));
		return new PNLPage(driver);
	}


	public PatientsearchPortal clickPatientsearchClick()
	{
		wait.until(ExpectedConditions.elementToBeClickable(Patientsearch)).click();
		wait.until(ExpectedConditions.elementToBeClickable(NHSNumber));
		return new PatientsearchPortal(driver);
	}
	
	public ColposcopyDischargeReport clickColposcopy()
	{
		wait.until(ExpectedConditions.elementToBeClickable(linkColposcopy)).click();

		return new ColposcopyDischargeReport(driver);
	}
	
	public HPV_Vaccination clickHPV()
	{
		wait.until(ExpectedConditions.elementToBeClickable(linkHPV)).click();

		return new HPV_Vaccination(driver);
	}


}
