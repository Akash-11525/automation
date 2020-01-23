package pageobjects.PL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;


public class CreateNewApp extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(xpath="//*[@id='PersonalDetails']/i")
	WebElement TickMarkPersonaldetail;
	
	@FindBy(xpath="//*[@id='EmploymentHistory']/i")
	WebElement TickMarkEmployment;
	
	
	@FindBy(id="EmploymentHistory")
	WebElement EmploymentHistoryTab;
	
	@FindBy(id="Referees")
	WebElement RefereesTab;
	
	@FindBy(id="PersonalDetails")
	WebElement PersonalDetailsTab;
	
	@FindBy(id="ProfessionalCapacity")
	WebElement CapacityTab;
	
	
	@FindBy(id="InsuranceDetails")
	WebElement InsuranceTab;
	
	@FindBy(id="Nationality")
	WebElement NationalityTab;
	
	@FindBy(id="ProposedEmploymentDetails")
	WebElement EmployementTab;
	
	@FindBy(id="TraineeDetails")
	WebElement TraineeTab;
	
	@FindBy(id="ProfessionalQualification")
	WebElement QualificationTab;
	
	@FindBy(id="ProfessionalDetails")
	WebElement ProfessionalDetailsTab;
	
	@FindBy(id="AppraisalHistory")
	WebElement AppraisalTab;
	
	@FindBy(id="TrainingDetails")
	WebElement TrainingDetailsTab;
	
	@FindBy(id="CommunicationSkills")
	WebElement CommunicationSkillsTab;
	
	@FindBy(id="AdditionalInformation")
	WebElement AdditionalInfoTab;
	
	@FindBy(id="Declaration")
	WebElement DeclarationTab;
	
	@FindBy(id="PLI_BodyCorporateDeclarations")
	WebElement DeclarationBodyTab;
	
	@FindBy(id="PLI_DBS")
	WebElement PoliceCheckTab;
	
	@FindBy(id="NetTeamPreference")
	WebElement TeamPreferenceTab;
	
	@FindBy(id="OccupationalHealthClearance")
	WebElement HealthPreferenceTab;
	
	@FindBy(id="PLI_Undertakings")
	WebElement UndertakingTab;
	
	@FindBy(id="SubmitApplication")
	WebElement SubmitAppTab;
	
