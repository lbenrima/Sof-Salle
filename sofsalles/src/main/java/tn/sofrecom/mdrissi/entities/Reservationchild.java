/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "reservationchild")
@SqlResultSetMapping(name="CurrentReservationsChild", 
entities={ 
    @EntityResult(entityClass=Reservationchild.class),
    @EntityResult(entityClass=Reservation.class),
    @EntityResult(entityClass=Room.class),
    @EntityResult(entityClass=Floor.class),
    @EntityResult(entityClass=Building.class),
    @EntityResult(entityClass=Block.class)
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservationchild.findAll", query = "SELECT r FROM Reservationchild r"),
    @NamedQuery(name = "Reservationchild.findById", query = "SELECT r FROM Reservationchild r WHERE r.id = :id"),
    @NamedQuery(name = "Reservationchild.findByStartdate", query = "SELECT r FROM Reservationchild r WHERE r.startdate = :startdate"),
    @NamedQuery(name = "Reservationchild.findByEnddate", query = "SELECT r FROM Reservationchild r WHERE r.enddate = :enddate"),
    @NamedQuery(name = "Reservationchild.findByStarttime", query = "SELECT r FROM Reservationchild r WHERE r.starttime = :starttime"),
    @NamedQuery(name = "Reservationchild.findByEndtime", query = "SELECT r FROM Reservationchild r WHERE r.endtime = :endtime")})
public class Reservationchild implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "startdate")
    @Temporal(TemporalType.DATE)
    private Date startdate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "enddate")
    @Temporal(TemporalType.DATE)
    private Date enddate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "starttime")
    @Temporal(TemporalType.TIME)
    private Date starttime;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "endtime")
    @Temporal(TemporalType.TIME)
    private Date endtime;
    @JoinColumn(name = "idreservation", referencedColumnName = "idreservation")
    @ManyToOne(optional = false)
    private Reservation idreservation;

    public Reservationchild() {
    }

    public Reservationchild(Long id) {
        this.id = id;
    }

    public Reservationchild(Long id, Date startdate, Date enddate, Date starttime, Date endtime) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
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
        if (!(object instanceof Reservationchild)) {
            return false;
        }
        Reservationchild other = (Reservationchild) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Reservationchild[ id=" + id + " ]";
    }
    
}
