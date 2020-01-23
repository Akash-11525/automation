package reporting;

import helpers.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

import org.testng.log4testng.Logger;

public class ChangeExecutionStatus 
{
	public void changeExecutionStatusOfInprogressExecution() throws InterruptedException, SQLException
	{
		List<String> executionID = DatabaseHelper.CreateDataListForAListOfRows("SELECT * FROM execution_details Where Exec_Status = 'In Progress' Order By Date", "EXECUTION_ID", "automationdb", "Local");
		DatabaseHelper.updateQuery("Update execution_details Set Exec_Status = 'Completed' Where EXECUTION_ID = "+Integer.parseInt(executionID.get(0))+";", "automationdb", "Local");
	}
	
	public static void main(String[] args) throws InterruptedException, SQLException
	{
				
		final Logger LOGGER = Logger.getLogger(ChangeExecutionStatus.class);
		LOGGER.info("START : ChangeExecutionStatus : main");
		new ChangeExecutionStatus().changeExecutionStatusOfInprogressExecution();
		LOGGER.info("STOP : ChangeExecutionStatus : main");


	}

}
