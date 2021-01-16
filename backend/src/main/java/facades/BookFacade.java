package facades;

import DTOs.BookDTO;
import DTOs.BooksDTO;
import entities.Book;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
            return new BooksDTO(em.createQuery("SELECT b FROM Book b").getResultList());
        } finally {
            em.close();
        }
    }

    public BookDTO deleteBook(Long id) throws NotFoundException {
        EntityManager em = getEntityManager();
        Book book = em.find(Book.class, id);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }else{
            try {
                em.getTransaction().begin();
                em.remove(book);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new BookDTO(book);
        }
    }

    

    public BookDTO addBook(String isbn, String title, String author, String publisher, String publishYear) throws MissingInputException {
        if ((isbn.length() == 0) || (title.length() == 0) || (author.length() == 0) || (publisher.length() == 0) || (publishYear.length() == 0)) {
            throw new MissingInputException("some inputs are missing");
        }
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

    public BookDTO editBook(BookDTO b) throws NotFoundException, MissingInputException {
        if ((b.getIsbn().length() == 0) || (b.getTitle().length() == 0) || (b.getAuthors().length() == 0) || (b.getPublishers().length() == 0) || (b.getPublishYear().length() == 0)) {
            throw new MissingInputException("some inputs are missing");
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, b.getId());
            if (book == null) {
                throw new NotFoundException("Book not found");

            } else {
                book.setIsbn(b.getIsbn());
                book.setTitle(b.getTitle());
                book.setAuthors(b.getAuthors());
                book.setPublisher(b.getAuthors());
                book.setPublishYear(b.getPublishYear());
                em.merge(book);
            }
            em.getTransaction().commit();
            return new BookDTO(book);
        } finally {
            em.close();
        }
    }

}
