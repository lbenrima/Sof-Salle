package tn.sofrecom.mdrissi.dto;

import java.util.ArrayList;
import java.util.List;

public class Days {
	List<DayDTO> daysLst ;
	
	public Days() {
		daysLst = new ArrayList<DayDTO>();
		daysLst.add(new DayDTO("Monday",1));
		daysLst.add(new DayDTO("Tuesday",2));
		daysLst.add(new DayDTO("Wednesday",3));
		daysLst.add(new DayDTO("Thursday",4));
		daysLst.add(new DayDTO("Friday",5));
		daysLst.add(new DayDTO("Saturday",6));	
	}

	public List<DayDTO> getDaysLst() {
		return daysLst;
	}

	public void setDaysLst(List<DayDTO> daysLst) {
		this.daysLst = daysLst;
	}
	
	
	
}
