package helpers;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.stream.events.Comment;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jxl.Workbook;
import pageobjects.XMLParseHelpers.XMLhelpers;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CommonFunctions extends Support{

	static int randomNumber=0;
	static String number = null;
	//static Boolean fl = false;
	static boolean flag = false;
	public WebDriver driver;
	
	

	//static String random = null;

	
	
	


	public static String generateRandomNo(){

		Random random=new Random();

		boolean loop=true;
		while(loop) {
			randomNumber=random.nextInt();
			if(Integer.toString(randomNumber).length()==10 && !Integer.toString(randomNumber).startsWith("-")) {
				loop=false;
			}
		}


		number = String.valueOf(randomNumber);
		return number;
	}

	public static boolean validNHS(String i){

		//i = generateRandomNo();
		int len = i.length();
		int sum = 0, rem = 0;
		int[] digArr = new int[len];
		for (int k=1; k<=len; k++) // compute weighted sum
			sum += (11 - k) * Character.getNumericValue(i.charAt(k - 1));
		if ((rem = sum % 11) == 0) 
		{
			flag =true;
			System.out.println("Valid NHS: "+i);
		}

		else
		{
			flag =false;
		}


		return flag;

	}


	public static String generateValidNHSNo()
	{
		String random = null;
		boolean fl= false;
		
		while(!fl){

			/*RandomNumber ObjRN = new RandomNumber();
	Temp ObjTemp = new Temp();*/

			random = generateRandomNo();
			fl = validNHS(random);
		}
		//String random;
		//System.out.println(random);
		return random;
	}

	public static List<String> getAllOptions(WebElement element) {
		List<String> options = new ArrayList<String>();
		for (WebElement option : new Select(element).getOptions()) {
			String txt = option.getText();
			if (option.getAttribute("value") != "") options.add(option.getText());
		}
		return options;
	}

	public static String getSelectedOptionFromDropdown(WebElement element) {
		String selectedOption = null;
		selectedOption = new Select(element).getFirstSelectedOption().getText();

		return selectedOption;
	}

	public static void selectOptionFromDropDown(WebElement ele, String value)
	{
		Select dropdown = new Select(ele);
		dropdown.selectByVisibleText(value);
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		System.out.println("Dropdown Value Selected:  "+selectedValue);
	}

	public static void selectOptionFromOptGroupDropDown(WebElement ele, String groupName, String value)
	{
		
		List <WebElement> options = ele.findElements(By.xpath("//optgroup[@id='"+groupName+"']/option"));

		for (WebElement option : options)
		{
			if(option.getText().equals(value))
			{
				option.click();
				break;
			}
		}

	}

	public static String getDOB(String age){
		LocalDateTime DT = LocalDateTime.now();
		System.out.println(DT);
		// Pass here Age of Patient
		int ageInt = Integer.parseInt(age);
		LocalDateTime DOBDT = DT.minusYears(ageInt);
		LocalDateTime DOB = DOBDT.minusDays(10);


		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("MM/dd/yyyy");
		System.out.println(DT.format(formatter));
		System.out.println(DOB.format(formatter));

		String date = DOB.toString();

		return date;

	}
	
	public static String getDate_Months(String age){
		LocalDateTime DT = LocalDateTime.now();
		System.out.println(DT);
		// Pass here Age of Patient
		int ageInt = Integer.parseInt(age);
		LocalDateTime DOBDT = DT.minusMonths(ageInt);
		LocalDateTime DOB = DOBDT.minusDays(1);


		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("MM/dd/yyyy");
		System.out.println(DT.format(formatter));
		System.out.println(DOB.format(formatter));

		String date = DOB.toString();

		return date;

	}
	
	public static String getDOB(String age , String Pattern) throws InterruptedException{
		LocalDateTime DT = LocalDateTime.now();
		System.out.println(DT);
		// Pass here Age of Patient
		int ageInt = Integer.parseInt(age);
		LocalDateTime DOBDT = DT.minusYears(ageInt);
		LocalDateTime DOB = DOBDT.minusDays(10);


		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern(Pattern);
		System.out.println(DT.format(formatter));
		System.out.println(DOB.format(formatter));
		Thread.sleep(500);
		String date = DOB.format(formatter).toString();
		System.out.println("The converted Date " + date );
		return date;

	}

	public static String getDate(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("MM/dd/yyyy");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static String getDate(String Format){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern(Format);
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static String getDate_UK(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("dd/MM/yyyy");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	 public static String Tomorrowdate(String Currentdate , int days) throws ParseException
	 {
		 String date;
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("dd/MM/yyyy");
		 Calendar c = Calendar.getInstance();
		 c.setTime(sdf.parse(Currentdate));
		 c.add(Calendar.DATE, days);  // number of days to add
		 date = sdf.format(c.getTime());
		System.out.println("Date is : "+date);
	return date;
	 }

/*	public static String getDate_UK(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("dd/MM/yyyy");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}*/

	public static Boolean Verifybutton(String Buttonvalue, WebDriver driver) {
		boolean buttononpage = false;
		try{	

			List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);
				String ButtonValue = button.getText(); 
				if(ButtonValue.equalsIgnoreCase(Buttonvalue))
				{
					buttononpage =  true;
				}
				else
				{
					buttononpage = false; 
				}
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}	
		return buttononpage;
	}

	public static String generateTS(String str)
	{

		Date now = new java.util.Date(); 
		Timestamp current = new java.sql.Timestamp(now.getTime());
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);
		String newStr = str.concat(timeStamp);	

		return newStr;
	}
	public static String generateDtTimeStamp()
	{

		String newStr = new SimpleDateFormat("MMddyyyyHHmm").format(new Date());
		/*Date now = new java.util.Date(); 
		Timestamp current = new java.sql.Timestamp(now.getTime());
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);
		String newStr = str.concat(timeStamp);	*/

		return newStr;
	}
	public static Date convertStringtoCalDate(String date, String df) throws ParseException
	{

		Date d1 = null;
		try{
			Date d;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(df, Locale.ENGLISH);
			cal.setTime(sdf.parse(date));
			d1 = cal.getTime();

		}

		catch (ParseException e)
		{
			System.out.println("Parse Exception occured: " +e);
		}

		return d1;


	}


	public static String convertDateToString(Date d, String df) throws ParseException
	{

		//System.out.println(d);// Current time
		SimpleDateFormat sdf1 = new SimpleDateFormat(df); // Set your date format
		String currentDate = sdf1.format(d);
		//System.out.println(currentDate);
		return currentDate;


	}
	

	public static String GenerateUniqueAplphaNumeric_XML(String OldXmlValue) throws ParseException
	{
		String UpdatedXMLvalue = null;
		
		try{
			
		 	String AlphaLastLetter = OldXmlValue.split("-")[4].trim();
		   // System.out.println(AlphaLastLetter);
		    String DigitOnly = AlphaLastLetter.replaceAll("\\D+", "");
		    String Digit = DigitOnly.trim();
		    System.out.println(Digit);	
		    int foo = Integer.parseInt(Digit);
		/*    Random random=new Random();
			boolean loop=true;
			while(loop)
			{
				 posRandint = random.nextInt(Integer.MAX_VALUE);
			//	posRandint = random.nextInt(Integer.MAX_VALUE)+1;
			randomNumber=random.nextInt(Integer.MAX_VALUE);
System.out.println(Integer.toString(randomNumber).length());
System.out.println(Digit.length());


				if(Integer.toString(randomNumber).length()==Digit.length())
				{
					loop=false;
				}
			}*/
			Random random = new Random();
			Integer randomNumber;
			randomNumber = random.nextInt(Integer.SIZE - 1) + foo;
			System.out.println(randomNumber);
			//System.out.println(randomNumber);
			String RanNumber = 	String.valueOf(randomNumber);
		//	System.out.println(RanNumber);
			char[] Arr1 = AlphaLastLetter.toCharArray();
		//	System.out.println(Arr1[0]);
		//	System.out.println(Arr1.length);			
			char[] Arr2 = RanNumber.toCharArray();
		//	System.out.println(Arr2[0]);
			int M =0;
			for(int l=0;l<Arr1.length;l++)
			{
				char Choi  = Arr1[l];
				String Choice = Character.toString(Choi);
				if(!(Choice.matches("\\D+")))
				{
					Arr1[l] = Arr2[M];
					M= M+1; 
				}
				
			}
			String LastXMLvalue= Arrays.toString(Arr1);
			String text = String.valueOf(Arr1);
	//		System.out.println(text);
				
			UpdatedXMLvalue = OldXmlValue.replaceAll(OldXmlValue.split("-")[4].trim(), text);
			System.out.println(UpdatedXMLvalue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

			return UpdatedXMLvalue;
	}
	
	public static String ReadXML_GetCodeValue(String Filename,String Nodename,String AttributeName) {
		String OldXMLValue = null;
		try{
			
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
			System.out.println(filePath);
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//"+Nodename);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			int randomNumber = 0;
			for (int i = 0; i < nl.getLength(); i++)
			{
			    org.w3c.dom.Node currentItem = nl.item(i);
			  
			    OldXMLValue = currentItem.getAttributes().getNamedItem(AttributeName).getNodeValue();
			}
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return OldXMLValue;
	}
	
	public static void ReadXML_UpdateCodeValue(String Filename,String Nodename,String AttributeName,String UpdatedXMLValue) throws FileNotFoundException {
		String Key = null;
		try{
			
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;		
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//"+Nodename);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			int randomNumber = 0;
			for (int i = 0; i < nl.getLength(); i++)
			{
			    org.w3c.dom.Node currentItem = nl.item(i);
			  
			    Key = currentItem.getAttributes().getNamedItem(AttributeName).getNodeValue();
			    currentItem.getAttributes().getNamedItem(AttributeName).setTextContent(UpdatedXMLValue);
			}
			TransformerFactory transformerFactory = 
			          TransformerFactory.newInstance();
		Transformer transformer=
	                         transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result=new StreamResult(new File(filePath));
		transformer.transform(source, result);
		/*StreamResult consoleResult = new StreamResult(System.out);
		transformer.transform(source, consoleResult);*/
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	
	public static String ExtractClaimId_String(String xmlString, String StartwithElement, WebDriver driver) {
		String ClaimID = null;
		try{
			
			String Text =  xmlString.substring(xmlString.indexOf(StartwithElement) + 0 , xmlString.length());
			System.out.println(Text);
			String Text1 = Text.split("=")[1];
			System.out.println(Text1);
			String Text2 = Text1.replaceAll("\"", " ");
			System.out.println("Text2 is "+Text2 );
			ClaimID = Text2.split("/")[0].trim();
			System.out.println(ClaimID);
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ClaimID;
	}
	
	public static void ClickOnRadioButton(String Text,WebDriver driver) {

		try {
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{  
				/*JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);*/ //Commented by Rupesh -We have changed the code for scroll screen.
				
				Support support=new Support();
				support.scrolltoElement(driver, Radiobutton);	
				

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);





				if (RadioValue.equalsIgnoreCase(Text))
				{

					Radiobutton.click();


				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	

		public static void ClickOnRadioButton_type(String Text,WebDriver driver) {

		try {
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio']/label"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);

				if (RadioValue.equalsIgnoreCase(Text))
				{

					Radiobutton.click();


				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public static void ClickOnButton(String Text,WebDriver driver) {

		try {
			Thread.sleep(1000);
			List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{   
		
				/*JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);*/ //Commented by Rupesh -We have change the code for scroll screen.
				
				Support support=new Support();
				support.scrolltoElement(driver, button);	
				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String ButtonValue = button.getText(); 


				if (ButtonValue.equalsIgnoreCase(Text))
				{
					
					button.click();
					break;

				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	/*Added By Rupesh : Purpose for this method  - without using JavascriptExecutor*/
	public static void ClickOnButtonByText(String Text,WebDriver driver) {

		try {
			Thread.sleep(1000);
			List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{ 				
												/*JavascriptExecutor jse = (JavascriptExecutor)driver;            	
												jse.executeScript("arguments[0].scrollIntoView();", button);*/
												
				String ButtonValue = button.getText(); 


				if (ButtonValue.equalsIgnoreCase(Text))
				{
					
					button.click();
					break;

				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void ClickOnButton_TypeSubmit(String Text,WebDriver driver) {

		try {
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			List<WebElement> buttons=driver.findElements(By.xpath("//*[@class='col-xs-6 col-sm-6 text-right padding-right-0']/input"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{  
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);
				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String ButtonValue = button.getAttribute("value"); 
				System.out.println(ButtonValue);
				


				if (ButtonValue.equalsIgnoreCase(Text))
				{

					button.click();
					helpers.Support.PageLoadExternalwait(driver);
					break;

				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}



	public static void selectOptionFromTable(WebElement ele, String value)
	{

		List<WebElement> rows = ele.findElements(By.tagName("tr"));
		System.out.println(rows.size());

		for(WebElement row:rows)
		{
			WebElement rowData = row.findElement(By.tagName("td"));
			//String rowData = row.findElement(By.tagName("td")).getText();
			if (rowData.getText().equals(value))
			{
				rowData.click();
				System.out.println("The value is found in table.");
				break;
			}


		}






	}


	public static void ClickOnAllCheckBox(WebDriver driver) {

		try {
			Thread.sleep(3000);
			List<WebElement> CheckBoxs=driver.findElements(By.xpath("//div[@class='checkbox']"));
			System.out.println("total Checkbox "+CheckBoxs.size());
			for (WebElement CheckBox : CheckBoxs)
			{    

				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", CheckBox);
				Thread.sleep(1000);
				CheckBox.click();		  

			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static void ClickOnAllCheckBox_Type(WebDriver driver) {

		try {
			Thread.sleep(2500);
			List<WebElement> CheckBoxs=driver.findElements(By.xpath("//*[@type='checkbox']"));
			System.out.println("total Checkbox "+CheckBoxs.size());
			for (WebElement CheckBox : CheckBoxs)
			{    

				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", CheckBox);
				Thread.sleep(1000);
				CheckBox.click();		  

			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public static void ClickOnCheckBox(WebDriver driver ,String Text) {

		try {
			Thread.sleep(2000);
			List<WebElement> CheckBoxs=driver.findElements(By.xpath("//div[@class='checkbox']"));
			System.out.println("total Checkbox "+CheckBoxs.size());
			for (WebElement CheckBox : CheckBoxs)
			{    

				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", CheckBox);
				Thread.sleep(2000);
				if((CheckBox.getText()).equalsIgnoreCase(Text))
				{
					CheckBox.click();
					break;
				}

			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static boolean VerifyEnabledButton(WebDriver driver, String Text) {
		boolean disable = false;
		try {
			Thread.sleep(1000);
			List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{    
				
				/*JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);*/ //Commented by Rupesh -We have change the code for scroll screen.
				
				Support support=new Support();
				support.scrolltoElement(driver, button);	
				
				if((button.getText()).equalsIgnoreCase(Text))	
				{
					if(!(button.isEnabled()))
					{
						disable = true;
						break;
					}
				}

			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return disable;
	}

	public static Boolean VerifyTickMarkPresent(WebElement ele) throws InterruptedException {
		Thread.sleep(1500);
		boolean TickMarkflag = false;
		boolean ispresent = false;
		
		try{
			ispresent = ele.isEnabled();
			String menuName = ele.getAttribute("name");
			System.out.println(ispresent);
			if(ispresent)	
			{
				if(ele.findElement(By.tagName("i")).isDisplayed())

				{
					///WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i"));
					//scrolltoElement(driver, TickMarkOntab);
					TickMarkflag = true;

				}


			}
		}

		catch(NoSuchElementException e)
		{
			System.out.println("No Menu Option is enabled.  " +e);
		}
		return TickMarkflag;
	}

	public static Date getDateBeforeDays(int days) throws ParseException
	{
		LocalDate DaysAgo = LocalDate.now().minusDays(days);
		System.out.println(DaysAgo);
		
		java.util.Date dt = java.sql.Date.valueOf(DaysAgo);
				
		return dt;
	}
	
	public static Date getDateAfterDays(int days) throws ParseException
	{
		LocalDate DaysAfter = LocalDate.now().plusDays(days);
		System.out.println(DaysAfter);
		
		java.util.Date dt = java.sql.Date.valueOf(DaysAfter);
				
		return dt;
	}

	public static void ClickOnbutton_tag(String Text, WebDriver driver) {
		try {
			Thread.sleep(2000);
			List<WebElement> buttons=driver.findElements(By.tagName("button"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{  
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);
				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String ButtonValue = button.getText(); 


				if (ButtonValue.equalsIgnoreCase(Text))
				{

					button.click();
					break;

				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
		
		public static void Uploadfile(String filePath, WebDriver driver) {
		try {
	/* Robot robot = new Robot();
		

     robot.setAutoDelay(1000);

     StringSelection selection = new StringSelection(filePath);
     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,null);

     robot.setAutoDelay(1000);

     robot.keyPress(KeyEvent.VK_CONTROL);
     robot.keyPress(KeyEvent.VK_V);

     robot.keyRelease(KeyEvent.VK_CONTROL);
     robot.keyRelease(KeyEvent.VK_V);

     robot.setAutoDelay(1000);

     robot.keyPress(KeyEvent.VK_ENTER);
     robot.keyRelease(KeyEvent.VK_ENTER);*/
			

			WebElement uploadWindow = driver.switchTo().activeElement();
			uploadWindow.sendKeys(filePath);			
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void selectFirstOptionFromDropDown(WebElement ele)
	{
		Select dropdown = new Select(ele);
		dropdown.selectByIndex(0);
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		System.out.println("Dropdown Value Selected:  "+selectedValue);
	}
	
	public static void selectOptionFromDropDownByValue(WebElement ele, String value)
	{
		Select dropdown = new Select(ele);
		//dropdown.selectByIndex(0);
		dropdown.selectByVisibleText(value);
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		System.out.println("Dropdown Value Selected:  "+selectedValue);
	}
	
		public static void selectOptionFromList(WebElement ele, String option) throws InterruptedException {
	    	try {
	    
	        Thread.sleep(2000);
	        List<WebElement> options = ele.findElements(By.tagName("li"));
	        // Loop through the options and select the one that matches
	        for (WebElement opt : options) {
	        	WebElement optLink = opt.findElement(By.tagName("a"));
	            if (optLink.getText().equalsIgnoreCase(option)) {
	            	optLink.click();
						Thread.sleep(1000);
	                break;
	            }
	        }
	       // throw new NoSuchElementException("Can't find " + option + " in dropdown");
	}
	catch (org.openqa.selenium.StaleElementReferenceException ex)
		{
			
			System.out.println("The Stale Reference Exception has occured while selecting option " +ele.getAttribute("id"));
			//ex.printStackTrace();
			Thread.sleep(2000);
			 List<WebElement> options = ele.findElements(By.tagName("li"));
		        // Loop through the options and select the one that matches
		        for (WebElement opt : options) {
		        	WebElement optLink = opt.findElement(By.tagName("a"));
		            if (optLink.getText().equals(option)) {
		            	optLink.click();
		                break;
		            }
		        }
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
		}
	       // throw new NoSuchElementException("Can't find " + option + " in dropdown");
	
	

	public static List<String> compareStrings(List<String> actList, List<String> expList)
	{
		//List list3 = new ArrayList();
		List<String> unmatchedList = new ArrayList<String>(); 
		int actsize = actList.size();
		int expsize = expList.size();

		if ((actsize)==(expsize))
		{
			for(int i=0;i<actsize;i++){
				if(actList.contains(expList.get(i))){
					//System.out.println("Expected list content exists in Actual : "+expList.get(i));
				}else{
					System.out.println("Expected list content not exists in Actual : "+expList.get(i));
					unmatchedList.add(expList.get(i));
				}
			}
		}
	
		else 
		{
			System.out.println("Size of Actual List is not matching with Expected List size. Actual list size:"+actsize+". Expected List size is: "+expsize);
			unmatchedList.add("Size of Actual List is not matching with Expected List size.");
		}


		return unmatchedList;

	}
	
	public static Boolean VerifyProgressIndicator(WebElement ele) throws InterruptedException {
		Thread.sleep(3000);
		boolean TickMarkflag = false;
		boolean ispresent = false;
		
		try{
			ispresent = ele.isEnabled();
			System.out.println(ispresent);
			if(ispresent)	
			{
				if(ele.findElement(By.tagName("a")).isDisplayed())

				{
					///WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@id='"+ActualidTick+"']/i"));
					//scrolltoElement(driver, TickMarkOntab);
					TickMarkflag = true;

				}


			}
		}

		catch(NoSuchElementException e)
		{
			System.out.println("No Menu Option is enabled.  " +e);
		}
		return TickMarkflag;
	}
	
public static void TakeScreenshots(WebDriver driver, String ClassName,String methodName, String Status) throws InterruptedException, IOException 
	{
		
		try{
				
		/*	String className = getClass().getSimpleName();
			System.out.println(className);*/
			
			   File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        //    String filePath = System.getProperty("user.dir") + "\\Evidence\\";
	            String filePath = createDirectory() + "\\";
	            String filepathwithName = filePath + "["+ClassName+"_"+methodName+"_"+Status+"].png";
	            System.out.println(filepathwithName);
	            FileUtils.copyFile(scrfile,new File(filepathwithName),true );
			
		}

		catch(NoSuchElementException e)
		{
			System.out.println("The Screenshots are not happen" +e);
		}
	 
	}

public static void TakeScreenshots(WebDriver driver, String ClassName) throws InterruptedException, IOException 
{
	
	try{
			
	/*	String className = getClass().getSimpleName();
		System.out.println(className);*/
		
		   File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //    String filePath = System.getProperty("user.dir") + "\\Evidence\\";
            String filePath = createDirectory() + "\\";
            String filepathwithName = filePath + "["+ClassName+"].png";
            System.out.println(filepathwithName);
            FileUtils.copyFile(scrfile,new File(filepathwithName),true );
		
	}

	catch(NoSuchElementException e)
	{
		System.out.println("The Screenshots are not happen" +e);
	}
 
}
	
	private static String createDirectory()
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
	


	public static String getTodayDate(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("dd/MM/yyyy");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static String getTodayDate_Format(String date , String Pattern){
		
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern(Pattern);
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static String getTodayDate_DotFormat(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("dd.MM.yyyy");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static String getTodayDate_DashFormat(){
		String date;
		LocalDateTime DT = LocalDateTime.now();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("yyyy-MM-dd");
		date = DT.format(formatter).toString();
		System.out.println("Date is : "+date);
		return date;

	}
	
	public static boolean AllElementDisabled(WebDriver driver, String type)
	{
	boolean isenabled = false;
	try {
		Thread.sleep(2000);
		List<WebElement> Elements=driver.findElements(By.xpath("//input[@type='"+type+"']"));
		System.out.println("total WebElements "+Elements.size());
		for (WebElement Element : Elements)
		{  
			if(Element.isEnabled())
			{
				isenabled = true;
			}
		}
	}
	catch(Exception e){
		System.out.println("The Element is not foount whose type is "+type);
	}
	return isenabled;
	}
		public static String getdate(String Date) {
		String[] fields = Date.split("[ \\/]");
		String CalendDate = fields[1];
		String CalenderDate = CalendDate.replaceFirst("^0+(?!$)", "");
		return CalenderDate;  
	}
	
		public static String getMonth(String Date) {
		String[] fields = Date.split("[ \\/]");
		String CalenderMonth = fields[0];
		return CalenderMonth;  
		}

			public static String getYear(String Date) {
		String[] fields = Date.split("[ \\/]");
		String CalenderYear = fields[2];
		return CalenderYear;
			}

		
	// Copy required value to clipboard
	public static void copyToClipbord(String copyTo)
	{
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	StringSelection str = new StringSelection(copyTo);
	clipboard.setContents(str, null );
	}


	// Paste into required text field
	public static void setText(WebElement element, String value)
	{
	copyToClipbord(value);
	element.click();
	element.sendKeys(value);
	//element.sendKeys(Keys.chord(Keys.CONTROL, "v"), "");
	}
	
	//This method can be used for verifying a message on web page by reading a data through excel file
	public static boolean verifyMessageWithExcel(String actualMsg,String file, String sheet, String key, int colNumber) throws InterruptedException {
		String expMsg= ExcelUtilities.getKeyValueFromExcelWithPosition(file, sheet, key, colNumber);
		boolean isMessageVerified=false;
			if(expMsg.equalsIgnoreCase(actualMsg)){
				isMessageVerified=true;
				//assertEquals(actualMsg, expMsg, "Messages are not matching, expected is: "+expMsg+", actual is: "+actualMsg);
				System.out.println("For a file: "+file+", messsage is matching for key: "+key +" under a sheet "+sheet);
				
			}else{
				isMessageVerified=false;
				System.out.println("For a file: "+file+", messsage is not matching for key: "+key +" under a sheet "+sheet);
			}
		return isMessageVerified;
	}
	
	
	
/*	// This method is added by Akshay to select a RLT under Select Contractor window
	public static List<String> tickSelectedRLTFromList(List<WebElement> listOfRLT, String rltName, WebElement modalWindowHeader)
	{
		int rltCount= listOfRLT.size();
		List<String> tempData= new LinkedList<String>();
		String elementName="";
		WebElement element = null;
		try{
		
			String[] rltArray= rltName.split(",");

			for (String tempArray : rltArray) 
			{
				
				for (int i=0; i<rltCount; i++)
				{
					element= listOfRLT.get(i);
					elementName= element.getText().toString().trim();
					if(elementName.equalsIgnoreCase(tempArray.trim()))
					{
						element.click();
						System.out.println("RLT name "+tempArray+" is selected from a drop down list");
						tempData.add(elementName);
						break;
					}	
					
				}
					
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return tempData;
	}


	// This method is added by Akshay to select a CCG under Select Contractor window
	public static List<String> tickSelectedCCGFromList(List<WebElement> finalCCGList, String ccgName) {
		int ccgCount= finalCCGList.size();
		int finalCCGCount= finalCCGList.size();
		List<String> tempData= new LinkedList<String>();
		String elementName="";
		WebElement element = null;
		try{

			String[] ccgArray= ccgName.split(",");
			
				for(int i=0;i<ccgArray.length;i++){
					
					String ccgvalue= ccgArray[i].toString().trim();
					
					for(int j=0; j<finalCCGCount;j++)
					{
						element= finalCCGList.get(j);
						elementName= element.getText().toString().trim();
						if(elementName.equalsIgnoreCase(ccgvalue))
						{
							element.click();
							System.out.println("CCG name "+ccgvalue+" is selected from a drop down list");
							tempData.add(elementName);
							break;
						}	
						
					}
					
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}
		return tempData;
	}

	// This method is added by Akshay to select a practice under Select Contractor window
	public static List<String> tickSelectedPracticeFromList(List<WebElement> finalPracticeList, String practiceName) {
		int practiceCount= finalPracticeList.size();
		int finalPracticeCount= finalPracticeList.size();
		List<String> tempData= new LinkedList<String>();
		String elementName="";
		WebElement element = null;
		
		try{
			String[] practiceArray= practiceName.split(",");
			
			for(int i=0;i<practiceArray.length;i++){
				
				String practicevalue= practiceArray[i].toString().trim();
				
				for(int j=0; j<finalPracticeCount;j++)
				{
					element= finalPracticeList.get(j);
					elementName= element.getText().toString().trim();
					if(elementName.equalsIgnoreCase(practicevalue))
					{
						element.click();
						System.out.println("Practice name "+practicevalue+" is selected from a drop down list");
						tempData.add(elementName);
						break;
					}	
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		
		return tempData;
	}*/
	
	public static String getCurrentHourMin()
	{
		String tm;
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HHmm");
		System.out.println(format.format(date));
		tm = format.format(date);
		return tm;
	}
	
	public static String generateRandomNo(int num){

		Random random=new Random();

		boolean loop=true;
		while(loop) {
			randomNumber=random.nextInt();
			if(Integer.toString(randomNumber).length()==num && !Integer.toString(randomNumber).startsWith("-")) {
				loop=false;
			}
		}


		number = String.valueOf(randomNumber);
		return number;
	}
	// this function can be used to convert any number in desired currency format. E.g. to convert it in UK format, pass arguments as "en","GB"
	public static String convertToCurrencyFormat(String strValue, String country, String currency, boolean isUSFormat) {
		BigDecimal amount= new BigDecimal(strValue);
		if(isUSFormat){
			NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
			strValue= defaultFormat.format(amount);
		}else{
			Locale locale = new Locale(country, currency);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			strValue= numberFormat.format(amount);
		}
		System.out.println("converted value is: "+strValue);
		
		return strValue;
	}
	
	// This method can be used to break a list in multiple lists of different sizes. All child list should be of Object type
	public static List<List<Object>> breakListInDifferentLists(List<Object> finalList, List<Integer> listSize) {
		List<List<Object>> multiList= new ArrayList<List<Object>>();
		List<Object> loopList= new ArrayList<Object>();
		int fromIndex=0;
		try{
			outerloop:
			for(int i=0;i<finalList.size();i++){
				
				loopList= finalList.subList(fromIndex, fromIndex+listSize.get(i));
				fromIndex= fromIndex+listSize.get(i);
				//toIndex= fromIndex;
				System.out.println("List after breaking is: "+loopList);
				multiList.add(loopList);
				
				if(multiList.size()==listSize.size()){
					break outerloop;
				}
			}
		}catch(Exception e){
			System.out.println("Loop ended for breaking a list");
			}
		return multiList;

	}
	
	public static void selectOptionFromDropDownAtIndex(WebElement ele, int index)
	{
		Select dropdown = new Select(ele);
		dropdown.selectByIndex(index);
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		System.out.println("Dropdown Value Selected:  "+selectedValue);
	}
	
	
	public static void ReadXML_UpdateCodeValue(String Filename,String Nodename,String AttributeName,String UpdatedXMLValue, String key) throws FileNotFoundException {
		String Key = null;
		try{
			
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;		
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//"+Nodename);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			int randomNumber = 0;
			for (int i = 0; i < nl.getLength(); i++)
			{
			    org.w3c.dom.Node currentItem = nl.item(i);
			  
			    Key = currentItem.getAttributes().getNamedItem(AttributeName).getNodeValue();
			    currentItem.getAttributes().getNamedItem(AttributeName).setTextContent(UpdatedXMLValue);
			}
			TransformerFactory transformerFactory = 
			          TransformerFactory.newInstance();
		Transformer transformer=
	                         transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result=new StreamResult(new File(filePath));
		transformer.transform(source, result);
		/*StreamResult consoleResult = new StreamResult(System.out);
		transformer.transform(source, consoleResult);*/
		
		}
		catch (FileNotFoundException exception)
		{
		// This needs to rewrite...
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			String newremark = remark+", No File is found";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "", key, "REMARKS"); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
		public static void BULKReadXML_UpdateCodeValue(String Filename,String Nodename,String AttributeName,String UpdatedXMLValue, String key, String MainFile) throws FileNotFoundException {
		String Key = null;
		try{
			
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;		
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//"+Nodename);
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			int randomNumber = 0;
			for (int i = 0; i < nl.getLength(); i++)
			{
			    org.w3c.dom.Node currentItem = nl.item(i);
			  
			    Key = currentItem.getAttributes().getNamedItem(AttributeName).getNodeValue();
			    currentItem.getAttributes().getNamedItem(AttributeName).setTextContent(UpdatedXMLValue);
			    System.out.println("The Attribute "+AttributeName+" value is set to:  "+UpdatedXMLValue);
			}
			TransformerFactory transformerFactory = 
			          TransformerFactory.newInstance();
		Transformer transformer=
	                         transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result=new StreamResult(new File(filePath));
		transformer.transform(source, result);
		/*StreamResult consoleResult = new StreamResult(System.out);
		transformer.transform(source, consoleResult);*/
		
		}
		catch (FileNotFoundException exception)
		{
			// This needs to rewrite...
			String remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			String newremark = remark+", No File is found";
			ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "", key, "REMARKS"); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	public static void ReadXML_UpdateCodeValue_Attribute(String XMLType, String Filename, String SheetName, String AttributeName, String key) throws FileNotFoundException {
			String UpdatedAttributeValue = null;
			org.w3c.dom.Node employee = null;
		NodeList nodes = null;
		NodeList node = null;
		String TestDataNINumber = null;
		try {
			System.out.println(Filename);
			String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", SheetName, AttributeName,1);
			List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);          
            Iterator iter = NodePaths.iterator();
            while (iter.hasNext())
            {
               	employee = document.getElementsByTagName((String) iter.next()).item(0); 
            }         	 
            nodes = employee.getChildNodes();
            System.out.println(nodes);
            Pattern p = Pattern.compile(".*,\\s*(.*)");
            Matcher m = p.matcher(AttributeName);

            if (m.find())
            {
            	AttributeName = m.group(1);
            	System.out.println(m.group(1));
            }
            for (int i = 0; i < nodes.getLength(); i++) 
              {            	
            	if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
            	{
            		org.w3c.dom.Node element =  nodes.item(i);
            		System.out.println(element.getNodeName());
            		if (AttributeName.equals(element.getNodeName()))
                {
                	switch(AttributeName) 
                	{
                	case "DOB": 
                	    String age = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		if(Filename.contains("BS_"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
                		}
                		System.out.println("The XML DOB is "+UpdatedAttributeValue);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Surname": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "First-Names": 
                		String FirstName = "Automation";
                		String HrMin = CommonFunctions.getCurrentHourMin();
                    	String userID = FirstName+HrMin;
                    	utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", userID, AttributeName, key);
                    	Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "PostCode": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NHSNumber": 
                		String NHSNUmber = generateValidNHSNo();
                		utilities.ExcelUtilities.setKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", NHSNUmber, AttributeName, key);
                		Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
					break;
                	case "NINumber":
                		TestDataNINumber = element.getTextContent();
                		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Under16":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Over60":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Voucher-Type":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Complex":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Prism":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Tint":
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "GOS3-Voucher-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "GOS3-Authorisation-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
						
                	
                }
                }
                    
                } 
            

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was updated ");

        } catch (ParserConfigurationException pce) {

          pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }

    

		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	
	
	public static void BULKReadXML_UpdateCodeValue_Attribute(String XMLType, String Filename, String SheetName, String AttributeName, String key, String MainFile) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
	NodeList nodes = null;
	NodeList node = null;
	NodeList node1 = null;
	String TestDataNINumber = null;
	String ThirdLastAttribute = null;
	String AttributePrevious = null;
	String SecondLastAttribute=null;
	int AttributeSize=0;
	try {
		System.out.println(Filename);
		String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, SheetName, AttributeName,1);
		List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
		String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(filePath);
      
        Iterator iter = NodePaths.iterator();
        while (iter.hasNext())
        {
           	employee = document.getElementsByTagName((String) iter.next()).item(0); 
        }         	 
        nodes = employee.getChildNodes();
        /*System.out.println(nodes);
        AttributePrevious = AttributeName;*/
        AttributePrevious = AttributeName;
        if(AttributeName.contains(","))
        {
    		List<String> NodeMatches = Arrays.asList(AttributeName.split("\\s*,\\s*"));
        	AttributeSize = NodeMatches.size();
        	System.out.println(NodeMatches.size());
        	if(AttributeSize==4){
        		ThirdLastAttribute= NodeMatches.get(AttributeSize - 3);
            	SecondLastAttribute = NodeMatches.get(AttributeSize - 2);
            	AttributeName = NodeMatches.get(AttributeSize - 1);
        	}else{
        		SecondLastAttribute = NodeMatches.get(AttributeSize - 2);
            	AttributeName = NodeMatches.get(AttributeSize - 1);
        	}
        }
        else
        {
        	SecondLastAttribute = AttributeName;
        }
    
        for (int i = 0; i < nodes.getLength(); i++) 
          {            	
        	if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
        	{
        		org.w3c.dom.Node element =  nodes.item(i);
        		System.out.println(element.getNodeName());
        		if(AttributeSize==4){
            		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(ThirdLastAttribute))
            		{
            			node1 = element.getChildNodes();
            			secondAttrLoop:
    					for (int a=0; a<node1.getLength(); a++)
    					{
    						if((node1.item(a).getNodeType() == Node.ELEMENT_NODE))
    			        	{
    			        		element =  node1.item(a);
    			        		System.out.println(element.getNodeName());
    			        		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(SecondLastAttribute))
    		            		{
    		    			   		node1 = element.getChildNodes();
    		    					for (int index=0; index<node1.getLength(); index++)
    		    					{
    		    						if((node1.item(index).getNodeType() == Node.ELEMENT_NODE))
    		    						{
    		    						//	System.out.println(node1.item(a).getNodeName());
    		    							if (node1.item(index).getNodeName().equalsIgnoreCase(AttributeName))
    		    							{       				
    		    								element = node1.item(index);
    		    								break secondAttrLoop;
    		    							}
    		    						}
    		    		        	}
    		    					System.out.println(element.getNodeName());
    		            		}
    			        	}	
    					}
            		}
        		}else{
            		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(SecondLastAttribute))
            		{
    			   		node1 = element.getChildNodes();
    					for (int a=0; a<node1.getLength(); a++)
    					{
    						if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
    						{
    						//	System.out.println(node1.item(a).getNodeName());
    							if (node1.item(a).getNodeName().equalsIgnoreCase(AttributeName))
    							{       				
    								element = node1.item(a);
    								break;
    							}
    						}
    		        	}
    					System.out.println(element.getNodeName());
            		}
        		}
        	//	System.out.println(element.getNodeName());
        		if (AttributeName.equals(element.getNodeName()))
            {
            	switch(AttributeName) 
            	{
            	case "DOB": 
            	    String age = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
            		}
            		System.out.println("The XML DOB is "+UpdatedAttributeValue);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The DOB is updated: "+UpdatedAttributeValue);
            		break;
            	case "Title": 
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Surname": 
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The Surname is updated: "+UpdatedAttributeValue);
            		break;
            	case "First-Names": 
            		String FirstName = "Automation";
            		String HrMin = CommonFunctions.getCurrentHourMinSeconds();
                	String userID = FirstName+HrMin;
                	//utilities.ExcelUtilities.setKeyValueinExcelWithPosition(MainFile, SheetName, AttributeName, userID, 2);
                	//utilities.ExcelUtilities.setKeyValueByPosition(MainFile, SheetName, userID, AttributeName, key);
                	Thread.sleep(3000);
            		//UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		UpdatedAttributeValue = userID;
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The First-Names is updated: "+UpdatedAttributeValue);
            		break;
            	case "PostCode": 
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The Postcode is updated: "+UpdatedAttributeValue);
            		break;
            	case "NHSNumber": 
            		String NHSNUmber = generateValidNHSNo();
            		//utilities.ExcelUtilities.setKeyValueByPosition(MainFile, SheetName, NHSNUmber, AttributeName, key);
            		Thread.sleep(3000);
            		//UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, SheetName, AttributeName, 2);
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(NHSNUmber);
            		System.out.println("The NHSNumber is updated: "+NHSNUmber);
				break;
            	case "NINumber":
            		TestDataNINumber = element.getTextContent();
            		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The NINumber is updated: "+UpdatedAttributeValue);
            		break;
 
				case "Private-Test-Cost-Pounds":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The Private-Test-Cost-Pounds value is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "HC3-Certificate-Number":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The HC3-Certificate-Number value is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "FirstPatientAtAddress":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The FirstPatientAtAddress flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "SecondPatientAtAddress":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The SecondPatientAtAddress flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "ThirdPatientAtAddress":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The ThirdPatientAtAddress flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "Pounds":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributePrevious, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The Pounds value is updated: "+UpdatedAttributeValue);
            		break;

					case "Under16":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Over60":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Voucher-Type":       		
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
              	case "Complex":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Prism":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Tint":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "GOS3-Voucher-Code":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
            	case "GOS3-Authorisation-Code":
        			UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
        			if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
        			break;					
				case "Eligibility-HC-Certificate":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;	
				case "Eligibility-HC-Certificate-Type":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-HC-Certificate-Number":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-HC3-Voucher-Reduction":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Prescribed-Complex-Lenses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Sph":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Cyl":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Axis":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Base":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Distance-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Bifocals-Varifocals":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue.toUpperCase());
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Small-Glasses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Small-Glasses-Measurement":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Special-Facial-Characteristics":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Prism-Controlled-Bifocals":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-First-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-Second-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-Total":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-First-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-Second-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-Total":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Contribution":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				
				case "Total-Claim":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date-First-Pair-Supplied":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            			if(XMLType.contains("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
                		}
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date-Second-Pair-Supplied":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            			if(XMLType.contains("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
                		}
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Last-Sight-Test"://||XMLType.equalsIgnoreCase("PMS_GOS1")
        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
        		if(XMLType.equalsIgnoreCase("BS_GOS1")||XMLType.equalsIgnoreCase("BS_GOS5")||XMLType.equalsIgnoreCase("PMS_GOS5")||XMLType.equalsIgnoreCase("PMS_GOS1")){
    				element.setTextContent(UpdatedAttributeValue);
        		}else{
	        		if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
            		}
        		}	
        		element.setTextContent(UpdatedAttributeValue);
        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
					break;
				case "Eligibility-Evidence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
			
				case "FullTimeStudent16_17_18":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				
				case "Eligibility-Diabetes":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Glaucoma":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Risk-of-Glaucoma":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Registered-Blind-With-LA":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
			
			
			
				
				case "Near-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
            		
				case "FirstTest":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "NotKnown":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "Cannot-Attend-Because":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		break;
            		
				case "IReceive":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
        		
				case "ReceiveEmploymentSupportAllowance":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "MyPartnerReceives":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "ReceiveUniversal":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "ReceiveIncomeSupport":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "ReceivePensionCreditGuarantee":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "ReceiveIncomeJobseekersAllowance":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "ReceiveTaxCredit":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Eligibility-Valid-HC2":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Eligibility-HC2-Certificate-Number":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Person-Getting-Benefit-Name":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}else{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Person-Getting-Benefit-NINumber":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}else{	
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Person-Getting-Benefit-DOB":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}else{
            			if(XMLType.equalsIgnoreCase("BS_GOS1")||XMLType.equalsIgnoreCase("BS_GOS5")||XMLType.equalsIgnoreCase("PMS_GOS5")){
            				element.setTextContent(UpdatedAttributeValue);
            			}else{
        	        		if(XMLType.contains("BS"))
                    		{
                    			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(UpdatedAttributeValue , "ddMMyyyy");
                    		}
                    		else
                    		{
                    			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(UpdatedAttributeValue , "yyyy-MM-dd");
                    		}
            			}
    	        		element.setTextContent(UpdatedAttributeValue);
    	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Patient-Declaration-Date":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
            		}
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Mixed-WhiteBlackCaribbean":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "SightTestDate":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
					if(XMLType.contains("BS"))
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
	        		}
	        		else 
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
	        		}
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Domiciliary-Visit-To-Conduct-Sight-Test":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Domiciliary-Visit-To-Several-Patients":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Patient-Was-Substituted":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Domicilary-Fee-3rdOrSubsequentPatient":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Domicilary-Fee-1stOr2ndPatient":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "NHS-Sight-Test-Fee":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Contractor-Declaration-Date"://cont decl date
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        			if(XMLType.contains("BS"))
	            		{
	            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
	            		}
	            		else 
	            		{
	            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
	            		}
	            		element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
	        		break;
	        		
				case "Over40andParent_Brother_Sister_Child":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
        		
				case "Eligibility-Prisoner":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
        		
				case "Eligibility-Establishment-Name":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}else{
            			element.setTextContent(UpdatedAttributeValue);
    	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Eligibility-Establishment-Town":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}else{
            			element.setTextContent(UpdatedAttributeValue);
    	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
            		}
	        		break;
				
				case "Patient":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Tested-On-Date":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            			if(XMLType.contains("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
                		}
	            		element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		break;
	        		
				case "Several-Patients":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Patient-Added-Substituted":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
	        		
				case "Domicilary-Fee":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
				case "Previous-Surname":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Voucher-Issued":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Declaration-Name":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Declaration-Address":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Declaration-Postcode":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "New-Changed-Prescription-Issued":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Referred-GP-Ophthalmic-Hospital":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Statement-Showing-No-Prescription-Required":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Unchanged-Prescription-Issued":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Performer-Declaration-Date":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            			/*int days= Integer.parseInt(UpdatedAttributeValue);
    					Date date= CommonFunctions.getDateAfterDays(days);
    					UpdatedAttributeValue= CommonFunctions.convertDateToString(date, "yyyy-MM-dd");*/
            			if(XMLType.contains("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
                		}
	            		element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Partner":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "First-Voucher-Type":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Second-Voucher-Type":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Benefit":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Identity":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Private-Test-Cost":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Will-Pay-Amount":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Cannot-Attend-Reason":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Declaration-Identity":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Number-At-Address":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Private-Charge-NHS-Sight-Test":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patients-Contribution":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Max-Claimable":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Private-Charge-NHS-Domicilary-Visit":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Address":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Under18andLocalAuthorityCare":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Loss-Damage-Explanation":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "NHS-England-Approval":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "NHS-England-Approval-Code":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Type":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Repaired-Replaced":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Lens-CL":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Frame":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Voucher-Value":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Lens-Value":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Frame-Value":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Supplement-Value":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Plus-Supplements":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Parts-Plus-Supplements":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Explain-Loss-Damage":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date-of-Prescription":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            			String prescriptionDate="";
            			if(XMLType.contains("BS"))
                		{
            				int days= Integer.parseInt(UpdatedAttributeValue);
        					Date date= CommonFunctions.getDateBeforeDays(days);
        					prescriptionDate= CommonFunctions.convertDateToString(date, "ddMMyyyy");
                		}
                		else 
                		{
                			int days= Integer.parseInt(UpdatedAttributeValue);
        					Date date= CommonFunctions.getDateBeforeDays(days);
        					prescriptionDate= CommonFunctions.convertDateToString(date, "yyyy-MM-dd");
                		}
	            		element.setTextContent(prescriptionDate);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Performer-Name":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Performer-List-Number":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Encoded-Data":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Sight-Test-Ref":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Second-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            			if(XMLType.contains("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
                		}
	            		element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retest-Code":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "DateOfLastTest":
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(XMLType.equalsIgnoreCase("BS_GOS1")||XMLType.equalsIgnoreCase("BS_GOS5")||XMLType.equalsIgnoreCase("PMS_GOS5")){
        				element.setTextContent(UpdatedAttributeValue);
	        		}else{
		        		if(XMLType.contains("BS"))
	            		{
	            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
	            		}
	            		else 
	            		{
	            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
	            		}
	        		}	
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The "+AttributeName+" flag is updated: "+UpdatedAttributeValue);
	        		break;
				case "Seen":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "NotSeen":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Parent":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "CarerGuardian":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "White-Other":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "White-Irish":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "White-British":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Other-Other":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Other-NotStated":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Other-Chinese":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Mixed-WhiteBlackAfrican":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Mixed-WhiteAsian":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Mixed-Other":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Black-Other":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Black-Caribbean":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Black-African":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Asian-Pakistani":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Asian-Other":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Asian-Indian":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Asian-Bangladeshi":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retest-CodeMajor":
				UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
				if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
        			break;
        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
        			UpdatedAttributeValue="";
        			element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
        		}
        		else
        		{
        		element.setTextContent(UpdatedAttributeValue);
        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
        		}
					break;
				case "Retest-CodeMinor":
				UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
				if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
        			break;
        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
        			UpdatedAttributeValue="";
        			element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
        		}
        		else
        		{
        		element.setTextContent(UpdatedAttributeValue);
        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
        		}
					break;
				case "Eligibility-Valid-HC3":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
	        			UpdatedAttributeValue="";
	        			element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Eligibility-HC-Certificate-Number-Prefix":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
	        			UpdatedAttributeValue="";
	        			element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Eligibility-HC3-Voucher-Reduction-Pounds":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
	        			UpdatedAttributeValue="";
	        			element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Eligibility-HC3-Voucher-Reduction-Pence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
	        			UpdatedAttributeValue="";
	        			element.setTextContent(UpdatedAttributeValue);
	            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Sph-PosNeg":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Sph-Integral":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Sph-Fractional":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Cyl-PosNeg":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Cyl-Integral":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Cyl-Fractional":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Prism-Integral":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Prism-Fractional":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Glasses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "ContactLenses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "NewChangedPrescription":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "UnchangedPrescription":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "SmallGlasses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Pence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Private-Test-Cost-Pence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Cannot-Attend":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "RetestCodeIntegral":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "RetestCodeFractional":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
						break;
				case "Near":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
					break;
				case "Cyl-Axis":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	        			break;
	        		}
					break;
				case "Patient-Declaration-Bulk-Scan-Signature-Reference":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Approved":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "NotApproved":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "DistancePair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "NearPair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "BifiocalContactLenses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Repaired":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Replaced":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Patient-Declaration-PostCode":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "VoucherValuePounds":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case  "VoucherValuePence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Right":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Left":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Both":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "ValuePounds":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "ValuePence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Front":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Side":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Whole":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
	        		else
	        		{
		        		element.setTextContent(UpdatedAttributeValue);
		        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
	        		default://default case for BSPMS processing
	        			System.out.println("The "+AttributeName+" is not found to update.");
            	
            	}
            }
                
        	} 
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));
        transformer.transform(domSource, streamResult);
        System.out.println("The XML File was updated ");

    } catch (ParserConfigurationException pce) {

      pce.printStackTrace();

    } catch (TransformerException tfe) {

        tfe.printStackTrace();

    } catch (IOException ioe) {

        ioe.printStackTrace();

    } catch (SAXException sae) {

        sae.printStackTrace();

    }
	catch (Exception e)
	{
		e.printStackTrace();
	}
	
		//return Key;
}
	public static void ReadXML_UpdateCodeValue_Attribute(String MainFile,String XMLType, String Filename, String SheetName, String AttributeNamePrevious, String key) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
	NodeList nodes = null;
	NodeList node = null;
	NodeList node1 = null;
	String TestDataNINumber = null;
	String AttributeNameNew = null;
	String AttributePrevious = null;
	String SecondLastAttribute = null;
	try {
		String AttributeName = AttributeNamePrevious;
		System.out.println(Filename);
		String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, SheetName, AttributeName,1);
		List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
		String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(filePath);      
        Iterator iter = NodePaths.iterator();
        while (iter.hasNext())
        {
           	employee = document.getElementsByTagName((String) iter.next()).item(0); 
        }         	 
        nodes = employee.getChildNodes();
   //     System.out.println(nodes);
        AttributePrevious = AttributeName;
        if(AttributeName.contains(","))
        {
        	List<String> NodeMatches = Arrays.asList(AttributeName.split("\\s*,\\s*"));
        	int AttributeSize = NodeMatches.size();
        	System.out.println(NodeMatches.size());
        	SecondLastAttribute = NodeMatches.get(AttributeSize - 2);
        	AttributeName = NodeMatches.get(AttributeSize - 1);
        	
        }
        else
        {
        	SecondLastAttribute = AttributeName;
        }
      
        
    /*    if(AttributeName.contains(","))
        {
        	Pattern p = Pattern.compile(".*,\\s*(.*)");
        	Matcher m = p.matcher(AttributeName);
        	if (m.find())
        	{
        		AttributeName = m.group(1);
        		AttributeNameNew = AttributeName;
        		System.out.println(m.group(1));
        	}
        }
	    else
	    {
	    	AttributeNameNew = AttributeName;
	    }*/
 //       System.out.println(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) 
          {            	
        	if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
        	{
        	   
        		org.w3c.dom.Node element =  nodes.item(i);
        		System.out.println(element.getNodeName());
        		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(SecondLastAttribute))
        		{
        			   		node1 = element.getChildNodes();
        					for (int a=0; a<node1.getLength(); a++)
        					{
        						
        							if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
        							{
        							//	System.out.println(node1.item(a).getNodeName());
        								if (node1.item(a).getNodeName().equalsIgnoreCase(AttributeName))
        								{       				
        									element = node1.item(a);
        								}
        							}
        			        	}
        					
        			
        			
        		
        		
        		System.out.println(element.getNodeName());
        		}
        		
        	
        	if (AttributeName.equals(element.getNodeName()))
            {
            	switch(AttributeName) 
            	{
              	case "Address":
            	    UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Title":
            	    UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "DOB": 
            	    String age = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
            		}
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		
            		break;
            	case "Surname":
            	    UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "First-Voucher-Type" :
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            			 org.w3c.dom.Node parent = element.getParentNode();

            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		/*UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);*/
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		break;
            case "First-Names": 
            		String FirstName = "Automation";
            		String HrMin = CommonFunctions.getCurrentHourMin();
                	String userID = FirstName+HrMin;
                	utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues" ,userID, AttributeName, key);
                	Thread.sleep(3000);
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "PostCode": 
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            case "NHSNumber": 
            		String NHSNUmber = generateValidNHSNo();
            		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues", NHSNUmber, AttributeName, key);
            		Thread.sleep(3000);
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "UpdateTagValues", AttributeName, 2);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            	case "NINumber":
            		TestDataNINumber = element.getTextContent();
            		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Under16":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Over60":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Voucher-Type":            		
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
              	case "Complex":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Prism":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "Tint":
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		break;
            	case "GOS3-Voucher-Code":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
            	case "GOS3-Authorisation-Code":
        			UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
        			if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
        			break;					
				case "Eligibility-HC-Certificate":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;	
				case "Eligibility-HC-Certificate-Type":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-HC-Certificate-Number":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-HC3-Voucher-Reduction":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Prescribed-Complex-Lenses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Sph":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Cyl":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Axis":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Base":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Distance-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Bifocals-Varifocals":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}else if(UpdatedAttributeValue.equalsIgnoreCase("Blank")){
            			UpdatedAttributeValue="";
            			element.setTextContent(UpdatedAttributeValue);
                		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Small-Glasses":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Small-Glasses-Measurement":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Special-Facial-Characteristics":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Prism-Controlled-Bifocals":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-First-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-Second-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Retail-Cost-Total":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-First-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-Second-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Vouchers-Supplements-Total":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Patient-Contribution":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "ReceiveUniversal":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;			
				case "Total-Claim":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date-First-Pair-Supplied":
					if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
            		}
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Date-Second-Pair-Supplied":
					if(XMLType.contains("BS"))
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
            		}
            		else 
            		{
            			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
            		}
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Last-Sight-Test":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Evidence":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Over40andParent_Brother_Sister_Child":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "FullTimeStudent16_17_18":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Prisoner":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Diabetes":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Glaucoma":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Risk-of-Glaucoma":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Registered-Blind-With-LA":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Establishment-Name":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Eligibility-Establishment-Town":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "ReceiveIncomeSupport":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "ReceivePensionCreditGuarantee":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
				case "Near-Pair":
					UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            			break;
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
					break;
            	}
        			
        	/*	if(AttributeName.equalsIgnoreCase("DOB"))
        		{
        			  String age = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
              		if(XMLType.contains("BS"))
              		{
              			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
              		}
              		else 
              		{
              			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
              		}
              		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
              		{
              				org.w3c.dom.Node parent = element.getParentNode();
              			    parent.removeChild(element);
              			    parent.normalize();
              			
              		}
              		else
              		{
              		element.setTextContent(UpdatedAttributeValue);
              		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
              		}
        		}
        		else if(AttributeName.equalsIgnoreCase("First-Names"))
        		{
        			String FirstName = "Automation";
            		String HrMin = CommonFunctions.getCurrentHourMin();
                	String userID = FirstName+HrMin;
                	utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues" ,userID, AttributeName, key);
                	Thread.sleep(3000);
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
            		
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
        		}
        		else if(AttributeName.equalsIgnoreCase("NHSNumber"))
        		{
        			String NHSNUmber = generateValidNHSNo();
            		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues", NHSNUmber, AttributeName, key);
            		Thread.sleep(3000);
            		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "UpdateTagValues", AttributeName, 2);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
               		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
        		}
        		else if(AttributeName.equalsIgnoreCase("NINUMBER"))
        		{
        			TestDataNINumber = element.getTextContent();
            		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
            		if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();
               		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
            		
        		}
        		else 
        		{
        			
        			UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributePrevious, key);
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
            		{
            				org.w3c.dom.Node parent = element.getParentNode();
            			    parent.removeChild(element);
            			    parent.normalize();           		
            		}
            		else
            		{
            		element.setTextContent(UpdatedAttributeValue);
            		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
            		}
        		}*/
              }                
           } 
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));
        transformer.transform(domSource, streamResult);
   //     System.out.println("The XML File was updated with  " + AttributeName);

    } catch (ParserConfigurationException pce) {

      pce.printStackTrace();

    } catch (TransformerException tfe) {

        tfe.printStackTrace();

    } catch (IOException ioe) {

        ioe.printStackTrace();

    } catch (SAXException sae) {

        sae.printStackTrace();

    }
	catch (Exception e)
	{
		e.printStackTrace();
	}
}
	
	
	public static void ReadXML_UpdateCodeValue_Attribute_Test(String Filename, String DataFileName, String SheetName, String AttributeName, String key, int days) throws FileNotFoundException {
		String Key = null;
		String TestDataNINumber = null;
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employeenode = null;
		org.w3c.dom.NodeList employee = null;
		try {
			System.out.println(Filename);
			String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName,1);
			List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Iterator iter = NodePaths.iterator();
            while (iter.hasNext())
            {
            	
            	employee = document.getElementsByTagName((String) iter.next());         
            }
           for (int j=0; j<employee.getLength();j++)
           {
            NodeList nodes = employee.item(j).getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) 
            {
               org.w3c.dom.Node element =  nodes.item(i);
                System.out.println(element.getNodeName());
                if (AttributeName.equals(element.getNodeName()))
                {
                	switch(AttributeName) 
                	{
                	case "DOB": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Surname": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "First-Names": 
                		String FirstName = "Automation";
                		String HrMin = CommonFunctions.getCurrentHourMin();
                    	String userID = FirstName+HrMin;
                    	utilities.ExcelUtilities.setKeyValueinExcelWithPosition(DataFileName, SheetName, AttributeName, userID, 2);
                    	Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "PostCode": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NHSNumber": 
                		String NHSNUmber = generateValidNHSNo();
                		utilities.ExcelUtilities.setKeyValueinExcelWithPosition(DataFileName, SheetName, AttributeName, NHSNUmber, 2);
                		Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
					break;
                	case "NINumber":
                		TestDataNINumber = element.getTextContent();
                		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
					case "GOS3-Voucher-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "GOS3-Authorisation-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "Notification-Date": 
                		//UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		UpdatedAttributeValue = CommonFunctions.getTodayDate_DashFormat();
                		System.out.println("Notification-Date: "+UpdatedAttributeValue);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                		
					case "Date-Of-Visit": 
						Date dt = CommonFunctions.getDateAfterDays(days);
						//UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(DataFileName, SheetName, AttributeName, 2);
                		UpdatedAttributeValue = CommonFunctions.convertDateToString(dt, "yyyy-MM-dd");
                		System.out.println("Date-Of-Visit: "+UpdatedAttributeValue);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	}
                    
                } 
            }

            }
           
           
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was updated ");

        } catch (ParserConfigurationException pce) {

          pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }

    

		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	
	public static void ReadXML_UpdateCodeValue_AttributeGOS4(String Filename, String SheetName, String AttributeName, String key) throws FileNotFoundException {
		String Key = null;
		String TestDataNINumber = null;
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
		try {
			System.out.println(Filename);
			String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", SheetName, AttributeName,1);
			List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Iterator iter = NodePaths.iterator();
            while (iter.hasNext())
            {
            	employee = document.getElementsByTagName((String) iter.next()).item(0);          
            }
            NodeList nodes = employee.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) 
            {
               org.w3c.dom.Node element =  nodes.item(i);
                System.out.println(element.getNodeName());
                if (AttributeName.equals(element.getNodeName()))
                {
                	switch(AttributeName) 
                	{
                	case "DOB": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Surname": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "First-Names": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "PostCode": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NHSNumber": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NINumber":
                		TestDataNINumber = element.getTextContent();
                		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
					case "GOS3-Voucher-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "GOS3-Authorisation-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
                	}
                    
                } 

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was updated ");

        } catch (ParserConfigurationException pce) {

          pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }

    

		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	
	public static List<String> getactualerrors(String errors) {

		List<String> Actualerrors = null;
		
		try{
			errors = errors.replaceAll("\\[", "").replaceAll("\\]","");
			errors = errors.replaceAll(" ", "");
			errors = errors.trim();
			errors = errors.toLowerCase();
			   String[] items = errors.split(",");
			   Actualerrors = Arrays.asList(items);
			
			
		System.out.println(Actualerrors);
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The Actual Errors list is not created  " +e);
		}
		return Actualerrors;
	}
	
	
	public static int VerifyErrorMessage(List<String> ExpectedList,List<String> ActualList, String Key ) throws IOException {
		int count = 0;
		String ActErrors = "";
		int expListSize = ExpectedList.size();
		int actListSize = ActualList.size();
		
		for (int m=0; m<actListSize; m++)
		{
			ActErrors = ActErrors+ActualList.get(m)+" ";
						
		}
		
		System.out.println("Actual Errors: "+ActErrors);
		
		
		try{
			
			System.out.println("Actual Errors: "+ActErrors);
			for(int i=0;i<expListSize;i++)
			{
				if(ActErrors.contains(ExpectedList.get(i)))
				{
					count = 0;
					System.out.println("Error details exists : "+ExpectedList.get(i));
					
				}
				else 
				{
					count = 1;
					System.out.println("Error details does not Exist : "+ExpectedList.get(i));
				}
			}
			
	          
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The Matched Error is not happen " +e);
		}
		return count;
	}
	
	public static void TakeScreenshots(WebDriver driver,String Key, String ClaimsStatusonCRM, int rownumber) throws InterruptedException, IOException 
	{
		
		try{
			
			   File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	            String filePath = System.getProperty("user.dir") + "\\Screenshots\\";
	            String filepathwithName = filePath + Key+"_"+ClaimsStatusonCRM+"_"+rownumber+".png";
	            System.out.println(filepathwithName);
	            FileUtils.copyFile(scrfile,new File(filepathwithName),true );
			
		}

		catch(NoSuchElementException e)
		{
			System.out.println("The Screenshots are not happen" +e);
		}
	 
	}
	public static boolean ExcuteSystemJob_XML(String JobName, String environment) throws InterruptedException, IOException 
	{
		boolean JobRun = false;
		
		try{
			boolean ExcuteJobRun= DatabaseHelper.ExecuteJob("StagingDb", "Staging",JobName,environment);
			if(ExcuteJobRun)
			{
				JobRun = true;
			}
		}
		
		
		/*catch (SQLException sqlEx)
		{
			System.out.println("There is som SQL Exception occured while executing the job "+JobName);
			Thread.sleep(120000);
			boolean ExcuteJobRun= DatabaseHelper.ExecuteJob("StagingDb", "Staging",JobName);
			if(ExcuteJobRun)
			{
				JobRun = true;
			}
		}*/
		
		catch(Exception e)
		{
			System.out.println("The Job run is not successful: "+JobName);
		}
		return JobRun;
	}
	
	
	public static String GetValueFormParagaraph(String Paragarph,String StartwithElement) throws InterruptedException{
		
	String FinalValue = null;
	try {
		Thread.sleep(2000);
		Pattern p = Pattern.compile(""+StartwithElement+"\\S+");
		java.util.regex.Matcher matcher = p.matcher(Paragarph);
		while (matcher.find())
		{
			String ValueInParagraph = (matcher.group());
			String [] Value = ValueInParagraph.split("=");
			System.out.println(Value[1]);
			FinalValue = Value[1].replace("\"", "").trim();
		    System.out.println("The Value is included in Paragarph " + FinalValue);
		}
	}
	catch(Exception e)
	{
		System.out.println("The Paragraph value is not captured");
	}
	return FinalValue;
	}

	 public static String Tomorrowdate(String Currentdate , int days, String df) throws ParseException
	 {
		 String date;
		 SimpleDateFormat sdf = new SimpleDateFormat(df);
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern(df);
		 Calendar c = Calendar.getInstance();
		 c.setTime(sdf.parse(Currentdate));
		 c.add(Calendar.DATE, days);  // number of days to add
		 date = sdf.format(c.getTime());
		 System.out.println("Date is : "+date);
		 return date;
	 }
	 
	public static void haltExecution() throws InterruptedException {
		String ProcessDelay = ConfigurationData.ProcessDelay;
			if((ProcessDelay != null && ProcessDelay.length() > 0))
			{
				int Delaytime = Integer.parseInt(ProcessDelay);
				if(Delaytime!=0)
				{
					System.out.println("Thread started at :"+CommonFunctions.generateDtTimeStamp());
					Thread.sleep(Delaytime*90*1000);
					System.out.println("Thread ended at :"+CommonFunctions.generateDtTimeStamp());
				}	
				else
				{
					Delaytime = 1;
					Thread.sleep(Delaytime*90*1000);
				}
			}
			else
			{

				Thread.sleep(1000);
			} 
				
	}
	
	public static void haltExecution_XML() throws InterruptedException {
		String ProcessDelay = ConfigurationData.ProcessDelay_XML;
			if((ProcessDelay != null && ProcessDelay.length() > 0))
			{
				int Delaytime = Integer.parseInt(ProcessDelay);
				if(Delaytime!=0)
				{
					System.out.println("Thread started at :"+CommonFunctions.generateDtTimeStamp());
					Thread.sleep(Delaytime*90*1000);
					System.out.println("Thread ended at :"+CommonFunctions.generateDtTimeStamp());
				}	
				else
				{
					Delaytime = 1;
					Thread.sleep(Delaytime*90*1000);
				}
			}
			else
			{

				Thread.sleep(1000);
			} 
				
	}
	
	
	public static void PageLoadExternalwait_OP(WebDriver driver) throws InterruptedException
	{
			int counter = 1;
			boolean ispresent = IsElementPresent(driver,Spinner);	
			System.out.println("The spinner is completed " +ispresent);
			Thread.sleep(8000);
			while(!ispresent)
			{
					Thread.sleep(1000);
					counter = counter + 1;
					if(counter > 30)
					{
						break;
					}
					System.out.println("The count for Counter" +counter );
					ispresent = IsElementPresent(driver,Spinner);	
			}
		
	}

	public static boolean IsElementPresent (WebDriver driver, WebElement ele) throws InterruptedException
	{
		try{
						
			WebDriverWait wait = new WebDriverWait(driver, 1);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			//jse.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='loader hide-spinner']")));
			jse.executeScript("arguments[0].scrollIntoView();", ele );

		//	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='loader hide-spinner']")));
			return true;
		}
	 catch (org.openqa.selenium.NoSuchElementException
	        | org.openqa.selenium.StaleElementReferenceException
	        | org.openqa.selenium.TimeoutException e) 
	{
	    return false;
	}
		catch(Exception e)
		{
			return false;
				}
			}
		
	public static String getFinYearLastDate(String dateFormat) throws ParseException {
		String finYrDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		int monthIndex = calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		
/*		int monthIndex =2;
		int currentYear = 2019;*/
		
		if(monthIndex<3){
			monthIndex=2;
			calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.MONTH, monthIndex);
			calendar.set(Calendar.YEAR, currentYear);
			int maxDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(currentYear, monthIndex, maxDate);
			today = calendar.getTime();
			finYrDate= CommonFunctions.convertDateToString(today, dateFormat);
			System.out.println("Fin year last date is: "+finYrDate);
		}else{
			monthIndex=2;
			calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.MONTH, monthIndex);
			currentYear= currentYear+1;
			calendar.set(Calendar.YEAR, currentYear);
			int maxDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(currentYear, monthIndex, maxDate);
			today = calendar.getTime();
			finYrDate= CommonFunctions.convertDateToString(today, dateFormat);
			System.out.println("Fin year last date is: "+finYrDate);
		}
		return finYrDate;
	}
	
	public static int getMonthsBetweenDates(String fromDate, String endDate) throws ParseException{
		
		Date from= CommonFunctions.convertStringtoCalDate(fromDate, "dd/MM/yyyy");
		Date to= CommonFunctions.convertStringtoCalDate(endDate, "dd/MM/yyyy");
		
        Calendar start = Calendar.getInstance();
        start.setTime(from);

        Calendar end = Calendar.getInstance();
        end.setTime(to);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);      

        if(dateDiff<0){
            int maxDate = end.getActualMaximum(Calendar.DAY_OF_MONTH);     
             dateDiff = (end.get(Calendar.DAY_OF_MONTH)+maxDate)-start.get(Calendar.DAY_OF_MONTH);
             monthsBetween--;
             if(dateDiff>0){
                 monthsBetween++;
             }
        }
        else{
            monthsBetween++;
        }      
            monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);      
            monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;   
            System.out.println("Number of months between given date range are: "+monthsBetween);
            return monthsBetween;
     }
	
	public static long getNumberOfDays(String fromDate, String endDate,String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		long diff= 0;
		try {
		    Date date1= sdf.parse(fromDate);
		    Date date2= sdf.parse(endDate);
		    diff= date2.getTime()-date1.getTime();
		    diff= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		    System.out.println ("Number of days within date range are: " +diff);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return diff;
	}
		
	public static String GetFailureMessage(String Paragarph,String StartwithElement) throws InterruptedException{
		
		String FinalValue = null;
		try {
			/*Thread.sleep(2000);
			Pattern p = Pattern.compile(""+StartwithElement+"\\S+");
			java.util.regex.Matcher matcher = p.matcher(Paragarph);
			while (matcher.find())
			{
				String ValueInParagraph = (matcher.group());
				String [] Value = ValueInParagraph.split("=");
				System.out.println("Value of String: "+Value[1]);
				FinalValue = Value[1].replace("\"", "").trim();
			    System.out.println("The Value is included in Paragarph " + FinalValue);
			}*/
			
			FinalValue = Paragarph.substring(Paragarph.lastIndexOf("=") + 1);
			FinalValue = FinalValue.split("\\>")[0];
			System.out.println("Substring value : "+FinalValue);
		}
		catch(Exception e)
		{
			System.out.println("The Paragraph value is not captured");
		}
		return FinalValue;
		}
	
	public static String getFinYearStartDate(String dateFormat) throws ParseException {
		String finYrStartDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		int monthIndex = calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		
/*		int monthIndex =4;
		int currentYear = 2018;*/
		
		if(monthIndex<3){
			monthIndex=3;
			calendar = Calendar.getInstance();
			calendar.clear();
			currentYear= currentYear-1;
			calendar.set(Calendar.MONTH, monthIndex);
			calendar.set(Calendar.YEAR, currentYear);
			int minDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			calendar.set(currentYear, monthIndex, minDate);
			today = calendar.getTime();
			finYrStartDate= CommonFunctions.convertDateToString(today, dateFormat);
		}else{
			monthIndex=3;
			calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.MONTH, monthIndex);
			calendar.set(Calendar.YEAR, currentYear);
			int minDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			calendar.set(currentYear, monthIndex, minDate);
			today = calendar.getTime();
			finYrStartDate= CommonFunctions.convertDateToString(today, dateFormat);
		}
		return finYrStartDate;
	}
	
	public static void ReadXML_UpdateCodeValue_Attribute_new(String Filename, String SheetName, String AttributeName, String key,String filePath) throws FileNotFoundException {
		String Key = null;
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
		String TestDataNINumber = null;
		try {
			System.out.println(Filename);
			String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", SheetName, AttributeName,1);
			List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
			filePath = filePath+"\\"+Filename;
			//filePath = System.getProperty("user.dir")+filePath+"\\"+Filename;
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Iterator iter = NodePaths.iterator();
            while (iter.hasNext())
            {
            	employee = document.getElementsByTagName((String) iter.next()).item(0);          
            }
            NodeList nodes = employee.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) 
            {
               org.w3c.dom.Node element =  nodes.item(i);
                System.out.println(element.getNodeName());
                if (AttributeName.equals(element.getNodeName()))
                {
                	switch(AttributeName) 
                	{
                	case "DOB": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Surname": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "First-Names": 
                		String FirstName = "Automation";
                		String HrMin = CommonFunctions.getCurrentHourMin();
                    	String userID = FirstName+HrMin;
                    	utilities.ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, userID, 2);
                    	Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "PostCode": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NHSNumber": 
                		String NHSNUmber = generateValidNHSNo();
                		utilities.ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, NHSNUmber, 2);
                		Thread.sleep(3000);
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 2);
                		element.setTextContent(UpdatedAttributeValue);
					break;
                	case "NINumber":
                		TestDataNINumber = element.getTextContent();
                		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
					case "GOS3-Voucher-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "GOS3-Authorisation-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "StartDate":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "QOFAttribute", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "EndDate":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "QOFAttribute", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "PracticeIdentifier":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "QOFAttribute", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "Name":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "QOFAttribute", AttributeName, 2);
						element.setTextContent(UpdatedAttributeValue);
						break;
                	}
                    
                } 

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was updated ");

        } catch (ParserConfigurationException pce) {

          pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }

    

		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		//return Key;
	}
	

	public static void ReadXML_UpdateCodeValue_Attribute_Duplicate(String Filename, String SheetName, String AttributeName, String key,String XMLType) throws FileNotFoundException, InterruptedException {
		String TestDataNINumber = null;
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
		
		try {
			System.out.println(Filename);
			String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", SheetName, AttributeName,1);
			List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Iterator iter = NodePaths.iterator();
            while (iter.hasNext())
            {
            	employee = document.getElementsByTagName((String) iter.next()).item(0);          
            }
            NodeList nodes = employee.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) 
            {
               org.w3c.dom.Node element =  nodes.item(i);
                System.out.println(element.getNodeName());
                if (AttributeName.equals(element.getNodeName()))
                {
                	switch(AttributeName) 
                	{
                	case "DOB": 
                		String age = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		if(XMLType.equalsIgnoreCase("BS"))
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
                		}
                		else 
                		{
                			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
                		}
                		System.out.println("The XML DOB is "+UpdatedAttributeValue);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "Surname": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "First-Names": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "PostCode": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
                		break;
                	case "NHSNumber": 
                		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		element.setTextContent(UpdatedAttributeValue);
					break;
                	case "NINumber": 
                		TestDataNINumber = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
                		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
                		element.setTextContent(UpdatedAttributeValue);
					break;					
					case "GOS3-Voucher-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "GOS3-Authorisation-Code":
						UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
						element.setTextContent(UpdatedAttributeValue);
						break;
					case "Tested-On-Date":
						String Months = utilities.ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
						String Lastsightdate = helpers.CommonFunctions.getDate_Months(Months);
						System.out.println("The Last sight date "+Lastsightdate);
						if(XMLType.equalsIgnoreCase("BS"))
							{
								UpdatedAttributeValue = helpers.CommonFunctions.getTodayDate_Format(Lastsightdate , "ddMMyyyy");
							}
							else 
							{
								UpdatedAttributeValue = helpers.CommonFunctions.getTodayDate_Format(Lastsightdate , "yyyy-MM-dd");
							}
						element.setTextContent(UpdatedAttributeValue);
						break;
						
                	}
                    
                } 

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(domSource, streamResult);
            System.out.println("The XML File was updated ");

        } catch (ParserConfigurationException pce) {

          pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();
        } catch (IOException ioe) {

            ioe.printStackTrace();
        } catch (SAXException sae) {

            sae.printStackTrace();
        }catch (Exception e){
					e.printStackTrace();
		}
	}

	// Amit : This method is added to return boolean as TRUE when attribute is present in element
			public static boolean isAttribtuePresent(WebElement element, String attribute) {
			    Boolean result = false;
			    try {
			        String value = element.getAttribute(attribute);
			        if (value != null){
			            result = true;
			        }
			    } catch (Exception e) {}

			    return result;
			}
			
			public static String GenerateUniqueAplphaNumeric_XML(String OldXmlValue , int Position) throws ParseException
			{
				String UpdatedXMLvalue = null;
				
				try{
					
				 	String AlphaLastLetter = OldXmlValue.split("-")[Position].trim();
				   // System.out.println(AlphaLastLetter);
				    String DigitOnly = AlphaLastLetter.replaceAll("\\D+", "");
				    String Digit = DigitOnly.trim();
				   // System.out.println(Digit);	
				    int foo = Integer.parseInt(Digit);
				    int NoofDigit = Digit.length();
				    if(NoofDigit > 1)
				    {
				       		Random random=new Random();
				       		boolean loop=true;
				       		while(loop)
				       		{
				       			//	 posRandint = random.nextInt(Integer.MAX_VALUE);
				       			//	posRandint = random.nextInt(Integer.MAX_VALUE)+1;
				       			randomNumber=random.nextInt(Integer.MAX_VALUE);
				       			randomNumber = random.nextInt(Integer.MAX_VALUE)+1;
				       			if(Integer.toString(randomNumber).length()==Digit.length())
				       			{
				       				loop=false;
				       			}
				       		}
			/*		Random random = new Random();
					Integer randomNumber;
					randomNumber = random.nextInt(Integer.SIZE - 1) + foo;*/
					System.out.println(randomNumber);
				    }
				    else 
				    {
				    	randomNumber = foo+ 1;
				    }
					//System.out.println(randomNumber);
					String RanNumber = 	String.valueOf(randomNumber);
				//	System.out.println(RanNumber);
					char[] Arr1 = AlphaLastLetter.toCharArray();
				//	System.out.println(Arr1[0]);
				//	System.out.println(Arr1.length);			
					char[] Arr2 = RanNumber.toCharArray();
				//	System.out.println(Arr2[0]);
					int M =0;
					for(int l=0;l<Arr1.length;l++)
					{
						char Choi  = Arr1[l];
						String Choice = Character.toString(Choi);
						if(!(Choice.matches("\\D+")))
						{
							Arr1[l] = Arr2[M];
							M= M+1; 
						}
						
					}
					String LastXMLvalue= Arrays.toString(Arr1);
					String text = String.valueOf(Arr1);
			//		System.out.println(text);
						
					UpdatedXMLvalue = OldXmlValue.replaceAll(OldXmlValue.split("-")[Position].trim(), text);
				//	System.out.println(UpdatedXMLvalue);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

					return UpdatedXMLvalue;
			}
			
			public static String GenerateUniqueNINumber_XML(String NINumber) throws ParseException
			{
				String UpdatedXMLvalue = null;
				
				try{
					
				 	String DigitOnly = NINumber.replaceAll("\\D+", "");
				    String Digit = DigitOnly.trim();
				    System.out.println(Digit);	
				    int foo = Integer.parseInt(Digit);
				    int NoofDigit = Digit.length();
				    if(NoofDigit > 1)
				    {
				       		Random random=new Random();
				       		boolean loop=true;
				       		while(loop)
				       		{
				       		 	randomNumber=random.nextInt(Integer.MAX_VALUE);
				       			randomNumber = random.nextInt(Integer.MAX_VALUE)+1;
				       			if(Integer.toString(randomNumber).length()==Digit.length())
				       			{
				       				loop=false;
				       			}
				       		}
					System.out.println(randomNumber);
				    }
				    else 
				    {
				    	randomNumber = foo+ 1;
				    }		
					String RanNumber = 	String.valueOf(randomNumber);				
					char[] Arr1 = NINumber.toCharArray();						
					char[] Arr2 = RanNumber.toCharArray();				
					int M =0;
					for(int l=0;l<Arr1.length;l++)
					{
						char Choi  = Arr1[l];
						String Choice = Character.toString(Choi);
						if(!(Choice.matches("\\D+")))
						{
							Arr1[l] = Arr2[M];
							M= M+1; 
						}
						
					}
					String LastXMLvalue= Arrays.toString(Arr1);
					String text = String.valueOf(Arr1);					
					UpdatedXMLvalue = NINumber.replaceAll(NINumber,text);
					System.out.println(UpdatedXMLvalue);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

					return UpdatedXMLvalue;
			}
			
			
			public static void ReadXML_AllAttribute(String Filename, String SheetName, String AttributeName, String key) throws FileNotFoundException, InterruptedException {
				String Key = null;
				String UpdatedAttributeValue = null;
				String AttributeValue = null;
				org.w3c.dom.Node employee = null;
				try {
					System.out.println(Filename);
					String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", SheetName, AttributeName,1);
					List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
					String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
		            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		            Document document = documentBuilder.parse(filePath);
		            Iterator iter = NodePaths.iterator();
		            while (iter.hasNext())
		            {
		            	employee = document.getElementsByTagName((String) iter.next()).item(0);          
		            }
		            NodeList nodes = employee.getChildNodes();

		            for (int i = 0; i < nodes.getLength(); i++) 
		            {
		               org.w3c.dom.Node element =  nodes.item(i);
		               
		              //  System.out.println(element.getNodeName());
		                if (AttributeName.equals(element.getNodeName()))
		                {
		                	
		                	AttributeValue = element.getTextContent();
		                	System.out.println("The Attribute "+AttributeName+ "Value is"  +AttributeValue);
		                	ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "GetAllAttribute", AttributeValue, AttributeName, key);
		                	Thread.sleep(2000);
		                } 

		            }
		     
		        } catch (ParserConfigurationException pce) {

		          pce.printStackTrace();

		        }  catch (IOException ioe) {

		            ioe.printStackTrace();
		        } catch (SAXException sae) {

		            sae.printStackTrace();
		        }catch (Exception e){
							e.printStackTrace();
				}
			}
			
	public static String getFirstDayOfMonth(String dateFormat,int monthsToAdd) throws ParseException {
		String finYrStartDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int monthIndex = calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		monthIndex =monthIndex+monthsToAdd;
/*		int monthIndex =8+monthsToAdd;
		int currentYear = 2018;*/
		calendar.set(Calendar.MONTH,monthIndex);
		calendar.set(Calendar.YEAR,currentYear);
		int minDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(currentYear,monthIndex,minDate);
		today = calendar.getTime();
		finYrStartDate= CommonFunctions.convertDateToString(today, dateFormat);
		return finYrStartDate;
	}
			
	public static String getLastDayOfMonth(String dateFormat,int monthsToAdd) throws ParseException {
		String finYrStartDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int monthIndex = calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		monthIndex =monthIndex+monthsToAdd;
/*		int monthIndex =7+monthsToAdd;
		int currentYear = 2018;*/
		calendar.set(Calendar.MONTH,monthIndex);
		calendar.set(Calendar.YEAR,currentYear);
		int minDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(currentYear,monthIndex,minDate);
		today = calendar.getTime();
		finYrStartDate= CommonFunctions.convertDateToString(today,dateFormat);
		return finYrStartDate;
	}
	
	
	public static int ReadXML_UpdatetagValue(String environment, String XMLTYPE, String Filename, String AttributeName, String TestDataFile, String SheetName, String ColumnName,int NoofTagValue, int ExpectedTagValue) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		NodeList nodes = null;
		String tagValues = null;
		int l=0;
		
		try 
		{
			tagValues = ExcelUtilities.getKeyValueByPosition(TestDataFile, SheetName, XMLTYPE,ColumnName);
			List<String> Tagtravelpaths = Arrays.asList(tagValues.split("\\s*,\\s*"));
				for(String Tagtravelpath : Tagtravelpaths)
				{
					switch(Tagtravelpath)
					{
					case "Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
					case "Performer-Declaration":
						if(AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
											
					case "Contractor-Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
						
						}
					break;
					}
				}
            
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(filePath); 
			if(document != null)
			{
				List<String> ls = new ArrayList<>();
				ls.addAll( Arrays.asList(tagValues) );
				NodeList nList = document.getElementsByTagName(tagValues);
					for (int i = 0; i < nList.getLength(); i++) 
					{
						Element element1 = (Element)nList.item(i);
						if (ls.contains(element1.getNodeName()))
						{					
						//	System.out.println(element1.getNodeName()+": "+element1.getTextContent());					
							nodes = element1.getChildNodes();
						//	System.out.println("nodes: "+nodes.getLength());   
							for (int a = 0; a < nodes.getLength(); a++) 
								{
									
									
									org.w3c.dom.Node element =  nodes.item(a);
									if(element.getNodeType() == Node.ELEMENT_NODE)
									{
										//System.out.println(element.getNodeName());
										l++;		            				
									if (AttributeName.equals(element.getNodeName()))
									{
										switch(AttributeName) 
										{
											case "Organisation-Code":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");															
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_OrganizationCode");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Contractor-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Contractor-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													//AssertMessage.add("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													//AssertMessage.add("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}	
											case "Suppliers-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
												//	AssertMessage.add("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Suppliers-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													//AssertMessage.add("Supplier Name is set to : :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													//AssertMessage.add("Supplier Name is set to : :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Performer-Name":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer Name is set to :"+UpdatedAttributeValue);
													//AssertMessage.add("Performer Name is set to : :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;												
											case "Performer-List-Number":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "OPPerformerNumber");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer List Number is set to :"+UpdatedAttributeValue);
												//	AssertMessage.add("Performer List Number is set to : :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
													
										}
									}
								}
                           } 
					}
				}
			}
		
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));
        transformer.transform(domSource, streamResult);
        System.out.println("The XML File was updated ");

    } catch (ParserConfigurationException pce) {

      pce.printStackTrace();

    } catch (TransformerException tfe) {

        tfe.printStackTrace();

    } catch (IOException ioe) {

        ioe.printStackTrace();

    } catch (SAXException sae) {

        sae.printStackTrace();

    }
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return ExpectedTagValue;	
	}
	
	public static int BULKReadXML_UpdatetagValue(String environment, String XMLTYPE, String Filename, String AttributeName, String TestDataFile, String SheetName, String ColumnName,int NoofTagValue, int ExpectedTagValue) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		NodeList nodes = null;
		String tagValues = null;
		int l=0;
		
		try 
		{
			tagValues = ExcelUtilities.getKeyValueByPosition(TestDataFile, SheetName, XMLTYPE,ColumnName);
			List<String> Tagtravelpaths = Arrays.asList(tagValues.split("\\s*,\\s*"));
				for(String Tagtravelpath : Tagtravelpaths)
	
				{
					switch(Tagtravelpath)
					{
					case "Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
					case "Performer-Declaration":
						if(AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
						
					case "Contractor-Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
						
						}
					break;
					case "Part1-Contractor-Details":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
						
						}
					break;
					}
				}
            
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(filePath); 
			if(document != null)
			{
				List<String> ls = new ArrayList<>();
				ls.addAll( Arrays.asList(tagValues) );
				NodeList nList = document.getElementsByTagName(tagValues);
					for (int i = 0; i < nList.getLength(); i++) 
					{
						Element element1 = (Element)nList.item(i);
						if (ls.contains(element1.getNodeName()))
						{					
						//	System.out.println(element1.getNodeName()+": "+element1.getTextContent());					
							nodes = element1.getChildNodes();
						//	System.out.println("nodes: "+nodes.getLength());   
							for (int a = 0; a < nodes.getLength(); a++) 
								{
									
									
									org.w3c.dom.Node element =  nodes.item(a);
									if(element.getNodeType() == Node.ELEMENT_NODE)
									{
										System.out.println(element.getNodeName());
										l++;		            				
									if (AttributeName.equals(element.getNodeName()))
									{
										switch(AttributeName) 
										{
											case "Organisation-Code":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");															
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_OrganizationCode");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												
											
											case "Contractor-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Contractor-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}	
											case "Suppliers-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Suppliers-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Performer-Name":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "XMLOPPerformer");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;												
											case "Performer-List-Number":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "XMLOPPerformerNumber");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer List Number is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
											case "Contract-Code":
												UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "XMLPVNContractCode");
												element.setTextContent(UpdatedAttributeValue);
												System.out.println("Performer List Number is set to :"+UpdatedAttributeValue);
												ExpectedTagValue = ExpectedTagValue + 1;
												break;
													
										}
									}
								}
                           } 
					}
				}
			}
		
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));
        transformer.transform(domSource, streamResult);
        System.out.println("The XML File was updated ");

    } catch (ParserConfigurationException pce) {

      pce.printStackTrace();

    } catch (TransformerException tfe) {

        tfe.printStackTrace();

    } catch (IOException ioe) {

        ioe.printStackTrace();

    } catch (SAXException sae) {

        sae.printStackTrace();

    }
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return ExpectedTagValue;	
	}
	
	
	public static int BULKReadXML_UpdatetagValue(String key, String environment, String XMLTYPE, String Filename, String AttributeName, String TestDataFile, String SheetName, String ColumnName,int NoofTagValue, int ExpectedTagValue) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		NodeList nodes = null;
		String tagValues = null;
		int l=0;
		
		try 
		{
			tagValues = ExcelUtilities.getKeyValueByPosition(TestDataFile, SheetName, XMLTYPE,ColumnName);
			List<String> Tagtravelpaths = Arrays.asList(tagValues.split("\\s*,\\s*"));
				for(String Tagtravelpath : Tagtravelpaths)
	
				{
					switch(Tagtravelpath)
					{
					case "Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
					case "Performer-Declaration":
						if(AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
							
						}
						break;
						
					case "Contractor-Declaration":
						if(!AttributeName.startsWith("Performer"))
						{
							tagValues	= Tagtravelpath;
						
						}
					break;
					}
				}
            
			String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(filePath); 
			if(document != null)
			{
				List<String> ls = new ArrayList<>();
				ls.addAll( Arrays.asList(tagValues) );
				NodeList nList = document.getElementsByTagName(tagValues);
					for (int i = 0; i < nList.getLength(); i++) 
					{
						Element element1 = (Element)nList.item(i);
						if (ls.contains(element1.getNodeName()))
						{					
						//	System.out.println(element1.getNodeName()+": "+element1.getTextContent());					
							nodes = element1.getChildNodes();
						//	System.out.println("nodes: "+nodes.getLength());   
							for (int a = 0; a < nodes.getLength(); a++) 
								{
									
									
									org.w3c.dom.Node element =  nodes.item(a);
									if(element.getNodeType() == Node.ELEMENT_NODE)
									{
										System.out.println(element.getNodeName());
										l++;		            				
									if (AttributeName.equals(element.getNodeName()))
									{
										switch(AttributeName) 
										{
											case "Organisation-Code":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");															
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_OrganizationCode");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Organisation code is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												
											case "PVN-Reference":
												
												if(Filename.contains("_BS_"))
												{
													//UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");
													UpdatedAttributeValue = ExcelUtilities.getKeyValueByPosition("BSGOS6XMLFILEDATA.xlsx", "XML", key, "PVN REF");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("PVN-Reference is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = ExcelUtilities.getKeyValueByPosition("PMSGOS6XMLFILEDATA.xlsx", "XML", key, "PVN REF");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("PVN-Reference is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Contractor-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Contractor-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_ContractorDecName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Contractor Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}	
											case "Suppliers-Declaration-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_Suppliers-Declaration-Name");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Declaration Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Suppliers-Name":
												if(Filename.contains("_BS_"))
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
												else 
												{
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_SupplierName");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Supplier Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
												}
											case "Performer-Name":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer Name is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;												
											case "Performer-List-Number":
													UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "OPPerformerNumber");
													element.setTextContent(UpdatedAttributeValue);
													System.out.println("Performer List Number is set to :"+UpdatedAttributeValue);
													ExpectedTagValue = ExpectedTagValue + 1;
													break;
													
										}
									}
								}
                           } 
					}
				}
			}
		
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath));
        transformer.transform(domSource, streamResult);
        System.out.println("The XML File was updated ");

    } catch (ParserConfigurationException pce) {

      pce.printStackTrace();

    } catch (TransformerException tfe) {

        tfe.printStackTrace();

    } catch (IOException ioe) {

        ioe.printStackTrace();

    } catch (SAXException sae) {

        sae.printStackTrace();

    }
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return ExpectedTagValue;	
	}

	public static void getAttributeValue(String environment) {
		// TODO Auto-generated method stub
		
	}

	public static String getUpdateAttributeValue(String environment, String MainAttributeName, String Filename) {
		String UpdatedAttributeValue = null;
		try{
			
			switch(MainAttributeName) 
			{
				case "Organisation-Code":
					if(Filename.contains("_BS_"))
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");
						System.out.println("The Organisation-Code is updated with " +UpdatedAttributeValue);
						break;
					
					}
					else 
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_OrganizationCode");
						System.out.println("The Organisation-Code is updated with " +UpdatedAttributeValue);
						break;
					}
				case "PSK":
					if(Filename.contains("_BS_"))
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_PSK");
						System.out.println("The PSK is updated with " +UpdatedAttributeValue);
						break;
					}
					else 
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_PSK");
						System.out.println("The PSK is updated with " +UpdatedAttributeValue);
						break;
					}
				case "PVN-Reference":
					if(Filename.contains("_PMS_"))
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PVN_ReferenceNumber");
						System.out.println("The PVN-Reference is updated with " +UpdatedAttributeValue);
						break;
					}
					
				
			}
		}
		catch(Exception e)
		{
			System.out.println("The Updated Attribute value is not found in Configuration File");
		}
		return UpdatedAttributeValue;
	}
	
	public static String getUpdateAttributeValue(String key, String environment, String MainAttributeName, String Filename) {
		String UpdatedAttributeValue = null;
		try{
			
			switch(MainAttributeName) 
			{
				case "Organisation-Code":
					if(Filename.contains("_BS_"))
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_OrganizationCode");
						break;
					
					}
					else 
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_OrganizationCode");
						break;
					}
				case "PSK":
					if(Filename.contains("_BS_"))
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "BS_PSK");
						break;
					}
					else 
					{
						UpdatedAttributeValue = testdata.ConfigurationData.getRefDataDetails(environment, "PMS_PSK");
						break;
					}
					
				case "PVN-Reference":
					
					if(Filename.contains("_BS_"))
					{
						UpdatedAttributeValue = ExcelUtilities.getKeyValueByPosition("BSGOS6XMLFILEDATA.xlsx", "XML", key, "PVN REF");
						break;
					}
					else 
					{
						UpdatedAttributeValue = ExcelUtilities.getKeyValueByPosition("PMSGOS6XMLFILEDATA.xlsx", "XML", key, "PVN REF");
						System.out.println("PVN REF: "+UpdatedAttributeValue);
						break;
					}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Updated Attribute value is not found in Configuration File");
		}
		return UpdatedAttributeValue;
	}

	public static void updateMainAttribute(String FileNames,String XMLType, String attr,String UpdatedAttributeValue,int ActualupdateAttr,int ExpectedupdateAttr,String Key) 
	{	
		String AttributePath = null;
		try{
			
			switch(attr) 
			{
				case "PSK":
					AttributePath = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "MasterAttribute", XMLType, "AttributePath");
					helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key);
					break;
				case "Organisation-Code":
					if(FileNames.contains("PVN"))
					{
						AttributePath = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "MasterAttribute", XMLType, "AttributePath");
						helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key);
					}
					else
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", attr, UpdatedAttributeValue, Key);
					}
					break;			
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Main Attribute of XMl is not updated sucessfully "+e);
		}
		
	}

	public static void BulkupdateMainAttribute(String FileNames,String XMLType, String attr,String UpdatedAttributeValue,int ActualupdateAttr,int ExpectedupdateAttr,String Key, String MainFile) 
	{	
		String AttributePath = null;
		try{
			
			switch(attr) 
			{
				case "PSK":
					AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key, MainFile);
					break;
				case "Organisation-Code":
					if(FileNames.contains("PVN"))
					{
						AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key, MainFile);
					}
					else
					{
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", attr, UpdatedAttributeValue, Key, MainFile);
					}
					break;		
					
				case "PVN-Reference":
					AttributePath = "Claim-Message";
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key, MainFile);
					break;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Main Attribute of XMl is not updated sucessfully "+e);
		}
		
	}
	
	public static void BulkupdateMainAttribute(String MainFile, String FileNames,String XMLType, String attr,String UpdatedAttributeValue,int ActualupdateAttr,int ExpectedupdateAttr,String Key) 
	{	
		String AttributePath = null;
		AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
		
		
		try{
			
			switch(attr) 
			{
				case "PSK":
				//	AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, AttributePath, attr, UpdatedAttributeValue, Key, MainFile);
					break;
				case "Organisation-Code":
					if(FileNames.contains("PVN"))
					{
				//		AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", attr, UpdatedAttributeValue, Key, MainFile);
					}
					else
					{
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", attr, UpdatedAttributeValue, Key, MainFile);
					}
					break;		
					
				case "PVN-Reference":
				//	AttributePath = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "AttributePath");
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", attr, UpdatedAttributeValue, Key, MainFile);
					break;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Main Attribute of XMl is not updated sucessfully "+e);
		}
		
	}
	public static String getXMLType(String FileName) {
		String XMLType = null;	
		try{
		
		if(FileName.contains("GOS1_BS"))
			{
				XMLType = "BS_GOS1";
			}
		else if(FileName.contains("GOS3_BS"))
			{
				XMLType = "BS_GOS3";
			}
		else if(FileName.contains("GOS4_BS"))
			{
				XMLType = "BS_GOS4";
			}
		else if(FileName.contains("GOS5_BS"))
			{
				XMLType = "BS_GOS5";
			}
		else if(FileName.contains("GOS6_BS"))
			{
				XMLType = "BS_GOS6";
			}
		else if(FileName.contains("GOS6_Old_BS"))
				{
					XMLType = "BS_Old_GOS6";
				}
		else if(FileName.contains("GOS1_PMS"))
			{
				XMLType = "PMS_GOS1";
			}
		else if(FileName.contains("GOS3_PMS"))
			{
				XMLType = "PMS_GOS3";
			}
		else if(FileName.contains("GOS4_PMS"))
			{
				XMLType = "PMS_GOS4";
			}
		else if(FileName.contains("GOS5_PMS"))
			{
				XMLType = "PMS_GOS5";
			}
		else if(FileName.contains("GOS6_PMS"))
			{
				XMLType = "PMS_GOS6";
			}
		else if(FileName.contains("VOC"))
			{
				XMLType = "GOS3_VOC";
			}
		else if(FileName.contains("GOS6PVN_PMS_CREATE"))
			{
				XMLType = "PMS_GOS6PVN_CREATE";
			}
		else if(FileName.contains("GOS6PVN_PMS"))
			{
				XMLType = "PMS_GOS6PVN";
			}
		else 
		{
			XMLType = null;
		}
			
		}
		catch(Exception e)
		{
			System.out.println("The XMLType is not getting from Filename - Invalid FileName"+e);
		}
		return XMLType;	
	}

	public static boolean ComparetoString(String expectedAmountDue, String actAmountDue) {
		boolean Correctcomparison = false;
		try
		{
			if(expectedAmountDue != null && !expectedAmountDue.isEmpty())
			{
				if(actAmountDue != null && !actAmountDue.isEmpty())
				{
					Correctcomparison = true;
				}
				else 
				{
					System.out.println("The null value is Actual Result ");
				}
			}
			else 
			{
				System.out.println("The null value is Expected  Result ");
			}
		}
		catch(Exception e)
		{
			System.err.println("The Expected or Actual value for comparison is null "+e);		
		}
		return false;
	}
	
	// Amit 11/07/2018 : Added below method to copy the XML file based on Master File & placed it in XML folder for further processing.
	
	public static void copyFile(String masterFileName, String key) throws IOException
	{
		String projectPath = System.getProperty("user.dir");
		String MasterFileInputPath = projectPath+"\\XML\\MASTER";
		String xmlFileOutputPath = projectPath+"\\XML";
		
		
		String outputFileName = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "FILE NAME");;
		if(outputFileName.contains(","))
		{
			
			String[] filenameList = outputFileName.split(",");
			int FileNumber = filenameList.length;
			for(int j= 0;j<FileNumber;j++)
			{
				if(filenameList[j].contains("VOC"))
				{
					outputFileName = filenameList[0];
					break;
				}
				else
				{
					outputFileName = filenameList[1];
					break;
				}
			}
		}
		
		
		BufferedReader reader = new BufferedReader(new FileReader(MasterFileInputPath+"\\"+masterFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFileOutputPath+"\\"+outputFileName));

        String line = null;

        while ((line = reader.readLine()) != null)
        {
                   	
        	writer.write(line);
        }
       
        // Close to unlock.
        reader.close();
        // Close to unlock and flush to disk.
        writer.close();
        boolean present =  ((new File(xmlFileOutputPath+"\\"+outputFileName)).exists() && !((new File(xmlFileOutputPath+"\\"+outputFileName)).length() == 0));
        if(present)
        	System.out.println("The XML file is copied successfully.");
        else
        	System.out.println("Either the file is not present OR Size of file is NULL ");
	}
	
	public static void copyFile(String MainFile, String masterFileName, String key) throws IOException
	{
		String projectPath = System.getProperty("user.dir");
		String MasterFileInputPath = projectPath+"\\XML\\MASTER";
		String xmlFileOutputPath = projectPath+"\\XML";
		
		
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		
		BufferedReader reader = new BufferedReader(new FileReader(MasterFileInputPath+"\\"+masterFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFileOutputPath+"\\"+outputFileName));

        String line = null;

        while ((line = reader.readLine()) != null)
        {
                   	
        	writer.write(line);
        }
       
        // Close to unlock.
        reader.close();
        // Close to unlock and flush to disk.
        writer.close();
        boolean present =  ((new File(xmlFileOutputPath+"\\"+outputFileName)).exists() && !((new File(xmlFileOutputPath+"\\"+outputFileName)).length() == 0));
        if(present)
        	System.out.println("The XML file is copied successfully.");
        else
        	System.out.println("Either the file is not present OR Size of file is NULL ");
	}
	
	public static void copyFile(String MainFile, String masterFileName, String key, int Index) throws IOException
	{
		String projectPath = System.getProperty("user.dir");
		String MasterFileInputPath = projectPath+"\\XML\\MASTER";
		String xmlFileOutputPath = projectPath+"\\XML";
		
		
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		
		String[] outputFileNames = outputFileName.split(",");
		outputFileName = outputFileNames[Index];
		
		
		BufferedReader reader = new BufferedReader(new FileReader(MasterFileInputPath+"\\"+masterFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFileOutputPath+"\\"+outputFileName));

        String line = null;

        while ((line = reader.readLine()) != null)
        {
                   	
        	writer.write(line);
        }
       
        // Close to unlock.
        reader.close();
        // Close to unlock and flush to disk.
        writer.close();
        boolean present =  ((new File(xmlFileOutputPath+"\\"+outputFileName)).exists() && !((new File(xmlFileOutputPath+"\\"+outputFileName)).length() == 0));
        if(present)
        	System.out.println("The XML file is copied successfully.");
        else
        	System.out.println("Either the file is not present OR Size of file is NULL ");
	}
	
	public static void copyFileDestinationFile(String MainFile, String masterFileName,String DestinationFile , String key) throws IOException
	{
		String projectPath = System.getProperty("user.dir");
		String MasterFileInputPath = projectPath+"\\XML\\MASTER";
		String xmlFileOutputPath = projectPath+"\\XML";
		
		
			
		
		BufferedReader reader = new BufferedReader(new FileReader(MasterFileInputPath+"\\"+masterFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFileOutputPath+"\\"+DestinationFile));

        String line = null;

        while ((line = reader.readLine()) != null)
        {
                   	
        	writer.write(line);
        }
       
        // Close to unlock.
        reader.close();
        // Close to unlock and flush to disk.
        writer.close();
        boolean present =  ((new File(xmlFileOutputPath+"\\"+DestinationFile)).exists() && !((new File(xmlFileOutputPath+"\\"+DestinationFile)).length() == 0));
        if(present)
        	System.out.println("The XML file is copied successfully.");
        else
        	System.out.println("Either the file is not present OR Size of file is NULL ");
	}
	//Added by Akshay to get the first day of the year 
	public static String getFirstDayOfYear(String dateFormat, int afterYears) throws ParseException {
		String firstCalDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int monthIndex = calendar.get(Calendar.MONTH);
		monthIndex= monthIndex-monthIndex;
		int currentYear = calendar.get(Calendar.YEAR);
		currentYear= currentYear+afterYears;
		calendar.set(Calendar.MONTH,monthIndex);
		calendar.set(Calendar.YEAR,currentYear);
		int minDate= calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(currentYear,monthIndex,minDate);
		today = calendar.getTime();
		firstCalDate= CommonFunctions.convertDateToString(today, dateFormat);
		return firstCalDate;
	}
	
	//Added by Akshay to get the last day of the year 
	public static String getLastDayOfYear(String dateFormat,int afterYears) throws ParseException {
		String lastCalDate="";
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int monthIndex = 11;
		int currentYear = calendar.get(Calendar.YEAR);
		currentYear= currentYear+afterYears;
		calendar.set(Calendar.MONTH,monthIndex);
		calendar.set(Calendar.YEAR,currentYear);
		int minDate= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(currentYear,monthIndex,minDate);
		today = calendar.getTime();
		lastCalDate= CommonFunctions.convertDateToString(today,dateFormat);
		return lastCalDate;
	}

	public static void selectOptometristFromList(WebElement ele, String option) throws InterruptedException {
    	try {
    
        Thread.sleep(2000);
        List<WebElement> options = ele.findElements(By.tagName("li"));
        // Loop through the options and select the one that matches
        for (WebElement opt : options) {
        	WebElement optLink = opt.findElement(By.tagName("a"));
            if (optLink.getText().contains(option)) {
            	optLink.click();
					Thread.sleep(1000);
                break;
            	}
        	}
       // throw new NoSuchElementException("Can't find " + option + " in dropdown");
		}
		catch (org.openqa.selenium.StaleElementReferenceException ex)
			{
				
				System.out.println("The Stale Reference Exception has occured while selecting option " +ele.getAttribute("id"));
				//ex.printStackTrace();
				Thread.sleep(2000);
				 List<WebElement> options = ele.findElements(By.tagName("li"));
			        // Loop through the options and select the one that matches
			        for (WebElement opt : options) {
			        	WebElement optLink = opt.findElement(By.tagName("a"));
			            if (optLink.getText().contains(option)) {
			            	optLink.click();
			                break;
			            }
			        }
			}
			
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}

	public static String Getmasterfilename(String FileName) {
		String MasterXMLfileName = null;
		if(FileName.contains("GOS1_BS"))
		{
			MasterXMLfileName = "GOS1_BS_Master.xml";
		}
	else if(FileName.contains("GOS3_BS"))
		{
		MasterXMLfileName = "BS_GOS3";
		}
	else if(FileName.contains("GOS4_BS"))
		{
		MasterXMLfileName = "BS_GOS4";
		}
	else if(FileName.contains("GOS5_BS"))
		{
		MasterXMLfileName = "GOS5_BS_Master.xml";
		}
	else if(FileName.contains("GOS6_BS"))
		{
		MasterXMLfileName = "GOS6_BS_Master.xml";
		}
	else if(FileName.contains("GOS1_PMS"))
		{
		MasterXMLfileName = "PMS_GOS1";
		}
	else if(FileName.contains("GOS3_PMS"))
		{
		MasterXMLfileName = "PMS_GOS3";
		}
	else if(FileName.contains("GOS4_PMS"))
		{
		MasterXMLfileName = "PMS_GOS4";
		}
	else if(FileName.contains("GOS5_PMS"))
		{
		MasterXMLfileName = "GOS5_PMS_Master.xml";
		}
	else if(FileName.contains("GOS6_PMS"))
		{
		MasterXMLfileName = "PMS_GOS6";
		}
	else if(FileName.contains("VOC"))
		{
		MasterXMLfileName = "GOS3 VO Master.xml";
		}
	else if(FileName.contains("GOS6PVN_PMS_CREATE"))
		{
		MasterXMLfileName = "GOS6_PVN_PMS.xml";
		}
	else if(FileName.contains("GOS6PVN_PMS"))
		{
		MasterXMLfileName = "PMS_GOS6PVN";
		}
	else 
	{
		MasterXMLfileName = null;
	}
		return MasterXMLfileName;
	}
	
	//this method will give you a next working day after today's date. e.g. pass parameters like {"03/08/2018","dd/MM/yyyy","1"}
	 public static String getNextWorkingDayExcludingWeekends(String[] args) throws ParseException {
         // format of date is passed as an argument
         SimpleDateFormat sdf = new SimpleDateFormat(args[1]);
         // base date which will be incremented
         String dateStr = args[0];
         Date date = sdf.parse(dateStr);
         Calendar c = Calendar.getInstance();
         // set calendar time with given date
         c.setTime(date);
         // number of days to increment
         int maxIncrement = Integer.parseInt(args[2]);
         // add days to date 
         c.add(Calendar.DAY_OF_WEEK, maxIncrement);
         // check if the date after addition is a working day. 
         // If not then keep on incrementing it till it is a working day
         while(!isWorkingDay(c.getTime(), c)) {
             c.add(Calendar.DAY_OF_WEEK, 1);
         }
           return sdf.format(c.getTime());
     }

     private static boolean isWorkingDay(Date date, Calendar calendar) {
         // set calendar time with given date
         calendar.setTime(date);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         // check if it is Saturday(day=7) or Sunday(day=1)
         if ((dayOfWeek == 7) || (dayOfWeek == 1)) {
             return false;
         }
         return true;
      }
		

