package tn.sofrecom.mdrissi.mail;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
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

import tn.sofrecom.mdrissi.dto.DayDTO;
import tn.sofrecom.mdrissi.dto.WeeklyRes;
import tn.sofrecom.mdrissi.entities.Participant;
import tn.sofrecom.mdrissi.entities.Reservation;

@Component
public class WeeklyMailService implements ConvertToUTC {

	HashMap<String, String> hmap = new HashMap<String, String>();
private final int CANCLEDEVENT = 0;
	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private String port;

	@Value("${mail.smtp.starttls.enable}")
	private String starttls;

	public WeeklyMailService() {
		hmap.put("Monday", "MO");
		hmap.put("Tuesday", "TU");
		hmap.put("Wednesday", "WE");
		hmap.put("Thursday", "TH");
		hmap.put("Friday", "FR");
		hmap.put("Saturday", "SA");
	}

	public void sendWeeklyWithEnddate(WeeklyRes weeklyRes, List<Participant> parts, int status) throws Exception {
		Reservation res = weeklyRes.getReservation();
		List<DayDTO> sdays = weeklyRes.getSelected();

		String days = "";
		for (int i = 0; i < sdays.size(); i++) {
			String day = hmap.get(sdays.get(i).getName());
			days = days.concat(day + ",");
		}

		days = days.substring(0, days.length() - 1);

		 String start = dateForPeriodicReservation(res.getStartdate(), res.getStarttime());
	  	    String end = dateForPeriodicReservation(res.getEnddate(), res.getEndtime());
	  	    String endtimeevent = dateForPeriodicReservation(res.getStartdate(), res.getEndtime());

		try {

			Properties prop = new Properties();
			prop.put("mail.smtp.starttls.enable", starttls);
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			Session session = Session.getDefaultInstance(prop, null);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.addHeaderLine("method=REQUEST");
			message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=VEVENT");

			message.setFrom(new InternetAddress(res.getReservedby().getMail()));
			String listMails = "";
			for (int i = 0; i < parts.size(); i++) {
				listMails = listMails + parts.get(i).getMail() + ",";
			}
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(listMails + res.getReservedby().getMail()));
			String title = (status == CANCLEDEVENT) ? "Annuler " + res.getTitle() : res.getTitle();
			message.setSubject(title);

			StringBuffer sb = new StringBuffer();
			// int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);

			String attendees = "";

			for (int i = 0; i < parts.size(); i++) {
				String name = parts.get(i).getIntern() ? parts.get(i).getName() + " SOFRECOM" : parts.get(i).getName();
				if (parts.get(i).isObligatoire()) {
					attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
							+ parts.get(i).getMail() + ";" + "\n";
				} else {
					attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=OPT-PARTICIPANT;RSVP=TRUE:MAILTO:"
							+ parts.get(i).getMail() + ";" + "\n";
				}
			}

			StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"
					+ "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" + "VERSION:2.0\n" 
                    +"METHOD:" + (status == CANCLEDEVENT ? "CANCEL" : "REQUEST") + "\n"
					+ "BEGIN:VEVENT\n" + "ORGANIZER;CN=" + res.getReservedby().getName() + " SOFRECOM:MAILTO:"
					+ res.getReservedby().getMail() + "\n" + "ATTENDEE;CN=" + res.getReservedby().getName()
					+ " SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:" + res.getReservedby().getMail() + "\n" + // l'organisateur
																										// ==> pourqu'il
																										// puisse
																										// recevoir la
																										// notif chez
																										// lui aussi
					attendees + "DTSTART:"  + start + "\n" + "DTEND:"+endtimeevent+"\r\n"+ "RRULE:FREQ=WEEKLY;BYDAY=" + days + ";INTERVAL="
					+ res.getFrequency() + ";WKST=SU;UNTIL=" + end + "\n" + // reccurent event
					// "DTEND:"+end+"\n"+
					"LOCATION:" + res.getRoom().getName() +" étage " +res.getRoom().getIdfloor().getNumfloor()+" bloc " +res.getRoom().getIdblock().getNameblock()+ "\n" 
					+ "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:"
					+ res.getUidmail() + "\n" + "DTSTAMP:" + start + "\n" + "CATEGORIES:Meeting\n" + "DESCRIPTION:"
					+ res.getDescription() + "\n\n" + "SUMMARY:" + res.getDescription() + "\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n" + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID", "calendar_message");
			messageBodyPart
					.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very
																													// important

			// Create a Multipart
			Multipart multipart = new MimeMultipart();

			// Add part one
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// send message
			Transport.send(message);

		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendWeeklyWithnumberRec(WeeklyRes weeklyRes, List<Participant> parts) throws Exception {
		System.out.println("sendWeeklyWithnumberRec WeeklyMailService");
		Reservation res = weeklyRes.getReservation();
		List<DayDTO> sdays = weeklyRes.getSelected();

		String days = "";
		for (int i = 0; i < sdays.size(); i++) {
			String day = hmap.get(sdays.get(i).getName());
			System.out.println(day);
			days = days.concat(day + ",");
			System.out.println(days);
		}
		System.out.println(days);

		days = days.substring(0, days.length() - 1);

		System.out.println(days);

		String start = dateToUTC(res.getStartdate(), res.getStarttime());

		try {

			Properties prop = new Properties();
			prop.put("mail.smtp.starttls.enable", starttls);
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			Session session = Session.getDefaultInstance(prop, null);

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.addHeaderLine("method=REQUEST");
			message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=VEVENT");

			message.setFrom(new InternetAddress(res.getReservedby().getMail()));
			String listMails = "";
			for (int i = 0; i < parts.size(); i++) {
				listMails = listMails + parts.get(i).getMail() + ",";
			}
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(listMails + res.getReservedby().getMail()));
			message.setSubject(res.getTitle());

			StringBuffer sb = new StringBuffer();

			// int randomNum = ThreadLocalRandom.current().nextInt(10000, 9999999);

			String attendees = "";

			for (int i = 0; i < parts.size(); i++) {
				String name = parts.get(i).getIntern() ? parts.get(i).getName() + " SOFRECOM" : parts.get(i).getName();
				if (parts.get(i).isObligatoire()) {
					attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
							+ parts.get(i).getMail() + ";" + "\n";
				} else {
					attendees = attendees + "ATTENDEE;CN=" + name + ";ROLE=OPT-PARTICIPANT;RSVP=TRUE:MAILTO:"
							+ parts.get(i).getMail() + ";" + "\n";
				}
			}

			StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"
					+ "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" + "VERSION:2.0\n" + "METHOD:REQUEST\n"
					+ "BEGIN:VEVENT\n" + "ORGANIZER;CN=" + res.getReservedby().getName() + " SOFRECOM:MAILTO:"
					+ res.getReservedby().getMail() + "\n" + "ATTENDEE;CN=" + res.getReservedby().getName()
					+ " SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:" + res.getReservedby().getMail() + "\n" + // l'organisateur
																										// ==> pourqu'il
																										// puisse
																										// recevoir la
																										// notif chez
																										// lui aussi
					attendees + "DTSTART:" + start + "\n" + "RRULE:FREQ=WEEKLY;BYDAY=" + days + ";INTERVAL="
					+ res.getFrequency() + ";WKST=SU;COUNT=" + res.getReccurenceNumber() +
					// "DTEND:"+end+"\n"+
					"LOCATION:" + res.getRoom().getName() + "\n" + "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:"
					+ res.getUidmail() + "\n" + "DTSTAMP:" + start + "\n" + "CATEGORIES:Meeting\n" + "DESCRIPTION:"
					+ res.getDescription() + "\n\n" + "SUMMARY:" + res.getDescription() + "\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n" + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID", "calendar_message");
			messageBodyPart
					.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very
																													// important

			// Create a Multipart
			Multipart multipart = new MimeMultipart();

			// Add part one
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// send message
			Transport.send(message);

		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
