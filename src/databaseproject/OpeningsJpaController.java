/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import databaseproject.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author s0damachine
 */
public class OpeningsJpaController implements Serializable {

    public OpeningsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Openings openings) {
        if (openings.getReservationsCollection() == null) {
            openings.setReservationsCollection(new ArrayList<Reservations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fields fieldnum = openings.getFieldnum();
            if (fieldnum != null) {
                fieldnum = em.getReference(fieldnum.getClass(), fieldnum.getFieldnum());
                openings.setFieldnum(fieldnum);
            }
            Collection<Reservations> attachedReservationsCollection = new ArrayList<Reservations>();
            for (Reservations reservationsCollectionReservationsToAttach : openings.getReservationsCollection()) {
                reservationsCollectionReservationsToAttach = em.getReference(reservationsCollectionReservationsToAttach.getClass(), reservationsCollectionReservationsToAttach.getRid());
                attachedReservationsCollection.add(reservationsCollectionReservationsToAttach);
            }
            openings.setReservationsCollection(attachedReservationsCollection);
            em.persist(openings);
            if (fieldnum != null) {
                fieldnum.getOpeningsCollection().add(openings);
                fieldnum = em.merge(fieldnum);
            }
            for (Reservations reservationsCollectionReservations : openings.getReservationsCollection()) {
                Openings oldOidOfReservationsCollectionReservations = reservationsCollectionReservations.getOid();
                reservationsCollectionReservations.setOid(openings);
                reservationsCollectionReservations = em.merge(reservationsCollectionReservations);
                if (oldOidOfReservationsCollectionReservations != null) {
                    oldOidOfReservationsCollectionReservations.getReservationsCollection().remove(reservationsCollectionReservations);
                    oldOidOfReservationsCollectionReservations = em.merge(oldOidOfReservationsCollectionReservations);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Openings openings) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Openings persistentOpenings = em.find(Openings.class, openings.getOid());
            Fields fieldnumOld = persistentOpenings.getFieldnum();
            Fields fieldnumNew = openings.getFieldnum();
            Collection<Reservations> reservationsCollectionOld = persistentOpenings.getReservationsCollection();
            Collection<Reservations> reservationsCollectionNew = openings.getReservationsCollection();
            if (fieldnumNew != null) {
                fieldnumNew = em.getReference(fieldnumNew.getClass(), fieldnumNew.getFieldnum());
                openings.setFieldnum(fieldnumNew);
            }
            Collection<Reservations> attachedReservationsCollectionNew = new ArrayList<Reservations>();
            for (Reservations reservationsCollectionNewReservationsToAttach : reservationsCollectionNew) {
                reservationsCollectionNewReservationsToAttach = em.getReference(reservationsCollectionNewReservationsToAttach.getClass(), reservationsCollectionNewReservationsToAttach.getRid());
                attachedReservationsCollectionNew.add(reservationsCollectionNewReservationsToAttach);
            }
            reservationsCollectionNew = attachedReservationsCollectionNew;
            openings.setReservationsCollection(reservationsCollectionNew);
            openings = em.merge(openings);
            if (fieldnumOld != null && !fieldnumOld.equals(fieldnumNew)) {
                fieldnumOld.getOpeningsCollection().remove(openings);
                fieldnumOld = em.merge(fieldnumOld);
            }
            if (fieldnumNew != null && !fieldnumNew.equals(fieldnumOld)) {
                fieldnumNew.getOpeningsCollection().add(openings);
                fieldnumNew = em.merge(fieldnumNew);
            }
            for (Reservations reservationsCollectionOldReservations : reservationsCollectionOld) {
                if (!reservationsCollectionNew.contains(reservationsCollectionOldReservations)) {
                    reservationsCollectionOldReservations.setOid(null);
                    reservationsCollectionOldReservations = em.merge(reservationsCollectionOldReservations);
                }
            }
            for (Reservations reservationsCollectionNewReservations : reservationsCollectionNew) {
                if (!reservationsCollectionOld.contains(reservationsCollectionNewReservations)) {
                    Openings oldOidOfReservationsCollectionNewReservations = reservationsCollectionNewReservations.getOid();
                    reservationsCollectionNewReservations.setOid(openings);
                    reservationsCollectionNewReservations = em.merge(reservationsCollectionNewReservations);
                    if (oldOidOfReservationsCollectionNewReservations != null && !oldOidOfReservationsCollectionNewReservations.equals(openings)) {
                        oldOidOfReservationsCollectionNewReservations.getReservationsCollection().remove(reservationsCollectionNewReservations);
                        oldOidOfReservationsCollectionNewReservations = em.merge(oldOidOfReservationsCollectionNewReservations);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = openings.getOid();
                if (findOpenings(id) == null) {
                    throw new NonexistentEntityException("The openings with id " + id + " no longer exists.");
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
            Openings openings;
            try {
                openings = em.getReference(Openings.class, id);
                openings.getOid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The openings with id " + id + " no longer exists.", enfe);
            }
            Fields fieldnum = openings.getFieldnum();
            if (fieldnum != null) {
                fieldnum.getOpeningsCollection().remove(openings);
                fieldnum = em.merge(fieldnum);
            }
            Collection<Reservations> reservationsCollection = openings.getReservationsCollection();
            for (Reservations reservationsCollectionReservations : reservationsCollection) {
                reservationsCollectionReservations.setOid(null);
                reservationsCollectionReservations = em.merge(reservationsCollectionReservations);
            }
            em.remove(openings);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Openings> findOpeningsEntities() {
        return findOpeningsEntities(true, -1, -1);
    }

    public List<Openings> findOpeningsEntities(int maxResults, int firstResult) {
        return findOpeningsEntities(false, maxResults, firstResult);
    }

    private List<Openings> findOpeningsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Openings.class));
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

    public Openings findOpenings(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Openings.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpeningsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Openings> rt = cq.from(Openings.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
