package helpers;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateParser 
{
	public static String convertDBDateToFormat(String dBDate, String dateFormat) throws ParseException
	{
		// Convert input string into a date
		//dbdateFormat = "yyyy-MM-dd hh:mm:ss.S"
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		Date date = inputFormat.parse(dBDate);
		// Format date into output format
		//dateFormat="DD-MMMM-YYYY"
		DateFormat outputFormat = new SimpleDateFormat(dateFormat);
		String outputString = outputFormat.format(date);
		return outputString;
	}

	
	public static String convertSDFToDBDate(String sDFDate) throws ParseException
	{
		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date inputdate = input.parse(sDFDate);
		String formattedTime = output.format(inputdate);
/*		// Convert input string into a date
		//dbdateFormat = "yyyy-MM-dd hh:mm:ss.S"
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		//DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		Date date = inputFormat.parse(sDFDate);
		// Format date into output format
		//dateFormat="DD-MMMM-YYYY"
		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		String outputString = outputFormat.format(sDFDate);*/
		return formattedTime;
	}
	
	
	public static String getFirstCollectionDate(LocalDate date, int collectionDay, String schemaName, String environment) throws InterruptedException, SQLException, ParseException 
	{
		List<String> bufferDays = DatabaseHelper.CreateDataListForAListOfRows("Select value From FIRM_REFERENCE Where name = 'DDBufferDays';", "value", schemaName, environment);  
		List<String> holidays = DatabaseHelper.CreateDataListForAListOfRows("SELECT * FROM HOLIDAY_MASTER", "DATE", "commondb", environment);
		LocalDate result = date;
		LocalDate finalResultDate = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		int addedDays = 0;
		while (addedDays < Integer.valueOf(bufferDays.get(0))) 
		{
			result = result.plusDays(1);
			if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY || holidays.contains(dtf.format(result)))) 
			{
				++addedDays;
			}
		}

		String collectionDate = date.getYear()+"-"+date.getMonthValue()+"-"+String.valueOf(collectionDay);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
		formatter = formatter.withLocale( Locale.UK );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
		LocalDate collectionDateFormatted = LocalDate.parse(collectionDate, formatter);
		
		if(date.isAfter(collectionDateFormatted))
			collectionDateFormatted = collectionDateFormatted.plusMonths(1);

		if(collectionDateFormatted.isAfter(result))
		{
			finalResultDate = collectionDateFormatted;
			boolean isWeekEnd = false;
			do
			{
				if ((finalResultDate.getDayOfWeek() == DayOfWeek.SATURDAY || finalResultDate.getDayOfWeek() == DayOfWeek.SUNDAY || holidays.contains(dtf.format(finalResultDate)))) 
				{
					finalResultDate = finalResultDate.plusDays(1);
					isWeekEnd = true;
				}
				else
					isWeekEnd = false;
			}while(isWeekEnd);
		}
		else
		{
			finalResultDate = collectionDateFormatted.plusMonths(1);
			boolean isWeekEnd = false;
			do
			{
				if ((finalResultDate.getDayOfWeek() == DayOfWeek.SATURDAY || finalResultDate.getDayOfWeek() == DayOfWeek.SUNDAY || holidays.contains(dtf.format(finalResultDate)))) 
				{
					finalResultDate = finalResultDate.plusDays(1);
					isWeekEnd = true;
				}
				else
					isWeekEnd = false;
			}while(isWeekEnd);

		}
		dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
		return dtf.format(finalResultDate);
	}

}
