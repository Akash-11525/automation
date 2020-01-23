package pageobjects.OP;

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
import testdata.ConfigurationData;

public class OPHomePage extends Support{

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SelectPerformer")
	WebElement selectPerformer;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement submitButton;
	
	@FindBy(css="input[class='form-control form-control input-sm']")
	WebElement searchTxtBox;
	
	@FindBy(xpath="//table[@id='myTable']/tbody/tr[1]/td[4]")
	WebElement firstClaimStaus;
	
	@FindBy(xpath="//table[@id='SearchTable']/tbody/tr[1]/td[1]")
	WebElement firstClaimNo;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSOne')]")
	WebElement GOSONEButton;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSThree')]")
	WebElement GOSTHREEButton;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSFive')]")
	WebElement GOSFIVEButton;
	
	@FindBy(css = "button[class*='button-image1']")
	WebElement makeAClaimButton;
	
	@FindBy(css = "button[class*='button-image2']")
	WebElement searchClaimButton;
	
	@FindBy(css = "button[class*='button-image3']")
	WebElement statementsButton;
	
	@FindBy(css = "button[class*='button-image4']")
	WebElement hC5Button;
	
	@FindBy(css = "button[id*='btnSearch']")
	WebElement searchButton;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[4]/button")
	WebElement adjustmentButton;
	
	@FindBy(xpath="//ul[@class='row text-right']//li")
	WebElement tabNames;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[2]/div[3]/button")
	WebElement adjustmentButton_NHSE;
	

	
	public OPHomePage(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public OPPatientDetails selectPerformer(String environment) throws InterruptedException
	{
		String performer = ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
		helpers.CommonFunctions.selectOptionFromDropDown(selectPerformer, performer);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public OPPatientDetails clickSubmitWithoutSelectingPerformer() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public String getStatusfromClaim(String clm)
	{
		String status = null;
		wait.until(ExpectedConditions.elementToBeClickable(searchTxtBox)).click();
		searchTxtBox.sendKeys(clm);
		
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimStaus));
		status = firstClaimStaus.getText();
		
		
		
		return status;
		
	}
	
	public String getFirstClaimNo()
	{
		String ClmNo = null;
		
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimNo));
		ClmNo = firstClaimNo.getText();
		System.out.println(ClmNo);
		return ClmNo;
	}
	
	public OPPatientDetails clickGOSONEButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSONEButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public OPMakeAClaim clickOnMakeAClaimButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(makeAClaimButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPMakeAClaim(driver);
	}

	public OPSearchForClaim clickOnSearchClaimButton() throws InterruptedException
	{
		Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(searchClaimButton)).click();
		return new OPSearchForClaim(driver);
	}
	
	public AdjustmentHomePage clickOnAdjustmentButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(adjustmentButton)).click();
		return new AdjustmentHomePage(driver);
	}
	
	public OPStatement redirectToStatementPage(String environment)
	{
		String statementURL= ConfigurationData.getRefDataDetails(environment, "PORTALSTATEMENT");
		driver.navigate().to(statementURL);
		return new OPStatement(driver);
		
	}
	
	public <T>T ClickonTab(String TabName,Class<T>expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			ActualTabNames =driver.findElements(By.xpath("//ul[@class='nav navbar-nav']//li"));
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
			 Thread.sleep(3000);
	}
	catch(Exception e)
	{
		System.out.println("The Sub menu tab is not clicked"+TabName);
	}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	public <T>T ClickOnPageHeader(String TabName,Class<T>expectedPage) {
		List<WebElement> ActualTabNames = null;
		try 
		{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(tabNames));
			//	wait.until(ExpectedConditions.elementToBeClickable(tabNames));
			System.out.println("TAB NAMES: "+tabNames);
			ActualTabNames =driver.findElements(By.xpath("//ul[@class='row text-right']//li"));
			System.out.println(ActualTabNames);
			 for (WebElement ActualTabName:ActualTabNames)
			 {
				 scrolltoElement(driver, ActualTabName);
				 String TabNameOnPortal = ActualTabName.getText();
				 System.out.println(TabNameOnPortal);
				 if(TabNameOnPortal.contains(TabName))
				 {
				 	Actions action = new Actions(driver);
					action.moveToElement(ActualTabName);
					System.out.println("Moved to desired Element: "+ActualTabName.getText());

					if(TabName.equalsIgnoreCase("Messages")){
						Thread.sleep(10000);
						action.doubleClick().build().perform();
					}else{
						action.doubleClick().build().perform();
						System.out.println("double click action is performed on element");
					}
					
					break;
				 }
			 }
	}
	catch(Exception e)
	{
		System.out.println("The Sub menu tab is not clicked: "+TabName);
	}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	public boolean verifyHeaderPresence(String TabName) {
		List<WebElement> ActualTabNames = null;
		boolean isPresent= false;
		wait.until(ExpectedConditions.elementToBeClickable(tabNames));
		ActualTabNames =driver.findElements(By.xpath("//ul[@class='row text-right']//li"));
		System.out.println(ActualTabNames);
		 for (WebElement ActualTabName:ActualTabNames)
		 {
			 scrolltoElement(driver, ActualTabName);
			 String TabNameOnPortal = ActualTabName.getText();
			 System.out.println(TabNameOnPortal);
			 if(TabNameOnPortal.contains(TabName))
			 {
				System.out.println(ActualTabName.getText()+" Tab is present on UI");
				isPresent=true;
				break;
			 }
		 }
		return isPresent;
	}
	
	public AdjustmentHomePage clickOnAdjustmentButton_NHSE()
	{
		wait.until(ExpectedConditions.elementToBeClickable(adjustmentButton_NHSE)).click();
		return new AdjustmentHomePage(driver);
	}


}
