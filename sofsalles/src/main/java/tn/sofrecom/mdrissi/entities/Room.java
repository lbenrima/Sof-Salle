package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
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
@Table(name = "room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Room.findAll", query = "SELECT r FROM Room r"),
    @NamedQuery(name = "Room.findByIdroom", query = "SELECT r FROM Room r WHERE r.idroom = :idroom"),
    @NamedQuery(name = "Room.findByName", query = "SELECT r FROM Room r WHERE r.name = :name"),
    @NamedQuery(name = "Room.findByCapacity", query = "SELECT r FROM Room r WHERE r.capacity = :capacity"),
    @NamedQuery(name = "Room.findByAdresse", query = "SELECT r FROM Room r WHERE r.adresse = :adresse"),
    @NamedQuery(name = "Room.findByTelephone", query = "SELECT r FROM Room r WHERE r.telephone = :telephone"),
    @NamedQuery(name = "Room.findByVideoproj", query = "SELECT r FROM Room r WHERE r.videoproj = :videoproj"),
    @NamedQuery(name = "Room.findByPontteleph", query = "SELECT r FROM Room r WHERE r.pontteleph = :pontteleph")})

@SqlResultSetMapping(name="availableRooms", 
entities={ 
    @EntityResult(entityClass=Room.class), 
    @EntityResult(entityClass=Floor.class),
    @EntityResult(entityClass=Building.class),
    @EntityResult(entityClass=Block.class)
}) 
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idroom")
    private Integer idroom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "capacity")
    private int capacity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "adresse")
    private String adresse;
    
    private String color;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telephone")
    private int telephone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "videoproj")
    private boolean videoproj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pontteleph")
    private boolean pontteleph;
    @Basic(optional = false)
    @NotNull
    @Column(name = "visio")
    private boolean visio;
    @JoinColumn(name = "idbuilding", referencedColumnName = "idbuiding")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Building idbuilding;
    @JoinColumn(name = "idfloor", referencedColumnName = "idfloor")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Floor idfloor;
    @JoinColumn(name = "idblock", referencedColumnName = "idblock")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Block idblock;
    @Transient
    @JsonIgnore
    private List<Reservation> reservationList;

    public Room() {
    }

    public Room(Integer idroom) {
        this.idroom = idroom;
    }

    public Room(Integer idroom, String name, int capacity, String adresse, int telephone, boolean videoproj, boolean pontteleph) {
        this.idroom = idroom;
        this.name = name;
        this.capacity = capacity;
        this.adresse = adresse;
        this.telephone = telephone;
        this.videoproj = videoproj;
        this.pontteleph = pontteleph;
    }

    public Integer getIdroom() {
        return idroom;
    }

    public void setIdroom(Integer idroom) {
        this.idroom = idroom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public boolean getVideoproj() {
        return videoproj;
    }

    public void setVideoproj(boolean videoproj) {
        this.videoproj = videoproj;
    }

    public boolean getPontteleph() {
        return pontteleph;
    }

    public void setPontteleph(boolean pontteleph) {
        this.pontteleph = pontteleph;
    }

    public Building getIdbuilding() {
        return idbuilding;
    }

    public void setIdbuilding(Building idbuilding) {
        this.idbuilding = idbuilding;
    }

    public Floor getIdfloor() {
        return idfloor;
    }

    public void setIdfloor(Floor idfloor) {
        this.idfloor = idfloor;
    }

    public Block getIdblock() {
        return idblock;
    }

    public void setIdblock(Block idblock) {
        this.idblock = idblock;
    }

    public boolean isVisio() {
		return visio;
	}

	public void setVisio(boolean visio) {
		this.visio = visio;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idroom != null ? idroom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.idroom == null && other.idroom != null) || (this.idroom != null && !this.idroom.equals(other.idroom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Room[ idroom=" + idroom + " ]";
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
    
}
