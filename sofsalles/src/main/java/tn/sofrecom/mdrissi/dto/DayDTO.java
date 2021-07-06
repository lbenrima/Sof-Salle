package tn.sofrecom.mdrissi.dto;

public class DayDTO {
	
	String name;
	int numberd;
	
	public DayDTO() {
		
	}
	
	public DayDTO(String name, int numberd) {
		super();
		this.name = name;
		this.numberd = numberd;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberd() {
		return numberd;
	}
	public void setNumberd(int numberd) {
		this.numberd = numberd;
	}
	

}
