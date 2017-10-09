package code.challenge.library.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_book")
public class UserBook implements Serializable {

    @Embeddable
    public static class UserBookId implements Serializable {

        @Column(name = "book_id")
        private Long bookId;

        @Column(name = "username")
        private String username;

        private UserBookId() {
        }

        public UserBookId(Long bookId, String username) {
            this.bookId = bookId;
            this.username = username;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            UserBookId that = (UserBookId) obj;
            return Objects.equals(bookId, that.bookId) &&
                    Objects.equals(username, that.username);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bookId, username);
        }
    }

    @EmbeddedId
    private UserBookId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("username")
    private User user;

    @Column(name = "status")
    private UserBookStatus status;

    public UserBook() {
    }

    public UserBook(User user, Book book) {
        this.book = book;
        this.user = user;
        this.id = new UserBookId(book.getId(), user.getUsername());
        this.status = UserBookStatus.UNREAD;
    }

    public UserBookId getUserBookId() {
        return id;
    }

    public void setUserBookId(UserBookId id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserBookStatus getStatus() {
        return status;
    }

    public void setStatus(UserBookStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserBook userBook = (UserBook) obj;

        return id.equals(userBook.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
