package browsersetup;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class Setup 
{
	//static String urlTestCrm = ConfigurationData.urlTestCrm;
	//static String urlTestCrm = ConfigurationData.getRefDataDetails(environment, keyName);
	static boolean Grid=false; //This is the grid change commented by Akshay
	static String urlDevCrm = ConfigurationData.urlDevCrm;
	static String urlTestCrmOP = ConfigurationData.urlTestCrmOP;
	static String urlAutoCrmOP = ConfigurationData.urlAutoCrmOP;
	static String urlAutoCrmPL = ConfigurationData.urlAutoCrmPL;
	static String urlTestCrmPL = ConfigurationData.urlTestCrmPL;
	static String urlDevCrmPL = ConfigurationData.urlDevCrmPL;
	static String urlTestGMP = ConfigurationData.urlTestGMP;
	static String urlTestNHSE = ConfigurationData.urlTESTNHSE;
	static String urlDevCrmOP = ConfigurationData.urlDevCrmOP;
	static String urlTestPortal = ConfigurationData.urlTestPortal;
	static String urlDevPortal = ConfigurationData.urlDevPortal;
	static String urlTestPLPortal = ConfigurationData.urlTestPLPortal;
	static String urlDevPLPortal = ConfigurationData.urlDevPLPortal;
	static String urlAutoPLPortal = ConfigurationData.urlAutoPLPortal;
	static String urlTestOPPortal = ConfigurationData.urlTestOPPortal;
	static String urlTestOPStatementPortal = ConfigurationData.urlTestOPStatementPortal;
	static String urlDevOPPortal = ConfigurationData.urlDevOPPortal;
	static String urlAutoOPPortal = ConfigurationData.urlAutoOPPortal;
	static String baseurlEnvironmentThree = ConfigurationData.baseurlEnvironmentThree;
	static String baseurlEnvironmentFour = ConfigurationData.baseurlEnvironmentFour;
	private WebDriver driver;
	static String driverPath = System.getProperty("user.dir");
	static String driverPathIE = ConfigurationData.driverPathIE;
	static String driverPathChrome = ConfigurationData.driverPathChrome;
	static String driverPathFirefox = ConfigurationData.driverPathFirefox;
	// Amit R : Added url for 
	static String CRMTestEnvironment = ConfigurationData.crmurlTestEnvironment;
	// Amit R : Added download file path in Setup.
	public static String downloadFilePath = driverPath + File.separator + "Download";
	String driverFilePath = driverPath + File.separator + "exe";
	//Amit R : Add config details for GP
	static String GP = ConfigurationData.GP;
	String scriptfileFolderPath = "D:\\Product\\";
	static String CRMUser = ConfigurationData.crmUser;
	String domain;
	
	//Akshay S: Added Test URL for GPP flow
	static String urlTestGPPPortal= ConfigurationData.urlTestGPPPortal;
	static String urlTestGPPCRM= ConfigurationData.urlTestGPPCRM;
	static String urlAutoGPPPortal= ConfigurationData.urlAutoGPPPortal;
	static String urlAutoCRMGPP= ConfigurationData.urlAutoCRMGPP;
	static String urlPLActivationLink = ConfigurationData.activationLink;
	static String urlDevGMP = ConfigurationData.urlDevGMP;
	static String urlDevNHSE = ConfigurationData.urlDEVNHSE;
	static String urlDevGPPCRM = ConfigurationData.urlDevCRMGPP;
	static String CQRSBizTalkPath= ConfigurationData.CQRSBizTalkPath;
	static String QOFAspirationFilePath= ConfigurationData.QOFAspirationFilePath;
	static String QOFAchievementFilePath= ConfigurationData.QOFAchievementFilePath;
	static String ProcessDelay= ConfigurationData.ProcessDelay;
	static String BizTalkDomain= ConfigurationData.BizTalkDomain;
	static String BiZTalkUserName= ConfigurationData.BiZTalkUserName;
	static String BizTalkPassword= ConfigurationData.BizTalkPassword;
	static String Asp_IterationFilePath= ConfigurationData.Asp_IterationFilePath;
	
	//CS Test details
	static String urlTestCSCRM= ConfigurationData.urlTestCSCRM;
	
	//CS CSV file path
	static String ColposcopySampleFilePath= ConfigurationData.ColposcopySampleFilePath;
	static String ColposcopyUploadFilePath= ConfigurationData.ColposcopyUploadFilePath;
	static String HPVSampleFilePath= ConfigurationData.HPVSampleFilePath;
	static String HPVUploadFilePath= ConfigurationData.HPVUploadFilePath;
	
	// Adjustments file path
	static String GPPAdjSampleFilePath= ConfigurationData.GPPAdjSampleFilePath;
	static String GPPAdjUploadFilePath= ConfigurationData.GPPAdjUploadFilePath;
	static String OPAdjSampleFilePath= ConfigurationData.OPAdjSampleFilePath;
	static String OPAdjUploadFilePath= ConfigurationData.OPAdjUploadFilePath;
	static String configurationFileName = "ConfigurationDetails.xlsx";

	//Added by Akshay to fetch .dat file created dynamically for QOF process
	static String NewAspSourcePath= ConfigurationData.NewAspSourcePath;
	static String NewAchSourcePath= ConfigurationData.NewAchSourcePath;	
	
	public WebDriver setupBrowser(String browser,String environment,String clientName, String module, String user) throws InterruptedException
	{
		
		domain = testdata.ConfigurationData.getRefDataDetails(environment, "DOMAIN");
		String urlTestCrm = testdata.ConfigurationData.getRefDataDetails(environment, "CRM");
		String urlTestCrmOP = testdata.ConfigurationData.getRefDataDetails(environment, "CRMOP");
		String urlTestCrmPL = testdata.ConfigurationData.getRefDataDetails(environment, "CRMPL");
		String urlTestCrmGPP = testdata.ConfigurationData.getRefDataDetails(environment, "CRMGPP");
		String urlTestGMP = testdata.ConfigurationData.getRefDataDetails(environment, "GMP");
		String urlTestNHSE = testdata.ConfigurationData.getRefDataDetails(environment, "NHSE");
		String urlTestPLPortal = testdata.ConfigurationData.getRefDataDetails(environment, "PORTALPL"); 
		String urlTestOPPortal = testdata.ConfigurationData.getRefDataDetails(environment, "PORTALOP");
		String urlTestOPStatementPortal = testdata.ConfigurationData.getRefDataDetails(environment, "PORTALSTATEMENT");
		String urlTestGPPPortal = testdata.ConfigurationData.getRefDataDetails(environment, "PORTALGPP");
		String urlTestInternalPortal = testdata.ConfigurationData.getRefDataDetails(environment, "PLPORTALINT");
		String urlTestMEPORTAL = testdata.ConfigurationData.getRefDataDetails(environment, "PORTALME");
				
		
		if (module.contains("CRM") )
		{						
			browser = "chrome";			
		}
		
		if(!Grid)
		{
			if (browser.equalsIgnoreCase("Firefox"))
			{
				File pathToFirefoxBinary = new File("D:\\Program Files\\Mozilla Firefox\\firefox.exe");
				FirefoxBinary firefoxbin = new FirefoxBinary(pathToFirefoxBinary); 
				FirefoxProfile firefoxProfile = new FirefoxProfile(); 
				firefoxProfile.setPreference("browser.startup.homepage", "about:blank");
				firefoxProfile.setPreference("startup.homepage_welcome_url", "about:blank");
				firefoxProfile.setPreference("startup.homepage_welcome_url.additional", "about:blank");
				firefoxProfile.setEnableNativeEvents(true);

				System.setProperty("webdriver.gecko.driver", driverPathFirefox);
				DesiredCapabilities capabilities=DesiredCapabilities.firefox();
				capabilities.setCapability("marionette", true);
	
				driver = new FirefoxDriver(firefoxbin, firefoxProfile, capabilities);
				//driver = new FirefoxDriver(firefoxbin, firefoxProfile);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			}
			else 
				if (browser.equalsIgnoreCase("chrome"))
				{
					try{
					 
						FileUtils.cleanDirectory(new File(downloadFilePath));
						ChromeOptions options = new ChromeOptions();
						options.addArguments("start-maximized");
						options.addArguments("disable-infobars");
						options.addArguments("--disable-notifications");
						//options.addArguments("--disable-browser-side-navigation");
						HashMap<String, Object> prefs = new HashMap<String, Object>();
						prefs.put("credentials_enable_service", false);
						prefs.put("profile.password_manager_enabled", false);
						prefs.put("profile.default_content_settings.popups", 0);
						prefs.put("download.default_directory", downloadFilePath);
						prefs.put("browser.helperApps.neverAsk.openFile",
								"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
						prefs.put("browser.helperApps.neverAsk.saveToDisk",
								"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
						options.setExperimentalOption("prefs", prefs);
						driverPathChrome = ""+driverFilePath+"\\chromedriver.exe";
						System.setProperty("webdriver.chrome.driver", driverPathChrome);
						//driver = new ChromeDriver(options);
						DesiredCapabilities capabilities = DesiredCapabilities.chrome();
						capabilities.setCapability(ChromeOptions.CAPABILITY, options);
						
						driver = new ChromeDriver(capabilities);
						driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
					
						// Amit: Added this script to execute the CRM application using CHROME browser
					
					} 
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			else if (browser.equalsIgnoreCase("Internet Explorer")) 
				 {			
					driverPathIE = ""+driverFilePath+"\\IEDriverServer.exe";
					System.setProperty("webdriver.ie.driver", driverPathIE);
					DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
					dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					dc.setCapability(CapabilityType.BROWSER_NAME, "IE");
					dc.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");
					dc.setCapability("EnableNativeEvents", false);
					dc.setCapability("ignoreZoomSetting", true);
					//dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);// Add the above capability when the security level of IE not set to same.
					dc.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
					//dc.setBrowserName("SELENIUM");
					dc.setJavascriptEnabled(true);
					dc.setCapability("browserstack.ie.enablePopups", "false");
					dc.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
					dc.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "ignore");
					dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, "false");
					dc.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					dc.setCapability("browserstack.ie.noFlash", "true");
					System.out.println("The capability has been set up for IE browser");
					driver = new InternetExplorerDriver(dc);
					System.out.println("Driver has been set up for IE ");
		
				 }	
		}
		else
			{
					DesiredCapabilities dc =null;
					if(browser.equalsIgnoreCase("chrome"))
					{
						try{
				 //           FileUtils.cleanDirectory(new File(downloadFilePath));				
				              ChromeOptions options = new ChromeOptions();				
				              options.addArguments("start-maximized");				
				              options.addArguments("disable-infobars");				
				              options.addArguments("--disable-notifications");				
				              //options.addArguments("--disable-browser-side-navigation");				
				              HashMap<String, Object> prefs = new HashMap<String, Object>();				
				              prefs.put("credentials_enable_service", false);				
				              prefs.put("profile.password_manager_enabled", false);				
				              prefs.put("profile.default_content_settings.popups", 0);			
				              prefs.put("download.default_directory", downloadFilePath);				
				              prefs.put("browser.helperApps.neverAsk.openFile",				
				                            "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
				              prefs.put("browser.helperApps.neverAsk.saveToDisk",				
				                            "text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");
				              options.setExperimentalOption("prefs", prefs);
				              driverPathChrome = ""+driverFilePath+"\\chromedriver.exe";
				              System.out.println("Drive useing "+driverPathChrome);				
				              System.setProperty("webdriver.chrome.driver", driverPathChrome);				
				              //driver = new ChromeDriver(options);				
				              dc = DesiredCapabilities.chrome();				
				              dc.setCapability(ChromeOptions.CAPABILITY, options);                    

						}catch(Exception e)

						{

							e.printStackTrace();

						}

					}

					else if (browser.equalsIgnoreCase("Internet Explorer"))
						 {
			              driverPathIE = ""+driverFilePath+"\\IEDriverServer.exe";			
			              System.setProperty("webdriver.ie.driver", driverPathIE);			
			              dc = DesiredCapabilities.internetExplorer();			
			               dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);			
			              dc.setCapability(CapabilityType.BROWSER_NAME, "IE");			
			              dc.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");			
			              dc.setCapability("EnableNativeEvents", false);			
			              dc.setCapability("ignoreZoomSetting", true);			
			              //dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);// Add the above capability when the security level of IE not set to same.
			              dc.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);			
			              //dc.setBrowserName("SELENIUM");			
			              dc.setJavascriptEnabled(true);			
			              dc.setCapability("browserstack.ie.enablePopups", "false");			
			              dc.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);			
			              dc.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "ignore");			
			              dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, "false");			
			              dc.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);			
			              dc.setCapability("browserstack.ie.noFlash", "true");			
			              System.out.println("The capability has been set up for IE browser");                  
			              }

					 try{						
							driver=new RemoteWebDriver(new URL("http://172.16.206.43:4444/wd/hub"), dc);      
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
			}
		
		switch(module.toUpperCase())
		{
			case "CS" :
					driver.navigate().to(urlTestPortal);
					break;
			case "PL" :
					driver.navigate().to(urlTestPLPortal);
					break;
			case "OP" :
					driver.navigate().to(urlTestOPPortal);
					break;
			case "OPSTATEMENT" :
					driver.navigate().to(urlTestOPStatementPortal);
					break;
					//Akshay S: added GPP Test URL
			case "GPP" :
					driver.navigate().to(urlTestGPPPortal);
					break;
			case "CRM" :
					String CRMCSURL = getCRMURLString(urlTestCrm , user, environment);
					driver.navigate().to(CRMCSURL);
				//driver.navigate().to(urlTestCrm);
					break;
			case "CRMOP" :		
					String CRMOPURL = getCRMURLString(urlTestCrmOP, user, environment);
					driver.navigate().to(CRMOPURL);
					//	driver.navigate().to(urlTestCrmOP);		
					break;
			case "CRMPL" :
					String CRMPLURL = getCRMURLString(urlTestCrmPL, user, environment);
					driver.navigate().to(CRMPLURL);
					//driver.navigate().to(urlTestCrmPL);			
					break;
			case "CRMGPP" :
					String CRMGPPURL = getCRMURLString(urlTestCrmGPP, user, environment);
					driver.navigate().to(CRMGPPURL);
					//driver.navigate().to(urlTestGPPCRM);
					break;
			case "GMP" :
					driver.navigate().to(urlTestGMP);
					break;
			case "NHSE" :
					driver.navigate().to(urlTestNHSE);
					break;			
			case "ACTIVATIONLINK" :
				//	String PLUserActivation = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ActivationLink");// Commented by Rupesh - For Selenium GIRD
					String TestCaseName = ExcelUtilities.getTestCaseName();
					String PLUserActivation =ExcelUtilities.getKeyValueByPosition("ReadWrite_TestData_PL.xlsx", "PLTestData", TestCaseName, "ActivationLink");
					//String PLUserActivation =ExcelUtilities.getKeyValueFromExcelWithPosition("ConfigurationDetails.xlsx", "PLTestData", TestCaseName, 5);
					System.out.println(PLUserActivation);
					driver.navigate().to(PLUserActivation);
					break;
			
			case "CRMCS" :
					String CRMTESTCSURL = getCRMURLString(urlTestCSCRM, user, environment);
					driver.navigate().to(CRMTESTCSURL);
					//driver.navigate().to(urlTestCSCRM);
					break;	
			case "PLPORTALINT" :
					String PortalInternalURL = getCRMURLString(urlTestInternalPortal, user, environment);
					driver.navigate().to(PortalInternalURL);
					break;
			case "ME" :
					driver.navigate().to(urlTestMEPORTAL);
					break;		
			default :
					Assert.fail(module + "OR" +environment+ "dosen't exsists");
		}		
		
					driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
					driver.manage().window().maximize();
	
	 return driver;
	}
	
	public String getCRMURLString(String url, String user, String environment)
	{
		
		String crmURL = null;
		String urlPart1 = url.substring(0,7);
		//String urlPart2 = ExcelUtilities.getKeyValueByPosition("ConfigurationDetails.xlsx", "CRMUSER", user, "ID");
		String urlPart2 = ConfigurationData.getUserDetails(environment, user+"_ID");
		//String urlPart3 = ExcelUtilities.getKeyValueByPosition("ConfigurationDetails.xlsx", "CRMUSER", user, "ID");
		String urlPart3 = ConfigurationData.getUserDetails(environment, user+"_PASSWORD");
		String urlPart4 = url.substring(7);
		crmURL = urlPart1+urlPart2+":"+urlPart3+"@"+urlPart4;
				
		return crmURL;
	}
	

}
