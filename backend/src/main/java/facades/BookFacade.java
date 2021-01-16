package facades;

import DTOs.BookDTO;
import DTOs.BooksDTO;
import entities.Book;
import entities.RenameMe;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class BookFacade {

    private static BookFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private BookFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static BookFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public BooksDTO getAllBooks() {
        EntityManager em = getEntityManager();
        try {
            return new BooksDTO(em.createNamedQuery("BOOK.getAllRows").getResultList());
        } finally {
            em.close();
        }
    }


    public BookDTO deleteBook(Long id) {
        EntityManager em = getEntityManager();
        Book book = em.find(Book.class, id);

        try {
            em.getTransaction().begin();
            em.remove(book);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new BookDTO(book);
    }
    
    public BookDTO addBook(String isbn, String title, String author, String publisher, String publishYear) {
        EntityManager em = getEntityManager();
        Book book = new Book(isbn, title, author, publisher, publishYear);

        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } finally {
            em.close();

        }
        return new BookDTO(book);
    }

}
    