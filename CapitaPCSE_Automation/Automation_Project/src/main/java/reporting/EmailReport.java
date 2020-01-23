/*package reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.activation.*;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import helpers.DatabaseHelper;

public class EmailReport 
{
	public static void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {
		 
		Properties props = System.getProperties();
		 
		String host = "outlook.office365.com";
		 
		props.put("mail.smtp.starttls.enable", "true");
		 
		props.put("mail.smtp.host", host);
		 
		props.put("mail.smtp.user", from);
		 
		props.put("mail.smtp.password", pass);
		 
		props.put("mail.smtp.port", "587");
		 
		props.put("mail.smtp.auth", "true");
		 
		Session session = Session.getDefaultInstance(props);
		 
		MimeMessage message = new MimeMessage(session);
		 
		try {
		 
		    //Set from address
		 
		message.setFrom(new InternetAddress(from));
		 
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		 
		//Set subject
		 
		message.setSubject(subject);
		 
		message.setText(body);
		 
		BodyPart objMessageBodyPart = new MimeBodyPart();
		 
		objMessageBodyPart.setText("Please Find The Attached Report File!");
		 
		Multipart multipart = new MimeMultipart();
		 
		multipart.addBodyPart(objMessageBodyPart);
		 
		objMessageBodyPart = new MimeBodyPart();
		 
		//Set path to the pdf report file
		 
		String filename = System.getProperty("user.dir")+"\\Default test.pdf";
		 
		//Create data source to attach the file in mail
		 
		DataSource source = new FileDataSource(filename);
		 
		objMessageBodyPart.setDataHandler(new DataHandler(source));
		 
		objMessageBodyPart.setFileName(filename);
		 
		multipart.addBodyPart(objMessageBodyPart);
		 
		message.setContent(multipart);
		 
		Transport transport = session.getTransport("smtp");
		 
		transport.connect(host, from, pass);
		 
		transport.sendMessage(message, message.getAllRecipients());
		 
		transport.close();
		 
		}
		 
		catch (AddressException ae) {
		 
		ae.printStackTrace();
		 
		}
		 
		catch (MessagingException me) {
		 
		me.printStackTrace();
		 
		}
}
	
	public static void sendEmail() throws InterruptedException, SQLException
	{
		
			
		// Amit : Getting latest Execution ID from Automation DB
				List<String> executionID = DatabaseHelper.CreateDataListForAListOfRows("SELECT EXECUTION_ID FROM automationdb.execution_details Where Exec_Status = 'Completed' Order By Date desc;", "EXECUTION_ID", "automationdb", "Local");
				int latestExecutionID = Integer.parseInt(executionID.get(0));
				System.out.println("Execution ID : " + latestExecutionID);
		
			final String username = "amitkumarr@mastek.com";
	        final String password = "Malhar@2019";

	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", "outlook.office365.com");
	        props.put("mail.smtp.port", "587");
		
		// Create object of Property file
				Properties props = new Properties();
		 
				// this will set host of server- you can change based on your requirement 
				props.put("mail.smtp.host", "outlook.office365.com");
		 
				// set the port of socket factory 
				props.put("mail.smtp.socketFactory.port", "587");
		 
				// set socket factory
				props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		 
				// set the authentication to true
				props.put("mail.smtp.auth", "true");
		 
				// set the port of SMTP server
				props.put("mail.smtp.port", "465");
		 
				// This will handle the complete authentication
				Session session = Session.getDefaultInstance(props,
		 
						new javax.mail.Authenticator() {
		 
							protected PasswordAuthentication getPasswordAuthentication() {
		 
							return new PasswordAuthentication(username, password);
		 
							}
		 
						});
		 
				try {
		 
					// Create object of MimeMessage class
					Message message = new MimeMessage(session);
		 
					// Set the from address
					message.setFrom(new InternetAddress(username));
		 
					// Set the recipient address
					message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(username));
		            
		                        // Add the subject link
					message.setSubject("Test Execution Report_ExecutionID_ "+latestExecutionID);
		 
					// Create object to add multimedia type content
					BodyPart messageBodyPart1 = new MimeBodyPart();
		 
					// Set the body of email
					messageBodyPart1.setText("Hi, Please see attached Test Results");
										
					//messageBodyPart1.setText("This is new line.");
					
					
		 
					// Create another object to add another content
					MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		 
					// Mention the file which you want to send
					//String filename = "D:\\screenshot.jpeg";
					//String filename = System.getProperty("user.dir")+"\\Reports\\ConsolidatedReport_"+latestExecutionID+".xlsx";
					String filename = System.getProperty("user.dir")+"\\Reports\\ConsolidatedReport_434.xlsx";
					// Create data source and pass the filename
					DataSource source = new FileDataSource(filename);
		 
					// set the handler
					messageBodyPart2.setDataHandler(new DataHandler(source));
		 
					// set the file
					messageBodyPart2.setFileName("ConsolidatedReport_434.xlsx");
		 
					// Create object of MimeMultipart class
					Multipart multipart = new MimeMultipart();
		 
					// add body part 1
					multipart.addBodyPart(messageBodyPart2);
		 
					// add body part 2
					multipart.addBodyPart(messageBodyPart1);
				//	multipart.addBodyPart(messageBodyPart3);
		 
					// set the content
					message.setContent(multipart);
		 
					// finally send the email
					Transport.send(message);
		 
					System.out.println("=====Email Sent=====");
		 
				} catch (MessagingException e) {
		 
					throw new RuntimeException(e);
		 
				}
		 
	}

	public static void main(String[] args) throws InterruptedException, SQLException
	{
		new EmailReport().sendEmail();
	}
}



*/