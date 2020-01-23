package pageobjects.PL;
import java.io.IOException;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class CommunicationSkills extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="CommunicationSkills")
	WebElement CommunicationSkillsTab;
	
	/*@FindBy(xpath="//*[@name='Files']")
	WebElement Uploadfilebutton;*/
	
	@FindBy(xpath="//div[@id='dvquestion0']//div[2]/div//label/input")
	WebElement Uploadfilebutton;
	
	
/*	@FindBy(xpath="//div[@id='dvquestion0']/div[2]/div[1]/label/span")
	WebElement Uploadfilebutton;*/
	

	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(xpath = "//*[@class='table PCSE-table table-condensed']//tbody")
	WebElement Uploadtable;
	
	
	
	public CommunicationSkills(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

	public String EnterCommunicationSkillDetails(String Text) throws InterruptedException, IOException
	{
		String ActualTablename_CommunicationSkills = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(CommunicationSkillsTab));
			ActualTablename_CommunicationSkills = CommunicationSkillsTab.getAttribute("id");
			System.out.println(ActualTablename_CommunicationSkills);
			Thread.sleep(1500);
			 List<WebElement> buttons=driver.findElements(By.xpath("//*[@id='Declarations_0__Answer']"));
	           System.out.println("total radio "+buttons.size());
	          for (WebElement button : buttons)
	          {      
	        	  scrolltoElement(driver, button);
	        	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
	        	   String ButtonValue = button.getAttribute("value"); 
	        	 
	        	   Thread.sleep(1000);
	        	  if (ButtonValue.equalsIgnoreCase(Text))
	        	  {
	        		
	        		 button.click();
	        		  break;
	        		  
	        	  }
	          }
	     scrolltoElement(driver, Uploadfilebutton);
		
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).clear();
		
	/*		String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePath);
			Runtime.getRuntime().exec(filePath);*/
	     
		/*	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton));
			wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).click();
			Thread.sleep(1000);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			helpers.CommonFunctions.Uploadfile(filePath, driver);*/
	 	List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
		for(String Extension : Extensions)
		{
	     	scrolltoElement(driver, Uploadfilebutton);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
			//jse.executeScript("arguments[0].click();", Uploadfilebutton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton.sendKeys(filePath);


			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
			Thread.sleep(7000);
			
		}
		
		}
	
	
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Communication skills tab" +e);
		}	
		return ActualTablename_CommunicationSkills;		
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
			System.out.println("The element is not found Savebutton - Communication Tab" +e);
		}	
		return new CreateNewApp(driver);
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

	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Uploadfilebutton);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	}
	
	


