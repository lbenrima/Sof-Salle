package tn.sofrecom.mdrissi.mail;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tn.sofrecom.mdrissi.entities.Participant;
import tn.sofrecom.mdrissi.entities.Reservation;

@Component
public class MonthlyMailService implements ConvertToUTC{
	
	@Value("${mail.smtp.host}")
	private String host;
	
	@Value("${mail.smtp.port}")
	private String port;
	
	@Value("${mail.smtp.starttls.enable}")
	private String starttls;
	private final int CANCLEDEVENT = 0;
	HashMap<String, String> hmap = new HashMap<String, String>();
			
		public MonthlyMailService() {
			    		
			   hmap.put("MONDAY","MO");
			   hmap.put("TUESDAY","TU");
			   hmap.put("WEDNESDAY","WE");
			   hmap.put("THURSDAY","TH");
			   hmap.put("FRIDAY","FR");
			   hmap.put("SATURDAY","SA");
			   hmap.put("SUNDAY","SU");			    	
			}

		public void sendMonthlyWithEnddate(Reservation res,List<Participant> parts, int status) throws Exception {
			  	    	
			  	    	Date startdate = res.getStartdate();
			  	    	SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
			  	    	TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Africa/Tunis"));	
			  	    	sdfs.setTimeZone(tz);
			  	    	String datestart = sdfs.format(startdate); //2011-05-10  
			  	    	String startday  = datestart.substring(datestart.lastIndexOf('-')+1, datestart.length());
			  	    	String startmonth = datestart.substring(datestart.indexOf('-')+1, datestart.lastIndexOf('-'));
			  	    	String startyear   = datestart.substring(0, datestart.indexOf('-'));
			  	    	
			  	    
			  	    	
			  	    	LocalDate date = LocalDate.of(Integer.parseInt(startyear),Integer.parseInt(startmonth),Integer.parseInt(startday)); 	    	
			  	    	System.out.println(date);
			  	    	
				    	 String start = dateForPeriodicReservation(res.getStartdate(), res.getStarttime());
					  	    String end = dateForPeriodicReservation(res.getEnddate(), res.getEndtime());
					  	    String endtimeevent = dateForPeriodicReservation(res.getStartdate(), res.getEndtime());
			  	    	System.out.println(start);
			  	    	System.out.println(end);
			  	    		  	    	
			        try {     
			        	
			        	Properties prop = new Properties();
			        	prop.put("mail.smtp.starttls.enable", starttls);
			            prop.put("mail.smtp.host", host);
			            prop.put("mail.smtp.port", port);   		        	
			            Session session = Session.getDefaultInstance(prop,null); 	         
			       
			            // Define message
			            MimeMessage message = new MimeMessage(session);
			            message.addHeaderLine("method=REQUEST");
			            message.addHeaderLine("charset=UTF-8");
			            message.addHeaderLine("component=VEVENT");
			            
			            message.setFrom(new InternetAddress(res.getReservedby().getMail()));
			            String listMails = "";
			            for(int i = 0;i<parts.size();i++) {
			            	listMails = listMails + parts.get(i).getMail()+",";
			            }	         
			            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(listMails+res.getReservedby().getMail()));
						String title = (status == CANCLEDEVENT) ? "Annuler " + res.getTitle() : res.getTitle();

			            message.setSubject(title);

			            StringBuffer sb = new StringBuffer();
			            
			            String attendees = "";
			            
			            for(int i=0;i<parts.size();i++) {
			            	String name = parts.get(i).getIntern() ? parts.get(i).getName() + " SOFRECOM" : parts.get(i).getName();
							if (parts.get(i).isObligatoire()) {
								attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
										+ parts.get(i).getMail() + ";" + "\n";
							} else {
								attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=OPT-PARTICIPANT;RSVP=TRUE:MAILTO:"
										+ parts.get(i).getMail() + ";" + "\n";
							}
		                }
			            
			            if(res.isDayofmonth()) {
			            	int day = date.getDayOfMonth();
//				            int randomNum = ThreadLocalRandom.current().nextInt(10000, 99999999);
			            	StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
				                    "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" +
				                    "VERSION:2.0\n" +
									"METHOD:" + (status == CANCLEDEVENT ? "CANCEL" : "REQUEST") + "\n" +
				                    "BEGIN:VEVENT\n" +
				                    "ORGANIZER;CN="+res.getReservedby().getName()+" SOFRECOM:MAILTO:"+res.getReservedby().getMail()+"\n" +	         
				                    "ATTENDEE;CN="+res.getReservedby().getName()+" SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:"+res.getReservedby().getMail()+ "\n" + //l'organisateur ==> pourqu'il puisse recevoir la notif chez lui aussi		                  
				                    attendees+
				                    "DTSTART:"+start+"\n"+
				                    "DTEND:"+endtimeevent+"\r\n"+
				                    "RRULE:FREQ=MONTHLY;BYMONTHDAY="+day+";INTERVAL="+res.getFrequency()+";UNTIL="+end+"\n" + // reccurent event
									"LOCATION:" + res.getRoom().getName() +" étage " +res.getRoom().getIdfloor().getNumfloor()+" bloc " +res.getRoom().getIdblock().getNameblock()+ "\n" +
				                    "TRANSP:OPAQUE\n" +
				                    "SEQUENCE:0\n" +
				                    "UID:"+res.getUidmail()+"\n"+
				                    "DTSTAMP:"+start+"\n"+
				                    "CATEGORIES:Meeting\n" +
				                    "DESCRIPTION:"+res.getDescription()+"\n\n" +
				                    "SUMMARY:" + res.getDescription()+"\n" +
				                    "PRIORITY:5\n" +
				                    "CLASS:PUBLIC\n" +                  
				                    "BEGIN:VALARM\n" +
				                    "TRIGGER:PT1440M\n" +
				                    "ACTION:DISPLAY\n" +
				                    "DESCRIPTION:Reminder\n" +
				                    "END:VALARM\n" +
				                    "END:VEVENT\n" +
				                    "END:VCALENDAR");

				            // Create the message part
				            BodyPart messageBodyPart = new MimeBodyPart();

				            // Fill the message
				            messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
				            messageBodyPart.setHeader("Content-ID", "calendar_message");
				            messageBodyPart.setDataHandler(new DataHandler(
				                    new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

				            // Create a Multipart
				            Multipart multipart = new MimeMultipart();

				            // Add part one
				            multipart.addBodyPart(messageBodyPart);

				            // Put parts in message
				            message.setContent(multipart);

				            // send message
				            Transport.send(message);

			            }
			            else if (res.isDayofweek()) {               	
			            	DayOfWeek day = date.getDayOfWeek();
			            	String sday = hmap.get(day.toString());
			            	System.out.println("*******"+sday+"*******");
			            	
			            	 Calendar calendar = Calendar.getInstance();
			            	 calendar.setTime(startdate);
			            	 int n =  calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			            	    
//				            int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
			            	StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
				                    "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" +
				                    "VERSION:2.0\n" +
				                    "METHOD:REQUEST\n" +                 
				                    "BEGIN:VEVENT\n" +
				                    "ORGANIZER;CN="+res.getReservedby().getName()+" SOFRECOM:MAILTO:"+res.getReservedby().getMail()+"\n" +	         
				                    "ATTENDEE;CN="+res.getReservedby().getName()+" SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:"+res.getReservedby().getMail()+ "\n" + //l'organisateur ==> pourqu'il puisse recevoir la notif chez lui aussi		                  
				                    attendees+
				                    "DTSTART:"+start+"\n"+
				                    "DTEND:"+endtimeevent+"\r\n"+
				                    "RRULE:FREQ=MONTHLY;INTERVAL="+res.getFrequency()+";BYSETPOS="+n+";BYDAY="+sday+";UNTIL="+end+"\n" + // recuurent event
				                    "LOCATION:"+ res.getRoom().getName()+"\n" +
				                    "TRANSP:OPAQUE\n" +
				                    "SEQUENCE:0\n" +
				                    "UID:"+res.getUidmail()+"\n"+
				                    "DTSTAMP:"+start+"\n"+
				                    "CATEGORIES:Meeting\n" +
				                    "DESCRIPTION:"+res.getDescription()+"\n\n" +
				                    "SUMMARY:" + res.getDescription()+"\n" +
				                    "PRIORITY:5\n" +
				                    "CLASS:PUBLIC\n" +                  
				                    "BEGIN:VALARM\n" +
				                    "TRIGGER:PT1440M\n" +
				                    "ACTION:DISPLAY\n" +
				                    "DESCRIPTION:Reminder\n" +
				                    "END:VALARM\n" +
				                    "END:VEVENT\n" +
				                    "END:VCALENDAR");

				            // Create the message part
				            BodyPart messageBodyPart = new MimeBodyPart();

				            // Fill the message
				            messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
				            messageBodyPart.setHeader("Content-ID", "calendar_message");
				            messageBodyPart.setDataHandler(new DataHandler(
				                    new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

				            // Create a Multipart
				            Multipart multipart = new MimeMultipart();

				            // Add part one
				            multipart.addBodyPart(messageBodyPart);

				            // Put parts in message
				            message.setContent(multipart);

				            // send message
				            Transport.send(message);
			            }
			            			            
			        } catch (MessagingException me) {
			            me.printStackTrace();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			    }

		public void sendMonthlyWithnumberRec(Reservation res,List<Participant> parts) throws Exception {
						
				Date startdate = res.getStartdate();
			  	SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");	 
			  	TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Africa/Tunis"));	
			  	sdfs.setTimeZone(tz);
			  	String datestart = sdfs.format(startdate); //2011-05-10  
			  	String startday  = datestart.substring(datestart.lastIndexOf('-')+1, datestart.length());
			  	String startmonth = datestart.substring(datestart.indexOf('-')+1, datestart.lastIndexOf('-'));
			  	String startyear   = datestart.substring(0, datestart.indexOf('-'));
			  	    				  	    	
			  	Date starttime = res.getStarttime();
			  	SimpleDateFormat startt = new SimpleDateFormat("H:mm");
			  	startt.setTimeZone(tz);
			  	String timestart = startt.format(starttime);   	    		  	    		  	    
			  	String shours = timestart.substring(0,timestart.indexOf(':'));
			  	String sminutes = timestart.substring(timestart.indexOf(':')+1,timestart.length());
			  	    	
			  	Date endtime = res.getEndtime();
			  	SimpleDateFormat endt = new SimpleDateFormat("H:mm");
			  	endt.setTimeZone(tz);
			  	String timeend = endt.format(endtime);   	    	  	    		  	    	
			  	String ehours = timeend.substring(0,timeend.indexOf(':'));
			  	String eminutes = timeend.substring(timeend.indexOf(':')+1,timeend.length());
			  	    	
			  	LocalDate date = LocalDate.of(Integer.parseInt(startyear),Integer.parseInt(startmonth),Integer.parseInt(startday)); 	    	
			  	System.out.println(date);
			  	    	
			  	if(shours.length()==1) {
				    shours = '0'+shours;
				 }
				    	
				System.out.println(shours);
			  	    	
			  	String start = startyear+startmonth+startday+'T'+shours+sminutes+"00Z";
			  	System.out.println(start);
			  	    		  	    	
			    try {     
			        	
			        Properties prop = new Properties();
			        prop.put("mail.smtp.starttls.enable", starttls);
		            prop.put("mail.smtp.host", host);
		            prop.put("mail.smtp.port", port);     		        	
			        Session session = Session.getDefaultInstance(prop,null); 	         
			       
			         // Define message
			        MimeMessage message = new MimeMessage(session);
			        message.addHeaderLine("method=REQUEST");
			        message.addHeaderLine("charset=UTF-8");
			        message.addHeaderLine("component=VEVENT");
			            
			        message.setFrom(new InternetAddress(res.getReservedby().getMail()));
			        String listMails = "";
		            for(int i = 0;i<parts.size();i++) {
		            	listMails = listMails + parts.get(i).getMail()+",";
		            }	         
		            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(listMails+res.getReservedby().getMail()));		            
			        message.setSubject(res.getTitle());

			        String attendees = "";
		            
		            for(int i=0;i<parts.size();i++) {
		            	String name = parts.get(i).getIntern() ? parts.get(i).getName() + " SOFRECOM" : parts.get(i).getName();
						if (parts.get(i).isObligatoire()) {
							attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
									+ parts.get(i).getMail() + ";" + "\n";
						} else {
							attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=OPT-PARTICIPANT;RSVP=TRUE:MAILTO:"
									+ parts.get(i).getMail() + ";" + "\n";
						}
	                }
			        
			        StringBuffer sb = new StringBuffer();
			            
			        if(res.isDayofmonth()) {
			            	
			            int day = date.getDayOfMonth();
//				        int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);
			            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
				              "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" +
				              "VERSION:2.0\n" +
				              "METHOD:REQUEST\n" +                 
				              "BEGIN:VEVENT\n" +
				              "ORGANIZER;CN="+res.getReservedby().getName()+" SOFRECOM:MAILTO:"+res.getReservedby().getMail()+"\n" +	         
			                  "ATTENDEE;CN="+res.getReservedby().getName()+" SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:"+res.getReservedby().getMail()+ "\n" + //l'organisateur ==> pourqu'il puisse recevoir la notif chez lui aussi		                  
			                  attendees+
			                  "DTSTART:"+start+"\n"+
				              "RRULE:FREQ=MONTHLY;BYMONTHDAY="+day+";INTERVAL="+res.getFrequency()+";COUNT="+res.getReccurenceNumber()+
				              "LOCATION:"+ res.getRoom().getName()+"\n" +
				              "TRANSP:OPAQUE\n" +
				              "SEQUENCE:0\n" +
				              "UID:"+res.getUidmail()+"\n"+
				              "DTSTAMP:"+start+"\n"+
				              "CATEGORIES:Meeting\n" +
				              "DESCRIPTION:"+res.getDescription()+"\n\n" +
				              "SUMMARY:" + res.getDescription()+"\n" +
				              "PRIORITY:5\n" +
				              "CLASS:PUBLIC\n" +                  
				              "BEGIN:VALARM\n" +
				              "TRIGGER:PT1440M\n" +
				              "ACTION:DISPLAY\n" +
				              "DESCRIPTION:Reminder\n" +
				              "END:VALARM\n" +
				              "END:VEVENT\n" +
				              "END:VCALENDAR");

			            // Create the message part
			            BodyPart messageBodyPart = new MimeBodyPart();

			            // Fill the message
				        messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
				        messageBodyPart.setHeader("Content-ID", "calendar_message");
				        messageBodyPart.setDataHandler(new DataHandler(
				                    new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

				        // Create a Multipart
				        Multipart multipart = new MimeMultipart();

				        // Add part one
				        multipart.addBodyPart(messageBodyPart);

				        // Put parts in message
				        message.setContent(multipart);

				        // send message
				        Transport.send(message);
				            
			            }
			            
			            else if (res.isDayofweek()) {
			            	
			            	Calendar calendar = Calendar.getInstance();
			            	calendar.setTime(startdate);
			            	int n =  calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			            	
			            	DayOfWeek day = date.getDayOfWeek();
			            	String sday = hmap.get(day.toString());
			            	System.out.println("*******"+sday+"*******");
//				            int randomNum = ThreadLocalRandom.current().nextInt(10000, 99999999);
			            	StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
				                    "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" +
				                    "VERSION:2.0\n" +
				                    "METHOD:REQUEST\n" +                 
				                    "BEGIN:VEVENT\n" +
						            "ORGANIZER;CN="+res.getReservedby().getName()+" SOFRECOM:MAILTO:"+res.getReservedby().getMail()+"\n" +	         
					                "ATTENDEE;CN=DRISSI Maroua SOFRECOM;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+"maroua.drissi@sofrecom.com" +";"+ "\n" +	                   	                    
					                "ATTENDEE;CN="+res.getReservedby().getName()+" SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:"+res.getReservedby().getMail()+ "\n" + //l'organisateur ==> pourqu'il puisse recevoir la notif chez lui aussi	
				                    "DTSTART:"+start+"\n"+
				                    "RRULE:FREQ=MONTHLY;INTERVAL="+res.getFrequency()+";BYSETPOS="+n+";BYDAY="+sday+";COUNT="+res.getReccurenceNumber()+
				                    "LOCATION:"+ res.getRoom().getName()+"\n" +
				                    "TRANSP:OPAQUE\n" +
				                    "SEQUENCE:0\n" +					
				                    "UID:"+res.getUidmail()+"\n"+
				                    "DTSTAMP:"+start+"\n"+
				                    "CATEGORIES:Meeting\n" +
				                    "DESCRIPTION:"+res.getDescription()+"\n\n" +
				                    "SUMMARY:" + res.getDescription()+"\n" +
				                    "PRIORITY:5\n" +
				                    "CLASS:PUBLIC\n" +                  
				                    "BEGIN:VALARM\n" +
				                    "TRIGGER:PT1440M\n" +
				                    "ACTION:DISPLAY\n" +
				                    "DESCRIPTION:Reminder\n" +
				                    "END:VALARM\n" +
				                    "END:VEVENT\n" +
				                    "END:VCALENDAR");

				            // Create the message part
				            BodyPart messageBodyPart = new MimeBodyPart();

				            // Fill the message
				            messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
				            messageBodyPart.setHeader("Content-ID", "calendar_message");
				            messageBodyPart.setDataHandler(new DataHandler(
				                    new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

				            // Create a Multipart
				            Multipart multipart = new MimeMultipart();

				            // Add part one
				            multipart.addBodyPart(messageBodyPart);

				            // Put parts in message
				            message.setContent(multipart);

				            // send message
				            Transport.send(message);   
			            }
			            		            
			        } catch (MessagingException me) {
			            me.printStackTrace();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
						
						
					}

}
