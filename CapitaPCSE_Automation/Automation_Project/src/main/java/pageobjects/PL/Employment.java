package pageobjects.PL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;
import testdata.ConfigurationData;

public class Employment extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="ProposedEmploymentDetails")
	WebElement EmployementTab;
	
	@FindBy (xpath="//*[@id='DivAddAnotherPracticeButton']/button")
	WebElement AddanotherPractice;
	
	@FindBy (xpath="//*[@id='DivAddAnotherPractice']/button")
	WebElement AddPractice;
	
	@FindBy (id="Practice_Name")
	WebElement Practicename;
	
	@FindBy (id="LevelOfCommitment")
	WebElement Commitment;
	
	@FindBy (xpath="//button[@name='btnAddEditRecord']")
	WebElement SaveOnPratice;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id ="BtnSearchCounAdd")
	WebElement SearchButton;
	
	@FindBy(xpath="//*[@id='PracticeAddressResult']/li")
	WebElement Result_Pratice;
	
	@FindBy(id="HasEmploymentAtPractice")
	WebElement EmploymentPraticeYes;
	
	@FindBy(name="btnAddEditRecord")
	WebElement AddPratice_Dental;
	
	@FindBy(id="btnNewRecord")
	WebElement AddNewProfessionalqualification;
	
	@FindBy(id="tblPracticeDetails")
	WebElement AddPracticeResultTable;
	
	@FindBy(id="SelectedCounty")
	WebElement CountryDropdown;
	
	@FindBy(xpath="//div[@id='DivAddLocalOfficeButton']/button")
	WebElement AddAnotherLocalOfficeButton;
	
	@FindBy(css="select[id='LocalOfficeCode']")
	WebElement SelectLocalOfficeCode;
	
	@FindBy(css="input[id='OfficeLevelOfCommitment']")
	WebElement levelOfCommitmentTxtBox;
	
	@FindBy(css="button[name='btnAddEditOfficeRecord']")
	WebElement AddOfficeButton;
	
	
	
	String PraticeName_Emp = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Employment", "PraticeName",1);
	String GP_Emp = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Employment", "GP",1);
	String Commitment_Emp = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Employment", "Commitment",1);

	
	public Employment(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

	public String selectstatement() throws InterruptedException {
		String ActualTableName = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(EmployementTab));
			ActualTableName = EmployementTab.getAttribute("id");
			Thread.sleep(1000);
			//wait.until(ExpectedConditions.elementToBeClickable(EmploymentPraticeYes)).click();
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0']"));
		//	List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0 ipad-radio-btn-margin']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);





				if (RadioValue.equalsIgnoreCase("Yes"))
				{

					Radiobutton.click();
					Thread.sleep(2000);
					break;

				}
			}

			
		//	helpers.CommonFunctions.ClickOnRadioButton("NO", driver);
		}
		
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}


		return ActualTableName;
	}
	
	
	public String selectstatement_No() throws InterruptedException {
		String ActualTableName = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(EmployementTab));
			ActualTableName = EmployementTab.getAttribute("id");
			//wait.until(ExpectedConditions.elementToBeClickable(EmploymentPraticeYes)).click();
		//	List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0']"));
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0 ipad-radio-btn-margin']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);





				if (RadioValue.equalsIgnoreCase("No"))
				{

					Radiobutton.click();
					Thread.sleep(1000);
					break;

				}
			}

			
		//	helpers.CommonFunctions.ClickOnRadioButton("NO", driver);
		}
		
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}


		return ActualTableName;
	}

	public Employment AddPractice(int Position ) throws InterruptedException {
		try {
			String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
			String MedPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",Position);
			if(AddPractice.isDisplayed())
			{
			scrolltoElement(driver, AddPractice);
			wait.until(ExpectedConditions.elementToBeClickable(AddPractice));
			wait.until(ExpectedConditions.elementToBeClickable(AddPractice)).click();
			}
			else
			{
				scrolltoElement(driver, AddanotherPractice);
				wait.until(ExpectedConditions.elementToBeClickable(AddanotherPractice));
				wait.until(ExpectedConditions.elementToBeClickable(AddanotherPractice)).click();
			}
			scrolltoElement(driver, Practicename);
			wait.until(ExpectedConditions.elementToBeClickable(Practicename));
			wait.until(ExpectedConditions.elementToBeClickable(Practicename)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Practicename)).sendKeys(PraticeName_Emp);
			Thread.sleep(1000);
			scrolltoElement(driver, SearchButton);
			Actions action = new Actions(driver);
			action.moveToElement(SearchButton);
			action.click().build().perform();
			Thread.sleep(3000);
			Actions action1 = new Actions(driver);
			action1.moveToElement(Result_Pratice);
			action1.click().build().perform();	
		
			if(PerformerList_cap.startsWith("Medical")&& MedPerformer_cap.equalsIgnoreCase("GP Contractor"))
			{
			helpers.CommonFunctions.ClickOnRadioButton(GP_Emp, driver);
			}
			if(PerformerList_cap.startsWith("Medical")&& MedPerformer_cap.equalsIgnoreCase("GP Performer"))
			{
			helpers.CommonFunctions.ClickOnRadioButton(GP_Emp, driver);
			}
			scrolltoElement(driver, Commitment);
			wait.until(ExpectedConditions.elementToBeClickable(Commitment)).sendKeys(Commitment_Emp);
		//	wait.until(ExpectedConditions.elementToBeClickable(SaveOnPratice)).click();
			if(PerformerList_cap.startsWith("Medical"))
			{
				Thread.sleep(1500);
			wait.until(ExpectedConditions.elementToBeClickable(SaveOnPratice)).click();
			}
			
			if(PerformerList_cap.startsWith("Dental") ||PerformerList_cap.startsWith("Ophthalmic"))
			{
			Thread.sleep(1500);
			wait.until(ExpectedConditions.elementToBeClickable(AddPratice_Dental)).click();
			}
		}
		
	/*		Thread.sleep(1000);
			scrolltoElement(driver, CountryDropdown);
			Select dropdown = new Select(CountryDropdown);
			dropdown.selectByVisibleText("DORSET");*/
	//	}
			
			catch (NoSuchElementException e)
			{
				System.out.println("The Data is not filled properly on capacity Tab ."+e);
			}

			return new Employment (driver);	
			
		
	}
	
		
		public Employment AddLocaloffice() throws InterruptedException {
			try {
				Thread.sleep(2000);
				scrolltoElement(driver, CountryDropdown);
				Select dropdown = new Select(CountryDropdown);
				dropdown.selectByVisibleText("DORSET");
			}
			catch(Exception e)
			{
				System.out.println();
			}
			return new Employment (driver);	
		}
		
		public void AddAnotherLocaloffice(String expLevelOfCommitment) throws InterruptedException {
			try {
				
				
				scrolltoElement(driver, AddAnotherLocalOfficeButton);
				AddAnotherLocalOfficeButton.click();
				Thread.sleep(1100);
				String localOfficeCode = ConfigurationData.localOffice;
				CommonFunctions.selectOptionFromDropDownByValue(SelectLocalOfficeCode, localOfficeCode);
				wait.until(ExpectedConditions.elementToBeClickable(levelOfCommitmentTxtBox)).clear();
				levelOfCommitmentTxtBox.sendKeys(expLevelOfCommitment);				
				wait.until(ExpectedConditions.elementToBeClickable(AddOfficeButton)).click();
				
				
				
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			//return new Employment (driver);	
		//	tet
		}
		
		public void AddAnotherLocaloffice(String environment ,String expLevelOfCommitment) throws InterruptedException {
			try {
				
				
				scrolltoElement(driver, AddAnotherLocalOfficeButton);
				AddAnotherLocalOfficeButton.click();
				Thread.sleep(1100);
				String localOfficeCode = testdata.ConfigurationData.getRefDataDetails(environment, "PLContractor");
			//	String localOfficeCode = "Lenskart RLT";
				CommonFunctions.selectOptionFromDropDownByValue(SelectLocalOfficeCode, localOfficeCode);
				wait.until(ExpectedConditions.elementToBeClickable(levelOfCommitmentTxtBox)).clear();
				levelOfCommitmentTxtBox.sendKeys(expLevelOfCommitment);
				
				wait.until(ExpectedConditions.elementToBeClickable(AddOfficeButton)).click();
				
				
				
				
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			//return new Employment (driver);	
		//	tet
		}
		
	
	

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver,Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton" +e);
		}	
		return new CreateNewApp(driver);
	}

	public boolean VerifyNonEditablefield() {
		boolean NonEditable = false;
		try{
			
			if(AddPracticeResultTable.isDisplayed())
			{
				for(int i=1;i<4;i+=2)
				{
				
				boolean Editable = driver.findElements(By.xpath("//*[@id='tblPracticeDetails']//tr[1]/td["+i+"]/input")).size() != 0;
				if(!Editable)
				{
					NonEditable = true;
				}
				}
			}
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found result of add pratice" +e);
		}	
		return NonEditable;
	}

	public Boolean verifybuttononResult(String Button1, String Button2) {
		boolean buttonpresent = false;
		try
		{
			if(AddPracticeResultTable.isDisplayed())
			{
						
				String Edit_Button = driver.findElement(By.xpath("//*[@id='tblPracticeDetails']//tr[1]/td[5]/button[1]")).getText();
				if(Edit_Button.equalsIgnoreCase(Button1))
				{
					String delete_Button = driver.findElement(By.xpath("//*[@id='tblPracticeDetails']//tr[1]/td[5]/button[2]")).getText();
					if(delete_Button.equalsIgnoreCase(Button2))
					{
						buttonpresent = true;
					}
				}
				
			}
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found result of add pratice - Edit and delete button" +e);
		}
		return buttonpresent;
	}

}
