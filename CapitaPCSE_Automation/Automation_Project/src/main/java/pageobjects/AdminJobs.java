package pageobjects;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;

import helpers.WindowHandleSupport;

public class AdminJobs {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//*[@id='Tabscr_job-main']/a/span/span")
	WebElement Adminbutton;
	
	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(id="crmQuickFindTD")
	WebElement srcRecords;
	
	//@FindBy(css="img[id*=crmGrid_findCriteriaImg]")

	@FindBy(xpath= "//*[@id='crmGrid_findCriteriaImg']")
	WebElement srcCriIcon;
	
	//@FindBy(css="iframe[id*='contentIFrame']")
	@FindBy(xpath="//iframe[contains(@id, 'contentIFrame']")
	WebElement frame;
	
	@FindBy(css="table[id='gridBodyTable']")
	WebElement searchResulttbl;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody/tr/td[2]")
	WebElement beforefirstSearchResultItem;
	
	
	//@FindBy(xpath="//*[@id='gridBodyTable_primaryField_{F4B05451-EB48-E611-80D9-0050569B6A23}_0']")
	@FindBy(xpath="//table[@id='gridBodyTable']//td[2]")
	WebElement firstSearchResultItem;
	
	@FindBy(xpath="//li[contains(@id,'scr_job.LaunchJob.Button')]")
	WebElement launchJobButton;
	
	@FindBy(xpath = "//div[@id='crmRibbonManager']/div/ul/li[1]")
	WebElement launchJobBtn;
	
	@FindBy(xpath="//span[contains(@command,'LaunchWorkflow.Command')]")
	WebElement launchJob;
	
	@FindBy(xpath = "//*[contains(@id, 'commandContainer')]")
	//@FindBy(id*="commandContainer")
	WebElement commandContainer3;
	
	@FindBy(xpath="//iframe[contains(@id,'contentIFrame']")
	WebElement iframe;
	
	/*@FindBy(xpath = "//*[contains(@id, 'contentIFrame')]")
	List<WebElement> iframes;
	*/
	
	@FindBy(tagName="iframe")
	List<WebElement> iframes;
	
	@FindBy(id="crmGrid_clearCriteriaImg")
	WebElement clearImg;
	
	


	public AdminJobs(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		
		}
	
	public String switchframe(WebElement ele){
		int iframeCount = 0;
		String iframeName = null;
		
			//	
		iframeCount = iframes.size();
		System.out.println("Count of frames: "+iframeCount);
		//iframeCount = iframeCount-1;
		try{
			//List<WebElement> frames = driver.findElements(By.tagName("iframe"));
			for(WebElement iframe:iframes)
			{
				
					driver.switchTo().frame(iframe);
					try
					{
					if (ele.isDisplayed())
					{
						iframeName = iframe.getText();
						System.out.println(iframeName);
						break;
					}
					}
					
					catch (Exception NoSuchElementException)
					{
						System.out.println("The element" +ele+ "does not found under iframe: contentIFrame"+iframe.getText()+"");
					}
					
				}
			
			}
				
				catch (Exception NoSuchElementException)
				{
					System.out.println("The element" +ele+ "does not found under iframe: contentIFrame"+iframeCount+"");
					
				}
			
			//WebElement iframeEle = driver.findElement(By.id("contentIFrame"+iframeCount+""));
			//driver.switchTo().frame(iframeEle);
			return iframeName;
			
		}
		
		//WebElement iframeEle = driver.findElement(By.id("contentIFrame"+iframeCount+""));
		//driver.switchTo().frame(iframeEle);
		
	
	
	public AdminJobs searchForRecords (String txt)
	{
		try {
		//driver.switchTo().frame(frame);
		//driver.switchTo().frame("contentIFrame0");
		WindowHandleSupport.getRequiredWindowDriver(driver, "Admin Jobs Active Jobs ");
		driver.switchTo().frame("contentIFrame0");
			//driver.switchTo().frame(frame);
		//switchframe(srcCriTxt);
		srcRecords.click();
		//srcCriTxt.click();
		srcCriTxt.clear();
		srcCriTxt.sendKeys(txt);
		
		srcCriTxt.sendKeys(Keys.ENTER);
		//wait.until(ExpectedConditions.elementToBeClickable(searchResulttbl));
		//driver.switchTo().defaultContent();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new AdminJobs(driver);	
	}
	
	public AdminJobs clearSearch ()
	{
		try {
		switchframe(clearImg);
		clearImg.click();
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new AdminJobs(driver);	
	}
	
	
	
	public GenNewPatientsScreeningRecords clickOnFirstSearchRecord()
	{
		try {
			WindowHandleSupport.getRequiredWindowDriver(driver, "Admin Job:");
			//driver.switchTo().frame("contentIFrame0");
			//aswitchframe(searchResulttbl);
			System.out.println("Switched to IFrame Successfully.");
			if(searchResulttbl.isDisplayed())
			{
				
		/*	Actions action = new Actions(driver);
				action.moveToElement(beforefirstSearchResultItem).build().perform();
				String linktextvalue = firstSearchResultItem.getText();
				System.out.println("Linked Text: "+linktextvalue);
				
				System.out.println(firstSearchResultItem.getText());*/
				System.out.println(firstSearchResultItem.getText());
				wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem));
				
				firstSearchResultItem.click();
				
				
			//wait.until(ExpectedConditions.visibilityOf(firstSearchResultItem)).click();;
				
			
			//	 driver.findElement(By.linkText("Generate New Patient Screening Records")).click();
				//wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				//driver.findElement(By.linkText(linktextvalue)).click();
				
				//wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				//firstSearchResultItem.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(launchJobBtn));
				
			}
			else{
				System.out.println("No Search results are displayed.");
			}
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("Table is not displayed.");
		}

		return new GenNewPatientsScreeningRecords(driver);
	}
	
