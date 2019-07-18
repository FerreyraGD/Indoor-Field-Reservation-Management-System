/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import databaseproject.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author s0damachine
 */
public class ReservationsJpaController implements Serializable {

    public ReservationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservations reservations) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customers cid = reservations.getCid();
            if (cid != null) {
                cid = em.getReference(cid.getClass(), cid.getCid());
                reservations.setCid(cid);
            }
            Openings oid = reservations.getOid();
            if (oid != null) {
                oid = em.getReference(oid.getClass(), oid.getOid());
                reservations.setOid(oid);
            }
            em.persist(reservations);
            if (cid != null) {
                cid.getReservationsCollection().add(reservations);
                cid = em.merge(cid);
            }
            if (oid != null) {
                oid.getReservationsCollection().add(reservations);
                oid = em.merge(oid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservations reservations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservations persistentReservations = em.find(Reservations.class, reservations.getRid());
            Customers cidOld = persistentReservations.getCid();
            Customers cidNew = reservations.getCid();
            Openings oidOld = persistentReservations.getOid();
            Openings oidNew = reservations.getOid();
            if (cidNew != null) {
                cidNew = em.getReference(cidNew.getClass(), cidNew.getCid());
                reservations.setCid(cidNew);
            }
            if (oidNew != null) {
                oidNew = em.getReference(oidNew.getClass(), oidNew.getOid());
                reservations.setOid(oidNew);
            }
            reservations = em.merge(reservations);
            if (cidOld != null && !cidOld.equals(cidNew)) {
                cidOld.getReservationsCollection().remove(reservations);
                cidOld = em.merge(cidOld);
            }
            if (cidNew != null && !cidNew.equals(cidOld)) {
                cidNew.getReservationsCollection().add(reservations);
                cidNew = em.merge(cidNew);
            }
            if (oidOld != null && !oidOld.equals(oidNew)) {
                oidOld.getReservationsCollection().remove(reservations);
                oidOld = em.merge(oidOld);
            }
            if (oidNew != null && !oidNew.equals(oidOld)) {
                oidNew.getReservationsCollection().add(reservations);
                oidNew = em.merge(oidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reservations.getRid();
                if (findReservations(id) == null) {
                    throw new NonexistentEntityException("The reservations with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservations reservations;
            try {
                reservations = em.getReference(Reservations.class, id);
                reservations.getRid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservations with id " + id + " no longer exists.", enfe);
            }
            Customers cid = reservations.getCid();
            if (cid != null) {
                cid.getReservationsCollection().remove(reservations);
                cid = em.merge(cid);
            }
            Openings oid = reservations.getOid();
            if (oid != null) {
                oid.getReservationsCollection().remove(reservations);
                oid = em.merge(oid);
            }
            em.remove(reservations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservations> findReservationsEntities() {
        return findReservationsEntities(true, -1, -1);
    }

    public List<Reservations> findReservationsEntities(int maxResults, int firstResult) {
        return findReservationsEntities(false, maxResults, firstResult);
    }

    private List<Reservations> findReservationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservations.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Reservations findReservations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservations.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservations> rt = cq.from(Reservations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
