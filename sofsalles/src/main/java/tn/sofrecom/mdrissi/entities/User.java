/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
//    @NamedQuery(name = "User.findByMail", query = "SELECT u FROM User u WHERE u.mail = :mail"),
    @NamedQuery(name = "User.findByStateuser", query = "SELECT u FROM User u WHERE u.stateuser = :stateuser")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "mail")
    private String mail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "stateuser")
    private boolean stateuser;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "profileuser")
    private boolean profileuser;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservedby")
    @JsonIgnore
    private List<Reservation> reservationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser")
    @JsonIgnore
    private List<Grparticipants> grparticipantsList;

    @ManyToMany
   private List<Room> rooms;
    
    public List<Room> getRooms(){
    	return this.rooms;
    }
    public void setRooms(List<Room> rooms) {
    	this.rooms = rooms;
    }
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String mail, boolean stateuser, boolean profileuser) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.stateuser = stateuser;
        this.profileuser = profileuser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean getStateuser() {
        return stateuser;
    }

    public void setStateuser(boolean stateuser) {
        this.stateuser = stateuser;
    }
    

    public boolean getProfileuser() {
		return profileuser;
	}

	public void setProfileuser(boolean profileuser) {
		this.profileuser = profileuser;
	}

	@XmlTransient
    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @XmlTransient
    public List<Grparticipants> getGrparticipantsList() {
        return grparticipantsList;
    }

    public void setGrparticipantsList(List<Grparticipants> grparticipantsList) {
        this.grparticipantsList = grparticipantsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.User[ id=" + id + " ]";
    }

}
