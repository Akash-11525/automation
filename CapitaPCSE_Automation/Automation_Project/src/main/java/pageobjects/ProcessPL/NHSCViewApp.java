package pageobjects.ProcessPL;
import java.io.IOException;
import java.util.ArrayList;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import pageobjects.PL.PerformerHome;
import pageobjects.PL.PerformerList;
import pageobjects.PerformerPL.ActivitiesQueues;
import pageobjects.PerformerPL.PerformerManagement;
import testdata.ConfigurationData;

public class NHSCViewApp extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	

	@FindBy(xpath="//*[@id='divMainContainer']/div[2]/div[2]/p/a/u/i")
	WebElement Localoffice;
	
	@FindBy(xpath="//table[@id='tblAssignedApplication']")
	WebElement AppQueue;
	
	@FindBy(xpath="//form[@action='/PL/ProcessApplication/ReturnApplicationToPCSE']")
	WebElement ReturnAppPopUp;
	
	@FindBy(id="ReasonOfReturn")
	WebElement ReturnAppReason;
	
	@FindBy(xpath="//input[@data-target-id='dvReturnApplication']")
	WebElement OKReturnApp;
	
	@FindBy(xpath="//label[@for='ReasonOfReturn']")
	WebElement ReturnAppMessageOnPopUp;
	
	@FindBy(id="AssignedApplicationQueue")
	WebElement Assignedapp;
	
	@FindBy(id="searchTxt")
	WebElement searchText;
	    
	@FindBy(xpath="//select[@id='Organisation']")
	WebElement OrganisationNameResult;
	    
	@FindBy(css="input[name='Update']")
	WebElement updateButton;
	
	@FindBy(id="NHSE_ChangeRequestQueue")
	WebElement ActivitiesQueueTab;
	
	@FindBy(xpath="//table[@id='tblNHSEQueue']")
	WebElement RefQueue;
	
	@FindBy(xpath="//*[@id='tblAssignedApplication_next']/a")
	WebElement NextButtonOnassignedApp;
	
	@FindBy(id="NHSE_PerformerManagement")
	WebElement PerformerManagementTab;
	
	
	String ReturnAppPopUpExecel = utilities.ExcelUtilities.getKeyValueFromExcel("ProcessPortal.xlsx", "NHSE", "ReturnApplication");
			
	
	public NHSCViewApp(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(80, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public ProcessViewApp clickonApplication(String applicationNumber) {
	try{
		int ResultCount = 0;
		wait.until(ExpectedConditions.elementToBeClickable(AppQueue));
		scrolltoElement(driver, AppQueue);
		List<WebElement> listOfRows = AppQueue.findElements(By.tagName("tr"));
		System.out.println("Rows: "+listOfRows.size());
		for (int i =1 ;i<(listOfRows.size());i++)
		{	
			
			WebElement RefNHS = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]/a"));
			scrolltoElement(driver, RefNHS);
			String RefNo = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]/a")).getText();
			if (applicationNumber.equalsIgnoreCase(RefNo))
			{
				ResultCount = 1;
				scrolltoElement(driver, RefNHS);
				wait.until(ExpectedConditions.elementToBeClickable(RefNHS));
				Actions actions1 = new Actions(driver);
		    	actions1.moveToElement(RefNHS);
		    	actions1.doubleClick().build().perform();
		    	helpers.CommonFunctions.PageLoadExternalwait(driver);
		    	
		    	break;
			}
		}
		while(ResultCount == 0 && NextButtonOnassignedApp.isEnabled())  
		{
			wait.until(ExpectedConditions.elementToBeClickable(NextButtonOnassignedApp)).click();
			List<WebElement> listOfRows1 = AppQueue.findElements(By.tagName("tr"));
			System.out.println("Rows: "+listOfRows1.size());
			for (int i =1 ;i<(listOfRows1.size());i++)
			{	
				
				WebElement RefNHS = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]/a"));
				scrolltoElement(driver, RefNHS);
				String RefNo = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]/a")).getText();
				if (applicationNumber.equalsIgnoreCase(RefNo))
				{
					ResultCount = 1;
					scrolltoElement(driver, RefNHS);
					wait.until(ExpectedConditions.elementToBeClickable(RefNHS));
					Actions actions1 = new Actions(driver);
			    	actions1.moveToElement(RefNHS);
			    	actions1.doubleClick().build().perform();
			    	helpers.CommonFunctions.PageLoadExternalwait(driver);
			    	break;
				}
			}
		
		}
				
	}
	catch(Exception e)
	{
		System.out.println("The Application link on NHSE page is not clicked");
	}
		return new ProcessViewApp(driver) ;
	}


		public NHSCViewApp clickonReturnPCSE(String applicationNumber) {
			try{
				int ResultCount = 0;
				wait.until(ExpectedConditions.elementToBeClickable(AppQueue));
				scrolltoElement(driver, AppQueue);
				List<WebElement> listOfRows = AppQueue.findElements(By.tagName("tr"));
				System.out.println("Rows: "+listOfRows.size());
				for (int i =1 ;i<(listOfRows.size());i++)
				{	
					
					WebElement RefNHS = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]"));
					scrolltoElement(driver, RefNHS);
					String RefNo = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]")).getText();
					if (applicationNumber.equalsIgnoreCase(RefNo))
					{
						ResultCount = 1;
						WebElement RetPCSE = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody//tr["+i+"]//td[8]//div[3]/button"));
						scrolltoElement(driver, RetPCSE);
						Actions actions1 = new Actions(driver);
				    	actions1.moveToElement(RetPCSE);
				    	actions1.doubleClick().build().perform();
				    	wait.until(ExpectedConditions.elementToBeClickable(ReturnAppPopUp));
				    	
					}
				}
				while(ResultCount == 0 && NextButtonOnassignedApp.isEnabled())  
				{
					wait.until(ExpectedConditions.elementToBeClickable(NextButtonOnassignedApp)).click();
					List<WebElement> listOfRows1 = AppQueue.findElements(By.tagName("tr"));
					System.out.println("Rows: "+listOfRows1.size());
					for (int i =1 ;i<(listOfRows.size());i++)
					{	
						
						WebElement RefNHS = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]"));
						scrolltoElement(driver, RefNHS);
						String RefNo = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody/tr["+i+"]/td[1]")).getText();
						if (applicationNumber.equalsIgnoreCase(RefNo))
						{
							ResultCount = 1;
							WebElement RetPCSE = driver.findElement(By.xpath("//table[@id='tblAssignedApplication']//tbody//tr["+i+"]//td[8]//div[3]/button"));
							scrolltoElement(driver, RetPCSE);
							Actions actions1 = new Actions(driver);
					    	actions1.moveToElement(RetPCSE);
					    	actions1.doubleClick().build().perform();
					    	wait.until(ExpectedConditions.elementToBeClickable(ReturnAppPopUp));
					    	
						}
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("The Return to PCSE link is not clicked");
			}
			return new NHSCViewApp(driver);
		}

		public NHSCViewApp fillreturnApp() {
			try
			{
				wait.until(ExpectedConditions.elementToBeClickable(ReturnAppPopUp));
				wait.until(ExpectedConditions.elementToBeClickable(ReturnAppReason)).sendKeys("Automation Purpose");
				Actions actions = new Actions(driver);
		    	actions.moveToElement(OKReturnApp);
		    	actions.doubleClick().build().perform();
		    	helpers.CommonFunctions.PageLoadExternalwait(driver);
		    
		    
			}
			catch(Exception e)
			{
				System.out.println("The Return Application is not filled");
			}
			return new NHSCViewApp(driver);
		}

		public String getReturnAppMessage() {
		String ReturnAppMessage = null;
		try{
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			if(ReturnAppPopUp.isDisplayed())
			{
				ReturnAppMessage = ReturnAppMessageOnPopUp.getText();
				System.out.println(ReturnAppMessage);
			}
		}
		catch(Exception e)
		{
			System.out.println("The Return Application Message is not captured");
		}
			return ReturnAppMessage;
		}

		public boolean VerifyPopUpMessage(String ActualReturnApp) {
			boolean ReturnPopUpMessage = false;
			try{
				System.out.println(ReturnAppPopUpExecel);
				if(ReturnAppPopUpExecel.equalsIgnoreCase(ActualReturnApp))
				{
					ReturnPopUpMessage = true;
				}
			}
			catch(Exception e)
			{
				System.out.println("The Return Pop up message is not matched");
			}
			return ReturnPopUpMessage;
		}

		public void ScreenShotofPopUP(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, ReturnAppMessageOnPopUp);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");

			
		}

		public NHSCViewApp clickonAssignApp() {
			try{
				helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
				scrolltoElement(driver, Assignedapp);
				Actions actions1 = new Actions(driver);
		    	actions1.moveToElement(Assignedapp);
		    	actions1.doubleClick().build().perform();
		    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
				
			}
			catch(Exception e)
			{
				System.out.println("The Assigned App is not clicked");
			}
			return new NHSCViewApp(driver) ;
		}

		public NHSCViewApp opennewtab(WebDriver driver, String performer_PerformerPortal) {
			try{
				JavascriptExecutor js= (JavascriptExecutor)driver;
				js.executeScript("window.open('http://172.16.251.24:9030/','_blank');");
			    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			    driver.switchTo().window(tabs.get(1)); //switches to new tab
			    driver.get("http://172.16.251.24:9030/");
			    PerformerHome ObjPerformerHome = new PerformerHome(driver);
			    ObjPerformerHome = ObjPerformerHome.SelectPerformer(performer_PerformerPortal);
				PerformerList ObjPerformerList =  ObjPerformerHome.ClickOnSubmit();
			    driver.switchTo().window(tabs.get(0)); // switch back to main screen        
			    driver.get("http://172.16.251.24:9030/PerformerManagement/NHSE_navigation");


			}
			catch(Exception e)
			{
				System.out.println("The new tab PCSE is not opened");
			}
			return new NHSCViewApp(driver) ;
		}
	
		
		  public NHSCViewApp selectOrganisation(String environment) throws InterruptedException
			{
				
				String contractor = ConfigurationData.getRefDataDetails(environment, "PLContractor");
				
			//	CommonFunctions.setText(searchText, contractor);
				
				
				Select dropdown = new Select(OrganisationNameResult);
				dropdown.selectByVisibleText(contractor);
				
			//	helpers.CommonFunctions.selectOptionFromList(OrganisationNameResult, contractor );
				
				wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
				
				return new NHSCViewApp(driver);
				
				
			}

		public NHSCViewApp clickonActiviesQueue() {
			try{
				Thread.sleep(3000);
				scrolltoElement(driver, ActivitiesQueueTab);
				Actions actions1 = new Actions(driver);
		    	actions1.moveToElement(ActivitiesQueueTab);
		    	actions1.doubleClick().build().perform();
		    	helpers.Support.PageLoadExternalwait_Performer(driver);
				
			}
			catch(Exception e)
			{
				System.out.println("The Activities queue is not clicked");
			}
			return new NHSCViewApp(driver);
		}

		public ActivitiesQueues clickonRefNo(String refNo) {
			try{
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(RefQueue));
				scrolltoElement(driver, RefQueue);
				List<WebElement> listOfRows = RefQueue.findElements(By.tagName("tr"));
				System.out.println("Rows: "+listOfRows.size());
				for (int i =1 ;i<(listOfRows.size());i++)
				{	
					
					WebElement Refperfommer = driver.findElement(By.xpath("//table[@id='tblNHSEQueue']//tbody/tr["+i+"]/td[1]/a"));
					scrolltoElement(driver, Refperfommer);
					String RefNo = driver.findElement(By.xpath("//table[@id='tblNHSEQueue']//tbody/tr["+i+"]/td[1]/a")).getText();
					if (refNo.equalsIgnoreCase(RefNo))
					{
						scrolltoElement(driver, Refperfommer);
						wait.until(ExpectedConditions.elementToBeClickable(Refperfommer));
						Actions actions1 = new Actions(driver);
				    	actions1.moveToElement(Refperfommer);
				    	actions1.click().build().perform();
				    	helpers.CommonFunctions.PageLoadExternalwait(driver);
				    	break;
					}
				}
						
			}
			catch(Exception e)
			{
				System.out.println("The Ref Link on Activities queue  is not clicked");
			}
			return new ActivitiesQueues(driver);
		}

		
			public PerformerManagement clickonPerformerMangament() {
				try{
					Thread.sleep(2000);
					scrolltoElement(driver, PerformerManagementTab);
					Actions actions1 = new Actions(driver);
			    	actions1.moveToElement(PerformerManagementTab);
			    	actions1.doubleClick().build().perform();
			    	helpers.Support.PageLoadExternalwait_Performer(driver);
				}
				catch(Exception e)
				{
					System.out.println("The Performent Management is not clicked");
				}
				return new PerformerManagement(driver);
			}
	
}
