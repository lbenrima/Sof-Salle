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
@Table(name = "block")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Block.findAll", query = "SELECT b FROM Block b"),
    @NamedQuery(name = "Block.findByIdblock", query = "SELECT b FROM Block b WHERE b.idblock = :idblock"),
    @NamedQuery(name = "Block.findByNameblock", query = "SELECT b FROM Block b WHERE b.nameblock = :nameblock")})
public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idblock")
    private Integer idblock;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nameblock")
    private String nameblock;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idblock")
    @JsonIgnore
    private List<Room> meetingroomList;

    public Block() {
    }

    public Block(Integer idblock) {
        this.idblock = idblock;
    }

    public Block(Integer idblock, String nameblock, String description) {
        this.idblock = idblock;
        this.nameblock = nameblock;
        this.description = description;
    }

    public Integer getIdblock() {
        return idblock;
    }

    public void setIdblock(Integer idblock) {
        this.idblock = idblock;
    }

    public String getNameblock() {
        return nameblock;
    }

    public void setNameblock(String nameblock) {
        this.nameblock = nameblock;
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
        hash += (idblock != null ? idblock.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Block)) {
            return false;
        }
        Block other = (Block) object;
        if ((this.idblock == null && other.idblock != null) || (this.idblock != null && !this.idblock.equals(other.idblock))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tn.sofrecom.mdrissi.entities.Block[ idblock=" + idblock + " ]";
    }
    
}
