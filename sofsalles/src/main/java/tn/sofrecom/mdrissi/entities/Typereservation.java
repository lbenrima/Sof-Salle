/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "typereservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typereservation.findAll", query = "SELECT t FROM Typereservation t"),
    @NamedQuery(name = "Typereservation.findById", query = "SELECT t FROM Typereservation t WHERE t.id = :id"),
    @NamedQuery(name = "Typereservation.findByName", query = "SELECT t FROM Typereservation t WHERE t.name = :name")})
public class Typereservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeres")
    @JsonIgnore
    @Transient
    private List<Reservation> reservationList;

    public Typereservation() {
    }

    public Typereservation(Integer id) {
        this.id = id;
    }

    public Typereservation(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    @XmlTransient
//    public List<Reservation> getReservationList() {
//        return reservationList;
//    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
    
    public void addToReservationList(Reservation res) {
    	this.reservationList.add(res);
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
        if (!(object instanceof Typereservation)) {
            return false;
        }
        Typereservation other = (Typereservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Typereservation[ id=" + id + " ]";
    }
    
}
