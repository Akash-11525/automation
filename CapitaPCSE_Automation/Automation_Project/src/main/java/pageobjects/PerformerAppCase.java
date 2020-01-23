package pageobjects;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import helpers.WindowHandleSupport;
import pageobjects.ProcessPL.*;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
public class PerformerAppCase extends Support
{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//table[@id='tbldivTable']")
	WebElement PerformerapplicationTable;
	
/*	@FindBy(xpath="//button[@onclick='getAccountServices()']")
	WebElement GetDetailsButton;*/
	
	@FindBy(xpath="//button[@onclick='getPerformerApplicationCaseList()']")
	WebElement GetDetailsButton;
	
	
	@FindBy(xpath="//button[@onclick='getPerformerApplicationCaseAndActivityList()']")
	WebElement GetActivityDetailsButton;
	
	@FindBy(xpath="//button[@onclick='getActivityList()']")
	WebElement GetActivityLogButton;
	
	@FindBy(xpath="//*[@id='dtsub']/img[2]")
	WebElement DownArrow;	
	
	@FindBy(xpath="//*[@id='dtcreated']/img[2]")
	WebElement DownArrowDtCreated;	
	
	@FindBy(xpath="//*[@id='dtsub']/img[1]")
	WebElement UpArrow;
	
	@FindBy(xpath="//table[@id='tbldivTable']//tr")
	List<WebElement> rowElements;
	
	@FindBy(xpath="//table[@id='tbldivTable']/tbody/tr")
	List<WebElement> tableRows;
	
	@FindBy(xpath="//table[@id='tbldivTable']/tbody")
	WebElement tableBody;
	
	@FindBy(xpath="//table[@id='tbldivTable']/tbody")
	WebElement taskTableBody;	
	
	@FindBy(xpath="//*[@id='pcs_portalrole']")
	WebElement PortalRoleDiv;	

	@FindBy(xpath="//*[@id='pcs_portalrole']/div[1]/span[1]")
	WebElement PortalRoleText;
	
	@FindBy(xpath="//div[@id='pcs_organisation']")
	WebElement PortalorgDiv;	

	@FindBy(xpath="//*[@id='pcs_organisation_ledit']")
	WebElement PortalorgText;
	
	@FindBy(xpath="//*[@id='savefooter_statuscontrol']")
	WebElement SaveButton;
	
	@FindBy(xpath="//img[@id='pcs_portalrole_i']")
	WebElement Search;
	
	@FindBy(xpath="//a[@title='Look Up More Records']")
	WebElement LookUpMoreRecords;
	
	@FindBy(xpath="//input[@id='crmGrid_findCriteria']")
	WebElement SearchTextBox;

	@FindBy(xpath="//button[@id='butBegin']")
	WebElement AddButton;
	
	@FindBy(xpath="//*[@id='ReAssignCase']/tbody/tr[2]/td[1]/button")
	WebElement AssigntoCaseofficer;
	
	@FindBy(xpath="//select[@id='caseOfficer']")
	WebElement CaseoffierName;
	
	
	
	String ApplicationNum;
	
