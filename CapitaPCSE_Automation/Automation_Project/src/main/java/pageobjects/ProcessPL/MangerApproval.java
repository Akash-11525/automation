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

public class MangerApproval extends Support {

	WebDriver driver;
	WebDriverWait wait;	
	

	@FindBy(name="PCSEManagerReviewComments")
	WebElement ManagerComment;
	
	@FindBy(name="btnApproveDossier")
	WebElement ApproveDossier;
	
	@FindBy(name="IsDossierApproved")
	WebElement DossierCheckBox;
	
	@FindBy(xpath="//div[@id='divApplicantDetails']/div/div[2]/div/div[1]/strong")
	WebElement ApplicationStatus;
	
	@FindBy(name="btnReturnToCaseOfficer")
	WebElement ReturnToCaseoff;
	
	@FindBy(xpath="//*[@id='divApplicantDetails']/div/div[1]/div[2]/strong")
	WebElement Casestatus;
	
	
	@FindBy(name="CaseOfficerReview")
	WebElement Caseofficereviewtab;
	
	public MangerApproval(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public Boolean verifyallcheckboxDisabled(String Text) {
		boolean Enabled = false;
	
		try
		{
		helpers.Support.PageLoadExternalwait_Performer(driver);
		List<WebElement> CheckBoxes=driver.findElements(By.xpath("//input[@type='checkbox']"));
        System.out.println("total Tabs "+CheckBoxes.size());
        for(WebElement CheckBox: CheckBoxes)
        {
        	
        	Thread.sleep(500);
        	scrolltoElement(driver, CheckBox);
        	System.out.println(CheckBox.getAttribute("id"));
        	if(!(CheckBox.getAttribute("id").equalsIgnoreCase(Text)))
        	{
        		if(CheckBox.isEnabled())
        		{
        			Enabled = true;
        	     	Thread.sleep(500);
        		}
        	}
        }
		}
		catch(Exception e)
		{
			System.out.println("The all check box is not captured");
		}
		return Enabled;
	}

	public MangerApproval clickonAppovedossier() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(ManagerComment));
			scrolltoElement(driver, ManagerComment);
			wait.until(ExpectedConditions.elementToBeClickable(ManagerComment)).sendKeys("Automation Purpose");
			wait.until(ExpectedConditions.elementToBeClickable(DossierCheckBox));
			wait.until(ExpectedConditions.elementToBeClickable(DossierCheckBox)).click();
			/*Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(DossierCheckBox);
	    	actions1.doubleClick().build().perform();*/
			
		}
		catch(Exception e)
		{
			System.out.println("The Click on Approve dossier is not happened");
		}
		return new MangerApproval(driver);
	}

	public Boolean verifyApproveDossier() {
		boolean approveDossierbutton = false;
		try{
			scrolltoElement(driver, ApproveDossier);
			helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			if(ApproveDossier.isEnabled())
			{
				approveDossierbutton = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Approve dossier is not captured");
		}
		return approveDossierbutton;
	}

	public MangerApproval clickonApproveDossierbutton() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(ApproveDossier));
			scrolltoElement(driver, ApproveDossier);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ApproveDossier);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
	    }
		catch(Exception e)
		{
			System.out.println("The ApproveDossier button is not clicked");
		}
		return new MangerApproval(driver);
	}
	
	

	public Boolean verifyapplicationstatus(String Text) {
		boolean AppStatusflag = false;
		String AppStatus = null;
		try{
			
		//	helpers.Support.PageLoadExternalwait_Performer(driver);
		AppStatus =wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus)).getText();
		if(AppStatus.equalsIgnoreCase(Text))
		{
			AppStatusflag = true;
		}
			
		}
		catch(Exception e)
		{
			System.out.println("The Application Status is not captured");
		}

	return AppStatusflag;
}

	public MangerApproval ReturntoCaseofficer() {
	try{
		scrolltoElement(driver, ManagerComment);
		wait.until(ExpectedConditions.elementToBeClickable(ManagerComment)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(ManagerComment)).sendKeys("Automation Purpose");
		wait.until(ExpectedConditions.elementToBeClickable(DossierCheckBox));
		wait.until(ExpectedConditions.elementToBeClickable(DossierCheckBox)).click();
		scrolltoElement(driver, ReturnToCaseoff);
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(ReturnToCaseoff);
    	actions1.doubleClick().build().perform();
    	Thread.sleep(2000);
    	driver.navigate().refresh();
    	Thread.sleep(3000);
	}
	catch(Exception e)
	{
		System.out.println("The Return to Case officer is not done");
	}
		return new MangerApproval(driver);
	}

	public boolean verifyCasestatus(String Text) {
		boolean status = false;
		try{
		scrolltoElement(driver, Casestatus);
		String CasestatusPL = wait.until(ExpectedConditions.elementToBeClickable(Casestatus)).getText();
		if(CasestatusPL.equalsIgnoreCase(Text))
		{	
			status = true;
		}
	}
	catch(Exception e)
	{
		System.out.println("The case status is not captured");
	}
		return status;
	}

	public void ScreenshotofStatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Casestatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public void Applicationstatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ApplicationStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");


		
	}

	public CaseofficerReview clickonCaseofficerreview() {
		try{
		scrolltoElement(driver, Caseofficereviewtab);
		wait.until(ExpectedConditions.elementToBeClickable(Caseofficereviewtab));
		Actions actions = new Actions(driver);
    	actions.moveToElement(Caseofficereviewtab);
    	actions.doubleClick().build().perform();
	}
	catch(Exception e)
	{
		System.out.println("The case officer review is not clicked");
	}
	// TODO Auto-generated method stub
	return new CaseofficerReview(driver);
	}

	
	

}
