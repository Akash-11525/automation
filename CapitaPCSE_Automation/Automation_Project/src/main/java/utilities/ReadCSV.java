package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.testng.Assert;

import com.csvreader.CsvReader;
import com.google.common.base.Joiner;

import testdata.ConfigurationData;

public class ReadCSV {
	
	public static String colposcopySourcePath= System.getProperty("user.dir")+ ConfigurationData.ColposcopySampleFilePath+"\\SampleColposcopy.csv";
	public static String HPVSourcePath= System.getProperty("user.dir")+ ConfigurationData.HPVSampleFilePath+"\\HPVSampleFile.csv";
	public static String GPPAdjSourcePath= System.getProperty("user.dir")+ ConfigurationData.GPPAdjSampleFilePath+"\\GPPBulkAdjsutment.csv";
	public static String OPAdjSampleFilePath= System.getProperty("user.dir")+ConfigurationData.OPAdjSampleFilePath+"\\OPHBulkAdjsutment.csv";
	public static CsvReader csvReader;
	public static Scanner scanFile;
	public static String [][]CSVData;
	public static String [] header;		
	
	public static String fileLoc = "./Download/";
	//File fileLocation = new File("./Download/");
	//File FileLocation = new File("C:\\Users\\amitkumarr\\Downloads\\");
	File fileW = new File("./Download/sto1.csv");

	public static boolean generateAndCopyCSVFile(List<String> nHSDetails, int rowIndex, int duplicateRecordIndex, List<Integer> colIndexValues, 
			String sourcePath, String feature, char character) {
		boolean isDataSaved=false;
		try{
			int lineCount= calculateLines(sourcePath);
			CSVData= readCSVData(sourcePath,lineCount);
			System.out.println("Sample CSV data has been captured");
			String[][] UpdatedCSV= WriteCSV.appendRowForIndex(CSVData,nHSDetails,rowIndex,colIndexValues);
			System.out.println("Data has been appended at row index: "+rowIndex);		
			String[][]finalCSVData= WriteCSV.copyRowForIndex(UpdatedCSV,rowIndex,duplicateRecordIndex);
			System.out.println("Data has been copied for row index: "+duplicateRecordIndex);
			String destFilePath= WriteCSV.getDestPathForModule(feature);
			char separator= character;
			String fileName= feature+"Upload";
			WriteCSV.writeFileData(finalCSVData,destFilePath,separator,fileName);
			isDataSaved=true;
		}catch(Exception e){
			isDataSaved=false;
		}
		return isDataSaved;
	}
	
	public static String[][] readCSVData(String sourcePath, int lineCount) throws IOException {
		String [][]CSVData= null;
		csvReader = new CsvReader(sourcePath);
		csvReader.readHeaders();
		while (csvReader.readRecord())
		{	
			int columnCount= csvReader.getColumnCount();
			CSVData= new String[lineCount][columnCount];
			@SuppressWarnings("resource")
			BufferedReader bufRdr  = new BufferedReader(new FileReader(sourcePath));
	        String line = null;
	        int row = 0;
	        int col = 0;
	        while((line = bufRdr.readLine()) != null)
	        {
	            StringTokenizer st = new StringTokenizer(line,",");
	            while (st.hasMoreTokens())
	            {
	            	//get next token and store it in the array
	            	CSVData[row][col] = st.nextToken();
	            	col++;
	            }
	            col=0;
	            row++;
	        }
	      
		}
		
		return CSVData;
	}

	public static int calculateLines(String sourcePath) throws IOException {
		int totalLines = 0;
		BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
		while (reader.readLine() != null)
			totalLines++;
		reader.close();
		System.out.println("No. of lines in file: " + totalLines);
		return totalLines;
	}
	
