package queuingmechanism;

import helpers.DatabaseHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;

import reporting.DumpReportToDB;



public class PollNBuildTestNGXML 
{

	@SuppressWarnings("resource")
	public void pollAndBuildTestNGXML() throws InterruptedException, SQLException
	{
		String xmlFileContent = null;
		List<String> xmlFiles = DatabaseHelper.CreateDataListForAListOfRows("SELECT * FROM automationdb.execution_details Where Exec_Status = 'Not Started' Order By Date", "EXECUTION_XML", "automationdb", "Local");
		List<String> executionID = DatabaseHelper.CreateDataListForAListOfRows("SELECT * FROM automationdb.execution_details Where Exec_Status = 'Not Started' Order By Date", "EXECUTION_ID", "automationdb", "Local");
		if(xmlFiles.size() != 0)
		{
			xmlFileContent = xmlFiles.get(0);
			try
			{
				String path = DumpReportToDB.class.getClassLoader().getResource("./").getPath();
				//path = path.replace("bin", "src");
				File txtFile = new File(path + "../../automationrunner/run/adhocrun.txt");
				Writer output = null;
				output = new BufferedWriter(new FileWriter(txtFile));
				File xmlFile = new File(path + "../../adhocrun.xml");
				output = new BufferedWriter(new FileWriter(xmlFile));
				output.write(xmlFileContent);
				output.close();
				DatabaseHelper.updateQuery("Update execution_details Set Exec_Status = 'In Progress' Where EXECUTION_ID = "+Integer.parseInt(executionID.get(0))+";", "automationdb", "Local");
				System.out.println("Executed");
			} 
			catch (Exception e) 
			{
				System.out.println(e);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, SQLException
	{
		new PollNBuildTestNGXML().pollAndBuildTestNGXML();

	}

}
