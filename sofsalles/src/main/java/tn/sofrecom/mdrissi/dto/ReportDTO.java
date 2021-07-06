package tn.sofrecom.mdrissi.dto;

import org.springframework.stereotype.Component;

@Component
public class ReportDTO {
	
	private String from;
	private String subject;
	private String description;
	
	public ReportDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
