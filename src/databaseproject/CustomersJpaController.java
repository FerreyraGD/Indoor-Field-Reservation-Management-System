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
public class CustomersJpaController implements Serializable {

    public CustomersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customers customers) {
        if (customers.getReservationsCollection() == null) {
            customers.setReservationsCollection(new ArrayList<Reservations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Reservations> attachedReservationsCollection = new ArrayList<Reservations>();
            for (Reservations reservationsCollectionReservationsToAttach : customers.getReservationsCollection()) {
                reservationsCollectionReservationsToAttach = em.getReference(reservationsCollectionReservationsToAttach.getClass(), reservationsCollectionReservationsToAttach.getRid());
                attachedReservationsCollection.add(reservationsCollectionReservationsToAttach);
            }
            customers.setReservationsCollection(attachedReservationsCollection);
            em.persist(customers);
            for (Reservations reservationsCollectionReservations : customers.getReservationsCollection()) {
                Customers oldCidOfReservationsCollectionReservations = reservationsCollectionReservations.getCid();
                reservationsCollectionReservations.setCid(customers);
                reservationsCollectionReservations = em.merge(reservationsCollectionReservations);
                if (oldCidOfReservationsCollectionReservations != null) {
                    oldCidOfReservationsCollectionReservations.getReservationsCollection().remove(reservationsCollectionReservations);
                    oldCidOfReservationsCollectionReservations = em.merge(oldCidOfReservationsCollectionReservations);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customers customers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customers persistentCustomers = em.find(Customers.class, customers.getCid());
            Collection<Reservations> reservationsCollectionOld = persistentCustomers.getReservationsCollection();
            Collection<Reservations> reservationsCollectionNew = customers.getReservationsCollection();
            Collection<Reservations> attachedReservationsCollectionNew = new ArrayList<Reservations>();
            for (Reservations reservationsCollectionNewReservationsToAttach : reservationsCollectionNew) {
                reservationsCollectionNewReservationsToAttach = em.getReference(reservationsCollectionNewReservationsToAttach.getClass(), reservationsCollectionNewReservationsToAttach.getRid());
                attachedReservationsCollectionNew.add(reservationsCollectionNewReservationsToAttach);
            }
            reservationsCollectionNew = attachedReservationsCollectionNew;
            customers.setReservationsCollection(reservationsCollectionNew);
            customers = em.merge(customers);
            for (Reservations reservationsCollectionOldReservations : reservationsCollectionOld) {
                if (!reservationsCollectionNew.contains(reservationsCollectionOldReservations)) {
                    reservationsCollectionOldReservations.setCid(null);
                    reservationsCollectionOldReservations = em.merge(reservationsCollectionOldReservations);
                }
            }
            for (Reservations reservationsCollectionNewReservations : reservationsCollectionNew) {
                if (!reservationsCollectionOld.contains(reservationsCollectionNewReservations)) {
                    Customers oldCidOfReservationsCollectionNewReservations = reservationsCollectionNewReservations.getCid();
                    reservationsCollectionNewReservations.setCid(customers);
                    reservationsCollectionNewReservations = em.merge(reservationsCollectionNewReservations);
                    if (oldCidOfReservationsCollectionNewReservations != null && !oldCidOfReservationsCollectionNewReservations.equals(customers)) {
                        oldCidOfReservationsCollectionNewReservations.getReservationsCollection().remove(reservationsCollectionNewReservations);
                        oldCidOfReservationsCollectionNewReservations = em.merge(oldCidOfReservationsCollectionNewReservations);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customers.getCid();
                if (findCustomers(id) == null) {
                    throw new NonexistentEntityException("The customers with id " + id + " no longer exists.");
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
            Customers customers;
            try {
                customers = em.getReference(Customers.class, id);
                customers.getCid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customers with id " + id + " no longer exists.", enfe);
            }
            Collection<Reservations> reservationsCollection = customers.getReservationsCollection();
            for (Reservations reservationsCollectionReservations : reservationsCollection) {
                reservationsCollectionReservations.setCid(null);
                reservationsCollectionReservations = em.merge(reservationsCollectionReservations);
            }
            em.remove(customers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customers> findCustomersEntities() {
        return findCustomersEntities(true, -1, -1);
    }

    public List<Customers> findCustomersEntities(int maxResults, int firstResult) {
        return findCustomersEntities(false, maxResults, firstResult);
    }

    private List<Customers> findCustomersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customers.class));
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

    public Customers findCustomers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customers.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customers> rt = cq.from(Customers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
