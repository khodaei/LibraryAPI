package code.challenge.library.service;

import code.challenge.library.domain.Book;

public interface BookService {

    Iterable<Book> list();

    Book create(Book book);

    Book retrieve(long id);

    Book update(long id, Book book);

    void delete(long id);

}
