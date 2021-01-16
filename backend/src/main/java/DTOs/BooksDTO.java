/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.Book;
import java.util.ArrayList;
import java.util.List;
import static org.eclipse.persistence.expressions.ExpressionOperator.all;

/**
 *
 * @author Marcus
 */
public class BooksDTO {
 
    private List<BookDTO> all = new ArrayList<>();
    
    
    public BooksDTO(List<Book> bookList) {
        for (Book book : bookList) {
            all.add(new BookDTO(book));
        }
    }

    public List<BookDTO> getAll() {
        return all;
    }
    
    
}
