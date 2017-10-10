package code.challenge.library.service;

import code.challenge.library.domain.Book;
import code.challenge.library.domain.User;
import code.challenge.library.domain.UserBook;
import code.challenge.library.repository.BookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookServiceImplTest {

    @TestConfiguration
    static class BookServiceImplTestContextConfiguration {

        @Bean
        public BookService bookService() {
            return new BookServiceImpl();
        }
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Before
    public void setup() {
        bookRepository.deleteAll();
        Book book = bookRepository.save(new Book("title", "author"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyTitle() {
        Book book = new Book("", "author");
        bookService.create(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyAuthor() {
        Book book = new Book("title", "");
        bookService.create(book);
    }

    @Test
    public void testCreate() {
        Book book = new Book("title1", "author1");
        Book addedBook = bookService.create(book);
        Assert.assertEquals(addedBook.getAuthor(), "author1");
        Assert.assertEquals(addedBook.getTitle(), "title1");
        Assert.assertNotNull(addedBook.getId());
    }
}
