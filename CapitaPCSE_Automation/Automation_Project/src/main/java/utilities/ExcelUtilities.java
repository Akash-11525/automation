package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import net.sourceforge.htmlunit.cyberneko.HTMLScanner.CurrentEntity;


public class ExcelUtilities 
{
	static String projectPath = System.getProperty("user.dir");
	
	
	public static String getTestCaseName()
	{
		String TestCaseName = null;
		try{			
		
			List<String> tc=ExcelUtilities.getCellValuesInExcel("ReadWrite_TestData_PL.xlsx", "PLTestData", 1);
			for(int j=1;j<=tc.size();j++)
			{    	
				for(int i=1;i<5;i++)
				{
					TestCaseName=new Exception().getStackTrace()[i].getMethodName();
					if(TestCaseName.equals(tc.get(j)))
					{
						System.out.println("=======TestCase Name Is========"+tc.get(j));
						return TestCaseName;
					}
					/*else
					{
						System.out.println("===Actual TestCase Name Is==="+TestCaseName+"===Expected TestCase Name Is==="+tc.get(j));
					}*/
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			//return TestCaseName;
		}
		return TestCaseName;
		
	}

	public static String getExcelCellValue(String fileName,String sheetName,int rowNumber,int columnNumber) throws IOException
	{
		/*		String path = ExcelUtilities.class.getClassLoader().getResource("./").getPath();
		path = path.replace("bin", "src");
		//Create a object of File class to open xlsx file
		File file = new File(path + "../ExcelDataFiles/"+fileName);*/
		File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		String cellValue = null;
		Workbook excelWorkbook = null;
		//Find the file extension by spliting file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx"))
		{
			//If it is xlsx file then create object of XSSFWorkbook class
			excelWorkbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls"))
		{
			//If it is xls file then create object of XSSFWorkbook class
			excelWorkbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet excelSheet = excelWorkbook.getSheet(sheetName);
		//Find number of rows in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
		excelSheet.getRow(0).getCell(0);
		//Create a loop over all the rows of excel file to read it
		for (int i = 0; i < rowCount+ 1; i++)
		{
			if(i == rowNumber - 1)
			{
				Row row = excelSheet.getRow(i);
				//System.out.print(rowCount);
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum() + 1 ; j++)
				{
					if(j == columnNumber - 1)
					{
						//Print excel data in console
						try 
						{
							@SuppressWarnings("deprecation")
							int cel_Type = row.getCell(j).getCellType();                           
							switch(cel_Type) 
							{
							case 0: cellValue = String.valueOf((int)(row.getCell(j).getNumericCellValue()));
							break;
							case 1:	cellValue = row.getCell(j).getStringCellValue();
							break;
							case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							cellValue = sdf.format(row.getCell(j).getDateCellValue());
							break;
							case 4: cellValue = String.valueOf(row.getCell(j).getBooleanCellValue());
							break;
							default:
								cellValue = "";
							}
						} 
						catch(Exception e) 
						{
							e.printStackTrace();
							cellValue = "";
						}
						break;
					}
				}
				break;
			}
		}
		return cellValue;
	}

	public static String getKeyValueFromExcelWithPosition(String fileName,String sheetName, String keyName,int positionNo) 
	{
		// Call readExcelFile() method by passing it location of xls
		// This method will load keys and values from xls to HashMap
		return getKeyValue(fileName,sheetName,keyName,positionNo);
	}

	public static String getKeyValueFromExcel(String fileName,String sheetName, String keyName) 
	{
		// Call readExcelFile() method by passing it location of xls
		// This method will load keys and values from xls to HashMap
		return getKeyValue(fileName,sheetName,keyName,1);
	}

	private static String getKeyValue(String fileName,String sheetName, String keyName,int positionNo)     
	{
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String keyValue = null;
		/*		int environmentNo = 0;
		switch(environment.toUpperCase())
		{
		case "INTEGRATION" :
			environmentNo = 1;
			break;
		case "UAT" :
			environmentNo = 2;
			break;
		default :
			Assert.fail(environment + "dosen't exsists");
		}*/

		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);

			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			//Create a loop over all the rows of excel file to read it

			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				List<String> values = new ArrayList<String>();
				String key = null;
				String val = null;
				//Create a loop to print cell values in a row
				int collumnCounter = 1;
				for (int j = 0; j < row.getLastCellNum(); j++) 
				{
					//Print excel data in console
					if(collumnCounter==1)
					{
						try 
						{
							@SuppressWarnings("deprecation")
							int cel_Type = row.getCell(j).getCellType();                           
							switch(cel_Type) 
							{
							case 0: key = String.valueOf((int)(row.getCell(j).getNumericCellValue()));
							break;
							case 1:	key = row.getCell(j).getStringCellValue();
							break;
							case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							key = sdf.format(row.getCell(j).getDateCellValue());
							break;
							case 4: key = String.valueOf(row.getCell(j).getBooleanCellValue());
							break;
							default:
								key = "";
							}
						} 
						catch (Exception e) 
						{
							key = "";
						}
						collumnCounter++;
					}
					else
					{
						try 
						{
							@SuppressWarnings("deprecation")
							int cel_Type = row.getCell(j).getCellType();                           
							switch(cel_Type) 
							{
							case 0: val = String.valueOf((int)(row.getCell(j).getNumericCellValue()));
							break;
							case 1:	val = row.getCell(j).getStringCellValue();
							break;
							case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							val = sdf.format(row.getCell(j).getDateCellValue());
							break;
							case 4: val = String.valueOf(row.getCell(j).getBooleanCellValue());
							break;
							default:
								val = "";
							}
						} 
						catch (Exception e) 
						{
							val = "";
						}
						// extracting values from the cell2 
						values.add(val);
						// storing each properties into the HashMap
						collumnCounter++;
					}

				}
				map.put(key, values);
				
			}input.close();
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		for(Map.Entry<String, List<String>> entry : map.entrySet()) 
		{
			if(entry.getKey().equals(keyName))
			{
				keyValue = entry.getValue().get(positionNo - 1);
				break;
			}
		}
		
		return keyValue;                
	}

	/*	public static void openExcel(String fileName) throws InterruptedException
	{
		//Create a object of File class to open xlsx file
		//File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
        try
        {  
        	//Desktop.getDesktop().open(file);
        	Runtime.getRuntime().exec("cmd /c start "+projectPath + "\\ExcelDataFiles\\"+fileName);
        	Thread.sleep(4000);
        }
        catch(IOException  e)
        {
            e.printStackTrace();
        } 
	}

	public static void closeExcel() throws InterruptedException, IOException
	{
        try
        {   Runtime.getRuntime().exec("cmd /c taskkill /f /im excel.exe");
        	Thread.sleep(2000);  
        }
        catch(IOException  e)
        {
            e.printStackTrace();
        }
	}*/

	// Amit R. - Added Excel Function to set a value in the excel sheet.
	public static void setKeyValueinExcel(String fileName,String sheetName, String keyName, String value)     
	{

		try
		{                      

			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			//File file =    new File(projectPath + "\\ExcelOutFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);

			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			//Create a loop over all the rows of excel file to read it

			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				String key = null;
				// Counter k is created to read data from second cell
				int k=1;
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) 
				{
					try 
					{
						@SuppressWarnings("deprecation")
						int cel_Type = row.getCell(j).getCellType();                           
						switch(cel_Type) 
						{
						case 0: key = String.valueOf((int)(row.getCell(j).getNumericCellValue()));
						break;
						case 1:	key = row.getCell(j).getStringCellValue();
						break;
						case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						key = sdf.format(row.getCell(j).getDateCellValue());
						break;
						case 4: key = String.valueOf(row.getCell(j).getBooleanCellValue());
						break;
						default:
							key = "";
						}
							
					//	key =row.getCell(j).getStringCellValue();
						if (key.contains(keyName))
						{
							row.getCell(k).setCellValue(value);
							break;
						}


					}
					catch(Exception e) 
					{
						key = "";
						e.printStackTrace();
					}

				}

			}		

			//Writing output to excel sheet.
			FileOutputStream out =  new FileOutputStream(file);
			excelWorkbook.write(out);
			out.close();


		}

		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	// Amit R. - This method is introduced to get list of string after reading a column from Excel Sheet.

