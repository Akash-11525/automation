package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import com.csvreader.CsvWriter;
import testdata.ConfigurationData;

public class WriteCSV {
	public static String ColsposcopyFileUploadPath= System.getProperty("user.dir")+ConfigurationData.ColposcopyUploadFilePath;
	public static String HPVFileUploadPath= System.getProperty("user.dir")+ConfigurationData.HPVUploadFilePath;
	public static String GPPAdjUploadFilePath= System.getProperty("user.dir")+ConfigurationData.GPPAdjUploadFilePath;
	public static String OPAdjUploadFilePath= System.getProperty("user.dir")+ConfigurationData.OPAdjUploadFilePath;
			//"D:\\CSVTest\\New";
	//public static String newFileName=destPath+ "\\ColposcopyUpload.csv";
	
	public static String[][] appendRowForIndex(String[][] cSVData, List<String> newData, int rowIndex,
			List<Integer> colIndexValues) {
		
		int colCount= colIndexValues.size();
		int cSVDataLength= cSVData.length;
		String[]values;

			mainLoop:
			for(int i=0;i<cSVDataLength;i++){
				if(i==rowIndex){
					values= cSVData[i];
					int count=0;
					int colIndex=0;
					for(int arrayIndex=0;arrayIndex<(values.length);arrayIndex++){
						String actValue= values[arrayIndex].toString();

						int newIndex= colIndexValues.get(colIndex);
						String expValue= newData.get(newIndex).toString();						
							if(arrayIndex==newIndex){
								values[newIndex]= actValue.replace(actValue, expValue);
								count= count+1;
								colIndex++;
								if(colCount==count){
									cSVData[i]= values;
									break mainLoop;
								}
							}				
						}
					}
				}
			return cSVData;
		}

	public static String[][] copyRowForIndex(String[][] cSVData, int rowIndex, int duplicateRecordIndex) {
		int cSVDataLength= cSVData.length;
		String[]values;
		for(int i=0;i<cSVDataLength;i++){
			if(i==rowIndex){
				values= cSVData[i];
				cSVData[duplicateRecordIndex]= values;
				break;
			}
		}
		return cSVData;
	}

	public static void writeFileData(String[][] cSVData,String destFilePath ,char delimiter, String fileName) throws IOException {
		CsvWriter csvOutput = null;
		int cSVDataLength= cSVData.length;
		String [] values= new String [cSVDataLength];
		String CSVfile= destFilePath+"\\"+fileName+".csv";
		/*boolean alreadyExists = new File(CSVfile).exists();*/
		//if (alreadyExists){		
			deleteFiles(destFilePath);	
		//}
		csvOutput = new CsvWriter(new FileWriter(CSVfile, true), delimiter);
		for(int index=0;index<cSVDataLength;index++){
			values= cSVData[index];
			csvOutput.writeRecord(values);
		}
		csvOutput.close();
		System.out.println("CSV file has been created with name as: "+CSVfile);
	}

	private static void deleteFiles(String destPath) throws IOException {
		File[] fileArray= locateFiles(destPath);
		List<File> files =Arrays.asList(fileArray);
		int fileCount= files.size();
		for(int i=0;i<fileCount;i++){
			String filePath= files.get(i).toString();
			File file= new File(filePath);
			file.delete();
			System.out.println("File has been deleted for index: "+i);
		}
		
	}
	
	private static File[] locateFiles(String destPath) throws IOException {
	File readFolder= new File(destPath);
	File[] fileArray= readFolder.listFiles();
	return fileArray;
	}

	public static String getDestPathForModule(String feature) {
		String destPath="";
		switch(feature){
		case "Colposcopy":
			destPath= WriteCSV.ColsposcopyFileUploadPath;
			break;
		case "HPV":
			destPath= WriteCSV.HPVFileUploadPath;
			break;
		case "GPPAdj":
			destPath= WriteCSV.GPPAdjUploadFilePath;
			break;
		case "OPAdj":
			destPath= WriteCSV.OPAdjUploadFilePath;
			break;
		default:
			Assert.fail(feature+" feature is not found.");
			break;
		}
		
		return destPath;
	}

		
}
	



