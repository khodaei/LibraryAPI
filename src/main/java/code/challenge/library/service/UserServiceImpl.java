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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private BookRepository bookRepository;
    private UserBookRepository userBookRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    public void setUserBookRepository(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }


    @Override
    public User create(User user) {
        if (userRepository.findOne(user.getUsername()) != null) {
            throw new DuplicateUsernameException(user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> list() {
        logger.debug("retrieving all users from repository...");
        return userRepository.findAll();
    }

    @Override
    public User retrieve(String username) {
        logger.debug("retrieving user with username: " + username);
        return getUser(username);
    }

    @Override
    public void delete(String username) {
        User user = getUser(username);
        user.getBooks().forEach(ub -> userBookRepository.delete(ub));
        userRepository.delete(user);
    }

    @Override
    public UserBook addBook(String username, Long bookId) {

        User user = getUser(username);
        Book book = getBook(bookId);

        UserBook userBook = userBookRepository.findByUserAndBook(user, book);
        if(userBook == null) {
            userBook = new UserBook(user, book);
            book.getUsers().add(userBook);
            user.getBooks().add(userBook);
            userBookRepository.save(userBook);
        }
        return userBook;
    }

    @Override
    public void deleteBook(String username, Long bookId) {
        deleteUserBook(getUserBook(username, bookId));
    }

    @Override
    public UserBook retrieveBook(String username, Long bookId) {
        User user = userRepository.findOne(username);
        Book book = bookRepository.findOne(bookId);
        return getUserBook(user, book);
    }

    @Override
    public UserBook markBook(String username, Long bookId, UserBookStatus status) {
        UserBook userBook = getUserBook(username, bookId);
        userBook.setStatus(status);
        userBookRepository.save(userBook);
        return userBook;
    }

    @Override
    public List<UserBook> listBooks(String username, String author, UserBookStatus status) {

        User user = getUser(username);

        // retrieve books with given status otherwise get all books
        Stream<UserBook> userBooks = status != null ? userBookRepository.findByUserAndStatus(user, status).stream()
                : user.getBooks().stream();

        // filter out books not by author
        if(!StringUtils.isEmpty(author)) {
            return userBooks.filter(a -> a.getBook().getAuthor().toLowerCase().equals(author.toLowerCase())).collect(Collectors.toList());
        }

        return userBooks.collect(Collectors.toList());
    }

    private User getUser(String username) {
        User user = userRepository.findOne(username);
        if(user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }

    private Book getBook(long bookId) {
        Book book = bookRepository.findOne(bookId);
        if(book == null) {
            throw new BookNotFoundException(bookId);
        }
        return book;
    }

    private UserBook getUserBook(User user, Book book) {
        UserBook userBook = userBookRepository.findByUserAndBook(user, book);
        if(userBook == null) {
            throw new UserBookNotFoundException(user.getUsername(), book.getId());
        }
        return userBook;
    }

    private UserBook getUserBook(String username, long bookId) {
        return getUserBook(getUser(username), getBook(bookId));
    }

    private void deleteUserBook(UserBook userBook) {
        Book book = userBook.getBook();
        User user = userBook.getUser();
        book.getUsers().remove(userBook);
        user.getBooks().remove(userBook);
        userBookRepository.delete(new UserBook.UserBookId(book.getId(), user.getUsername()));
    }
}