	public static List<String> getCellValuesInExcel(String fileName,String sheetName,int columnNumber) throws IOException
	{

		List<String> CellValues = new ArrayList<String>();
		File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook excelWorkbook = null;
		String CellValue=null;
		//Find the file extension by spliting file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx"))
		{
			//If it is xlsx file then create object of XSSFWorkbook class
			excelWorkbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls"))
		{
			//If it is xls file then create object of XSSFWorkbook class
			excelWorkbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet excelSheet = excelWorkbook.getSheet(sheetName);
		//Find number of rows in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
		//excelSheet.getRow(0).getCell(0);
		//Create a loop over all the rows of excel file to read it
		for (int i = 0; i < rowCount+ 1; i++)
		{
			/*  if(i == rowNumber - 1)
	{*/
			Row row = excelSheet.getRow(i);
			//System.out.print(rowCount);
			//Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum() + 1 ; j++)
			{
				if(j == columnNumber - 1)
				{
					//Print excel data in console
					try 
					{
						CellValues.add(row.getCell(j).getStringCellValue());
					} 
					catch (Exception e) 
					{
						CellValue="";
						//e.printStackTrace();
					}

				}
			}

		}
		//inputStream.close();
		System.out.println("CellValues:" +CellValues);
		return CellValues;
	}

	public static void setKeyValueinExcelWithPosition(String fileName,String sheetName, String keyName, String value, int position)     
	{

		try
		{                      

			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			//File file =    new File(projectPath + "\\ExcelOutFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);

			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			//Create a loop over all the rows of excel file to read it

			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				String key = null;
				// Counter k is created to read data from second cell
				int k=position;
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) 
				{

					try 
					{
						DataFormatter formatter = new DataFormatter();
						key = formatter.formatCellValue(row.getCell(0));
						if (key.contains(keyName))
						{
							row.getCell(k).setCellValue(value);
							break;
						}


					}

					catch (Exception e) 
					{
						key = "";
						e.printStackTrace();
					}

				}

			}		

			//Writing output to excel sheet.
			FileOutputStream out =  new FileOutputStream(file);
			excelWorkbook.write(out);			
			out.close();


		}

		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

	/* Amit R: Below method was introduced to optimize the "getKeyValueFromExcelWithPosition" which loops twice in excel.
	 * With below implementation, user can get cell value based on test id/field Name (key) and column name (columnName).
	 * It gives flexibility to select a column based on 
                                                                                                            */
	public static String getKeyValueByPosition(String fileName,String sheetName, String keyName,String columnName)     
	{
		
		String Value=null;
		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			//Create a loop over all the rows of excel file to read it
			int rowNumber = 0;
			int columnNumber = 0;
			
			for (int i = 0; i < rowCount+1; i++) 
			{
				Row row = excelSheet.getRow(i);
				String key = null;
				//String val = null;
				int firstCell = 0;
				@SuppressWarnings("deprecation")
				int cel_Type = row.getCell(firstCell).getCellType();
				switch(cel_Type) 
				{
				case 0: key = String.valueOf((int)(row.getCell(firstCell).getNumericCellValue()));
				break;
				case 1:	key = row.getCell(firstCell).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				key = sdf.format(row.getCell(firstCell).getDateCellValue());
				break;
				case 4: key = String.valueOf(row.getCell(firstCell).getBooleanCellValue());
				break;
				default:
					key = "";
				}

				if (key.equalsIgnoreCase(keyName))
				{
					rowNumber = i;
					break;
				}
				//rowNumber++;
			}
			// This will set row for respective key value 
			Row KeyRow = excelSheet.getRow(rowNumber);

			for (int a = 0; a<columnCount;a++)
			{
				String val = null;
				@SuppressWarnings("deprecation")
				int cel_Type = firstRow.getCell(a).getCellType();                           
				switch(cel_Type) 
				{
				case 0: val = String.valueOf((double)(firstRow.getCell(a).getNumericCellValue()));
				break;
				case 1:	val = firstRow.getCell(a).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				val = sdf.format(firstRow.getCell(a).getDateCellValue());
				break;
				case 4: val = String.valueOf(firstRow.getCell(a).getBooleanCellValue());
				break;
				default:
					val = "";
				}
				
				if(val.equalsIgnoreCase(columnName))
				{
					columnNumber = a;
					break;
				}
			}
			
			
		//Value = KeyRow.getCell(columnNumber).getStringCellValue();
			
			@SuppressWarnings("deprecation")
			int cel_Type = KeyRow.getCell(columnNumber).getCellType();                           
			switch(cel_Type) 
			{
			case 0: Value = String.valueOf((int)(KeyRow.getCell(columnNumber).getNumericCellValue()));
			break;
			case 1:	Value = KeyRow.getCell(columnNumber).getStringCellValue();
			break;
			case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Value = sdf.format(KeyRow.getCell(columnNumber).getDateCellValue());
			break;
			case 3:	Value = KeyRow.getCell(columnNumber).getStringCellValue();
			break;
			case 4: Value = String.valueOf(KeyRow.getCell(columnNumber).getBooleanCellValue());
			break;
			default:
				Value = "";
			}
		}
		
		catch(NullPointerException ex)
		{
			Value = "";
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			Value = "";
		}
		
		return Value;

	}
	
