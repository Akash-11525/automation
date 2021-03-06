package testscripts;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Lists;

import com.mysql.fabric.xmlrpc.base.Array;

import browsersetup.BaseClass;
import helpers.CommonFunctions;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.SelectOrganisation;
import pageobjects.OP.*;
import reporting.ListenerClass;
import utilities.ExcelUtilities;
import verify.Verify;

@Listeners(ListenerClass.class)

public class OP_STATEMENTScripts extends BaseClass 
{
	/*	***********************************************************************************************************
	 * Amit Rasal : - 12683 - OPT_Statements_PRT_BR_11 -To validate creation of Statement in CRM
					  12684 - OPT_Statements_PRT_BR_12 -To validate creation of Statement in Portal

	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void OPT_CreateStatementCRM(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("OPT_Statements_PRT_BR_11","OPT_Statements_PRT_BR_12");
		String keyname = keys.get(0);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		String primaryEntity = "Payment Files";
		List<String> GroupTypeValueList = Arrays.asList("fld");
		List<String> FieldValueList = Arrays.asList("Payment File Status");
		List<String> ConditionValueList = Arrays.asList("Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup");
		List<String> ValueForFieldValueList = Arrays.asList("ISFE Validated");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				//ObjAdvancedFindResult= ObjAdvancedFindResult.clickOnPortalRole(keys.get(0)+"_Role Privilege");
				ObjAdvancedFindResult.takeScreenshot("Payment File",keys.get(0)+"_PaymentFile");
			}
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No Payment File record avaialble");

		}
		ObjCrmHome = ObjAdvancedFindResult .closeAdvanceFindWindow();
		ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		
		String primaryEntity1 = "Payment Statements";
		List<String> GroupTypeValueList1 = Arrays.asList("fld");
		List<String> FieldValueList1 = Arrays.asList("Payment Statement Name");
		List<String> ConditionValueList1 = Arrays.asList("Contains");
		List<String> ValueTypeList1 = Arrays.asList("text");
		List<String> ValueForFieldValueList1 = Arrays.asList("Contractual (England)");
		AdvancedFindResult ObjAdvancedFindResult1 = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity1, GroupTypeValueList1,FieldValueList1,ConditionValueList1, ValueTypeList1, ValueForFieldValueList1);
		
		boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
		if (flag1)
		{
			
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				
				ObjAdvancedFindResult.takeScreenshot("Payment Statement",keys.get(0)+"_PaymentStatement");
			}
		}
		else
		{

			
			System.out.println("No Payment Statement record avaialble");

		}
		ObjCrmHome = ObjAdvancedFindResult .closeAdvanceFindWindow();
		quit(browser);
		
		setup(browser, environment, clientName,"OPSTATEMENT");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home("PCSEPaymentsProcessor", environment);
		OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
		
		ObjOPStatement.enterFromdate(85);
		ObjOPStatement.clickOnSearchButton();
		if (evidence)
		{
		for (String key: keys)
		ObjOPStatement.OPStatementSnaps(key+"StatementSearchRecord");
		}
		OP_StatementDetails ObjOP_StatementDetails = ObjOPStatement.clickOnStatementRefLink(environment,"OPContractorCode","OpthoContractor");
		ObjOP_StatementDetails.clickOnAdjustmentLink();
		if(evidence)
		{
			for (String key:keys)
			ObjOP_StatementDetails.OPStatementDetailsSnaps(key+"StatementDetails");
		}
		
		
		
	}
	
	
	/*	***********************************************************************************************************
	 * Amit Rasal : - 17203 - TC_OPT_RBAC_77 -Validate if user with role "GOSClaimant" with privilege "View12MonthsStatement", then 
	 * user is allowed to search and view 12 months statements for their organisation.
	
	 	*******************************************************************************************************************/
	@Test(retryAnalyzer = helpers.RetryMechanism.class, groups = {"PCSE","OP","Regression"})
	@Parameters({ "browser","environment", "clientName", "evidence" })
	public void PRTSupervisor_View12MonthsStatement(String browser, String environment, String clientName, boolean evidence ) throws InterruptedException, IOException, AWTException, ParseException
	{
		//String key= "OPT_GOS5_PTR_BR_35";
		List<String> keys = Arrays.asList("TC_OPT_RBAC_74");
		String keyname = keys.get(0);
		
		setup(browser, environment, clientName,"CRMOP","SUPERUSER");	
		CrmHome ObjCrmHome  = new CrmHome(getDriver());
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		int colNum = 1;
	
		String primaryEntity = "Portal User Roles";
		List<String> GroupTypeValueList = Arrays.asList("fld","fld");
		List<String> FieldValueList = Arrays.asList("Portal Role","Portal User");
		List<String> ConditionValueList = Arrays.asList("Equals","Equals");
		List<String> ValueTypeList = Arrays.asList("Lookup", "Lookup");
		List<String> ValueForFieldValueList = Arrays.asList("GOSClaimant", "amitkumarr@capitapcsetest.com");
		AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult_multiple_Test(primaryEntity, GroupTypeValueList,FieldValueList,ConditionValueList, ValueTypeList, ValueForFieldValueList);

		boolean flag = ObjAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,1);
			if(title!= null)
			{
				ObjAdvancedFindResult= ObjAdvancedFindResult.clickOnPortalRole(keys.get(0)+"_Role Privilege");
			}
		}
		else
		{

			//Assert.assertEquals(flag, true, "No records found under results");
			System.out.println("No Portal User record avaialble");

		}
		quit(browser);
		
		setup(browser, environment, clientName,"OPSTATEMENT");
		LoginScreen ObjLoginScreen = new LoginScreen(getDriver());
		OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home("CETPRTSUPERVISOR", environment);
		OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
		
		ObjOPStatement.enterFromdate(365);
		ObjOPStatement.clickOnSearchButton();
		
		String msg = ObjOPStatement.getNotificationText(keyname+"_ErrorMessage");
		String expMsg = "You can only search for statements in 12 month periods.";
		
		if (msg.equalsIgnoreCase(expMsg))
		{
			System.out.println("The expected & actual pop up messages are matching");
		}
		
		else{
			System.out.println("The expected & actual pop up messages are not matching");
		}
		
		
		ObjOPStatement.enterFromdate(364);
		ObjOPStatement.clickOnSearchButton();
		
		
		ObjOPStatement.OPStatementSnaps(keyname+"_SearchRecord");
		
		
		
	}
	
	
	
	
	
	
	
}	
