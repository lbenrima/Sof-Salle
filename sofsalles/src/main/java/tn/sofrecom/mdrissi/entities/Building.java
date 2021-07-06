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
import javax.persistence.Lob;
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
@Table(name = "building")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Building.findAll", query = "SELECT b FROM Building b"),
    @NamedQuery(name = "Building.findByIdbuiding", query = "SELECT b FROM Building b WHERE b.idbuiding = :idbuiding"),
    @NamedQuery(name = "Building.findByNamebuilding", query = "SELECT b FROM Building b WHERE b.namebuilding = :namebuilding")})
public class Building implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbuiding")
    private Integer idbuiding;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "namebuilding")
    private String namebuilding;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idbuilding")
    @JsonIgnore
    private List<Room> meetingroomList;

    public Building() {
    }

    public Building(Integer idbuiding) {
        this.idbuiding = idbuiding;
    }

    public Building(Integer idbuiding, String namebuilding, String description) {
        this.idbuiding = idbuiding;
        this.namebuilding = namebuilding;
        this.description = description;
    }

    public Integer getIdbuiding() {
        return idbuiding;
    }

    public void setIdbuiding(Integer idbuiding) {
        this.idbuiding = idbuiding;
    }

    public String getNamebuilding() {
        return namebuilding;
    }

    public void setNamebuilding(String namebuilding) {
        this.namebuilding = namebuilding;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Room> getMeetingroomList() {
        return meetingroomList;
    }

    public void setMeetingroomList(List<Room> meetingroomList) {
        this.meetingroomList = meetingroomList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idbuiding != null ? idbuiding.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Building)) {
            return false;
        }
        Building other = (Building) object;
        if ((this.idbuiding == null && other.idbuiding != null) || (this.idbuiding != null && !this.idbuiding.equals(other.idbuiding))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Building[ idbuiding=" + idbuiding + " ]";
    }
    
}
