package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UITableDataSupport 
{
	/*
	 * This method gets all the values of the given table - table id should contain tbody
	 */
	static WebDriver driver;
	static WebDriverWait wait;
	public static List<ArrayList<String>> getUITableData(WebElement tableID)
	{
		return getRowDataForColumn(tableID);
	}

	/*
	 * This method gets all the header values of the given table - table id should contain thead
	 */
	public static List<ArrayList<String>> getUITableHeader(WebElement tableID)
	{
		return getHeaderDataForColumn(tableID);
	}

	public static List<ArrayList<String>> getSpecificColumnDataInTable(WebElement tableID, List<Integer> tablecolumnnumbers)
	{
		List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
		List<WebElement> rows = tableID.findElements(By.tagName("tr"));
		System.out.println(rows.size());

		for(WebElement row:rows)
		{
			List<WebElement> rowElements = new ArrayList<WebElement>();
			for(int eachColumnNumber : tablecolumnnumbers)
			{
				rowElements.add(row.findElements(By.tagName("td")).get(eachColumnNumber-1));
				//rowElements.add(row.findElement(By.xpath("//td["+ (eachColumnNumber-1) +"]")));
				//rowElements.add(row.findElement(By.xpath("//table[@id='pcss-todo-week4']/tr[r]//td["+(eachColumnNumber-1)+"]")));
				//rowElements.add(driver.findElement(By.xpath("//table[@id='pcss-todo-week4']/tr[r]//td["+(eachColumnNumber-1)+"]")));
			}

			ArrayList<String> rowData = new ArrayList<String>();

			rowData.addAll(getRowData(rowElements));

			rowsData.add(rowData);


		}

		Collections.sort(rowsData, new Comparator<ArrayList<String>>() {    
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(0).compareTo(o2.get(0));
			}               
		});

		return rowsData;
	}

	public static List<String> getDataForAColumnInTable(WebElement tableID, int tablecolumnnumber)
	{
		List<String> rowData = new ArrayList<String>();
		List<WebElement> rows = tableID.findElements(By.tagName("tr"));
		for(WebElement row:rows)
		{
			List<WebElement> rowElements = new ArrayList<WebElement>();
			rowElements.add(row.findElements(By.tagName("td")).get(tablecolumnnumber-1));
			rowData.addAll(getRowData(rowElements));
			//rowsData.add(rowData);
		}

		return rowData;
	}

	private static List<String> getRowData(List<WebElement> rowElements)
	{
		List<String> rowData = new ArrayList<String>();
		for(WebElement column:rowElements)
		{
			/*if(column.getAttribute("class").contains("units"))
			{
				rowData.add(column.getText().replace(",", ""));
			}
			else if(column.getAttribute("class").contains("price"))
			{
				rowData.add(column.getText().substring(1).replace(",", ""));
			}
			else if(column.getAttribute("class").contains("originalCost"))
			{
				rowData.add(column.getText().substring(1).replace(",", ""));
			}
			else if(column.getAttribute("class").contains("gainLoss"))
			{
				rowData.add(column.getText().substring(1).replace("%", ""));
			}
			else if(column.getAttribute("class").contains("marketValue"))
			{
				rowData.add(column.getText().substring(1).replace(",", "").replace(" ", ""));
			}
			else if(column.getAttribute("class").contains("amount"))
			{
				rowData.add(column.getText().replace(",", "").replace(" ", "").replace("-", "").substring(1));
			}
			else if(column.getAttribute("class").contains("investmentAssetName"))
			{
				try 
				{
					rowData.add(column.findElement(By.tagName("div")).findElement(By.tagName("strong")).getText());
				} 
				catch (Exception e) 
				{
					rowData.add(column.getText());
				}
			}
			else if(column.getAttribute("class").contains("transact lastHeader"))
			{
				rowData.add(column.findElement(By.tagName("div")).findElement(By.tagName("i")).getAttribute("class"));
			}
			else if(column.getAttribute("class").contains("quantity"))
			{
				rowData.add(column.getText().replace(",", ""));
			}*/
			
			if(column.getAttribute("class").contains("patientNHSField"))
			{
				rowData.add(column.getText().replaceAll("\\s",""));

			}

			else if(column.getAttribute("class").contains("pcss-nhsnum"))
			{
				//rowData.add(column.getText().replaceAll("\\s",""));
				rowData.add(column.findElement(By.tagName("a")).getText().replaceAll("\\s", ""));

			}

			else if(column.getText().contains("Acknowledge"))
			{
				// do nothing
			}
			else if(column.getAttribute("class").contains("pcss-truncate-address"))
			{
				
				try
				{
					rowData.add(column.findElement(By.tagName("span")).getAttribute("data-content"));
				}
				catch(NoSuchElementException e)
				{
					rowData.add(column.getText().toString());
				}

			}

			else
				rowData.add(column.getText().toString());
		}
		return rowData;
	}

	private static List<ArrayList<String>> getHeaderDataForColumn(WebElement tableID)
	{
		List<WebElement> rows = tableID.findElements(By.tagName("tr"));
		List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();


		for(WebElement row:rows)
		{
			List<WebElement> rowElements = row.findElements(By.tagName("th"));

			ArrayList<String> rowData = new ArrayList<String>();

			for(WebElement column:rowElements)
			{
				rowData.add(column.getText());
			}
			rowsData.add(rowData);
		}
		return rowsData;
	}

	private static List<ArrayList<String>> getRowDataForColumn(WebElement tableID)
	{
		List<WebElement> rows = tableID.findElements(By.tagName("tr"));
		List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
		//	WebElement addr = tableID.findElement(By.tagName("span"));
		//	WebElement addr = tableID.findElement(By.tagName("span"));

		for(WebElement row:rows)
		{
			List<WebElement> rowElements = row.findElements(By.tagName("td"));

			ArrayList<String> rowData = new ArrayList<String>();

			for(WebElement column:rowElements)
			{
				/*boolean present = true;
				if (column.getText().contains("Acknowledge")){
					present = false;
				}
				else
				{
				boolean present;
				try {
					column.findElement(By.tagName("button"));
				   present = false;
				} catch (NoSuchElementException e) {
				   present = true;
				}

				while (present) {*/
				//if(column.getAttribute("class").contains("pcss-truncate-address") && (column.findElement(By.tagName("span")).getAttribute("class").contains("pcss-help pcsshelp")))
				//	if(column.findElement(By.tagName("span")).getAttribute("class").contains("pcsshelp"))

				//System.out.println(column.getText().toString());

				if(column.getAttribute("class").contains("pcss-truncate-address"))
				{
					//System.out.println("SPAN tag is present under TD tag: " +column.findElement(By.tagName("span")).isDisplayed());
					try
					{
						rowData.add(column.findElement(By.tagName("span")).getAttribute("data-content"));
					}
					catch(NoSuchElementException e)
					{
						rowData.add(column.getText().toString());
					}
					/*if (column.findElement(By.tagName("span")).isDisplayed()) 
					rowData.add(column.findElement(By.tagName("span")).getAttribute("data-content"));*/
					//rowData.add(addr.getAttribute("data-content"));
					//}
					/*else
					{
						rowData.add(column.getText().toString());
					}*/
				}
				else if(column.getAttribute("class").contains("patientNHSField"))
				{
					rowData.add(column.getText().replaceAll("\\s",""));

				}

				else if(column.getAttribute("class").contains("pcss-nhsnum"))
				{
					//rowData.add(column.getText().replaceAll("\\s",""));
					rowData.add(column.findElement(By.tagName("a")).getText().replaceAll("\\s", ""));

				}

				else if(column.getText().contains("Acknowledge"))
				{
					// do nothing
				}
				//else if(column.findElement(By.tagName("a"))



				/*	else if(column.findElement(By.tagName("button")).getSize()!= 0)
				{
					rowData.add(column.getText().substring(1).replace("%", ""));
					break;
				}

				else if(column.getAttribute("class").contains("marketValue"))
				{
					rowData.add(column.getText().substring(1).replace(",", ""));
					break;
				}*/
				else
					/*	try
				{
					rowData.add(column.findElement(By.tagName("a")).getText());
				}
				catch(NoSuchElementException e)*/
				{
					rowData.add(column.getText().toString());
				}

				//	rowData.add(column.getText());
			}


			if (!rowData.isEmpty())
			{
				rowsData.add(rowData);
				//System.out.println("||" + rowData + " ");
				//System.out.println();
			}
		}
		Collections.sort(rowsData, new Comparator<ArrayList<String>>() {    
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(0).compareTo(o2.get(0));
			}               
		});
		System.out.println("Table Array list added to list: " +rowsData);
		System.out.println(rowsData);
		return rowsData;
	}

	public static List<ArrayList<String>> getSpecificColumnDataInTableWeek4(WebElement tableID, List<Integer> tablecolumnnumbers)
	{
		List<ArrayList<String>> rowsData = new ArrayList<ArrayList<String>>();
		List<WebElement> rows = tableID.findElements(By.tagName("tr"));
		System.out.println(rows.size());

		for(WebElement row:rows)
		{
			List<WebElement> rowElements = new ArrayList<WebElement>();
			for(int eachColumnNumber : tablecolumnnumbers)
			{
				rowElements.add(row.findElements(By.tagName("td")).get(eachColumnNumber-1));
				//rowElements.add(row.findElement(By.xpath("//td["+ (eachColumnNumber-1) +"]")));
				//rowElements.add(row.findElement(By.xpath("//table[@id='pcss-todo-week4']/tr[r]//td["+(eachColumnNumber-1)+"]")));
				//rowElements.add(driver.findElement(By.xpath("//table[@id='pcss-todo-week4']/tr[r]//td["+(eachColumnNumber-1)+"]")));
			}

			ArrayList<String> rowData = new ArrayList<String>();

			rowData.addAll(getRowData(rowElements));

			rowsData.add(rowData);


		}

		Collections.sort(rowsData, new Comparator<ArrayList<String>>() {    
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(0).compareTo(o2.get(0));
			}               
		});

		return rowsData;
	}

	public static WebElement getWebElementFromTable(WebElement tableID, int rownumber, int tablecolumnnumber)
	{
		//List<String> rowData = new ArrayList<String>();
		//List<WebElement> rowElements = new ArrayList<WebElement>();
		//WebElement row = tableID.findElement(By.xpath("/tr["+rownumber+"]"));
		
		
		WebElement rowElement = tableID.findElements(By.tagName("tr")).get(rownumber);
		WebElement column = rowElement.findElements(By.tagName("td")).get(tablecolumnnumber);
		
	
		
		return column;
	}
	
	public static Boolean getDataFromColumnInTable(WebElement table, int colNo, String name)
	{
		
		List<String> rowData = new ArrayList<String>();
		Boolean value = false;
		//WebElement table = driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (WebElement row:rows)
		{
			List<WebElement> rowItems = new ArrayList<WebElement>();
			rowItems.add(row.findElements(By.tagName("td")).get(colNo));
			for (WebElement rowItem:rowItems)
			{
				rowData.add(rowItem.getText());
				System.out.println(rowData);
			}
		
		}
		
		if (rowData.contains(name)) {
		    value = true;
		} else {
		    System.out.println("Org not found");
		}
		
	//	return rowData;
		return value;
	}
}

