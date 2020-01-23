package pageobjects.ProcessPL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
public class ProcessIndex extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(xpath="//*[@id='accordion']/div/div[1]/h4/a")
	WebElement ProcessIndexLink;
	
	@FindBy(xpath="//*[@id='PersonalDetails']")
	WebElement PersonalDetailsTab;
	
	@FindBy(xpath="//*[@id='TitleCode']")
	WebElement TitleCodePersonaldetails;
	
	
	@FindBy(xpath="//*[@id='EmploymentHistory']")
	WebElement EmploymentHistoryTab;
	
	@FindBy(xpath="//*[@id='upload1']")
	WebElement ChooseButtonEmp;	
	
	@FindBy(xpath="//*[@id='Referees']")
	WebElement RefereesTab;

	@FindBy(xpath="//*[@class='accordion-toggle remove-hyperlink collapsed']")
	WebElement Referee1;
	
	@FindBy(xpath="//*[@id='FirstReferee_TitleCode']")
	WebElement Titlecode_FirstReferee;
	
	@FindBy(xpath="//*[@id='ProfessionalCapacity']")
	WebElement CapacityTab;
	
	@FindBy(xpath="//select[@id='ListTypeCode']")
	WebElement SelectCapacity;	
	
	@FindBy(xpath="//*[@id='InsuranceDetails']")
	WebElement InsuranceTab;
	
	@FindBy(xpath="//*[@id='CertificateNumber']")
	WebElement CertificateNumber;
	
	@FindBy(xpath="//*[@id='Nationality']")
	WebElement NationalityTab;
	
	@FindBy(xpath="//select[@name='CountryOfBirthCode']")
	WebElement SelectNationality;			
	
	@FindBy(xpath="//*[@id='ProposedEmploymentDetails']")
	WebElement ProposedEmploymentTab;	
	
	@FindBy(xpath="//input[@id='HasEmploymentAtPractice']")
	WebElement EmploymentPraticeRadio;
		
	@FindBy(xpath="//*[@id='TraineeDetails']")
	WebElement TraineeTab;
	
	@FindBy(xpath="//input[@id='IsTrainee']")
	WebElement TraineeRadio;	
	
	@FindBy(xpath="//*[@id='ProfessionalQualification']")
	WebElement ProfessionalQualificationTab;
	
	@FindBy(xpath="//*[@id='ProfessionalDetails']")
	WebElement ProfessionalDetailsTab;
	
	@FindBy(xpath="//*[@id='AppraisalHistory']")
	WebElement AppraisalHistoryTab;	
	
	@FindBy(xpath="//*[@id='TrainingDetails']")
	WebElement TrainingDetailsTab;
	
	@FindBy(xpath="//*[@id='CommunicationSkills']")
	WebElement CommunicationSkillsTab;
	
	@FindBy(xpath="//*[@id='AdditionalInformation']")
	WebElement AdditionalInformationTab;
	
	@FindBy(xpath="//*[@id='Declaration']")
	WebElement DeclarationTab;
	
	@FindBy(xpath="//*[@id='PLI_BodyCorporateDeclarations']")
	WebElement CorporateDeclarationsTab;
	
	@FindBy(xpath="//*[@id='PLI_DBS']")
	WebElement PolicecheckTab;
	
	@FindBy(xpath="//*[@id='NetTeamPreference']")
	WebElement NetTeamPreferenceTab;
	
	@FindBy(xpath="//*[@id='OccupationalHealthClearance']")
	WebElement HealthclearanceTab;
	
	@FindBy(xpath="//*[@id='PLI_Undertakings']")
	WebElement UndertakingTab;
	
	@FindBy(xpath="//*[@id='SubmitApplication']")
	WebElement SubmitAppTab;	
	
	@FindBy(xpath="//input[@id='upload1']")
	WebElement ChoosebuttonQualification;	
	
