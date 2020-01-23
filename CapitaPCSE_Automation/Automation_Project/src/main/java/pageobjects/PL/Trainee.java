package pageobjects.PL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class Trainee  extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="TraineeDetails")
	WebElement TraineeTab;
	
	@FindBy(name="StartDate")
	WebElement  commencemnetdate;
	
	@FindBy(name="EndDate")
	WebElement  Placementdate;
	
	@FindBy(id="TrainerName")
	WebElement  TrainerName;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	
	String CommencementDate_Trainee = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Trainee", "Commencement",1);
	String PlacementDate_Trainee = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Trainee", "Placement",1);	
	String TrainerName_Trainee = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Trainee", "TrainerName",1);

	
	public Trainee(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public String selectTrainee() throws InterruptedException {
		String ActualTableName = null;
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(TraineeTab));
			ActualTableName = TraineeTab.getAttribute("id");
			Thread.sleep(3000);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio ipad-radio-btn-margin']"));
			for (WebElement Radiobutton : Radiobuttons)
			{  
				scrolltoElement(driver, Radiobutton);
				String RadioValue = Radiobutton.getText(); 
				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase("No"))
				{
					if(!Radiobutton.isSelected())
					{
					Radiobutton.click();
					}
				}
			}
		//	helpers.CommonFunctions.ClickOnRadioButton("No", driver);	
/*			wait.until(ExpectedConditions.elementToBeClickable(commencemnetdate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(commencemnetdate)).sendKeys(CommencementDate_Trainee);
			System.out.println(PlacementDate_Trainee);
			wait.until(ExpectedConditions.elementToBeClickable(Placementdate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Placementdate)).sendKeys(PlacementDate_Trainee);
			wait.until(ExpectedConditions.elementToBeClickable(TrainerName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TrainerName)).sendKeys(TrainerName_Trainee);	*/
		}
		
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}

		return  ActualTableName;
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Trainee tab" +e);
		}	
		return new CreateNewApp(driver);
		
	}
	
	public String selectTrainee(String Value) throws InterruptedException {
		String ActualTableName = null;
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(TraineeTab));
			ActualTableName = TraineeTab.getAttribute("id");
			Thread.sleep(3000);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio ipad-radio-btn-margin']"));
			for (WebElement Radiobutton : Radiobuttons)
			{  
				scrolltoElement(driver, Radiobutton);
				String RadioValue = Radiobutton.getText(); 
				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase(Value))
				{
					if(!Radiobutton.isSelected())
					{
					Radiobutton.click();
					}
				}
			}
			
			wait.until(ExpectedConditions.elementToBeClickable(commencemnetdate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(commencemnetdate)).sendKeys(CommencementDate_Trainee);
			System.out.println(PlacementDate_Trainee);
			wait.until(ExpectedConditions.elementToBeClickable(Placementdate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Placementdate)).sendKeys(PlacementDate_Trainee);
			wait.until(ExpectedConditions.elementToBeClickable(TrainerName)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TrainerName)).sendKeys(TrainerName_Trainee);	
		}
		
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}

		return  ActualTableName;
	}
	
	
	
	
	
}
