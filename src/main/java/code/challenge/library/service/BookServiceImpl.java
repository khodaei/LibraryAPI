package code.challenge.library.service;

import code.challenge.library.domain.Book;
import code.challenge.library.exception.BookNotFoundException;
import code.challenge.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private BookRepository bookRepository;

    @Override
    public Iterable<Book> list() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(Book book) {
        if(book == null || StringUtils.isEmpty(book.getAuthor()) || StringUtils.isEmpty(book.getTitle())) {
            throw new IllegalArgumentException("Invalid title or author for book");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book retrieve(long id) {
        Book book = bookRepository.findOne(id);
        if(book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    @Override
    public void delete(long id) {
        Book book = bookRepository.findOne(id);
        if(book == null) {
            throw new BookNotFoundException(id);
        }
        bookRepository.delete(book);
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
