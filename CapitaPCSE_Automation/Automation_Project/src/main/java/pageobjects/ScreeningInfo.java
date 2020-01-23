
	package pageobjects;

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

import helpers.WindowHandleSupport;



	public class ScreeningInfo{
		WebDriver driver;

		WebDriverWait wait;
		
		
		@FindBy(xpath="//table[@ologicalname='scr_cervicaltestresult']/tbody/tr[1]/td[6]")
		WebElement firstRecord1;
		
		@FindBy (xpath=("//table[@ologicalname='scr_cervicaltestresult']"))
		WebElement cervialTestResult;
		
		@FindBy(xpath="//div[@id='crmGrid_scr_screening_incident_screeningrecord']")
		WebElement screeningInfoMain;
		
		@FindBy(xpath="//div[@id='crmGrid_scr_screening_incident_screeningrecord_gridBodyContainer']")
		WebElement screeningInfoGridBody;
		
		@FindBy(xpath="fixedrow")
		WebElement gridFixedRow;
		
		@FindBy(xpath="//table[@id='crmGrid_scr_screening_incident_screeningrecord_gridBar']//tbody/tr[1]/th[4]")
		WebElement createdOnHeader;
		
		@FindBy(xpath="//th[@field='createdon']//table[@class='ms-crm-List-Sortable']/tbody/tr[1]/td[1]/a/nobr/label[1]")
		WebElement createdOnTxt;
		
		@FindBy(xpath="//div[@class='ms-crm-grid-databodycontainer']")
		WebElement dataContainer;
		
		@FindBy(xpath="//div[@id='crmGrid_scr_screening_incident_screeningrecord_divDataBody']")
		WebElement dataBody;
		
		@FindBy(xpath="//table[@id='gridBodyTable']//tbody/tr[1]/td[2]/nobr/a")
		WebElement firstRecord;
		
		public ScreeningInfo(WebDriver driver) {
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 30);

			//This initElements method will create all WebElements

			PageFactory.initElements(this.driver, this);
	// TODO Auto-generated constructor stub
		}
		
	
		public LabInfo CarvialTestResult() throws InterruptedException {
			
			Thread.sleep(1000);
			int records = 0;
			driver.switchTo().frame("contentIFrame1");
			try {
			
			List<WebElement> trCount = (List<WebElement>) cervialTestResult.findElements(By.tagName("tr"));
			records = trCount.size();
			System.out.println(records);
			
			System.out.println("Table found.");
			if (records >= 1){
				System.out.println(firstRecord1.getText());
				String linktextname = firstRecord1.getText();
			 wait.until(ExpectedConditions.elementToBeClickable(firstRecord1)).click();
							
			}
			   
			
			}
			catch (NoSuchElementException e){
				System.out.println("No table found Third.");
				
				
			}
			driver.switchTo().defaultContent();
			return new LabInfo(driver);
			

		}


		public ScreeningInfo getActiveCaseTitle() throws InterruptedException {
			Actions sort= new Actions(driver);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Screening:");
			driver.switchTo().frame("contentIFrame1");
			driver.switchTo().frame("area_scr_screening_incident_screeningrecordFrame");
			wait.until(ExpectedConditions.elementToBeClickable(screeningInfoMain));
			wait.until(ExpectedConditions.elementToBeClickable(screeningInfoGridBody));
			//wait.until(ExpectedConditions.elementToBeClickable(gridFixedRow)).click();
			wait.until(ExpectedConditions.elementToBeClickable(createdOnHeader));//.click();
			wait.until(ExpectedConditions.elementToBeClickable(createdOnTxt)).click();
			wait.until(ExpectedConditions.elementToBeClickable(createdOnTxt)).click();
			wait.until(ExpectedConditions.elementToBeClickable(dataContainer));
			wait.until(ExpectedConditions.elementToBeClickable(dataBody));
			wait.until(ExpectedConditions.elementToBeClickable(firstRecord)).click();
			return new ScreeningInfo(driver);
		}


	}
	
