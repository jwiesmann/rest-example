package de.inetsource.nms.rest;

import de.inetsource.nms.db.access.TopicAccess;
import de.inetsource.nms.db.entity.Topic;
import de.inetsource.nms.service.BusinessLogic;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 */
@Path("topic")
public class TopicService extends BaseService{


    private final TopicAccess topicAccess;

    public TopicService() {
        topicAccess = new TopicAccess();
        setBusinessLogic(new BusinessLogic<>());
        getBusinessLogic().setAccessFacade(topicAccess);
    }

    @GET
    @Produces({"application/json"})
    public List<Topic> findAll() {
        return topicAccess.findAll();
    }
    
}