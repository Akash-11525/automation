package testscripts;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import browsersetup.BaseClass;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.EmailDescription;
import pageobjects.LoginScreen;
import pageobjects.Registration;
import reporting.ListenerClass;
import utilities.ExcelUtilities;

@Listeners(ListenerClass.class)
public class CreateUserScripts extends BaseClass{
	
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","GPP","Regression","OP","PL","CS","Sanity"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void CreatePortalUsers(String browser, String environment, String clientName, boolean evidence) 
			throws InterruptedException, IOException, ParseException{
		
		String fileName= ExcelUtilities.getExcelParameterByModule("CreateUser","FileName");
		String userDetailsSheet= ExcelUtilities.getExcelParameterByModule("CreateUser","UserDetailsSheet");
		//String mappingDataSheet= ExcelUtilities.getExcelParameterByModule("CreateUser","MappingDataSheet");
		SoftAssert softAssert= new SoftAssert();
		List<String> roles= ExcelUtilities.GetAllKeyInArray(fileName, userDetailsSheet);
		
		for(int roleIndex=0;roleIndex<roles.size();roleIndex++){
			String role= roles.get(roleIndex);
			List<String> values= ExcelUtilities.getScriptParameters("CreateUser",role);
			System.out.println("Test Data value size is: "+values.size());
			
			List<String> portalValues= new ArrayList<String>();
			for(int i=0;i<=3;i++){
				String value= values.get(i);
				portalValues.add(value);
			}
			setup(browser, environment, clientName, "PL");
			LoginScreen objLoginScreen = new LoginScreen(getDriver());
			Registration objRegistration = objLoginScreen.clickOnRegistrationLink();
			List<String> UD = objRegistration.CompleteRegistration(environment,portalValues,role,fileName,userDetailsSheet);
			String fullName= UD.get(4);
			String user = UD.get(0);
			String userID= ExcelUtilities.getKeyValueByPosition(fileName, userDetailsSheet, role, "UserEmailAddress");
			System.out.println("User ID for role: "+role+" is: "+userID);
			setAssertMessage("The user "+user+" is successfully registerted.", 1);
			
			quit(browser);
			
			//Activate the registered account
			setup(browser, environment, clientName, "CRMOP", "SUPERUSER");
			CrmHome ObjCrmHome  = new CrmHome(getDriver());
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			int position= 1;
			String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "AdvancedFind", "selectPrimaryEntity",position);
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "AdvancedFind", "selectoptGroupType",position);
			String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "AdvancedFind", "selectField",position);
			String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(fileName, "AdvancedFind", "selectfilterCondition",position);
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue,user);
			
			boolean flag = ObjAdvancedFindResult.resultRecordFound();
			softAssert.assertTrue(flag, "Records are not found for user ID: "+user);
			if (flag)
			{
				System.out.println("The user record found");
				EmailDescription ObjEmailDescription = ObjAdvancedFindResult.clickOnLinkFromRecord(0,1);
				ObjEmailDescription.getEmailActivationLink();
				quit(browser);
				setup(browser, environment, clientName, "ActivationLink");
				ObjEmailDescription  = new EmailDescription(getDriver());
				ObjEmailDescription.browseEmailActivationLink();
				System.out.println("User "+user+" has been activated successfully");
				quit(browser);
			}
			List<String> crmFilter= new ArrayList<String>();
			List<String> GroupType= new ArrayList<String>();
			List<String> FieldName= new ArrayList<String>();
			List<String> Condition= new ArrayList<String>();
			List<String> ValueType= new ArrayList<String>();
			List<String> ValueForFieldValue= new ArrayList<String>();
			//Update records in Portal User entity
			try{
				if(!(values.get(4).isEmpty())){
					setup(browser, environment, clientName, "CRMOP", "SUPERUSER");
					ObjCrmHome  = new CrmHome(getDriver());
					ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					crmFilter= Arrays.asList(values.get(4).split(","));
					
					String entityName= crmFilter.get(0);
					String strFieldValue= crmFilter.get(1);
					String strFieldName= crmFilter.get(2);
					String strCondition= crmFilter.get(3);
					String userType= values.get(5);
					
					GroupType= Arrays.asList(strFieldValue);
					FieldName= Arrays.asList(strFieldName);
					Condition= Arrays.asList(strCondition);
					ValueType= Arrays.asList("Lookup");
					ValueForFieldValue= Arrays.asList(userID);
					ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
					ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupType, FieldName, Condition, ValueType, ValueForFieldValue);
					ObjAdvancedFilter.clickResults();
					AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
					flag = objAdvancedFindResult.resultRecordFound();
					if (flag)
					{
						String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
						if(title!= null)
						{
							boolean isUpdated= objAdvancedFindResult.changePortalUserType(userType);
							softAssert.assertTrue(isUpdated, "Portal user type is not changed to "+userType);
							System.out.println("User type for User ID "+userID+" is changed to "+userType);
							objAdvancedFindResult.closeAdvanceFindWindow();
						}
					}
					GroupType.remove(GroupType);
					FieldName.remove(FieldName);
					Condition.remove(Condition);
					ValueType.remove(ValueType);
					ValueForFieldValue.remove(ValueForFieldValue);
					crmFilter.remove(crmFilter);
				}
			}catch(Exception e){
				System.out.println("Values are not present in a cell at index: 4");
			}
			//Update records in Portal User Role entity
			try{
				if(!(values.get(6).isEmpty())){
					Thread.sleep(2000);
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					crmFilter= Arrays.asList(values.get(6).split(","));
					
					String entityName= crmFilter.get(0);
					String strFieldValue= crmFilter.get(1);
					String strFieldName= crmFilter.get(2);
					String strCondition= crmFilter.get(3);
					String portalRole= values.get(7);
					List<String> portalUserRoleValues= new ArrayList<String>();
					if(!(values.get(8).isEmpty())){
						String organisation= values.get(8);
						portalUserRoleValues= Arrays.asList(portalRole,organisation);
					}else{
						portalUserRoleValues= Arrays.asList(portalRole);
					}
					GroupType= Arrays.asList(strFieldValue);
					FieldName= Arrays.asList(strFieldName);
					Condition= Arrays.asList(strCondition);
					ValueType= Arrays.asList("Lookup");
					ValueForFieldValue= Arrays.asList(userID);
					ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
					ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupType, FieldName, Condition, ValueType, ValueForFieldValue);
					ObjAdvancedFilter.clickResults();
					AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
					flag = objAdvancedFindResult.resultRecordFound();
					if (flag)
					{
						String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
						if(title!= null)
						{
							boolean isUpdated= objAdvancedFindResult.updatePortalUserRolesValues(portalUserRoleValues);
							softAssert.assertTrue(isUpdated, "Portal user role values have not been changed");
							System.out.println("Portal user role values for User ID "+userID+" have been changed to "+portalUserRoleValues);
							objAdvancedFindResult.closeAdvanceFindWindow();
						}
					}
				}	
			}catch(Exception e){
				System.out.println("Values are not present in a cell at index: 6");
			}
			//Update records in Contacts entity
			try{
				if(!(values.get(9).isEmpty())){
					Thread.sleep(2000);
					ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
					crmFilter= Arrays.asList(values.get(9).split(","));
					
					String entityName= crmFilter.get(0);
					String strFieldValue= crmFilter.get(1);
					String strFieldName= crmFilter.get(2);
					String strCondition= crmFilter.get(3);
					String performerListType= values.get(10);
					String performerType= values.get(11);
					List<String> contactValues= Arrays.asList(performerListType,performerType);
					
					GroupType= Arrays.asList(strFieldValue);
					FieldName= Arrays.asList(strFieldName);
					Condition= Arrays.asList(strCondition);
					ValueType= Arrays.asList("Lookup");
					ValueForFieldValue= Arrays.asList(fullName);
					ObjAdvancedFilter.selectPrimaryEntityofCriteria(entityName);
					ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupType, FieldName, Condition, ValueType, ValueForFieldValue);
					ObjAdvancedFilter.clickResults();
					AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(getDriver());
					flag = objAdvancedFindResult.resultRecordFound();
					if (flag)
					{
						String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
						if(title!= null)
						{
							boolean isUpdated= objAdvancedFindResult.updateContactDetails(contactValues);
							softAssert.assertTrue(isUpdated, "Contact values have not been changed");
							System.out.println("Contact values for User "+user+" have been changed to "+contactValues);
							objAdvancedFindResult.closeAdvanceFindWindow();
						}
					}
				}
			}catch (Exception e) {
				System.out.println("Values are not present in a cell at index: 9");
			}
		
		}
		softAssert.assertAll();
	}
	
}