	String OfficerName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "PCSECheck", "OfficerName",1);

	public PerformerAppCase(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public ProcessViewApp clickOnStatus(String Status , String ApplicationNumber) {
		try{
			Thread.sleep(2000); 
			scrolltoElement(driver, DownArrow);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrow);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	 Actions actions6 = new Actions(driver);
		    	actions6.moveToElement(DownArrow);
		    	actions6.doubleClick().build().perform();
		    	Thread.sleep(2000);
		    	
		    	 
		      
					
			int rowCount=driver.findElements(By.xpath("//table[@id='tbldivTable']/tbody/tr")).size();
			for(int i=2;i<rowCount+1;i++)
			{
		     WebElement CaseIdentifier = driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[1]"));	
		     WebElement StatusField = driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[3]"));
		     String  AppStatus = driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[3]")).getText();
		     System.out.println(AppStatus);
		     if(AppStatus != null && !AppStatus.isEmpty())
		     {
		     WebElement ApplicationNumber1 = driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[2]/a"));
		     String ApplicationonCRM = ApplicationNumber1.getText().trim();
		     System.out.println(ApplicationonCRM);
		     Thread.sleep(2000);
//		     && StatusField.getText().equalsIgnoreCase("Submitted")
		     if(CaseIdentifier.getText().contains(Status) && ApplicationonCRM.equalsIgnoreCase(ApplicationNumber))
		     {
		    	  
		    	// WebElement ApplicationNumber = driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[3]/a"));
		    	 scrolltoElement(driver, ApplicationNumber1);
		    	 Thread.sleep(3000);
		    	 System.out.println(ApplicationNumber1.getText());
		    	 //  ApplicationNumber1.click();
		    	  Actions actions = new Actions(driver);
			    	actions.moveToElement(ApplicationNumber1);
			    	actions.click().build().perform();
			    	Thread.sleep(2000);
		   			WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
		   			driver.manage().window().maximize();
		    	   break;
		    	   
		     }			
			}
			}	
			}
		catch(Exception e)
		{
			System.out.println("The Application Number is not found "+Status);
		}
		return new ProcessViewApp (driver);
	}

	public PerformerAppCase clickonButton() {
		try{
			Thread.sleep(1000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Performer Application Case");
			driver.manage().window().maximize();
		//	switchToFrame(PerformerapplicationTable, driver);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_Performerapplicationcasehtml");
		//	Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(GetDetailsButton)).click();
			 /* Actions actions = new Actions(driver);
		    	actions.moveToElement(GetDetailsButton);
		    	actions.doubleClick().build().perform();*/
			
		//	helpers.CommonFunctions.ClickOnButton("Get case details",driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The button is not clicked for get the result on performer app case page");
		}
		
		// TODO Auto-generated method stub
		return new PerformerAppCase(driver);
	}
	
	public ProcessViewApp clickOnCommenceCheck(String Status , String ApplicationNumber) {
		try{
			Thread.sleep(2000); 
		/*	Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrow);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	 Actions actions6 = new Actions(driver);
		    	actions6.moveToElement(DownArrow);
		    	actions6.doubleClick().build().perform();
		    	Thread.sleep(2000);*/
					
			
			  for (WebElement row : rowElements) {
				  ApplicationNum = row.findElement(By.xpath("//td[2]")).getText();
				  if (ApplicationNum.equalsIgnoreCase(ApplicationNumber))
				  {
					  System.out.println("The Application Number is found");
					  row.findElement(By.xpath("//td[7]/Button[@onClick.contains(text(),'CommenceCheck']")).click();
				  }
				  
				  
			  }
			
			
		}
		catch(Exception e)
		{
			System.out.println("The Application Number is not found "+Status);
		}
		return new ProcessViewApp (driver);
	}
	
	public LoginScreen clickOnCaseCommenceCheck(String ApplicationNumber, String Status) {
		try{
			
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrow);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  /*  int rowCount=tableBody.findElements(By.xpath("//tr")).size();
		    
	    	//for (WebElement tableRow: tableRows){
		    
				
				for (int i=2; i<rowCount+1; i++){
				String row1 =Integer.toString(i);
	    		System.out.println(tableBody.findElement(By.xpath("//tr["+i+"]/td[1]")).getText());
	    		String appNum = tableBody.findElement(By.xpath("//tr["+i+"]/td[2]")).getText();
				System.out.println(appNum);
	    		if(appNum.equalsIgnoreCase(ApplicationNumber))
	    		{
	    			//driver.findElement(By.xpath("//table[@id='tbldivTable']/tbody/tr["+i+"]/td[7]/Button[@onClick.contains(text(),'CommenceCheck']")).click();
	    			tableBody.findElement(By.xpath("//tr["+i+"]/td[7]/Button[2]")).click();
	    			break;
	    		}
	    		Thread.sleep(3000);
	    	}
	    	
			WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");*/
	    	
	    	  List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
			    outerloop:
				for(WebElement row:rows)
				{
					List<WebElement> rowItems = row.findElements(By.tagName("td"));
					while (rowItems.size() > 0)
					{
					String appNum = row.findElements(By.tagName("td")).get(1).getText();
					System.out.println(appNum);
					
					if(appNum.equalsIgnoreCase(ApplicationNumber))
					{
						String appStatus =row.findElements(By.tagName("td")).get(2).getText();
						System.out.println(appStatus);
						
						if (appStatus.equalsIgnoreCase(Status))
						{					
						//row.findElements(By.tagName("td")).get(0).click();
							//row.findElement(By.xpath("//td[7]/Button[@onClick.contains(text(),'CommenceCheck']")).click();
							row.findElements(By.tagName("td")).get(7).findElements(By.tagName("Button")).get(1).click();
						break outerloop;
						}
						else
						{
							break;
						}
						
					}
					else
					{
						break;
					}
					}
				
					//rowsData.add(rowData);
				}

			    
				driver.switchTo().defaultContent();
		    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSS");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
  		return new LoginScreen (driver);
	}
	
	public String clickOnCaseCommenceCheck_PortalUser(String ApplicationNumber, String Status) {
		String PortalURL = null;
		try{
			
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrow);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
   	
	    	  List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
			    outerloop:
				for(WebElement row:rows)
				{
					List<WebElement> rowItems = row.findElements(By.tagName("td"));
					while (rowItems.size() > 0)
					{
					String appNum = row.findElements(By.tagName("td")).get(1).getText();
					System.out.println(appNum);
					
					if(appNum.equalsIgnoreCase(ApplicationNumber))
					{
						String appStatus =row.findElements(By.tagName("td")).get(2).getText();
						System.out.println(appStatus);
						
						if (appStatus.equalsIgnoreCase(Status))
						{	
							Thread.sleep(3000);
						//row.findElements(By.tagName("td")).get(0).click();
							//row.findElement(By.xpath("//td[7]/Button[@onClick.contains(text(),'CommenceCheck']")).click();
						//	row.findElements(By.tagName("td")).get(7).findElements(By.tagName("Button")).get(1).click();
							PortalURL = row.findElements(By.tagName("td")).get(7).findElements(By.tagName("Button")).get(1).getAttribute("onclick");
							row.findElements(By.tagName("td")).get(7).findElements(By.tagName("Button")).get(1).click();
						break outerloop;
						}
						else
						{
							break;
						}
						
					}
					else
					{
						break;
					}
					}
				
					
				}

			    
				driver.switchTo().defaultContent();
			
		    //	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSS");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return PortalURL;
	}
	
	public PerformerAppCase clickonGetActivityDetails() {
		try{
			Thread.sleep(1000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Performer Application Case");
			driver.manage().window().maximize();
			driver.switchTo().defaultContent();
		//	switchToFrame(PerformerapplicationTable, driver);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_CaseOfficerActivityQueue");
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(GetActivityDetailsButton)).click();
			 /* Actions actions = new Actions(driver);
		    	actions.moveToElement(GetDetailsButton);
		    	actions.doubleClick().build().perform();*/
			
		//	helpers.CommonFunctions.ClickOnButton("Get case details",driver);
		//	driver.switchTo().defaultContent();
		//	driver.switchTo().defaultContent();
			
		}
		catch(Exception e)
		{
			System.out.println("The button is not clicked for get the result on performer app case page");
		}
		
		// TODO Auto-generated method stub
		return new PerformerAppCase(driver);
	}
	
	public PerformerAppCase clickonDQTGetActivityDetails() {
		try{
			Thread.sleep(2000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Performer Application Case");
			driver.manage().window().maximize();
		//	switchToFrame(PerformerapplicationTable, driver);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_DataQualityTeamActivityQueue");
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(GetActivityLogButton)).click();
			
			 /* Actions actions = new Actions(driver);
		    	actions.moveToElement(GetDetailsButton);
		    	actions.doubleClick().build().perform();*/
			
		//	helpers.CommonFunctions.ClickOnButton("Get case details",driver);
		//	driver.switchTo().defaultContent();
		//	driver.switchTo().defaultContent();
			
		}
		catch(Exception e)
		{
			System.out.println("The button is not clicked for get the result on performer app case page");
		}
		
		// TODO Auto-generated method stub
		return new PerformerAppCase(driver);
	}
	
	public LoginScreen clickOnTask(String ApplicationNumber, String task) {
		try{
			//Thread.sleep(2000); 
			/*driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_CaseOfficerActivityQueue");*/
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(2).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{					
					row.findElements(By.tagName("td")).get(0).click();
					break outerloop;
					}
					
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
				}
			
				//rowsData.add(rowData);
			}

		    
			driver.switchTo().defaultContent();
	    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSS");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return new LoginScreen (driver);
	}
	
	public String clickOnTask_PLPortal(String ApplicationNumber, String task, String Env) {
		String PortalUserLink = null;
		try{
			
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(2).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{
					WebElement Pl  = row.findElements(By.tagName("td")).get(0);
					PortalUserLink = Pl.findElement(By.tagName("a")).getAttribute("href");
					
					row.findElements(By.tagName("td")).get(0).click();
					break outerloop;
					}
					
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
				}
	
			}

		    
			driver.switchTo().defaultContent();
			 ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", PortalUserLink, "PLPORTALINT", Env);
	    //	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSS");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return PortalUserLink;
	}
	
	public LoginScreen clickOnDQTTask(String ApplicationNumber, String task) {
		try{
			//Thread.sleep(2000); 
		/*	driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_DataQualityTeamActivityQueue");*/
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(3).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{					
					row.findElements(By.tagName("td")).get(0).click();
					break outerloop;
					}
					
				}
				else
				{
					break;
				}
				}
			
				//rowsData.add(rowData);
			}

		    
			driver.switchTo().defaultContent();
	    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return new LoginScreen (driver);
	}
	
	public String clickOnDQTTask_PLINTUSER(String ApplicationNumber, String task, String Env) {
		String PortalUserLink = null;
		try{
	
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   	    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(3).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{
						WebElement Pl =  row.findElements(By.tagName("td")).get(0);
						PortalUserLink = Pl.findElement(By.tagName("a")).getAttribute("href");
					row.findElements(By.tagName("td")).get(0).click();
					break outerloop;
					}
					
				}
				else
				{
					break;
				}
				}
				 ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", PortalUserLink, "PLPORTALINT", Env);
				//rowsData.add(rowData);
			}

		    
			driver.switchTo().defaultContent();
	    
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return PortalUserLink;
	}
	
	public LoginScreen clickOnActivityTask(String ApplicationNumber, String task) {
		try{
			//Thread.sleep(2000); 
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_NetTeamActivityQueue");
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(3).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{					
					row.findElements(By.tagName("td")).get(0).click();
					break outerloop;
					}
					
				}
				else
				{
					break;
				}
				}
			
				//rowsData.add(rowData);
			}

		    
			driver.switchTo().defaultContent();
	    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return new LoginScreen (driver);
	}
	
	
	public String  clickOnActivityTask_PLPortal(String ApplicationNumber, String task, String Env) {
		String PortalUserLink = null;
		try{
			//Thread.sleep(2000); 
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_NetTeamActivityQueue");
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String appNum = row.findElements(By.tagName("td")).get(3).getText();
				System.out.println(appNum);
				
				if(appNum.equalsIgnoreCase(ApplicationNumber))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{	
						WebElement Pl = row.findElements(By.tagName("td")).get(0);
						PortalUserLink = Pl.findElement(By.tagName("a")).getAttribute("href");
					row.findElements(By.tagName("td")).get(0).click();
					
					break outerloop;
					}
					
				}
				else
				{
					break;
				}
				}
			
				//rowsData.add(rowData);
			}
		    ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", PortalUserLink, "PLPORTALINT", Env);
		    
			driver.switchTo().defaultContent();
	    	
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+ApplicationNumber);
		}
		return PortalUserLink;
	}
	
	public PerformerAppCase clickonGetActivityLog() {
		try{
			Thread.sleep(1000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Performer Application Case");
			driver.manage().window().maximize();
		//	switchToFrame(PerformerapplicationTable, driver);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_NetTeamActivityQueue");
		//	Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(GetActivityLogButton)).click();
			 /* Actions actions = new Actions(driver);
		    	actions.moveToElement(GetDetailsButton);
		    	actions.doubleClick().build().perform();*/
			
		//	helpers.CommonFunctions.ClickOnButton("Get case details",driver);
			driver.switchTo().defaultContent();
		//	driver.switchTo().defaultContent();
			
		}
		catch(Exception e)
		{
			System.out.println("The button is not clicked for get the result on performer app case page");
		}
		
		// TODO Auto-generated method stub
		return new PerformerAppCase(driver);
	}

	public boolean VerifyApplicationNumber(String Firstname, String status)
	{
		boolean ApplicationonCRM = false;
		try{
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(2000);	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrow);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(2000);   	
	    	  List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
			    outerloop:
				for(WebElement row:rows)
				{
					List<WebElement> rowItems = row.findElements(By.tagName("td"));
					while (rowItems.size() > 0)
					{
						String CaseIdentifier = row.findElements(By.tagName("td")).get(0).getText();
						System.out.println(CaseIdentifier);				
						if(CaseIdentifier.startsWith(Firstname))
							{
								String appStatus =row.findElements(By.tagName("td")).get(2).getText();
								System.out.println(appStatus);						
								if (appStatus.equalsIgnoreCase(status))
								{					
									String AppNumber = row.findElements(By.tagName("td")).get(1).getText();
									utilities.ExcelUtilities.setKeyValueinExcelWithPosition("ProcessPortal.xlsx", "ProcessHelper", "ApplicationNumber",AppNumber, 1);
									ApplicationonCRM = true;
									break outerloop;
								}
								else
								{
									break;
								}
							}
					
						else
						{
							break;
						}
					
					}
					
				}
				
			}
				
				
	    	  catch(Exception e)
	    	  {
	    		  System.out.println("The Application number is not captured" + e);
	    	  }		
		return ApplicationonCRM;
		
	}

	public PerformerAppCase AddPortalRole() {
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Portal User Role:");
			switchToFrame(PortalRoleDiv, driver);
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(PortalRoleDiv)).click();
			Actions actions = new Actions(driver);
			actions.moveToElement(Search);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	Actions actions1 = new Actions(driver);
			actions1.moveToElement(LookUpMoreRecords);
	    	actions1.click().build().perform();
	    	Thread.sleep(1000);
	    	driver.switchTo().defaultContent();
	    	driver.switchTo().frame("InlineDialog_Iframe");
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys("PL Performer");
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
	    	Actions actions2 = new Actions(driver);
			actions2.moveToElement(AddButton);
	    	actions2.doubleClick().build().perform();
	    	driver.switchTo().defaultContent();
	    	switchToFrame(PortalorgDiv, driver);
	    	
		//	wait.until(ExpectedConditions.elementToBeClickable(PortalRoleText)).sendKeys("PL Performer");
			wait.until(ExpectedConditions.elementToBeClickable(PortalorgDiv)).click();	
			String localOfficeCode_Text = ConfigurationData.localOffice;
			System.out.println(localOfficeCode_Text);
			wait.until(ExpectedConditions.elementToBeClickable(PortalorgText)).sendKeys(localOfficeCode_Text);
			wait.until(ExpectedConditions.elementToBeClickable(PortalorgText)).sendKeys(Keys.TAB);
			driver.switchTo().defaultContent();
			Thread.sleep(3000);
			switchToFrame(SaveButton, driver);
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			
			
		}
		catch(Exception e)
		{
			System.out.println("The performer role is not added sucessfully ");
		}
		return new PerformerAppCase(driver);
	}

	public PerformerAppCase ClickonAssignCase(String GMCnumber) {
		try{
			/*driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_CaseOfficerActivityQueue");*/
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrow);
			actions4.doubleClick().build().perform();
			Thread.sleep(2000);
    	
			Actions actions5 = new Actions(driver);
			actions5.moveToElement(DownArrow);
			actions5.doubleClick().build().perform();
			Thread.sleep(1000);
			
			List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
			outerloop:
				for(WebElement row:rows)
				{
					List<WebElement> rowItems = row.findElements(By.tagName("td"));
					while (rowItems.size() > 0)
					{
							String Caseidentifier = row.findElements(By.tagName("td")).get(0).getText();
							System.out.println(Caseidentifier);			
							if(Caseidentifier.endsWith(GMCnumber))
							{
								row.findElements(By.tagName("td")).get(7).findElements(By.tagName("Button")).get(0).click();
								break outerloop;					
							}
							else
							{
								break;
							}
					}
				}

	
			}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Application Number is not found "+GMCnumber);
		}
		return new PerformerAppCase (driver);
	}

	public PerformerAppCase ClickonassignCaseofficer() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, CaseoffierName);
			Select dropdown = new Select(CaseoffierName);
	    	dropdown.selectByVisibleText(OfficerName);			
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(AssigntoCaseofficer);
			actions4.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Assign to case officer is not clicked");
		}
		return new PerformerAppCase(driver);
	}
	
	public LoginScreen clickOnTask_performer(String date, String task) {
		try{
			//Thread.sleep(2000); 
			/*driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_CaseOfficerActivityQueue");*/
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String Datecreated = row.findElements(By.tagName("td")).get(1).getText();
				System.out.println(Datecreated);
				
				if(date.equalsIgnoreCase(Datecreated))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{					
						//row.findElements(By.tagName("td")).get(0).click();
						row.findElements(By.tagName("td")).get(0).findElements(By.tagName("a")).get(0).click();
					break outerloop;
					}
					
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
				}
	
			}

		    
			driver.switchTo().defaultContent();
	    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Change name request is not found for "+date);
		}
		return new LoginScreen (driver);
	}

	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, PortalRoleDiv);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}

	public void Screenshot_assign(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, AssigntoCaseofficer);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}
	
	public String clickOnTask_performer(String date, String task, String Env) {
		String PortalUserLink = null;
		try{
			//Thread.sleep(2000); 
			/*driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("WebResource_CaseOfficerActivityQueue");*/
			//int row=1;
			scrolltoElement(driver, DownArrowDtCreated);
			Actions actions4 = new Actions(driver);
			actions4.moveToElement(DownArrowDtCreated);
	    	actions4.doubleClick().build().perform();
	    	Thread.sleep(500);
	    	
	    	 Actions actions5 = new Actions(driver);
	    	actions5.moveToElement(DownArrowDtCreated);
	    	actions5.doubleClick().build().perform();
	    	Thread.sleep(500);
	   
		  //  int rowCount1=taskTableBody.findElements(By.tagName("tr")).size();
		    
		    List<WebElement> rows = taskTableBody.findElements(By.tagName("tr"));
		    outerloop:
			for(WebElement row:rows)
			{
				List<WebElement> rowItems = row.findElements(By.tagName("td"));
				while (rowItems.size() > 0){
				String Datecreated = row.findElements(By.tagName("td")).get(1).getText();
				System.out.println(Datecreated);
				
				if(date.equalsIgnoreCase(Datecreated))
				{
					String taskName =row.findElements(By.tagName("td")).get(0).getText();
					System.out.println(taskName);
					
					if (taskName.equalsIgnoreCase(task))
					{					
						//row.findElements(By.tagName("td")).get(0).click();
						WebElement Pl = row.findElements(By.tagName("td")).get(0);
						PortalUserLink = Pl.findElement(By.tagName("a")).getAttribute("href");
						row.findElements(By.tagName("td")).get(0).findElements(By.tagName("a")).get(0).click();
					
					break outerloop;
					}
					
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
				
				}
				ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", PortalUserLink, "PLPORTALINT", Env);
			}

		    
			driver.switchTo().defaultContent();
	    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The Change name request is not found for "+date);
		}
		return PortalUserLink;
	}

	
	
}


