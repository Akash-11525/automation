package pageobjects.PL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class Qualification extends Support{

	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(id="upload1")
	WebElement Uploadfilebutton;
	
/*	@FindBy(xpath="//*[@class='btn btn-primary']")
	WebElement Uploadfilebutton;*/
	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(id="ProfessionalQualification")
	WebElement QualificationTab;
	
	@FindBy(id="btnNewRecord")
	WebElement AddNewQualificationButton;
	

	@FindBy(id="QualificationName")
	WebElement Qualification_Name;
	
	@FindBy(id="InstitutionName")
	WebElement Institution_Name;
	
	@FindBy(name="QualificationDate")
	WebElement Qualification_Date;
	
	@FindBy(xpath ="//button[@id='btnSaveQualification']")
	WebElement AddProfessionalQualificationbutton;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="tableQualificationList")
	WebElement qualificationtable;
	
	@FindBy(xpath="//button[@name='btnClearData']")
	WebElement CloseonAddqualificationbutton;

	@FindBy(xpath = "//*[@id='tblUploadedQualificationDocuments']//tbody")
	WebElement Uploadtable;
	
	
	String QualificationName_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "QualificationName",1);
	String INSTITUTION_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "INSTITUTION",1);	
	String DateOfQualification_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "DateOfQualification",1);
	
	
	public Qualification(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 50);
		PageFactory.initElements(this.driver, this);
	}	
	
	public String Uploadfile() throws InterruptedException, IOException {
		String QualificationTableName = null;
		try {
			wait.until(ExpectedConditions.elementToBeClickable(QualificationTab));
			QualificationTableName = QualificationTab.getAttribute("id");
		/*scrolltoElement(driver, Uploadfilebutton);
		wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).clear();
		Thread.sleep(2000);
	String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
		System.out.println(filePath);
		Runtime.getRuntime().exec(filePath);
		String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";		
		helpers.CommonFunctions.Uploadfile(filePath, driver);
		Thread.sleep(5000);*/

			scrolltoElement(driver, Uploadfilebutton);
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
			//jse.executeScript("arguments[0].click();", Uploadfilebutton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton.sendKeys(filePath);

		//	helpers.CommonFunctions.PageLoadExternalwait(driver);
			//	String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";
		scrolltoElement(driver, BrowseButton);
		wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
			}
		
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The file is not loaded on Employment history tab " +e);
		}
		return QualificationTableName;

	}
	public Qualification AddProfessionalQualification(int position) throws InterruptedException {
		try{
			String QualificationName_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "QualificationName",position);
			String INSTITUTION_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "INSTITUTION",position);	
			String DateOfQualification_Qualifi = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Qualification", "DateOfQualification",position);
			Thread.sleep(2000);
			scrolltoElement(driver, AddNewQualificationButton);
		wait.until(ExpectedConditions.elementToBeClickable(AddNewQualificationButton)).click();	
		Thread.sleep(2000);
			scrolltoElement(driver, Qualification_Name);
			wait.until(ExpectedConditions.elementToBeClickable(Qualification_Name));	
			wait.until(ExpectedConditions.elementToBeClickable(Qualification_Name)).sendKeys(QualificationName_Qualifi);
			wait.until(ExpectedConditions.elementToBeClickable(Institution_Name)).sendKeys(INSTITUTION_Qualifi);
			wait.until(ExpectedConditions.elementToBeClickable(Qualification_Date)).sendKeys(DateOfQualification_Qualifi);
			wait.until(ExpectedConditions.elementToBeClickable(Qualification_Date)).sendKeys(Keys.TAB);
			Thread.sleep(1500);
			scrolltoElement(driver, AddProfessionalQualificationbutton);
			wait.until(ExpectedConditions.elementToBeClickable(AddProfessionalQualificationbutton)).click();
			Thread.sleep(5000);
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Add Additional Qualification is not added in Qualification tab" +e);
		}
		return new Qualification(driver);
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			scrolltoElement(driver, Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.Support.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Qualification tab" +e);
		}	
		return new CreateNewApp(driver);
	}

	public int countofexistingqulaification() {
		int Existingqulaification = 0;
		try{
			if(qualificationtable.isDisplayed())
			{
				List<WebElement> rows = qualificationtable.findElements(By.tagName("tr"));
				Existingqulaification = rows.size() - 1;
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found qualification table - Qualification tab" +e);
		}	
		return Existingqulaification;
	}

	public Qualification clickonAddprofessionalqualification() {
		try{
			scrolltoElement(driver, AddNewQualificationButton);
			wait.until(ExpectedConditions.elementToBeClickable(AddNewQualificationButton)).click();	
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found add qualification button - Qualification tab" +e);
		}	
		return new Qualification(driver);
	}

	public boolean verifybutton_disabled() {
		boolean verifybutton_disable = false;
		try {
		scrolltoElement(driver, qualificationtable);
		WebElement buttonEdit_Web = driver.findElement(By.xpath("//table[@id='tableQualificationList']//tr[2]/td[4]/button")); 
		scrolltoElement(driver, buttonEdit_Web);
		List<WebElement> buttonsEdit = driver.findElements(By.xpath("//table[@id='tableQualificationList']//tr[2]/td[4]/button"));
		for (WebElement button : buttonsEdit)
		{   
			System.out.println(button.getText());
			if(button.getText().equalsIgnoreCase("Edit"))
			{
		
				String Classofbuttonedit = button.getAttribute("class");
				System.out.println(Classofbuttonedit);
				if(Classofbuttonedit.contains("disabled"))
				{
			verifybutton_disable = true;	
				}
			}
		}
		
		/*scrolltoElement(driver, qualificationtable);
		WebElement buttondelete = driver.findElement(By.xpath("//table[@id='tableQualificationList']//tr[2]/td[4]/button/button"));
		String Classofbuttondelete = buttondelete.getAttribute("class");
		System.out.println(Classofbuttonedit);
		if(Classofbuttonedit.contains("disabled"))
		{
			verifybutton_disable = true;	
		}*/
		
		List<WebElement> buttonsdelete = driver.findElements(By.xpath("//table[@id='tableQualificationList']//tr[2]/td[4]/button"));
		for (WebElement button : buttonsdelete)
		{   
			if(button.getText().equalsIgnoreCase("Delete"))
			{
		
				String Classofbuttonedit = button.getAttribute("class");
				System.out.println(Classofbuttonedit);
				if(Classofbuttonedit.contains("disabled"))
				{
			verifybutton_disable = true;	
				}
			}
		}
		scrolltoElement(driver, AddNewQualificationButton);
		if(!(AddNewQualificationButton.isEnabled()))
		{
			verifybutton_disable = true;
		}
		}
		
	
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on delete edit and delete button on qualification table - Qualification tab" +e);
		}	
		return verifybutton_disable;
	}

	public Qualification clickonClosebutton() {
		try{
			scrolltoElement(driver, CloseonAddqualificationbutton);
			wait.until(ExpectedConditions.elementToBeClickable(CloseonAddqualificationbutton)).click();	
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found close button - Qualification tab" +e);
		}	
		return new Qualification(driver);
	}

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Uploadfilebutton);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public Boolean verifyCount(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = Uploadtable.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}
				
		return Uploadcount;
	}


}
