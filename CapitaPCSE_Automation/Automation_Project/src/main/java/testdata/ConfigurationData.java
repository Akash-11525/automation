package testdata;

import org.junit.Assert;

import utilities.ExcelUtilities;

public class ConfigurationData 
{
	static String configurationFileName = "ConfigurationDetails.xlsx";	
	
	public static String getUserDetails(String environment, String keyName)
	{
		
		
		String sheetName = "USERDETAILS";
				
		int keyValueNumber = 0;
		switch(environment.toUpperCase())
		{
		case "TEST" :
			keyValueNumber = 2;
			break;
		case "CLONE" :
			keyValueNumber = 3;
			break;
		case "SIT1" :
			keyValueNumber = 4;
			break;
		case "SECURITY" :
			keyValueNumber = 5;
			break;
		case "SIT3" :
			keyValueNumber = 6;
			break;
		default :
			Assert.fail(environment + "dosen't exsists");
		}
		return ExcelUtilities.getKeyValueFromExcelWithPosition(configurationFileName, sheetName, keyName, keyValueNumber);
	}
	
	public static String getRefDataDetails(String environment, String keyName)
	{		
		
		String sheetName = "REFDATA";
				
		int keyValueNumber = 0;
		switch(environment.toUpperCase())
		{
		case "TEST" :
			keyValueNumber = 1;
			break;
		case "CLONE" :
			keyValueNumber = 2;
			break;
		case "SIT1" :
			keyValueNumber = 3;
			break;
		case "SECURITY" :
			keyValueNumber = 4;
			break;
		case "SIT3" :
			keyValueNumber = 5;
			break;
		default :
			Assert.fail(environment + "dosen't exsists");
		}
		return ExcelUtilities.getKeyValueFromExcelWithPosition(configurationFileName, sheetName, keyName, keyValueNumber);
	}
	
	// CRM URL Details
	public static String urlTestCrm = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRM");
	public static String urlDevCrm = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVCRM");
	public static String urlTestCrmOP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRMOP");
	public static String urlTestCrmPL = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRMPL");
	public static String urlDevCrmPL = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVCRMPL");
	public static String urlTestGMP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTGMP");
	public static String urlDevGMP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVGMP");
	public static String urlTESTNHSE = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTNHSE");
	public static String urlDEVNHSE = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVNHSE");
	public static String urlAutoCrmPL = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOCRMPL");
	public static String urlDevCrmOP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVCRMOP");
	public static String urlAutoCrmOP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOCRMOP");
	public static String baseurlEnvironmentThree = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Environment 3");
	public static String baseurlEnvironmentFour = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Environment 4");
	
	//Portal URL Details
		public static String urlTestPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTPORTAL");
		public static String urlDevPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVPORTAL");
		public static String urlTestPLPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTPLPORTAL");
		public static String urlDevPLPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVPLPORTAL");
		public static String urlAutoPLPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOPLPORTAL");
		public static String urlTestOPPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTOPPORTAL");
		public static String urlTestOPStatementPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTOPSTATEMENTPORTAL");
		public static String urlDevOPPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVOPPORTAL");
		public static String urlAutoOPPortal = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOOPPORTAL");
		public static String portalurlEnvironmentThree = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Portal URL Env 3");
		public static String portalurlEnvironmentFour = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Portal URL Env 4");
		
		public static String crmurlTestEnvironment = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRM");
	
	//Onboarding URL
	/*public static String onboardingSEIIntegartion = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "SEI Onboarding Integration URL");
	public static String onboardingBrewinIntegartion = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Brewin Onboarding Integration URL");
	public static String onboardingSEIUAT = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "SEI Onboarding UAT URL");
	public static String onboardingBrewinUAT = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Brewin Onboarding UAT URL");
	public static String onboardingSEIPreprod = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "SEI Onboarding Preprod URL");
	public static String onboardingBrewinPreprod = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Brewin Onboarding Preprod URL");
	//static String driverPath = System.getProperty("user.dir");*/
	
	//Driver Path Details
	public static String driverPathIE = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Internet Explorer Driver");	
	public static String driverPathChrome = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Chrome Driver");
	public static String driverPathFirefox = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Firefox Driver");
	
