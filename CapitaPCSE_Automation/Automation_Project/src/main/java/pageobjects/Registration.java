package pageobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import helpers.Support;
import helpers.WindowHandleSupport;
import junit.framework.Assert;
import utilities.ExcelUtilities;

public class Registration extends Support {
	
WebDriver driver;
WebDriverWait wait;
    
    @FindBy(id="UserName")
    WebElement UserName;
    
    @FindBy(id="ConfirmUserName")
    WebElement ConfirmUserName;
    
    @FindBy(id="FirstName")
    WebElement FirstName;
    
    @FindBy(id="SurName")
    WebElement SurName;
    
    @FindBy(id="GMC_GDC_GOC_Number")
    WebElement GMC_GDC_GOC_Number;
    
    @FindBy(id="ConfirmGMC_GDC_GOC_Number")
    WebElement ConfirmGMC_GDC_GOC_Number;
    
    @FindBy(id="Password")
    WebElement Password;
    
    @FindBy(id="ConfirmPassword")
    WebElement ConfirmPassword;
    
    @FindBy(id ="validateRegistration")
    WebElement RegisterButton;
    
    @FindBy(xpath="\\div[@class='modal-dialog']")
    WebElement ModalWindow;
    
    @FindBy(id="ApproveModal")
    WebElement ApprocalModalWindow;
    
    @FindBy(xpath="//div[@class='modal-body']/div[1]")
    WebElement ModalPara1;
    
    @FindBy(xpath="//div[@class='modal-body']/div[2]")
    WebElement ModalPara2;
    
    @FindBy(xpath="//div[@class='modal-body']/div[3]")
    WebElement ModalPara3;
    
    @FindBy(css="div[class='modal-dialog']")
	WebElement ModalDialog;
    
    @FindBy(css="input[id='saveRegistration']")
    WebElement saveButton;
    
    String domain;
    int position;
    String applicant;

    
    public Registration(WebDriver driver)
    {
           this.driver = driver;
           this.driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
           wait = new WebDriverWait(this.driver, 30);
           //this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
           PageFactory.initElements(this.driver, this);
    }

    public List<String> enterRegistrationDetails(String environment, String appType) throws InterruptedException, IOException
    {
    	 
    	ArrayList<String> UD = new ArrayList<String>();
    	domain = testdata.ConfigurationData.getRefDataDetails(environment, "DOMAIN");
    	    	
    /*	switch(env.toUpperCase())
    	{
    	case "TEST":
    		domain = testdata.ConfigurationData.testDomain;
    		break;
    		
    	case "AUTO":
    		domain = testdata.ConfigurationData.testDomain;
    		break;
    		
    	case "DEV":
    		domain = testdata.ConfigurationData.devDomain;
    		break;
    		
    		default:
    			System.out.println("The environment provided is not valid");
    			Assert.fail(env + "dosen't exsists");
    	}*/
    	
    	switch(appType.toUpperCase())
    	{
    	case "MEDICAL":
    		position = 1;
    		applicant = "MEDICAL";
    		break;
    		
    	case "OPTHO":
    		position = 2;
    		applicant = "OPTHO";
    		break;
    		
    	case "DENTAL":
    		position = 3;
    		applicant = "DENTAL";
    		break;
    		
    		default:
    			System.out.println("The Applicaton Type provided is not valid");
    			Assert.fail(appType + "dosen't exsists");
    	}
    	
    	
    	String userName = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "UserName", position);
    	System.out.println(userName);
    	String RandonNumber = helpers.CommonFunctions.generateRandomNo(4);
      	String HrMin = CommonFunctions.getCurrentHourMin();
    	String userID = userName+HrMin+RandonNumber;
    	 	
    	String userNameText =userID+"@"+domain;
    	String TestCaseName = ExcelUtilities.getTestCaseName();
    
    	System.out.println("__________________________"+TestCaseName);
    // String userNameText =userID+domain;
    //		ExcelUtilities.setKeyValueinExcelWithPosition("PerformerPortal.xlsx", "Register", "UserEmail", userNameText, position); Commented by Rupesh for GRID
    	
    	/*Method method;
    	String mName=method.getName();*/
    	/*BaseClass bs=new BaseClass();
    	String TestCaseName=bs.getTestCaseName();
    	System.out.println("++++++++++++++++"+TestCaseName);*/
    /*	BaseClass bc=new BaseClass();
    	bc.testCaseName();*/
    	ExcelUtilities.setValueByKeyInColumn("ReadWrite_TestData_PL.xlsx", "PLTestData", userNameText, TestCaseName, "UserEmail");    	
    	
