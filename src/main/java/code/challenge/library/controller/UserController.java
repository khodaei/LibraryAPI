package code.challenge.library.controller;

import code.challenge.library.controller.type.Book;
import code.challenge.library.domain.User;
import code.challenge.library.domain.UserBook;
import code.challenge.library.domain.UserBookStatus;
import code.challenge.library.exception.UserNotFoundException;
import code.challenge.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user instance with the given username
     * @param user  must have a unique non-empty username
     * @return      user instance create
     */
    @PostMapping(value = "")
    public User create(@RequestBody User user) {
        if(StringUtils.isEmpty(user.getUsername())) {
            throw new IllegalArgumentException("Invalid username");
        }
        return userService.create(user);
    }

    /**
     * Adds book with given bookId to user's library
     * @param username  username associated with the library to add book to
     * @param book      book with specified bookId to be added to user's library
     */
    @PostMapping("/{username}/books")
    public Book addBookToLibrary(@PathVariable String username,
                                 @RequestBody Book book) {

        if(book.getBookId() == null) {
            logger.error("bookId is null");
            throw new IllegalArgumentException("Invalid bookId");
        }
        return userBookToBook.apply(userService.addBook(username, book.getBookId()));
    }

    /**
     * Removes book with given bookId from library associated with username
     * @param username  username associated with the library to remove book from
     * @param bookId    bookId associated wit the book to be removed from library
     */
    @DeleteMapping("/{username}/books/{bookId}")
    public void deleteBookFromLibrary(@PathVariable String username,
                                      @PathVariable Long bookId) {
        userService.deleteBook(username, bookId);
    }

    /**
     * @param username
     * @param bookId
     * @return  Book associated with bookId in the user library with username
     */
    @GetMapping("/{username}/books/{bookId}")
    public Book retrieveBook(@PathVariable String username,
                             @PathVariable Long bookId) {
        UserBook userBook = userService.retrieveBook(username, bookId);
        return userBookToBook.apply(userBook);
    }

    /**
     * Marks a book in user library as READ or UNREAD
     * @param username
     * @param bookId
     * @param book
     * @return book instance that was modified
     */
    @PostMapping("/{username}/books/{bookId}")
    public Book markBookInLibrary(@PathVariable String username,
                                  @PathVariable Long bookId,
                                  @RequestBody Book book) {

        UserBookStatus userBookStatus = UserBookStatus.valueOf(book.getStatus().toUpperCase());
        UserBook userBook = userService.markBook(username, bookId, userBookStatus);
        return userBookToBook.apply(userBook);
    }

    /**
     * Lists books in the user library. Author and status parameters can be optionally
     * set to narrow the list.
     * @param username
     * @param author (optional)   Match books written by specified author
     * @param status (optional)   READ or UNREAD - Match books with specified status
     * @return  all matching books in the user library
     */
    @GetMapping(value = "/{username}/books")
    public Iterable<Book> listBooksInLibrary(@PathVariable String username,
                                             @RequestParam(required = false) String author,
                                             @RequestParam(required = false) String status) {

        UserBookStatus userBookStatus = StringUtils.isEmpty(status) ? null : UserBookStatus.valueOf(status.toUpperCase());
        List<UserBook> userBooks = userService.listBooks(username, author, userBookStatus);

        return userBooks.stream().map(userBookToBook).collect(Collectors.toList());
    }


    @GetMapping(value = "")
    public Iterable<User> list() {
        return userService.list();
    }


    @GetMapping(value = "/{username}")
    public User retrieve(@PathVariable(value = "username") String username) {
        User user = userService.retrieve(username);
        if(user == null) {
            throw new UserNotFoundException("User with username: " + username + " is not found.");
        }
        return user;
    }

    @DeleteMapping(value = "/{username}")
    public void delete(@PathVariable(value = "username") String username) {
        userService.delete(username);
    }

    private static Function<UserBook, Book> userBookToBook = new Function<UserBook, Book>() {
        @Override
        public Book apply(UserBook userBook) {
            Book book = new Book();
            book.setBookId(userBook.getBook().getId());
            book.setTitle(userBook.getBook().getTitle());
            book.setAuthor(userBook.getBook().getAuthor());
            book.setStatus(userBook.getStatus().name());
            return book;
        }
    };
}
