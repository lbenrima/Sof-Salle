package tn.sofrecom.mdrissi.mail;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tn.sofrecom.mdrissi.entities.Participant;
import tn.sofrecom.mdrissi.entities.Reservation;
import tn.sofrecom.mdrissi.entities.Reservationchild;

@Component
public class SimpleMailService implements ConvertToUTC {

	private final int CREATEEVENT = 1;
	private final int CANCLEDEVENT = 0;
	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.port}")
	private String port;

	@Value("${mail.smtp.starttls.enable}")
	private String starttls;

	public SimpleMailService() {

	}

	public void sendSimple(Reservation res, List<Participant> parts, int status) throws Exception {

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
			// String listMailscc = "";
			// parts.stream().map(Participant::getMail).collect(Collectors.joining(","));

			for (int i = 0; i < parts.size(); i++) {
				// if(parts.get(i).isObligatoire()) {
				listMails = listMails + parts.get(i).getMail() + ",";
				// }else {
				// listMailscc = listMailscc + parts.get(i).getMail()+",";
				// }

			}
			System.out.println("listMails:" + listMails);
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(listMails + res.getReservedby().getMail()));
			String title = (status == CANCLEDEVENT) ? "Annuler " + res.getTitle() : res.getTitle();
			// String title =res.getTitle();
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
					+ "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" + "VERSION:2.0\n" +
					// CANCEL,REQUEST
					"METHOD:" + (status == CANCLEDEVENT ? "CANCEL" : "REQUEST") + "\n" + "BEGIN:VEVENT\n"
					+ "ORGANIZER;CN=" + res.getReservedby().getName() + " SOFRECOM:MAILTO:"
					+ res.getReservedby().getMail() + "\n" + "ATTENDEE;CN=" + res.getReservedby().getName()
					+ " SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:" + res.getReservedby().getMail() + "\n" + // l'organisateur
																										// ==> pourque
																										// l'event
																										// s'ajoute chez
																										// lui aussi
					attendees + "DTSTART:" + start + "\n" + "DTEND:" + end + "\n" + 
					"LOCATION:" + res.getRoom().getName() +" étage " +res.getRoom().getIdfloor().getNumfloor()+" bloc " +res.getRoom().getIdblock().getNameblock()+ "\n" 
					+ "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:" + res.getUidmail()
					+ "\n" +
					// CONFIRMED,CANCELLED
					"STATUS:" + (status == CANCLEDEVENT ? "CANCELLED" : "CONFIRMED") + "\r\n" + "DTSTAMP:" + start
					+ "\n" +
					// Réunion,Annuler réunion
					"CATEGORIES:" + (status == CANCLEDEVENT ? "Annuler réunion" : "Réunion") + "\n" + "DESCRIPTION:"
					+ res.getDescription() + "\n\n" + "SUMMARY:" + res.getDescription() + "\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n" + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
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
	public void deleteOccurence(Reservationchild resch, List<Participant> parts) throws Exception {

		String start = dateToUTC(resch.getStartdate(), resch.getStarttime());
		String end = dateToUTC(resch.getEnddate(), resch.getEndtime());

		Reservation res = resch.getIdreservation();
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
			// String listMailscc = "";
			// parts.stream().map(Participant::getMail).collect(Collectors.joining(","));

			for (int i = 0; i < parts.size(); i++) {
				// if(parts.get(i).isObligatoire()) {
				listMails = listMails + parts.get(i).getMail() + ",";
				// }else {
				// listMailscc = listMailscc + parts.get(i).getMail()+",";
				// }

			}
			System.out.println("listMails:" + listMails);
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(listMails + res.getReservedby().getMail()));
			String title = "Annuler " + res.getTitle();
			// String title =res.getTitle();
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
					+ "PRODID:-//Microsoft Corporation//Outlook 10 MIMEDIR//EN\n" + "VERSION:2.0\n" +
					// CANCEL,REQUEST
					"METHOD:" +  "CANCEL" + "\n" + "BEGIN:VEVENT\n"
					+ "ORGANIZER;CN=" + res.getReservedby().getName() + " SOFRECOM:MAILTO:"
					+ res.getReservedby().getMail() + "\n" + "ATTENDEE;CN=" + res.getReservedby().getName()
					+ " SOFRECOM;PARTSTAT=ACCEPTED:MAILTO:" + res.getReservedby().getMail() + "\n" + // l'organisateur
																										// ==> pourque
																										// l'event
																										// s'ajoute chez
																										// lui aussi
					attendees + "DTSTART:" + start + "\n" + "DTEND:" + end + "\n" + 
					"LOCATION:" + res.getRoom().getName() +" étage " +res.getRoom().getIdfloor().getNumfloor()+" bloc " +res.getRoom().getIdblock().getNameblock()+ "\n" 
					+ "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:" + res.getUidmail()
					+ "\n" +
					// CONFIRMED,CANCELLED
					"STATUS:" +  "CANCELLED"  + "\r\n" + "DTSTAMP:" + start
					+ "\n" +
					"RECURRENCE-ID:" +start+ 
					// Réunion,Annuler réunion
					"CATEGORIES:" + "Annuler réunion" + "\n" + "DESCRIPTION:"
					+ res.getDescription() + "\n\n" + "SUMMARY:" + res.getDescription() + "\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n" + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
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
