package code.challenge.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "user") // , cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserBook> books = new HashSet<>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<UserBook> getBooks() {
        return books;
    }

    public void setBooks(Set<UserBook> books) {
        this.books = books;
    }

    public UserBook addBook(Book book) {

        UserBook userBook = new UserBook(this, book);
        books.add(userBook);
        book.getUsers().add(userBook);

        logger.info("added book: " + book.getTitle() + " to user: " + username);
        logger.info("library size: " + books.size());
        return userBook;
    }

    public void removeBook(UserBook userBook) {
        userBook.getBook().getUsers().remove(userBook);
        getBooks().remove(userBook);
        userBook.setBook(null);
        userBook.setUser(null);
//
//        for(UserBook userBook : ) {
//            if(userBook.getBook().equals(book) && userBook.getUser().equals(this)) {
//
//            }
//        }
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username +
                "\'}";
    }

    // TODO: override hashCode and equals?
}
