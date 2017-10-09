package code.challenge.library.controller;

import code.challenge.library.domain.Book;
import code.challenge.library.exception.BookNotFoundException;
import code.challenge.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;

    @PostMapping(value = "")
    public Book create(@RequestBody Book book, HttpServletResponse response) {
        if(StringUtils.isEmpty(book.getAuthor()) || StringUtils.isEmpty(book.getTitle())) {
            throw new IllegalArgumentException("author and title must be specified");
        }
        Book bookCreated = bookService.create(book);
        response.setStatus(HttpStatus.CREATED.value());
        return bookCreated;
    }

    @GetMapping(value = "/{id}")
    public Book retrieve(@PathVariable(value = "id") long id) {
        Book book = bookService.retrieve(id);
        if(book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    @GetMapping(value = "")
    public Iterable<Book> list() {
        logger.info("BookController:: list is called to retrieve all books");
        return bookService.list();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") long id) {
        bookService.delete(id);
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