	public static void setKeyValueByPosition(String fileName,String sheetName, String value, String keyName,String columnName)     
	{
		
		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			int columnNumber = 0;
			
		//	Row KeyRow = excelSheet.getRow(rowNumber);

			for (int a = 0; a<columnCount;a++)
			{
				String val = null;
				@SuppressWarnings("deprecation")
				int cel_Type = firstRow.getCell(a).getCellType();                           
				switch(cel_Type) 
				{
				case 0: val = String.valueOf((int)(firstRow.getCell(a).getNumericCellValue()));
				break;
				case 1:	val = firstRow.getCell(a).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				val = sdf.format(firstRow.getCell(a).getDateCellValue());
				break;
				case 4: val = String.valueOf(firstRow.getCell(a).getBooleanCellValue());
				break;
				default:
					val = "";
				}
				
				if(val.equalsIgnoreCase(columnName))
				{
					columnNumber = a;
					break;
				}
			}
			
			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				String key = null;
				// Counter k is created to read data from second cell
				int k=columnNumber;
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) 
				{

					try 
					{
						DataFormatter formatter = new DataFormatter();
						key = formatter.formatCellValue(row.getCell(0));
						if (key.contains(keyName))
						{
							row.getCell(k).setCellValue(value);
							break;
						}


					}

					catch (Exception e) 
					{
						key = "";
						e.printStackTrace();
					}

				}

			}		

			//Writing output to excel sheet.
			FileOutputStream out =  new FileOutputStream(file);
			excelWorkbook.write(out);
			out.close();


		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static synchronized void setValueByKeyInColumn(String fileName,String sheetName, String value, String keyName,String columnName)     
	{
		/*Thread dr =Thread.currentThread();
		String name =dr.getName();
		boolean alive =dr.isAlive();
		//String name =dr.resume();
	int count =Thread.activeCount();*/
	/*	if(alive)
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	
	//	System.out.println(name+" Name of Thread    "+alive+" Alive of Thread   "+count+" Active Count ");
		
		
		
		//Lock lock=new ReentrantLock();
		try
		{    
			//lock.lock();
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();
		//	System.out.println(columnCount+ "columnCount");
			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
	//		System.out.println(rowCount+"rowCount");
			int columnNumber = 0;
			
		//	Row KeyRow = excelSheet.getRow(rowNumber);

			for (int a = 0; a<columnCount;a++)
			{
				String val = null;
				
				val = firstRow.getCell(a).getStringCellValue();
			
				if(val.equalsIgnoreCase(columnName))
				{					
					columnNumber = a;
					break;
				}
				
			}
			
			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				String key = null;
				// Counter k is created to read data from second cell
				int k=columnNumber;
				//Create a loop to print cell values in a row									
					try 
					{
						DataFormatter formatter = new DataFormatter();
						key = formatter.formatCellValue(row.getCell(0));
						
						if (key.equalsIgnoreCase(keyName))
						{	
							   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
							   LocalDateTime now = LocalDateTime.now();  
							   System.out.println(value+"=="+dtf.format(now));
							   Thread.sleep(k);
							   row.getCell(k).setCellValue(value);
							   break;
						}


					}

					catch (Exception e) 
					{
						key = "";
						e.printStackTrace();
					}

			}		
		//	lock.unlock();
			//Writing output to excel sheet.
			FileOutputStream out =  new FileOutputStream(file);
			excelWorkbook.write(out);
			out.close();
			excelWorkbook.close();
			input.close();


		}

		catch (Exception e)
		{//lock.unlock();
			e.printStackTrace();
		}

	
	}
	
	
	public static List<String> GetAllKeyInArray(String fileName,String sheetName)     
	{
		List<String> currentOptions = new ArrayList<>();
		try{
		
		String projectPath = System.getProperty("user.dir");
	
		File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		String cellValue = null;
		Workbook excelWorkbook = null;
		//Find the file extension by spliting file name in substring and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx"))
		{
			//If it is xlsx file then create object of XSSFWorkbook class
			excelWorkbook = new XSSFWorkbook(inputStream);
		}
		else if(fileExtensionName.equals(".xlsm"))
		{
			//If it is xlsx file then create object of XSSFWorkbook class
			excelWorkbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls"))
		{
			//If it is xls file then create object of XSSFWorkbook class
			excelWorkbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet excelSheet = excelWorkbook.getSheet(sheetName);
		//Find number of rows in excel file
		int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
		excelSheet.getRow(0).getCell(0);
		//Create a loop over all the rows of excel file to read it
		for (int i = 1; i <= rowCount; i++)
		{
			
				Row row = excelSheet.getRow(i);
				int j = 0;
						//Print excel data in console
						try 
						{
							@SuppressWarnings("deprecation")
							int cel_Type = row.getCell(j).getCellType();                           
							switch(cel_Type) 
							{
							case 0: cellValue = String.valueOf((int)(row.getCell(j).getNumericCellValue()));
							currentOptions.add(cellValue);
							break;
							case 1:	cellValue = row.getCell(j).getStringCellValue();
							currentOptions.add(cellValue);
							break;
							case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							cellValue = sdf.format(row.getCell(j).getDateCellValue());
							currentOptions.add(cellValue);
							break;
							case 4: cellValue = String.valueOf(row.getCell(j).getBooleanCellValue());
							currentOptions.add(cellValue);
							break;
							default:
								cellValue = "";
								break;
							}
						} 
						catch (Exception e) 
						{
							cellValue = "";
							break;
						}
						
					}
			//	}
			
		//	}
		
		System.out.println("The Total Keys"+currentOptions.size());
		System.out.println(currentOptions);
		
	
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
		return currentOptions;
	}
	

	public static Double getNumValueByPosition(String fileName,String sheetName, String keyName,String columnName)     
	{
		
		Double Value=null;
		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			//Create a loop over all the rows of excel file to read it
			int rowNumber = 0;
			int columnNumber = 0;
			
			for (int i = 0; i < rowCount+1; i++) 
			{
				Row row = excelSheet.getRow(i);
				String key = null;
				//String val = null;
				int firstCell = 0;
				@SuppressWarnings("deprecation")
				int cel_Type = row.getCell(firstCell).getCellType();
				switch(cel_Type) 
				{
				case 0: key = String.valueOf((int)(row.getCell(firstCell).getNumericCellValue()));
				break;
				case 1:	key = row.getCell(firstCell).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				key = sdf.format(row.getCell(firstCell).getDateCellValue());
				break;
				case 4: key = String.valueOf(row.getCell(firstCell).getBooleanCellValue());
				break;
				default:
					key = "";
				}

				if (key.equalsIgnoreCase(keyName))
				{
					rowNumber = i;
					break;
				}
				//rowNumber++;
			}
			// This will set row for respective key value 
			Row KeyRow = excelSheet.getRow(rowNumber);

			for (int a = 0; a<columnCount;a++)
			{
				String val = null;
				@SuppressWarnings("deprecation")
				int cel_Type = firstRow.getCell(a).getCellType();                           
				switch(cel_Type) 
				{
				case 0: val = String.valueOf((int)(firstRow.getCell(a).getNumericCellValue()));
				break;
				case 1:	val = firstRow.getCell(a).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				val = sdf.format(firstRow.getCell(a).getDateCellValue());
				break;
				case 4: val = String.valueOf(firstRow.getCell(a).getBooleanCellValue());
				break;
				default:
					val = "";
				}
				
				if(val.equalsIgnoreCase(columnName))
				{
					columnNumber = a;
					break;
				}
			}
			
			
			Value = KeyRow.getCell(columnNumber).getNumericCellValue();
			System.out.println(Value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return Value;

	}

	public static int getColumnCountFromExcel(String fileName, String sheetName) {
		int colNum=0;
		try{
			String projectPath = System.getProperty("user.dir");
			
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			//Create an object of FileInputStream class to read excel file
			FileInputStream inputStream = new FileInputStream(file);
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(inputStream);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(inputStream);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(inputStream);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);
			//Find number of columns in excel file
			Row row = excelSheet.getRow(0);
			colNum = row.getLastCellNum();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return colNum;
	}

	
	public static void setValuesInExcel(String fileName,String sheetName, List<String> values, List<String> keyName)     
	{
		
		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();
			int lastRowNum = excelSheet.getLastRowNum();
			Row NextRow = excelSheet.getRow(lastRowNum+1);
			if(NextRow == null){
				NextRow = excelSheet.createRow(lastRowNum+1);
				//NextRow.createCell(a);
				}

			excelSheet.getLastRowNum();
			excelSheet.getFirstRowNum();
			String key = null;
			
		//	Row KeyRow = excelSheet.getRow(rowNumber);
			
			for (int a=0; a<columnCount; a++)
			{
				key =firstRow.getCell(a).getStringCellValue();
				if (key.contains(keyName.get(a)))
				{
					NextRow.createCell(a, CellType.STRING);		
					//NextRow.getCell(a).set
					String value = values.get(a);
					NextRow.getCell(a).setCellValue(value);
					//break;
				}
			}

			/*for (int a = 0; a<columnCount;a++)
			{
				@SuppressWarnings("deprecation")
				String val = null;
				int cel_Type = firstRow.getCell(a).getCellType();                           
				switch(cel_Type) 
				{
				case 0: val = String.valueOf((int)(firstRow.getCell(a).getNumericCellValue()));
				break;
				case 1:	val = firstRow.getCell(a).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				val = sdf.format(firstRow.getCell(a).getDateCellValue());
				break;
				case 4: val = String.valueOf(firstRow.getCell(a).getBooleanCellValue());
				break;
				default:
					val = "";
				}
				
				if(val.equalsIgnoreCase(columnName))
				{
					columnNumber = a;
					break;
				}
			}
			
			for (int i = 0; i < rowCount+1; i++) 
			{

				Row row = excelSheet.getRow(i);
				String key = null;
				// Counter k is created to read data from second cell
				int k=columnNumber;
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) 
				{

					try 
					{
						key =row.getCell(j).getStringCellValue();
						if (key.contains(keyName))
						{
							row.getCell(k).setCellValue(value);
							break;
						}


					}

					catch (Exception e) 
					{
						key = "";
						e.printStackTrace();
					}

				}*/

			//}		

			//Writing output to excel sheet.
			FileOutputStream out =  new FileOutputStream(file);
			excelWorkbook.write(out);
			out.close();


		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static List<String> getCellValuesByPosition(String fileName,String sheetName,String columnName)     
	{
		List<String> CellValues= new ArrayList<String>();
		try
		{                      
			File file =    new File(projectPath + "\\ExcelDataFiles\\"+fileName);
			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(file);
			
			Workbook excelWorkbook = null;
			//Find the file extension by spliting file name in substring and getting only extension name
			String fileExtensionName = fileName.substring(fileName.indexOf("."));
			//Check condition if the file is xlsx file
			if(fileExtensionName.equals(".xlsx"))
			{
				//If it is xlsx file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Check condition if the file is xls file
			else if(fileExtensionName.equals(".xls"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new HSSFWorkbook(input);
			}
			else if(fileExtensionName.equals(".xlsm"))
			{
				//If it is xls file then create object of XSSFWorkbook class
				excelWorkbook = new XSSFWorkbook(input);
			}
			//Read sheet inside the workbook by its name
			Sheet excelSheet = excelWorkbook.getSheet(sheetName);

			// Get column count from sheet
			Row firstRow = excelSheet.getRow(0);
			int columnCount = firstRow.getLastCellNum();

			//Find number of rows in excel file
			int rowCount = excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
			int columnNumber = 0;

			// This will set row for respective key value 

			for (int a = 0; a<columnCount;a++)
			{
				String val = null;
				@SuppressWarnings("deprecation")
				int cel_Type = firstRow.getCell(a).getCellType();                           
				switch(cel_Type) 
				{
				case 0: val = String.valueOf((double)(firstRow.getCell(a).getNumericCellValue()));
				break;
				case 1:	val = firstRow.getCell(a).getStringCellValue();
				break;
				case 2: SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				val = sdf.format(firstRow.getCell(a).getDateCellValue());
				break;
				case 4: val = String.valueOf(firstRow.getCell(a).getBooleanCellValue());
				break;
				default:
					val = "";
				}
				
				if(val.equalsIgnoreCase(columnName))
				{
					columnNumber = a;
					break;
				}
			}
			
			
			//Create a loop over all the rows of excel file to read it
			for (int i = 1; i <= rowCount; i++)
			{
				Row row = excelSheet.getRow(i);
				//Create a loop to print cell values in a row
				for (int j = 0; j < row.getPhysicalNumberOfCells() ; j++)
				{
					if(j == columnNumber)
					{
						//Print excel data in console
						try 
						{
							String value= row.getCell(j).getStringCellValue();
							if(!value.isEmpty()){
								CellValues.add(row.getCell(j).getStringCellValue());
							}
						} 
						catch (Exception e)
						{
							System.out.println("Record is not found for row index: "+i+" and column index: "+j);
						}
					}
				}
			}
			System.out.println("CellValues:" +CellValues);
		}
		
		catch(NullPointerException ex)
		{
			CellValues.add("");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			CellValues.add("");
		}
		return CellValues;
	}

	//Before executing this method, make sure that there will be data under ScriptParameter and KeyMapping sheet
	public static List<String> getScriptParameters(String moduleType, String key) {
		List<String> values= new ArrayList<String>();
		String testDataFileName = null, paramSheetName=null, mappingDataSheet = null, value=null;
		switch(moduleType){
		case"GOS1":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "NewTestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "OPGOS1KeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case"GOS3":
			break;
		case"GOS4":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "NewTestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "OPGOS4KeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case"GOS5":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "OPGOS5KeyDataMapping", "NewTestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "OPGOS5KeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case"GOS6":
			break;
		case "GPSolo":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("GPPSoloTestData.xlsx", "GPSoloKeyDataMapping", "TestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "GPSoloKeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case "GPAVC":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("GPPVolContriTestData.xlsx", "GPAVCKeyDataMapping", "TestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "GPAVCKeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case "CreateUser":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("PortalUserDetails.xlsx", "KeyDataMapping", "FileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "KeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "UserDetailsSheet", "Data");
			break;
		case "GPSalChange":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("GPSalaryChange.xlsx", "GPSalaryChangeKeyDataMapping", "TestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "GPSalaryChangeKeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		case "ME":
			testDataFileName= ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "MEKeyDataMapping", "TestDataFileName", "Data");
			mappingDataSheet= ExcelUtilities.getKeyValueByPosition(testDataFileName, "MEKeyDataMapping", "MappingDataSheet", "Data");
			paramSheetName= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, "ParameterSheetName", "Data");
			break;
		default:
			Assert.fail(moduleType+ " module is not found");
			break;
		}
		
		String strColumns= ExcelUtilities.getKeyValueByPosition(testDataFileName, mappingDataSheet, key, "Data");
		String []columnArray= strColumns.split(",");
		List<String> columns= Arrays.asList(columnArray);
		int columnCount= columns.size();
		
		for(int i=0;i<columnCount;i++){
			String columnName= columns.get(i).toString().trim();
			value= ExcelUtilities.getKeyValueByPosition(testDataFileName, paramSheetName, key, columnName);
			if(!(value.isEmpty())){
				values.add(value);
			}
		}
		
		return values;
	}

	public static String getExcelParameterByModule(String module, String type) {
		String value="";
		
		switch(module){
		case"GOS1":{
			switch(type){
			case "NewTestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "NewTestDataFileName", "Data");
				break;
			case "PatientEleOption":
				value= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "PatEleOptionSheet", "Data");
				break;
			case "PatDeclOption":
			value= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "PatDeclOptionSheet", "Data");
				break;
			case "PerDeclOption":
				value= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "PerDeclOptionSheet", "Data");
				break;
			case "ValidationMessage":
				value= ExcelUtilities.getKeyValueByPosition("OPTESTDATA_New.xlsx", "OPGOS1KeyDataMapping", "ValidationMsgSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case"GOS3":
			break;
		case"GOS4":{
			switch(type){
			case "NewTestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "NewTestDataFileName", "Data");
				break;
			case "PatientEleOption":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "PatEleOptionSheet", "Data");
				break;
			case "PatDeclOption":
			value= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "PatDeclOptionSheet", "Data");
				break;
			case "SupDeclOptionSheet":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "SupDeclOptionSheet", "Data");
				break;
			case "PatDeclOptionSheet2":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS4TESTDATA_New.xlsx", "OPGOS4KeyDataMapping", "PatDeclOptionSheet2", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case "GOS5":{
			switch(type){
			case "NewTestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "OPGOS5KeyDataMapping", "NewTestDataFileName", "Data");
				break;
			case "PatientEleOption":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "OPGOS5KeyDataMapping", "PatEleOptionSheet", "Data");
				break;
			case "PatDeclOption":
			value= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "OPGOS5KeyDataMapping", "PatDeclOptionSheet", "Data");
				break;
			case "PerDeclOption":
				value= ExcelUtilities.getKeyValueByPosition("OPGOS5TESTDATA_New.xlsx", "OPGOS5KeyDataMapping", "PerDeclOptionSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case"GOS6":
			break;
		case "GPSolo":{
			switch(type){
			case "TestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("GPPSoloTestData.xlsx", "GPSoloKeyDataMapping", "TestDataFileName", "Data");
				break;
			case "TestDataSheet":
				value= ExcelUtilities.getKeyValueByPosition("GPPSoloTestData.xlsx", "GPSoloKeyDataMapping", "TestDataSheet", "Data");
				break;
			case "SoloAmtTierSheet":
				value= ExcelUtilities.getKeyValueByPosition("GPPSoloTestData.xlsx", "GPSoloKeyDataMapping", "SoloAmtTierSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case "GPAVC":{
			switch(type){
			case "TestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("GPPVolContriTestData.xlsx", "GPAVCKeyDataMapping", "TestDataFileName", "Data");
				break;
			case "TestDataSheet":
				value= ExcelUtilities.getKeyValueByPosition("GPPVolContriTestData.xlsx", "GPAVCKeyDataMapping", "TestDataSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case "CreateUser":{
			switch(type){
			case "FileName":
				value= ExcelUtilities.getKeyValueByPosition("PortalUserDetails.xlsx", "KeyDataMapping", "FileName", "Data");
				break;
			case "UserDetailsSheet":
				value= ExcelUtilities.getKeyValueByPosition("PortalUserDetails.xlsx", "KeyDataMapping", "UserDetailsSheet", "Data");
				break;
			case "MappingDataSheet":
				value= ExcelUtilities.getKeyValueByPosition("PortalUserDetails.xlsx", "KeyDataMapping", "MappingDataSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		case "GPSalChange":{
			switch(type){
			case "TestDataFile":
				value= ExcelUtilities.getKeyValueByPosition("GPSalaryChange.xlsx", "GPSalaryChangeKeyDataMapping", "TestDataFileName", "Data");
				break;
			case "TestDataSheet":
				value= ExcelUtilities.getKeyValueByPosition("GPSalaryChange.xlsx", "GPSalaryChangeKeyDataMapping", "TestDataSheet", "Data");
				break;
			case "SalChangeAmtTierSheet":
				value= ExcelUtilities.getKeyValueByPosition("GPSalaryChange.xlsx", "GPSalaryChangeKeyDataMapping", "SalChangeAmtTierSheet", "Data");
				break;
			default:
				Assert.fail(type+" is not found inside module: "+module);
				break;
			}
		}
			break;
		default:
			Assert.fail(module+" module is not found");
			break;
		}
		return value;
	}

}