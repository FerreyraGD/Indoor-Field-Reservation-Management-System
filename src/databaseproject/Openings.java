/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author s0damachine
 */
@Entity
@Table(name = "openings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Openings.findAll", query = "SELECT o FROM Openings o"),
    @NamedQuery(name = "Openings.findByOid", query = "SELECT o FROM Openings o WHERE o.oid = :oid"),
    @NamedQuery(name = "Openings.findByDate", query = "SELECT o FROM Openings o WHERE o.date = :date"),
    @NamedQuery(name = "Openings.findByTime", query = "SELECT o FROM Openings o WHERE o.time = :time")})
public class Openings implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OID")
    private Integer oid;
    @Column(name = "DATE")
    private String date;
    @Column(name = "TIME")
    private String time;
    @OneToMany(mappedBy = "oid")
    private Collection<Reservations> reservationsCollection;
    @JoinColumn(name = "FIELDNUM", referencedColumnName = "FIELDNUM")
    @ManyToOne
    private Fields fieldnum;

    public Openings() {
    }

    public Openings(Integer oid) {
        this.oid = oid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        Integer oldOid = this.oid;
        this.oid = oid;
        changeSupport.firePropertyChange("oid", oldOid, oid);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String oldDate = this.date;
        this.date = date;
        changeSupport.firePropertyChange("date", oldDate, date);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        String oldTime = this.time;
        this.time = time;
        changeSupport.firePropertyChange("time", oldTime, time);
    }

    @XmlTransient
    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    }

    public Fields getFieldnum() {
        return fieldnum;
    }

    public void setFieldnum(Fields fieldnum) {
        Fields oldFieldnum = this.fieldnum;
        this.fieldnum = fieldnum;
        changeSupport.firePropertyChange("fieldnum", oldFieldnum, fieldnum);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oid != null ? oid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Openings)) {
            return false;
        }
        Openings other = (Openings) object;
        if ((this.oid == null && other.oid != null) || (this.oid != null && !this.oid.equals(other.oid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "databaseproject.Openings[ oid=" + oid + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
