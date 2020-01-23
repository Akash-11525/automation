@ECHO OFF

set projectLocation=D:\CapitaPCSEAutomation_Akshay\TestAutomation\PCSE_Automation_Maven\Automation_Project
cd %projectLocation%
call mvn -f sanity_pom.xml clean install
REM call mvn exec:java@RunChangeExecutionStatus
REM call mvn exec:java@RunDumpReportToDB
REM call mvn exec:java@RunExcelReportGenerator
echo "Generating Report..."
timeout /t 5