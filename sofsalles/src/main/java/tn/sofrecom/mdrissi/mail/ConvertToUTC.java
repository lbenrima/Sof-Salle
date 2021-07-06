package tn.sofrecom.mdrissi.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public interface ConvertToUTC {
	default String dateToUTC(Date date, Date time){
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Africa/Tunis")));
		System.out.println(cal.getTimeZone().getOffset(date.getTime()));
	    Date d = new Date(date.getTime() - cal.getTimeZone().getOffset(date.getTime()));
	    DateFormat df = new SimpleDateFormat("yyyyMMdd");
	    String sDate = df.format(d);
	    DateFormat tf = new SimpleDateFormat("HHmmss");
	    String tDate = tf.format(d);
	    sDate+="T"+tDate+"Z";
	    return sDate;
	}
	
	
	/*work around */
	default String dateForPeriodicReservation(Date date, Date time){
		date.setHours(time.getHours());
		date.setMinutes(time.getMinutes());
		 Date d = date ;
		if(TimeZone.getDefault().inDaylightTime(date)) {
			System.out.println("in DayLight");
			  d = new Date(date.getTime() + 3600000);
		}
		 DateFormat df = new SimpleDateFormat("yyyyMMdd");
		    String sDate = df.format(d);
		    DateFormat tf = new SimpleDateFormat("HHmmss");
		    String tDate = tf.format(d);
		    sDate+="T"+tDate+"Z";
		    System.out.println(sDate);
		    return sDate;
	}
}
