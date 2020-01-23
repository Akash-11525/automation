package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

//import com.mysql.jdbc.CallableStatement;
import java.sql.CallableStatement;

import testdata.ConfigurationData;
import utilities.ExcelUtilities;


public class DatabaseHelper 
{
	static String hostautomationdb = ConfigurationData.databaseHostNameautomationdb;
	static String userNameautomationdb = ConfigurationData.databaseUserNameautomationdb;
	static String passwordautomationdb = ConfigurationData.databasePasswordautomationdb;	

	//static String hoststgdb = ConfigurationData.databaseHostNamestagingdb;
//	String hoststgdb,userNamestgdb,passwordstgdb="";
//	static String userNamestgdb = ConfigurationData.databaseUserNamestagingdb;
//	static String passwordstgdb = ConfigurationData.databasePasswordstagingdb;

	/*
	 * Execute a DDL Query
	 */
	/*
	public static void executeDDLQuery(String query,String schemaName) throws InterruptedException, SQLException
	{
		Thread.sleep(2000);
		Statement stm = createDBConnection(schemaName);
		try
		{        
			stm.executeUpdate(query);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			stm.close();
		}
	}
	 */

	/*
	 * Updates a Table
	 */
	public static void updateQuery(String query,String schemaName,String environment) throws InterruptedException, SQLException
	{
		Thread.sleep(2000);
		Statement stm = createDBConnection(schemaName,environment);
		try
		{        
			stm.executeUpdate(query);
		}

		finally
		{
			stm.close();
		}
	}


