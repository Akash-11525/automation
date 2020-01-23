set projectLocation=D:\CapitaPCSEAutomation_Akshay\TestAutomation\PCSE_Automation_Maven\Automation_Project
cd %projectLocation%
call mvn -f adhoc_pom.xml clean install
call mvn exec:java@RunChangeExecutionStatus
call mvn exec:java@RunDumpReportToDB
call mvn exec:java@RunExcelReportGenerator
call mvn exec:java@RunConsolidatedReport
echo "Generating Report..."
timeout /t 5