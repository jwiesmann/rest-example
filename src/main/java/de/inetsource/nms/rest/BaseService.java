package de.inetsource.nms.rest;

import de.inetsource.nms.service.BusinessLogic;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Basic functionality for rest webservice
 * @author Joerg Wiesmann joerg.wiesmann@gmail.com
 * @param <T>
 */
public class BaseService<T> {

    private BusinessLogic<T> businessLogic;

    @POST
    @Consumes({"application/json"})
    public Response create(T entity) {
        return businessLogic.create(entity);
    }

    @PUT
    @Consumes({"application/json"})
    public Response edit(T entity) {
        return businessLogic.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        return businessLogic.remove(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response find(@PathParam("id") Integer id) {
        return businessLogic.find(id);
    }

    public BusinessLogic<T> getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BusinessLogic<T> businessLogic) {
        this.businessLogic = businessLogic;
    }

}
