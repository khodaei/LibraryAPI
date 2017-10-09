package code.challenge.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;

    @JsonIgnore
    @OneToMany(mappedBy = "book")// , cascade = CascadeType.ALL, orphanRemoval = true) //, fetch = FetchType.EAGER)
    private Set<UserBook> users = new HashSet<>();

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<UserBook> getUsers() {
        return users;
    }

    public void setUsers(Set<UserBook> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    // TODO: override equals() and hashCode() ?
}
