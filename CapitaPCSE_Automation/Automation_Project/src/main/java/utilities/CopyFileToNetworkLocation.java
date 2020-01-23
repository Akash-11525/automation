package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;

import helpers.DatabaseHelper;
import helpers.GPPHelpers;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import testdata.ConfigurationData;

public class CopyFileToNetworkLocation {

	/*	public static void main(String [] args) throws IOException, InterruptedException, SQLException{
		placeFileToNetworkPath(GPPHelpers.aspSourcePath, GPPHelpers.CQRSdestination, "Aspiration");
		System.out.println("************Aspiration Done***********");
		placeFileToNetworkPath(GPPHelpers.achSourcePath, GPPHelpers.CQRSdestination, "Achievement");
		System.out.println("************Achievement Done***********");
	}*/

	public static List<String> placeFileToNetworkPath(String sourcePath, String destination, String fileType, String environment) throws IOException, InterruptedException, SQLException {
		File[] fileArray=locateFiles(sourcePath);
		int fileArrayLength= fileArray.length;
		List<File> sourceFileList= Arrays.asList(fileArray);

		List<String> copiedFiles= getFileDetails(fileArray,sourcePath,destination,fileArrayLength,environment);
		boolean allFilesCopied= verifyFileCopyStatus(sourceFileList,copiedFiles);
		Assert.assertEquals(allFilesCopied, true, "All files are not copied to path: "+destination);
		System.out.println("Files copied to network path- "+(destination)+", are "+copiedFiles);

		GPPHelpers.haltExecution();
		List<String> interfaceDetails= GPPHelpers.getQOFInfoFromStagingTable(fileArray,fileType,environment);
		System.out.println("Interface Run details values before execution are: "+interfaceDetails);

		List<String> deletedFiles= deleteFiles(copiedFiles,destination);
		boolean allFilesDeleted= verifyFileDeleteStatus(deletedFiles,copiedFiles);
		Assert.assertEquals(allFilesDeleted, true, "All files are not deleted from path: "+destination);
		System.out.println("Files deleted from network path- "+(destination)+", are "+deletedFiles);

		//haltExecution();
		String expectedFileStatus="6";
		String interfaceRunID= interfaceDetails.get(0);
		String SSIS_status= DatabaseHelper.getStatusAfterJobExec("StagingDb", "Staging",interfaceRunID ,"PCSE.ETL.GPP.QOFDataLoad",fileType, environment);
		ExcelUtilities.setKeyValueinExcelWithPosition("GPPQOFTESTDATA.xlsx", "FileStatus", fileType, SSIS_status, 3);

		System.out.println("File status after SSIS job is updated in excel file for value: "+SSIS_status);
		//getStatusPostSSIS(interfaceRunID);
		Assert.assertEquals(SSIS_status, expectedFileStatus, "File status is different");
		System.out.println("File has been processed in CRM and status has been updated to "+SSIS_status);
		return interfaceDetails;
	}

	//Added to helpers
	private static List<String> deleteFiles(List<String> copiedFiles, String destination) throws InterruptedException {
		Thread.sleep(4000);
		List<String> deletedFiles= new ArrayList<String>();
		for(int i=0;i<copiedFiles.size();i++){
			String filePath= destination+"\\"+copiedFiles.get(i);
			File readNetworkFolder= new File(filePath);
			readNetworkFolder.delete();
			System.out.println("File has been deleted for index: "+i);
			deletedFiles.add(filePath);
		}

		return deletedFiles;
	}

	private static File[] locateFiles(String source) throws IOException {
		File readFolder= new File(source);
		File[] fileArray= readFolder.listFiles();
		return fileArray;
	}

	private static boolean verifyFileCopyStatus(List<File> sourceFileList, List<String> copiedFiles) {
		boolean allFilesCopied;
		int sourceCount= sourceFileList.size();
		int destCount= copiedFiles.size();
		if(sourceCount==destCount){
			allFilesCopied=true;
		}else{
			allFilesCopied=false;
		}
		return allFilesCopied;
	}
	private static boolean verifyFileDeleteStatus(List<String> deletedFiles, List<String> copiedFiles) {
		boolean allFilesDeleted;
		int copyFileCount= copiedFiles.size();
		int deleteFileCount= deletedFiles.size();
		if(copyFileCount==deleteFileCount){
			allFilesDeleted=true;
		}else{
			allFilesDeleted=false;
		}
		return allFilesDeleted;
	}

