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
@Table(name = "visibilitytype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visibilitytype.findAll", query = "SELECT v FROM Visibilitytype v"),
    @NamedQuery(name = "Visibilitytype.findById", query = "SELECT v FROM Visibilitytype v WHERE v.id = :id"),
    @NamedQuery(name = "Visibilitytype.findByTypev", query = "SELECT v FROM Visibilitytype v WHERE v.typev = :typev")})
public class Visibilitytype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "typev")
    private String typev;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visibilitytyperes")
    @Transient
    @JsonIgnore
    private List<Reservation> reservationList;

    public Visibilitytype() {
    }

    public Visibilitytype(Integer id) {
        this.id = id;
    }

    public Visibilitytype(Integer id, String typev, String description) {
        this.id = id;
        this.typev = typev;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypev() {
        return typev;
    }

    public void setTypev(String typev) {
        this.typev = typev;
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
        if (!(object instanceof Visibilitytype)) {
            return false;
        }
        Visibilitytype other = (Visibilitytype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Visibilitytype[ id=" + id + " ]";
    }
    
}
