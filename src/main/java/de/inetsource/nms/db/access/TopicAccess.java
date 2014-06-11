package de.inetsource.nms.db.access;

import de.inetsource.nms.db.entity.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 */
public class TopicAccess extends AccessFacade<Topic> {

    private EntityManager em = null;

    public TopicAccess() {
        super(Topic.class);
    }

    @Override
    public EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("nmsPU");
            em = emf.createEntityManager();
        }
        return em;
    }

}
