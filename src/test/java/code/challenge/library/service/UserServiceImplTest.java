package code.challenge.library.service;

import code.challenge.library.domain.Book;
import code.challenge.library.domain.User;
import code.challenge.library.domain.UserBook;
import code.challenge.library.domain.UserBookStatus;
import code.challenge.library.exception.BookNotFoundException;
import code.challenge.library.exception.DuplicateUsernameException;
import code.challenge.library.exception.UserBookNotFoundException;
import code.challenge.library.exception.UserNotFoundException;
import code.challenge.library.repository.BookRepository;
import code.challenge.library.repository.UserBookRepository;
import code.challenge.library.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);
    private static final String USERNAME = "amir";
    private static final Long BOOK_ID = new Long(1);

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        userBookRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(new User(USERNAME));
        Book book1 = bookRepository.save(new Book("title", "author"));
        UserBook userBook = userBookRepository.save(new UserBook(user, book1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyUsername() {
        User user = new User("");
        userService.create(user);
    }

    @Test(expected = DuplicateUsernameException.class)
    public void testCreateWithDuplicateUsername() {
        User user = new User("amir");
        userService.create(user);
    }

    @Test
    public void testCreate() {
        String username = "new user";
        User user = new User(username);
        User createdUser = userService.create(user);
        Assert.assertEquals(createdUser.getUsername(), username);
        Assert.assertNotNull(userRepository.findOne(username));
    }

    @Test(expected = BookNotFoundException.class)
    public void testAddBookWithInvalidBookId() {
        userService.addBook(USERNAME, new Long(3));
    }

    @Test(expected = UserNotFoundException.class)
    public void testAddBookWithInvalidUsername() {
        userService.addBook("bad username", BOOK_ID);
    }

    @Test
    public void testAddBookExisting() {

        UserBook existingUserBook = userBookRepository.findAll().iterator().next();
        int librarySize = existingUserBook.getUser().getBooks().size();
        Assert.assertNotNull(existingUserBook);
        UserBook userBookAdded = userService.addBook(existingUserBook.getUser().getUsername(), existingUserBook.getBook().getId());
        Assert.assertEquals(existingUserBook, userBookAdded);
        Assert.assertEquals(librarySize, userBookAdded.getUser().getBooks().size());
    }

    @Test
    public void testAddBook() {
        Book addedBook = bookRepository.save(new Book("title1", "author1"));
        Assert.assertNull(userBookRepository.findByUserAndBook(new User(USERNAME), addedBook));
        UserBook userBookAdded = userService.addBook(USERNAME, addedBook.getId());
        Assert.assertNotNull(userBookRepository.findByUserAndBook(new User(USERNAME), addedBook));
    }

    @Test
    public void testMarkBook() {
        UserBook userBook = userBookRepository.findAll().iterator().next();
        Assert.assertEquals(userBook.getStatus(), UserBookStatus.UNREAD);
        userService.markBook(userBook.getUser().getUsername(), userBook.getBook().getId(), UserBookStatus.READ);
        Assert.assertEquals(userBook.getStatus(), UserBookStatus.READ);
        userService.markBook(userBook.getUser().getUsername(), userBook.getBook().getId(), UserBookStatus.UNREAD);
        Assert.assertEquals(userBook.getStatus(), UserBookStatus.UNREAD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkBookNullStatus() {
        UserBook userBook = userBookRepository.findAll().iterator().next();
        Assert.assertEquals(userBook.getStatus(), UserBookStatus.UNREAD);
        userService.markBook(userBook.getUser().getUsername(), userBook.getBook().getId(), null);
    }

    @Test
    public void testDeleteUserBook() {
        UserBook userBook = userBookRepository.findAll().iterator().next();
        User user = userBook.getUser();
        Book book = userBook.getBook();
        Assert.assertNotNull(userBookRepository.findByUserAndBook(user, book));
        userService.deleteBook(user.getUsername(), book.getId());
        Assert.assertNull(userBookRepository.findByUserAndBook(user, book));
    }

    @Test(expected = UserBookNotFoundException.class)
    public void testDeleteUserBookNonExisting() {
        Book book = bookRepository.save(new Book("newtitle", "new author"));
        User user = userRepository.findOne(USERNAME);
        Assert.assertNull(userBookRepository.findByUserAndBook(user, book));
        userService.deleteBook(user.getUsername(), book.getId());
        Assert.assertNull(userBookRepository.findByUserAndBook(user, book));
    }


    @Test
    public void testListBooksAll() {
        User user = userService.retrieve(USERNAME);
        List<UserBook> userBooks = userBookRepository.findByUser(user);
        int size = userBooks.size();
        List<UserBook> userBooks1 = userService.listBooks(user.getUsername(), "",null);
        Assert.assertEquals("repo: " + size + "\tservice: " + userBooks1.size(),
                size, userBooks1.size());


        List<Book> books = new ArrayList<>();
        books.add(new Book("Headspace 1", "Andy"));
        books.add(new Book("Headspace 2", "Andy"));
        books.add(new Book("Headspace 3", "Andy"));
        books.add(new Book("Headspace 4", "Another Dude"));
        Iterable<Book> booksAdded = bookRepository.save(books);
        booksAdded.forEach(b -> userService.addBook(user.getUsername(), b.getId()));

        Assert.assertEquals(size + 4, userService.listBooks(user.getUsername(), "", null).size());
    }

    @Test
    public void testListBooksWithAuthor() {
        User user = userService.retrieve(USERNAME);

        int bookWithAuthorNameAndy = userService.listBooks(user.getUsername(), "Andy", null).size();

        List<Book> books = new ArrayList<>();
        books.add(new Book("Headspace 1", "Andy"));
        books.add(new Book("Headspace 2", "Andy"));
        books.add(new Book("Headspace 3", "Andy"));
        books.add(new Book("Headspace 4", "Another Dude"));
        Iterable<Book> booksAdded = bookRepository.save(books);

        booksAdded.forEach(b -> userService.addBook(user.getUsername(), b.getId()));
        bookWithAuthorNameAndy += 3;

        Assert.assertEquals(bookWithAuthorNameAndy,
                userService.listBooks(user.getUsername(), "Andy", null).size());
        Assert.assertTrue(bookWithAuthorNameAndy < userService.listBooks(user.getUsername(), "", null).size());
        Assert.assertEquals(0, userService.listBooks(user.getUsername(), "someone who doesn't exist", null).size());
    }

    @Test
    public void testListBooksWithStatus() {
        User user = userService.retrieve(USERNAME);

        int booksUnread = userService.listBooks(user.getUsername(), null, UserBookStatus.UNREAD).size();

        List<Book> books = new ArrayList<>();
        books.add(new Book("Headspace 1", "Andy"));
        books.add(new Book("Headspace 2", "Andy"));
        books.add(new Book("Headspace 3", "Andy"));
        books.add(new Book("Headspace 4", "Another Dude"));

        Iterable<Book> booksAdded = bookRepository.save(books);
        booksAdded.forEach(b -> userService.addBook(user.getUsername(), b.getId()));
        booksUnread += 4;

        Assert.assertEquals(booksUnread,
                userService.listBooks(user.getUsername(), null, UserBookStatus.UNREAD).size());

        Iterator<Book> iter = booksAdded.iterator();
        Book readBook1 = iter.next();
        Book readBook2 = iter.next();
        userService.markBook(user.getUsername(), readBook1.getId(), UserBookStatus.READ);
        userService.markBook(user.getUsername(), readBook2.getId(), UserBookStatus.READ);

        Assert.assertEquals(booksUnread - 2,
                userService.listBooks(user.getUsername(), null, UserBookStatus.UNREAD).size());

        Assert.assertEquals(2,
                userService.listBooks(user.getUsername(), null, UserBookStatus.READ).size());
    }

    @Test
    public void testListBooksWithAuthorAndStatus() {
        User user = userService.retrieve(USERNAME);

        int booksUnread = userService.listBooks(user.getUsername(), null, null).size();

        List<Book> books = new ArrayList<>();
        books.add(new Book("Headspace 1", "Andy"));
        books.add(new Book("Headspace 2", "Andy"));
        books.add(new Book("Headspace 3", "Andy"));
        books.add(new Book("Headspace 4", "Another Dude"));

        Iterable<Book> booksAdded = bookRepository.save(books);
        booksAdded.forEach(b -> userService.addBook(user.getUsername(), b.getId()));

        List<UserBook> unreadByAndy = userService.listBooks(user.getUsername(), "Andy", UserBookStatus.UNREAD);
        Assert.assertEquals(3, unreadByAndy.size());

        userService.markBook(user.getUsername(), unreadByAndy.get(0).getBook().getId(), UserBookStatus.READ);
        userService.markBook(user.getUsername(), unreadByAndy.get(1).getBook().getId(), UserBookStatus.READ);

        Assert.assertEquals(1, userService.listBooks(user.getUsername(), "Andy", UserBookStatus.UNREAD).size());
        Assert.assertEquals(2, userService.listBooks(user.getUsername(), "Andy", UserBookStatus.READ).size());
    }
}