    	UD.add(userNameText);
    	//String ConfirmUserName = ExcelUtilities.getKeyValueFromExcel("PerformerPortal.xlsx", "Register", "ConfirmUserName");
    	String FirstNametxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "FirstName", position);
    	String SurNametxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "SurName", position);
    	String Passwordtxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "Password", position);
    	
    	String GMCNumExp = CommonFunctions.generateRandomNo(5);
    	//String GMCNum ="77"+"-"+GMCNumExp; Commented by Rupesh - Changed the Name for GMC Number String
    	String GMCNumbertxt ="77"+"-"+GMCNumExp;
    	System.out.println(GMCNumbertxt);
    	//ExcelUtilities.setKeyValueByPosition("PerformerPortal.xlsx", "Register", GMCNum, "GMCNumber", applicant); // Commented by Rupesh for GRID
    	ExcelUtilities.setValueByKeyInColumn("ReadWrite_TestData_PL.xlsx", "PLTestData", GMCNumbertxt, TestCaseName, "GMCNumber");  
    	
    //	Thread.sleep(2000);
    	//String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",position);
    	//String GMCNumbertxt = ExcelUtilities.getKeyValueFromExcelWithPosition("ConfigurationDetails.xlsx", "PLTestData", TestCaseName, 3);
//    	String GMCNumbertxt =ExcelUtilities.getKeyValueByPosition("ReadWrite_TestData_PL.xlsx", "PLTestData", TestCaseName, "GMCNumber");// Commented by Rupesh - Here No need to get the GMC Number 
    	UD.add(GMCNumbertxt);
    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
       //  	System.out.println(driver.getPageSource());
		   File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        //    String filePath = System.getProperty("user.dir") + "\\Evidence\\";
	            String filePath =System.getProperty("user.dir") + "\\";
	            String filepathwithName = filePath + "[Registration].png";
	            System.out.println(filepathwithName);
	            FileUtils.copyFile(scrfile,new File(filepathwithName),true );
