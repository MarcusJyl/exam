/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

/**
 *
 * @author Marcus
 */
import DTOs.BookDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entities.Book;
import entities.User;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.BookFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import security.UserPrincipal;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("book")
public class BookResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final BookFacade FACADE = BookFacade.getFacadeExample(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public List allBooks() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<BookDTO> query = em.createQuery("select b from Book b", BookDTO.class);
            List<BookDTO> books = query.getResultList();

            return books;
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deleteBook(@PathParam("id") Long id) throws NotFoundException {
        BookDTO bDelete = FACADE.deleteBook(id);
        return GSON.toJson(bDelete);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String addBook(String book) throws MissingInputException {
        BookDTO b = GSON.fromJson(book, BookDTO.class);
        BookDTO bNew = FACADE.addBook(b.getIsbn(), b.getTitle(), b.getAuthors(), b.getPublishers(), b.getPublishYear());
        return GSON.toJson(bNew);
    }
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editBook(@PathParam("id")Long id, String book) throws NotFoundException, MissingInputException{
        BookDTO bDTO = GSON.fromJson(book, BookDTO.class);
        bDTO.setId(id);
       BookDTO bNew = FACADE.editBook(bDTO);
       return GSON.toJson(bNew);
    }
}
