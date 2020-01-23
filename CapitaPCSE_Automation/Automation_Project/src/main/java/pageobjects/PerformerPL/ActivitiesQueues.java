package pageobjects.PerformerPL;
import java.io.IOException;
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
public class ActivitiesQueues extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="btnHourRetirementApprove")
	WebElement AppproveButton;
	
	
	@FindBy(id="ApproveChangeComment")
	WebElement CommentBox;
	
	@FindBy(xpath="//*[@id='divApproveChange']/div[2]/div[2]/input")
	WebElement ConfirmApprove;
		
	@FindBy(id="btnHourRetirementReject")
	WebElement RejectButton;
	
	@FindBy(id="RejectChangeComment")
	WebElement RejectCommentBox;
	

	
	@FindBy(xpath="//*[@id='divRejectChange']/div[2]/div[2]/input")
	WebElement ConfirmReject;
	
	@FindBy(id="NHSE_ChangeRequestQueue")
	WebElement ActivitiesQueueTab;
	
	@FindBy(xpath="//*[@id='DivContainer']/form/div[2]/div[2]/input")
	WebElement ApprovePratice;
	
	@FindBy(xpath="//*[@id='DivContainer']/form/div[2]/div[1]/input")
	WebElement RejectPratice;	
	
	@FindBy(id="ApproveChangeForLocalOfficeMoveComment")
	WebElement CommentPraticechangeBox;
	
	@FindBy(xpath="//*[@id='divApproveFinalChange']/div[2]/div[2]/input")
	WebElement ConfirmPratice;
	
	@FindBy(xpath="//*[@id='divRejectChange']/div[2]/div[2]/input")
	WebElement RejectConfirmPratice;
	
	
	
	@FindBy(id="btnConfirm")
	WebElement ConfirmLocaloffice;
	
	@FindBy(id="FurtherDiscussionNotes")
	WebElement DiscussionNotes;
	
	@FindBy(id="LeavingOfficeContactName")
	WebElement CommentLocaloffice;
	
	@FindBy(id="LeavingOfficeContactNumber")
	WebElement LeavingContanctNumber;
	
	@FindBy(id="LeavingOfficeContactEmail")
	WebElement LeavingEmail;	
	
	@FindBy(id="btnLeavingOfficeSubmit")
	WebElement LeaveSubmit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(name="btnNewRecord")
	WebElement ApproveGainingLocaloffice;
	
	@FindBy(name="GainingComment")
	WebElement GainingCommentLocaloffice;
	
	@FindBy(xpath="//input[@class='btn btn-success btn-largesize']")
	WebElement Confirmgaininglocaloffice;
	

	@FindBy(id="btnWithdrawApprove")
	WebElement WithdrawApprove;
	
	@FindBy(id="ApproveChangeComment")
	WebElement WithdrawApproveComment;
	
	@FindBy(xpath="//*[@id='divApproveChange']/div[2]/div[2]/input")
	WebElement WithdrawApproveConfirm;
	
	@FindBy(id="btnWithdrawReject")
	WebElement RejectApprove;
	
	@FindBy(id="RejectChangeComment")
	WebElement WithdrawRejectComment;
	
	@FindBy(xpath="//*[@id='divRejectChange']/div[2]/div[2]/input")
	WebElement WithdrawRejectConfirm;
	
	
	
	
	
	
	
	public ActivitiesQueues(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public ActivitiesQueues clickonApproveHours() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(AppproveButton));
			scrolltoElement(driver, AppproveButton);
			Actions actions = new Actions(driver);
			actions.moveToElement(AppproveButton);
	    	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Approve on Activities is not clicked");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues Clickonconfirm() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(CommentBox)).sendKeys("Automation Purpose");
			scrolltoElement(driver, ConfirmApprove);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmApprove);
	    	actions.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for 24 Hours commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public boolean verifyApproveButton() {
		boolean ApproveNHSE = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//*[@id='btnHourRetirementApprove']")).size() != 0;
			if(ispresent)
			{
				ApproveNHSE = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The activities queue is not captured");
		}
		return ApproveNHSE;
	}

	public ActivitiesQueues clickonRejectHours() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(RejectButton));
			scrolltoElement(driver, RejectButton);
			Actions actions = new Actions(driver);
			actions.moveToElement(RejectButton);
	    	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Reject Button on Activities is not clicked");
		}
		return new ActivitiesQueues(driver);
	}
	
	public ActivitiesQueues Clickonconfirm_Reject() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(RejectCommentBox)).sendKeys("Automation Purpose");
			scrolltoElement(driver, ConfirmReject);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmReject);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(6000);
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for 24 Hours commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public boolean verifyRejectButton() {
		boolean ApproveNHSE = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//*[@id='btnHourRetirementReject']")).size() != 0;
			if(ispresent)
			{
				ApproveNHSE = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The activities queue is not captured");
		}
		return ApproveNHSE;
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ActivitiesQueueTab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}
	
	public ActivitiesQueues clickonApprovePractice() {
		try{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(ApprovePratice));
			scrolltoElement(driver, ApprovePratice);
			Actions actions = new Actions(driver);
			actions.moveToElement(ApprovePratice);
	    	actions.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Approve on Activities is not clicked");
		}
		return new ActivitiesQueues(driver);
	}
	
	public ActivitiesQueues Clickonconfirm_Pratice() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(CommentPraticechangeBox)).sendKeys("Automation Purpose");
			scrolltoElement(driver, ConfirmPratice);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmPratice);
	    	actions.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for 24 Hours commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}
	
	public boolean verifyApproveButton_Practice() {
		boolean ApproveNHSE = false;
		try{
			Thread.sleep(3000);
			boolean ispresent = driver.findElements(By.xpath("//*[@id='DivContainer']/form/div[2]/div[2]/input")).size() != 0;
			if(ispresent)
			{
				ApproveNHSE = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The activities queue is not captured");
		}
		return ApproveNHSE;
	}
	
	public boolean verifyRejectButton_Practice() {
		boolean ApproveNHSE = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//*[@id='DivContainer']/form/div[2]/div[1]/input")).size() != 0;
			if(ispresent)
			{
				ApproveNHSE = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The activities queue is not captured");
		}
		return ApproveNHSE;
	}

	public ActivitiesQueues Clickonconfirm_LocalOffice() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ConfirmLocaloffice);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmLocaloffice);
	    	actions.doubleClick().build().perform();
			
			
		}
		catch(Exception e)
		{
			System.out.println("The Confirm button is not clicked");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues FillDeclarationForm() {
		try{
			Thread.sleep(2000);
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(DiscussionNotes)).clear();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(DiscussionNotes)).sendKeys("Automation Purpose");
			wait.until(ExpectedConditions.elementToBeClickable(CommentLocaloffice)).clear();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(CommentLocaloffice)).sendKeys("Automation Purpose");
			wait.until(ExpectedConditions.elementToBeClickable(LeavingContanctNumber)).clear();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(LeavingContanctNumber)).sendKeys("1234567890");
			wait.until(ExpectedConditions.elementToBeClickable(LeavingEmail)).clear();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(LeavingEmail)).sendKeys("abc@mastek.com");
		}
		catch(Exception e)
		{
			System.out.println("The Fill Declaration form is not filled properly");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues clickonSubmit_leaveLocalOffice() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, LeaveSubmit);
			Actions actions = new Actions(driver);
			actions.moveToElement(LeaveSubmit);
	    	actions.doubleClick().build().perform();
			
			
		}
		catch(Exception e)
		{
			System.out.println("The Submit button - Declaration form is not clicked");
		}
		return new ActivitiesQueues(driver);
	}
	
	public ActivitiesQueues clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new ActivitiesQueues (driver);
	}

	public boolean verifyConfirmLocaloffice() {
		boolean ConfirmLocaloffice = false;
		try{
			Thread.sleep(3000);
			boolean ispresent = driver.findElements(By.xpath("//*[@id='btnConfirm']")).size() != 0;
			if(ispresent)
			{
				ConfirmLocaloffice = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The activities queue is not captured");
		}
		return ConfirmLocaloffice;
	}

	public ActivitiesQueues clickonApproveLocalOfficegaining() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, ApproveGainingLocaloffice);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ApproveGainingLocaloffice);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
		System.out.println("The Approve for gaining local office is not clicked");
			
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues ClickonconfirmgainingLocaloffice() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(GainingCommentLocaloffice));
			scrolltoElement(driver, GainingCommentLocaloffice);
			wait.until(ExpectedConditions.elementToBeClickable(GainingCommentLocaloffice)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(GainingCommentLocaloffice)).sendKeys("Automation Purpose");
			wait.until(ExpectedConditions.elementToBeClickable(Confirmgaininglocaloffice));
			scrolltoElement(driver, Confirmgaininglocaloffice);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Confirmgaininglocaloffice);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait(driver);
			
		}
		catch(Exception e)
		{
			
			System.out.println("The Confirm for gaining local office is not clicked");
		
		}
		return new ActivitiesQueues(driver);
	}

	public boolean verifyconfirmgainingLocaloffice() {
		boolean ConfirmgainingLocaloffice = false;
		try{
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			boolean ispresent = driver.findElements(By.xpath("//*[@name='btnNewRecord']")).size() != 0;
			if(ispresent)
			{
				ConfirmgainingLocaloffice = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The confirm gaining local office is not captured");
		}
		return ConfirmgainingLocaloffice;
	}

	public ActivitiesQueues clickonRejectPractice() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, RejectPratice);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(RejectPratice);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The ");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues ClickonRejectConfirm_Pratice() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(RejectCommentBox)).sendKeys("Automation Purpose");
			scrolltoElement(driver, RejectConfirmPratice);
			Actions actions = new Actions(driver);
			actions.moveToElement(RejectConfirmPratice);
	    	actions.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for 24 Hours commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues clickonApproveWithdraw() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, WithdrawApprove);
			Actions actions = new Actions(driver);
			actions.moveToElement(WithdrawApprove);
	    	actions.doubleClick().build().perform();
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(1000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The withdrawal Approve button is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public ActivitiesQueues ClickonconfirmWithdraw() {
		try{
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(WithdrawApproveComment)).sendKeys("Automation Purpose");
			scrolltoElement(driver, WithdrawApproveConfirm);
			Actions actions = new Actions(driver);
			actions.moveToElement(WithdrawApproveConfirm);
	    	actions.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for withdrawal commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public boolean verifyWithdrawApproveButton() {
		boolean WithdrawApproveButton = false;
		try{
			boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(1000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			boolean ispresent = driver.findElements(By.xpath("//*[@id='btnWithdrawApprove']")).size() != 0;
			if(ispresent)
			{
				WithdrawApproveButton = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The WithdrawApprove Button is not captured");
		}
		return WithdrawApproveButton;
	}

	public ActivitiesQueues clickonRejectWithdraw() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, RejectApprove);
			Actions actions = new Actions(driver);
			actions.moveToElement(RejectApprove);
	    	actions.doubleClick().build().perform();
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(1000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Reject of NHSE - Withdrawal  is not captured");
		}
		return new ActivitiesQueues(driver);
	}
	
	public ActivitiesQueues ClickonRejectConfirmWithdraw() {
		try{
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(WithdrawRejectComment)).sendKeys("Automation Purpose");
			scrolltoElement(driver, WithdrawRejectConfirm);
			Actions actions = new Actions(driver);
			actions.moveToElement(WithdrawRejectConfirm);
	    	actions.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Comment for withdrawal commitment is not captured");
		}
		return new ActivitiesQueues(driver);
	}

	public boolean verifyWithdrawRejectButton() {
		boolean WithdrawApproveButton = false;
		try{
			boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(1000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			boolean ispresent = driver.findElements(By.xpath("//*[@id='btnWithdrawReject']")).size() != 0;
			if(ispresent)
			{
				WithdrawApproveButton = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The WithdrawApprove Button is not captured");
		}
		return WithdrawApproveButton;
	}

}