	// Amit R: GP Details
	public static String GP = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "GP Name");
	
	// Amit R: Performer for Opthalmic
		public static String OpthoPerformer = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "OpthoPerformer");
		public static String OPPerformerNumber = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "OPPerformerNumber");
	//	public static String PLContractor = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "PLContractor");
		
	// Suraj G: Performer List -Medical , Dental 
		/*public static String PerformerName_Medical = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Performer_Medical");
		public static String PerformerName_Dental = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Performer_Dental");
		public static String PerformerName_Opthalmic = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Performer_Opthalmic");
		public static String Process_Medical = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Process_Medical");
		public static String Process_Ophthalmic = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Process_Ophthalmic");
		public static String Process_Dental = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Process_Dental");*/
		public static String ProcessPL_Approve = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ProcessPL_Approve");
		
	// Akshay S: GPP codes for StandardClaim
		public static String StdClaim_GMCCode = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "StdClaimsGMCCode");
		public static String urlTestGPPPortal= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTGPPPortal");
		public static String urlTestGPPCRM= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRMGPP");
		public static String urlAutoGPPPortal= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOGPPPortal");
		public static String urlAutoCRMGPP= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOCRMGPP");
		public static String urlDevGPPPortal= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "AUTOGPPPortal");
		public static String urlDevCRMGPP= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVCRMGPP");
		public static String CQRSBizTalkPath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "CQRSBizTalkPath");
		public static String QOFAspirationFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "QOFAspirationFilePath");
		public static String QOFAchievementFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "QOFAchievementFilePath");
		public static String ProcessDelay= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ProcessDelay");
		public static String ProcessDelay_XML = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ProcessDelayXML");
		public static String BizTalkDomain= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "BizTalkDomain");
		public static String BiZTalkUserName= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "BiZTalkUserName");
		public static String BizTalkPassword= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "BizTalkPassword");
		public static String Asp_IterationFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Asp_IterationFilePath");
	
	
	public static String databaseHostNameautomationdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Automation DB");
	public static String databaseUserNameautomationdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Automation DB User Name");
	public static String databasePasswordautomationdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Automation DB Password");
	
	/*public static String databaseHostNamestagingdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Staging DB");
	public static String databaseUserNamestagingdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Staging DB User Name");
	public static String databasePasswordstagingdb = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "Staging DB Password");
	*/
	//Reading Webservice URL from Config file
	//public static String webserviceURL = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "WEBSERVICEURL");

	//CRM User Details	
	public static String crmUserID = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "CRM User ID");
	public static String crmPassword = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "CRM Password");
    public static String crmUser = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "CRM User Name");
	// PL activation link
  //CRM User Details	
  	public static String activationLink = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ActivationLink");
  	public static String localOffice = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "LOCALOFFICE");
  	public static String NewlocalOffice = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "NEWLOCALOFFICE");
  	
  //Domain Details	
  	/*public static String testDomain = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTDOMAIN");
	public static String autoDomain = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTDOMAIN");
	public static String devDomain = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "DEVDOMAIN");*/
	
	//CS URL details
	public static String urlTestCSCRM= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "TESTCRMCS");
	
	//CS CSV file path
	public static String ColposcopySampleFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ColposcopySampleFilePath");
	public static String ColposcopyUploadFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "ColposcopyUploadFilePath");
	public static String HPVSampleFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "HPVSampleFilePath");
	public static String HPVUploadFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "HPVUploadFilePath");
	
	// Adjustments file path
	public static String GPPAdjSampleFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "GPPAdjSampleFilePath");
	public static String GPPAdjUploadFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "GPPAdjUploadFilePath");
	public static String OPAdjSampleFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "OPAdjSampleFilePath");
	public static String OPAdjUploadFilePath= ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "OPAdjUploadFilePath");
	
	//Contractor for GPP
	public static String GPPContractor = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "GPPContractor");
	
	//GP pensions break in service parameter
	public static String BreakInServiceStartAfterDays = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "BreakInServiceStartAfterDays");
	public static String BreakInServiceEndAfterDays = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "BreakInServiceEndAfterDays");
	
	//Added by Akshay to fetch .dat file created dynamically for QOF process
	public static String NewAspSourcePath = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "NewAspSourcePath");
	public static String NewAchSourcePath = ExcelUtilities.getKeyValueFromExcel(configurationFileName, "Config", "NewAchSourcePath");
}
