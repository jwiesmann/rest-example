package de.inetsource.nms.service;

import de.inetsource.nms.db.access.AccessFacade;
import de.inetsource.nms.db.access.exceptions.DBOperationException;
import de.inetsource.nms.db.access.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 * @param <T>
 */
public class BusinessLogic<T> {

    private AccessFacade<T> accessFacade;
    private static final Logger LOGGER = Logger.getLogger(AccessFacade.class.getName());

    /**
     *
     * @param entity to be updated
     * @return OK if entity was created, otherwise NOT_MODIFIED with corresponding exception
     */
    public Response create(T entity) {
        try {
            accessFacade.create(entity);
            return Response.ok().build();
        } catch (DBOperationException ex) {
            LOGGER.log(Level.SEVERE, "Creating entity failed", ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    /**
     * @param entity to be updated
     * @return OK with the updated entity, otherwise NOT_MODIFIED
     */
    public Response edit(T entity) {
        try {
            T edited = accessFacade.edit(entity);
            return Response.ok(edited, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (DBOperationException ex) {
            LOGGER.log(Level.SEVERE, "Entity could not be changed", ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    /**
     *
     * @param id of the entity to delete
     * @return a valid response, OK if everything works fine, else NOT_MODIFIED
     */
    public Response remove(Integer id) {
        try {
            accessFacade.deleteById(id);
            return Response.ok().build();
        } catch (NonexistentEntityException ex) {
            LOGGER.log(Level.WARNING, "Entity to delete not found ", ex);
            return Response.notModified(ex.getMessage()).build();
        }
    }

    /**
     *
     * @param id to search for
     * @return NO_CONTENT if entity could not be found, else OK with entity as object
     */
    public Response find(Integer id) {
        T foundEntity = accessFacade.find(id);
        if (foundEntity == null) {
            return Response.noContent().build();
        } else {
            return Response.ok(foundEntity, MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

    public List<T> findAll() {
        return accessFacade.findAll();
    }

    public void setAccessFacade(AccessFacade<T> accessFacade) {
        this.accessFacade = accessFacade;
    }
}
