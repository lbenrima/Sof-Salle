package tn.sofrecom.mdrissi.mail;

import java.util.List;
import java.util.Properties;

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
public class YearlyMailService implements ConvertToUTC {

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private String port;

	@Value("${mail.smtp.starttls.enable}")
	private String starttls;

	public YearlyMailService() {
	}

	public void sendYearlyWithEnddate(Reservation res, List<Participant> parts) throws Exception {

		String start = dateToUTC(res.getStartdate(), res.getStarttime());
		String end = dateToUTC(res.getEnddate(), res.getEndtime());

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
																										// ==> pourque
																										// l'event
																										// s'ajoute chez
																										// lui aussi
					attendees + "DTSTART:" + start + "\n" + "RRULE:FREQ=YEARLY;INTERVAL=" + res.getFrequency()
					+ ";UNTIL=" + end + "\n" + // reccurent event
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

	public void sendYearlyWithnumberRec(Reservation res, List<Participant> parts) throws Exception {

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
																										// ==> pourque
																										// l'event
																										// s'ajoute chez
																										// lui aussi
					attendees + "DTSTART:" + start + "\n" + "RRULE:FREQ=YEARLY;INTERVAL=" + res.getFrequency()
					+ ";COUNT=" + res.getReccurenceNumber() + "LOCATION:" + res.getRoom().getName() + "\n"
					+ "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:" + res.getUidmail() + "\n" + "DTSTAMP:" + start + "\n"
					+ "CATEGORIES:Meeting\n" + "DESCRIPTION:" + res.getDescription() + "\n\n" + "SUMMARY:"
					+ res.getDescription() + "\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
					+ "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
					+ "END:VEVENT\n" + "END:VCALENDAR");

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