	/*
	 * Returns a list of value for the database rows for the specified column (Multiple Rows & Single Column)
	 */
	public static List<String> CreateDataListForAListOfRows(String query,String tablecolumnname,String schemaName,String environment) throws InterruptedException, SQLException
	{
		List<String> dataList = new ArrayList<String>();
		String dataItem = null;
		Thread.sleep(2000);

		Statement stm = createDBConnection(schemaName,environment);

		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next()) 
			{
				dataItem = null;
				dataItem = rst.getString(tablecolumnname);
				dataItem=dataItem.replaceAll("[\r\n]+", " ");
				dataList.add(dataItem);
			}
		}

		finally
		{
			stm.close();
		}
		return dataList;
	}

	/*
	 * Returns a List of value for the database columns specified in the List (Single Row & Multiple Columns)
	 */
	public static List<String> CreateDataListForAListOfColumns(String query,List<String> tablecolumnnames,String schemaName, String dbenvironment, String environment) throws InterruptedException, SQLException
	{
		List<String> dataList = new ArrayList<String>();
		Thread.sleep(2000);       

		//Statement stm = createDBConnection(schemaName,environment);
		Connection con = createSTGDBConnection(schemaName,dbenvironment,environment);
		Statement stm = con.createStatement();
		
		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next()) 
			{
				for(String eachString : tablecolumnnames)
				{
					dataList.add(rst.getString(eachString));
				}
			}
		}

		finally
		{
			stm.close();
		}
		return dataList;
	}

	/*
	 * Returns a List of value for the database columns specified in the List (Multiple Rows & Multiple Columns)
	 */
	public static List<ArrayList<String>> createMulipleColumnDataListForMultipleRowsInDB(String query,List<String> tablecolumnnames,String schemaName, String environment) throws InterruptedException, SQLException
	{
		List<ArrayList<String>> dataListMultipleRow = new ArrayList<ArrayList<String>>();
		Thread.sleep(2000);       

		Statement stm = createDBConnection(schemaName,environment);

		//

		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next()) 
			{
				ArrayList<String> dataListSingleRow = new ArrayList<String>();
				for(String eachString : tablecolumnnames)
				{
					dataListSingleRow.add(rst.getString(eachString));
				}
				dataListMultipleRow.add(dataListSingleRow);
			}
		}

		finally
		{
			stm.close();
		}
		return dataListMultipleRow;
	}

	private static Statement createDBConnection(String schemaName,String environment) throws SQLException
	{
		Connection connection = null;
		if(environment.contains("Local"))
		{
			try
			{
				connection = DriverManager.getConnection(hostautomationdb,userNameautomationdb,passwordautomationdb);
			}
			catch(Exception e)
			{
				System.out.println("There was a problem while establish a connection : " + e);
			}
		}
		return connection.createStatement();
	}

	
	
	private static Connection createSTGDBConnection(String schemaName,String dbenvironment, String environment) throws SQLException
	{
		Connection connection = null;
		String hoststgdb = ConfigurationData.getRefDataDetails(environment, "Staging DB");
		String userNamestgdb = ConfigurationData.getRefDataDetails(environment, "Staging DB User Name");
		String passwordstgdb = ConfigurationData.getRefDataDetails(environment, "Staging DB Password");
		if(dbenvironment.contains("Staging"))
		{
			try
			{
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				connection = DriverManager.getConnection(hoststgdb,userNamestgdb,passwordstgdb);
				
			}
			catch(Exception e)
			{

				System.out.println("There was a problem while establish a connection : " + e);
			}
		}
		return connection;
	}

	public static Connection createCRMDBConnection(String dbenvironment, String environment) throws SQLException
	{
		Connection connection = null;
		String hoststgdb = ConfigurationData.getRefDataDetails(environment, "CRM DB");
		String userNamestgdb = ConfigurationData.getRefDataDetails(environment, "CRM DB User Name");
		String passwordstgdb = ConfigurationData.getRefDataDetails(environment, "CRM DB Password");
		if(dbenvironment.contains("CRM"))
		{
			try
			{
				//connection = DriverManager.getConnection(hoststgdb,userNamestgdb,passwordstgdb);

				//String url = "jdbc:sqlserver://172.16.251.31:1433;databaseName=StagingDb;Integratedsecurity=true;Domain=CAPITAPCSETEST";
				//String url = "jdbc:jtds:sqlserver://172.16.251.31:1433;databaseName=StagingDb;Integratedsecurity=true;Domain=CAPITAPCSETEST";
				//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				//connection = DriverManager.getConnection(url,userNamestgdb,passwordstgdb);
				connection = DriverManager.getConnection(hoststgdb,userNamestgdb,passwordstgdb);
				System.out.println("connection successful");
			}
			catch(Exception e)
			{
				System.out.println("There was a problem while establish a connection : " + e);
			}
		}
		return connection;


	}

	/*
	 * Returns a string value for the database column specified in query (Useful where user needs to get single value )
	 */
	public static String getValueFromQuery(String query,String columnName,String schemaName, String dbenvironment, String environment) throws InterruptedException, SQLException
	{

		String value = null;
		Thread.sleep(1000);       

		Connection con = createSTGDBConnection(schemaName,dbenvironment,environment);
		Statement stm = con.createStatement();
		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next())
			{
				value = rst.getString(columnName);
			}
		}

		finally
		{
			stm.close();
		}
		//System.out.println(value);
		return value;
	}
	
	public static String getCRMValueFromQuery(String query,String columnName, String dbenvironment, String environment) throws InterruptedException, SQLException
	{

		String value = null;
		Thread.sleep(1000);  
		int count = 0;
		
		/*List<String> QueryList = Arrays.asList(query.split(","));
		
		query = QueryList.get(0);*/
		

		Connection con = createCRMDBConnection(dbenvironment,environment);
		Statement stm = con.createStatement();
		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
		
			while (rst.next())
			{
				value = rst.getString(columnName);
				count++;
			}
			
		/*	if (count>1)
			{
				String query1 = QueryList.get(1);
				stm.close();
				con = createCRMDBConnection(dbenvironment,environment);
				stm = con.createStatement();
				ResultSet rstnew;
				rstnew = stm.executeQuery(query1);
				
				while (rstnew.next())
				{
					value = rstnew.getString(columnName);
				}
			}*/
		}

		finally
		{
			stm.close();
		}
		//System.out.println(value);
		return value;
	}

	public  static void callProcedure(String procName, String schemaName, String dbenvironment, String Claim, String environment) throws SQLException {
		Connection con = createSTGDBConnection(schemaName,dbenvironment,environment);
		CallableStatement cs = null;
		try {
			//cs = (CallableStatement) con.prepareCall("{procName}");

			String sql = "{call "+procName+" (?)}";
			cs = con.prepareCall(sql);
			// Input Parameter as Claim Number
			cs.setString(1, Claim);
			// Output Parameter as Integer
		//	cs.registerOutParameter(2, java.sql.Types.INTEGER);

			//Use execute method to run stored procedure.
			System.out.println("Executing stored procedure..." );
			cs.execute();

		//	int count=cs.getInt(2);
		//	System.out.println(count);
			System.out.println("Executed stored procedure..." );
			cs.close();
			con.close();
			System.out.println("Executed Store Procedure Successfully.");
		}

		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(cs!=null)
					cs.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(con!=null)
					con.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		




	}
	
	public static String BULKgetStatusFromClaim(String schemaName, String dbenvironment, String key, String environment, String MainFile) throws InterruptedException, SQLException
	{
	
		String Status = null,Claim=null;
		try{
		
		//String Claim = BULKgetClaimFromClaimTbl(schemaName, dbenvironment, key, environment,MainFile); //Commented by Akshay 
		
		//Akshay S: Added a loop to eliminate thread sleep
		boolean flag= false;
		while(!flag){
			Claim = BULKgetClaimFromClaimTbl(schemaName, dbenvironment, key, environment,MainFile);
			if(Claim==null){//!Claim.isEmpty()&&
				flag= false;
			}else{
				flag=true;
			}
		}
		System.out.println(Claim);
		Thread.sleep(2000);
		if (Claim!= null && !Claim.isEmpty())
		{
			String query = "select Status from ClaimStatus where ClaimId = '"+Claim+"'";
			System.out.println(query);
		
			//Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment); //Commented by Akshay 
			
			//Akshay S: Added a loop to eliminate thread sleep
			flag= false;
			while(!flag){
				Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
				System.out.println("Staging db status is: "+Status);
				if (Status!= null && !Status.isEmpty()){
					flag=true;
				}
			}
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	
	
	public static String getStatusFromClaim(String schemaName, String dbenvironment, String key, String environment) throws InterruptedException, SQLException
	{
	
		String Status = null;
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		String Claim = getClaimFromClaimTbl(schemaName, dbenvironment, key, environment);
		System.out.println(Claim);
		Thread.sleep(2000);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select Status from ClaimStatus where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	
	public static String getStatusFromClaim(String schemaName, String dbenvironment, String key, String environment, String Claim) throws InterruptedException, SQLException
	{
	
		String Status = null;
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
	//	String Claim = getClaimFromClaimTbl(schemaName, dbenvironment, key, environment);
		System.out.println(Claim);
		Thread.sleep(2000);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select Status from ClaimStatus where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	
	
	public static String getPVNStatusFromClaim(String schemaName, String dbenvironment, String key, String pvnReference, String environment) throws InterruptedException, SQLException
	{
	
		String Status = null;
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		String pvnID = getPVNIDFromClaimTbl(schemaName, dbenvironment, key, pvnReference, environment);
		System.out.println(pvnID);
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", pvnID, key, "CLAIM ID");
		Thread.sleep(2000);
		if (pvnID!= null && !pvnID.isEmpty())
		{
		String query = "select Status from PVNStatus where PVNId = '"+pvnID+"'";
		System.out.println(query);
	
		Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
		//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Status, key, "Expected CLAIM STATUS IN STG");
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	public static String getPVNStatusFromClaim(String MainFile, String schemaName, String dbenvironment, String key, String pvnReference, String environment) throws InterruptedException, SQLException
	{
	
		String Status = null,pvnID=null;
		boolean flag= false;
		try{
			
		//String pvnID = getPVNIDFromClaimTbl(schemaName, dbenvironment, key, pvnReference, environment);
		//Added by Akshay to eliminate thread sleep
		while(!flag){
			pvnID = getPVNIDFromClaimTbl(schemaName, dbenvironment, key, pvnReference, environment);
			System.out.println("PVN status from staging db is: "+pvnID);
			if(!pvnID.isEmpty()&&pvnID!=null){
				flag= true;
			}
		}
		System.out.println(pvnID);
		ExcelUtilities.setKeyValueByPosition(MainFile, "XML", pvnID, key, "CLAIM ID");
		Thread.sleep(2000);
		if (pvnID!= null && !pvnID.isEmpty())
		{
			String query = "select Status from PVNStatus where PVNId = '"+pvnID+"'";
			System.out.println(query);
		
			//Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
			//Added by Akshay to eliminate thread sleep
			flag= false;
			while(!flag){
				Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
				System.out.println("Staging db status is: "+Status);
				if(!Status.isEmpty()&&Status!=null){
					flag= true;
				}
			}
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	public static String getClaimFromClaimTbl(String schemaName, String dbenvironment, String key, String environment) throws InterruptedException, SQLException
	{
		String Claim = null;
		try {
		//String MSGID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key, 2);
			String MSGID = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "UNIQUE MSG ID");
		System.out.println("MSG ID: "+MSGID);
		String query = "select top 1 ClaimId from Claim where MessageId = '"+MSGID+"'";
		System.out.println(query);
	
		Claim = DatabaseHelper.getValueFromQuery(query, "ClaimId", schemaName, dbenvironment,environment);
		//ExcelUtilities.setKeyValueinExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", Claim, 3);
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Claim, key, "CLAIM ID");
		/*String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
		String newremark = remark+",No Claim has Generated";
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "REMARKS"); */
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
			
		return Claim;
	}
	
	public static String BULKgetClaimFromClaimTbl(String schemaName, String dbenvironment, String key, String environment, String MainFile) throws InterruptedException, SQLException
	{
		String Claim = null;
		try {
		//String MSGID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key, 2);
			String MSGID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println("MSG ID: "+MSGID);
		String query = "select ClaimId from Claim where MessageId = '"+MSGID+"'";
		System.out.println(query);
	
		Claim = DatabaseHelper.getValueFromQuery(query, "ClaimId", schemaName, dbenvironment,environment);
		//ExcelUtilities.setKeyValueinExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", Claim, 3);
		ExcelUtilities.setKeyValueByPosition(MainFile, "XML", Claim, key, "CLAIM ID");
		/*String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
		String newremark = remark+",No Claim has Generated";
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "REMARKS"); */
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
			
		return Claim;
	}
	
	public static String getPVNIDFromClaimTbl(String schemaName, String dbenvironment, String key, String pvnReference, String environment) throws InterruptedException, SQLException
	{
		String pvnid = null;
		try {
		//String MSGID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key, 2);
			//String MSGID = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "UNIQUE MSG ID");
		System.out.println("PVN REFERENCE: "+pvnReference);
		String query = "select PVNId from GOS6PVN where PVNReference = '"+pvnReference+"'";
		//System.out.println(pvnid);
	
		pvnid = DatabaseHelper.getValueFromQuery(query, "PVNId", schemaName, dbenvironment, environment);
		//ExcelUtilities.setKeyValueinExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", Claim, 3);
		//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Claim, key, "CLAIM ID");
		/*String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
		String newremark = remark+",No Claim has Generated";
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "REMARKS"); */
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
			
		return pvnid;
	}
	
	public static List<String> getErrorsFromClaim(String schemaName, String dbenvironment, String key, String environment) throws InterruptedException, SQLException
	{
		List<String> Errors = new ArrayList<String>();
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		//String Claim = getClaimFromClaimTbl(schemaName, environment,key);
		//String Claim = "CCAA3B43-9F0A-4B80-894A-A0083F9290B0";
		String Claim = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "CLAIM ID");
		System.out.println(Claim);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select ErrorCode from ClaimError where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Errors = DatabaseHelper.GetDataFromAColumn(query, "ErrorCode", schemaName, dbenvironment, environment);
		}
		else
		{
			System.out.println("No Claim has Generated");
			//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "NULL OR NOT GENERATED", key, "CLAIM ID");
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Errors;
	}
	
	public static List<String> getErrorsFromClaim_PMS(String schemaName, String dbenvironment, String key, String environment) throws InterruptedException, SQLException
	{
		List<String> Errors = new ArrayList<String>();
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		//String Claim = getClaimFromClaimTbl(schemaName, environment,key);
		//String Claim = "CCAA3B43-9F0A-4B80-894A-A0083F9290B0";
		String Claim = ExcelUtilities.getKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", key, "CLAIM ID");
		System.out.println(Claim);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select ValidationErrors from ClaimStatus where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Errors = DatabaseHelper.GetDataFromAColumn(query, "ValidationErrors", schemaName, dbenvironment, environment);
		}
		else
		{
			System.out.println("No Claim has Generated");
			//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "NULL OR NOT GENERATED", key, "CLAIM ID");
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Errors;
	}
	
	public static List<String> getErrorsFromClaim(String schemaName, String dbenvironment, String key, String Claim, String environment) throws InterruptedException, SQLException
	{
		List<String> Errors = new ArrayList<String>();
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		//String Claim = getClaimFromClaimTbl(schemaName, environment,key);
		//String Claim = "CCAA3B43-9F0A-4B80-894A-A0083F9290B0";
		//String Claim = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "CLAIM ID");
		System.out.println(Claim);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select ErrorCode from ClaimError where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Errors = DatabaseHelper.GetDataFromAColumn(query, "ErrorCode", schemaName, dbenvironment, environment);
		}
		else
		{
			System.out.println("No Claim has Generated");
			//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "NULL OR NOT GENERATED", key, "CLAIM ID");
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Errors;
	}
	
	public static List<String> getErrorsFrompvnStatus(String schemaName, String dbenvironment, String key, String environment) throws InterruptedException, SQLException
	{
		List<String> Errors = new ArrayList<String>();
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		//String Claim = getClaimFromClaimTbl(schemaName, environment,key);
		//String Claim = "CCAA3B43-9F0A-4B80-894A-A0083F9290B0";
		String Claim = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "CLAIM ID");
		System.out.println(Claim);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select ValidationErrors from pvnstatus where PVNId = '"+Claim+"'";
		System.out.println(query);
	
		Errors = DatabaseHelper.GetDataFromAColumn(query, "ValidationErrors", schemaName, dbenvironment, environment);
		}
		else
		{
			System.out.println("No pvn ID has Generated");
			//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "NULL OR NOT GENERATED", key, "CLAIM ID");
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Errors;
	}
	
	/*
	 * Returns a list of value for the database rows for the specified column (Multiple Rows & Single Column)
	 */
	public static List<String> GetDataFromAColumn(String query,String tablecolumnname,String schemaName,String dbenvironment, String environment) throws InterruptedException, SQLException
	{
		List<String> dataList = new ArrayList<String>();
		Thread.sleep(2000);

		Connection con = createSTGDBConnection(schemaName,dbenvironment, environment);
		Statement stm = con.createStatement();

		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next()) 
			{
				dataList.add(rst.getString(tablecolumnname));
			}
		}

		finally
		{
			stm.close();
		}
		return dataList;
	}

	//Added by Akshay for GPP scipting
	public static List<String> getInterfaceRunDetails(String schemaName, String dbenvironment, String fileName, String environment) {
		List<String> values = new ArrayList<String>();
		
		try{
					
			StringBuilder buildquery = new StringBuilder();
			buildquery.append("select  top 1 InterfaceRunID, [FileName],[Status],convert(varchar,DateTimeReceived ,103)as ");
			buildquery.append("FileDate from InterfaceRun");
			buildquery.append(" where Cast(DateTimeReceived as date)>=cast(getdate()as date)");
			buildquery.append(" and InterfaceIdentifier='CQRS Generic Payment' and [FileName]='"+fileName+"' order by DateTimeReceived desc").toString();
							//"and InterfaceIdentifier='CQRS Generic Payment' and [FileName]= '"+fileName+"'"
			String query= buildquery.toString();	
			System.out.println("Query is: "+query);
			
			List<String>tablecolumnnames= Arrays.asList("InterfaceRunID","FileName","Status","FileDate");
			values = DatabaseHelper.CreateDataListForAListOfColumns(query,tablecolumnnames, schemaName, dbenvironment, environment);

		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
	
		return values;
	}
	
	public static String getStatusAfterJobExec(String schemaName, String dbenvironment, String interfaceRunID, String jobName, String fileType, String environment) throws InterruptedException, SQLException
	{
	
		String Status = null;
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
/*		String Claim = getClaimFromClaimTbl(schemaName, environment, key);
		System.out.println(Claim);*/
		Thread.sleep(2000);

		
		String executeJobQuery= "exec msdb.dbo.sp_start_job '"+jobName+"'";
		boolean isJobExecuted= DatabaseHelper.executeSSISJob(executeJobQuery, schemaName, dbenvironment,interfaceRunID,environment);
		if(isJobExecuted){
			System.out.println("Job has been successfully executed. "+jobName);
		}

		String query = "select Status from InterfaceRun where InterfaceRunID = '"+interfaceRunID+"'";
		System.out.println(query);
		Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);

		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
			System.out.println("Error occured while executing SQL Job: "+jobName);
			System.out.println("System will wait for couple of minutes.");
			//se.printStackTrace();
			Thread.sleep(120000);
			String executeJobQuery= "exec msdb.dbo.sp_start_job '"+jobName+"'";
			boolean isJobExecuted= DatabaseHelper.executeSSISJob(executeJobQuery, schemaName, dbenvironment, environment);
			if(isJobExecuted)
			{
				System.out.println("Job has been successfully executed. " + jobName);
				isJobExecuted = true;
				String query = "select Status from InterfaceRun where InterfaceRunID = '"+interfaceRunID+"'";
				System.out.println(query);
				Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
			}
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();

		}

		return Status;
	}
	
	public static boolean executeSSISJob(String executeJobQuery,String schemaName,String dbenvironment,String interfaceRunID, String environment) throws InterruptedException, SQLException
	{
		boolean isStatusChanged=false;
	
		Thread.sleep(2000);       

		Connection con = createSTGDBConnection(schemaName,dbenvironment, environment);
		PreparedStatement  stm = con.prepareStatement(executeJobQuery);
		//createStatement();
		try
		{        
			stm.execute();
/*			ResultSet rst;
			rst = stm.executeQuery(executeJobQuery);
			executeQuery(executeJobQuery);*/
			
			String query = "select Status from InterfaceRun where InterfaceRunID = '"+interfaceRunID+"'";
			System.out.println(query);
			
			while(!isStatusChanged){
				int newStatus = Integer.parseInt(DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment));
				int expectedStatus=6;
				if(newStatus==expectedStatus){
					isStatusChanged=true;
					break;
				}
			}

		}

		finally
		{
			stm.close();
		}
		//System.out.println(value);
		return isStatusChanged;
	}
	
	public static List<String> getGPPCQRSPaymentData(String schemaName, String dbenvironment, String payCode, String environment) {
		List<String> values = new ArrayList<String>();
		
		try{
					
			StringBuilder buildquery = new StringBuilder();
			buildquery.append("select top 1 PracticeIdentifier, Name,GrossPaymentAmount, FinancialYearStartDate,PaymentPaycode ");
			buildquery.append(" from [CQRS].PaymentDetails");
			buildquery.append(" where PaymentPaycode='"+payCode+"' order by [TimeStamp] desc").toString();
							//"and InterfaceIdentifier='CQRS Generic Payment' and [FileName]= '"+fileName+"'"
			String paymentDetailsQuery= buildquery.toString();	
			System.out.println("Query is: "+paymentDetailsQuery);
			
			List<String>paymentTableColumnNames= Arrays.asList("PracticeIdentifier","Name","GrossPaymentAmount","FinancialYearStartDate","PaymentPaycode");
			values = DatabaseHelper.CreateDataListForAListOfColumns(paymentDetailsQuery,paymentTableColumnNames, schemaName, dbenvironment, environment);

		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
	
		return values;
	}

/*	//Added by Akshay for GPP Testing
	public static List<String> getresultListFromQuery(String query,String schemaName, String environment) throws InterruptedException, SQLException
	{

		List<String> values = new ArrayList<String>();
		Thread.sleep(2000);       

		Connection con = createSTGDBConnection(schemaName,environment);
		Statement stm = con.createStatement();
		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			int columnCount= rst.getMetaData().getColumnCount();
				for(int i=0;i<columnCount;i++){
					values.add(rst.getString(i+1));
				}
		}

		finally
		{
			stm.close();
		}
		 System.out.println("values from query are: "+values);
		return values;
	}*/
	
	public static boolean ExecuteJob(String schemaName, String dbenvironment,  String jobName, String environment) throws InterruptedException, SQLException
	{
	
		boolean ExceuteJobRun = false;
		try{
		Thread.sleep(2000);

		
		String executeJobQuery= "exec msdb.dbo.sp_start_job '"+jobName+"'";
		boolean isJobExecuted= DatabaseHelper.executeSSISJob(executeJobQuery, schemaName, dbenvironment, environment);
		if(isJobExecuted)
		{
			System.out.println("The Execute job is run sucessfully" + jobName);
			ExceuteJobRun = true;
		}


		}
		catch(SQLException se){
			//Handle errors for JDBC
			System.out.println("Error occured while executing SQL Job: "+jobName);
			System.out.println("System will wait for couple of minutes.");
			//se.printStackTrace();
			Thread.sleep(120000);
			String executeJobQuery= "exec msdb.dbo.sp_start_job '"+jobName+"'";
			boolean isJobExecuted= DatabaseHelper.executeSSISJob(executeJobQuery, schemaName, dbenvironment, environment);
			if(isJobExecuted)
			{
				System.out.println("The Execute job is run sucessfully: " + jobName);
				ExceuteJobRun = true;
			}
			
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();

		}

		return ExceuteJobRun;
	}
	
	public static boolean executeSSISJob(String executeJobQuery,String schemaName,String dbenvironment, String environment) throws InterruptedException, SQLException
	{
		boolean isStatusChanged=false;
	
		Thread.sleep(2000);       

		Connection con = createSTGDBConnection(schemaName,dbenvironment, environment);
		PreparedStatement  stm = con.prepareStatement(executeJobQuery);
		//createStatement();
		try
		{        
			stm.execute();
			isStatusChanged = true;
		}

		finally
		{
			stm.close();
		}
		//System.out.println(value);
		return isStatusChanged;
	}
	
	public static String getClaimFromClaimTbl(String schemaName, String dbenvironment, String key, String environment,String file) throws InterruptedException, SQLException
	{
		String Claim = null;
		try {
		//String MSGID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key, 2);
			String MSGID = ExcelUtilities.getKeyValueByPosition(file, "XML", key, "UNIQUE MSG ID");
		System.out.println("MSG ID: "+MSGID);
		String query = "select top 1 ClaimId from Claim where MessageId = '"+MSGID+"'";
		System.out.println(query);
	
		Claim = DatabaseHelper.getValueFromQuery(query, "ClaimId", schemaName, dbenvironment,environment);
		//ExcelUtilities.setKeyValueinExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", Claim, 3);
		ExcelUtilities.setKeyValueByPosition(file, "XML", Claim, key, "CLAIM ID");
		/*String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
		String newremark = remark+",No Claim has Generated";
		ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "REMARKS"); */
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
			
		return Claim;
	}
	
	public static String getStatusFromClaim_New(String schemaName, String dbenvironment, String key, String environment,String file) throws InterruptedException, SQLException
	{
	
		String Status = null;
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		String Claim = getClaimFromClaimTbl(schemaName, dbenvironment, key, environment,file);
		System.out.println(Claim);
		Thread.sleep(2000);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select Status from ClaimStatus where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Status = DatabaseHelper.getValueFromQuery(query, "Status", schemaName, dbenvironment, environment);
		}
		
		else
		{
			System.out.println("No Claim has Generated");
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "CLAIM ID");
			System.out.println("The Claims Id is found null");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			String newremark = remark+",The Claims Id is found null";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Status;
	}
	
	public static List<String> getErrorsFromClaim_PMS(String schemaName, String dbenvironment, String key, String environment,String MainFile) throws InterruptedException, SQLException
	{
		List<String> Errors = new ArrayList<String>();
		try{
			
		//String Claim = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", "TEST1", 3);
		//String Claim = getClaimFromClaimTbl(schemaName, environment,key);
		//String Claim = "CCAA3B43-9F0A-4B80-894A-A0083F9290B0";
		String Claim = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
		System.out.println(Claim);
		if (Claim!= null && !Claim.isEmpty())
		{
		String query = "select ValidationErrors from ClaimStatus where ClaimId = '"+Claim+"'";
		System.out.println(query);
	
		Errors = DatabaseHelper.GetDataFromAColumn(query, "ValidationErrors", schemaName, dbenvironment, environment);
		}
		else
		{
			System.out.println("No Claim has Generated");
			//ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "NULL OR NOT GENERATED", key, "CLAIM ID");
		}
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
					
			//end finally try
		}//end try
		
		
			
		return Errors;
	}
	
	public static List<String> CreateCRMDataListForAListOfColumns(String query,List<String> tablecolumnnames, String dbenvironment, String environment) throws InterruptedException, SQLException
	{
		List<String> dataList = new ArrayList<String>();
		Thread.sleep(2000);       

		//Statement stm = createDBConnection(schemaName,environment);
		Connection con = createCRMDBConnection(dbenvironment,environment);
		Statement stm = con.createStatement();
		
		try
		{        
			ResultSet rst;
			rst = stm.executeQuery(query);
			while (rst.next()) 
			{
				for(String eachString : tablecolumnnames)
				{
					dataList.add(rst.getString(eachString));
				}
			}
		}

		finally
		{
			stm.close();
		}
		return dataList;
	}


	public static String getValueByCastingDataFromColumn(String columnToCast, String castType, String tableName, String columnEquals, String value,
			String dbEnvironment, String environment) throws InterruptedException, SQLException {
		List<String>castValues= new ArrayList<String>();
		
		String query="",databaseValue="";
		String columnName= "cast("+columnToCast+" as "+castType+") as "+columnToCast;
		query = "select "+columnName+" from "+tableName+" where "+columnEquals+"='"+value+"'";
		System.out.println(query);
		if(dbEnvironment.contains("Staging")){
			databaseValue= DatabaseHelper.getValueFromQuery(query, columnToCast, "StagingDB", dbEnvironment, environment);
			castValues.add(databaseValue);
		}else if(dbEnvironment.contains("CRM")){
			databaseValue= DatabaseHelper.getCRMValueFromQuery(query, columnToCast, dbEnvironment, environment);
			castValues.add(databaseValue);
		}
		return databaseValue;
	}

}
