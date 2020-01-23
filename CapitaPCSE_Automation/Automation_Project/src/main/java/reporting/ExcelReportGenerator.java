package reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import helpers.CommonFunctions;

public class ExcelReportGenerator {

	public void generateExcelReport() throws ParserConfigurationException, SAXException, IOException
	{
	//	String path = ExcelReportGenerator.class.getClassLoader().getResource("./").getPath();
		//path = path.replace("bin", "src");
		String path = "D:\\PCSE_Automation\\PCSE_Automation_Maven\\PCSE\\Automation\\TestAutomation\\PCSE_Automation_Maven\\Automation_Project\\test-output";
	//	File xmlFile = new File(path + "../surefire-reports/testng-results.xml");
		File xmlFile = new File(path + "/testng-results.xml");
		//File xmlFile = new File("D:\\Automation\\Code\\PCSE_Automation_Maven\\Automation_Project\\test-output\\testng-results.xml");
		//XML Parsing
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder build = fact.newDocumentBuilder();
		Document doc = build.parse(xmlFile);
		doc.getDocumentElement().normalize();

		XSSFWorkbook book = new XSSFWorkbook();
		NodeList testNodeList = doc.getElementsByTagName("test");

		//Creating Sheet by Test name
		XSSFSheet sheet = book.createSheet("Results");

		//Reading Test Suite or Test name
		for(int i=0; i < testNodeList.getLength(); i++)
		{
			int r = 0;
			Node testNode = testNodeList.item(i);
			if(r == 0)
			{
				//Creating the Headers
				XSSFRow row = sheet.createRow(r++);
				//Creating Feature Column
				XSSFCell serialNoHeader = row.createCell(0);
				serialNoHeader.setCellValue("Serial No.");

				XSSFCell featureHeader = row.createCell(1);
				featureHeader.setCellValue("Feature");

				XSSFCell testCaseHeader = row.createCell(2);
				testCaseHeader.setCellValue("Test Case Name");

				XSSFCell statusHeader = row.createCell(3);
				statusHeader.setCellValue("Status");

				XSSFCell browserHeader = row.createCell(4);
				browserHeader.setCellValue("Browser");

				XSSFCell exceptionHeader = row.createCell(5);
				exceptionHeader.setCellValue("Test Outcome");

				/*XSSFCell passedCasesHeader = row.createCell(6);
				passedCasesHeader.setCellValue("Passed Test Steps");*/

				XSSFCell screenShotHeader = row.createCell(6);
				screenShotHeader.setCellValue("Screenshot Path");

				/*				XSSFCell exceptionMessageHeader = row.createCell(8);
				exceptionMessageHeader.setCellValue("Error Details");*/

			}			
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
						String methodStatus = ((Element)methodNode).getAttribute("status");
						//Creating Rows
						XSSFRow row = sheet.createRow(r++);
						//Creating Serial Number Column
						XSSFCell serialName = row.createCell(0);
						serialName.setCellValue(r-1);
						//Creating Feature Column
						XSSFCell featureName = row.createCell(1);
						featureName.setCellValue(className.substring(12));
						String methodName = ((Element)methodNode).getAttribute("name");
						//String methodStatus = ((Element)methodNode).getAttribute("status"); 
						//Creating Test Method Name
						XSSFCell testMethodName = row.createCell(2);
						testMethodName.setCellValue(methodName);
						//Creating Status Column
						XSSFCell testStatus = row.createCell(3);
						testStatus.setCellValue(methodStatus);

						//Creating Browser Column
						String testName = ((Element)testNode).getAttribute("name");
						XSSFCell browserName = row.createCell(4);
						browserName.setCellValue(testName.substring(8));

						XSSFCell testExceptionMessage;
						XSSFCell testStackMessage;
						XSSFCell screenshot;

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
								testExceptionMessage = row.createCell(5);
								testExceptionMessage.setCellValue(("Steps Passed ("+String.valueOf((length))+")\n"+passresults.toString().replace(",", "\n").replace("[", "").replace("]", "")).replace("              ", ""));
							}
							else
							{
								testExceptionMessage = row.createCell(5);
								testExceptionMessage.setCellValue("");
							}
						}
						else if("fail".equalsIgnoreCase(methodStatus))
						{
							//Creating Error Type Column
							NodeList exceptionNodeList = ((Element)methodNode).getElementsByTagName("exception");
							Node exceptionNode = exceptionNodeList.item(0);
							String exceptionMessage = ((Element)exceptionNode).getAttribute("class");
							testExceptionMessage = row.createCell(5);
							if(exceptionMessage.equals("java.lang.Throwable"))
								exceptionMessage = "There were some Failures";
							testExceptionMessage.setCellValue(exceptionMessage);

							//Creating Error Type Column


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

							String screenshotpath = passresults.get(length - 1);
							passresults.remove(length - 1);

							screenshot = row.createCell(6);
							screenshot.setCellValue(screenshotpath);


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
							testExceptionMessage = row.createCell(5);
							testExceptionMessage.setCellValue(("Steps Passed ("+String.valueOf((length-1))+")\n"+passresults.toString().replace(",", "\n").replace("[", "").replace("]", "") + "\n" + stackMessage).replace("              ", ""));
						}
					}
				}
			}
		}
		//FileOutputStream fout = new FileOutputStream(path + "../../Reports/Report.xlsx");
		String TS = CommonFunctions.generateDtTimeStamp();
		FileOutputStream fout = new FileOutputStream(path + "../../Reports/Report"+TS+".xlsx");
		book.write(fout);
		fout.close();
		System.out.println("Report Generated");
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		new ExcelReportGenerator().generateExcelReport();

	}

}
