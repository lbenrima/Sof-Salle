package tn.sofrecom.mdrissi.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ReservationDTO {
	
	private int id ;
	private int resourceId;
	private String title;
	private String start;
	private String end;
	private Date startDate;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	private Long idchild;
	private boolean allDay;
	private String color;
	private String description;
	private boolean simple;
	private boolean daily;
	private boolean weekly;
	private boolean monthly;
	private boolean yearly;
	private String type;
	private int userId;
	private String reservedBy;  
	private String mailReservedBy; 
	
	public ReservationDTO() {
			
		}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getIdchild() {
		return idchild;
	}

	public void setIdchild(Long idchild) {
		this.idchild = idchild;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(String rreservedBy) {
		this.reservedBy = rreservedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}	
	
	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public boolean isSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public boolean isWeekly() {
		return weekly;
	}

	public void setWeekly(boolean weekly) {
		this.weekly = weekly;
	}

	public boolean isMonthly() {
		return monthly;
	}

	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	public boolean isYearly() {
		return yearly;
	}

	public void setYearly(boolean yearly) {
		this.yearly = yearly;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getMailReservedBy() {
		return mailReservedBy;
	}
	public void setMailReservedBy(String mailReservedBy) {
		this.mailReservedBy = mailReservedBy;
	}
	
}

