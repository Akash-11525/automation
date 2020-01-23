package pageobjects.ProcessPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
public class CaseofficerReview extends Support{
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(name="divIsNetTeamChecksComplete")
	WebElement NetTeamCheck;
	
	@FindBy(name="btnSendToManager")
	WebElement SendtoManager;

	@FindBy(xpath="//div[@id='divApplicantDetails']/div/div/div[2]/strong")
	WebElement Casestatus;
	
	@FindBy(xpath="//div[@id='dvProcessAppContainer']/div/div/div/span[contains(@class,'h3')]")
	WebElement netTeamHeader;
	
	@FindBy(name="NHSEFacetoFaceChecks")
	WebElement ManagerApproval;
	
	
	
	
	public CaseofficerReview(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public Boolean verifyallcheckbox() {
		boolean checked = false;
			try	{
				Thread.sleep(2000);
				List<WebElement> CheckBoxs = driver.findElements(By.xpath("//input[@type='checkbox']"));
				System.out.println(CheckBoxs.size());
				Thread.sleep(1000);
				for(WebElement CheckBox : CheckBoxs) 
				{
					Thread.sleep(1000);
					if(CheckBox.isEnabled())
					{
						checked = true;
						Thread.sleep(500);
					}
					
				}
				}
		catch(Exception e)
		{
			System.out.println("The all checkbox on Case reviewer tab is not captured");
		}
		return checked;
	}

	public CaseofficerReview clickonSendmanager() {
		try
		{
			
			scrolltoElement(driver, SendtoManager);
			wait.until(ExpectedConditions.elementToBeClickable(SendtoManager));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(SendtoManager);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_ProcessApp(driver);
	    /*	driver.navigate().refresh();
	    	Thread.sleep(4000);*/
	    	
			
		}
		catch(Exception e)
		{
			System.out.println("The send manager is not clicked ");
		}
		// TODO Auto-generated method stub
		return new CaseofficerReview(driver) ;
	}

	public Boolean verifyCasestatus(String Text) {
	boolean status = false;
	int count = 0;
		try{
	//	helpers.CommonFunctions.PageLoadExternalwait(driver);
		scrolltoElement(driver, Casestatus);
	
		String CasestatusPL = wait.until(ExpectedConditions.elementToBeClickable(Casestatus)).getText();
		while (!CasestatusPL.equalsIgnoreCase(Text) && count < 5 )
		{
			Thread.sleep(2000);
			count = count + 1;
			CasestatusPL = wait.until(ExpectedConditions.elementToBeClickable(Casestatus)).getText();
			if(CasestatusPL.equalsIgnoreCase(Text))
			{	
				status = true;
				System.out.println("The value is matched for Count " +count);
				break;
			}
		}
		
	}
	catch(Exception e)
	{
		System.out.println("The case status is not captured");
	}
		return status;
	}

	public MangerApproval clickonMangerApproval() {
		try{
			scrolltoElement(driver, ManagerApproval);
			wait.until(ExpectedConditions.elementToBeClickable(ManagerApproval));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ManagerApproval);
	    	actions1.doubleClick().build().perform();
		
		}
		catch(Exception e)
		{
			System.out.println("The Manager Aprroval button is not clicked");
		}
		
		return new MangerApproval(driver);
		
	}

	public void ScreenshotofStatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Casestatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public void ScreenshotCheckbox(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Casestatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	

}
