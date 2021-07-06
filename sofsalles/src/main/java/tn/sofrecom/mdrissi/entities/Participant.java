/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "participant")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Participant.findAll", query = "SELECT p FROM Participant p"),
		@NamedQuery(name = "Participant.findByIdparticipant", query = "SELECT p FROM Participant p WHERE p.idparticipant = :idparticipant"),
		@NamedQuery(name = "Participant.findByName", query = "SELECT p FROM Participant p WHERE p.name = :name"),
		@NamedQuery(name = "Participant.findByMail", query = "SELECT p FROM Participant p WHERE p.mail = :mail"),
		@NamedQuery(name = "Participant.findByIntern", query = "SELECT p FROM Participant p WHERE p.intern = :intern"), })
public class Participant implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idparticipant")
	private Integer idparticipant;
	@Basic(optional = false)
	@NotNull
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Column(name = "mail")
	private String mail;
	@Basic(optional = false)
	@NotNull
	@Column(name = "intern")
	private boolean intern;

	@Basic(optional = false)
	@Column(name = "obligatoire", columnDefinition = "TINYINT")
	private boolean obligatoire;

	@JoinColumn(name = "reservationid", referencedColumnName = "idreservation")
	@ManyToOne(optional = false)
	@JsonIgnore
	private Reservation reservationid;

	public Participant() {
	}

	public Participant(Integer idparticipant) {
		this.idparticipant = idparticipant;
	}

	public Participant(Integer idparticipant, String name, String mail, boolean intern) {
		this.idparticipant = idparticipant;
		this.name = name;
		this.mail = mail;
		this.intern = intern;
	}

	public Integer getIdparticipant() {
		return idparticipant;
	}

	public void setIdparticipant(Integer idparticipant) {
		this.idparticipant = idparticipant;
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

	public boolean getIntern() {
		return intern;
	}

	public void setIntern(boolean intern) {
		this.intern = intern;
	}

	public Reservation getReservationid() {
		return reservationid;
	}

	public void setReservationid(Reservation reservationid) {
		this.reservationid = reservationid;
	}

	public boolean isObligatoire() {
		return obligatoire;
	}

	public void setObligatoire(boolean obligatoire) {
		this.obligatoire = obligatoire;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idparticipant != null ? idparticipant.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Participant)) {
			return false;
		}
		Participant other = (Participant) object;
		if ((this.idparticipant == null && other.idparticipant != null)
				|| (this.idparticipant != null && !this.idparticipant.equals(other.idparticipant))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "tn.sofrecom.mdrissi.entities.Participant[ idparticipant=" + idparticipant + " ]";
	}

}
