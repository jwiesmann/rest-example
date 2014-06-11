package de.inetsource.nms.rest;

import de.inetsource.nms.db.access.TopicAccess;
import de.inetsource.nms.db.access.exceptions.DBOperationException;
import de.inetsource.nms.db.entity.Article;
import de.inetsource.nms.db.entity.Topic;
import de.inetsource.nms.service.BusinessLogic;
import de.inetsource.nms.testtools.ObjectAccessor;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 */
public class TopicServiceTest {

    private final TopicService topicService;
    private final BusinessLogic<Article> businessLogic;
    private Topic topic;

    @Mock
    private TopicAccess topicAccess;

    public TopicServiceTest() {
        this.topicService = new TopicService();
        this.businessLogic = new BusinessLogic<>();
    }

    @Before
    public void initMocks() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        setupTopic();
        setNonPublics();
        defineReturnValues();
    }

    /**
     * defines the return values to check the mocked objects
     */
    private void defineReturnValues() {
        when(topicAccess.find(1)).thenReturn(topic);
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);
        topics.add(new Topic(5));
        when(topicAccess.findAll()).thenReturn(topics);
    }

    /**
     * changes some non public fields which need to be mocked away
     * @throws NoSuchFieldException
     * @throws IllegalAccessException 
     */
    private void setNonPublics() throws NoSuchFieldException, IllegalAccessException {
        topicService.setBusinessLogic(businessLogic);
        ObjectAccessor.setNotPublicField(topicService.getBusinessLogic(), "accessFacade", topicAccess);
        ObjectAccessor.setNotPublicField(topicService, "topicAccess", topicAccess);
    }

    @Test
    public void findTestSuccess() {
        Response response = topicService.find(1);
        assertEquals(topic, response.getEntity());
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());
        verify(topicAccess).find(1);
    }

    @Test
    public void findTestFailure() {
        Response response = topicService.find(2);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.NO_CONTENT.toString());
        verify(topicAccess).find(2);
    }

    @Test
    public void testCreateSuccess() throws DBOperationException {
        Response response = topicService.create(topic);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());
        verify(topicAccess).create(topic);
    }

    @Test
    public void testCreateFailure() throws DBOperationException {
        when(businessLogic.create((Article) anyObject())).thenThrow(new DBOperationException("TEST on purpose Exception"));
        Response response = topicService.create(topic);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.NOT_MODIFIED.toString());
        verify(topicAccess).create(topic);
    }

    @Test
    public void testEdit() {
        Response response = topicService.edit(topic);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());

    }

    @Test
    public void testRemove() {
        Response response = topicService.remove(1);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());
    }

    @Test
    public void testFindAll() {
        assertEquals(2, topicService.findAll().size());
    }

    /**
     * sets up an topic and the topic
     */
    private void setupTopic() {
        topic = new Topic(1);
        topic.setDescription("Topic Description");
    }
}
