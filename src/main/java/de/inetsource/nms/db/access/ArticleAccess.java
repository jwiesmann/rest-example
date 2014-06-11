package de.inetsource.nms.db.access;

import de.inetsource.nms.db.entity.Article;
import de.inetsource.nms.db.entity.Topic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 */
public class ArticleAccess extends AccessFacade<Article> {

    private EntityManager em = null;

    public ArticleAccess() {
        super(Article.class);
    }

    @Override
    public EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("nmsPU");
            em = emf.createEntityManager();
        }
        return em;
    }

    /**
    * 
    * @param topicId
    * @return a list of article for a specific topic
    */
    public List<Article> findArticlesFromTopic(Integer topicId) {
        Query query = getEntityManager().createNamedQuery("Article.findByTopicId");
        query.setParameter("topic", new Topic(topicId));
        return query.getResultList();
    }

}
