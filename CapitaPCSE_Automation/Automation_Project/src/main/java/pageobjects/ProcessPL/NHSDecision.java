package pageobjects.ProcessPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;
public class NHSDecision extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="uploadDeclaration")
	WebElement Choosefile;
	
	@FindBy(id="browseBtn")
	WebElement Uploadbutton;
	
	@FindBy(id="DocumentNote")
	WebElement NotesText;
	
	@FindBy(name="AppReason")
	WebElement DeferReason;
	
	
	@FindBy(id="Confirm")
	WebElement ConfirmCheckbox;
	
	@FindBy(id="btnSubmitDecision")
	WebElement SubmitDecision;
	
	@FindBy(xpath="//div[@class='popover-content']/p")
	WebElement ParagraphConfirmation;
	
	@FindBy(xpath="//div[@class='btn-group']//a[2]")
	WebElement Continueconfirmation;
	
	@FindBy(xpath="//input[@id='ConfirmWithCond']")
	WebElement ConfirmCheckboxWithCondition;
	
	@FindBy(id="NHSDecisionDate")
	WebElement NHSEDate;
	
	
	
	
	@FindBy(xpath="//*[@id='divApplicantDetails']/div/div[1]/div[2]/strong")
	WebElement ApplicationStatus;
	
	public NHSDecision(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public NHSDecision ApproveApp(String Text) {
		try{
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio margin-top-10']/label"));
			System.out.println("total Radio buttons"+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{ 
				String RadioValue = Radiobutton.getText(); 
				if (RadioValue.equalsIgnoreCase(Text))
				{
					Actions actions4 = new Actions(driver);
		        	actions4.moveToElement(Radiobutton);
		        	actions4.doubleClick().build().perform();
					break;
				}
			}
			Thread.sleep(500);
			scrolltoElement(driver, Choosefile);
    		/*Actions actions4 = new Actions(driver);
        	actions4.moveToElement(Choosefile);
        	actions4.doubleClick().build().perform();*/
			
			scrolltoElement(driver, Choosefile);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Choosefile);
			jse.executeScript("arguments[0].click();", Choosefile);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";	
			Choosefile.sendKeys(filePath);
		/*	wait.until(ExpectedConditions.elementToBeClickable(Choosefile)).clear();
        	Thread.sleep(2000);
			String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.txt";				
			helpers.CommonFunctions.Uploadfile(filePath1, driver);
			Thread.sleep(3000);*/
			Actions actions9 = new Actions(driver);
        	actions9.moveToElement(Uploadbutton);
        	actions9.doubleClick().build().perform();
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadbutton)).click();
			helpers.Support.PageLoadExternalwait_Performer(driver);
			wait.until(ExpectedConditions.elementToBeClickable(NotesText)).sendKeys("Automation Purpose");
			if(Text.equalsIgnoreCase("Approve with conditions") ||Text.equalsIgnoreCase("Refuse")||Text.equalsIgnoreCase("Approve"))
			{
			wait.until(ExpectedConditions.elementToBeClickable(ConfirmCheckbox)).click();
			}
						if(Text.equalsIgnoreCase("Approve with conditions") ||Text.equalsIgnoreCase("Refuse"))
			{
				wait.until(ExpectedConditions.elementToBeClickable(ConfirmCheckboxWithCondition)).click();
			}
					if(Text.equalsIgnoreCase("Defer"))
					{
						wait.until(ExpectedConditions.elementToBeClickable(DeferReason)).sendKeys("Automation Purpose");
						
					}
					scrolltoElement(driver, NHSEDate);
					String todayDate = CommonFunctions.getDate_UK();
					NHSEDate.sendKeys(todayDate);
					
			
			}
		catch(Exception e)
		{
			System.out.println("The Approve application on NHS decison is not done");
		}
		
		return new NHSDecision(driver);
	}

	public String ClickonSubmitDecisionApprove(String StartwithElement) {
		String PLNoOnMessage = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(SubmitDecision));
			Actions actions = new Actions(driver);
        	actions.moveToElement(SubmitDecision);
        	actions.doubleClick().build().perform();
        	helpers.Support.PageLoadExternalwait_Performer(driver);
        		String str = ParagraphConfirmation.getText();
        		Pattern p = Pattern.compile(""+StartwithElement+"\\d+");
        		java.util.regex.Matcher matcher = p.matcher(str);
        		while (matcher.find())
        		{
        			PLNoOnMessage = (matcher.group());
        		    System.out.println(PLNoOnMessage);
        		}
        	}
	
		
		catch(Exception e)
		{
			System.out.println("The Submit Decision is not clicked");
		}
		return PLNoOnMessage;
	}
	
	public String RemovePerformerName(String StartwithElement) {
		String PerformerName = null;
		try{
		/*	wait.until(ExpectedConditions.elementToBeClickable(SubmitDecision));
			Actions actions = new Actions(driver);
        	actions.moveToElement(SubmitDecision);
        	actions.doubleClick().build().perform();*/
			String str = ParagraphConfirmation.getText();	
			Pattern p = Pattern.compile(""+StartwithElement+"\\w+");
        		java.util.regex.Matcher matcher = p.matcher(str);
        		while (matcher.find())
        		{
        			PerformerName = (matcher.group());
        		    System.out.println(PerformerName);
        		    break;
        		}
        	}
	
		
		catch(Exception e)
		{
			System.out.println("The Submit Decision is not clicked");
		}
		return PerformerName;
	}

	public Boolean verifysubmittedmessage(String PLONMessage , String Action) throws IOException, InterruptedException {
		boolean ActualSubmittedmessage = false;
		List<String> ExpectedSubmittedMessageList = null;
		try{
			scrolltoElement(driver, ParagraphConfirmation);
			String str = ParagraphConfirmation.getText();
			Thread.sleep(3000);
			if(Action.equalsIgnoreCase("Approve"))
			{
				ExpectedSubmittedMessageList = ExcelUtilities.getCellValuesInExcel("ProcessPortal.xlsx", "ConfirmationMessage", 1);
			}
			if(Action.equalsIgnoreCase("Refuse"))
			{
				ExpectedSubmittedMessageList = ExcelUtilities.getCellValuesInExcel("ProcessPortal.xlsx", "ConfirmationMessage", 2);
			}

			System.out.println("The Expected Message is " +ExpectedSubmittedMessageList );
			String ActualSubmittedMessage = str.replaceFirst(""+PLONMessage+".*?", "");
			System.out.println("The Actual Submitted message is " +ActualSubmittedMessage);
			String ExpectedSubmittedMessage = ExpectedSubmittedMessageList.toString();
			System.out.println(ExpectedSubmittedMessage);
			String ExpSubmittedMessage = ExpectedSubmittedMessage.replace("[","");
			ExpSubmittedMessage = ExpSubmittedMessage.replace("]","");
			System.out.println(ExpSubmittedMessage);
				
			if(ExpSubmittedMessage.equalsIgnoreCase(ActualSubmittedMessage))
			{
				ActualSubmittedmessage = true;
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The submitted message is not captured on submiited tab" +e);
		}
		return ActualSubmittedmessage;
	
	}
	
	public Boolean verifysubmittedmessage_NHSE(String PLONMessage ,String PerformerName1, String PerformerName2, String Action) throws IOException, InterruptedException {
		boolean ActualSubmittedmessage = false;
		List<String> ExpectedSubmittedMessageList = null;
		try{
			scrolltoElement(driver, ParagraphConfirmation);
			String str = ParagraphConfirmation.getText();
			Thread.sleep(500);
			if(Action.equalsIgnoreCase("Approve"))
			{
				ExpectedSubmittedMessageList = ExcelUtilities.getCellValuesInExcel("ProcessPortal.xlsx", "ConfirmationMessage", 1);
			}
			if(Action.equalsIgnoreCase("Refuse"))
			{
				ExpectedSubmittedMessageList = ExcelUtilities.getCellValuesInExcel("ProcessPortal.xlsx", "ConfirmationMessage", 2);
			}

			System.out.println("The Expected Message is " +ExpectedSubmittedMessageList );
			String ActualSubmittedMessage = str.replaceFirst(""+PLONMessage+".*?", "");
			String FinalActualSubmittedMessage1 = ActualSubmittedMessage.replaceFirst(""+PerformerName1+".*?", "");
			String FinalActualSubmittedMessage = FinalActualSubmittedMessage1.replaceFirst(""+PerformerName2+".*?", "");
			
			System.out.println("The Actual Submitted message is " +FinalActualSubmittedMessage);
			String ExpectedSubmittedMessage = ExpectedSubmittedMessageList.toString();
			System.out.println(ExpectedSubmittedMessage);
			String ExpSubmittedMessage = ExpectedSubmittedMessage.replace("[","");
			ExpSubmittedMessage = ExpSubmittedMessage.replace("]","");
			System.out.println(ExpSubmittedMessage);
			System.out.println(FinalActualSubmittedMessage);	
			if(ExpSubmittedMessage.trim().equalsIgnoreCase(FinalActualSubmittedMessage.trim()))
			{
				ActualSubmittedmessage = true;
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The submitted message is not captured on submiited tab" +e);
		}
		return ActualSubmittedmessage;
	
	}


	public NHSDecision clickonContinueconfirmation() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, Continueconfirmation);
			wait.until(ExpectedConditions.elementToBeClickable(Continueconfirmation));
			Actions actions = new Actions(driver);
        	actions.moveToElement(Continueconfirmation);
        	actions.doubleClick().build().perform();
            helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Continue on confiramation is not clicked");
		}
		return new NHSDecision(driver) ;
	}
	
	
	public String verifyApplicationStatus() {
		String AppStatus = null;
		try{
			helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			scrolltoElement(driver, ApplicationStatus);
		AppStatus =wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus)).getText();
			
		}
		catch(Exception e)
		{
			System.out.println("The Application Status is not captured");
		}


		return AppStatus;
	}

	public String  verifycheckbox() {
		String  RadioValue = null;
		try
		{
			Thread.sleep(2000);
		List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio margin-top-10']//label"));
		System.out.println("total Radio buttons "+Radiobuttons.size());
		for (WebElement Radiobutton : Radiobuttons)
		{ 
			Thread.sleep(1000);
			System.out.println(Radiobutton.getText()); 
			
			boolean checkedradio = Radiobutton.isSelected(); 
		//	boolean checkedradio = (Radiobutton.getAttribute("checked") == "Checked");
			scrolltoElement(driver, Radiobutton);
			if(checkedradio)
			{
				RadioValue = Radiobutton.getAttribute("Value"); 
				
			}
		}
		}
		catch(Exception e)
		{
			System.out.println("The Default checkbox is not captured");
		}
		return RadioValue;
	}

	public boolean verifycheckedradio(String ExpectedCheckedValue) {
		boolean defaultcheckbox = false;
		try{
			Thread.sleep(3000);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio margin-top-10']//label"));
			
			System.out.println("total Radio buttons "+Radiobuttons.size());
			Thread.sleep(1000);
			for (WebElement Radiobutton : Radiobuttons)
			{ 
				Thread.sleep(1000);
				System.out.println(Radiobutton.getText()); 
				if(Radiobutton.getText().equalsIgnoreCase(ExpectedCheckedValue))
				{
					System.out.println(Radiobutton.findElement(By.tagName("input")).getAttribute("checked"));
					
					if(Radiobutton.findElement(By.tagName("input")).getAttribute("checked").equalsIgnoreCase("true"))
					{
					defaultcheckbox = true;
					break;
					}
				}
			}
			/*if(ActualCheckedValue.toLowerCase().indexOf(ExpectedCheckedValue.toLowerCase()) != -1)
			{
				defaultcheckbox = true;
			}*/
		}
		catch(Exception e)
		{
			System.out.println("The Correct Default check box is not captured");
		}
		return defaultcheckbox;
	}

	public NHSDecision SubmitDecision() {
		try
		{
			scrolltoElement(driver, SubmitDecision);
			wait.until(ExpectedConditions.elementToBeClickable(SubmitDecision));
			Actions actions = new Actions(driver);
        	actions.moveToElement(SubmitDecision);
        	actions.doubleClick().build().perform();
        	Thread.sleep(3000);
        	driver.navigate().refresh();
        	Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("The Submit Decision is not clicked");
		}
		return new NHSDecision(driver) ;
	}

	public NHSDecision SubmitDecision_ApproveCondition() {
		try
		{
			scrolltoElement(driver, SubmitDecision);
			wait.until(ExpectedConditions.elementToBeClickable(SubmitDecision));
			Actions actions = new Actions(driver);
        	actions.moveToElement(SubmitDecision);
        	actions.doubleClick().build().perform();
        	Thread.sleep(1000);
		}
		catch(Exception e)
		{
			System.out.println("The Submit Decision is not clicked");
		}
		return new NHSDecision(driver) ;
	}
	
	public void Applicationstatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ApplicationStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

	public String RemovePerformerName2(String StartwithElement , String str) {
		String PerformerName = null;
		try{
		/*	wait.until(ExpectedConditions.elementToBeClickable(SubmitDecision));
			Actions actions = new Actions(driver);
        	actions.moveToElement(SubmitDecision);
        	actions.doubleClick().build().perform();*/
			
			Pattern p = Pattern.compile(""+StartwithElement+"\\w+");
        		java.util.regex.Matcher matcher = p.matcher(str);
        		while (matcher.find())
        		{
        			PerformerName = (matcher.group());
        		    System.out.println(PerformerName);
        		    break;
        		}
        	}
	
		
		catch(Exception e)
		{
			System.out.println("The Submit Decision is not clicked");
		}
		return PerformerName;
	}
	

}
