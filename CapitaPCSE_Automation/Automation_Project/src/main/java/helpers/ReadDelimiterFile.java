package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import helpers.ReadDataFromFolder;

public class ReadDelimiterFile {
	static String filePath = System.getProperty("user.dir") + "\\Download\\";	
	static String paymentfilePath;/* = "D:\\Akshay Testing\\X24N_PCSE_20170928NHS ENGLAND NORTH (LANCASHIRE).1.txt"*/
	//static String sampleFilePath = "D:\\Akshay Testing\\format.txt";
	public static String gmpFilePath= filePath;
	static Scanner scanFile;
	static String[][] sampleFile;
	static String[][] paymentFile;
	static String[] gmpFiles=ReadDataFromFolder.fetchFilesFromFolder(gmpFilePath);;
	static boolean proceedCalculation;

	public static void ReadDotonefile() {
		
		for(int i=0;i<gmpFiles.length;i++){
			
			paymentfilePath=gmpFiles[i];
			int totalLine = 0;
			try {
				totalLine = calculatePaymentLines(paymentfilePath);
		//		sampleFileLines = calculatePaymentLines(sampleFilePath);
			} catch (IOException e) {

				e.printStackTrace();
			}

			paymentFile = readfile(paymentfilePath, totalLine, !proceedCalculation);
		//	sampleFile = readfile(sampleFilePath, sampleFileLines, proceedCalculation);
		}

/*			verifyActualData(paymentFile,sampleFile);*/
	}

/*	private static void verifyActualData(String[][] paymentFile2, String[][] sampleFile2) {
		for(int i=0;i<paymentFile2.length;i++){
			for(int j=0;j<paymentFile2[i].length;j++){
				compareValue(paymentFile2[i],sampleFile2[i]);
			}
		}
	}*/

	private static int calculatePaymentLines(String paymentfilePath2) throws IOException {
		// Find no. of lines in text file
		int totalLines = 0;
		BufferedReader reader = new BufferedReader(new FileReader(paymentfilePath2));
		while (reader.readLine() != null)
			totalLines++;
		reader.close();
		System.out.println("No. of lines in file: " + totalLines);

		return totalLines;
	}

	private static String[][] readfile(String paymentfilePath2, int totalLine, boolean proceedCalculation2) {
		String[][] strFileData = new String[totalLine][];
		int missingChars=0;
		try {
			// this will find the file
			File readNhsFile = new File(paymentfilePath2);
			
			// this scanner will read the file for the next line
			scanFile = new Scanner(readNhsFile);

			for (int rowNo = 0; rowNo < totalLine; rowNo++) {

				String nextLine = scanFile.nextLine();
		
				// next line data
				System.out.println("Next line is: " + nextLine + " at " + rowNo + " index");

				// split the string
				String[] strLineData = nextLine.split("\\|");

				strFileData[rowNo] = strLineData;
				
				if(proceedCalculation2){
					missingChars=calculateMissingCharCount(strFileData[rowNo], nextLine);
					
					System.out.println(missingChars+" characters are missing for row number "+rowNo);
				}
/*					String [] finalLineData= new String [strLineData.length+missingChars];
					for(int j=(strLineData.length+1);j<((strLineData.length)+missingChars);j++){
						String temp="";
						finalLineData[j]=temp;
						strFileData[rowNo] = finalLineData;
					}*/

			}
					if (proceedCalculation2) {
						validatePaymentData(strFileData, totalLine);
					}

			else {
				validateNonPaymentData(strFileData, totalLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strFileData;
	}

	private static int calculateMissingCharCount(String[] strFileData, String nextLine) {
		int missingCount = 0;
		int lineCount= (nextLine.length()-1);
		try {

			for (int i = lineCount; (nextLine.charAt(i)) == '|'; i--) {
				missingCount = missingCount + 1;			
				}
			if(missingCount==0){
				missingCount=1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (missingCount-1);

	}

	private static void validateNonPaymentData(String[][] strFileData, int totalLine) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void validatePaymentData(String[][] fileData, int totalLine) {
		try {
			calculatePaymentAttributes(fileData, totalLine);

			/* calculateNonPaymentAttributes(); */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void calculatePaymentAttributes(String[][] fileData, int totalLine) {
		double finalHeaderAmount = 0;
		try {

			for (int headerno = 1; headerno <= (Integer.parseInt(fileData[totalLine - 1][2])); headerno++) {
				double amountdue = 0;
				String strheaderNo = Integer.toString(headerno);
				amountdue = amountdue(fileData, amountdue, strheaderNo);
				finalHeaderAmount = finalHeaderAmount + amountdue;
			}

			verifyFooterData(fileData[totalLine - 1], finalHeaderAmount);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void verifyFooterData(String[] fileData, double amountdue) {
		double footerValue = 0;
		footerValue = Double.parseDouble(fileData[1]);
		if (footerValue == amountdue) {
			System.out.println("Header and Footer values are matching");
		} else {
			System.out.println("Header and Footer values are not matching");
		}

	}

	private static double amountdue(String[][] fileData, double amountdue, String strheaderNo) {
		double headerAmount = 0, templineAmount = 0, invoiceAmount = 0;
		int lineCount = 0;
		try {
			for (int rowCount = 0; rowCount < fileData.length; rowCount++) {
				if (fileData[rowCount][0].equals("IH") && fileData[rowCount][1].equals(strheaderNo)) {
					headerAmount = Double.parseDouble(fileData[rowCount][10]);
					amountdue = amountdue + headerAmount;
					System.out.println("Header value for " + strheaderNo + " is:" + headerAmount);

				} else if (fileData[rowCount][0].equals("IL") && fileData[rowCount][1].equals(strheaderNo)) {
					lineCount = lineCount + 1;
					templineAmount = Double.parseDouble(fileData[rowCount][15]);
					invoiceAmount = invoiceAmount + templineAmount;
					System.out.println("Line amount for number " + lineCount + " is: " + templineAmount);
				}

			}
			if (amountdue == invoiceAmount) {
				System.out.println("Header Amount and Invoice Amount are matching for header number " + strheaderNo);
			} else {
				System.out
						.println("Header Amount and Invoice Amount are not matching for header number " + strheaderNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amountdue;
	}
}
