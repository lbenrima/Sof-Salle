package tn.sofrecom.mdrissi.services.collaborateurs;

public class Collaborateur {

	private String firstName;
	private String lastName;
	private String mail;
	private String occupation;
	private boolean obligatoire;
	
	public Collaborateur() {
		
	}
	public Collaborateur(String firstName, String lastName, String mail, String occupation,boolean obligatoire) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.occupation = occupation;
		this.obligatoire = obligatoire;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMail() {
		return mail;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public boolean getObligatoire() {
		return obligatoire;
	}
	public void setObligatoire(boolean obligatoire) {
		this.obligatoire = obligatoire;
	}
	@Override
	public String toString() {
		return "Collaborateur [firstName=" + firstName + ", lastName=" + lastName + ", mail=" + mail + ", occupation="
				+ occupation + "]";
	}
	
	
}