	public static String getSourcePathForModule(String feature) {
		String sourcePath="";
		switch(feature){
		case "Colposcopy":
			sourcePath= ReadCSV.colposcopySourcePath;
			break;
		case "HPV":
			sourcePath= ReadCSV.HPVSourcePath;
			break;
		case "GPPAdj":
			sourcePath= ReadCSV.GPPAdjSourcePath;
			break;
		case "OPAdj":
			sourcePath= ReadCSV.OPAdjSampleFilePath;
			break;
		default:
			Assert.fail(feature+" feature is not found.");
			break;
		}
		
		return sourcePath;
	}
		
	// In Below Method - filename is CSV file name & boolean flag is concatenate address fields which are appearing in case Week4 To Do PNL.
	public static List<ArrayList<String>> getCSVFile(String filename, boolean flag, int addrstrfieldno, int addrendfieldno) throws IOException
	{
		List<ArrayList<String>> csvData = new ArrayList<ArrayList<String>>();
		BufferedReader BR = null;

		Path path = Paths.get(fileLoc+filename);
		Charset charset = Charset.forName("windows-1252");
		//Charset charset = Charset.forName("Unicode");
		// BR = Files.newBufferedReader(path);
		//	 reader = new CSVReader(new FileReader(csvFile));
		try 
		{




			String Line = null;
			//BR=new BufferedReader( new FileReader(path.toString()));
			//BR = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())));
			BR = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()),charset ));

			//String headerLine = 
			BR.readLine();
			while (BR.ready()) {
				Line = BR.readLine().trim().replace("\"", ""); // replace adds to remove the extra quotes appearing.

				if (!Line.isEmpty())
				{

					// System.out.println("Raw CSV data: " + Line);
					//System.out.println("Converted ArrayList data: " + CSVtoArrayList(Line) );

					if (flag){

						ArrayList<String> TL=  ConcatenateAddressFields(CSVtoArrayList(Line), addrstrfieldno, addrendfieldno);


						csvData.add(TL);
						// System.out.println(TL.size());
					}
					else{

						csvData.add(CSVtoArrayList(Line));
						// System.out.println(CSVtoArrayList(Line).size());
					}


				}

			}

			//  csvData.removeAll(Arrays.asList(null,""));	
			// Amit R : This code is added to sort the Array List based on NHS Number (First Field)
			Collections.sort(csvData, new Comparator<ArrayList<String>>() {    
				@Override
				public int compare(ArrayList<String> o1, ArrayList<String> o2) {
					return o1.get(0).compareTo(o2.get(0));
				}               
			});
			//System.out.println("CSV Array list added to list: " +csvData);
			//System.out.println(csvData.size());




		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (BR != null) BR.close();
			} catch (IOException Ex) {
				Ex.printStackTrace();
			}
		}


		return csvData;
	}
	
	// Utility which converts CSV to ArrayList using Split Operation
	public static ArrayList<String> CSVtoArrayList(String CSVLine) {
		ArrayList<String> Result = new ArrayList<String>();

		if (CSVLine != null) {
			String[] splitData = CSVLine.trim().split("\\s*,\\s*");
			// splitData = CSVLine.split("\",\"");
			for (int i = 0; i < splitData.length; i++) {
				if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {



					splitData[i].replaceAll("\\s+","");
					//splitData[i]=splitData[i].replaceAll("\\s","");

					Result.add(splitData[i].trim());

				}
			}
		}

		return Result;
	}

	public static ArrayList<String> ConcatenateAddressFields(ArrayList<String> newList, int addrstrfieldno, int addrendfieldno){
		
		//ArrayList<String> newList = CSVtoArrayList(Line);
		ArrayList<String> NL = new ArrayList<String>();
		
		ArrayList<String> AddressList = new ArrayList<String>();
	
		for (int j=0; j<newList.size();j++)
		{
			if (j>addrstrfieldno && j<addrendfieldno)
			{
				if(newList.get(j).length()>0)
				{
					AddressList.add(newList.get(j));
				}
			}
			else 
				if (j==addrendfieldno)
				{
					System.out.println(AddressList);
					NL.add(Joiner.on(",").join(AddressList));
					NL.add(newList.get(j));
				}
				else
					NL.add(newList.get(j));
		}
		return NL;

	}

	

}
