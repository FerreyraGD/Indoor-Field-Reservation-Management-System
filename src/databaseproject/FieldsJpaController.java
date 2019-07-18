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
public class FieldsJpaController implements Serializable {

    public FieldsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fields fields) {
        if (fields.getOpeningsCollection() == null) {
            fields.setOpeningsCollection(new ArrayList<Openings>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Openings> attachedOpeningsCollection = new ArrayList<Openings>();
            for (Openings openingsCollectionOpeningsToAttach : fields.getOpeningsCollection()) {
                openingsCollectionOpeningsToAttach = em.getReference(openingsCollectionOpeningsToAttach.getClass(), openingsCollectionOpeningsToAttach.getOid());
                attachedOpeningsCollection.add(openingsCollectionOpeningsToAttach);
            }
            fields.setOpeningsCollection(attachedOpeningsCollection);
            em.persist(fields);
            for (Openings openingsCollectionOpenings : fields.getOpeningsCollection()) {
                Fields oldFieldnumOfOpeningsCollectionOpenings = openingsCollectionOpenings.getFieldnum();
                openingsCollectionOpenings.setFieldnum(fields);
                openingsCollectionOpenings = em.merge(openingsCollectionOpenings);
                if (oldFieldnumOfOpeningsCollectionOpenings != null) {
                    oldFieldnumOfOpeningsCollectionOpenings.getOpeningsCollection().remove(openingsCollectionOpenings);
                    oldFieldnumOfOpeningsCollectionOpenings = em.merge(oldFieldnumOfOpeningsCollectionOpenings);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fields fields) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fields persistentFields = em.find(Fields.class, fields.getFieldnum());
            Collection<Openings> openingsCollectionOld = persistentFields.getOpeningsCollection();
            Collection<Openings> openingsCollectionNew = fields.getOpeningsCollection();
            Collection<Openings> attachedOpeningsCollectionNew = new ArrayList<Openings>();
            for (Openings openingsCollectionNewOpeningsToAttach : openingsCollectionNew) {
                openingsCollectionNewOpeningsToAttach = em.getReference(openingsCollectionNewOpeningsToAttach.getClass(), openingsCollectionNewOpeningsToAttach.getOid());
                attachedOpeningsCollectionNew.add(openingsCollectionNewOpeningsToAttach);
            }
            openingsCollectionNew = attachedOpeningsCollectionNew;
            fields.setOpeningsCollection(openingsCollectionNew);
            fields = em.merge(fields);
            for (Openings openingsCollectionOldOpenings : openingsCollectionOld) {
                if (!openingsCollectionNew.contains(openingsCollectionOldOpenings)) {
                    openingsCollectionOldOpenings.setFieldnum(null);
                    openingsCollectionOldOpenings = em.merge(openingsCollectionOldOpenings);
                }
            }
            for (Openings openingsCollectionNewOpenings : openingsCollectionNew) {
                if (!openingsCollectionOld.contains(openingsCollectionNewOpenings)) {
                    Fields oldFieldnumOfOpeningsCollectionNewOpenings = openingsCollectionNewOpenings.getFieldnum();
                    openingsCollectionNewOpenings.setFieldnum(fields);
                    openingsCollectionNewOpenings = em.merge(openingsCollectionNewOpenings);
                    if (oldFieldnumOfOpeningsCollectionNewOpenings != null && !oldFieldnumOfOpeningsCollectionNewOpenings.equals(fields)) {
                        oldFieldnumOfOpeningsCollectionNewOpenings.getOpeningsCollection().remove(openingsCollectionNewOpenings);
                        oldFieldnumOfOpeningsCollectionNewOpenings = em.merge(oldFieldnumOfOpeningsCollectionNewOpenings);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fields.getFieldnum();
                if (findFields(id) == null) {
                    throw new NonexistentEntityException("The fields with id " + id + " no longer exists.");
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
            Fields fields;
            try {
                fields = em.getReference(Fields.class, id);
                fields.getFieldnum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fields with id " + id + " no longer exists.", enfe);
            }
            Collection<Openings> openingsCollection = fields.getOpeningsCollection();
            for (Openings openingsCollectionOpenings : openingsCollection) {
                openingsCollectionOpenings.setFieldnum(null);
                openingsCollectionOpenings = em.merge(openingsCollectionOpenings);
            }
            em.remove(fields);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fields> findFieldsEntities() {
        return findFieldsEntities(true, -1, -1);
    }

    public List<Fields> findFieldsEntities(int maxResults, int firstResult) {
        return findFieldsEntities(false, maxResults, firstResult);
    }

    private List<Fields> findFieldsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fields.class));
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

    public Fields findFields(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fields.class, id);
        } finally {
            em.close();
        }
    }

    public int getFieldsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fields> rt = cq.from(Fields.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