//     	Thread.sleep(2000);
    	wait.until(ExpectedConditions.elementToBeClickable(UserName)).clear();
       	UserName.sendKeys(userNameText);
      	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmUserName)).clear();
    	ConfirmUserName.sendKeys(userNameText);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();
    	FirstName.sendKeys(FirstNametxt);
    	UD.add(FirstNametxt);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
    	SurName.sendKeys(SurNametxt);
    	UD.add(SurNametxt);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(GMC_GDC_GOC_Number)).clear();
    	GMC_GDC_GOC_Number.sendKeys(GMCNumbertxt);
    	System.out.println("The GMC number is entered");
    	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmGMC_GDC_GOC_Number)).clear();
    	ConfirmGMC_GDC_GOC_Number.sendKeys(GMCNumbertxt);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(Password)).clear();
    	Password.sendKeys(Passwordtxt);
    	System.out.println("The Password is entered");
    	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmPassword)).clear();
    	ConfirmPassword.sendKeys(Passwordtxt);
    	
    	System.out.println("The Registeration form filled");
    	
    	return UD;    	
    	   	
    }
    
    public void clickOnRegisterButton() throws InterruptedException
    {
    	wait.until(ExpectedConditions.elementToBeClickable(RegisterButton)).click();
    	System.out.println("The clicking on Reg button");
    	//Thread.sleep(10000);
    	
   	helpers.Support.PageLoadExternalwait(driver);
    	//wait.until(ExpectedConditions.attributeContains(ApprocalModalWindow, "Style", "display: block"));
    	//Thread.sleep(5000); 
    }
    
    public void clickOKButtonModal() throws InterruptedException
    {
    	System.out.println("Para 2: "+ModalPara2.getText());
    	System.out.println("Para 3: "+ModalPara3.getText());
    	wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    	
    	//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("input[id='saveRegistration']")));
    	
   	helpers.Support.PageLoadExternalwait(driver); //Added by Rupesh
    //	helpers.Support.PageLoadExternalwait_Performer(driver); Commented by Rupesh - New ID for Spinner and changed Logic.
    }
    
      
    public List<String> CompleteRegistration(String environment, String type) throws InterruptedException, IOException
    {
    	List<String> UD = enterRegistrationDetails(environment, type);
    	String uName = UD.get(0);
    	int position = 0;
    	clickOnRegisterButton();
        	//getDetailsFromModalWindow();
  	clickOKButtonModal();
    	 	
       	switch(environment.toUpperCase())
		{
		case "TEST" :
			position = 2;
			break;
		case "CLONE" :
			position = 3;
			break;
		case "SECURITY" :
			position = 5;
			break;
		default :
			Assert.fail(environment + "dosen't exsists");
		}
       	String TestCaseName = ExcelUtilities.getTestCaseName();
    	ExcelUtilities.setKeyValueinExcelWithPosition("ConfigurationDetails.xlsx", "USERDETAILS", "PLAPPLICANT_ID", uName, position);
          ExcelUtilities.setValueByKeyInColumn("ReadWrite_TestData_PL.xlsx", "PLTestData", uName, TestCaseName, "PLAPPLICANT_ID");    	
    	//Thread.sleep(1000);
    	return UD;
    }
    
    public List<String> CompleteRegistration(String environment, List<String> portalValues,String role,String file, String sheet) throws InterruptedException, IOException
    {
    	List<String> UD = enterRegistrationDetails(environment,portalValues,role,file,sheet);
    	//String uName = UD.get(0);
    	//int position = 0;
    	clickOnRegisterButton();
        	//getDetailsFromModalWindow();
    	 	clickOKButtonModal();
    	 	
       	switch(environment.toUpperCase())
		{
		case "TEST" :
			position = 2;
			break;
		case "DEV" :
			position = 3;
			break;
		case "CLONE" :
			position = 4;
			break;
		default :
			Assert.fail(environment + "dosen't exsists");
		}
/*    	ExcelUtilities.setKeyValueinExcelWithPosition("ConfigurationDetails.xlsx", "USERDETAILS", role, uName, position);
    	System.out.println("Username has been saved in Config file for role: "+role);*/
    	Thread.sleep(1000);
    	return UD;
    }
    
    public List<String> enterRegistrationDetails(String environment, List<String> portalValues,String role,String file,String sheet) throws InterruptedException, IOException
    { 
    	String userName= portalValues.get(0).toString();
    	String firstName= portalValues.get(1).toString();
    	String surname= portalValues.get(2).toString();
    	String password= portalValues.get(3).toString();

    	ArrayList<String> UD = new ArrayList<String>();
    	domain = testdata.ConfigurationData.getRefDataDetails(environment, "DOMAIN");
    	
    	String userID = (userName+role).toLowerCase();
    	String userNameText =userID+"@"+domain;
    	System.out.println("Username length is for user "+userNameText+" is "+userNameText.length());
    	UD.add(userNameText);
    	
    	String GMCNum = CommonFunctions.generateRandomNo(5);
    	System.out.println("GMC Code for role: "+role+" is "+GMCNum);
    	Thread.sleep(2000);
    	UD.add(GMCNum);
    	WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
    	File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String filePath =System.getProperty("user.dir") + "\\";
        String filepathwithName = filePath + "UserRegistration_"+role+".png";
        System.out.println(filepathwithName);
        FileUtils.copyFile(scrfile,new File(filepathwithName),true );
     	Thread.sleep(2000);
    	wait.until(ExpectedConditions.elementToBeClickable(UserName)).clear();
       	UserName.sendKeys(userNameText);
      	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmUserName)).clear();
    	ConfirmUserName.sendKeys(userNameText);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(FirstName)).clear();
    	FirstName.sendKeys(userName+firstName);
    	UD.add(userName+firstName);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(SurName)).clear();
    	SurName.sendKeys(surname);
    	UD.add(surname);
    	
    	String fullName= userName+firstName+" "+surname;
    	UD.add(fullName);
    	wait.until(ExpectedConditions.elementToBeClickable(GMC_GDC_GOC_Number)).clear();
    	GMC_GDC_GOC_Number.sendKeys(GMCNum);
    	System.out.println("The GMC number is entered");
    	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmGMC_GDC_GOC_Number)).clear();
    	ConfirmGMC_GDC_GOC_Number.sendKeys(GMCNum);
    	
    	wait.until(ExpectedConditions.elementToBeClickable(Password)).clear();
    	Password.sendKeys(password);
    	System.out.println("The Password is entered");
    	
    	wait.until(ExpectedConditions.elementToBeClickable(ConfirmPassword)).clear();
    	ConfirmPassword.sendKeys(password);
    	ExcelUtilities.setKeyValueByPosition(file, sheet, userNameText, role, "UserEmailAddress");
    	
    	System.out.println("The Registeration form filled");
    	return UD;  	
    }

}