public static List<String> getPVNReference(String MainFile, String key, String pvnFileName, String environment) throws IOException, InterruptedException
{
	//List<String> pvnReferenceList = null;
	ArrayList<String> pvnReferenceList = new ArrayList<String>();
	boolean Process = false;
	boolean JobRun = false;
	int i = 1;
	//String dataFileName = "XMLStagingCRM.xlsx";
	String sheetName= "PVNAttributes";
	List<String> assertMessage = null;
	//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
	String XMLType = helpers.CommonFunctions.getXMLType(pvnFileName);
	System.out.println("XML Type is: "+XMLType);
	XMLhelpers objXMLhelpersUpdateMasterData = new XMLhelpers(Process, assertMessage);
	objXMLhelpersUpdateMasterData = objXMLhelpersUpdateMasterData.BULKXMLUpdateMasterAttribute(MainFile, environment, pvnFileName, XMLType,key);
	
	if(objXMLhelpersUpdateMasterData.Process)
	{
		
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, sheetName, 1);
		for(String AttributeName : AttributeNames )
		{
			//String UpdatedXMLValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLStagingCRM.xlsx", "UpdatedAttribute", AttributeName, 1);
			helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_Test(pvnFileName,MainFile,sheetName, AttributeName,key,22);
			 
		}
		//System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(MainFile,pvnFileName,key, environment);
		String pvnReference = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Success PVN-Reference=");
		System.out.println("PVN_Ref: "+pvnReference);
		pvnReferenceList.add(pvnReference);
		ExcelUtilities.setKeyValueByPosition(MainFile, "XML", pvnReference, key, "PVN REF");
		ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", pvnReference, "PVN_ReferenceNumber", environment);
		
	}
	for(String AssetMessage : objXMLhelpersUpdateMasterData.AssetMessage)
	{
		pvnReferenceList.add(AssetMessage);
		//setAssertMessage(AssetMessage, i);
		i = i + 1;
	}
	
	return pvnReferenceList;
}
	
	public static void BULKReadXML_UpdateCodeValue_Attribute_New(String XMLType, String Filename, String SheetName, String AttributeName, String key, String MainFile) throws FileNotFoundException {
		String UpdatedAttributeValue = null;
		org.w3c.dom.Node employee = null;
	NodeList nodes = null;
	NodeList node = null;
	String TestDataNINumber = null;
	String AttributeNameNew = null;
	String AttributePrevious = null;
	try {
		System.out.println(Filename);
		String NodePath = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, SheetName, AttributeName,1);
		List<String> NodePaths = Arrays.asList(NodePath.split("\\s*,\\s*"));
		String filePath = System.getProperty("user.dir") + "\\XML\\"+Filename;	
	    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    Document document = documentBuilder.parse(filePath);
	  
	    Iterator iter = NodePaths.iterator();
	    while (iter.hasNext())
	    {
	       	employee = document.getElementsByTagName((String) iter.next()).item(0); 
	    }         	 
	    nodes = employee.getChildNodes();
	    System.out.println(nodes);
	    AttributePrevious = AttributeName;
	
	    for (int i = 0; i < nodes.getLength(); i++) 
	      {            	
	    	if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
	    	{
	    	    if(AttributeName.contains(","))
	            {
	            	Pattern p = Pattern.compile(".*,\\s*(.*)");
	            	Matcher m = p.matcher(AttributeName);
	            	if (m.find())
	            	{
	            		AttributeName = m.group(1);
	            		AttributeNameNew = AttributeName;
	            		System.out.println(m.group(1));
	            	}
	            }
	    	    else
	    	    {
	    	    	AttributeNameNew = AttributeName;
	    	    }
	    		org.w3c.dom.Node element =  nodes.item(i);
	    	//	System.out.println(element.getNodeName());
	    		if (AttributeNameNew.equals(element.getNodeName()))
	        {
	        	switch(AttributeName) 
	        	{
	        	case "DOB": 
	        	    String age = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		if(XMLType.contains("BS"))
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "ddMMyyyy");
	        		}
	        		else 
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDOB(age , "yyyy-MM-dd");
	        		}
	        		System.out.println("The XML DOB is "+UpdatedAttributeValue);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The DOB is updated: "+UpdatedAttributeValue);
	        		break;
	        	case "Surname": 
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The Surname is updated: "+UpdatedAttributeValue);
	        		break;
	        	case "First-Names": 
	        		String FirstName = "Automation";
	        		String HrMin = CommonFunctions.getCurrentHourMin();
	            	String userID = FirstName+HrMin;
	            	//utilities.ExcelUtilities.setKeyValueinExcelWithPosition(MainFile, SheetName, AttributeName, userID, 2);
	            	utilities.ExcelUtilities.setKeyValueByPosition(MainFile, SheetName, userID, AttributeName, key);
	            	Thread.sleep(3000);
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		//UpdatedAttributeValue = userID;
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The First-Names is updated: "+UpdatedAttributeValue);
	        		break;
	        	case "PostCode": 
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The Postcode is updated: "+UpdatedAttributeValue);
	        		break;
	        	case "NHSNumber": 
	        		String NHSNUmber = generateValidNHSNo();
	        		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, SheetName, NHSNUmber, AttributeName, key);
	        		Thread.sleep(3000);
	        		//UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, SheetName, AttributeName, 2);
	        		UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributeName, key);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The NHSNumber is updated: "+UpdatedAttributeValue);
				break;
	        	case "NINumber":
	        		TestDataNINumber = element.getTextContent();
	        		UpdatedAttributeValue = helpers.CommonFunctions.GenerateUniqueNINumber_XML(TestDataNINumber);
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The NINumber is updated: "+UpdatedAttributeValue);
	        		break;
        		case "Date-First-Pair-Supplied":
					if(XMLType.contains("BS"))
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
	        		}
	        		else 
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
	        		}
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
				case "Date-Second-Pair-Supplied":
					if(XMLType.contains("BS"))
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("ddMMyyyy");
	        		}
	        		else 
	        		{
	        			UpdatedAttributeValue = helpers.CommonFunctions.getDate("yyyy-MM-dd");
	        		}
					if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	        		{
        				org.w3c.dom.Node parent = element.getParentNode();
        			    parent.removeChild(element);
        			    parent.normalize();
	        			break;
	        		}
	        		else
	        		{
	        		element.setTextContent(UpdatedAttributeValue);
	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	        		}
					break;
	        		default:
	        			//System.out.println("The "+AttributeName+" is not found for update.");
	    	        	UpdatedAttributeValue = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, SheetName, AttributePrevious, key);
	    				if(UpdatedAttributeValue.equalsIgnoreCase("Remove"))
	            		{
	        				org.w3c.dom.Node parent = element.getParentNode();
	        			    parent.removeChild(element);
	        			    parent.normalize();
	            			break;
	            		}
	            		else
	            		{
	    	        		element.setTextContent(UpdatedAttributeValue);
	    	        		System.out.println("The XML Attribute "+AttributePrevious+ "With Value " +UpdatedAttributeValue);
	            		}
	        	}
	        }
	            
	    	} 
	    }
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(filePath));
    transformer.transform(domSource, streamResult);
    System.out.println("The XML File was updated ");
	
	} catch (ParserConfigurationException pce) {
	
	  pce.printStackTrace();
	
	} catch (TransformerException tfe) {
	
	    tfe.printStackTrace();
	
	} catch (IOException ioe) {
	
	    ioe.printStackTrace();
	
	} catch (SAXException sae) {
	
	    sae.printStackTrace();
	
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	
		//return Key;
	}
	
	public static void BULKReadXML_GetAndSaveValue_Attribute(String xmlFileName, String AttributeName, String key, String MainFile,String referenceSheet,
			String targetSheet, String expectedDataKey,String tagName) throws FileNotFoundException {

		String attributeValue=null;
		try{
				
				String filePath = System.getProperty("user.dir") + "\\XML\\"+xmlFileName;		
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(filePath);
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("//"+tagName);
				NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < nl.getLength(); i++)
				{
				    org.w3c.dom.Node currentItem = nl.item(i);
				    attributeValue= currentItem.getAttributes().getNamedItem(AttributeName).getNodeValue();
				    ExcelUtilities.setKeyValueByPosition(MainFile, targetSheet, attributeValue, expectedDataKey, key);
				    System.out.println("The Attribute "+AttributeName+" value is set to:  "+attributeValue+" for key: "+key);
				} 
			}catch (ParserConfigurationException pce){
			      pce.printStackTrace();
			    } catch (IOException ioe){
			        ioe.printStackTrace();
			    } catch (SAXException sae){
			        sae.printStackTrace();
			    }catch (Exception e){
					e.printStackTrace();
				}	
	}

	public static void BULKReadXML_GetAndSaveValue_ChildTagAttribute(String xmlFileName, String attributeName,
			String key, String mainFile, String referenceSheet, String targetSheet, String expectedDataKey,
			String tagTravelPath) {
		org.w3c.dom.Node employee = null;
		NodeList nodes,node1 = null;
		int AttributeSize=0;
		String ThirdLastAttribute = null;
		String AttributePrevious=null,SecondLastAttribute = null;
		try {
				System.out.println(xmlFileName);
				List<String> NodePaths = Arrays.asList(tagTravelPath.split("\\s*,\\s*"));
				String filePath = System.getProperty("user.dir") + "\\XML\\"+xmlFileName;	
		        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		        Document document = documentBuilder.parse(filePath);
		      
		        Iterator iter = NodePaths.iterator();
		        while (iter.hasNext())
		        {
		           	employee = document.getElementsByTagName((String) iter.next()).item(0); 
		        }         	 
		        nodes = employee.getChildNodes();
		        AttributePrevious = attributeName;
		        if(attributeName.contains(","))
		        {
		    		List<String> NodeMatches = Arrays.asList(attributeName.split("\\s*,\\s*"));
		        	AttributeSize = NodeMatches.size();
		        	System.out.println(NodeMatches.size());
		        	if(AttributeSize==4){
		        		ThirdLastAttribute= NodeMatches.get(AttributeSize - 3);
		            	SecondLastAttribute = NodeMatches.get(AttributeSize - 2);
		            	attributeName = NodeMatches.get(AttributeSize - 1);
		        	}else{
		        		SecondLastAttribute = NodeMatches.get(AttributeSize - 2);
		        		attributeName = NodeMatches.get(AttributeSize - 1);
		        	}
		        }
		        else
		        {
		        	SecondLastAttribute = attributeName;
		        }
		        for (int i = 0; i < nodes.getLength(); i++) 
		          {            	
		        	if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
		        	{
		        		org.w3c.dom.Node element =  nodes.item(i);
		        		System.out.println(element.getNodeName());
		        		if(AttributeSize==4){
		            		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(ThirdLastAttribute))
		            		{
		            			node1 = element.getChildNodes();
		            			secondAttrLoop:
		    					for (int a=0; a<node1.getLength(); a++)
		    					{
		    						if((node1.item(a).getNodeType() == Node.ELEMENT_NODE))
		    			        	{
		    			        		element =  node1.item(a);
		    			        		System.out.println(element.getNodeName());
		    			        		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(SecondLastAttribute))
		    		            		{
		    		    			   		node1 = element.getChildNodes();
		    		    					for (int index=0; index<node1.getLength(); index++)
		    		    					{
		    		    						if((node1.item(index).getNodeType() == Node.ELEMENT_NODE))
		    		    						{
		    		    						//	System.out.println(node1.item(a).getNodeName());
		    		    							if (node1.item(index).getNodeName().equalsIgnoreCase(attributeName))
		    		    							{       				
		    		    								element = node1.item(index);
		    		    								break secondAttrLoop;
		    		    							}
		    		    						}
		    		    		        	}
		    		    					System.out.println(element.getNodeName());
		    		            		}
		    			        	}	
		    					}
		            		}
		        		}else{
		            		if (element.hasChildNodes() && element.getNodeName().equalsIgnoreCase(SecondLastAttribute))
		            		{
		    			   		node1 = element.getChildNodes();
		    					for (int a=0; a<node1.getLength(); a++)
		    					{
		    						if((nodes.item(i).getNodeType() == Node.ELEMENT_NODE))
		    						{
		    						//	System.out.println(node1.item(a).getNodeName());
		    							if (node1.item(a).getNodeName().equalsIgnoreCase(attributeName))
		    							{       				
		    								element = node1.item(a);
		    								break;
		    							}
		    						}
		    		        	}
		    					System.out.println(element.getNodeName());
		            		}
		        		}
		        		if (attributeName.equals(element.getNodeName()))
		            {
						String attributeValue= element.getTextContent().toString();
						if(expectedDataKey.equalsIgnoreCase("POINTOFSERVICEDATE")){
							if(mainFile.contains("BS")){
								Date date= CommonFunctions.convertStringtoCalDate(attributeValue, "ddMMyyyy");
								attributeValue= CommonFunctions.convertDateToString(date, "yyyy-MM-dd");
							}else{
								Date date= CommonFunctions.convertStringtoCalDate(attributeValue, "yyyy-MM-dd");
								attributeValue= CommonFunctions.convertDateToString(date, "yyyy-MM-dd");
							}
						}
						ExcelUtilities.setKeyValueByPosition(mainFile, targetSheet, attributeValue, expectedDataKey, key);
					    System.out.println("The Attribute "+attributeName+" value is set to:  "+attributeValue+" for key: "+key);
					    break;
		            }
		                
		        	} 
		        }
	
	    }catch (ParserConfigurationException pce){
	      pce.printStackTrace();
	    }catch (IOException ioe){
	        ioe.printStackTrace();
	    }catch (SAXException sae){
	        sae.printStackTrace();
	    }catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String getfinalInternalportaluserLink(String environment, String portalUser) {
		String InternalURL = null;
		try{
			if(portalUser != null && !portalUser.isEmpty())
			{
				String Expected1 = portalUser.substring(portalUser.indexOf("(")+1,portalUser.indexOf(")"));
				String first_word = Expected1.split(",")[0];
				String SecondWord = first_word.replaceAll("^\"|\"$", "");
				InternalURL = SecondWord.trim();
				ExcelUtilities.setKeyValueByPosition("ConfigurationDetails.xlsx", "REFDATA", InternalURL, "PLPORTALINT", environment);
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Internal Portal user is not created" +e);
		}
		// TODO Auto-generated method stub
		return InternalURL;
	}
	
	public static void ClickOnSubmitButton(String Text,WebDriver driver) {
		try {
			Thread.sleep(1000);
			List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='submit']"));
			System.out.println("total buttons "+buttons.size());
			for (WebElement button : buttons)
			{  		
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);
				String ButtonValue = button.getText();
				if (ButtonValue.equalsIgnoreCase(Text))
				{				
					button.click();
					break;
				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
			
	public static void ClickOnButtonWebElement (List<WebElement> ele, String Text,WebDriver driver) {
		try {
			Thread.sleep(1000);
		//	List<WebElement> buttons= driver.findElements(ele);
			System.out.println("total buttons "+ele.size());
			for (WebElement button : ele)
			{  		
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", button);
				String ButtonValue = button.getText();
				if (ButtonValue.equalsIgnoreCase(Text))
				{				
					button.click();
					break;
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	
	
	public static void clickElementByXpath (WebElement ele, WebDriver driver) {
		try {
			
				WebDriverWait wait=new WebDriverWait(driver,30);			
				wait.until(ExpectedConditions.elementToBeClickable(ele)).click();	
				helpers.Support.PageLoadExternalwait(driver);
				//spinnerwait(driver);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

	}
	
	public static void spinnerwait(WebDriver driver) {
		try {
			
				WebDriverWait wait=new WebDriverWait(driver,40);			
				
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader']")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

	}
	
	public static String getCurrentHourMinSeconds()
	{
		String tm;
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HHmmss");
		System.out.println(format.format(date));
		tm = format.format(date);
		return tm;
	}
	
	// this modifies the numeric values as per standard
	public static List<String> modifyNumericValuesForOPProcessing(List<String> actualValues) {
		int count= actualValues.size();
		for(int i=0;i<count;i++){
			String value= actualValues.get(i).toString();
			if(value.matches("-?\\d+(\\.\\d+)?")){
				String []valueArray= value.split("\\.");
				String decimalPart= valueArray[1];
				if(decimalPart.length()==4){
					value = value.substring(0, (value.length()-2));
					actualValues.set(i, value);
				}else if(decimalPart.length()==1){
					decimalPart= valueArray[1]+"0";
					value= valueArray[0]+"."+decimalPart;
					actualValues.set(i, value);
				}
			}
		}
		return actualValues;
	}	
	
	
}