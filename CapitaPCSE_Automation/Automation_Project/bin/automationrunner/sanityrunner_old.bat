set projectLocation=D:\feedrepo\d2cfeed\US_POC\d2cfeed\temp\test
cd %projectLocation%
set classpath=%projectLocation%\bin;%projectLocation%\libs\*
java org.testng.TestNG %projectLocation%\RunSanityScriptsOnIE.xml
echo "Generating Report..."
timeout /t 10
java reporting.ExcelReportGenerator
echo "Loading Report..."
timeout /t 10
start Excel.exe "D:\feedrepo\d2cfeed\US_POC\d2cfeed\temp\test\Reports\ReportTemplate.xlsm"
echo "Sending Report..."
timeout /t 10