package tn.sofrecom.mdrissi.services.collaborateurs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tn.sofrecom.mdrissi.entities.User;
import tn.sofrecom.mdrissi.repositories.UserRepository;

/**
 * @author m.drissi
 *
 */
@Component
public class ListCollaborateurs {

	private List<Collaborateur> collabs;
	
	@Autowired
	UserRepository usersRepo;
	
	public ListCollaborateurs() {	
		
		/*this.addToList(new Collaborateur("Maroua","DRISSI","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("Emna","DRISSI","emna.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("sabri","mtibaa","sabri.mtibaa@sofrecom.com","manager"));
		this.addToList(new Collaborateur("leila","saidi","maroua.drissi@sofrecom.com","architect"));
		this.addToList(new Collaborateur("walid","walid","maroua.drissi@sofrecom.com","RH"));
		this.addToList(new Collaborateur("samir","dridi","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("ali","benali","maroua.drissi@sofrecom.com","manager"));
		this.addToList(new Collaborateur("marwen","marwen","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("lamia","lamia","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("manar","manar","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("maram","saidi","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("jihen","jiji","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("yousef","benamor","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("zied","zied","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("seif","drissi","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("amel","ouesleti","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("salem","bensalem","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("amira","azouz","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("mehdi","ayari","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("sami","ghorbel","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("achref","achref","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("maher","maher","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("omar","omar","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("amine","amine","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("amina","amina","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("maroua","maroua","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("amir","amir","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("mariem","mariem","maroua.drissi@sofrecom.com","developer"));
		//this.addToList(new Collaborateur("jamil","jamil","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("wiem","wiem","maroua.drissi@sofrecom.com","developer"));
		this.addToList(new Collaborateur("takoua","takoua","maroua.drissi@sofrecom.com","developer"));*/
	}
	
	
	public List<Collaborateur> setUsersCollabs(){
		
		collabs = new ArrayList<Collaborateur>();
		// récupérer la liste des collaborateurs de sofrecom et l'insérer dans collabs
		//System.out.println("****"+usersRepo.toString());
		List<User> users = new ArrayList<User>();	
		users = usersRepo.findAll();
		System.out.println("****"+users.size());
		for(int i=0;i<users.size();i++) {
			Collaborateur collab = new Collaborateur();
			collab.setFirstName(users.get(i).getName());
			collab.setLastName(users.get(i).getName());
			collab.setMail(users.get(i).getMail());
			collab.setOccupation("sofrecom tn");
			this.addToList(collab);
		}
		
		return collabs;
	}
	
	public List<Collaborateur> getCollabs() {
		return collabs;
	}
	
	
	public void setCollabs(List<Collaborateur> collabs) {
		this.collabs = collabs;
	}
	
	public void addToList(Collaborateur collab) {
		//System.out.println(collab.toString());
		 this.collabs.add(collab);
	}
	
	public void showList() {
		for(int i=0; i<this.collabs.size(); i++) 
            System.out.println(this.collabs.get(i).toString()); 
	}
	
	public Boolean check(User user) {
		String fullName; 
		Boolean checkResult = false ;
		for(int i=0; i<collabs.size(); i++) { 
			fullName = collabs.get(i).getLastName();//+ " " + collabs.get(i).getFirstName();
			//System.out.println("----"+fullName+"--->"+user.getName()); 
			if((user.getName().equals(fullName)) && (user.getMail().equals((collabs).get(i).getMail()))){
				checkResult = true;
				break;
				}
				
		}
		return checkResult;
	}
	
}