	public GenerateRecalls clickOnFirstRecord()
	{
		try {
			if(searchResulttbl.isDisplayed())
			{
				System.out.println(firstSearchResultItem.getText());
				wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem));
				Actions action = new Actions(driver);
				action.doubleClick(firstSearchResultItem).perform();
				
			//	firstSearchResultItem.click();
				
				
			//	wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(launchJobButton));
				
			}
			else{
				System.out.println("No Search results are displayed.");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return new GenerateRecalls(driver);
	}
	
	public GenNewPatientsScreeningRecords launchJob() throws InterruptedException{
		
		//commandContainer3.click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJobButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(launchJob)).click();
		Thread.sleep(2000);
		return new GenNewPatientsScreeningRecords(driver);
	}
	
	public AdminJobs Adminbutton()
	{
		wait.until(ExpectedConditions.visibilityOf(Adminbutton));
		wait.until(ExpectedConditions.elementToBeClickable(Adminbutton)).click();
		driver.switchTo().defaultContent();
	
		return new AdminJobs(driver);
	}
	
	public GenerateRecalls clickOnFirstSearchRecordofGenerateRecalls()
	{
		try {
			
			//driver.switchTo().frame("contentIFrame0");
			switchframe(searchResulttbl);
			System.out.println("Switched to IFrame Successfully.");
			if(searchResulttbl.isDisplayed())
			{
				
		/*	Actions action = new Actions(driver);
				action.moveToElement(beforefirstSearchResultItem).build().perform();
				String linktextvalue = firstSearchResultItem.getText();
				System.out.println("Linked Text: "+linktextvalue);
				
				System.out.println(firstSearchResultItem.getText());*/
				System.out.println(firstSearchResultItem.getText());
				wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem));
				
			//	firstSearchResultItem.click();
				
				Actions action = new Actions(driver);
				action.doubleClick(firstSearchResultItem).perform();
				
				
				
			//wait.until(ExpectedConditions.visibilityOf(firstSearchResultItem)).click();;
				
			
			//	 driver.findElement(By.linkText("Generate New Patient Screening Records")).click();
				//wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				//driver.findElement(By.linkText(linktextvalue)).click();
				
				//wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultItem)).click();
				//firstSearchResultItem.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(launchJobBtn));
				
			}
			else{
				System.out.println("No Search results are displayed.");
			}
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("Table is not displayed.");
		}

		return new GenerateRecalls(driver);
	}
	
	public String switchToRequiredIframe(WebElement ele){
		int iframeCount = 0;
		String iframeName = null;
	//	int count = 0;
		
		iframeCount = iframes.size();
		System.out.println("Count of frames: "+iframeCount);
		//iframeCount = iframeCount-1;
		try{
			//List<WebElement> frames = driver.findElements(By.tagName("iframe"));
			for(WebElement iframe:iframes)
			{					
				driver.switchTo().frame(iframe);
				try
				{
				//if (ele.isDisplayed())
					if(ele.isEnabled())
					{
						iframeName = iframe.getAttribute("name");
						System.out.println(iframeName);
						break;
					}
				}					
				catch(Exception NoSuchElementException)
				{
					System.out.println("The element" +ele+ "does not found under iframe: contentIFrame"+iframe.getText()+"");
				}					
			}			
		}			
		catch (Exception NoSuchElementException)
		{
			System.out.println("The element" +ele+ "does not found under iframe: contentIFrame"+iframeCount+"");
					
		}		
			//WebElement iframeEle = driver.findElement(By.id("contentIFrame"+iframeCount+""));
			//driver.switchTo().frame(iframeEle);
		return iframeName;			
	}
}
