package pageobjects.CS;

import java.util.ArrayList;
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
import helpers.*;
import utilities.ExcelUtilities;
public class PNLPage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']/tbody")
    WebElement nonRespondersTable;
    
    @FindBy(xpath="//table[@id='pcss-todo']/tbody/tr[1]/td[8]")
    WebElement nonRespondersTableAction;
    
    @FindBy(css="button[id='newRegistrationCSV']")
    WebElement newRegCSVBtn;
    
    @FindBy(id="ceaseNHSNumber")
    WebElement ceaseNHSNumber;

    
    
    @FindBy(xpath="//table[@id='pcss-todo']/tbody/tr[1]/td[9]")
    WebElement submitarea; 
    
    @FindBy(xpath="//table[@id='pcss-todo']/tbody/tr[1]/td[9]/button")
    WebElement submitButton;
    
    @FindBy(css="div[class*='modal fade in']")
    WebElement ceaseModalWindowActive;

    @FindBy(xpath="//table[@id='pcss-patient-new-registration']/tbody")
    WebElement tblNewReg;
    
    @FindBy(id="newRegistrationsBadge")
    WebElement newRegBadge; 
    
    @FindBy(id="nonRespondersBadge")
    WebElement nonRespondersBadge; 
    
    @FindBy(id="modalCeasePopup")
    WebElement modalCeasePopup;
    
    @FindBy(xpath="//table[@class='table table-responsive table-striped']/tbody//select[@id='CeaseReason']")
	WebElement selectReasonsOptions;
	
	@FindBy(css= "div[contains(@class, 'pcss-button-group')")
	WebElement CSVTOPButtonDiv;
	
	@FindBy(xpath= "//button[@type='button'][@class='btn btn-success dropdown-toggle']")
	//@FindBy(css = ".btn.dropdown-toggle")
	//@FindBy(css="button[class*='dropdown-toggle'])")
	WebElement CSVTOPButton;
	
	@FindBy(xpath= "//button[@type='button'][@class='btn btn-success dropdown-toggle'][1]//i")
	WebElement CSVTOPButtonDropdown;
	
	@FindBy(xpath= "//div[@class='pull-right pcss-button-padding pcss-button-group open']/ul/li[4]")
	WebElement Week4DropdownLink;
	//div[contains(@class, 'Caption')
	
	@FindBy(xpath="//div[@class='col-md-3'][4]//h2[1]")
	WebElement Week4PatientsToDo;
	
	@FindBy(xpath="//div[@class='col-md-3'][1]//h2[1]")
	WebElement CurrentWeekPatientsToDo;
	
	@FindBy(xpath="//table[@id='pcss-todo-week4']/tbody")
	WebElement tblToDOWeek4;
	
	@FindBy(xpath="//h4[@class='panel-title'][2]")
	WebElement Week4Tab;
	
	//@FindBy(xpath="//a[@data-target='#collapseFour']")
	@FindBy(partialLinkText = "Week 4")
	WebElement Week4TabLink;
	
	//@FindBy(xpath="//div[@id='todo']//div[@class='panel-heading'][3]")
	@FindBy(id="collapseFour")
	WebElement Week4ToDoSec;
	
	  @FindBy(id="CeaseReason")
	    WebElement DropdownOnInline;
	    
	    @FindBy(id="ceaseSubmit")
	    WebElement Save_submitOnInline;
	    
	    @FindBy(xpath ="//button[@class='close cease-cancel']")
	    WebElement ClosebuttonOnInline;
	    @FindBy(xpath ="//table[@id='pcss-todo']/tbody/tr[1]/td[1]")
	    WebElement FirstNHSNo;
	
		@FindBy(id="ceaseFileUpload")
		WebElement ceaseFileUpload;

		@FindBy(id="submittedTab")
		WebElement Submittedtab;
		
		@FindBy(xpath ="//input[@class='pcss-searchbox form-control']")
		WebElement SearchBox;
		
		 @FindBy(xpath="//*[@id='genericmodal']/div/div")
		 WebElement ConfirmationDialogueBox;
		 String CeaseReason1 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason1");
		 String CeaseReason2 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason3");
		 String CeaseReason3 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason3");
		 
		 	
	public PNLPage(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public PNLPage SelectWeekFromCSVDropdown(String week) throws InterruptedException
	{
		
		boolean flag = toDoWeek4Patients();
		System.out.println(flag);
		
		//if(orgFound)
		
		scrolltoElement(driver, CSVTOPButton);
			
		wait.until(ExpectedConditions.elementToBeClickable(CSVTOPButton)).click();
		
		Thread.sleep(8000);
		
		
		driver.findElement(By.partialLinkText(week)).click();
	
		Thread.sleep(15000);
		
	
		
		return new PNLPage(driver);
	}
	
	public PNLPage SelectWeekFromBottomCSVDropdown(String week) throws InterruptedException
	{
		
		boolean flag = toDoWeek4Patients();
		System.out.println(flag);
		
		scrolltoElement(driver, CSVTOPButton);
		wait.until(ExpectedConditions.elementToBeClickable(CSVTOPButton)).click();
		
		Thread.sleep(3000);
		
		driver.findElement(By.partialLinkText(week)).click();
	
		Thread.sleep(15000);
		
	
		
		return new PNLPage(driver);
	}
	
	public boolean toDoWeek4Patients()
	{
		Boolean fl = false;
		String PC = Week4PatientsToDo.getText();
		//String PC = CurrentWeekPatientsToDo.getText();
		System.out.println(PC);
		String Week4PatientCount = PC.substring(0,PC.indexOf(' '));
		System.out.println(Week4PatientCount);
		
		int count = Integer.parseInt(Week4PatientCount);
		System.out.println(count);
		
		if(count > 0)
			fl = true;
		
		return fl;
				
		//return new PNLPage(driver);
	}
	
	public List<ArrayList<String>> getPNLWeek4Data()
	{
		List<ArrayList<String>> tblData = new ArrayList<ArrayList<String>>();
	//	Week4Tab.click();
		//tblData = UITableDataSupport.getUITableData(tblToDOWeek4);
		 List<Integer> colnums = new ArrayList<Integer>();
		 colnums.add(1);
		 colnums.add(2);
		 colnums.add(3);
		 colnums.add(4);
		 colnums.add(5);
		 colnums.add(6);

		tblData = UITableDataSupport.getSpecificColumnDataInTable(tblToDOWeek4,colnums );
		
		
		
		return tblData;
	}
	
	public PNLPage clickWeek4Tab()
	{
		
		/*JavascriptExecutor jse = (JavascriptExecutor)driver;
		//jse.executeScript("window.scrollBy(0,250)", "");
		//jse.executeScript("window.scrollBy(250,500)", "");
		jse.executeScript("arguments[0].scrollIntoView();", Week4TabLink);*/
		scrolltoElement(Week4TabLink);
		wait.until(ExpectedConditions.elementToBeClickable(Week4TabLink)).click();
		return new PNLPage(driver);
	}
	public void scrolltoElement(WebElement ele)
	{
		//this.driver = driver;
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView();", ele);
	}
	public PNLPage CountofPatientInWeek1() {
		try{
			List<String> currentOptions = new ArrayList<>();
			  List<WebElement> Weekchart=driver.findElements(By.xpath("//Div[@class='col-md-3']"));
              System.out.println("total checkboxes "+Weekchart.size());
              if(Weekchart.size()>0){
            	 List<WebElement> TodoList = driver.findElements(By.xpath("//Div[@class='col-md-3']//h2[1]"));
            	 int counter = 0;
            
            	 
            	  for (WebElement Todo : TodoList) {           		 
            	
            	     currentOptions.add(Todo.getText());
            	     String name = Todo.getText();
            	     System.out.println(name);
            	     String PatientCount = name.split(" ")[0].trim();
            	     System.out.println(PatientCount);
            	     int foo = Integer.parseInt(PatientCount);
            	     Thread.sleep(5000);
            	     
            	     
            	  //   driver.findElement(By.partialLinkText("Upcoming")).click();
            	     if(foo > 0 ){
            	    	
            	    	 String LinkTextname1 = driver.findElement(By.xpath("//Div[@class='col-md-3']//Div[@class='panel-heading']")).getText();
            	    	 System.out.println(LinkTextname1);
            	    	 String Linkname = LinkTextname1.split(" ")[0].trim();
            	    	 System.out.println(Linkname);
            	    	 if(Linkname.equalsIgnoreCase("Current"))
            	    	 {
            	    		 break;
            	    		 
            	    	 }
            	    	 driver.findElement(By.partialLinkText(Linkname)).click();
            	    		 
            	    	 break;
            	     }
            	     
            	    	 
            	    	 
            	    	 
            	    	 
            	    	             	    	            	    	 
            	    	/* List<WebElement> LinkTextname  = driver.findElements(By.xpath("//Div[@class='panel panel-default']//h4[1]"));
            	    	 for (WebElement Linktext : LinkTextname) {
                    	     currentOptions.add(Linktext.getText());
                    	     String Link = Linktext.getText();
                    	     System.out.println(Link);
            	    	 }
            	    	
            	    	 driver.findElement(By.partialLinkText(LinkTextname1)).click();
            	    	 */
            	     
            	     else 
            	     {
            	    	 break;
            	    	 
            	     }
            	     
            	     }
            	     
            	    
            	 
              /*for (int i=0;i<=TodoList.size();i++)
              {
               
                 String PatientCount = driver.findElement(By.xpath("//Div[@class='col-md-3']//h2[1]")).getText();
                 System.out.println(PatientCount);                                             
                      
             
              }
*/

			
              }       
           
		}
     catch(Exception e)
     {
            System.out.println("Found Exception : " + e);
     }

		
	
	return new PNLPage(driver);
	}
	
	public PNLPage selectActionFromFirstRecord(String text) throws InterruptedException
    {
           //WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
           WebElement column = nonRespondersTableAction;
           
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
           
           Select dropdown = new Select(nonRespondersTableAction.findElement(By.id("ActionId")));
    
           
           WebElement option = dropdown.getFirstSelectedOption();
           System.out.println("The previous dropdown value selected: "+option.getText());
           
           dropdown.selectByVisibleText(text);
           Thread.sleep(4000);
           WebElement option1 = dropdown.getFirstSelectedOption();
           
           System.out.println("The dropdown value selected: "+option1.getText());
           
           
           return new PNLPage(driver);     
    }

    public boolean clickSubmitAndCheckPopupUp() throws InterruptedException
    {
           boolean flag = false;
           try
           {
           
        //   wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
           Thread.sleep(2000);
           
           if (ceaseModalWindowActive.isDisplayed())
                  flag = true;
           
           //wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
           
                        
           
           }
           catch(Exception e)
           {
                  System.out.println("Found Exception : " + e);
           }
           
           return flag;
           
	
    }
    
    public List<String> getReasonForCease()
	{
		/*List<String> options = new ArrayList<String>();
	    for (WebElement option : new Select(driver.findElement(by)).getOptions()) {
	        String txt = option.getText();
	        if (option.getAttribute("value") != "") options.add(option.getText());
	    }
	    return options;*/
		
		List<String> options = helpers.CommonFunctions.getAllOptions(selectReasonsOptions);
		
		return options;
		
		
	}
    
    
	
	public PNLPage selectActionFromFirstRecord(String text ,String Actualtablename) throws InterruptedException
    {
           //WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
        System.out.println(Actualtablename); 
        WebElement Dropdownfocus = driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[8]"));
        
		WebElement column = driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[8]"));
           
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
           
           Select dropdown = new Select(Dropdownfocus.findElement(By.id("ActionId")));
    
           
           WebElement option = dropdown.getFirstSelectedOption();
           System.out.println("The previous dropdown value selected: "+option.getText());
           
           dropdown.selectByVisibleText(text);
           Thread.sleep(4000);
           WebElement option1 = dropdown.getFirstSelectedOption();
           
           System.out.println("The dropdown value selected: "+option1.getText());
           
           
           return new PNLPage(driver);     
    }

    public boolean clickSubmitAndCheckPopupUp(String Actualtablename) throws InterruptedException
    {
        System.out.println(Actualtablename);   
    	boolean flag = false;
           try
           {
           
           WebElement SubmitButton = driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[9]/button"));
       //    wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
           SubmitButton.click();
           Thread.sleep(2000);
           
           if (ceaseModalWindowActive.isDisplayed())
                  flag = true;
           
           //wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
           
                        
           
           }
           catch(Exception e)
           {
                  System.out.println("Found Exception : " + e);
           }
           
           return flag;
           
	
    }
    
    public String getNHSNoOfCeasePatient() {
        String NHSNo = ceaseNHSNumber.getText().replaceAll("\\s+","");
        System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
          
 
 // TODO Auto-generated method stub
 return NHSNo;
}
    
    public Boolean VerifyCeaseOnPortal(String Actualtablename) {
		  boolean flagforTickMark = false;
        try
        {
        Thread.sleep(3000);
        //wait.until(ExpectedConditions.elementToBeClickable(submitarea)).click();
        String Stylevalue = driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[9]/span")).getAttribute("style");
        System.out.println(Stylevalue);
        if (Stylevalue.equalsIgnoreCase("display: block;"))
        {
        	flagforTickMark = true;        
                
                     
        
        }
        else
        {
      	  System.out.println("Ceasing is not performed sucessfully due to some error");
        }
        }
        
        catch(Exception e)
        {
               System.out.println("Found Exception : " + e);
        }
        
        return flagforTickMark;
	}
    
    public PNLPage submitCease() {
		
		 try
        {
        
			// new Select(DropdownOnInline).selectByVisibleText("Due to Age"); 
			 
        wait.until(ExpectedConditions.elementToBeClickable(Save_submitOnInline)).click();
       
        Actions actions = new Actions(driver);
    	actions.moveToElement(ClosebuttonOnInline);
    	actions.click().build().perform();
        
                     
        
        }
        catch(Exception e)
        {
               System.out.println("The Element is not found in Inline Diolague Box " + e);
        }
        
		// TODO Auto-generated method stub
		 return new PNLPage(driver);
	}
    
    
    public String CountofPatientInWeekWithTable() {
		String Actualtablename = null;
		try{
			
			
			
			List<String> currentOptions = new ArrayList<>();
			  List<WebElement> Weekchart=driver.findElements(By.xpath("//Div[@class='col-md-3']"));
              System.out.println("total checkboxes "+Weekchart.size());
              if(Weekchart.size()>0){
            	 List<WebElement> TodoList = driver.findElements(By.xpath("//Div[@class='col-md-3']//h2[1]"));
            //	 List<WebElement> LinkList = driver.findElements(By.xpath("//Div[@class='col-md-3']//Div[@class='panel-heading']"));
            	 int counter = 0;
            //	 driver.findElement(By.partialLinkText("Upcoming")).click();
            
            	 
            	  for (WebElement Todo : TodoList) {           		 
            		 
            	     currentOptions.add(Todo.getText());
            	     String name = Todo.getText();
            	     System.out.println(name);
            	     String PatientCount = name.split(" ")[0].trim();
            	     System.out.println(PatientCount);
            	     int foo = Integer.parseInt(PatientCount);
            	     System.out.println(counter);
            	     Thread.sleep(5000);
            	     
            	     if (counter == 3)
              	     {
              	    	 String LinkTextname1 = driver.findElement(By.xpath("//Div[@class='col-md-3']//Div[@class='panel-heading ']")).getText();
              	    	 System.out.println(LinkTextname1);
            	    	 String Linkname = LinkTextname1.split(" ")[0].trim();
            	    	 System.out.println(Linkname);
            	    	 if(Linkname.startsWith("Week"))
            	    	 {
            	    		 String Linkname0 = LinkTextname1.split("\n")[0].trim();
            	    		 //String Linkname1 = LinkTextname1.trim(); 
            	    		 System.out.println(Linkname0);
            	    		 String Linkname1 = Linkname0.replaceAll("\\s+","");
            	    		 System.out.println(Linkname1);
            	    		 String Tablename1 = ("pcss-todo-"+Linkname1);
            	    		 Actualtablename = Tablename1.toLowerCase();
                        	 
                	    	 System.out.println(Actualtablename);
                	    	
                	    WebElement Link = driver.findElement(By.partialLinkText(Linkname0));
                	    scrolltoElement(driver, Link);
                	    Link.click();
                	    
                	    	 break;
            	    	 }
              	     }
            	     
            	  //   driver.findElement(By.partialLinkText("Upcoming")).click();
            	     if(foo > 0 ){
            	    	 String LinkTextname1 = driver.findElements(By.xpath("//Div[@class='col-md-3']//Div[@class='panel-heading']")).get(counter).getText();
            	     
            	    	
            	    	 
            	    	// String LinkTextname1 = driver.findElement(By.xpath("//Div[@class='col-md-3']//Div[@class='panel-heading']["+counter+"]")).getText();
                    //	 String LinkTextname1 = LinkList['+counter+"'];
                    	 System.out.println(LinkTextname1);
            	    	 String Linkname = LinkTextname1.split(" ")[0].trim();
            	    	 System.out.println(Linkname);
            	    	 if(Linkname.startsWith("Week"))
            	    	 {
            	    		 String Linkname0 = LinkTextname1.split("\n")[0].trim();
            	    		 //String Linkname1 = LinkTextname1.trim(); 
            	    		 System.out.println(Linkname0);
            	    		 String Linkname1 = Linkname0.replaceAll("\\s+","");
            	    		 System.out.println(Linkname1);
            	    		 String Tablename1 = ("pcss-todo-"+Linkname1);
            	    		 Actualtablename = Tablename1.toLowerCase();
                        	 
                	    	 System.out.println(Actualtablename);
                	    	 System.out.println(Linkname0);
                	    	 WebElement Link = driver.findElement(By.partialLinkText(Linkname0));
                	    	 scrolltoElement(driver, Link);
                	    	 Link.click();
                  	    	 break;
            	    	 }
            	    	 if(Linkname.equalsIgnoreCase("Current"))
            	    	 {
            	    		 Actualtablename = "pcss-todo";
            	    		 break;
            	    		 
            	    	 }
            	    	 Thread.sleep(3000);
            	    	 driver.findElement(By.partialLinkText(Linkname)).click();
            	    	 
            	    	 String Tablename1 = ("pcss-todo-"+Linkname);
            	    	 Actualtablename = Tablename1.toLowerCase();
            	 
            	    	 System.out.println(Actualtablename);
            	    	 
            	    	// WebElement Tablename = driver.findElement(By.xpath("//table[@id='pcss-todo-"+Linkname+"']/tbody/tr[1]/td[8]"));
            	    	  
            	    		 
            	    	 break;
            	    	
            	     }
            	     counter++;
            	    	 
            	    	 
            	    	 
            	    	 
            	    	             	    	            	    	 
            	    	/* List<WebElement> LinkTextname  = driver.findElements(By.xpath("//Div[@class='panel panel-default']//h4[1]"));
            	    	 for (WebElement Linktext : LinkTextname) {
                    	     currentOptions.add(Linktext.getText());
                    	     String Link = Linktext.getText();
                    	     System.out.println(Link);
            	    	 }
            	    	
            	    	 driver.findElement(By.partialLinkText(LinkTextname1)).click();
            	    	 */
            	     
            	     /*else 
            	     {
            	    	 break;
            	    	 
            	     }*/
            	     
            	     }
            	     
            	    
            	 
              /*for (int i=0;i<=TodoList.size();i++)
              {
               
                 String PatientCount = driver.findElement(By.xpath("//Div[@class='col-md-3']//h2[1]")).getText();
                 System.out.println(PatientCount);                                             
                      
             
              }
*/

			
              }       
           
		}
     catch(Exception e)
     {
            System.out.println("Found Exception : " + e);
     }

		
	
	
	return Actualtablename;
	}
   
    
	public PNLPage selectCeasingReasonandUploadFile(String reason) throws InterruptedException {
		Select dropdown = new Select(driver.findElement(By.id("CeaseReason")));
		dropdown.selectByVisibleText(reason);
		
		if (reason.contentEquals("Informed Consent"))
		{
		
		wait.until(ExpectedConditions.elementToBeClickable(ceaseFileUpload));
		
		String filePath = System.getProperty("user.dir") + "\\Upload\\Sample.pdf";
		System.out.println(filePath);
		ceaseFileUpload.sendKeys(filePath);
		Thread.sleep(5000);
	
		}
		return new PNLPage(driver);
	}

	public String getNHSNoOfCeasePatientWithSpaces() {
		String InlineNhsNo = ceaseNHSNumber.getText();
		
       System.out.println("The NHSNUMBER on Cease Modal Window: "+InlineNhsNo);
          
 
 // TODO Auto-generated method stub
 return InlineNhsNo;
}
	
	public PNLPage SubmittedTab()
	{
		scrolltoElement(driver, Submittedtab);
		Actions actions = new Actions(driver);
		actions.moveToElement(Submittedtab);
		actions.click().build().perform();
		
		wait.until(ExpectedConditions.elementToBeClickable(SearchBox));
		return new PNLPage(driver);
	}

	public PNLPage SearchNHSNoOnSubmittedTab(String InlineNhsNo)
	{
			
		scrolltoElement(driver, SearchBox);
    	wait.until(ExpectedConditions.elementToBeClickable(SearchBox)).click();
		SearchBox.sendKeys(InlineNhsNo);
		SearchBox.sendKeys(Keys.ENTER);
		
		return new PNLPage(driver);
	}
	
	public PNLSearchResultPage FirstResultofsearchOnSubmitted(String InlineNhsNo)
	{
		
		driver.findElement(By.partialLinkText(InlineNhsNo)).click();
		
		return new PNLSearchResultPage(driver);
	
	}
	
public String Verifyactionaftercancelcease(String Actualtablename1) {
		
	    System.out.println(Actualtablename1); 
        WebElement Dropdownfocus = driver.findElement(By.xpath("//table[@id='"+Actualtablename1+"']/tbody/tr[1]/td[8]"));
        
		WebElement column = driver.findElement(By.xpath("//table[@id='"+Actualtablename1+"']/tbody/tr[1]/td[8]"));
           
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
           
           Select dropdown = new Select(Dropdownfocus.findElement(By.id("ActionId")));
    
           
           WebElement option = dropdown.getFirstSelectedOption();
           String actionaftercancelcease = option.getText();
           System.out.println("The previous dropdown value selected: "+actionaftercancelcease);
      
           
          
           
		// TODO Auto-generated method stub
		return actionaftercancelcease;
	}

	public String getNHSNoOfCeasePatientWithSpacesforGPCallRecall(String Actualtablename ) {
	
	
	String InlineNhsNo = driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[1]")).getText();
	
   System.out.println("The NHSNUMBER on Cease Modal Window: "+InlineNhsNo);
      

// TODO Auto-generated method stub
   return InlineNhsNo;
	}

	public String getNHSNoOfCeasePatientforcallRecall(String InlineNhsNo) {
	System.out.println(InlineNhsNo);
	
    String NHSNo = InlineNhsNo.replaceAll("\\s+","");
    System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
      

// TODO Auto-generated method stub
    return NHSNo;
	}
	
	public PNLPage ClickOnSubmitOntable(String Actualtablename) {
		
		driver.findElement(By.xpath("//table[@id='"+Actualtablename+"']/tbody/tr[1]/td[9]/button")).click();
 
 // TODO Auto-generated method stub
 return new PNLPage(driver);
}
	
	public PNLPage selectActionFromFirstRecordonSubmitted(String text) throws InterruptedException
    {
           //WebElement column = UITableDataSupport.getWebElementFromTable(nonRespondersTable, 1, 8);
      
        WebElement Dropdownfocus = driver.findElement(By.xpath("//table[@id='pcss-submitted']/tbody/tr[1]/td[8]"));
        
		WebElement column = driver.findElement(By.xpath("//table[@id='pcss-submitted']/tbody/tr[1]/td[8]"));
           
           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
           
           Select dropdown = new Select(Dropdownfocus.findElement(By.id("ActionId")));
    
           
           WebElement option = dropdown.getFirstSelectedOption();
           System.out.println("The previous dropdown value selected: "+option.getText());
           
           dropdown.selectByVisibleText(text);
           Thread.sleep(4000);
           WebElement option1 = dropdown.getFirstSelectedOption();
           
           System.out.println("The dropdown value selected: "+option1.getText());
           
           
           return new PNLPage(driver);     
    }
	
	public PNLSearchResultPage ClickOnSubmitOnSubmittedtab() {
		driver.findElement(By.xpath("//table[@id='pcss-submitted']/tbody/tr[1]/td[9]/button")).click();
		 
		 // TODO Auto-generated method stub
		 return new PNLSearchResultPage(driver);
	}


	public Boolean VerifyConfirmationDialgoueBox() {
		boolean ConfirmationBox = false;
		if(ConfirmationDialogueBox.isDisplayed()){
			ConfirmationBox = true;
		}
		else
		{
			ConfirmationBox = false;
		}
		// TODO Auto-generated method stub
			return ConfirmationBox;	
	}
	
	public Boolean VerifyActiononSubmittedTab(String InlineNhsNo) throws InterruptedException {
		driver.navigate().refresh();
		wait.until(ExpectedConditions.elementToBeClickable(SearchBox)).click();
		SearchBox.sendKeys(InlineNhsNo);
		SearchBox.sendKeys(Keys.ENTER);
		
		Thread.sleep(3000);
		boolean actiononsubmittedtab =false;
		 WebElement Dropdownfocus = driver.findElement(By.xpath("//table[@id='pcss-submitted']/tbody/tr[1]/td[8]"));
	        
			WebElement column = driver.findElement(By.xpath("//table[@id='pcss-submitted']/tbody/tr[1]/td[8]"));
	           
	           ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", column);
	           
	           Select dropdown = new Select(Dropdownfocus.findElement(By.id("ActionId")));
	    
	           
	           WebElement option = dropdown.getFirstSelectedOption();
	           String ActiononSubmittedaftercancel = option.getText();
	           
	           System.out.println("The previous dropdown value selected: "+option.getText());
		if(ActiononSubmittedaftercancel.equalsIgnoreCase("GP Cease"))
		{
			actiononsubmittedtab = true;
		}
		else
		{
			actiononsubmittedtab = false;
		}
		return actiononsubmittedtab;
	}
	
	 public Boolean Ceaseoptions()
	 {
		 Boolean optionFlag = false;
		 List<String> options = getReasonForCease();
		
		 if(options.contains(CeaseReason1) && options.contains(CeaseReason2) && options.contains(CeaseReason3))
			{
			 optionFlag = true;
			}
				 
		 return optionFlag;
		 
	 }

	public String getNHSNoonPNL(String actualtablename) {
		String NHSNumberForPatientDetail = null;		
		System.out.println("The table name is passed: "+actualtablename);
		try { 
			Thread.sleep(2000);
			 NHSNumberForPatientDetail= driver.findElement(By.xpath("//table[@id='"+actualtablename+"']/tbody/tr[1]/td[1]")).getText();
		}
		 catch(Exception e)
	     {
	            System.out.println("The first record on PNL ( NHS number ) not getting : " + e);
	     }
		return NHSNumberForPatientDetail;
	}
	
	public boolean checkActiveRecord(String tableName, int columnNumber)
	{
		boolean activeRecordFlag = false;
		int rowNumber=1;
		
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='"+tableName+"']/tbody/tr"));
		int rowCount = rows.size();
		System.out.println(rowCount);
		
		for (int i=1; i<=rowCount;i++)

		{
			try{
			
				WebElement subCol = driver.findElement(By.xpath("//table[@id='"+tableName+"']/tbody/tr["+i+"]/td["+columnNumber+"]"));
				System.out.println(subCol.getText());
				String value = subCol.findElement(By.tagName("button")).getAttribute("disabled");
				System.out.println(value);
				System.out.println(subCol.findElement(By.tagName("button")).getAttribute("id"));
						if (value == null && rowNumber<=rowCount)
						{
			        	break;
						}
						else
							rowNumber++;
						
				}
			        catch (Exception e) {
			        	
			    	}
			
				
		       
		    } 
		
		if (rowNumber>rowCount)
		{
			System.out.println("No active records in table.");
			return false;
		}
			
			
		return true;
		
		
		//return activeRecordFlag;					
	}

	public Boolean VerifyCurrentWeek(String PortalNo) {
		boolean PresentNHSNO = false;
		// TODO Auto-generated method stub
		try{
	
				System.out.println(PortalNo);
				WebElement table = driver.findElement(By.id("pcss-todo"));
				List<WebElement> listOfRows = table.findElements(By.tagName("tr"));
				System.out.println("Rows: "+listOfRows.size());
				for (int i =1 ;i<(listOfRows.size());i++)
				{
					
					
					String ActualNHSNumber = driver.findElement(By.xpath("//*[@id='pcss-todo']//tbody/tr["+i+"]/td[1]//a")).getText();
					if (ActualNHSNumber.equalsIgnoreCase(PortalNo))
					{
						
						PresentNHSNO = true; 
						break;
						
						
					}
				}
			
			
		}
		 catch(Exception e)
	     {
	            System.out.println("The Record is not found in Current Week" + e);
	     }
		return PresentNHSNO;
	}
}
