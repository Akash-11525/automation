package pageobjects.ProcessPL;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import pageobjects.LoginScreen;
public class FacetoFaceAppointment extends Support{
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(xpath="//*[@id='IsNetTeamChecksComplete']")
	WebElement NetTeamCheck;
	
	@FindBy(name="CaseOfficerReview")
	WebElement Caseofficereviewtab;
	
	@FindBy(xpath="//div[@id='dvProcessAppContainer']/div/div/div/span[contains(@class,'h3')]")
	WebElement Caseofficereviewtab1;
	
	@FindBy(name="Save")
	WebElement SaveButton;
	
	@FindBy(partialLinkText="Log out")
    WebElement Logout;
	
	@FindBy(xpath="//div[@id='divApplicantDetails']/div/div/div[2]/strong")
	WebElement Casestatus;
	
	@FindBy(id="NetTeamChecksExplanation")
	WebElement NetTeamCheckComment;
	
	
	
	public FacetoFaceAppointment(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public FacetoFaceAppointment clickonNetTeamComplete() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, NetTeamCheck);
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck)).click();
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheckComment)).sendKeys("Test");
			
		/*	Actions actions = new Actions(driver);
	    	actions.moveToElement(NetTeamCheck);
	    	actions.click().build().perform();*/
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton));
	    	Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(SaveButton);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
	    	
		}
		catch(Exception e)
		{
			System.out.println("The net team complete checkbox is not clicked");
		}
		return new FacetoFaceAppointment(driver);
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

	public void ScreenshotofFaceapp(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Casestatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);

		
	}
	
	public LoginScreen logout()
	{
		wait.until(ExpectedConditions.elementToBeClickable(Logout)).click();
		return new LoginScreen(driver);
	}	
	

}
