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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m.drissi
 */
@Entity
@Table(name = "attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a"),
    @NamedQuery(name = "Attachment.findByIdattachment", query = "SELECT a FROM Attachment a WHERE a.idattachment = :idattachment"),
    @NamedQuery(name = "Attachment.findByName", query = "SELECT a FROM Attachment a WHERE a.name = :name"),
    @NamedQuery(name = "Attachment.findByExtension", query = "SELECT a FROM Attachment a WHERE a.extension = :extension")})
public class Attachment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idattachment")
    private Integer idattachment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "extension")
    private String extension;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "file")
    private byte[] file;
    @JoinColumn(name = "idreservation", referencedColumnName = "idreservation")
    @ManyToOne(optional = false)
    private Reservation idreservation;

    public Attachment() {
    }

    public Attachment(Integer idattachment) {
        this.idattachment = idattachment;
    }

    public Attachment(Integer idattachment, String name, String extension, byte[] file) {
        this.idattachment = idattachment;
        this.name = name;
        this.extension = extension;
        this.file = file;
    }

    public Integer getIdattachment() {
        return idattachment;
    }

    public void setIdattachment(Integer idattachment) {
        this.idattachment = idattachment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
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
        hash += (idattachment != null ? idattachment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.idattachment == null && other.idattachment != null) || (this.idattachment != null && !this.idattachment.equals(other.idattachment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Attachment[ idattachment=" + idattachment + " ]";
    }
    
}
