package pageobjects.GPP.CI;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import pageobjects.Adjustments.AdjustmentHomePage;
import pageobjects.GPP.PMSAPMS.PMSinstructionScreen;
import pageobjects.GPP.QOF.QOFPaymentScreen;
import pageobjects.GPP.SC.StdClaimsPortal;

public class GPPHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//em[@class='fa fa-home']")
	WebElement btn_pcseHome;
	
	@FindBy(xpath="//a[@href='/ChildImmunisation/Cohorts/PracticeCohort']")
	WebElement childImmMenu;
	
	@FindBy(xpath="//a[@href='/StandardClaims/ClaimPortal']")
	WebElement stdClaimsMenu;

	@FindBy(xpath="//a[@href='/Adjustment/Adjustment']")
	WebElement adjustmentMenu;

	@FindBy(xpath="//a[@href='/GSUM']")
	WebElement gsumOptServMenu;
	
	@FindBy(xpath="//a[@href='/GSUM/VarianceHeader']")
	WebElement gsumVarReportMenu;
	
	@FindBy(xpath="//a[@href='/GSUM/GSUMRun']")
	WebElement gsumRunMenu;
	
	@FindBy(xpath="//a[@href='/QOFPayment']")
	WebElement qofPaymentMenu;
	
	@FindBy(xpath="//a[@href='/PMSAPMS/PaymentInstruction']")
	WebElement pmsApmsMenu;
	
	@FindBy(xpath="//a[@href='/StandardClaims/SCApproval']")
	WebElement stdClaimsMenu_Commissioner;
	
	public GPPHomePage(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public GPPPCSEPracticeCohort clickOnChildImmMenuButton()
	{
		System.out.println("Entered in CI click method");
		scrolltoElement(driver, childImmMenu);
		
		wait.until(ExpectedConditions.elementToBeClickable(childImmMenu)).click();
		return new GPPPCSEPracticeCohort(driver);
	}
	
	public StdClaimsPortal clickOnStdClaimsMenuButton()
	{
		System.out.println("Entered in SC click method");
		scrolltoElement(driver, stdClaimsMenu);
		wait.until(ExpectedConditions.elementToBeClickable(stdClaimsMenu)).click();
		return new StdClaimsPortal(driver);
	}
	
	public AdjustmentHomePage clickOnAdjustmentMenuButton()
	{
		System.out.println("Entered in Adj click method");
		scrolltoElement(driver, adjustmentMenu);
		wait.until(ExpectedConditions.elementToBeClickable(adjustmentMenu)).click();
		return new AdjustmentHomePage(driver);
	}
	
	public GPPPCSEPracticeCohort clickOnGSUMOptServicesMenuButton()
	{
		System.out.println("Entered in GSUM opt serv click method");
		scrolltoElement(driver, gsumOptServMenu);
		wait.until(ExpectedConditions.elementToBeClickable(gsumOptServMenu)).click();
		return new GPPPCSEPracticeCohort(driver);
	}
	
	public GPPPCSEPracticeCohort clickOnGSUMVarianceMenuButton()
	{
		System.out.println("Entered in GSUM variance click method");
		scrolltoElement(driver, gsumVarReportMenu);
		wait.until(ExpectedConditions.elementToBeClickable(gsumVarReportMenu)).click();
		return new GPPPCSEPracticeCohort(driver);
	}
	
	public GPPPCSEPracticeCohort clickOnGSUMRunMenuButton()
	{
		System.out.println("Entered in GSUMRun click method");
		scrolltoElement(driver, gsumRunMenu);
		wait.until(ExpectedConditions.elementToBeClickable(gsumRunMenu)).click();
		return new GPPPCSEPracticeCohort(driver);
	}
	
	public QOFPaymentScreen clickOnQofPaymentMenuButton()
	{
		System.out.println("Entered in Qof click method");
		scrolltoElement(driver, qofPaymentMenu);
		wait.until(ExpectedConditions.elementToBeClickable(qofPaymentMenu)).click();
		return new QOFPaymentScreen(driver);
	}
	
	public PMSinstructionScreen clickOnPmsNApmsMenuButton()
	{
		System.out.println("Entered in APMS click method");
		scrolltoElement(driver, pmsApmsMenu);
		wait.until(ExpectedConditions.elementToBeClickable(pmsApmsMenu)).click();
		return new PMSinstructionScreen(driver);
	}
	
	public StdClaimsPortal clickOnStdClaimsMenuButton_Commissioner()
	{
		System.out.println("Entered in SC click method");
		scrolltoElement(driver, stdClaimsMenu_Commissioner);
		wait.until(ExpectedConditions.elementToBeClickable(stdClaimsMenu_Commissioner)).click();
		return new StdClaimsPortal(driver);
	}
	
	public <T> T ClickonTab(String TabName,Class<T> expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//div[@class='row-fluid child-navbar']//li"));
			System.out.println(ActualTabNames);
			
			 for (WebElement ActualTabName:ActualTabNames)
			 {
				 scrolltoElement(driver, ActualTabName);
				 String TabNameOnPortal = ActualTabName.getText();
				 if(TabNameOnPortal.equalsIgnoreCase(TabName))
				 {
					 	Actions action = new Actions(driver);
						action.moveToElement(ActualTabName);
						action.doubleClick().build().perform(); 
						break;
				 }
				
				 
			 }
	}
	catch(Exception e)
	{
		System.out.println("The Sub menu tab is not clicked"+TabName);
	}
		return PageFactory.initElements(driver, expectedPage);
	}

}
