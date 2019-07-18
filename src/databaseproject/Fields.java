/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author s0damachine
 */
@Entity
@Table(name = "fields")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fields.findAll", query = "SELECT f FROM Fields f"),
    @NamedQuery(name = "Fields.findByFieldnum", query = "SELECT f FROM Fields f WHERE f.fieldnum = :fieldnum"),
    @NamedQuery(name = "Fields.findByType", query = "SELECT f FROM Fields f WHERE f.type = :type"),
    @NamedQuery(name = "Fields.findBySize", query = "SELECT f FROM Fields f WHERE f.size = :size")})
public class Fields implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FIELDNUM")
    private Integer fieldnum;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "SIZE")
    private String size;
    @OneToMany(mappedBy = "fieldnum")
    private Collection<Openings> openingsCollection;

    public Fields() {
    }

    public Fields(Integer fieldnum) {
        this.fieldnum = fieldnum;
    }

    public Integer getFieldnum() {
        return fieldnum;
    }

    public void setFieldnum(Integer fieldnum) {
        this.fieldnum = fieldnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @XmlTransient
    public Collection<Openings> getOpeningsCollection() {
        return openingsCollection;
    }

    public void setOpeningsCollection(Collection<Openings> openingsCollection) {
        this.openingsCollection = openingsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fieldnum != null ? fieldnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fields)) {
            return false;
        }
        Fields other = (Fields) object;
        if ((this.fieldnum == null && other.fieldnum != null) || (this.fieldnum != null && !this.fieldnum.equals(other.fieldnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "databaseproject.Fields[ fieldnum=" + fieldnum + " ]";
    }
    
}
