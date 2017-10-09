package code.challenge.library.service;

import code.challenge.library.domain.User;
import code.challenge.library.domain.UserBook;
import code.challenge.library.domain.UserBookStatus;

import java.util.List;

public interface UserService {

    /**
     * @return list of all users in the data store
     */
    Iterable<User> list();

    /**
     * Creates a new user in data store with the username provided in User object
     * @param user  user details for new user
     * @return      user instance created in the data store
     */
    User create(User user);

    UserBook addBook(String username, Long bookId);

    UserBook markBook(String username, Long bookId, UserBookStatus status);

    UserBook retrieveBook(String username, Long bookId);

    void deleteBook(String username, Long bookId);

    List<UserBook> listBooks(String username, String author, UserBookStatus status);

    User retrieve(String username);

    void delete(String username);

    // Iterable<UserBook> listBooks(String username, String author, UserBookStatus status)

}
