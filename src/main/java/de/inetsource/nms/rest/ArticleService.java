package de.inetsource.nms.rest;

import de.inetsource.nms.db.access.ArticleAccess;
import de.inetsource.nms.db.entity.Article;
import de.inetsource.nms.service.BusinessLogic;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 */
@Path("article")
public class ArticleService extends BaseService {

    private final ArticleAccess articleAccess;

    public ArticleService() {
        articleAccess = new ArticleAccess();
        setBusinessLogic(new BusinessLogic<>());
        getBusinessLogic().setAccessFacade(articleAccess);
    }

    @GET
    @Path("topic/{id}")
    @Produces({"application/json"})
    public List<Article> findArticlesFromTopic(@PathParam("id") Integer topicId) {
        return articleAccess.findArticlesFromTopic(topicId);
    }

    @GET
    @Produces({"application/json"})
    public List<Article> findAll() {
        return articleAccess.findAll();
    }
}