/*	@FindBy(xpath="//input[@type='file']")
	WebElement ChoosebuttonCommunications;*/
	
	@FindBy(xpath="//div[@id='dvquestion0']//div[2]//div[1]//label//input")
	WebElement ChoosebuttonCommunications;
	
	
	
	@FindBy(id="PCSEChecks")
	WebElement PCSECheck;	
	
	@FindBy(id="UploadedDocuments")
	WebElement UploadDocumenttab;		
	
	@FindBy(xpath="//textarea[@id='AdditionalInformation']")
	WebElement AddInfoText;	
	
	@FindBy(id="NETTeamPreferenceCode")
	WebElement NetPreferenceElement;
	
	public ProcessIndex(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}


	public boolean verifyReadonlyMode() {
		boolean FlagEnabled = false;
		try
		{
			ReadOnlyMode: 
			{
			scrolltoElement(driver, PersonalDetailsTab);
			wait.until(ExpectedConditions.elementToBeClickable(PersonalDetailsTab));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(PersonalDetailsTab);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeled = helpers.CommonFunctions.AllElementDisabled(driver,"radio");
	    //	boolean isEnabeled = ElementisEnabledPersonaldetails();
	    	if(isEnabeled)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    	
	    	
	
	    	scrolltoElement(driver, EmploymentHistoryTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(EmploymentHistoryTab));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(EmploymentHistoryTab);
	    	actions1.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledEmploymentHistoryTab = ElementisEnabledEmployeetab();
	    	if(isEnabeledEmploymentHistoryTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    	
	    	
	      	scrolltoElement(driver, RefereesTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(RefereesTab));
			Actions actions2 = new Actions(driver);
	    	actions2.moveToElement(RefereesTab);
	    	actions2.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledRefereesTab = helpers.CommonFunctions.AllElementDisabled(driver,"radio");
	    	if(isEnabeledRefereesTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    	
	    	
	    	
	
	    	scrolltoElement(driver, CapacityTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(CapacityTab));
			Actions actions3 = new Actions(driver);
	    	actions3.moveToElement(CapacityTab);
	    	actions3.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledCapacityTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	      	if(isEnabeledCapacityTab)
	    	{
	      		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	      	
	    	
	
	    	scrolltoElement(driver, InsuranceTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(InsuranceTab));
			Actions actions4 = new Actions(driver);
	    	actions4.moveToElement(InsuranceTab);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledInsuranceTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledInsuranceTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	 
	    	

	    	scrolltoElement(driver, NationalityTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(NationalityTab));
			Actions action5 = new Actions(driver);
	    	action5.moveToElement(NationalityTab);
	    	action5.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledNationalityTab = ElementisEnabledNationalityTab();
	    	if(isEnabeledNationalityTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	   
	    	
	 
	    	scrolltoElement(driver, ProposedEmploymentTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(ProposedEmploymentTab));
			Actions actions6 = new Actions(driver);
	    	actions6.moveToElement(ProposedEmploymentTab);
	    	actions6.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledProEmployementTab =  helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledProEmployementTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	   
	    	scrolltoElement(driver, TraineeTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(TraineeTab));
			Actions actions7 = new Actions(driver);
	    	actions7.moveToElement(TraineeTab);
	    	actions7.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledTraineeTab = ElementisEnabledTraineeTab();
	    	if(isEnabeledTraineeTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    
	    	scrolltoElement(driver, ProfessionalQualificationTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(ProfessionalQualificationTab));
			Actions actions8 = new Actions(driver);
	    	actions8.moveToElement(ProfessionalQualificationTab);
	    	actions8.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledQualificationTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledQualificationTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	   
	    	
	    	
	    	scrolltoElement(driver, ProfessionalDetailsTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(ProfessionalDetailsTab));
			Actions actions9 = new Actions(driver);
	    	actions9.moveToElement(ProfessionalDetailsTab);
	    	actions9.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledProfessionaldetailsTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledQualificationTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    
	    	scrolltoElement(driver, AppraisalHistoryTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(AppraisalHistoryTab));
			Actions actions10 = new Actions(driver);
	    	actions10.moveToElement(AppraisalHistoryTab);
	    	actions10.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledAppraisalHistoryTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledAppraisalHistoryTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	 
	    	
	    	
	    	scrolltoElement(driver, TrainingDetailsTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(TrainingDetailsTab));
			Actions actions11 = new Actions(driver);
	    	actions11.moveToElement(TrainingDetailsTab);
	    	actions11.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledTrainingDetailsTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledTrainingDetailsTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    
	    	scrolltoElement(driver, CommunicationSkillsTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(CommunicationSkillsTab));
			Actions actions12 = new Actions(driver);
	    	actions12.moveToElement(CommunicationSkillsTab);
	    	actions12.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledCommunicationSkillsTab = ElementisEnabledCommunicationTab();
	    	if(isEnabeledCommunicationSkillsTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	   
	    	
	    	
	    	scrolltoElement(driver, AdditionalInformationTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(AdditionalInformationTab));
			Actions actions13 = new Actions(driver);
	    	actions13.moveToElement(AdditionalInformationTab);
	    	actions13.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledAdditionalInformationTab = ElementisEnabledAdditionalInformationTab();
	    	if(isEnabeledAdditionalInformationTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	 
	    	
	    
	    	scrolltoElement(driver, DeclarationTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(DeclarationTab));
			Actions actions14 = new Actions(driver);
	    	actions14.moveToElement(DeclarationTab);
	    	actions14.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledDeclarationTab = helpers.CommonFunctions.AllElementDisabled(driver,"radio");
	    	if(isEnabeledDeclarationTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	  
	    	
	    	scrolltoElement(driver, CorporateDeclarationsTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(CorporateDeclarationsTab));
			Actions actions15 = new Actions(driver);
	    	actions15.moveToElement(CorporateDeclarationsTab);
	    	actions15.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledCorporateDeclarationsTab = helpers.CommonFunctions.AllElementDisabled(driver,"radio");
	    	if(isEnabeledDeclarationTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	   
	    	
	    	
	    	scrolltoElement(driver, PolicecheckTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(PolicecheckTab));
			Actions actions16 = new Actions(driver);
	    	actions16.moveToElement(PolicecheckTab);
	    	actions16.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledPolicecheckTab = helpers.CommonFunctions.AllElementDisabled(driver,"text");
	    	if(isEnabeledPolicecheckTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    	
	    	scrolltoElement(driver, NetTeamPreferenceTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(NetTeamPreferenceTab));
			Actions actions17 = new Actions(driver);
	    	actions17.moveToElement(NetTeamPreferenceTab);
	    	actions17.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledNetTeamPreferenceTab = ElementisEnabledNetTeamTab();
	    	if(isEnabeledNetTeamPreferenceTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	
	    
	    	scrolltoElement(driver, HealthclearanceTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(HealthclearanceTab));
			Actions actions18 = new Actions(driver);
	    	actions18.moveToElement(HealthclearanceTab);
	    	actions18.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledHealthclearanceTab = helpers.CommonFunctions.AllElementDisabled(driver,"checkbox");
	    	if(isEnabeledHealthclearanceTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	  
	    	
	    
	    	scrolltoElement(driver, UndertakingTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(UndertakingTab));
			Actions actions19 = new Actions(driver);
	    	actions19.moveToElement(UndertakingTab);
	    	actions19.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledUndertakingTab = helpers.CommonFunctions.AllElementDisabled(driver,"radio");
	    	if(isEnabeledUndertakingTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    	scrolltoElement(driver, SubmitAppTab);
	    	wait.until(ExpectedConditions.elementToBeClickable(SubmitAppTab));
			Actions actions20 = new Actions(driver);
	    	actions20.moveToElement(SubmitAppTab);
	    	actions20.doubleClick().build().perform();
	    	Thread.sleep(5000);
	    	boolean isEnabeledSubmitAppTab = helpers.CommonFunctions.AllElementDisabled(driver,"checkbox");
	    	if(isEnabeledSubmitAppTab)
	    	{
	    		FlagEnabled = true;
	    		break ReadOnlyMode;
	    	}
	    
	    	
	    	
			}
	    	
		}
		catch(Exception e)
		{
			System.out.println("The Process index tab is not captured");
		}
		return 	FlagEnabled;
	}
	
	 public boolean ElementisEnabledEmployeetab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ChooseButtonEmp);
			//wait.until(ExpectedConditions.elementToBeClickable(ChooseButtonEmp));
			if(ChooseButtonEmp.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Employeetab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledPersonaldetails(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, TitleCodePersonaldetails);
			//wait.until(ExpectedConditions.elementToBeClickable(TitleCodePersonaldetails));
			if(TitleCodePersonaldetails.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Personal tab is not enabled");
		}
		return isEnabeled;
	 }
	
	 public boolean ElementisEnabledRefreeTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Referee1);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(Referee1);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	scrolltoElement(driver, Titlecode_FirstReferee);
			if(Titlecode_FirstReferee.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Referee tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledCapacityTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, SelectCapacity);
			if(SelectCapacity.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Capacity tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledInsuranceTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, CertificateNumber);
			if(CertificateNumber.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Insurance tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledCommunicationTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ChoosebuttonCommunications);
			if(ChoosebuttonCommunications.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Insurance tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledNationalityTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, SelectNationality);
			if(SelectNationality.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Nationality tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledProEmployementTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, EmploymentPraticeRadio);
			if(EmploymentPraticeRadio.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Proposed Employment tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledTraineeTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, TraineeRadio);
			if(TraineeRadio.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Trainee tab is not enabled");
		}
		return isEnabeled;
	 }
	 
	 public boolean ElementisEnabledQualificationTab(){
		 boolean isEnabeled = false;
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, ChoosebuttonQualification);
			if(ChoosebuttonQualification.isEnabled())
			{
				isEnabeled = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Element on Qualification tab is not enabled");
		}
		return isEnabeled;
	 }
	

		public PCSECheck ClickonPCSECheck() {
			try{
				Thread.sleep(2000);
				scrolltoElement(driver, PCSECheck);
				wait.until(ExpectedConditions.elementToBeClickable(PCSECheck));
				Actions actions = new Actions(driver);
		    	actions.moveToElement(PCSECheck);
		    	actions.doubleClick().build().perform();
			//	wait.until(ExpectedConditions.elementToBeClickable(PCSECheck)).click();	
			}
			catch(Exception e)
			{
				System.out.println("The PCSECheck is not clicked");
			}
			return new PCSECheck(driver);
		}
	
		 public boolean ElementisEnabledAdditionalInformationTab(){
			 boolean isEnabeled = false;
			try{
				Thread.sleep(1000);
				scrolltoElement(driver, AddInfoText);
				if(AddInfoText.isEnabled())
				{
					isEnabeled = true;
				}
				
			}
			catch(Exception e)
			{
				System.out.println("The Element on Additional information tab is not enabled");
			}
			return isEnabeled;
		 }
		 
		 public boolean ElementisEnabledNetTeamTab(){
			 boolean isEnabeled = false;
			try{
				Thread.sleep(1000);
				scrolltoElement(driver, NetPreferenceElement);
				if(NetPreferenceElement.isEnabled())
				{
					isEnabeled = true;
				}
				
			}
			catch(Exception e)
			{
				System.out.println("The Element on Net Preference tab is not enabled");
			}
			return isEnabeled;
		 }


		public Uploaddocument clickonuploadeddocument() {
			try{
				wait.until(ExpectedConditions.elementToBeClickable(UploadDocumenttab));
				scrolltoElement(driver, UploadDocumenttab);
				Actions actions = new Actions(driver);
		    	actions.moveToElement(UploadDocumenttab);
		    	actions.doubleClick().build().perform();
		    	helpers.Support.PageLoadExternalwait(driver);
			}
			catch(Exception e)
			{
				System.out.println("The PCSECheck is not clicked");
			}
			return new Uploaddocument(driver);
		}
		
	
}
