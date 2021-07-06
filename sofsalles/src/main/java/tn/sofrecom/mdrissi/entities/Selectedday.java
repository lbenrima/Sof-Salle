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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "selectedday")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Selectedday.findAll", query = "SELECT s FROM Selectedday s"),
    @NamedQuery(name = "Selectedday.findById", query = "SELECT s FROM Selectedday s WHERE s.id = :id"),
    @NamedQuery(name = "Selectedday.findByName", query = "SELECT s FROM Selectedday s WHERE s.name = :name"),
    @NamedQuery(name = "Selectedday.findByNumberd", query = "SELECT s FROM Selectedday s WHERE s.numberd = :numberd")})
public class Selectedday implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numberd")
    private int numberd;
    @JoinColumn(name = "idreservation", referencedColumnName = "idreservation")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Reservation idreservation;

    public Selectedday() {
    }

    public Selectedday(Integer id) {
        this.id = id;
    }

    public Selectedday(Integer id, String name, int numberd) {
        this.id = id;
        this.name = name;
        this.numberd = numberd;
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

    public int getNumberd() {
        return numberd;
    }

    public void setNumberd(int numberd) {
        this.numberd = numberd;
    }

    public Reservation getIdreservation() {
        return idreservation;
    }

    public void setIdreservation(Reservation idreservation) {
        this.idreservation = idreservation;
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
        if (!(object instanceof Selectedday)) {
            return false;
        }
        Selectedday other = (Selectedday) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Selectedday[ id=" + id + " ]";
    }
    
}
