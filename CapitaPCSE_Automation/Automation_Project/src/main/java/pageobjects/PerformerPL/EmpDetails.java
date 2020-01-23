package pageobjects.PerformerPL;
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

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
public class EmpDetails extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="btnAddPractice")
	WebElement AddPratice;
		
	@FindBy(id="Practice_Name")
	WebElement PostalCodeText;
	
	@FindBy(id="BtnSearchCounAdd")
	WebElement Search;
	
	@FindBy(xpath="//ul[@id='PracticeAddressResult']/li/a")
	WebElement FirstResult;
	
	@FindBy(id="LevelOfCommitment")
	WebElement Commitment;
	
	@FindBy(id="StartDate")
	WebElement Startdate;
	
	@FindBy(name="btnAddEditRecord")
	WebElement AddthisPratice;
	
	@FindBy(name="Submit_Decision")
	WebElement Submit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(id="PerformerMain")
	WebElement HomeMaintab;
	
	@FindBy(xpath="//button[@id='EditLocalOfficeEmploymentDetails']")
	WebElement EditPratice;
	
	@FindBy(xpath="//input[@id='LocalOfficeEmploymentDetails_0__IsChangeLevelOfCommit']")
	WebElement CommitmentRadioButton;
	
	@FindBy(xpath="//input[@id='LocalOfficeEmploymentDetails_0__ProposedLevelOfCommmitment']")
	WebElement ProposedCommitment;
	
	
	
	
	public EmpDetails(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public EmpDetails AddPratice(String ValueCommitment) {
		try{
			Thread.sleep(2000);
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			wait.until(ExpectedConditions.elementToBeClickable(AddPratice));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(AddPratice);
	    	actions1.doubleClick().build().perform();
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(PostalCodeText)).sendKeys("PL22DN");
			Actions actions2 = new Actions(driver);
	    	actions2.moveToElement(Search);
	    	actions2.doubleClick().build().perform();
			Thread.sleep(2000);
			Actions actions3 = new Actions(driver);
	    	actions3.moveToElement(FirstResult);
	    	actions3.doubleClick().build().perform();
			Thread.sleep(1000);
			helpers.CommonFunctions.ClickOnRadioButton("Principal", driver);
			wait.until(ExpectedConditions.elementToBeClickable(Commitment)).sendKeys(ValueCommitment);
			String todayDate = CommonFunctions.getDate_UK();
			String TomorrowDate = CommonFunctions.Tomorrowdate(todayDate, 2);
			Startdate.sendKeys(TomorrowDate);
			wait.until(ExpectedConditions.elementToBeClickable(AddthisPratice));
			Actions actions4 = new Actions(driver);
	    	actions4.moveToElement(AddthisPratice);
	    	actions4.doubleClick().build().perform();			
		}
	
		catch(Exception e)
		{
			System.out.println("The Employment details is not filled properly");
		}
		return new EmpDetails (driver);
	}

	public EmpDetails clickonSubmit() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Submit));
			Actions actions4 = new Actions(driver);
	    	actions4.moveToElement(Submit);
	    	actions4.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Submit on Emp details is not clicked");
		}
		return new EmpDetails(driver);
	}

	
	public EmpDetails clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(2000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new EmpDetails (driver);
	}
	
	public HomeTab clickonHometab() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, HomeMaintab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(HomeMaintab);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Home tab is not clicked");
		}
		return new HomeTab(driver);
	}

	public boolean VerifyPratice(String ExpectedValue) {
		boolean defaultPratice= false;
		try{
			Thread.sleep(2000);
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			wait.until(ExpectedConditions.elementToBeClickable(AddPratice));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(AddPratice);
	    	actions1.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				if(Radiobutton.getText().equalsIgnoreCase(ExpectedValue))
				{
					String ActualValue = Radiobutton.findElements(By.xpath("//input[@id='IsPrincipalGPAtPractice']")).get(0).getAttribute("value");
					System.out.println(ActualValue);
					if(ActualValue.equalsIgnoreCase("true"))
					{
						defaultPratice = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Default principal option is not captured");
		}
		return defaultPratice;
	}

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, HomeMaintab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public EmpDetails clickonedit() {
		try{
			scrolltoElement(driver, EditPratice);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(EditPratice);
	    	actions1.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The edit button on change practice is not captured");
		}
		return new EmpDetails(driver);
	}

	public EmpDetails addnewcommitment(String CommitmentValue) {
		try{
			String CommitmentRadioText = CommitmentRadioButton.getAttribute("value");
					if(CommitmentRadioText.equalsIgnoreCase("True"))
					{
						CommitmentRadioButton.click();
					}
					wait.until(ExpectedConditions.elementToBeClickable(ProposedCommitment)).sendKeys(CommitmentValue);
					
			
		}
		catch(Exception e)
		{
			System.out.println("The new commitment is not added properly");
		}
		return new EmpDetails(driver);
	}
}
