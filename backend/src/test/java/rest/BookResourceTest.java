package rest;

import DTOs.BookDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.shaded.json.JSONObject;
import entities.Book;
import entities.RenameMe;
import entities.Role;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class BookResourceTest {
    
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Book b1, b2, b3;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }
    
    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Role").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
       
        User user = new User("admin", "admin123");
        Role role = new Role("admin");
        user.addRole(role);
        em.persist(role);
        em.persist(user);
        em.getTransaction().commit();
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/book/all");
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(b1 = new Book("12312", "harry potter", "Gwenyth paltrow", "egmont", "1999"));
            em.persist(b2 = new Book("8347", "harry potter2", "Gwenyth paltrow", "egmont", "1998"));
            em.persist(b3 = new Book("1231", "harry potter3", "Gwenyth paltrow", "egmont", "1997"));
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }
    
    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testGetAllBooks() throws Exception {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/books/").then()
                .body(notNullValue());
    }
    
    @Test
    public void addBook() {
        Book book = new Book("763", "Harry potter", "jens hansen", "egmint", "1999");
        login("admin", "admin123");
        JSONObject json = new JSONObject();
        Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        given()
                .contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(GSON.toJson(new BookDTO(book)))
                .when()
                .post("/book/")
                .then()
                .body("isbn", equalTo("763"))
                .body("title", equalTo("Harry potter"))
                .body("authors", equalTo("jens hansen"))
                .body("publishers", equalTo("egmint"))
                .body("publishYear", equalTo("1999"))
                .body("id", notNullValue());
    }
    
}