	private static List<String> getFileDetails(File[] fileArray,String source, String destination, int fileArrayLength, String environment) throws IOException {
		List<String> copiedFiles= new ArrayList<String>();
		int count=0;
		String [] textFiles= new String[fileArrayLength];
		String fileName="";
		String filePath= "";
		for(int i=0;i<fileArrayLength;i++){

			if(fileArray[i].isFile() && (fileArray[i].getName().endsWith(".dat") || fileArray[i].getName().endsWith(".txt"))){
				count= count+1;
				fileName= fileArray[i].getName().toString();
				filePath= fileArray[i].getAbsolutePath().toString();
				//int lineCount= calculateLines(filePath);
				//String[] content = readFileContent(filePath,lineCount);
				textFiles[count-1]= filePath;
				System.out.println("File name is "+textFiles[count-1]);
				List<String> Files = new ArrayList<String>(Arrays.asList(textFiles));
				Files.removeAll(Collections.singleton(null));
				copyFileToNetwork(source, destination,fileName,environment);
				copiedFiles.add(fileName);
			}

		}
		return copiedFiles;
	}

	private static void copyFileToNetwork(String source,String destination, String fileName, String environment) throws IOException {

		String domain= ConfigurationData.getRefDataDetails(environment, "BizTalkDomain");
		String userName= ConfigurationData.getRefDataDetails(environment, "BiZTalkUserName");
		String password= ConfigurationData.getRefDataDetails(environment, "BizTalkPassword");
		String user = domain+";"+userName+":"+password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
		//String path = destination + ("\\"+fileName);
		String PathWithFwdSlash= destination.replace("\\", "/");
		String PathWithSingleSlash= PathWithFwdSlash.replace("//", "/");
		String path="smb:/"+PathWithSingleSlash+("/"+fileName);
		System.out.println("Path: "+path);

		SmbFile sFile = new SmbFile(path, auth);
		System.out.println("path for SMB object is accepted");
		//byte[][] fileData= convertToBytes(content);
		System.out.println("Byte array is created");
		SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
		System.out.println("SmbFileOutputStream object is initiated");
		FileInputStream fis= new FileInputStream(source+"\\"+fileName);
		sfos.write(IOUtils.toByteArray(fis));
		sfos.close();
		System.out.println("Content is written for file name: "+fileName);

	}

	public static List<String> locateAndCopyToLocalFolder(String srcFilePath, String destFilePath, String nameContent, String extension) throws IOException, InterruptedException {
		List<String> copiedFiles= new ArrayList<String>();
		File file= new File(srcFilePath);
		File[] fileArray= file.listFiles();
		int fileCount= fileArray.length;
		for(int i=0; i<fileCount; i++){
			if(fileArray[i].isFile()&&fileArray[i].getName().startsWith(nameContent)&&fileArray[i].getName().endsWith(extension)){
				String fileName= fileArray[i].getName();
				deleteFile(fileName, destFilePath);
				copyFileToLocalFolder(srcFilePath,destFilePath,fileName);
				copiedFiles.add(fileName);
			}
		}
		return copiedFiles;
	}

	private static void deleteFile(String fileName, String destination) throws InterruptedException {
		Thread.sleep(4000);
		File readFolder= new File(destination+"\\"+fileName);
		if(readFolder.exists()){
			readFolder.delete();
			System.out.println("File has been deleted: "+fileName);
		}else{
			System.out.println("File is not present: "+fileName);
		}
	}

	private static void copyFileToLocalFolder(String srcFilePath, String destFilePath, String fileName) throws IOException, InterruptedException {
		File sourceFile = new File(srcFilePath);
		File destinationFile = new File(destFilePath);

		InputStream in = new FileInputStream(sourceFile+"\\"+fileName);
		OutputStream out = new FileOutputStream(destinationFile+"\\"+fileName);

		byte[] buffer = fileName.getBytes();
		int length;
		//copy the file content in bytes
		while ((length = in.read(buffer)) > 0){
			out.write(buffer, 0, length);
		}

		in.close();
		out.close();

		System.out.println("Files have been copied successfully");
	}

	public static String createDirectory()
	{
		File dir = new File(System.getProperty("user.dir") + "/Evidence/" + new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));

		if(dir.exists())
		{
			System.out.println("A folder with name '"+new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime())+"' already exist");
		}
		else
		{
			dir.mkdir();
		}
		return dir.getPath();
	}

}
