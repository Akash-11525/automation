package reporting;

import helpers.DatabaseHelper;

import helpers.DateParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.log4testng.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class DumpReportToDB 
{
	public void generateExcelReport() throws ParserConfigurationException, SAXException, IOException, InterruptedException, SQLException, ParseException
	{
		String path = DumpReportToDB.class.getClassLoader().getResource("./").getPath();
		//path = path.replace("bin", "src");

		File xmlFile = new File(path + "../surefire-reports/testng-results.xml");

		//XML Parsing
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder build = fact.newDocumentBuilder();
		Document doc = build.parse(xmlFile);
		doc.getDocumentElement().normalize();

		XSSFWorkbook book = new XSSFWorkbook();
		NodeList testNodeList = doc.getElementsByTagName("test");
		List<String> executionID = DatabaseHelper.CreateDataListForAListOfRows("SELECT * FROM automationdb.execution_details Where Exec_Status = 'Completed' Order By Date desc;", "EXECUTION_ID", "automationdb", "Local");
		int latestExecutionID = Integer.parseInt(executionID.get(0));
		System.out.println("Execution ID : " + latestExecutionID);
		
		for(int i=0; i < testNodeList.getLength(); i++)
		{
			Node testNode = testNodeList.item(i);		
			NodeList classNodeList = ((Element)testNode).getElementsByTagName("class");
			//Reading Class Name
			for(int j=0; j<classNodeList.getLength(); j++)
			{
				Node classNode = classNodeList.item(j);
				String className = ((Element)classNode).getAttribute("name");
				NodeList methodNodeList = ((Element)classNode).getElementsByTagName("test-method");
				//Reading Method Name
				for(int k=0; k<methodNodeList.getLength(); k++)
				{
					Node methodNode = methodNodeList.item(k);
					if(!((Element)methodNode).getAttribute("name").equalsIgnoreCase("setup") && !((Element)methodNode).getAttribute("name").equalsIgnoreCase("quit") && !((Element)methodNode).getAttribute("name").equalsIgnoreCase("removeAssertMessage") && !((Element)methodNode).getAttribute("name").equalsIgnoreCase("killIEBrowser"))
					{
						String startedAt = DateParser.convertSDFToDBDate(((Element)methodNode).getAttribute("started-at"));
						String finishedAt = DateParser.convertSDFToDBDate(((Element)methodNode).getAttribute("finished-at"));
						double scriptTime = Double.parseDouble(((Element)methodNode).getAttribute("duration-ms"))/1000;
						String methodStatus = ((Element)methodNode).getAttribute("status");
						System.out.println("Method Status : " + methodStatus);
						String methodName = ((Element)methodNode).getAttribute("name");
						System.out.println("Method Name : " + methodName);
						//Creating Browser Column
						String testName = ((Element)testNode).getAttribute("name");
						System.out.println(methodName);
						String testOutcome = null;
						String screenshotpath = "";
						if("pass".equalsIgnoreCase(methodStatus))
						{
							/*							testExceptionMessage = row.createCell(5);
							testExceptionMessage.setCellValue("");*/
							NodeList reporterNodeList = ((Element)methodNode).getElementsByTagName("reporter-output");
							//NodeList nl = docEle.getChildNodes();
							Node reporterNode = reporterNodeList.item(0);
							NodeList pathNodeList = ((Element)reporterNode).getElementsByTagName("line");
							List<String> passresults = new ArrayList<String>();
							int length = pathNodeList.getLength();
							if (pathNodeList != null) 
							{
								for (int c = 0; c < length; c++) 
								{
									if (pathNodeList.item(c).getNodeType() == Node.ELEMENT_NODE)
									{
										Node pathNode = pathNodeList.item(c);
										if(((Element)pathNode).getTextContent() != null)
											passresults.add(((Element)pathNode).getTextContent().replace("\n", ""));
										else
											passresults.add("Screenshot Not Found");
									}
								}
							}
							if(length != 0)
							{
								testOutcome = ("Steps Passed ("+String.valueOf((length))+")\n"+passresults.toString().replace(",", "\n").replace("[", "").replace("]", "")).replace("              ", "").replace("'", "''").replace("\\", "\\\\");
							}
							else
							{
								testOutcome = "";
							}
							System.out.println("Test Outcome : " + testOutcome);
						}
						else if("fail".equalsIgnoreCase(methodStatus))
						{
							//Creating Error Type Column
							NodeList exceptionNodeList = ((Element)methodNode).getElementsByTagName("exception");
							Node exceptionNode = exceptionNodeList.item(0);
							String exceptionMessage = ((Element)exceptionNode).getAttribute("class");
							if(exceptionMessage.equals("java.lang.Throwable"))
								exceptionMessage = "There were some Failures";

							//Creating Error Type Column
							NodeList reporterNodeList = ((Element)methodNode).getElementsByTagName("reporter-output");
							//NodeList nl = docEle.getChildNodes();
							Node reporterNode = reporterNodeList.item(0);
							List<String> failResults = new ArrayList<String>();
							NodeList pathNodeList = ((Element)reporterNode).getElementsByTagName("line");
							List<String> passresults = new ArrayList<String>();
							int length = pathNodeList.getLength();
							/*if (pathNodeList != null) 
							{
								for (int c = 0; c < length; c++) 
								{
									if (pathNodeList.item(c).getNodeType() == Node.ELEMENT_NODE) 
									{
										Node pathNode = pathNodeList.item(c);
										if(((Element)pathNode).getTextContent() != null)
											passresults.add(((Element)pathNode).getTextContent().replace("\n", ""));
										else
											passresults.add("Screenshot Not Found");
									}
								}
							}

							screenshotpath = passresults.get(length - 1).replace("              ", "").replace("\\", "\\\\");
							System.out.println("Screeshot Path : " + screenshotpath);
							passresults.remove(length - 1);*/
							
							// Changes made to code when Screen shot is not getting captured, still allow to dump the results into database.
							
							 if (pathNodeList != null && length != 0) 
                             {
                                             for (int c = 0; c < length; c++) 
                                             {
                                                             if (pathNodeList.item(c).getNodeType() == Node.ELEMENT_NODE) 
                                                             {
                                                                             Node pathNode = pathNodeList.item(c);
                                                                             if(((Element)pathNode).getTextContent() != null)
                                                                                             failResults.add(((Element)pathNode).getTextContent().replace("\n", ""));
                                                                             else
                                                                                             failResults.add("Screenshot Not Found");
                                                             }
                                             }
                             }
                             else
                                             failResults.add("Screenshot Not Found");

                             if(length != 0)
                             {
                                             screenshotpath = failResults.get(length - 1).replace("              ", "").replace("\\", "\\\\");
                                             System.out.println("Screeshot Path : " + screenshotpath);
                                             failResults.remove(length - 1);
                             }
                             else
                             {
                                             screenshotpath = failResults.get(length);
                                             System.out.println("Screeshot Path : " + screenshotpath);
                                             failResults.remove(length);
                             }


							
							

							/*NodeList reporterNodeList = ((Element)methodNode).getElementsByTagName("reporter-output");
							Node reporterNode = reporterNodeList.item(0);
							NodeList pathNodeList = ((Element)reporterNode).getElementsByTagName("line");
							Node pathNode = pathNodeList.item(0);
							String screenShotPath = ((Element)pathNode).getTextContent();
							try 
							{
								screenShotPath = ((Element)pathNode).getTextContent();
								testExceptionMessage = row.createCell(6);
								testExceptionMessage.setCellValue(screenShotPath.replace(" ", "").replace("\n", ""));
							} 
							catch (NullPointerException e1) 
							{
								// TODO Auto-generated catch block
								testExceptionMessage = row.createCell(6);
								testExceptionMessage.setCellValue("Screenshot Not Found");
								System.out.println("Screenshot Not Found for : " + ((Element)methodNode).getAttribute("name"));
								e1.printStackTrace();
							}*/

							//Creating Error Message Column
							String stackMessage = null;
							try
							{
								NodeList stackMessageNodeList = ((Element)exceptionNode).getElementsByTagName("message");
								Node stackMessageNode = stackMessageNodeList.item(0);
								stackMessage = ((Element)stackMessageNode).getTextContent();
								//testStackMessage = row.createCell(8);
								//testStackMessage.setCellValue(stackMessage.replace("java.lang.AssertionError:There were Some Failures as Shown above. expected [false] but found [true]", ""));
								//testStackMessage.setCellValue(stackMessage);
							}
							catch(NullPointerException e)
							{
								//testStackMessage = row.createCell(5);
								stackMessage = "NA";
								//testStackMessage.setCellValue(testStackMessage);
							}
							testOutcome = ("Steps Passed ("+String.valueOf((length-1))+")\n"+passresults.toString().replace(",", "\n").replace("[", "").replace("]", "") + "\n" + stackMessage).replace("              ", "").replace("'", "''").replace("\\", "\\\\");
							System.out.println("Test Outcome : " + testOutcome);
						}
						if(!"skip".equalsIgnoreCase(methodStatus))
							DatabaseHelper.updateQuery("Insert Into raw_results (Script_Name,Status,Test_Outcome,SCREEN_SHOT,Execution_ID,Start_Time,End_Time,Total_Script_Time) Values ('"+methodName+"', '"+methodStatus+"', '"+testOutcome+"', '"+screenshotpath+"',"+latestExecutionID+", '"+startedAt+"', '"+finishedAt+"',"+scriptTime+");", "automationdb", "Local");
					}
				}
			}
		}
		FileOutputStream fout = new FileOutputStream(path + "../../Reports/Report.xlsx");
		book.write(fout);
		fout.close();
		System.out.println("Report Generated");
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException, SQLException, ParseException 
	{
		final Logger LOGGER = Logger.getLogger(DumpReportToDB.class);
		LOGGER.info("START : DumpReportToDB : main");
		new DumpReportToDB().generateExcelReport();
		LOGGER.info("STOP : DumpReportToDB : main");

	}

}
