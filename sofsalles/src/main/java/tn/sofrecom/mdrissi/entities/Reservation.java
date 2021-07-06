/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.sofrecom.mdrissi.entities;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tn.sofrecom.mdrissi.dto.DayDTO;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "reservation")
@SqlResultSetMapping(name = "CurrentReservations", entities = { @EntityResult(entityClass = Reservation.class),
		@EntityResult(entityClass = Room.class), @EntityResult(entityClass = User.class)

})
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r"),
		@NamedQuery(name = "Reservation.findByIdreservation", query = "SELECT r FROM Reservation r WHERE r.idreservation = :idreservation"),
		@NamedQuery(name = "Reservation.findByTitle", query = "SELECT r FROM Reservation r WHERE r.title = :title"),
		@NamedQuery(name = "Reservation.findByColor", query = "SELECT r FROM Reservation r WHERE r.color = :color"),
		@NamedQuery(name = "Reservation.findBySimple", query = "SELECT r FROM Reservation r WHERE r.simple = :simple"),
		@NamedQuery(name = "Reservation.findByDaily", query = "SELECT r FROM Reservation r WHERE r.daily = :daily"),
		@NamedQuery(name = "Reservation.findByWeekly", query = "SELECT r FROM Reservation r WHERE r.weekly = :weekly"),
		@NamedQuery(name = "Reservation.findByMonthly", query = "SELECT r FROM Reservation r WHERE r.monthly = :monthly"),
		@NamedQuery(name = "Reservation.findByYearly", query = "SELECT r FROM Reservation r WHERE r.yearly = :yearly"),
		@NamedQuery(name = "Reservation.findByStartdate", query = "SELECT r FROM Reservation r WHERE r.startdate = :startdate"),
		@NamedQuery(name = "Reservation.findByEnddate", query = "SELECT r FROM Reservation r WHERE r.enddate = :enddate"),
		@NamedQuery(name = "Reservation.findByFrequency", query = "SELECT r FROM Reservation r WHERE r.frequency = :frequency"),
		@NamedQuery(name = "Reservation.findByReccurenceNumber", query = "SELECT r FROM Reservation r WHERE r.reccurenceNumber = :reccurenceNumber"),
		@NamedQuery(name = "Reservation.findByStateres", query = "SELECT r FROM Reservation r WHERE r.stateres = :stateres"), })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Reservation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idreservation")
	private Integer idreservation;
	@Basic(optional = false)
	// @NotNull
	@Size(min = 1, max = 125)
	@Column(name = "title")
	private String title;
	@Basic(optional = false)
	// @NotNull
	@Lob
	@Size(min = 1, max = 2147483647)
	@Column(name = "description")
	private String description;
	@Basic(optional = false)
	// @NotNull
	@Size(min = 1, max = 25)
	@Column(name = "color")
	private String color;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "numberpart")
	private int numberpart;
	@Basic(optional = false)
	// @NotNull
	@Size(min = 1, max = 300)
	@Column(name = "uidmail")
	private String uidmail;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "simple")
	private boolean simple;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "daily")
	private boolean daily;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "weekly")
	private boolean weekly;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "monthly")
	private boolean monthly;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "dayofmonth")
	private boolean dayofmonth;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "dayofweek")
	private boolean dayofweek;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "yearly")
	private boolean yearly;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis")
	@Column(name = "startdate")
	@Temporal(TemporalType.DATE)
	private Date startdate;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "starttime")
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "H:mm", timezone = "Africa/Tunis")
	private Date starttime;
	@Column(name = "enddate")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Tunis") // +01:00")
	private Date enddate;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "endtime")
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "H:mm", timezone = "Africa/Tunis")
	private Date endtime;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "allday")
	private boolean allday;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "frequency")
	private int frequency;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "reccurence_number")
	private int reccurenceNumber;
	@Basic(optional = false)
	// @NotNull
	@Column(name = "stateres")
	private boolean stateres;
	@Basic(optional = false)
	@Lob
	@Size(min = 1, max = 2147483647)
	// @NotNull
	@Column(name = "descp")
	private String descp;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idreservation")
	@JsonIgnore
	private List<Selectedday> selecteddayList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "reservationid")
	@JsonIgnore
	private Set<Participant> participantList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idreservation")
	@JsonIgnore
	private List<Attachment> attachmentList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idreservation")
	@JsonIgnore
	private List<Reservationchild> reservationchildList;

	public List<Reservationchild> getReservationchildList() {
		return reservationchildList;
	}

	public void setReservationchildList(List<Reservationchild> reservationchildList) {
		this.reservationchildList = reservationchildList;
	}

	@JoinColumn(name = "reservedby", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private User reservedby;
	@JoinColumn(name = "room", referencedColumnName = "idroom")
	@ManyToOne(optional = false)
	private Room room;
	@JoinColumn(name = "typeres", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Typereservation typeres;
	@JoinColumn(name = "visibilitytyperes", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Visibilitytype visibilitytyperes;

	@Transient
	private List<DayDTO> daysList = new ArrayList<>();

	@Transient
	private boolean acceptConflict;

	public boolean getAcceptConflict() {
		return this.acceptConflict;
	}

	public void setAcceptConflict(boolean conf) {
		this.acceptConflict = conf;
	}

	public List<DayDTO> getDaysList() {
		return daysList;
	}

	public void setDaysList(List<DayDTO> list) {
		this.daysList = list;
	}

	public Reservation() {
	}

	public Reservation(Integer idreservation) {
		this.idreservation = idreservation;
	}

	public Reservation(Integer idreservation, String uidmail, Integer numberpart, String title, String description,
			String color, boolean simple, boolean daily, boolean weekly, boolean monthly, boolean yearly,
			Date startdate, Date enddate, Date starttime, Date endtime, boolean allday, int frequency,
			int reccurenceNumber, boolean stateres, String descp) {
		this.idreservation = idreservation;
		this.title = title;
		this.description = description;
		this.color = color;
		this.uidmail = uidmail;
		this.numberpart = numberpart;
		this.simple = simple;
		this.daily = daily;
		this.weekly = weekly;
		this.monthly = monthly;
		this.yearly = yearly;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.allday = allday;
		this.frequency = frequency;
		this.reccurenceNumber = reccurenceNumber;
		this.stateres = stateres;
		this.descp = descp;
	}

	public boolean isDayofmonth() {
		return dayofmonth;
	}

	public void setDayofmonth(boolean dayofmonth) {
		this.dayofmonth = dayofmonth;
	}

	public boolean isDayofweek() {
		return dayofweek;
	}

	public void setDayofweek(boolean dayofweek) {
		this.dayofweek = dayofweek;
	}

	public Integer getIdreservation() {
		return idreservation;
	}

	public void setIdreservation(Integer idreservation) {
		this.idreservation = idreservation;
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

	public boolean isAllday() {
		return allday;
	}

	public boolean getAllday() {
		return allday;
	}

	public void setAllday(boolean allday) {
		this.allday = allday;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean getSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	public boolean getDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public boolean getWeekly() {
		return weekly;
	}

	public void setWeekly(boolean weekly) {
		this.weekly = weekly;
	}

	public boolean getMonthly() {
		return monthly;
	}

	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}

	public boolean getYearly() {
		return yearly;
	}

	public void setYearly(boolean yearly) {
		this.yearly = yearly;
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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getReccurenceNumber() {
		return reccurenceNumber;
	}

	public void setReccurenceNumber(int reccurenceNumber) {
		this.reccurenceNumber = reccurenceNumber;
	}

	public boolean getStateres() {
		return stateres;
	}

	public void setStateres(boolean stateres) {
		this.stateres = stateres;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	@XmlTransient
	public List<Selectedday> getSelecteddayList() {
		return selecteddayList;
	}

	public void setSelecteddayList(List<Selectedday> selecteddayList) {
		this.selecteddayList = selecteddayList;
	}

	@XmlTransient
	public Set<Participant> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(Set<Participant> participantList) {
		this.participantList = participantList;
	}

	@XmlTransient
	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public User getReservedby() {
		return reservedby;
	}

	public void setReservedby(User reservedby) {
		this.reservedby = reservedby;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Typereservation getTyperes() {
		return typeres;
	}

	public void setTyperes(Typereservation typeres) {
		this.typeres = typeres;
	}

	public Visibilitytype getVisibilitytyperes() {
		return visibilitytyperes;
	}

	public void setVisibilitytyperes(Visibilitytype visibilitytyperes) {
		this.visibilitytyperes = visibilitytyperes;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idreservation != null ? idreservation.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Reservation)) {
			return false;
		}
		Reservation other = (Reservation) object;
		if ((this.idreservation == null && other.idreservation != null)
				|| (this.idreservation != null && !this.idreservation.equals(other.idreservation))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "tn.sofrecom.mdrissi.entities.Reservation[ idreservation=" + idreservation + " ]";
	}

	public int getNumberpart() {
		return numberpart;
	}

	public void setNumberpart(int numberpart) {
		this.numberpart = numberpart;
	}

	public String getUidmail() {
		return uidmail;
	}

	public void setUidmail(String uidmail) {
		this.uidmail = uidmail;
	}

}
