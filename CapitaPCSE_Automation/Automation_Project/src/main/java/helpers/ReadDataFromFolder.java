package helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import helpers.ReadDelimiterFile;

public class ReadDataFromFolder {
	static String filePath= ReadDelimiterFile.gmpFilePath;
	
	public static void main(String[] args) throws IOException {
	
		fetchFilesFromFolder(filePath);
	}
	public static String[] fetchFilesFromFolder(String filePath) {
		int count=0;
		
		
		File readFolder= new File(filePath);
		File[] fileArray= readFolder.listFiles();
		String [] textFiles= new String[fileArray.length];
		for(int i=0;i<fileArray.length;i++){
			
			if(fileArray[i].isFile() && fileArray[i].getName().endsWith(".txt") && fileArray[i].getName().startsWith("X24N_PCSE_")){
				count= count+1;
				String fileName= fileArray[i].toString();
				textFiles[count-1]=fileName;
				System.out.println("File name is "+textFiles[count-1]);
			}
			
		}
		System.out.println("Count for text files is "+count);
		// this removes empty elements from an array
		List<String> gmpFiles = new ArrayList<String>(Arrays.asList(textFiles));
		gmpFiles.removeAll(Collections.singleton(null));
	    return gmpFiles.toArray(new String[gmpFiles.size()]);

	}

}
