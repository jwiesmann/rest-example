package de.inetsource.nms.rest;

import de.inetsource.nms.db.access.ArticleAccess;
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
public class ArticleServiceTest {

    private final ArticleService articleService;
    private final BusinessLogic<Article> businessLogic;
    private Article article;
    private Topic topic;

    @Mock
    private ArticleAccess articleAccess;

    public ArticleServiceTest() {
        this.articleService = new ArticleService();
        this.businessLogic = new BusinessLogic<>();
    }

    @Before
    public void initMocks() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        setupArticle();
        setNonPublics();
        defineReturnValues();
    }

    /**
     * defines the return values to check the mocked objects
     */
    private void defineReturnValues() {
        when(articleAccess.find(1)).thenReturn(article);
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        articles.add(new Article(2));
        when(articleAccess.findAll()).thenReturn(articles);
        when(articleAccess.findArticlesFromTopic(1)).thenReturn(articles);
    }

    /**
     * changes some non public fields which need to be mocked away
     * @throws NoSuchFieldException
     * @throws IllegalAccessException 
     */
    private void setNonPublics() throws NoSuchFieldException, IllegalAccessException {
        articleService.setBusinessLogic(businessLogic);
        ObjectAccessor.setNotPublicField(articleService.getBusinessLogic(), "accessFacade", articleAccess);
        ObjectAccessor.setNotPublicField(articleService, "articleAccess", articleAccess);
    }

    @Test
    public void findTestSuccess() {
        Response response = articleService.find(1);
        assertEquals(article, response.getEntity());
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());
        verify(articleAccess).find(1);
    }

    @Test
    public void findTestFailure() {
        Response response = articleService.find(2);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.NO_CONTENT.toString());
        verify(articleAccess).find(2);
    }

    @Test
    public void testCreateSuccess() throws DBOperationException {
        Response response = articleService.create(article);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());
        verify(articleAccess).create(article);
    }

    @Test
    public void testCreateFailure() throws DBOperationException {
        when(businessLogic.create((Article) anyObject())).thenThrow(new DBOperationException("TEST on purpose Exception"));
        Response response = articleService.create(article);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.NOT_MODIFIED.toString());
        verify(articleAccess).create(article);
    }

    @Test
    public void testEdit() {
        Response response = articleService.edit(article);
        assertEquals(response.getStatusInfo().getReasonPhrase(), Response.Status.OK.toString());

    }

    @Test
    public void testRemove() {
        articleService.remove(1);
    }

    @Test
    public void testFindAll() {
        assertEquals(2, articleService.findAll().size());
    }

    @Test
    public void testFindArticlesFromTopic() {
        articleService.findArticlesFromTopic(1);
    }

    /**
     * sets up an article and the topic
     */
    private void setupArticle() {
        topic = new Topic(1);
        topic.setDescription("Topic Description");
        article = new Article(1);
        article.setText("Text");
        article.setTopicId(topic);
        article.setAuthor("Jörg Wiesmann");
    }

}
