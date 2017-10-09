package code.challenge.library.repository;

import code.challenge.library.domain.Book;
import code.challenge.library.domain.User;
import code.challenge.library.domain.UserBook;
import code.challenge.library.domain.UserBookStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBookRepository extends CrudRepository<UserBook, UserBook.UserBookId> {

    List<UserBook> findByUser(User user);
    UserBook findByUserAndBook(User user, Book book);
    List<UserBook> findByUserAndStatus(User user, UserBookStatus status);
}