/*	@FindBy(xpath="//*[@id='DivContainer']/form/div[3]/div/button")
	WebElement SumittedAppButton;*/
	
	@FindBy(name="submit")
	WebElement SumittedAppButton;
	
	
	@FindBy(xpath="//div[@id='DivContainer']/form/div[2]/div/div")
	WebElement SumittedAppConfirmatonParagraph;
	
	
	
	
	
	public CreateNewApp(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public Boolean VerifyTickMarkPersonalDetail(String TabName) throws InterruptedException {
		Thread.sleep(2000);
		boolean TickMarkflag = false;
		boolean ispresent = false;
		System.out.println(TabName);
		String ActualidTick = TabName.replaceAll("\\s+","");
		try{
		//System.out.println((driver.findElements(By.xpath("//*[@id='"+ActualidTick+"']/i")).size()));
		ispresent = driver.findElements(By.xpath("//*[@id='"+ActualidTick+"']/i")).size() != 0;
		System.out.println(ispresent);
		if(ispresent)	
		{
			if(driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i")).isDisplayed())
		
			{
				WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i"));
				scrolltoElement(driver, TickMarkOntab);
			TickMarkflag = true;
			
			}
			
		
		}
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The Resident address manually button is not found " +e);
		}
		return TickMarkflag;
	}
	
	public Boolean VerifyTickMarkWithanotherTableId(String TabName) throws InterruptedException {
		/*Thread.sleep(3000);
		boolean TickMarkflag = false;
		System.out.println(TabName);
		//String ActualidTick = TabName.replaceAll("\\s+","");
		WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@id='"+TabName+"']/i"));		
		try{
		scrolltoElement(driver, TickMarkOntab);
		wait.until(ExpectedConditions.elementToBeClickable(TickMarkOntab));
		
		if(TickMarkOntab.isDisplayed())
		{
			TickMarkflag = true;
		}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Tick mark is not marked  " +TabName );
		}
		return TickMarkflag;*/
		Thread.sleep(2000);
		boolean TickMarkflag = false;
		boolean ispresent = false;
		System.out.println(TabName);
		String ActualidTick = TabName.replaceAll("\\s+","");
		try{
		//System.out.println((driver.findElements(By.xpath("//*[@id='"+ActualidTick+"']/i")).size()));
		ispresent = driver.findElements(By.xpath("//*[@id='"+ActualidTick+"']/i")).size() != 0;
		System.out.println(ispresent);
		if(ispresent)	
		{
			if(driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i")).isDisplayed())
		
			{
				WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i"));
				scrolltoElement(driver, TickMarkOntab);
			TickMarkflag = true;
			
			}
			
		}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The Tick Mark is not found " +e);
		}
		return TickMarkflag;
	}
	
	

	public NewApp_EmpHistory clickonEmploymentHistory() throws InterruptedException {
		try{
		
			wait.until(ExpectedConditions.elementToBeClickable(EmploymentHistoryTab));
			wait.until(ExpectedConditions.elementToBeClickable(EmploymentHistoryTab)).click();
		//	helpers.Support.PageLoadExternalwait(driver);
		}
			catch(NoSuchElementException e)
			{
				System.out.println("The employment history is not click it  " +e);
			}
			return new NewApp_EmpHistory(driver) ;
	}
	
	public  Referees clickonRefrees() throws InterruptedException {
		try{
		
			wait.until(ExpectedConditions.elementToBeClickable(RefereesTab)).click();
		//	helpers.Support.PageLoadExternalwait(driver); Commented by Rupesh
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Referees tab is not click it  " +e);
			}
			return new Referees(driver);
			
			
	}
	
	public  NewAppPersonalDetail clickonPersonaldetail() throws InterruptedException {
		try{
			
			wait.until(ExpectedConditions.elementToBeClickable(PersonalDetailsTab)).click();
			helpers.Support.PageLoadExternalwait(driver); //commented by Rupesh - No need to add spinner
		//	helpers.Support.PageLoadExternalwait(driver);
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Referees tab is not click it  " +e);
			}
			return new NewAppPersonalDetail(driver);
			
			
	}

	public Capacity clickonCapacity() throws InterruptedException {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(CapacityTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
			
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Referees tab is not click it  " +e);
			}
			return new Capacity(driver);
			
	}

	public InsuranceDetails clickonInsuranceTab() throws InterruptedException {
	try{
			wait.until(ExpectedConditions.elementToBeClickable(InsuranceTab)).click();
			helpers.Support.PageLoadExternalwait(driver);

			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Insurance tab is not click it  " +e);
			}
			return new InsuranceDetails(driver);
	}

	public Nationality clickonNationalityTab() throws InterruptedException {
		try{
			scrolltoElement(driver, NationalityTab);
			wait.until(ExpectedConditions.elementToBeClickable(NationalityTab)).click();
			helpers.Support.PageLoadExternalwait(driver);

			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Insurance tab is not click it  " +e);
			}
			return new Nationality(driver);
	}

	public Employment clickonEmploymentTab() throws InterruptedException {
		try{
		
			scrolltoElement(driver, EmployementTab);
			
			wait.until(ExpectedConditions.elementToBeClickable(EmployementTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Employment  tab is not click it  " +e);
			}
			return new Employment(driver);
	}

	public Trainee ClickOnTraineeTab() throws InterruptedException {
		try{
		
			scrolltoElement(driver, TraineeTab);
			wait.until(ExpectedConditions.elementToBeClickable(TraineeTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Trainee tab is not click it  " +e);
			}
			return new Trainee(driver);
	}

	public Qualification clickonQualification() throws InterruptedException {
		try{
			scrolltoElement(driver, QualificationTab);
			wait.until(ExpectedConditions.elementToBeClickable(QualificationTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Trainee tab is not click it  " +e);
			}
			return new Qualification(driver);
	}

	public ProfessionalDetails clickonProfessionaldetails() throws InterruptedException {
		try{
			scrolltoElement(driver, ProfessionalDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(ProfessionalDetailsTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Trainee tab is not click it  " +e);
			}
			return new ProfessionalDetails(driver);
	}

	public Appraisal clickonAppraisalTab() throws InterruptedException {
		try{
			scrolltoElement(driver, AppraisalTab);
			wait.until(ExpectedConditions.elementToBeClickable(AppraisalTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Apprisal tab is not click it  " +e);
			}
			return new Appraisal(driver);
	}

	public TrainingDetails clickonTrainingDetailsTab() throws InterruptedException {
		try{
			scrolltoElement(driver, TrainingDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(TrainingDetailsTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The TrainingDetails tab is not click it  " +e);
			}
			return new TrainingDetails(driver);
	}

	public CommunicationSkills ClickOnCommunicationSkillsDetails() throws InterruptedException {
		try{
			scrolltoElement(driver, CommunicationSkillsTab);
			wait.until(ExpectedConditions.elementToBeClickable(CommunicationSkillsTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The CommunicationSkills tab is not click it  " +e);
			}
			return new CommunicationSkills(driver);
	}

	public AdditionalInfo ClickOnAdditionalInfo() throws InterruptedException {
		try{
			scrolltoElement(driver, AdditionalInfoTab);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfoTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The AdditionalInformation tab is not click it  " +e);
			}
			return new AdditionalInfo(driver);
	}

	public Declaration ClickOndeclarationTab() throws InterruptedException {
		try{
			scrolltoElement(driver, DeclarationTab);
			wait.until(ExpectedConditions.elementToBeClickable(DeclarationTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Declaration tab is not click it  " +e);
			}
			return new Declaration(driver);
	}

	public DeclarationBody ClickOnDeclarationBody() throws InterruptedException {
		try{
			scrolltoElement(driver, DeclarationBodyTab);
			wait.until(ExpectedConditions.elementToBeClickable(DeclarationBodyTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The DeclarationBody tab is not click it  " +e);
			}
			return new DeclarationBody(driver);
	}

	public PoliceCheck ClickOnPoliceCheck() throws InterruptedException {
		try{
			scrolltoElement(driver, PoliceCheckTab);
			wait.until(ExpectedConditions.elementToBeClickable(PoliceCheckTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The PoliceCheck tab is not click it  " +e);
			}
			return new PoliceCheck(driver);
	}

	public TeamPreference ClickOnTeamPreference() throws InterruptedException {
		try{
			scrolltoElement(driver, TeamPreferenceTab);
			wait.until(ExpectedConditions.elementToBeClickable(TeamPreferenceTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The TeamPreference tab is not click it  " +e);
			}
			return new TeamPreference(driver);
	}

	public HealthClearance ClickOnHealthClearance() throws InterruptedException {
		try{
			scrolltoElement(driver, HealthPreferenceTab);
			wait.until(ExpectedConditions.elementToBeClickable(HealthPreferenceTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The HealthPreference tab is not click it  " +e);
			}
			return new HealthClearance(driver);
	}

	public Undertaking ClickOnUndertaking() throws InterruptedException {
		try{
			scrolltoElement(driver, UndertakingTab);
			wait.until(ExpectedConditions.elementToBeClickable(UndertakingTab)).click();
			helpers.Support.PageLoadExternalwait(driver);
		
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The Undertaking Tab is not click it  " +e);
			}
			return new Undertaking(driver);
	}

	public SubmitApplication ClickOnSubmitApp() throws InterruptedException {
		try{
			scrolltoElement(driver, SubmitAppTab);
			wait.until(ExpectedConditions.elementToBeClickable(SubmitAppTab)).click();
			helpers.Support.PageLoadExternalwait(driver);		
	   }
		catch(NoSuchElementException e)
		{
			System.out.println("The SubmitApplication Tab is not click it  " +e);
		}
		return new SubmitApplication(driver);
	}

	public PerformerList ClickOnSubmittedApp() throws InterruptedException {
		try{
			scrolltoElement(driver, SumittedAppButton);		
			wait.until(ExpectedConditions.elementToBeClickable(SumittedAppButton)).click();
			helpers.Support.PageLoadExternalwait(driver);			
		   }
			catch(NoSuchElementException e)
			{
				System.out.println("The SumittedAppButton is not click it  " +e);
			}
			return new PerformerList(driver);
	
	}

	public String getApplicationNumberonParagraph(String StartwithElement) throws InterruptedException{
		
	String ApplicationNumberOnparagraph = null;
	try {
		helpers.Support.PageLoadExternalwait(driver);
		String str = SumittedAppConfirmatonParagraph.getText();
		Pattern p = Pattern.compile(""+StartwithElement+"\\w+");
		java.util.regex.Matcher matcher = p.matcher(str);
		while (matcher.find())
		{
			ApplicationNumberOnparagraph = (matcher.group());
		    System.out.println(ApplicationNumberOnparagraph);
		}
	}
	
		catch(NoSuchElementException e)
		{
			System.out.println("The SumittedAppButton is not click it  " +e);
		}
		return ApplicationNumberOnparagraph;
	}

	public PerformerList clickonlink(String Text) throws InterruptedException {
		try{
            List<WebElement> linkes = driver.findElements(By.xpath("//*[@class='col-xs-12 col-sm-12  margin-top-10 bg-breadcrumb']/span/a"));
            for (WebElement Link:linkes)
            {
            	Thread.sleep(3000);
            	scrolltoElement(driver,Link);
            	if((Link.getText()).equalsIgnoreCase(Text))
            	{            	
            		Link.click();
            		break;
            		
            	}
            	}
            	
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The PCS home is not click " +e);
		}
		return new PerformerList(driver);
	}

	public boolean verifysubmittedmessage(String ApplicationNumberonParagraph) throws IOException, InterruptedException {
		boolean ActualSubmittedmessage = false;
		List<String> ExpectedSubmittedMessageList = null;
		try{
		
			scrolltoElement(driver, SumittedAppConfirmatonParagraph);
			
			String str = SumittedAppConfirmatonParagraph.getText();
			Thread.sleep(3000);
			ExpectedSubmittedMessageList = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "ExpectedSubmittedmsg", 1);
			System.out.println("The Expected Message is " +ExpectedSubmittedMessageList );
			String ActualSubmittedMessage = str.replaceFirst(""+ApplicationNumberonParagraph+".*?", "");			
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

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, SumittedAppConfirmatonParagraph);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}




}
