package helpers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class GPPHelpers {
	public static String CQRSdestination = ConfigurationData.CQRSBizTalkPath;
	public static String aspSourcePath = System.getProperty("user.dir")+ConfigurationData.QOFAspirationFilePath;
	public static String achSourcePath = System.getProperty("user.dir")+ConfigurationData.QOFAchievementFilePath;
	public static String asp_IterationPath = System.getProperty("user.dir")+ConfigurationData.Asp_IterationFilePath;
	public static final String USERNAME=ConfigurationData.BiZTalkUserName;
	public static final String PASSWORD=ConfigurationData.BizTalkPassword;
	public static final String DOMAIN=ConfigurationData.BizTalkDomain;
	public static String newAspSourcePath = System.getProperty("user.dir")+ConfigurationData.NewAspSourcePath;
	public static String newAchSourcePath = System.getProperty("user.dir")+ConfigurationData.NewAchSourcePath;
	
	public static List<String> getCurrentFiscalMonths(String fileName, String sheet, String key ,int colNumber) {
		String []finYearArray=null;
		String strFinYear_Excel= utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(fileName,sheet,key, colNumber);
		 int splitStartYear=0;
		Pattern p = Pattern.compile("/");   // the pattern to search for
		Matcher m = p.matcher(strFinYear_Excel);
		
		 if (m.find()){
			finYearArray= strFinYear_Excel.split("/");
		    System.out.println("Found a match for single slash");
		 }else {
			 finYearArray= strFinYear_Excel.split("-");
			 System.out.println("Found a match for hyphen");
		    } 
		 
		 for(int i=0;i<finYearArray.length;i++){
			 String year= finYearArray[i].toString();
			 if(year.length()==4){
				 splitStartYear= Integer.parseInt(year);
				 break;
			 }
		 }
		 
		//String []finYearArray= strFinYear_Excel.split("/");
		//int splitStartYear= Integer.parseInt(finYearArray[0].toString().trim());
		List<String> fiscalMonths=null;
		boolean printAll=false;
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int monthIndex = cal.get(Calendar.MONTH); // 1
		int currentYear = cal.get(Calendar.YEAR); // 2018
		System.out.println("Current month index is: "+monthIndex);
		System.out.println("Current year is: "+currentYear);
		
		if(splitStartYear<currentYear){
			fiscalMonths=getFiscalYearMonths(monthIndex,printAll);
			}else{
				if(monthIndex<3){
					fiscalMonths=getFiscalYearMonths(monthIndex,!printAll);
				}else{
					fiscalMonths=getFiscalYearMonths(monthIndex,printAll);
				}
			}

		return fiscalMonths;
	}

	public static List<String> getFiscalYearMonths(int monthIndex, boolean printAll)
	{
		
		List<String> monthList= Arrays.asList("April","May","June","July","August","September","October","November","December","January","February","March");
		List<String> localMonthList = new ArrayList<String>();
		if(!printAll){
			if(monthIndex<3)
			{
				for (int i=monthIndex+9;i<=(monthList.size()-1);i++)
				{
					localMonthList.add(monthList.get(i));
				}
			}else{
		
				for (int i=(monthIndex-3);i<=(monthList.size()-1);i++) 
				{
					localMonthList.add(monthList.get(i));
				}
			}
		}else{
				for (String months : monthList) {
					localMonthList.add(months);
				}
		}

		return localMonthList;
	}
	
	public static void haltExecution() throws InterruptedException {
		String ProcessDelay = ConfigurationData.ProcessDelay;
			if((ProcessDelay != null && ProcessDelay.length() > 0))
			{
				int Delaytime = Integer.parseInt(ProcessDelay);
				if(Delaytime!=0)
				{
					System.out.println("Thread started at :"+CommonFunctions.generateDtTimeStamp());
					Thread.sleep(Delaytime*60*1000);
					System.out.println("Thread ended at :"+CommonFunctions.generateDtTimeStamp());
				}	
				else
				{
					Delaytime = 1;
					Thread.sleep(Delaytime*60*1000);
				}
			}
			else
			{

				Thread.sleep(1000);
			} 
				
	}

	
	public static List<String> getQOFInfoFromStagingTable(File []fileArray, String fileType, String environment) {
		List<String> finalData= new ArrayList<String>();
		String fileName= GPPHelpers.getQOFFileNameForQuery(fileArray);
		String payCode="";
		boolean interfaceFlag= false,cqrsFlag= false;
		
		switch (fileType) {
		case "Aspiration":
			List<String> CQRSPaymentData= new ArrayList<String>(), InterfaceDetails= new ArrayList<String>();
			payCode= ExcelUtilities.getKeyValueFromExcel("GPPQOFTESTDATA.xlsx", "AdvanceFindFilter", "AspPayCode");
			System.out.println("Paycode for file type: "+fileType + " is "+payCode);
			while(!interfaceFlag){
				InterfaceDetails= DatabaseHelper.getInterfaceRunDetails("StagingDb", "Staging",fileName, environment);
				if(InterfaceDetails!=null){
					finalData.addAll(InterfaceDetails);
					interfaceFlag= true;
				}
			}
			while(!cqrsFlag){
				CQRSPaymentData= DatabaseHelper.getGPPCQRSPaymentData("StagingDb", "Staging",payCode, environment);
				if(CQRSPaymentData!=null){
					finalData.addAll(CQRSPaymentData);
					cqrsFlag= true;
				}
			}
			int size= InterfaceDetails.size()+CQRSPaymentData.size();
			if(size==finalData.size()){
				for(int i=0;i<finalData.size();i++){
					ExcelUtilities.setKeyValueinExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", fileType, finalData.get(i), i+1);
				}
			}else{
				Assert.fail("Interface and CQRS data count is not matching for "+fileType);
			}
			break;
		case "Achievement":
			CQRSPaymentData= new ArrayList<String>(); List<String>InterfaceDetailsAch= new ArrayList<String>();
			payCode= ExcelUtilities.getKeyValueFromExcel("GPPQOFTESTDATA.xlsx", "AdvanceFindFilter", "AchPayCode");
			System.out.println("Paycode for file type: "+fileType + " is "+payCode);
			while(!interfaceFlag){
				InterfaceDetailsAch= DatabaseHelper.getInterfaceRunDetails("StagingDb", "Staging",fileName, environment);
				if(InterfaceDetailsAch!=null){
					finalData.addAll(InterfaceDetailsAch);
					interfaceFlag= true;
				}
			}
			while(!cqrsFlag){
				CQRSPaymentData= DatabaseHelper.getGPPCQRSPaymentData("StagingDb", "Staging",payCode, environment);
				if(CQRSPaymentData!=null){
					finalData.addAll(CQRSPaymentData);
					cqrsFlag= true;
				}
			}
			size= InterfaceDetailsAch.size()+CQRSPaymentData.size();
			if(size==finalData.size()){
				for(int i=0;i<finalData.size();i++){
					ExcelUtilities.setKeyValueinExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", fileType, finalData.get(i), i+1);
				}
			}else{
				Assert.fail("Interface and CQRS data count is not matching for "+fileType);
			}
			break;
		default:
			Assert.fail(fileType + " dosen't exists");
			break;
		}

		System.out.println("All the values are stored in excel file");
		return finalData;	
	}
	
	public static String getQOFFileNameForQuery(File[] fileArray) {
		String fileName="";
		for(int i=0;i<fileArray.length;i++){
			if(fileArray[i].isFile()&&fileArray[i].getName().endsWith(".dat")){
				fileName= fileArray[i].getName();
			}
		}
		return fileName;
	}

	public static void updateXMLAndDATFile(String feature,String fileSheet,String stagingSheet,String key,String environment) throws ParseException, IOException {
		
		switch (feature) {
		case "GPPQOF":
			String finStartYear= CommonFunctions.getFinYearStartDate("yyyy-MM-dd");
			String finEndYear= CommonFunctions.getFinYearLastDate("yyyy-MM-dd");
			String practiceCode= ConfigurationData.getRefDataDetails(environment, "GPContractorCode");
			String practiceName= ConfigurationData.getRefDataDetails(environment, "GPPContractor");
			
			List<String> values= Arrays.asList(finStartYear,finEndYear,practiceCode,practiceName);
			List<String> keys= Arrays.asList("StartDate","EndDate","PracticeIdentifier","Name");
			int keyCount= keys.size();
			
			for(int i=0;i<keyCount;i++){
				String attributeKey= keys.get(i);
				String value= values.get(i);
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", stagingSheet, attributeKey, value, 2);
			}
			
			String AspFileName= ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", fileSheet, "AspFile", "FileName");
			String AchFileName= ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", fileSheet, "AchFile", "FileName");
			
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", stagingSheet, 1);
			
			for(String AttributeName:AttributeNames){
				helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_new(AspFileName,stagingSheet, AttributeName,key,newAspSourcePath);
			}
			
			for(String AttributeName:AttributeNames){
				helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_new(AchFileName,stagingSheet, AttributeName,key,newAchSourcePath);
			}
			break;
		default:
			Assert.fail(feature+" is not found");
			break;
		}
		
	}
	
	public static double convertValueToDecimals(double value, int decimalCount) {
		String strDecimal = "0";
		for(int i=1;i<decimalCount;i++){
			String tempDecimal="0";
			strDecimal= strDecimal+tempDecimal;
		}
		DecimalFormat deciFormat = new DecimalFormat("##."+strDecimal);
		String strNewValue= deciFormat.format(value);
		double newValue= Double.parseDouble(strNewValue);
		System.out.println("Number is: "+newValue); 
		return newValue;
	}
	
	public static String getEmployeePenContriAmtFromTiers(String fileName, String amtTierSheet, String rangeColName, String criteriaColName,
			double pensionableIncome, double expMonthlyIncome) throws InterruptedException {
		
		double criteria = 0.00,startRange=0.00,endRange,contributionAmt=0.00;
		
		List<String> ranges= ExcelUtilities.getCellValuesByPosition(fileName, amtTierSheet, rangeColName);
		int rangeSize= ranges.size();
		List<String> critria= ExcelUtilities.getCellValuesByPosition(fileName, amtTierSheet, criteriaColName);
		
		for(int i=0;i<rangeSize;i++){
			String range= ranges.get(i);
			String[]rangeArray= range.split("-");
			String strStartRange= rangeArray[0].toString();
			startRange= Double.parseDouble(strStartRange);
			Thread.sleep(500);
			startRange= GPPHelpers.convertValueToDecimals(startRange,2);
			String strEndRange= rangeArray[1].toString();
			if(!(strEndRange.equalsIgnoreCase("over"))){
				endRange= Double.parseDouble(strEndRange);
				Thread.sleep(500);
				endRange= GPPHelpers.convertValueToDecimals(endRange,2);
				if((pensionableIncome>=startRange)&&(pensionableIncome<=endRange)){
					String strCriteria= critria.get(i);
					criteria= Double.parseDouble(strCriteria);
					Thread.sleep(500);
					criteria= GPPHelpers.convertValueToDecimals(criteria,2);
					break;
				}
			}else{
				Thread.sleep(500);
				if((pensionableIncome>=startRange)){
					String strCriteria= critria.get(i);
					criteria= Double.parseDouble(strCriteria);
					Thread.sleep(500);
					criteria= GPPHelpers.convertValueToDecimals(criteria,2);
					break;
				}
			}
		}
		contributionAmt= (expMonthlyIncome*criteria)/100;
		contributionAmt= GPPHelpers.convertValueToDecimals(contributionAmt,2);
		String strContriAmt= Double.toString(contributionAmt);
		strContriAmt= CommonFunctions.convertToCurrencyFormat(strContriAmt, "en","GB",false);//to be compared
		System.out.println("Expected contribution amount is: "+contributionAmt);
		return strContriAmt;
	}

}
