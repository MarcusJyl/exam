package facades;

import DTOs.BookDTO;
import DTOs.BooksDTO;
import entities.Book;
import utils.EMF_Creator;
import entities.RenameMe;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadeExampleTest {

    private static EntityManagerFactory emf;
    private static BookFacade facade;
    private static Book b1, b2, b3;

    public FacadeExampleTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Book").executeUpdate();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(b1 = new Book("12312", "harry potter", "Gwenyth paltrow", "egmont", "1999"));
            em.persist(b2 = new Book("8347", "harry potter2", "Gwenyth paltrow", "egmont", "1998"));
            em.persist(b3 = new Book("1231", "harry potter3", "Gwenyth paltrow", "egmont", "1997"));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testDeleteBook() throws Exception {
        long id = b2.getId();
        BookFacade instance = BookFacade.getFacadeExample(emf);
        BookDTO expected = new BookDTO(b2);
        BookDTO result = instance.deleteBook(id);
        assertThat(expected, samePropertyValuesAs(result));
    }

    @Test
    public void testAddBook() throws MissingInputException {
        String isbn = "123456789";
        String title = "harry Potter";
        String authors = "hans";
        String publisher = "egmont";
        String publishYear = "1999";
        Book book = new Book(isbn, title, authors, publisher, publishYear);
        BookFacade instance = BookFacade.getFacadeExample(emf);
        BookDTO result = instance.addBook(isbn, title, authors, publisher, publishYear);
        BookDTO expected = new BookDTO(book);
        assertEquals(expected.getTitle(), result.getTitle());

    }

    @Test
    public void testGetAll() {
        BookFacade instance = BookFacade.getFacadeExample(emf);
        int expected = 3;
        BooksDTO result = instance.getAllBooks();
        assertEquals(expected, result.getAll().size());

    }

    @Test
    public void testEditBook() throws NotFoundException, MissingInputException {
        BookDTO b = new BookDTO(b1);
        BookFacade instance = BookFacade.getFacadeExample(emf);
        BookDTO expected = new BookDTO(b1);
        expected.setTitle("Harry Pooper");
        b.setTitle("Harry Pooper");
        BookDTO result = instance.editBook(b);
        assertEquals(expected.getTitle(), result.getTitle());

    }

}
