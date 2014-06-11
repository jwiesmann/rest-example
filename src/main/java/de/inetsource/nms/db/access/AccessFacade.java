package de.inetsource.nms.db.access;

import de.inetsource.nms.db.access.exceptions.DBOperationException;
import de.inetsource.nms.db.access.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

/**
 This facade is used to manage the basic database access.
 We are not using a jee container so we need to take care of our db transactions ourself.
 @author Joerg Wiesmann joerg.wiesmann@gmail.com
 @param <T>
 */
public abstract class AccessFacade<T> {

    private final Class<T> entityClass;

    public AccessFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * persists/create the new entity. Rolls back if any exception occurs
     * @param entity 
     * @throws de.inetsource.nms.db.access.exceptions.DBOperationException 
     */
    public void create(T entity) throws DBOperationException {
        persistOrMerge(entity, true);
    }

    /**
     * updates the entity. Rolls back if any exception occurs
     * @param entity 
     * @return merged entity
     * @throws de.inetsource.nms.db.access.exceptions.DBOperationException
     */
    public T edit(T entity) throws DBOperationException {
        return persistOrMerge(entity, false);
    }

    /**
     * handles the transactions of the db and inserts/updates the entity in the database
     * @param entity to persist/merge (insert/update)
     * @param persist true if it shall be persisted, false otherwise
     * @return merged entity or null if you persist an entity
     * @throws de.inetsource.nms.db.access.exceptions.DBOperationException
     */
    private T persistOrMerge(T entity, boolean persist) throws DBOperationException {
        T mergedEntity = null;
        try {
            getEntityManager().getTransaction().begin();
            if (persist) {
                getEntityManager().persist(entity);
            } else {
                mergedEntity = getEntityManager().merge(entity);
            }
            getEntityManager().getTransaction().commit();
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new DBOperationException("Persist or merge failed!", ex);
        }
        return mergedEntity;
    }

    /**
     * removes the entity of this class with the given id. 
     * If the entity does not exist this method will throw a NonexistentEntityException and rollback the transaction
     * @param id of the entity to remove
     * @throws NonexistentEntityException if entity could not be found
     */
    public void deleteById(Integer id) throws NonexistentEntityException {
        getEntityManager().getTransaction().begin();
        T entity;
        try {
            entity = getEntityManager().getReference(entityClass, id);
            getEntityManager().remove(entity);
            getEntityManager().getTransaction().commit();
        } catch (EntityNotFoundException e) {
            getEntityManager().getTransaction().rollback();
            throw new NonexistentEntityException("The Entity " + entityClass.getSimpleName() + " with id " + id + " does not exist", e);
        }
    }

    /**
     * 
     * @param id identifier of the entity
     * @return null or the found entity with the given id
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Basically does as select * from entity
     * Do not use it if you expect a huge amount of entities
     * @return a list of this entities. 
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
